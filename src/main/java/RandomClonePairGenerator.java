import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by chaiyong on 28/11/2016.
 */
public class RandomClonePairGenerator {
    public static ArrayList<Integer> samplesArr;
    public static String HOMEDIR = "/Users/chaiyong/Documents/StackoverflowChecker";
    public static int[] SEED = {53, 37};
    public static int TP_MODE = 0;
    public static int FP_MODE = 1;

    public static int MODE = FP_MODE;
    public static void main(String[] args) {
        RandomNumbers randNum = new RandomNumbers();
        Integer[] samples = randNum.generateRandomNumbers(SEED[MODE], 523, 146);
        samplesArr = new ArrayList<>(Arrays.asList(samples));
        readAndGenerateFromCsv(HOMEDIR + "/samples_fp.csv", HOMEDIR + "/selected_fp.csv");
    }

    public static void readAndGenerateFromCsv(String csvFile, String outFile) {
        String line;

        int count = 0;
        try {
            BufferedReader br = new BufferedReader(new FileReader(csvFile));
            File oFile = new File(outFile);
            FileWriter fw = new FileWriter(oFile.getAbsoluteFile(), false);

            while ((line = br.readLine()) != null) {
                // check if the clone pair is selected
                if (samplesArr.contains(count)) {
                    fw.write(line + "\n");
                }
                count++;
            }
            br.close();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
