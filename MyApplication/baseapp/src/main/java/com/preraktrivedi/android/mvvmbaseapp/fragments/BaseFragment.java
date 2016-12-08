package com.preraktrivedi.android.mvvmbaseapp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.preraktrivedi.android.mvvmbaseapp.datastore.AppData;

public class BaseFragment extends Fragment {

    protected Context mContext;
    protected AppData appData = AppData.getInstance();

    public static final String ARG_EMAIL_ADDRESS_VALUE = "ARG_EMAIL_ADDRESS_VALUE",
            ARG_PHONE_NUMBER_VALUE = "ARG_PHONE_NUMBER_VALUE",
            ARG_CONTACT_GUID_VALUE = "ARG_EMAIL_ADDRESS_GUID_VALUE";

    private static final String TAG = BaseFragment.class.getName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
    }

    public BaseFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    protected boolean isFragmentAttachedToActivity() {
        return getActivity() != null && isAdded();
    }

}