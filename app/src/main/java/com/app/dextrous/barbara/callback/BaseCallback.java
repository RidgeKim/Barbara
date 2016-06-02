package com.app.dextrous.barbara.callback;

import android.app.ProgressDialog;
import android.content.Context;

import com.app.dextrous.barbara.util.AndroidUtil;

import static com.app.dextrous.barbara.constant.BarbaraConstants.MSG_PROGRESS_DIALOG_DEFAULT_MESSAGE;
import static com.app.dextrous.barbara.constant.BarbaraConstants.MSG_PROGRESS_DIALOG_DEFAULT_TITLE;

public class BaseCallback {

    protected Context context;
    protected ProgressDialog progressDialog;

    public BaseCallback(Context context) {
        this.context = context;
       if(this.context != null) {
           this.progressDialog = AndroidUtil.showProgressDialog(context,
                   MSG_PROGRESS_DIALOG_DEFAULT_TITLE,
                   MSG_PROGRESS_DIALOG_DEFAULT_MESSAGE);
       }
    }
    protected void hideDialog() {
        if(this.progressDialog != null){
            this.progressDialog.hide();
        }
    }
}
