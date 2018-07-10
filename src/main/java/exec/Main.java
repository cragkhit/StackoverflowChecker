package exec;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

import data.Fragment;
import data.FragmentComparator;
import data.OkPair;
import data.ReportedFragment;

import java.io.BufferedWriter;
import java.io.FileWriter;

public class Main {

	// public static String basePath = "";
	// private static String pathToRemove = "/dev/shm/gbianco";
	private static String pathSo = "stackoverflow_formatted/";
	// private static String pathQualitas = "QualitasCorpus-20130901r/compressed_src";

    private static String tool1 = "cloverflow";
    private static String settings1 = "df";

    // choose mode between old, good, ok
    // private static String mode = "ok";
    private static double p = 0.5;

    private static String tool2 = "siamese";
    private static String settings2 = "df";
//    private static String ending = "_200817";
    private static String ending = "_010618";

//    private static int minCloneSize = 10;
    private static int minCloneSize = 6;

    private static HashMap<String, ArrayList<Fragment>> fragmentMap = new HashMap<>();
    private static ArrayList<ReportedFragment> firstFragmentList = new ArrayList<>();
    private static ArrayList<ReportedFragment> secondFragmentList = new ArrayList<>();

	public static void main(String[] args) {
//        readXMLFileToList("/Users/Chaiyong/IdeasProjects/cloverflowtools/results/fragments_"
//                        + tool1 + "_" + settings1 + ending + ".xml", firstFragmentList);
//        readXMLFileToList("/Users/Chaiyong/IdeasProjects/cloverflowtools/results/fragments_"
//                        + tool2 + "_" + settings2 + ending + ".xml", secondFragmentList);
//        findOkMatches(firstFragmentList, secondFragmentList,
//                "common_pairs_" + tool2 + "_" + settings2 + "-"
//                        + tool1 + "_" + settings1 + "-" + p + ending + ".csv");

        /* For comparing results from SIAMESE to CLOVERFLOW */
        readCSVFileToList("so-qualitas_2289.csv", firstFragmentList);
        readCSVFileToList("so-qualitas_siamese_clone_pairs_fw100_4rep.csv", secondFragmentList);
        findOkMatches(firstFragmentList, secondFragmentList,
                "common_pairs_" + tool2 + "_" + settings2 + "-"
                        + tool1 + "_" + settings1 + "-" + p + ending + ".csv");
        /* OLD CODE */
//        readSecondFileAndCompare("/Users/Chaiyong/IdeasProjects/cloverflowtools/results/fragments_"
//                        + tool2 + "_" + settings2 + ending + ".xml"
//                , "common_pairs_" + tool2 + "_" + settings2 + "-"
//                        + tool1 + "_" + settings1 + "-" + p + ending + ".csv");


	}

    public static ArrayList<ReportedFragment> readXMLFileToList(String file,
                                                                ArrayList<ReportedFragment> fragmentList) {
        System.out.println("Reading the first file: " + file);
        List<ReportedFragment> firstFileResult =
                IndvCloneFilter.getInstance().getClonePairs(file, minCloneSize);
        for (ReportedFragment rf: firstFileResult) {
//            addToFragmentMap(rf);
//            addToFragmentList(rf);
            fragmentList.add(rf);
        }
        return fragmentList;
    }

    public static ArrayList<ReportedFragment> readCSVFileToList(String file,
                                                                ArrayList<ReportedFragment> fragmentList) {
        System.out.println("Reading the file: " + file);
        List<ReportedFragment> flist =
                IndvCloneFilter.getInstance().getCSVClonePairs(file, minCloneSize);
        for (ReportedFragment rf: flist) {
//            addToFragmentMap(rf);
//            addToFragmentList(rf);
            fragmentList.add(rf);
        }
        return fragmentList;
    }

