package com.app.dextrous.barbara.callback;


import java.io.File;

public interface SoundRecorderCallback {
    public void onRecordingCompleted(File recordedFileName, boolean shouldExecuteTransactionImmediately);
}
