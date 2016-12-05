package com.preraktrivedi.android.instagrampopular.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.preraktrivedi.android.instagrampopular.R;
import com.preraktrivedi.android.instagrampopular.fragments.SplashFragment;
import com.preraktrivedi.android.instagrampopular.helpers.RuntimePermissionHelper;

import butterknife.ButterKnife;

/**
 * Created by preraktrivedi on 11/26/16.
 */
public class SplashActivity extends BaseActivity implements SplashFragment.SplashEventsListener {

    private static final String TAG = SplashActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        loadFragment(SplashFragment.newInstance(), true, R.id.fragment_container);
    }

    @Override
    public void onPermissionRequired(RuntimePermissionHelper.RuntimePermissionType permissionType) {
        Toast.makeText(mContext, "TODO - Handle permissions", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAppLaunchSuccessful() {
        Log.d(TAG, ">Launch PhotoFeedActivity");
        startActivity(PhotoFeedActivity.createIntent(this));
        this.finish();
    }
}
