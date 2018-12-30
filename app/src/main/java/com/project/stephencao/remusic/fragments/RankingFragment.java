package com.project.stephencao.remusic.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class RankingFragment extends Fragment {
    private String mTitle;
    public static final String BUNDLE_TITLE = "title";

    public static RankingFragment newInstance(String title) {
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_TITLE, title);
        RankingFragment fragment = new RankingFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        if (bundle != null) {
            mTitle = bundle.getString(BUNDLE_TITLE);

        }
        TextView textView = new TextView(getActivity());
        textView.setText(mTitle);
        textView.setGravity(Gravity.CENTER);
        return textView;
    }
}
