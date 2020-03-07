package com.irvin.makeapp.Adapters.ReportsFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.irvin.makeapp.R;

import androidx.fragment.app.Fragment;

/**
 *
 * @author irvin
 * @date 2/7/17
 */
public class TabFragmentReportCustomer extends Fragment {
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            view = inflater.inflate(R.layout.paid_tab, container, false);
 
        return view;
    }

}
