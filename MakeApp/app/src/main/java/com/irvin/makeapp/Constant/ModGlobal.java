package com.irvin.makeapp.Constant;

import com.google.firebase.database.DataSnapshot;
import com.irvin.makeapp.Models.Category;
import com.irvin.makeapp.Models.CustomerModel;
import com.irvin.makeapp.Models.Products;

import java.util.ArrayList;
import java.util.List;

public class ModGlobal {

    public static boolean isCreateNew = false;
    public static int customerId;
    public static DataSnapshot dataSnapshot;
    public static List<Products> ProductModelList = new ArrayList<>();
    public static List<CustomerModel> customerModelList = new ArrayList<>();
    public static List<Category> categories = new ArrayList<>();



}
