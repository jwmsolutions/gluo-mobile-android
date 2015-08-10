package com.gluo.pruebas;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gluo.pruebas.tests.TestContent;
import com.noveogroup.android.log.Logger;
import com.noveogroup.android.log.LoggerManager;

/**
 * Created by rbnquintero on 7/30/15.
 */
public class DefaultFragment extends Fragment {
    private static final Logger logger = LoggerManager.getLogger(TestDetailActivity.class);
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The dummy content this fragment is presenting.
     */
    public TestContent.TestItem mItem;

    /**
     * Inflate the view with given layout
     */
    public View inflateDefaultView(LayoutInflater inflater, ViewGroup container, int layout) {
        logger.d("Test seleccionado: " + mItem.toString());
        View rootView = inflater.inflate(layout, container, false);

        // Show the dummy content as text in a TextView.
        if (mItem != null && rootView.findViewById(R.id.test_detail) != null) {
            ((TextView) rootView.findViewById(R.id.test_detail)).setText(mItem.content);
        }

        return rootView;
    }

}
