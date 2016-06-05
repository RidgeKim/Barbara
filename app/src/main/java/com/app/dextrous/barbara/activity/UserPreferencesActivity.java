package com.app.dextrous.barbara.activity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.app.dextrous.barbara.R;
import com.app.dextrous.barbara.callback.UserPreferencesCallback;
import com.app.dextrous.barbara.constant.BarbaraConstants;
import com.app.dextrous.barbara.constant.BarbaraConstants.PREFERENCE_KEY;
import com.app.dextrous.barbara.model.User;
import com.app.dextrous.barbara.model.UserPreference;
import com.app.dextrous.barbara.response.GenericBeanResponse;
import com.app.dextrous.barbara.service.BarbaraService;
import com.app.dextrous.barbara.wrapper.RetrofitWrapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;

import static com.app.dextrous.barbara.constant.BarbaraConstants.FIELD_BUDGET;
import static com.app.dextrous.barbara.constant.BarbaraConstants.FIELD_NICK_NAME;
import static com.app.dextrous.barbara.constant.BarbaraConstants.FIELD_SECURITY_QUESTION;
import static com.app.dextrous.barbara.constant.BarbaraConstants.FIELD_USER_ID;
import static com.app.dextrous.barbara.constant.BarbaraConstants.INTENT_PARAM_USER_PREFERENCE_ITEM_ID_KEY;
import static com.app.dextrous.barbara.constant.BarbaraConstants.INTENT_PARAM_USER_PREFERENCE_ITEM_KEY;
import static com.app.dextrous.barbara.constant.BarbaraConstants.MSG_PREFERENCES_SAVED;
import static com.app.dextrous.barbara.constant.BarbaraConstants.MSG_SPEECH_RECOGNITION_NOT_SUPPORTED;
import static com.app.dextrous.barbara.constant.BarbaraConstants.REQUEST_RESULT_SPEECH;
import static com.app.dextrous.barbara.constant.BarbaraConstants.SPEECH_TO_TEXT_LOCALE_INDIA;
import static com.app.dextrous.barbara.constant.BarbaraConstants.STRING_BLANK;


public class UserPreferencesActivity extends AppCompatActivity {

