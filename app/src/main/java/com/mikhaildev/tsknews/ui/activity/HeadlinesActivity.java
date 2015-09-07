package com.mikhaildev.tsknews.ui.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.mikhaildev.tsknews.R;
import com.mikhaildev.tsknews.ui.fragment.HeadlinesFragment;
import com.mikhaildev.tsknews.util.StringUtils;


public class HeadlinesActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutResource();
        showMainFragment();
        showBackButton(false);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.a_headlines;
    }

    private void showMainFragment() {
        HeadlinesFragment headlinesFragment = (HeadlinesFragment) getSupportFragmentManager().findFragmentByTag(StringUtils.HEADLINE_FRAGMENT);
        if (headlinesFragment == null) {
            headlinesFragment = new HeadlinesFragment();
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, headlinesFragment, StringUtils.HEADLINE_FRAGMENT);
        transaction.commit();
    }
}
