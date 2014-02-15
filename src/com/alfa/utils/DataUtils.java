package com.alfa.utils;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Micha on 2/14/14.
 */
public class DataUtils {

    /**
     * @param directoryPath
     * @return all file names in directory
     */
    public static List<String> listFiles(String directoryPath) {
        List<String> fileNames = new LinkedList<String>();

        // get all files from directory
        File directory = new File(directoryPath);
        File[] fileList = directory.listFiles();

        // get file names
        if (fileList != null) {

            for (File file : fileList) {
                fileNames.add(file.getName().substring(0, file.getName().lastIndexOf(".")));
            }
        }

        return fileNames;
    }


}
