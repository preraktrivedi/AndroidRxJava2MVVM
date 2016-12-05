package com.preraktrivedi.android.instagrampopular.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.preraktrivedi.android.instagrampopular.R;
import com.preraktrivedi.android.instagrampopular.helpers.GenericAlertDialogBuilder;
import com.preraktrivedi.android.instagrampopular.helpers.RuntimePermissionHelper;
import com.preraktrivedi.android.instagrampopular.helpers.Util;
import com.preraktrivedi.android.instagrampopular.viewbinder.SplashBinder;
import com.preraktrivedi.android.instagrampopular.viewmodel.SplashViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by preraktrivedi on 11/26/16.
 *
 * Fragments need to implement the View Callbacks as well as define an instance of the ViewModel
 * which would be processing the business logic and provide callback method calls accordingly.
 *
 */
public class SplashFragment extends BaseFragment implements SplashBinder.View {

    @BindView(R.id.rlSplashView)
    RelativeLayout rlSplashView;

    private static final String TAG = SplashFragment.class.getSimpleName();

    public interface SplashEventsListener {
        void onPermissionRequired(RuntimePermissionHelper.RuntimePermissionType permissionType);
        void onAppLaunchSuccessful();
    }

    private SplashEventsListener mCallback;
    private SplashBinder.ViewModel mSplashViewModel;

    public static SplashFragment newInstance() {
        return new SplashFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_splash, container, false);
        ButterKnife.bind(this, view);
        initUi();
        return view;
    }

    private void initUi() {
        checkForPermissions();
    }

    private void checkForPermissions() {

        //Check For READ_PHONE_STATE first for device fingerprint
        RuntimePermissionHelper.RuntimePermissionType permissionRequired =
                RuntimePermissionHelper.RuntimePermissionType.DEVICE_DETAILS;

        if (!RuntimePermissionHelper.areNecessaryPermissionsGranted(getActivity(),
                permissionRequired)) {
            Log.d(TAG, "READ_PHONE_STATE not granted");
            mCallback.onPermissionRequired(permissionRequired);
            return;
        }

        initiateNetworkCalls();
    }

    private void initiateNetworkCalls() {
        if (!Util.isNetworkAvailable(getActivity())) {
            Log.d(TAG, "No Internet Detected");
            promptForInternetConnectivity();
            return;
        }

        getAppSettings();
    }

    private void promptForInternetConnectivity() {
        GenericAlertDialogBuilder.showAlertDialogWithCallback(getActivity(),
                GenericAlertDialogBuilder.AlertDialogUsecase.REQUEST_INTERNET,
                new GenericAlertDialogBuilder.AlertDialogCallbackInterface() {
                    @Override
                    public void onPositiveButtonClicked() {
                        initiateNetworkCalls();
                    }

                    @Override
                    public void onNegativeButtonClicked() {
                        getActivity().finish();
                    }
                });
    }

    public void getAppSettings() {
        mSplashViewModel = new SplashViewModel.Builder(Util.getAndroidId(mContext))
                .with("")
                .build();

        mSplashViewModel.onViewAttached(this); //Tell ViewModel where to send View callbacks
        mSplashViewModel.getAppSettings();
    }


    @Override
    public void onAppLaunchSuccess() {
        Log.d(TAG, ">onAppLaunchSuccess");
        mCallback.onAppLaunchSuccessful();
    }

    @Override
    public void onAppLaunchFailure(Throwable e) {
        Log.e(TAG, ">onAppLaunchFailure", e);
        GenericAlertDialogBuilder.showErrorWithMessage(getActivity(),
                getString(R.string.technical_error_message), true);
    }

    @Override
    public void onDestroy() {
        if (mSplashViewModel != null) {
            mSplashViewModel.onViewDestroyed();
        }
        super.onDestroy();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback = (SplashEventsListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement "
                    + SplashEventsListener.class.getSimpleName());
        }
    }
}