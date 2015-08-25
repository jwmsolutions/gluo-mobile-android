package com.gluo.pruebas;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.gluo.pruebas.tests.TestContent;
import com.noveogroup.android.log.Logger;
import com.noveogroup.android.log.LoggerManager;

/**
 * A fragment representing a single Test detail screen.
 * This fragment is either contained in a {@link TestListActivity}
 * in two-pane mode (on tablets) or a {@link TestDetailActivity}
 * on handsets.
 */
public class TestDetailFragment extends DefaultFragment {
    private static final Logger logger = LoggerManager.getLogger(TestDetailFragment.class);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            mItem = TestContent.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflateDefaultView(inflater, container, R.layout.fragment_test_detail);
        return rootView;
    }
}
