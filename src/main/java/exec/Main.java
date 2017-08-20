package exec;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import data.Fragment;
import data.FragmentComparator;
import data.ReportedFragment;

import java.io.BufferedWriter;
import java.io.FileWriter;

public class Main {

	// public static String basePath = "";
	// private static String pathToRemove = "/dev/shm/gbianco";
	private static String pathSo = "stackoverflow_formatted/";
	// private static String pathQualitas = "QualitasCorpus-20130901r/compressed_src";

    private static String tool1 = "scc";
    private static String settings1 = "df";

    // choose mode between old, good, ok
    // private static String mode = "ok";
    private static double p = 0.7;

    private static String tool2 = "simian";
    private static String settings2 = "df";
    private static String ending = "_130817";

    private static int minCloneSize = 10;

    private static HashMap<String, ArrayList<Fragment>> fragmentMap = new HashMap<>();
    private static ArrayList<ReportedFragment> firstFragmentList = new ArrayList<>();

	public static void main(String[] args) {
        readFirstFile("/Users/Chaiyong/IdeasProjects/StackOverflowAnalyzer/results/fragments_"
                        + tool1 + "_" + settings1 + ending + ".xml"
                , tool1 + "_fragments_pairs_" + settings1 + ".csv");

        readSecondFileAndCompare("/Users/Chaiyong/IdeasProjects/StackOverflowAnalyzer/results/fragments_"
                        + tool2 + "_" + settings2 + ending + ".xml"
                , "common_pairs_" + tool2 + "_" + settings2 + "-"
                        + tool1 + "_" + settings1 + "-" + p + ending + ".csv");
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

            for (Iterator<ReportedFragment> iterator = secondFragmentList.iterator(); iterator.hasNext();) {
                ReportedFragment rf = iterator.next();
//                goodOverlapWithFragmentMap(rf, writer);
                boolean found = okOverlapWithFragmentList(rf, writerOk);
                if (found) {
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

        for (Fragment frag: firstFragmentList) {
            // check if they're the same fragment pair
            if (frag.getFirstFile().equals(f.getFirstFile())
                    && frag.getSecondFile().equals(f.getSecondFile())) {
                // check ok-value
                double val = FragmentComparator.getOk(frag, f);
                if (val >= p) {
                    if (val > bestOkValue) {
                        bestMatch = frag;
                        bestOkValue = val;
                    }
                }
            }
        }

        if (bestOkValue != 0) {
            found = true;
            writer.print(f.toString() + "," + bestMatch.toString() + "\n");
            System.out.println(f.toString() + "," + bestMatch.toString());
            // remove the fragment from the list
            firstFragmentList.remove(bestMatch);
        }

        return found;
    }
}
