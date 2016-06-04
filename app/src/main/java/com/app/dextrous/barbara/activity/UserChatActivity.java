package com.app.dextrous.barbara.activity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.app.dextrous.barbara.R;
import com.app.dextrous.barbara.adapter.ChatArrayAdapter;
import com.app.dextrous.barbara.callback.SoundRecorderCallback;
import com.app.dextrous.barbara.callback.UserAuthenticationCallback;
import com.app.dextrous.barbara.model.ChatMessage;
import com.app.dextrous.barbara.model.CommandResponse;
import com.app.dextrous.barbara.model.User;
import com.app.dextrous.barbara.response.GenericBeanResponse;
import com.app.dextrous.barbara.service.BarbaraService;
import com.app.dextrous.barbara.task.WavRecordingAsyncTask;
import com.app.dextrous.barbara.util.AndroidUtil;
import com.app.dextrous.barbara.wrapper.RetrofitWrapper;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.app.dextrous.barbara.constant.BarbaraConstants.FIELD_COMMAND_STRING;
import static com.app.dextrous.barbara.constant.BarbaraConstants.FIELD_MULTIPART_FILE_WITH_NAME;
import static com.app.dextrous.barbara.constant.BarbaraConstants.FIELD_USER_ID;
import static com.app.dextrous.barbara.constant.BarbaraConstants.INTENT_PARAM_COMMAND_RESPONSE;
import static com.app.dextrous.barbara.constant.BarbaraConstants.MSG_GENERIC_ERROR;
import static com.app.dextrous.barbara.constant.BarbaraConstants.MSG_SPEECH_RECOGNITION_NOT_SUPPORTED;
import static com.app.dextrous.barbara.constant.BarbaraConstants.REQUEST_PERMISSION_SELF_CHECK;
import static com.app.dextrous.barbara.constant.BarbaraConstants.REQUEST_RESULT_SPEECH;
import static com.app.dextrous.barbara.constant.BarbaraConstants.SPEECH_TO_TEXT_LOCALE_INDIA;
import static com.app.dextrous.barbara.constant.BarbaraConstants.STRING_BLANK;
import static com.app.dextrous.barbara.constant.BarbaraConstants.TAG;
import static com.app.dextrous.barbara.constant.BarbaraConstants.USER_AUTH_OBJECT_PREFERENCE_KEY;
import static com.app.dextrous.barbara.util.AndroidUtil.checkAndRequestNecessaryPermissions;
import static com.app.dextrous.barbara.util.AndroidUtil.getRandomNumber;


public class UserChatActivity extends AppCompatActivity implements Callback<GenericBeanResponse<CommandResponse>>, SoundRecorderCallback {


