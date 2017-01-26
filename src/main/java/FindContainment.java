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
    public static String DIR = "/Users/Chaiyong/IdeasProjects/StackoverflowChecker/";

    public static void main(String args[]) {
//        checkAndMergePairsWithSpecificLength("/Users/Chaiyong/IdeasProjects/StackoverflowChecker/ok+good_160814_merged.csv"
//                , "/Users/Chaiyong/IdeasProjects/StackoverflowChecker/ok+good_160814.csv", 2, 13, false);
//        checkPairContainment("/Users/Chaiyong/Desktop/GOLD_ok+good_160816_merged_no_dup.csv"
//                , "/Users/Chaiyong/IdeasProjects/StackoverflowChecker/indv_simian_df_combined_latest_v_new_only_160825.csv");
        checkPairAndCopyDetails(
                DIR + "/good-ok_pairs/GOLD_FINAL_indv_nicad_df_130901_pt1+2+3_0.20.csv"
                ,DIR + "//indv_nicad_df_130901_pt1+2+3_fixed_clustering_failed_0.1.csv"
                , 1, 0, true, ",found_in_Nd_pairs");
//        checkIndvInGoodOkPairs(
//                DIR + "/PLATINUM_FINAL_good_130901_pt1+2+3.csv"
//                , DIR + "/a.csv"
//                , 2, 1, false, ",duplicated_with_good_pair");
//        checkTwoPairAndCopyDetails(
//                DIR + "/good-ok_pairs/GOLD_FINAL_indv_nicad_df_130901_pt1+2+3_0.20.csv"
//                , DIR + "/indv_nicad_df_130901_pt1+2+3_fixed_clustering_failed_0.15.csv"
//                , 1, 0, true, ",found_in_previous_ok_pairs");
//        checkExistAndCopyDetails(
//                "/Users/Chaiyong/IdeasProjects/StackoverflowChecker/indv_simian_df_combined_latest_v_new_only_160825.csv"
//                ,"/Users/Chaiyong/IdeasProjects/StackoverflowChecker/indv_nicad_df_combined_latest_v_new_only_160816_checked_equals.csv"
//                , 0, 5);
    }


    public static void checkIndvInGoodOkPairs(String baseFile, String searchFile, int offset, int offset2, boolean copyComments, String text) {
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
//                String key = "";
//                for (int i = start; i <= end; i++) {
//                    key += clone[i].trim();
//                }
                Fragment f = new Fragment(
                        clone[offset].trim(),
                        Integer.parseInt(clone[1 + offset]),
                        Integer.parseInt(clone[2 + offset]),
                        clone[3 + offset].trim(),
                        Integer.parseInt(clone[4 + offset]),
                        Integer.parseInt(clone[5 + offset]));
                baseFileMap.put(f.toString(), line);
                // System.out.println("key:" + f.toString());
            }
            br.close();

            br = new BufferedReader(new FileReader(searchFile));
            while ((line = br.readLine()) != null) {
                // System.out.println("line: " + line);
                // use comma as separator
                String[] clone = line.split(cvsSplitBy);
//                String key = "";
//                for (int i = start; i <= end; i++) {
//                    key += clone[i].trim();
//                }
                Fragment f = new Fragment(
                        clone[offset2].trim(),
                        Integer.parseInt(clone[1+offset2]),
                        Integer.parseInt(clone[2+offset2]),
                        clone[3+offset2].trim(),
                        Integer.parseInt(clone[4+offset2]),
                        Integer.parseInt(clone[5+offset2]));
                f.setOther(line);
                searchFileArr.add(f);
                // System.out.println("search:" + f.toString());
            }
            br.close();
            // System.out.println(baseFileMap.size());

            // start searching
            for (Fragment f : searchFileArr) {
                if (baseFileMap.containsKey(f.toString())) {
                    if (copyComments) {
                        String fline = baseFileMap.get(f.toString());
                        System.out.println(fline);
                    } else {
                        System.out.println(f.getOther() + text);
                    }
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

    public static void checkPairAndCopyDetails(String baseFile, String searchFile, int offset, int offset2, boolean copyComments, String text) {
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
//                String key = "";
//                for (int i = start; i <= end; i++) {
//                    key += clone[i].trim();
//                }
//                System.out.println(line);
                Fragment f = new Fragment(
                        clone[offset].trim(),
                        Integer.parseInt(clone[1 + offset]),
                        Integer.parseInt(clone[2 + offset]),
                        clone[3 + offset].trim(),
                        Integer.parseInt(clone[4 + offset]),
                        Integer.parseInt(clone[5 + offset]));
                baseFileMap.put(f.toString(), line);
                // System.out.println("key:" + f.toString());
            }
            br.close();

            br = new BufferedReader(new FileReader(searchFile));
            while ((line = br.readLine()) != null) {
                // System.out.println("line: " + line);
                // use comma as separator
                String[] clone = line.split(cvsSplitBy);
//                String key = "";
//                for (int i = start; i <= end; i++) {
//                    key += clone[i].trim();
//                }
                Fragment f = new Fragment(
                        clone[offset2].trim(),
                        Integer.parseInt(clone[1+offset2]),
                        Integer.parseInt(clone[2+offset2]),
                        clone[3+offset2].trim(),
                        Integer.parseInt(clone[4+offset2]),
                        Integer.parseInt(clone[5+offset2]));
                f.setOther(line);
                searchFileArr.add(f);
                // System.out.println("search:" + f.toString());
            }
            br.close();
            // System.out.println(baseFileMap.size());

            // start searching
            for (Fragment f : searchFileArr) {
                if (baseFileMap.containsKey(f.toString())) {
                    if (copyComments) {
                        String fline = baseFileMap.get(f.toString());
                        System.out.println(fline);
                    } else {
                        System.out.println(f.getOther() + text);
                    }
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

    public static void checkTwoPairAndCopyDetails(String baseFile, String searchFile, int offset, int offset2, boolean copyComments, String text) {
        HashMap<String, String> baseFileMap = new HashMap<>();
        ArrayList<Fragment> searchFileArr = new ArrayList<>();
        ArrayList<Fragment> searchFileArr2 = new ArrayList<>();
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";

        try {

            br = new BufferedReader(new FileReader(baseFile));
            while ((line = br.readLine()) != null) {
                // use comma as separator
                String[] clone = line.split(cvsSplitBy);
//                String key = "";
//                for (int i = start; i <= end; i++) {
//                    key += clone[i].trim();
//                }
                Fragment f = new Fragment(
                        clone[offset].trim(),
                        Integer.parseInt(clone[1 + offset]),
                        Integer.parseInt(clone[2 + offset]),
                        clone[3 + offset].trim(),
                        Integer.parseInt(clone[4 + offset]),
                        Integer.parseInt(clone[5 + offset]));
                Fragment f2 = new Fragment(
                        clone[6+offset].trim(),
                        Integer.parseInt(clone[7 + offset]),
                        Integer.parseInt(clone[8 + offset]),
                        clone[9 + offset].trim(),
                        Integer.parseInt(clone[10 + offset]),
                        Integer.parseInt(clone[11 + offset]));
                baseFileMap.put(f.toString() + "," + f2.toString(), line);

                // System.out.println("key:" + f.toString());
            }
            br.close();

            br = new BufferedReader(new FileReader(searchFile));
            while ((line = br.readLine()) != null) {
                // System.out.println("line: " + line);
                // use comma as separator
                String[] clone = line.split(cvsSplitBy);
//                String key = "";
//                for (int i = start; i <= end; i++) {
//                    key += clone[i].trim();
//                }
                Fragment f = new Fragment(
                        clone[offset2].trim(),
                        Integer.parseInt(clone[1+offset2].trim()),
                        Integer.parseInt(clone[2+offset2].trim()),
                        clone[3+offset2].trim(),
                        Integer.parseInt(clone[4+offset2].trim()),
                        Integer.parseInt(clone[5+offset2].trim()));
                f.setOther(line);

                Fragment f2 = new Fragment(
                        clone[6+offset2].trim(),
                        Integer.parseInt(clone[7 + offset2].trim()),
                        Integer.parseInt(clone[8 + offset2].trim()),
                        clone[9 + offset2].trim(),
                        Integer.parseInt(clone[10 + offset2].trim()),
                        Integer.parseInt(clone[11 + offset2].trim()));
                f2.setOther(line);

                searchFileArr.add(f);
                searchFileArr2.add(f2);
                // System.out.println("search:" + f.toString());
            }
            br.close();
            // System.out.println(baseFileMap.size());

            // start searching
            for (int i=0; i<searchFileArr.size(); i++) {
                Fragment f = searchFileArr.get(i);
                Fragment f2 = searchFileArr2.get(i);
                if (baseFileMap.containsKey(f.toString() + "," + f2.toString())) {
                    if (copyComments) {
                        String fline = baseFileMap.get(f.toString() + "," + f2.toString());
                        System.out.println(fline);
                    } else {
                        System.out.println(f.getOther() + text);
                    }
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
        HashMap<String, Fragment> soMap = new HashMap<>();
        HashMap<String, Fragment> qMap = new HashMap<>();
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
                Fragment f = new Fragment(clone[0], Integer.parseInt(clone[1]), Integer.parseInt(clone[2]),
                        clone[3], Integer.parseInt(clone[4]), Integer.parseInt(clone[5]));
                soMap.put(clone[0], f);
                qMap.put(clone[3], f);
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
                if (soMap.containsKey(f.getFirstFile())) {
                    Fragment soF = soMap.get(f.getFirstFile());
                    if (f.getMinCloneLine() > soF.getMinCloneLine())
                        System.out.println("duplicate with SO but bigger,keep," + f.getOther());
                    else
                        System.out.println("duplicate with SO but smaller,delete," + f.getOther());
                }
                else if (qMap.containsKey(f.getSecondFile())) {
//                    System.out.println(f.getOther() + ",duplicate with ok pair");
                    Fragment qF = qMap.get(f.getSecondFile());
                    if (f.getMinCloneLine() > qF.getMinCloneLine())
                        System.out.println("duplicate with Q but bigger,keep," + f.getOther());
                    else
                        System.out.println("duplicate with Q but smaller,delete," + f.getOther());
                } else {
                    System.out.println("x,x," + f.getOther());
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