    private Integer currentIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_preferences);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Object temp = getIntent().getSerializableExtra(INTENT_PARAM_USER_PREFERENCE_ITEM_KEY);
        if (temp != null
                && temp instanceof UserPreference) {
            final Button nextButton = (Button) findViewById(R.id.nextButton);
            final Button previousButton = (Button) findViewById(R.id.previousButton);
            final ImageButton speakButton = (ImageButton) findViewById(R.id.speakButton);
            final EditText preferenceValue = (EditText) findViewById(R.id.newPreferenceValueText);
            final TextView preferenceQuestion = (TextView) findViewById(R.id.preferenceQuestionLabel);
            final UserPreference userPreference = (UserPreference) temp;
            final Map<PREFERENCE_KEY, Object> preferencesMap = getPreferencesAsMap(userPreference);
            enableOrDisableButtons(nextButton, previousButton);
            loadQuestion(preferenceQuestion, preferenceValue, preferencesMap, currentIndex);
            if (nextButton != null
                    && previousButton != null
                    && speakButton != null
                    && preferenceValue != null) {
                nextButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String preferenceString = preferenceValue.getText().toString();
                        if (currentIndex == 2) {
                            // submit the form
                            preferencesMap.put(PREFERENCE_KEY.SECURITY_QUESTION, preferenceString);
                            BarbaraService service = RetrofitWrapper.build(getResources().getString(R.string.base_api_url));
                            Map<String, Object> form = new HashMap<>();
                            form.put(FIELD_USER_ID, userPreference.getUserId());
                            form.put(FIELD_NICK_NAME, preferencesMap.get(PREFERENCE_KEY.NICK_NAME));
                            form.put(FIELD_BUDGET, preferencesMap.get(PREFERENCE_KEY.BUDGET));
                            form.put(FIELD_SECURITY_QUESTION, preferencesMap.get(PREFERENCE_KEY.SECURITY_QUESTION));
                            Call<GenericBeanResponse<UserPreference>> responseCall = service.saveUserPreference(form);
                            responseCall.enqueue(new UserPreferencesCallback(getApplicationContext(), null, null, null, null));
                            Toast.makeText(getApplicationContext(), MSG_PREFERENCES_SAVED, Toast.LENGTH_LONG).show();
                            onBackPressed();
                        } else if (currentIndex < 2) {
                            switch (currentIndex) {
                                case 0:
                                    preferencesMap.put(PREFERENCE_KEY.NICK_NAME, preferenceString);
                                    break;
                                case 1:
                                    preferencesMap.put(PREFERENCE_KEY.BUDGET, preferenceString);
                                    break;
                                default:
                                    break;

                            }
                            currentIndex++;
                            loadQuestion(preferenceQuestion, preferenceValue, preferencesMap, currentIndex);
                        }
                        enableOrDisableButtons(nextButton, previousButton);
                    }
                });
                previousButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (currentIndex > 0) {
                            currentIndex--;
                            loadQuestion(preferenceQuestion, preferenceValue, preferencesMap, currentIndex);
                        }
                        enableOrDisableButtons(nextButton, previousButton);
                    }
                });
                speakButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(
                                RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

                        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, SPEECH_TO_TEXT_LOCALE_INDIA);
                        try {
                            startActivityForResult(intent, REQUEST_RESULT_SPEECH);
                        } catch (ActivityNotFoundException a) {
                            Toast t = Toast.makeText(getApplicationContext(),
                                    MSG_SPEECH_RECOGNITION_NOT_SUPPORTED,
                                    Toast.LENGTH_SHORT);
                            t.show();
                        }
                    }
                });
            }
        } else {
            temp = getIntent().getSerializableExtra(INTENT_PARAM_USER_PREFERENCE_ITEM_ID_KEY);
            if (temp != null) {
                // Create a callback
                // send the input fields
                // execute the service function
                String BASE_URL = getResources().getString(R.string.base_api_url);
                Log.d("BASE_URL", BASE_URL);
                BarbaraService apiService = RetrofitWrapper.build(BASE_URL);

            }
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
                    // send the text to preference field
                    if (!text.isEmpty()) {
                        final EditText preferenceValue = (EditText) findViewById(R.id.newPreferenceValueText);
                        if (preferenceValue != null) {
                            preferenceValue.setText(text.get(0));
                        }
                    }
                }
                break;
            }

        }
    }

    private void loadQuestion(TextView preferenceQuestion,
                              EditText preferenceValue,
                              Map<PREFERENCE_KEY, Object> preferenceKeyMap,
                              Integer currentIndex) {
        String currentQuestion = STRING_BLANK;
        PREFERENCE_KEY preferenceKey = null;
        switch (currentIndex) {
            case 0:
                preferenceKey = PREFERENCE_KEY.NICK_NAME;
                break;
            case 1:
                preferenceKey = PREFERENCE_KEY.BUDGET;
                break;
            case 2:
                preferenceKey = PREFERENCE_KEY.SECURITY_QUESTION;
                break;
        }
        if (preferenceKey != null) {
            currentQuestion = preferenceKey.getQuestion();
            String currentAnswer = STRING_BLANK;
            if(preferenceKeyMap.get(preferenceKey) != null) {
                currentAnswer = preferenceKeyMap.get(preferenceKey).toString();
            }
            preferenceValue.setText(currentAnswer);
        }
        preferenceQuestion.setText(currentQuestion);
    }

    private void enableOrDisableButtons(Button nextButton, Button previousButton) {
        if (currentIndex >= 0) {
            nextButton.setEnabled(true);
        }
        if (currentIndex <= 3) {
            previousButton.setEnabled(true);
        }
        if (currentIndex <= 0) {
            previousButton.setEnabled(false);
        }
        if (currentIndex > 3) {
            nextButton.setEnabled(false);
        }
    }

    private Map<PREFERENCE_KEY, Object> getPreferencesAsMap(UserPreference userPreference) {
        Map<PREFERENCE_KEY, Object> preferenceKeyObjectMap = new HashMap<>();
        preferenceKeyObjectMap.put(PREFERENCE_KEY.NICK_NAME, userPreference.getNickName());
        preferenceKeyObjectMap.put(PREFERENCE_KEY.BUDGET, userPreference.getBudget());
        preferenceKeyObjectMap.put(PREFERENCE_KEY.SECURITY_QUESTION, userPreference.getSecurityQuestion());
        return preferenceKeyObjectMap;
    }
}
