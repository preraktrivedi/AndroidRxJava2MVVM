package com.preraktrivedi.android.mvvmbaseapp.helpers;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;

import com.preraktrivedi.android.mvvmbaseapp.R;

import java.util.ArrayList;
import java.util.List;

public class RuntimePermissionHelper {

    public static final int REQUEST_CODE_DEVICE_DETAILS = 101;
    private static final String TAG = RuntimePermissionHelper.class.getSimpleName();

    public enum RuntimePermissionType {

        DEVICE_DETAILS(new String[]{Manifest.permission.READ_PHONE_STATE},
                REQUEST_CODE_DEVICE_DETAILS,
                R.string.rp_rationale_read_phone_state,
                R.string.rp_settings_read_phone_state_title,
                R.string.rp_settings_read_phone_state_msg);

        private String[] permissionsArray;
        private int requestCode, rationaleMsgResource, settingsMsgResource, settingsTitleResource;

        RuntimePermissionType(String[] permissionsArray,
                              int requestCode,
                              int rationaleMsgResource,
                              int settingsTitleResource,
                              int settingsMsgResource) {
            this.permissionsArray = permissionsArray;
            this.requestCode = requestCode;
            this.rationaleMsgResource = rationaleMsgResource;
            this.settingsTitleResource = settingsTitleResource;
            this.settingsMsgResource = settingsMsgResource;
        }

        public String[] getPermissionsArray() {
            return permissionsArray;
        }

        public int getRequestCode() {
            return requestCode;
        }

        public int getRationaleMsgResource() {
            return rationaleMsgResource;
        }

        public int getSettingsMsgResource() {
            return settingsMsgResource;
        }

        public int getSettingsTitleResource() {
            return settingsTitleResource;
        }
    }

    public static boolean areNecessaryPermissionsGranted(Context ctx,
                                                         RuntimePermissionHelper.RuntimePermissionType permissionToCheck) {
        return TextUtils.isEmpty(getFirstMissingPermission(ctx,permissionToCheck));
    }

    public static String getFirstMissingPermission(Context ctx,
                                                   RuntimePermissionHelper.RuntimePermissionType permissionToCheck) {
        for (String permission : permissionToCheck.getPermissionsArray()) {
            if (!(ContextCompat.checkSelfPermission(ctx, permission) == PackageManager.PERMISSION_GRANTED)) {
                Log.d(TAG, "Permission not granted - " + permission);
                return permission;
            }
        }
        //All permissions granted
        return "";
    }

    public static void requestRuntimePermission(Activity activity, boolean shouldShowRationale,
                                                RuntimePermissionType permissionToRequest) {
        requestRuntimePermission(activity, shouldShowRationale, permissionToRequest, null, null);
    }

    public static void requestRuntimePermission(Activity activity, boolean shouldShowRationale,
                                                RuntimePermissionType permissionToRequest,
                                                GenericAlertDialogBuilder.AlertDialogCallbackInterface callbackForRationale,
                                                GenericAlertDialogBuilder.AlertDialogCallbackInterface callbackForSettings) {

        if (!RuntimePermissionHelper.areNecessaryPermissionsGranted(activity, permissionToRequest)) {
            // Should we show an explanation?
            if (shouldShowRationale) {

                String missingPermission = RuntimePermissionHelper.getFirstMissingPermission(activity, permissionToRequest);

                //This checks if the user had previously clicked "Never ask again" or not
                if (!TextUtils.isEmpty(missingPermission) &&
                        ActivityCompat.shouldShowRequestPermissionRationale(activity, missingPermission)) {
                    if (callbackForRationale == null) {
                        showRationaleDialog(activity, permissionToRequest);
                    } else {
                        showRationaleDialog(activity, permissionToRequest, callbackForRationale);
                    }
                } else {
                    if (callbackForSettings == null) {
                        showOpenSettingsDialog(activity, permissionToRequest);
                    } else {
                        showOpenSettingsDialog(activity, permissionToRequest, callbackForSettings);
                    }
                }

            } else {
                // No explanation needed, we can request the permission.
                requestAndroidForPermission(activity, permissionToRequest);
            }
        }
    }

