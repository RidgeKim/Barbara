package com.app.dextrous.barbara.task;

import android.app.ProgressDialog;
import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.app.dextrous.barbara.callback.SoundRecorderCallback;
import com.app.dextrous.barbara.model.CommandResponse;
import com.app.dextrous.barbara.util.AndroidUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static com.app.dextrous.barbara.constant.BarbaraConstants.AUDIO_RECORDER_FILE_EXT_WAV;
import static com.app.dextrous.barbara.constant.BarbaraConstants.AUDIO_RECORDER_FOLDER;
import static com.app.dextrous.barbara.constant.BarbaraConstants.AUDIO_RECORDER_TEMP_FILE;
import static com.app.dextrous.barbara.constant.BarbaraConstants.DELIMITER_SLASH;
import static com.app.dextrous.barbara.constant.BarbaraConstants.MAX_RECORDING_TIME;
import static com.app.dextrous.barbara.constant.BarbaraConstants.MIN_WAITING_TIME;
import static com.app.dextrous.barbara.constant.BarbaraConstants.MSG_PROGRESS_DIALOG_RECORDER_MESSAGE;
import static com.app.dextrous.barbara.constant.BarbaraConstants.MSG_PROGRESS_DIALOG_RECORDER_TITLE;
import static com.app.dextrous.barbara.constant.BarbaraConstants.RECORDER_AUDIO_ENCODING;
import static com.app.dextrous.barbara.constant.BarbaraConstants.RECORDER_BPP;
import static com.app.dextrous.barbara.constant.BarbaraConstants.RECORDER_CHANNELS;
import static com.app.dextrous.barbara.constant.BarbaraConstants.RECORDER_SAMPLE_RATE;

public class WavRecordingAsyncTask extends AsyncTask<Void, Void, Void> {

    private SoundRecorderCallback soundRecorderCallback;
    private CommandResponse commandResponse;
    private ProgressDialog progressDialog;


    private AudioRecord recorder = null;
    private int bufferSize = 0;

    public WavRecordingAsyncTask(SoundRecorderCallback soundRecorderCallback,
                                 Context context, CommandResponse commandResponse) {
        this.soundRecorderCallback = soundRecorderCallback;
        this.commandResponse = commandResponse;

        bufferSize = AudioRecord.getMinBufferSize(RECORDER_SAMPLE_RATE,
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT);

        progressDialog = AndroidUtil.showProgressDialog(context,
                MSG_PROGRESS_DIALOG_RECORDER_TITLE,
                MSG_PROGRESS_DIALOG_RECORDER_MESSAGE);
    }

    @Override
    protected Void doInBackground(Void... params) {
        long timeInMillis = MIN_WAITING_TIME;
        while(timeInMillis >0) {
            timeInMillis--;
            // do nothing to wait
        }
        recorder = new AudioRecord(MediaRecorder.AudioSource.MIC,
                RECORDER_SAMPLE_RATE,
                RECORDER_CHANNELS,
                RECORDER_AUDIO_ENCODING,
                bufferSize);

        int i = recorder.getState();
        if (i == 1)
            recorder.startRecording();
        writeAudioDataToFile();
        stopRecording();
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        if (progressDialog != null) {
            progressDialog.hide();
        }
    }

    private String getTempFilename() {
        String filepath = Environment.getExternalStorageDirectory().getPath();
        File file = new File(filepath, AUDIO_RECORDER_FOLDER);

        if (!file.exists()) {
            file.mkdirs();
        }
        return String.format("%s%s%s", file.getAbsolutePath(),
                DELIMITER_SLASH,
                AUDIO_RECORDER_TEMP_FILE);
    }

    private String getFilename() {
        String filepath = Environment.getExternalStorageDirectory().getPath();
        File file = new File(filepath, AUDIO_RECORDER_FOLDER);

        if (!file.exists()) {
            file.mkdirs();
        }

        return String.format("%s%s%d%s", file.getAbsolutePath(),
                DELIMITER_SLASH, System.currentTimeMillis(),
                AUDIO_RECORDER_FILE_EXT_WAV);
    }

