package com.irvin.makeapp.Adapters.CustomerFragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.irvin.makeapp.Activities.Customer.CustomerDetailsActivity;
import com.irvin.makeapp.Constant.ModGlobal;
import com.irvin.makeapp.Database.DatabaseCustomer;
import com.irvin.makeapp.Database.DatabaseHelper;
import com.irvin.makeapp.Models.CustomerModel;
import com.irvin.makeapp.R;

import androidx.fragment.app.Fragment;

/**
 *
 * @author irvin
 * @date 2/7/17
 */
public class TabFragmentCustomerProfile extends Fragment {
    View view;
    DatabaseHelper databaseHelper;
    DatabaseCustomer databaseCustomer ;
    CustomerModel customerModel = new CustomerModel();
    EditText skinType, skinTone, skinConcern, interest;
    FloatingActionButton fab;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.tab_customer_profile, container, false);
        databaseHelper = new DatabaseHelper(getActivity());
        databaseCustomer = new DatabaseCustomer(getActivity());

        fab = view.findViewById(R.id.floating_action_button);
        customerModel = databaseCustomer.getAllCustomer(ModGlobal.customerId);

        skinType = view.findViewById(R.id.skinType);
        skinTone = view.findViewById(R.id.skinTone);
        skinConcern = view.findViewById(R.id.skinConcern);
        interest = view.findViewById(R.id.interest);

        skinType.setText(customerModel.getSkinType());
        skinConcern.setText(customerModel.getSkinConcern());
        skinTone.setText(customerModel.getSkinTone());
        interest.setText(customerModel.getInterests());

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    ModGlobal.isCreateNew = false;
                    ModGlobal.customerId = customerModel.getId();
                    ModGlobal.customerName = customerModel.getFullName();
                    Intent i = new Intent(getActivity(), CustomerDetailsActivity.class);
                    i.putExtra("toolBarTitle", "");
                    startActivity(i);
                    getActivity().finish();
                    getActivity().overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
            }
        });

        return  view;
    }


}