    private static void findOkMatches(ArrayList<ReportedFragment> firstFragmentList,
                                      ArrayList<ReportedFragment> secondFragmentList, String outFile) {
        int foundCount = 0;
        try {
            FileWriter fwriterOk = new FileWriter("ok_" + outFile, false);
            BufferedWriter bwOk = new BufferedWriter(fwriterOk);
            PrintWriter writerOk = new PrintWriter(bwOk);
            FileWriter fwriterIndv1 = new FileWriter(
                    "indv_"
                            + tool1 + "_"
                            + settings1
                            + ending + ".csv", false);
            BufferedWriter bwIndv1 = new BufferedWriter(fwriterIndv1);
            PrintWriter writerIndv1 = new PrintWriter(bwIndv1);
            FileWriter fwriterIndv2 = new FileWriter(
                    "indv_"
                            + tool2 + "_"
                            + settings2
                            + ending + ".csv", false);
            BufferedWriter bwIndv2 = new BufferedWriter(fwriterIndv2);
            PrintWriter writerIndv2 = new PrintWriter(bwIndv2);
            ArrayList<OkPair> okPairList = new ArrayList<>();
            // sort the two list
            Collections.sort(firstFragmentList);
            Collections.sort(secondFragmentList);
            for (Iterator<ReportedFragment> iterator = secondFragmentList.iterator(); iterator.hasNext();) {
                ReportedFragment rf = iterator.next();
                boolean isFound = okOverlapWithFragmentList(rf, writerOk);
                if (isFound) {
                    // remove fragment from the list
                    iterator.remove();
                    foundCount++;
                }
            }
            writeFragmentListToFile(firstFragmentList, writerIndv1);
            writeFragmentListToFile(secondFragmentList, writerIndv2);
            writerOk.close();
            writerIndv1.close();
            writerIndv2.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        if (foundCount == 0)
            System.out.println("\nhmmm... did not find any ok pairs..." +
                    "did you make sure the Qualitas file path are consistent?");
        else
            System.out.println("\nhooray! found " + foundCount + " ok pairs.");
    }

    public static void readFirstFile(String file, String outFile) {
        System.out.println("Reading the first file: " + file);

        List<ReportedFragment> firstFileResult =
                IndvCloneFilter.getInstance().getClonePairs(file, minCloneSize);

        for (ReportedFragment rf: firstFileResult) {
//            addToFragmentMap(rf);
            addToFragmentList(rf);
        }
    }

    private static void readSecondFileAndCompare(String file, String outFile) {
        System.out.println("\nReading the second file: " + file);
        int foundCount = 0;

        try {
//            FileWriter fwriter = new FileWriter("good_" + outFile, false);
//            BufferedWriter bw = new BufferedWriter(fwriter);
//            PrintWriter writer = new PrintWriter(bw);

            FileWriter fwriterOk = new FileWriter("ok_" + outFile, false);
            BufferedWriter bwOk = new BufferedWriter(fwriterOk);
            PrintWriter writerOk = new PrintWriter(bwOk);

            FileWriter fwriterIndv1 = new FileWriter(
                    "indv_"
                            + tool1 + "_"
                            + settings1
                            + ending + ".csv", false);
            BufferedWriter bwIndv1 = new BufferedWriter(fwriterIndv1);
            PrintWriter writerIndv1 = new PrintWriter(bwIndv1);

            FileWriter fwriterIndv2 = new FileWriter(
                    "indv_"
                    + tool2 + "_"
                    + settings2
                    + ending + ".csv", false);
            BufferedWriter bwIndv2 = new BufferedWriter(fwriterIndv2);
            PrintWriter writerIndv2 = new PrintWriter(bwIndv2);

            List<ReportedFragment> secondFragmentList =
                    IndvCloneFilter.getInstance().getClonePairs(file, minCloneSize);

            ArrayList<OkPair> okPairList = new ArrayList<>();

            // sort the two list
            Collections.sort(firstFragmentList);
            Collections.sort(secondFragmentList);

            for (Iterator<ReportedFragment> iterator = secondFragmentList.iterator(); iterator.hasNext();) {
                ReportedFragment rf = iterator.next();
//                goodOverlapWithFragmentMap(rf, writer);
//                ArrayList<OkPair> okPairs = okOverlapWithFragmentList(rf, writerOk);
                boolean isFound = okOverlapWithFragmentList(rf, writerOk);
//                okPairList.addAll(okPairs);
                if (isFound) {
                    // remove fragment from the list
                    iterator.remove();
                    foundCount++;
                }
            }

//            ArrayList<OkPair> resulst = filterOkPairs(okPairList);
//            foundCount = resulst.size();
//
//            for (OkPair pair: resulst) {
//                writerOk.write(pair.toString() + "\n");
//            }

            writeFragmentListToFile(firstFragmentList, writerIndv1);
            writeFragmentListToFile(secondFragmentList, writerIndv2);

            writerOk.close();
            writerIndv1.close();
            writerIndv2.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        if (foundCount == 0)
            System.out.println("\nhmmm... did not find any ok pairs..." +
                    "did you make sure the Qualitas file path are consistent?");
        else
            System.out.println("\nhooray! found " + foundCount + " ok pairs.");

    }

    private static ArrayList<OkPair> filterOkPairs(ArrayList<OkPair> okPairList) {
	    int amount = 0;
	    double highestOkVal = 0;

	    OkPair bestPair = new OkPair();

	    ArrayList<OkPair> results = new ArrayList<>();
	    // sort the list first
        Collections.sort(okPairList);

        Fragment f = okPairList.get(0).getF1();
        for (OkPair pair: okPairList) {
            // if finding the same left-handed fragment,
            // keep moving down.
            if (f == pair.getF1())
                bestPair = pair;
            else {
                // when we already saw the next fragment
                // add the last seen one (highest ok-val).
                results.add(bestPair);

                f = pair.getF1();
            }
        }

        return results;
    }

    private static void writeFragmentListToFile(List<ReportedFragment> list, PrintWriter writer) {
	    for (ReportedFragment rf: list) {
            writer.print(rf.toString() + "\n");
        }
    }

    private static void addToFragmentMap(Fragment f) {
        String key = f.getFirstFile() + ":" + f.getSecondFile();
        ArrayList<Fragment> flist;
        // this pairs of these two fragments are never found before, create the first pair
        if (!fragmentMap.containsKey(key)) {
            flist = new ArrayList<>();
        } else { // already have a list, get the existing list
            flist = fragmentMap.get(key);
        }
        // add to the list
        flist.add(f);
        fragmentMap.put(key, flist);
    }

    private static void addToFragmentList(ReportedFragment f) {
        firstFragmentList.add(f);
    }

    private static String matchWithFragmentMap(Fragment f) {
        String key = f.getFirstFile() + ":" + f.getSecondFile();
        String returnStr = "";

        if (fragmentMap.containsKey(key)) {
            ArrayList<Fragment> flist = fragmentMap.get(key);
            for (Fragment frag: flist) {
                if (frag.isMatch(f)) {
                    // print out if it's a match
//                    System.out.println(f.toString() + ", " + frag.toString());
                    System.out.print("Good ");
                    returnStr += f.toString() + ", " + frag.toString() + "\n";
                }
            }
        }
        return returnStr;
    }

    private static String goodOverlapWithFragmentMap(Fragment f, PrintWriter writer) {
        String key = f.getFirstFile() + ":" + f.getSecondFile();
        String returnStr = "";

        if (fragmentMap.containsKey(key)) {
            ArrayList<Fragment> flist = fragmentMap.get(key);
            for (Fragment frag: flist) {
                double val = FragmentComparator.getGood(frag, f);
                if (val >= p) {
                    // print out if it's a match
//                    System.out.print("good ");
//                    System.out.println(val + "," + f.toString() + "," + frag.toString());
                    writer.print(f.toString() + "," + frag.toString() + "\n");
                    returnStr += f.toString() + "," + frag.toString() + "\n";
                }
            }
        }
        return returnStr;
    }

    private static boolean okOverlapWithFragmentMap(Fragment f, PrintWriter writer, PrintWriter indvWriter) {
        String key = f.getFirstFile() + ":" + f.getSecondFile();
        String returnStr = "";
        boolean found = false;

        if (fragmentMap.containsKey(key)) {
            ArrayList<Fragment> flist = fragmentMap.get(key);
            for (Fragment frag: flist) {
                double val = FragmentComparator.getOk(frag, f);
                if (val >= p) {
                    // print out if it's a match
                    writer.print(f.toString() + "," + frag.toString() + "\n");
                    found = true;
                }
                // not found, it's an individual clone pair
                else {
                    indvWriter.print(f.toString() + "," + frag.toString() + "\n");
                }
            }
        }
        return found;
    }

    private static boolean okOverlapWithFragmentList(Fragment f, PrintWriter writer) {

	    Fragment bestMatch = new Fragment();
	    double bestOkValue = 0;
	    boolean found = false;
        ArrayList<OkPair> okPairList = new ArrayList<>();

        for (Fragment frag: firstFragmentList) {
            // check if they're the same fragment pair
            if (frag.getFirstFile().equals(f.getFirstFile())
                    && frag.getSecondFile().equals(f.getSecondFile())) {
                // check ok-value
                double val = FragmentComparator.getOk(frag, f);
                // add all pairs what have ok-values >= threshold p
                if (val >= p) {
                    if (val > bestOkValue) {
                        bestMatch = frag;
                        bestOkValue = val;
                    }
//                    okPairList.add(new OkPair(frag, f));
                }

            }
        }

        if (bestOkValue != 0) {
            found = true;
            ReportedFragment rf = (ReportedFragment) bestMatch;
            if (!rf.getNotes().equals("")) {
                writer.print(f.toString() + "," + rf.toString() + "," + rf.getNotes() + "\n");
            } else {
                writer.print(f.toString() + "," + bestMatch.toString() + "\n");
            }
            System.out.println(f.toString() + "," + bestMatch.toString());
            // remove the fragment from the list
            firstFragmentList.remove(bestMatch);
        }

        return found;
    }
}
