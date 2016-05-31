package com.app.dextrous.barbara.activity;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.app.dextrous.barbara.R;
import com.app.dextrous.barbara.adapter.ChatArrayAdapter;
import com.app.dextrous.barbara.callback.CreditTransactionListCallback;
import com.app.dextrous.barbara.callback.LoginResponseCallback;
import com.app.dextrous.barbara.callback.SoundRecorderCallback;
import com.app.dextrous.barbara.callback.TransactionListCallback;
import com.app.dextrous.barbara.callback.UserListCallback;
import com.app.dextrous.barbara.model.ChatMessage;
import com.app.dextrous.barbara.model.CommandResponse;
import com.app.dextrous.barbara.model.CreditTransaction;
import com.app.dextrous.barbara.model.Transaction;
import com.app.dextrous.barbara.model.User;
import com.app.dextrous.barbara.response.GenericBeanResponse;
import com.app.dextrous.barbara.response.GenericListResponse;
import com.app.dextrous.barbara.service.BarbaraService;
import com.app.dextrous.barbara.util.AndroidUtil;
import com.app.dextrous.barbara.wrapper.RetrofitWrapper;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.app.dextrous.barbara.constant.BarbaraConstants.FIELD_COMMAND_STRING;
import static com.app.dextrous.barbara.constant.BarbaraConstants.FIELD_PASSWORD;
import static com.app.dextrous.barbara.constant.BarbaraConstants.FIELD_USERNAME;
import static com.app.dextrous.barbara.constant.BarbaraConstants.FIELD_USER_ID;
import static com.app.dextrous.barbara.constant.BarbaraConstants.MSG_GENERIC_ERROR;
import static com.app.dextrous.barbara.constant.BarbaraConstants.MSG_SPEECH_RECOGNITION_NOT_SUPPORTED;
import static com.app.dextrous.barbara.constant.BarbaraConstants.SPEECH_TO_TEXT_LOCALE_INDIA;
import static com.app.dextrous.barbara.constant.BarbaraConstants.STRING_BLANK;
import static com.app.dextrous.barbara.constant.BarbaraConstants.USER_AUTH_OBJECT_PREFERENCE_KEY;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, Callback<GenericBeanResponse<CommandResponse>>, SoundRecorderCallback {

    private static final int RESULT_SPEECH = 999;
    final String TAG = "BarbaraAppActivity";
    private HomeActivity self = this;
    private ChatArrayAdapter chatArrayAdapter;
    private TextToSpeech textToSpeechInstance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.chat);
        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            });
        }
        initTextToSpeechInstance();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        if (drawer != null) {
            drawer.setDrawerListener(toggle);
        }
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            navigationView.setNavigationItemSelectedListener(this);
        }
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

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer != null) {
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                super.onBackPressed();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    public void initLoginView(final int screenToShowNext) {
        final ViewFlipper flipper = (ViewFlipper) findViewById(R.id.navigation_flip_view);
        String BASE_URL = getResources().getString(R.string.base_api_url);
        final BarbaraService apiService = RetrofitWrapper.build(BASE_URL);
        Button loginButton = (Button) findViewById(R.id.loginButton);
        final TextView usernameText = (TextView) findViewById(R.id.usernameText);
        final TextView passwordText = (TextView) findViewById(R.id.passwordText);
        if (loginButton != null
                && usernameText != null
                && passwordText != null) {
            loginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Login and set preference
                    Map<String, Object> form = new HashMap<String, Object>();
                    form.put(FIELD_USERNAME, usernameText.getText().toString());
                    form.put(FIELD_PASSWORD, passwordText.getText().toString());
                    Call<GenericBeanResponse<User>> loginCall = apiService.loginUser(form);
                    Log.d(TAG, form.toString());
                    loginCall.enqueue(new LoginResponseCallback(self, flipper, screenToShowNext));
                }
            });
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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
                        startActivityForResult(intent, RESULT_SPEECH);
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
                    if (msgContent != null
                            && msgContent.getText() != null) {
                        String commandText = msgContent.getText().toString();
                        User authUser = getLoggedInUser();
                        if (!STRING_BLANK.equals(commandText.trim())
                                && authUser!= null) {
                            String BASE_URL = getResources().getString(R.string.base_api_url);
                            final BarbaraService apiService = RetrofitWrapper.build(BASE_URL);
                            Map<String, Object> form = new HashMap<>();
                            addChatItem(true, commandText);
                            form.put(FIELD_USER_ID, authUser.getId());
                            form.put(FIELD_COMMAND_STRING, commandText.trim());
                            Call<GenericBeanResponse<CommandResponse>> responseCall = apiService.processCommand(form);
                            responseCall.enqueue(self);
                        }
                    }
                }
            });
        }
    }

    private void addChatItem(boolean isTextOriginSelf, String text) {
        if(chatArrayAdapter != null) {
            chatArrayAdapter.add(new ChatMessage(isTextOriginSelf, text));
        }
    }

    private void speakText(String text) {
        textToSpeechInstance.speak(text, TextToSpeech.QUEUE_FLUSH, null, String.valueOf(Math.random() * 9999));
    }

    private void processTextCommand(User authUser, String textCommand) {
        if (authUser != null
                && textCommand != null
                && !STRING_BLANK.equals(textCommand.trim())) {
            String BASE_URL = getResources().getString(R.string.base_api_url);
            final BarbaraService apiService = RetrofitWrapper.build(BASE_URL);
            Map<String, Object> form = new HashMap<>();
            form.put(FIELD_USER_ID, authUser.getId());
            form.put(FIELD_COMMAND_STRING, textCommand);
            Call<GenericBeanResponse<CommandResponse>> responseCall = apiService.processCommand(form);
            responseCall.enqueue(this); // this <-- activity implemented the command processing callbacks
        }
    }
    
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        final ViewFlipper flipper = (ViewFlipper) findViewById(R.id.navigation_flip_view);


        String BASE_URL = getResources().getString(R.string.base_api_url);
        final BarbaraService apiService = RetrofitWrapper.build(BASE_URL);

        Integer screenToShowNext = null;
        String userAuthKey = AndroidUtil.getStringPreferenceValue(getApplicationContext(), USER_AUTH_OBJECT_PREFERENCE_KEY);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        if (flipper != null && drawer != null) {
            if (STRING_BLANK.equals(userAuthKey)) {
                switch (id) {
                    case R.id.nav_transactions:
                        screenToShowNext = 2;
                    case R.id.nav_credit_transactions:
                        if (screenToShowNext == null) {
                            screenToShowNext = 3;
                        }
                    case R.id.nav_chat:
                        if (screenToShowNext == null) {
                            screenToShowNext = 4;
                        }
                        // Redirect to login page
                        flipper.setDisplayedChild(5);
                        initLoginView(screenToShowNext);
                        // load login screen and quit
                        drawer.closeDrawer(GravityCompat.START);
                        return true;
                    case R.id.nav_about:
                    case R.id.nav_users:
                    case R.id.nav_investments:
                        // ignore the non auth screens
                        break;
                }
            }
            if (id == R.id.nav_users) {
                flipper.setDisplayedChild(1);
                // API call
                Call<GenericListResponse<User>> userCall = apiService.getAllUsers();
                userCall.enqueue(new UserListCallback(self, (ListView) findViewById(R.id.userListView)));
            } else if (id == R.id.nav_transactions) {
                flipper.setDisplayedChild(2);
                User userAuth = getLoggedInUser();
                if (userAuth != null) {
                    Call<GenericListResponse<Transaction>> responseCall = apiService.getAllUserTransactions(String.valueOf(userAuth.getId()));
                    responseCall.enqueue(new TransactionListCallback(self, (ListView) findViewById(R.id.transactionListView)));
                }
            } else if (id == R.id.nav_credit_transactions) {
                flipper.setDisplayedChild(3);
                User userAuth = getLoggedInUser();
                if (userAuth != null) {
                    Call<GenericListResponse<CreditTransaction>> responseCall = apiService.getAllUserCreditTransactions(String.valueOf(userAuth.getId()));
                    responseCall.enqueue(new CreditTransactionListCallback(self, (ListView) findViewById(R.id.creditListView)));
                }
            } else if (id == R.id.nav_chat) {
                flipper.setDisplayedChild(4);
                initChatAdapter();
            } else if (id == R.id.nav_about) {

            } else if (id == R.id.nav_investments) {

            }
            drawer.closeDrawer(GravityCompat.START);
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case RESULT_SPEECH: {
                if (resultCode == RESULT_OK && null != data) {

                    List<String> text = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                    // send the command to server call directly
                    if (!text.isEmpty()) {
                        User authUser = getLoggedInUser();
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
                    if (statusResponse.getIsScheduleRequest()) {

                    } else if (statusResponse.getIsTransactionRequest()) {

                    }
                } else {
                    // do other checks to process the transaction
                    if (statusResponse.getIsReminderRequest()) {
                        // schedule a notification with the scheduled response on the particular time frame
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
    public void onRecordingCompleted(File recordedFileName, boolean shouldExecuteTransactionImmediately) {
        // get the file and post it to rest call
        // on response of rest call execute or schedule transaction
    }

    @Override
    protected void onDestroy() {
        if (textToSpeechInstance != null) {
            textToSpeechInstance.stop();
            textToSpeechInstance.shutdown();
        }
        super.onDestroy();
    }

    private User getLoggedInUser() {
        return AndroidUtil
                .getPreferenceAsObject(getApplicationContext(),
                        USER_AUTH_OBJECT_PREFERENCE_KEY,
                        User.class);
    }
}
