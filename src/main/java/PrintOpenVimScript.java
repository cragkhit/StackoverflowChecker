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
        printVimScript("/Users/Chaiyong/Desktop/a.csv");
    }

    public static void printVimScript(String file1) {
        String okFile = file1;
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";

        try {

            br = new BufferedReader(new FileReader(okFile));
            int count = 1;
            while ((line = br.readLine()) != null) {

                // use comma as separator
                if (count >= 1 && count <= 100) {
                    String[] clone = line.split(cvsSplitBy);
                    System.out.println("vim -c \":e QualitasCorpus-20130901r/projects_java_only_160816/" + clone[3] + "|:" + clone[4] + "|:vsplit " + clone[0] + "|:" + clone[1] + "\"");
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
