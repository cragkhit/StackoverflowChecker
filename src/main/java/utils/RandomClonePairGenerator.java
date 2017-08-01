package utils;

import data.RandomNumbers;

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
    public static int[] POPULATION = {523, 2927};
    public static int[] SAMPLE_SIZE = {52, 293};
    public static String[] NAMES = {"tp", "fp"};
    public static int TP_MODE = 0;
    public static int FP_MODE = 1;

    public static int MODE = FP_MODE;

    public static void main(String[] args) {
        RandomNumbers randNum = new RandomNumbers();
        Integer[] samples = randNum.generateRandomNumbers(SEED[MODE], POPULATION[MODE], SAMPLE_SIZE[MODE]);
        samplesArr = new ArrayList<>(Arrays.asList(samples));
        readAndGenerateFromCsv(HOMEDIR + "/clones_validation/samples_" + NAMES[MODE] + ".csv", HOMEDIR + "/clones_validation/selected_" + NAMES[MODE] + ".csv");
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
