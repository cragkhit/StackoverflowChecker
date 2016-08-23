import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Chaiyong on 8/14/16.
 */
public class FindContainment {

    public static void main(String args[]) {
//        checkAndMergePairsWithSpecificLength("/Users/Chaiyong/IdeasProjects/StackoverflowChecker/ok+good_160814_merged.csv"
//                , "/Users/Chaiyong/IdeasProjects/StackoverflowChecker/ok+good_160814.csv", 2, 13, false);
//        checkPairContainment("/Users/Chaiyong/Desktop/ok_pairs_new.csv"
//                , "/Users/Chaiyong/Desktop/good_pairs_old.csv", 2, 13);
//        checkPairAndCopyDetails(
//                "/Users/Chaiyong/IdeasProjects/StackAnalyzer/manual/indv_previous_investigation.csv"
//                ,"/Users/Chaiyong/IdeasProjects/StackAnalyzer/indv_nicad_df_combined.csv"
//                , 0, 5);
        checkExistAndCopyDetails(
                "/Users/Chaiyong/IdeasProjects/StackoverflowChecker/ok_pairs_old.csv"
                ,"/Users/Chaiyong/IdeasProjects/StackoverflowChecker/a.csv"
                , 0, 5);
    }

    public static void checkPairContainment(String file1, String file2) {
        String baseFile = file1;
        String searchFile = file2;
        HashMap<String, String> cloneMap = new HashMap<>();
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";

        try {

            br = new BufferedReader(new FileReader(baseFile));
            while ((line = br.readLine()) != null) {

                // use comma as separator
                String[] clone = line.split(cvsSplitBy);
                String key = "";
                for (int i = 2; i < clone.length; i++) {
                    key += clone[i].trim();
                }

                cloneMap.put(key, line);
            }
            br.close();

            br = new BufferedReader(new FileReader(searchFile));
            while ((line = br.readLine()) != null) {
                // use comma as separator
                String[] clone = line.split(cvsSplitBy);
                String key = "";
                for (int i = 2; i < clone.length; i++) {
                    key += clone[i].trim();
                }

                if (!cloneMap.containsKey(key)) {
                    System.out.println("missing," + line);
                } else {
                    System.out.println("found," + line);
                }
            }
            br.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void checkPairContainment(String file1, String file2, int start, int end) {
        String baseFile = file1;
        String searchFile = file2;
        HashMap<String, String> cloneMap = new HashMap<>();
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";

        try {

            br = new BufferedReader(new FileReader(baseFile));
            while ((line = br.readLine()) != null) {

                // use comma as separator
                String[] clone = line.split(cvsSplitBy);
                String key = "";
                for (int i = start; i <= end; i++) {
                    key += clone[i].trim();
                }

                cloneMap.put(key, line);
            }
            br.close();

            br = new BufferedReader(new FileReader(searchFile));
            while ((line = br.readLine()) != null) {
                // use comma as separator
                String[] clone = line.split(cvsSplitBy);
                String key = "";
                for (int i = start; i <= end; i++) {
                    key += clone[i].trim();
                }

                if (!cloneMap.containsKey(key)) {
                    System.out.println("missing," + line);
                } else {
                    System.out.println("found," + line);
                }
            }
            br.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void checkAndMergePairsWithSpecificLength(String file1, String file2, int start, int end, boolean isCombined) {
        String baseFile = file1;
        String searchFile = file2;
        HashMap<String, String> cloneMap = new HashMap<>();
        HashMap<String, String> resultMap = new HashMap<>();
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";

        try {

            br = new BufferedReader(new FileReader(baseFile));
            while ((line = br.readLine()) != null) {

                // use comma as separator
                String[] clone = line.split(cvsSplitBy);
                String key = "";
                for (int i = start; i <= end; i++) {
                    key += clone[i].trim();
                }
                cloneMap.put(key, line);
            }
            br.close();

            br = new BufferedReader(new FileReader(searchFile));
            while ((line = br.readLine()) != null) {
                // use comma as separator
                String[] clone = line.split(cvsSplitBy);
                String key = "";
                for (int i = start; i <= end; i++) {
                    key += clone[i].trim();
                }

                if (isCombined) {
                    if (clone[0].contains("good_")) {
                        resultMap.put(key, line);
                    }
                    else if (cloneMap.containsKey(key)) {
                        String c = cloneMap.get(key);
                        resultMap.put(key, "ok_" + c);
                    } else {
                        resultMap.put(key, line);
                    }
                } else {
                    if (!cloneMap.containsKey(key)) {
                        System.out.println("Can't find: " + line);
                    }
                }
            }
            br.close();


            if (isCombined) {
                Iterator it = resultMap.entrySet().iterator();

                while (it.hasNext()) {
                    Map.Entry pair = (Map.Entry) it.next();
                    // System.out.println(pair.getKey() + " = " + pair.getValue());
                    String dupClone = (String) pair.getValue();
                    System.out.println(dupClone);
                    it.remove(); // avoids a ConcurrentModificationException
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void checkPairAndCopyDetails(String file1, String file2, int start, int end) {
        String baseFile = file1;
        String searchFile = file2;
        HashMap<String, String> baseFileMap = new HashMap<>();
        ArrayList<Fragment> searchFileArr = new ArrayList<>();
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";

        try {

            br = new BufferedReader(new FileReader(baseFile));
            while ((line = br.readLine()) != null) {

                // use comma as separator
                String[] clone = line.split(cvsSplitBy);
                String key = "";
                for (int i = start; i <= end; i++) {
                    key += clone[i].trim();
                }
                Fragment f = new Fragment(clone[0],
                        Integer.parseInt(clone[1]),
                        Integer.parseInt(clone[2]),
                        clone[3],
                        Integer.parseInt(clone[4]),
                        Integer.parseInt(clone[5]));
                baseFileMap.put(f.toString(), line);
            }
            br.close();

            br = new BufferedReader(new FileReader(searchFile));
            while ((line = br.readLine()) != null) {

                // use comma as separator
                String[] clone = line.split(cvsSplitBy);
                String key = "";
                for (int i = start; i <= end; i++) {
                    key += clone[i].trim();
                }
                Fragment f = new Fragment(clone[0],
                        Integer.parseInt(clone[1]),
                        Integer.parseInt(clone[2]),
                        clone[3],
                        Integer.parseInt(clone[4]),
                        Integer.parseInt(clone[5]));
                f.setOther(line);
                searchFileArr.add(f);
            }
            br.close();

            // start searching
            for (Fragment f : searchFileArr) {
                if (baseFileMap.containsKey(f.toString())) {
                    String fline = baseFileMap.get(f.toString());
                    System.out.println(fline);
                } else {
                    System.out.println(f.getOther());
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /***
     * Check if either SO fragment or Q file exists in the previously investigated ok pairs or not.
     * @param file1
     * @param file2
     * @param start
     * @param end
     */
    public static void checkExistAndCopyDetails(String file1, String file2, int start, int end) {
        String baseFile = file1;
        String searchFile = file2;
        HashMap<String, String> soMap = new HashMap<>();
        HashMap<String, String> qMap = new HashMap<>();
        ArrayList<Fragment> searchFileArr = new ArrayList<>();
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";

        try {

            br = new BufferedReader(new FileReader(baseFile));
            while ((line = br.readLine()) != null) {

                // use comma as separator
                String[] clone = line.split(cvsSplitBy);
                String key = "";
                for (int i = start; i <= end; i++) {
                    key += clone[i].trim();
                }
                soMap.put(clone[0], line);
                qMap.put(clone[3], line);
            }
            br.close();

            br = new BufferedReader(new FileReader(searchFile));
            while ((line = br.readLine()) != null) {

                // use comma as separator
                String[] clone = line.split(cvsSplitBy);
                String key = "";
                for (int i = start; i <= end; i++) {
                    key += clone[i].trim();
                }
                Fragment f = new Fragment(clone[0],
                        Integer.parseInt(clone[1]),
                        Integer.parseInt(clone[2]),
                        clone[3],
                        Integer.parseInt(clone[4]),
                        Integer.parseInt(clone[5]));
                f.setOther(line);
                searchFileArr.add(f);
            }
            br.close();

            // start searching
            for (Fragment f : searchFileArr) {
                if (soMap.containsKey(f.getFirstFile()) || qMap.containsKey(f.getSecondFile())) {
                    System.out.println(f.getOther() + ",duplicate with ok pair");
                } else {
                    System.out.println(f.getOther());
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
