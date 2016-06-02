package com.app.dextrous.barbara.callback;


import com.app.dextrous.barbara.model.CommandResponse;

import java.io.File;

public interface SoundRecorderCallback {
    public void onRecordingCompleted(File recordedFileName, CommandResponse commandResponse);
}
