package com.preraktrivedi.android.instagrampopular.helpers;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.widget.Button;

import com.preraktrivedi.android.instagrampopular.R;

public class GenericAlertDialogBuilder {

    public interface AlertDialogCallbackInterface {
        void onPositiveButtonClicked();
        void onNegativeButtonClicked();
    }

    public enum AlertDialogUsecase {
        REQUEST_INTERNET(null, R.string.error_network_unavailable, R.string.common_retry, R.string.common_bt_cancel),
        RUNTIME_PERMISSION_RATIONALE(R.string.rp_dialog_title, null, R.string.rp_allow,R.string.rp_yes_sure),
        RUNTIME_PERMISSION_SETTINGS_DIALOG(R.string.rp_settings_dialog_title, null, R.string.rp_settings,R.string.common_bt_cancel),
        GENERIC_API_ERROR_HANDLE_DIALOG(null,null,R.string.common_bt_ok, null);

        private Integer title;
        private Integer message;
        private Integer positiveButton;
        private Integer negativeButton;

        AlertDialogUsecase(Integer title, Integer message, Integer positiveButton, Integer negativeButton) {
            this.title = title;
            this.message = message;
            this.positiveButton = positiveButton;
            this.negativeButton = negativeButton;
        }

        public Integer getTitleTxt() {
            return title;
        }
        public Integer getMessageTxt() {
            return message;
        }
        public Integer getPositiveButtonTxt() {
            return positiveButton;
        }
        public Integer getNegativeButtonTxt() {
            return negativeButton;
        }
    }

    public static AlertDialog getGenericAlertDialog(final AlertDialogUsecase usecase, final Context context){

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context, R.style.AppCompatAlertDialogStyle);

        /** set generic properties for the alert dialog and modify it later **/
        alertDialogBuilder.setCancelable(false);

        final AlertDialog alertDialog = alertDialogBuilder.create();

        Resources res = context.getResources();

        if(usecase.getTitleTxt() != null){
            alertDialog.setTitle(res.getString(usecase.getTitleTxt()));
        }

        if(usecase.getMessageTxt() != null){
            alertDialog.setMessage(res.getString(usecase.getMessageTxt()));
        }

        if(usecase.getPositiveButtonTxt() != null){
            setPositiveButtonText(res.getString(usecase.getPositiveButtonTxt()), alertDialog);
            styleButton(alertDialog.getButton(DialogInterface.BUTTON_POSITIVE),
                    ContextCompat.getColor(context, R.color.default_hyperlink_color));
        }

        if(usecase.getNegativeButtonTxt() != null){
            setNegativeButtonText(res.getString(usecase.getNegativeButtonTxt()), alertDialog);
            styleButton(alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE),
                    ContextCompat.getColor(context, R.color.default_hyperlink_color));
        }

        return alertDialog;
    }

    private static void styleButton(Button button, int colorResource) {
        if (button != null) {
            button.setBackgroundColor(colorResource);
        }
    }

    private static DialogInterface.OnClickListener getEmptyOnClickListener(final AlertDialog alertDialog) {
        return (dialog, which) -> alertDialog.dismiss();
    }

    private static void setPositiveButtonText(String text, AlertDialog alertDialog) {
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, text,
                getEmptyOnClickListener(alertDialog));
    }

    private static void setNegativeButtonText(String text, AlertDialog alertDialog) {
        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, text,
                getEmptyOnClickListener(alertDialog));
    }

    public static void showErrorWithMessage(final Activity activity, String msg,
                                            final boolean shouldFinishActivity) {
        if (activity != null && !activity.isFinishing()) {
            final AlertDialog alertDialog = getGenericAlertDialog(
                    AlertDialogUsecase.GENERIC_API_ERROR_HANDLE_DIALOG, activity);

            alertDialog.setMessage(msg);

            alertDialog.setOnShowListener(dialog -> {
                Button positiveButon = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                if (positiveButon != null) {
                    positiveButon.setOnClickListener(view -> {
                        alertDialog.dismiss();
                        if (shouldFinishActivity) {
                            activity.finish();
                        }
                    });
                }
            });
            alertDialog.show();
        }
    }

    //Useful when the content of alert dialog is static
    public static void showAlertDialogWithCallback(final Context ctx, AlertDialogUsecase usecase,
                                                   final AlertDialogCallbackInterface callback) {
        if (ctx != null) {
            final AlertDialog alertDialog = getGenericAlertDialog(usecase, ctx);

            alertDialog.setOnShowListener(dialog -> {
                Button positiveButon = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                if (positiveButon != null) {
                    positiveButon.setOnClickListener(view -> {
                        alertDialog.dismiss();
                        callback.onPositiveButtonClicked();
                    });
                }

                Button negativeButton = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                if (negativeButton != null) {
                    negativeButton.setOnClickListener(view -> {
                        alertDialog.dismiss();
                        callback.onNegativeButtonClicked();
                    });
                }
            });

            alertDialog.show();
        }
    }

}