    private UserChatActivity self = this;
    private ChatArrayAdapter chatArrayAdapter;
    private TextToSpeech textToSpeechInstance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_chat);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initChatAdapter();
        initTextToSpeechInstance();
        Object temp = getIntent().getSerializableExtra(INTENT_PARAM_COMMAND_RESPONSE);
        if (temp != null
                && temp instanceof CommandResponse) {
            CommandResponse response = (CommandResponse) temp;
            speakText(response.getResponseText());
            addChatItem(false, response.getResponseText());
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_RESULT_SPEECH: {
                if (resultCode == RESULT_OK && null != data) {

                    List<String> text = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                    // send the command to server call directly
                    if (!text.isEmpty()) {
                        User authUser = getLoggedInUser();
                        addChatItem(true, text.get(0));
                        processTextCommand(authUser, text.get(0));
                    }
                }
                break;
            }

        }
    }

    @Override
    public void onResponse(Call<GenericBeanResponse<CommandResponse>> call, Response<GenericBeanResponse<CommandResponse>> response) {
        // TODO process the response and add it to the adapter or ask for voice password
        GenericBeanResponse<CommandResponse> commandResponse = response.body();
        if (commandResponse != null
                && commandResponse.getSuccess()) {
            Log.d(TAG, commandResponse.toString());
            CommandResponse statusResponse = commandResponse.getItem();
            if (statusResponse != null) {
                speakText(statusResponse.getResponseText());
                addChatItem(false, statusResponse.getResponseText());
                if (statusResponse.getRequireAuthentication()) {
                    // Record audio and make another network call with the audio
                    WavRecordingAsyncTask asyncTask = new WavRecordingAsyncTask(self, self, statusResponse);
                    System.out.println("Starting the recording task...");
                    System.out.println(new Date().getTime());
                    asyncTask.execute();
                } else {
                    // do other checks to process the transaction
                    if (statusResponse.getIsReminderRequest()) {
                        // schedule a notification with the scheduled response on the particular time frame
                        AndroidUtil.scheduleNotificationForCommandResponse(self, getLoggedInUser(), statusResponse);
                    } else {
                        String scheduledResponseText = statusResponse.getScheduledResponseText();
                        if (scheduledResponseText != null
                                && !STRING_BLANK.equals(scheduledResponseText.trim())) {
                            addChatItem(false, scheduledResponseText);
                        }
                    }

                }
            }
        } else {
            String errorMsg = "Error retrieving response";
            Log.e(TAG, errorMsg);
            Toast.makeText(getApplicationContext(), String.format("%s%s", MSG_GENERIC_ERROR, errorMsg), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onFailure(Call<GenericBeanResponse<CommandResponse>> call, Throwable t) {
        Toast.makeText(getApplicationContext(), String.format("%s%s", MSG_GENERIC_ERROR, t.getMessage()), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRecordingCompleted(File recordedAudioFile,
                                     CommandResponse commandResponse) {
        // get the file and post it to rest call
        // on response of rest call execute or schedule transaction
        String BASE_URL = getResources().getString(R.string.base_api_url);
        final BarbaraService apiService = RetrofitWrapper.build(BASE_URL);
        System.out.println("receiving recorded file..." + recordedAudioFile.getAbsolutePath());
        System.out.println("commandResponse=" + commandResponse);
        System.out.println(new Date().getTime());
        User loggedInUser = getLoggedInUser();
        if (loggedInUser != null) {
            RequestBody audioFilePart = RequestBody.create(MediaType.parse("audio/wav"), recordedAudioFile);
            RequestBody userIdPart = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(loggedInUser.getId()));
            if (commandResponse.getIsScheduleRequest()) {
                // just call the authenticate method and in call back schedule transfer
                Map<String, RequestBody> multipartForm = new HashMap<>();
                multipartForm.put(FIELD_USER_ID, userIdPart);
                multipartForm.put(String.format(FIELD_MULTIPART_FILE_WITH_NAME, recordedAudioFile.getName()), audioFilePart);
                Call<GenericBeanResponse<User>> responseCall = apiService.authenticateUser(multipartForm);
                responseCall.enqueue(new UserAuthenticationCallback(self, commandResponse));
            } else if (commandResponse.getIsTransactionRequest()
                    && !commandResponse.getIsReadRequest()) {
                RequestBody commandStringPart = RequestBody.create(MediaType.parse("text/plain"), commandResponse.getScheduledResponseText());
                Map<String, RequestBody> multipartForm = new HashMap<>();
                multipartForm.put(FIELD_USER_ID, userIdPart);
                multipartForm.put(FIELD_COMMAND_STRING, commandStringPart);
                multipartForm.put(String.format(FIELD_MULTIPART_FILE_WITH_NAME, recordedAudioFile.getName()), audioFilePart);
                Call<GenericBeanResponse<CommandResponse>> responseCall = apiService.authenticateTransfer(multipartForm);
                responseCall.enqueue(self);
            }
        }
    }

    @Override
    protected void onDestroy() {
        if (textToSpeechInstance != null) {
            textToSpeechInstance.stop();
            textToSpeechInstance.shutdown();
            textToSpeechInstance = null;
        }
        super.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION_SELF_CHECK: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(getApplicationContext(), "Permission Granted!!", Toast.LENGTH_LONG).show();

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(getApplicationContext(), "Permission Granted!!", Toast.LENGTH_LONG).show();
                }
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private User getLoggedInUser() {
        return AndroidUtil
                .getPreferenceAsObject(getApplicationContext(),
                        USER_AUTH_OBJECT_PREFERENCE_KEY,
                        User.class);
    }

    private void initTextToSpeechInstance() {
        if (textToSpeechInstance == null) {
            textToSpeechInstance = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                @Override
                public void onInit(int status) {
                    textToSpeechInstance.setLanguage(Locale.UK);
                }
            });
        }
    }

    private void initChatAdapter() {
        final ListView listView = (ListView) findViewById(R.id.msgListView);

        chatArrayAdapter = new ChatArrayAdapter(getApplicationContext(), R.layout.chat_item_right);

        if (listView == null) {
            Log.e(TAG, "Couldn't find the list view for chat page");
            return;
        }
        listView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        listView.setAdapter(chatArrayAdapter);

        //to scroll the list view to bottom on data change
        chatArrayAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                listView.setSelection(chatArrayAdapter.getCount() - 1);
            }
        });
        final ImageButton speakButton = (ImageButton) findViewById(R.id.speakButton);
        final ImageButton sendButton = (ImageButton) findViewById(R.id.sendButton);
        final EditText msgContent = (EditText) findViewById(R.id.msgContent);
        if (speakButton != null) {
            speakButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(
                            RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

                    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, SPEECH_TO_TEXT_LOCALE_INDIA);
                    try {
                        startActivityForResult(intent, REQUEST_RESULT_SPEECH);
                        if (msgContent != null) {
                            msgContent.setText(STRING_BLANK);
                        }
                    } catch (ActivityNotFoundException a) {
                        Toast t = Toast.makeText(getApplicationContext(),
                                MSG_SPEECH_RECOGNITION_NOT_SUPPORTED,
                                Toast.LENGTH_SHORT);
                        t.show();
                    }
                }
            });
        }
        if (sendButton != null) {
            sendButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (AndroidUtil.isFileReadWritePermissionEnabled(self)
                            && AndroidUtil.isMicrophonePermissionEnabled(self)
                            && AndroidUtil.isContactsPermissionEnabled(self)) {
                        if (msgContent != null
                                && msgContent.getText() != null) {
                            String commandText = msgContent.getText().toString();
                            User authUser = getLoggedInUser();
                            processTextCommand(authUser, commandText);
                            addChatItem(true, commandText);
                            msgContent.setText(STRING_BLANK);
                        }
                    } else {
                        // request permissions
                        checkAndRequestNecessaryPermissions(self);
                    }
                }
            });
        }
    }

    private void addChatItem(boolean isTextOriginSelf, String text) {
        if (chatArrayAdapter != null) {
            chatArrayAdapter.add(new ChatMessage(isTextOriginSelf, text));
        }
    }

    private void speakText(String text) {
        textToSpeechInstance.speak(text, TextToSpeech.QUEUE_FLUSH, null, String.valueOf(getRandomNumber()));
    }

    private void processTextCommand(User authUser, String textCommand) {
        if (authUser != null
                && textCommand != null
                && !STRING_BLANK.equals(textCommand.trim())) {
            String BASE_URL = getResources().getString(R.string.base_api_url);
            final BarbaraService apiService = RetrofitWrapper.build(BASE_URL);
            System.out.println("in process command");
            textCommand = AndroidUtil.replaceRelationshipWordsWithNames(self, textCommand);
            Map<String, Object> form = new HashMap<>();
            form.put(FIELD_USER_ID, authUser.getId());
            form.put(FIELD_COMMAND_STRING, textCommand);
            Call<GenericBeanResponse<CommandResponse>> responseCall = apiService.processCommand(form);
            responseCall.enqueue(this); // this <-- activity implemented the command processing callbacks
        }
    }

}
