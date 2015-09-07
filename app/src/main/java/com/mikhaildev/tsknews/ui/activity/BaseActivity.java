package com.mikhaildev.tsknews.ui.activity;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.mikhaildev.tsknews.R;

import butterknife.ButterKnife;


public abstract class BaseActivity extends AppCompatActivity {

    private ActionBar mActionBar = null;
    private ProgressDialog progressDialog;
    private Toolbar mToolbar;
    private View mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResource());
        ButterKnife.bind(this);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar!=null) {
            mProgressBar = mToolbar.findViewById(R.id.toolbar_progress_bar);
            setSupportActionBar(mToolbar);
            styleActionBar();
            showBackButton(true); // By default, we show back button
        }
    }

    protected abstract int getLayoutResource();


    public Toolbar getToolbar() {
        return mToolbar;
    }

    /**
     * show back button in ToolBar's view
     * @param show show back button when true
     */
    public void showBackButton(boolean show) {
        if (mActionBar!=null) {
            mActionBar.setDisplayHomeAsUpEnabled(show);
        }
    }

    /**
     * Set title for ToolBar
     * @param title
     */
    public void setTitle(String title) {
        if (mActionBar != null) {
            mActionBar.setTitle(title);
        }
    }

    /**
     * Show progress bar in activity
     */
    public void showProgressBar() {
        if (mProgressBar!=null)
            mProgressBar.setVisibility(View.VISIBLE);
    }

    /**
     * Hide progress bar in activity.
     */
    public void hideProgressBar() {
        if (mProgressBar!=null)
            mProgressBar.setVisibility(View.GONE);
    }

    /**
     * Show progress dialog in activity
     */
    public void showProgressDialog() {
        if ((progressDialog!=null) && (progressDialog.isShowing()))
            return;
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.plase_wait));
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
    }

    /**
     * Hide progress dialog in activity.
     */
    public void hideProgressDialog() {
        if (progressDialog!=null)
            progressDialog.dismiss();
        progressDialog = null;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Set a style with action bar (ToolBar)
     */
    private void styleActionBar() {
        mActionBar = getSupportActionBar();
    }
}
