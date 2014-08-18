package com.pread.yoursoulmate.io;

import android.util.Log;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class FileIO {
    private List<String> resultList;

    public FileIO() {
        init();
    }

    private void init() {
        resultList = new ArrayList<String>();
    }

    public void read(InputStream inputStream) {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, "EUC_KR"));

            while (true) {
                String str = br.readLine();
                if (str == null) {
                    break;
                }

                resultList.add(str);
            }

        } catch (FileNotFoundException fe) {

            Log.e("FileNotFoundException", "File not found.");
        } catch (IOException ioe) {

            Log.e("IOException", "IOException");
        }
    }

    public List<String> getResult() {
        return resultList;
    }
}