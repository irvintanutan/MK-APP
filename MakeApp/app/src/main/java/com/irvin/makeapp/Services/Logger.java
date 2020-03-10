package com.irvin.makeapp.Services;

import android.content.Context;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * @author irvin
 */
public class Logger {

    public static void CreateNewEntry(Context context , Exception ex, File file) {


        StringWriter sw = new StringWriter();
        ex.printStackTrace(new PrintWriter(sw));

        try {


            if (!file.exists()) {
                file.createNewFile();

                FileWriter fr = new FileWriter(file, true);
                fr.write("\n");
                fr.write(sw.toString());
                fr.write("\n");

                fr.close();
            } else {

                FileWriter fr = new FileWriter(file, true);
                fr.write("\n");
                fr.write(sw.toString());
                fr.write("\n");
                fr.close();

            }

            Toast.makeText(context, "ERROR " + ex.getMessage(), Toast.LENGTH_SHORT).show();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
