import org.apache.commons.io.FileUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by Chaiyong on 4/22/17.
 */
public class MyFileWriter {
    /***
     * Write a given string to a file
     * @param location location of the file
     * @param filename name of the file
     * @param content string to be written to the file
     * @param isAppend a flag specifying if writing from scratch or appending from the existing file
     */
    public static void writeToFile(String location, String filename, String content, boolean isAppend, boolean isPrintMessage) {
        /* copied from http://stackoverflow.com/questions/28947250/create-a-directory-if-it-does-not-exist-and-then-create-the-files-in-that-direct */
        File directory = new File(location);
        if (! directory.exists()){
            // create the file's directory
            directory.mkdirs();
        }

        /* copied from https://www.mkyong.com/java/how-to-write-to-file-in-java-bufferedwriter-example/ */
        BufferedWriter bw = null;
        FileWriter fw = null;

        try {
            fw = new FileWriter(location + "/" + filename, isAppend);
            bw = new BufferedWriter(fw);
            bw.write(content);
            if (!isAppend && isPrintMessage)
                System.out.println("Saved as: " + filename);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bw != null)
                    bw.close();
                if (fw != null)
                    fw.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    /***
     * Delete a specified folder
     * @param location location of the folder
     * @return success or not
     */
    public static boolean recreateAFolder(String location) {
        boolean success = false;
        /* copied from
        http://stackoverflow.com/questions/779519/delete-directories-recursively-in-java
        */
        try {
            File directory = new File(location);
            FileUtils.deleteDirectory(directory);

            success = directory.mkdirs();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return success;
    }
}
