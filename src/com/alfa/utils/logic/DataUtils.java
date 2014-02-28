package com.alfa.utils.logic;

import java.io.File;
import java.util.*;

/**
 * Created by Micha on 2/14/14.
 */
public class DataUtils {

    /**
     * lists all files with fileExtension in a directory
     *
     * @param directoryPath
     * @param fileExtension
     * @return all file names in directory
     */
    public static List<String> listFiles(String directoryPath, String fileExtension) {
        List<String> fileNames = new LinkedList<String>();

        try {

            // get all files from directory
            File directory = new File(directoryPath);
            int dotIndex;

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

                String fileExtention;
                String fileName;
                // Removing the .mp3 extension from all downloaded files
                for (File currentSongFile : songFilesCollection) {

                    fileName = currentSongFile.getName();
                    dotIndex = currentSongFile.getName().lastIndexOf(".");
                    if (dotIndex > 0) {

                        fileExtention = fileName.substring(dotIndex, fileName.length());
                        if (fileExtention.equals(fileExtension)) {
                            fileNames.add(fileName.substring(0, dotIndex));
                        }
                    } else {
                        if (!currentSongFile.isDirectory()) {
                            // check if a valid mp3 file
                            fileNames.add(fileName);
                        }
                    }

                }
            }
        } catch (Exception e) {
            LogUtils.logError("list_files", e.toString());
        }

        return fileNames;
    }

    // delete files from disk
    public static void deleteFile(String fileName) {

        try {
            String filePath = SharedPref.songDirectory + "/" + fileName + SharedPref.songExtension;
            File file = new File(filePath);
            if (file.exists()) {
                file.delete();
            } else {

                // try to delete file if it is a directory
                filePath = SharedPref.songDirectory + "/" + fileName;
                file = new File(filePath);
                if (file.exists() && file.isDirectory()) {
                    file.delete();
                } else {
                    LogUtils.logError("delete_file", "file <" + filePath + "> does not exist!");
                }
            }

        } catch (Exception e) {
            LogUtils.logError("delete_file", e.toString());
        }
    }


    // delete files from disk
    public static void renameFile(String fileName, String newName) {

        try {
            String filePath = SharedPref.songDirectory + "/" + fileName + SharedPref.songExtension;
            String newPath = SharedPref.songDirectory + "/" + newName + SharedPref.songExtension;
            File file = new File(filePath);
            File renamedFile = new File(newPath);
            if (file.exists()) {
                file.renameTo(renamedFile);
            } else {
                LogUtils.logError("delete_file", "file does not exist!");
            }

        } catch (Exception e) {
            LogUtils.logError("delete_file", e.toString());
        }
    }

    public static LinkedList<String> getSongNamesFromDirectory() {
        LinkedList<String> songNames = new LinkedList<String>();

        songNames = (LinkedList<String>) DataUtils.listFiles(SharedPref.songDirectory, SharedPref.songExtension);

        if (songNames.size() == 0) {
            songNames.add("no songs in library");
        }
        return songNames;
    }


}
