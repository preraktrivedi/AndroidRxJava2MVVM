package com.preraktrivedi.android.mvvmbaseapp.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.preraktrivedi.android.mvvmbaseapp.R;
import com.preraktrivedi.android.mvvmbaseapp.datastore.AppData;
import com.preraktrivedi.android.mvvmbaseapp.helpers.GenericAlertDialogBuilder;


public abstract class BaseActivity extends AppCompatActivity {

    private static final String TAG = BaseActivity.class.getSimpleName();
    protected Context mContext;
    protected Toolbar toolbar;
    protected AppData appData = AppData.getInstance();
    private GenericAlertDialogBuilder.AlertDialogUsecase mDialogUsecase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    protected void loadFragment(Fragment fragment, boolean addToBackStack, int resourceId) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(resourceId, fragment);
        transaction = addToBackStack ?
                transaction.addToBackStack(fragment.getClass().getSimpleName())
                : transaction.disallowAddToBackStack();
        transaction.commit();
    }

    protected void configureToolbarWithBackButton(int res) {
        configureToolbarWithBackButton(getString(res), R.color.default_primary_background_text_color);
    }

    protected void configureToolbarWithBackButton(String title, int colorId) {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setTitle(title);
            setSupportActionBar(toolbar);
            if (getSupportActionBar() != null) {
                configureToolbarUpArrowColor(colorId);
            }
        }
    }

    protected void configureToolbarColor(int colorResource) {
        if (toolbar != null) {
            toolbar.setBackground(new ColorDrawable(ContextCompat.getColor(mContext, colorResource)));
        }
    }

    private void configureToolbarUpArrowColor(int colorId) {
        final Drawable upArrow = ContextCompat.getDrawable(this, R.drawable.abc_ic_ab_back_material);
        if (upArrow != null) {
            upArrow.setColorFilter(ContextCompat.getColor(this, colorId),
                    PorterDuff.Mode.SRC_ATOP);
            if (toolbar != null) {
                toolbar.setTitleTextColor(ContextCompat.getColor(mContext, colorId));
            }
            if (getSupportActionBar() != null) {
                getSupportActionBar().setHomeAsUpIndicator(upArrow);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        configureToolbarUpArrowColor(R.color.default_primary_background_text_color);
        if (mDialogUsecase != null) {
            showAlertDialog(mDialogUsecase);
        } else {
            if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
                this.finish();
            } else {
                super.onBackPressed();
            }
        }
    }


    private void showAlertDialog(GenericAlertDialogBuilder.AlertDialogUsecase dialogUsecase) {
        final AlertDialog alertDialog = GenericAlertDialogBuilder.
                getGenericAlertDialog(dialogUsecase, this);
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button positiveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                if (positiveButton != null) {
                    positiveButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            alertDialog.dismiss();
                            finish();
                        }
                    });
                }
                Button negativeButton = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                if (negativeButton != null) {
                    negativeButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            alertDialog.dismiss();
                        }
                    });
                }
            }
        });
        alertDialog.show();
    }


    public static boolean isActivityActive(Activity activity) {
        return activity != null && !activity.isFinishing();
    }

    protected void setSoftInputModeVisible() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
                | WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    protected void setSoftInputModeHidden() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
                | WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }
}