    synchronized
    private void stopRecording() {
        if (null != recorder) {
            int i = recorder.getState();
            if (i == 1)
                recorder.stop();
            recorder.release();

            recorder = null;
        }
        copyWaveFile(getTempFilename(), getFilename());
        deleteTempFile();

    }

    synchronized
    private void writeAudioDataToFile() {
        byte data[] = new byte[bufferSize];
        String filename = getTempFilename();
        FileOutputStream os = null;

        try {
            os = new FileOutputStream(filename);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        int read;
        int i = 0;
        if (null != os) {
            while (i < MAX_RECORDING_TIME) {
                read = recorder.read(data, 0, bufferSize);

                if (AudioRecord.ERROR_INVALID_OPERATION != read) {
                    try {
                        os.write(data);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                i++;
            }

            try {
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void deleteTempFile() {
        File file = new File(getTempFilename());
        file.delete();
    }

    private void copyWaveFile(String inFilename, String outFilename) {

        try {
            long longSampleRate = RECORDER_SAMPLE_RATE;
            int channels = 1;
            long byteRate = RECORDER_BPP * RECORDER_SAMPLE_RATE * channels / 8;

            byte[] data = new byte[bufferSize];
            FileInputStream in = new FileInputStream(inFilename);
            FileOutputStream out = new FileOutputStream(outFilename);

            long totalAudioLen = in.getChannel().size();
            long totalDataLen = totalAudioLen + 36;

            Log.i("RecordingAudio", "File size: " + totalDataLen);

            WriteWaveFileHeader(out, totalAudioLen, totalDataLen,
                    longSampleRate, channels, byteRate);

            while (in.read(data) != -1) {
                out.write(data);
            }

            in.close();
            out.close();
            File wavFile = new File(outFilename);
            this.soundRecorderCallback.onRecordingCompleted(wavFile, this.commandResponse);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void WriteWaveFileHeader(
            FileOutputStream out, long totalAudioLen,
            long totalDataLen, long longSampleRate, int channels,
            long byteRate) throws IOException {

        byte[] header = new byte[44];

        header[0] = 'R'; // RIFF/WAVE header
        header[1] = 'I';
        header[2] = 'F';
        header[3] = 'F';
        header[4] = (byte) (totalDataLen & 0xff);
        header[5] = (byte) ((totalDataLen >> 8) & 0xff);
        header[6] = (byte) ((totalDataLen >> 16) & 0xff);
        header[7] = (byte) ((totalDataLen >> 24) & 0xff);
        header[8] = 'W';
        header[9] = 'A';
        header[10] = 'V';
        header[11] = 'E';
        header[12] = 'f'; // 'fmt ' chunk
        header[13] = 'm';
        header[14] = 't';
        header[15] = ' ';
        header[16] = 16; // 4 bytes: size of 'fmt ' chunk
        header[17] = 0;
        header[18] = 0;
        header[19] = 0;
        header[20] = 1; // format = 1
        header[21] = 0;
        header[22] = (byte) channels;
        header[23] = 0;
        header[24] = (byte) (longSampleRate & 0xff);
        header[25] = (byte) ((longSampleRate >> 8) & 0xff);
        header[26] = (byte) ((longSampleRate >> 16) & 0xff);
        header[27] = (byte) ((longSampleRate >> 24) & 0xff);
        header[28] = (byte) (byteRate & 0xff);
        header[29] = (byte) ((byteRate >> 8) & 0xff);
        header[30] = (byte) ((byteRate >> 16) & 0xff);
        header[31] = (byte) ((byteRate >> 24) & 0xff);
        header[32] = (byte) (2 * 16 / 8); // block align
        header[33] = 0;
        header[34] = RECORDER_BPP; // bits per sample
        header[35] = 0;
        header[36] = 'd';
        header[37] = 'a';
        header[38] = 't';
        header[39] = 'a';
        header[40] = (byte) (totalAudioLen & 0xff);
        header[41] = (byte) ((totalAudioLen >> 8) & 0xff);
        header[42] = (byte) ((totalAudioLen >> 16) & 0xff);
        header[43] = (byte) ((totalAudioLen >> 24) & 0xff);

        out.write(header, 0, 44);
    }
}
