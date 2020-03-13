package com.irvin.makeapp.Adapters.CustomerFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;

import com.irvin.makeapp.R;

/**
 *
 * @author irvin
 * @date 2/7/17
 */
public class TabFragmentCustomer222 extends Fragment {
    LinearLayout nothing;
    View view;
    Button start222;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.tab_customer_222, container, false);



        nothing = view.findViewById(R.id.nothing);
        start222 = view.findViewById(R.id.start222);


        start222.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        return view;
    }

}
