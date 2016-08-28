import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by Chaiyong on 8/14/16.
 */
public class CheckEqualsMethods {

    public static void main(String[] args) {
        checkEqualsMethods("/Users/Chaiyong/IdeasProjects/StackoverflowChecker/160816_common_pairs_simianfse13-nicadfse13-good-0.7.csv", 1, -1, "/Users/Chaiyong/Downloads/stackoverflow/");
    }

    public static void checkEqualsMethods(String file1, int start, int end, String path) {
        String cloneFile = file1;
        BufferedReader br = null;
        BufferedReader sF = null;
        BufferedReader qF = null;
        String line = "", sLine = "", qLine = "";
        String cvsSplitBy = ",";

        try {

            br = new BufferedReader(new FileReader(cloneFile));
            int count = 1;
            while ((line = br.readLine()) != null) {
                // use comma as separator
                if (end == -1 || (count >= start && count <= end)) {
                    String[] clone = line.split(cvsSplitBy);
                    boolean foundFirst = false, foundSecond = false;

                    sF = new BufferedReader(new FileReader(path + clone[1]));
                    int lineCount = 0;
                    while ((sLine=sF.readLine()) != null) {
                        lineCount++;
                        // start line of SO fragment
                        if (lineCount == Integer.parseInt(clone[2].trim())) {
                            if (sLine.contains("boolean equals ("))
                                foundFirst = true;

                            break;
                        }
                    }
                    sF.close();

                    qF = new BufferedReader(new FileReader(path + "QualitasCorpus-20130901r/projects_java_only_160816/" + clone[4]));
                    lineCount = 0;
                    while ((qLine=qF.readLine()) != null) {
                        lineCount++;
                        // start line of SO fragment
                        if (lineCount == Integer.parseInt(clone[5].trim())) {
                            if (qLine.contains("boolean equals ("))
                                foundSecond = true;

                            break;
                        }
                    }
                    qF.close();

                    // both are equals() methods
                    if (foundFirst && foundSecond) {
                        System.out.println(line + ",D,similar equals() methods");
                    } else {
                        System.out.println(line);
                    }
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
