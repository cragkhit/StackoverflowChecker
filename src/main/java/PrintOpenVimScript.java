import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

/**
 * Created by Chaiyong on 8/14/16.
 */
public class PrintOpenVimScript {

    public static void main(String[] args) {
        String QPath = "QualitasCorpus-20130901r/projects";
        printVimScript("/Users/Chaiyong/IdeasProjects/StackoverflowChecker/GOLD_indv_nicad_df_combined_130901_0.2_checked+copied.csv"
                , QPath, 86);
    }

    public static void printVimScript(String file1, String QPath, int startingLine) {
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
                    System.out.println("vim -c \":e " + QPath + "/" + clone[3] + "|:" + clone[4] + "|:vsplit " + clone[0] + "|:" + clone[1] + "\"");
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
