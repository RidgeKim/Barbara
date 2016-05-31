package com.app.dextrous.barbara.task;

import android.os.AsyncTask;

import com.app.dextrous.barbara.callback.SoundRecorderCallback;

public class WavRecordingAsyncTask extends AsyncTask<Void, Void, Void> {

    private boolean shouldExecuteTransactionImmediately;
    private SoundRecorderCallback soundRecorderCallback;

    public WavRecordingAsyncTask(boolean shouldExecuteTransactionImmediately,
                                 SoundRecorderCallback soundRecorderCallback) {
        this.shouldExecuteTransactionImmediately = shouldExecuteTransactionImmediately;
        this.soundRecorderCallback = soundRecorderCallback;
    }

    @Override
    protected Void doInBackground(Void... params) {
        return null;
    }
}
