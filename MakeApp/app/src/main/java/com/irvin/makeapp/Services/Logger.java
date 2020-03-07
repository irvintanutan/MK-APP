package com.irvin.makeapp.Services;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * @author irvin
 */
public class Logger {

    public static void CreateNewEntry(Exception ex, File file) {


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

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
