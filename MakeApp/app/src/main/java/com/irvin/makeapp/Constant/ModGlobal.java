package com.irvin.makeapp.Constant;

import com.google.firebase.database.DataSnapshot;
import com.irvin.makeapp.Models.Category;
import com.irvin.makeapp.Models.CustomerModel;
import com.irvin.makeapp.Models.Products;
import com.irvin.makeapp.Models.StockIn;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ModGlobal {

    public static boolean isCreateNew = false;
    public static int customerId;
    public static String customerName = "";
    public static DataSnapshot dataSnapshot;
    public static List<Products> ProductModelList = new ArrayList<>();
    public static List<CustomerModel> customerModelList = new ArrayList<>();
    public static List<Category> categories = new ArrayList<>();
    public static List<StockIn> stockIns = new ArrayList<>();
    public static boolean indicator = false;
    public static int  position = -1;
    public static boolean isInSalesInvoice = false;
    public static String receiptLimit = "999999";

    public static boolean itemIsDuplicate(String prodCode) {
        boolean ind = false;


        for (int i = 0; i < stockIns.size(); i++) {

            if (stockIns.get(i).getProductCode().equals(prodCode)) {
                ind = true;
            }

        }

        return ind;
    }
}