    //Default method - either requests permission or finishes activity - generally used for PermissionCheckInterceptActivity
    public static void showRationaleDialog(final Activity activity, final RuntimePermissionType permissionToRequest) {
        showRationaleDialog(activity, permissionToRequest, new GenericAlertDialogBuilder.AlertDialogCallbackInterface() {
            @Override
            public void onPositiveButtonClicked() {
                requestAndroidForPermission(activity, permissionToRequest);
            }

            @Override
            public void onNegativeButtonClicked() {
                activity.finish();
            }
        });
    }

    //Custom callback method
    public static void showRationaleDialog(final Activity activity,
                                           final RuntimePermissionType permissionToRequest,
                                           final GenericAlertDialogBuilder.AlertDialogCallbackInterface callback) {

        if (activity == null) {
            return;
        }

        final android.support.v7.app.AlertDialog alertDialog = GenericAlertDialogBuilder.
                getGenericAlertDialog(GenericAlertDialogBuilder.AlertDialogUsecase.RUNTIME_PERMISSION_RATIONALE, activity);

        String appName = activity.getResources().getString(R.string.app_name);
        alertDialog.setMessage(activity.getResources().getString(permissionToRequest.getRationaleMsgResource(), appName));

        alertDialog.setOnShowListener(dialog -> {
            Button positiveButon = alertDialog.getButton(android.support.v7.app.AlertDialog.BUTTON_POSITIVE);
            if (positiveButon != null) {
                positiveButon.setOnClickListener(view -> {
                    alertDialog.dismiss();
                    callback.onPositiveButtonClicked();
                });
            }

            Button negativeButton = alertDialog.getButton(android.support.v7.app.AlertDialog.BUTTON_NEGATIVE);
            if (negativeButton != null) {
                negativeButton.setOnClickListener(view -> {
                    alertDialog.dismiss();
                    callback.onNegativeButtonClicked();
                });
            }
        });
        alertDialog.show();
    }

    //Default method - either requests permission or finishes activity - generally used for PermissionCheckInterceptActivity
    public static void showOpenSettingsDialog(final Activity activity, final RuntimePermissionType permissionToRequest) {
        showOpenSettingsDialog(activity, permissionToRequest, new GenericAlertDialogBuilder.AlertDialogCallbackInterface() {
            @Override
            public void onPositiveButtonClicked() {
                openAppSettings(activity);
            }

            @Override
            public void onNegativeButtonClicked() {
                activity.finish();
            }
        });
    }

    public static void showOpenSettingsDialog(final Activity activity,
                                              RuntimePermissionType mPermissionToRequest,
                                              final GenericAlertDialogBuilder.AlertDialogCallbackInterface callbackInterface) {

        if (activity == null) {
            return;
        }

        final AlertDialog alertDialog = GenericAlertDialogBuilder.
                getGenericAlertDialog(GenericAlertDialogBuilder.AlertDialogUsecase.RUNTIME_PERMISSION_SETTINGS_DIALOG, activity);

        String appName = activity.getResources().getString(R.string.app_name);
        alertDialog.setMessage(activity.getResources().getString(mPermissionToRequest.getSettingsMsgResource(), appName));
        alertDialog.setTitle(activity.getResources().getString(mPermissionToRequest.getSettingsTitleResource()));

        alertDialog.setOnShowListener(dialog -> {
            Button positiveButon = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
            if (positiveButon != null) {
                positiveButon.setOnClickListener(view -> {
                    alertDialog.dismiss();
                    callbackInterface.onPositiveButtonClicked();
                });
            }

            Button negativeButton = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
            if (negativeButton != null) {
                negativeButton.setOnClickListener(view -> {
                    alertDialog.dismiss();
                    callbackInterface.onNegativeButtonClicked();
                });
            }
        });
        alertDialog.show();
    }

    public static void openAppSettings(Activity activity) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
        intent.setData(uri);
        activity.startActivity(intent);
        activity.finish();
    }

    public static void requestAndroidForPermission(
            Activity activity, RuntimePermissionType permissionToRequest) {

        List<String> pendingPermissions = new ArrayList<>();
        for (String permission : permissionToRequest.getPermissionsArray()) {
            if (!(ContextCompat.checkSelfPermission(activity, permission)
                    == PackageManager.PERMISSION_GRANTED)) {
                Log.d(TAG, "Permission not granted - " + permission);
                pendingPermissions.add(permission);
            }
        }

        if (pendingPermissions.size() > 0) {
            ActivityCompat.requestPermissions(activity,
                    pendingPermissions.toArray(new String[pendingPermissions.size()]),
                    permissionToRequest.getRequestCode());
        }
    }
}