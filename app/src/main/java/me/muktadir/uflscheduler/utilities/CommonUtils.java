package me.muktadir.uflscheduler.utilities;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by ZOARDER AL MUKTADIR on 11/29/2016.
 */

public class CommonUtils {
    public static void showErrorDialog(Activity activity, String dialogTitle, String dialogMessage, String buttonTitle) {
        final SweetAlertDialog sDialog = new SweetAlertDialog(activity, SweetAlertDialog.ERROR_TYPE);
        sDialog.setTitleText(dialogTitle);
        sDialog.setContentText(dialogMessage);
        sDialog.setConfirmText(buttonTitle);
        sDialog.setCancelable(false);
        sDialog.setCanceledOnTouchOutside(false);
        sDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            public void onClick(SweetAlertDialog mDialog) {
                mDialog.dismiss();
            }
        });
        sDialog.show();
    }

    public static void requestFocus(Activity activity, View view) {
        if (view.requestFocus()) {
            activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    public static void hideSoftInputMode(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

    }
}
