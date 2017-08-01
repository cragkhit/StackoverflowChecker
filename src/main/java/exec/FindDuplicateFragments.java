package exec;

import data.Fragment;
import utils.MyFileWriter;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class FindDuplicateFragments {

    public static void main(String[] args) {
        FindDuplicateFragments fdf = new FindDuplicateFragments();
        fdf.run("/Users/Chaiyong/Desktop/nicad_clones.csv","/Users/Chaiyong/Desktop/scc_clones.csv");
    }

    public void run(String baseFile, String searchFile) {
        HashMap<String, Integer> baseFileMap = new HashMap<>();
        ArrayList<String> searchFileArr = new ArrayList<>();
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";
        StringBuilder results = new StringBuilder();

        int found = 0;
        int notFound = 0;

        try {
            br = new BufferedReader(new FileReader(baseFile));
            while ((line = br.readLine()) != null) {
                // use comma as separator
                String[] fragment = line.split(cvsSplitBy);
                baseFileMap.put(fragment[0], 1);
            }

            br.close();

            System.out.println("Base file size (" + baseFile + "): " + baseFileMap.size());

            br = new BufferedReader(new FileReader(searchFile));

            System.out.println("### Start searching ... ###");

            while ((line = br.readLine()) != null) {
                String[] fragment = line.split(cvsSplitBy);
                searchFileArr.add(fragment[0]);
            }

            System.out.println("Search file size (" + searchFile + "): " + searchFileArr.size());

            br.close();

            // start searching
            for (String f : searchFileArr) {

                if (baseFileMap.containsKey(f.toString())) {
                    found++;
                } else {
                    notFound++;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ArrayIndexOutOfBoundsException|NumberFormatException e2) {
            e2.printStackTrace();
        }

        System.out.println("Found: " + found);
        System.out.println("Not found: " + notFound);
    }
}
