package com.alfa.utils;

import java.io.File;
import java.util.*;

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

        // get file names
        if (directory.listFiles() != null) {
            List<File> songFilesCollection = Arrays.asList(directory.listFiles());

            final Map<File, Long> staticLastModifiedTimesOfSongFiles = new HashMap<File, Long>();
            for (final File currentSongFile : songFilesCollection) {
                staticLastModifiedTimesOfSongFiles.put(currentSongFile, currentSongFile.lastModified());
            }
            Collections.sort(songFilesCollection, new Comparator<File>() {
                @Override
                public int compare(final File f1, final File f2) {
                    return staticLastModifiedTimesOfSongFiles.get(f2)
                            .compareTo(staticLastModifiedTimesOfSongFiles.get(f1));
                }
            });
            // Removing the .mp3 suffix from all downloaded files
            for (File currentSongFile : songFilesCollection) {
                fileNames.add(currentSongFile.getName().substring(0, currentSongFile.getName().lastIndexOf(".")));
            }
        }

        return fileNames;
    }


}