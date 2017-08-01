package exec;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

/**
 * Created by Chaiyong on 8/14/16.
 */
public class PrintVimScript {

    public static void main(String[] args) {
        String QPath = "qualitas";

        printVimScript("/Users/Chaiyong/IdeasProjects/StackoverflowChecker/clones_validation/newly_added_pairs_10_percent_for_Mathues.csv"
                , QPath, 1, 0);
    }

    public static void printVimScript(String file1, String QPath, int startingLine, int startingIndex) {
        String okFile = file1;
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";

        try {

            br = new BufferedReader(new FileReader(okFile));
            int count = 1;
            while ((line = br.readLine()) != null) {

                // use comma as separator
                if (count >= startingLine) {
                    String[] clone = line.split(cvsSplitBy);
                    System.out.println("vim -c \":e " + QPath + "/" + clone[startingIndex + 3] + "|:" + clone[startingIndex + 4]
                            + "|:vsplit " + clone[startingIndex] + "|:" + clone[startingIndex + 1] + "\"");
                    // System.out.println("vim " + clone[2] + " +" + clone[3]);
                }
                count++;
            }
            br.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
