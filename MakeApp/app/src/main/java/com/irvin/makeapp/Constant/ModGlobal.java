package com.irvin.makeapp.Constant;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.irvin.makeapp.Models.Category;
import com.irvin.makeapp.Models.CustomerModel;
import com.irvin.makeapp.Models.Invoice;
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
    public static List<Products> ProductModelListCopy = new ArrayList<>();
    public static List<CustomerModel> customerModelList = new ArrayList<>();
    public static List<Category> categories = new ArrayList<>();
    public static List<StockIn> stockIns = new ArrayList<>();
    public static boolean indicator = false;
    public static int  position = -1;
    public static boolean isInSalesInvoice = false;
    public static String receiptLimit = "999999";
    public static String paymentId = "";
    public static String totalAmountPaid = "";
    public static Invoice invoice = new Invoice();

    ////please beware of changing value of database version
    public static int DATABASE_VERSION = 1;
    public static String imageFilePath;
    public static String totalBalance;

    public static boolean itemIsDuplicate(String prodCode) {
        boolean ind = false;


        for (int i = 0; i < stockIns.size(); i++) {

            if (stockIns.get(i).getProductCode().equals(prodCode)) {
                Log.e("PROD_CODE" , prodCode);
                ind = true;
            }

        }

        return ind;
    }

    public static void insertProduct(String productCode){

            for (int a = 0 ; a < ProductModelListCopy.size() ; a++){
                if (ProductModelListCopy.get(a).getProduct_id().equals(productCode)){
                    Log.e(productCode , "i lovey ou " + ProductModelListCopy.get(a).getProduct_id());
                    ProductModelList.add(ProductModelListCopy.get(a));
                    break;
                }
            }


    }

    public static void removeProduct() {

        for (int x = 0; x < stockIns.size(); x++) {
            for (int i = 0; i < ProductModelList.size(); i++) {

                if (ProductModelList.get(i).getProduct_id().equals(stockIns.get(x).getProductCode())) {
                    ProductModelList.remove(i);
                    break;
                }

            }
        }
    }



}
