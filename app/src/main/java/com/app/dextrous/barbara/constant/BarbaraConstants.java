package com.app.dextrous.barbara.constant;


import android.media.AudioFormat;

public class BarbaraConstants {
    public static final String SERVER_DATE_FORMAT = "EEE, dd MMM yyyy HH:mm:ss Z";
    public static final String LOCAL_SERVER_URL = "http://10.0.2.2:8090/api/";

    public static final String APP_SHARED_PREFERENCE_KEY = "BarbaraAppAuthPreferences";
    public static final String USER_AUTH_OBJECT_PREFERENCE_KEY = "userAuthKey";

    public static final String STRING_SETTING_LINK_FOR_ALERT = "Settings";
    public static final String STRING_CANCEL_LINK_FOR_ALERT = "Cancel";
    public static final String STRING_BLANK = "";

    public static final String MSG_PROGRESS_DIALOG_DEFAULT_TITLE = "Loading...";
    public static final String MSG_PROGRESS_DIALOG_DEFAULT_MESSAGE = "Please wait..";
    public static final String MSG_PROGRESS_DIALOG_RECORDER_TITLE = "Authenticating...";
    public static final String MSG_PROGRESS_DIALOG_RECORDER_MESSAGE = "Recording your voice...";

    public static final String INTENT_PARAM_USER_ITEM_KEY = "userDetailItemKey";
    public static final String INTENT_PARAM_USER_ITEM_ID_KEY = "userDetailItemIdKey";
    public static final String INTENT_PARAM_TRANSACTION_ITEM_KEY = "transactionDetailItemKey";
    public static final String INTENT_PARAM_TRANSACTION_ITEM_ID_KEY = "transactionDetailItemIdKey";

    public static final String FIELD_USERNAME = "username";
    public static final String FIELD_PASSWORD = "password";
    public static final String FIELD_USER_ID = "userId";
    public static final String FIELD_COMMAND_STRING = "command";
    public static final String FIELD_MULTIPART_FILE_WITH_NAME = "file\"; filename=\"%s\"";

    public static final String MSG_GENERIC_ERROR = "Something went wrong!! Try after sometime!! \n Error:";
    public static final String MSG_SPEECH_RECOGNITION_NOT_SUPPORTED = "Oops! Your device doesn't support Speech to Text";

    public static final String SPEECH_TO_TEXT_LOCALE_INDIA = "en-IN";

    public static final String INTENT_PARAM_COMMAND_RESPONSE = "commandResponseFromService";
    public static final String INTENT_PARAM_SCHEDULED_RESPONSE_TEXT = "commandResponse.ScheduledResponseText";
    public static final String INTENT_PARAM_IS_TRANSACTION_REQUEST = "commandResponse.IsTransactionRequest";

    public static final String DELIMITER_SPACE = " ";
    public static final String DELIMITER_SLASH = "/";

    public static final int RANDOM_NUMBER_PRECESSION = 9999;


    public static final String NOTIFICATION_TITLE = "Barbara here!";
    public static final int REQUEST_CODE_CHAT_ACTIVITY = 191919;
    public static final int REQUEST_PERMISSION_SELF_CHECK = 1191119;
    public static final int REQUEST_RESULT_SPEECH = 999;


    public static final String TAG = "BarbaraBankingMobileApp";

    // Audio recorder values
    public static final int RECORDER_BPP = 16;
    public static final String AUDIO_RECORDER_FILE_EXT_WAV = ".wav";
    public static final String AUDIO_RECORDER_FOLDER = "Barbara";
    public static final String AUDIO_RECORDER_TEMP_FILE = "record_temp.raw";
    public static final int RECORDER_SAMPLE_RATE = 16000;
    public static final int RECORDER_CHANNELS = AudioFormat.CHANNEL_IN_MONO;
    public static final int RECORDER_AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT;
    public static final int MAX_RECORDING_TIME = 1000; // --> 7-8 Seconds
    public static final int MIN_WAITING_TIME = 3000; // --> 2 Seconds

}
