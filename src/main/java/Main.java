import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;

public class Main {

	// public static String basePath = "";
	// private static String pathToRemove = "/dev/shm/gbianco";
	private static String pathSo = "stackoverflow_formatted/";
	// private static String pathQualitas = "QualitasCorpus-20130901r/compressed_src";

    private static String tool1 = "simian";
    private static String settings1 = "df";

    // choose mode between old, good, ok
    // private static String mode = "ok";
    private static double p = 0.7;

    private static String tool2 = "scc";
    private static String settings2 = "df";
    private static String ending = "_130901_pt1+2+3+4";

    private static int minCloneSize = 10;

    private static HashMap<String, ArrayList<Fragment>> fragmentMap = new HashMap<>();

	public static void main(String[] args) {
        readFirstFile("/Users/Chaiyong/IdeasProjects/StackOverflowAnalyzer/fragments_" + tool1 + "_" + settings1 + ending + ".xml"
                , "/Users/Chaiyong/IdeasProjects/StackOverflowAnalyzer/fragment_list_" + tool1 + "_" + settings1 + ending + ".txt"
                , tool1 + "_fragments_pairs_" + settings1 + ".csv");

        readSecondFileAndCompare("/Users/Chaiyong/IdeasProjects/StackOverflowAnalyzer/fragments_" + tool2 + "_" + settings2 + ending + ".xml"
                , "/Users/Chaiyong/IdeasProjects/StackOverflowAnalyzer/fragment_list_" + tool2 + "_" + settings2 + ending + ".txt"
                , "common_pairs_" + tool1 + settings1 + "-" + tool2 + settings2 + "-" + p + ending + ".csv");
	}

    public static void readFirstFile(String file, String fragListFile, String outFile) {
        System.out.println("Reading the first file: " + file);

        List<ReportedFragment> result =
                IndvCloneFilter.getInstance().getClonePairs(file, minCloneSize);

        for (ReportedFragment rf: result) {
            addToFragmentMap(rf);
        }
    }

    private static void readSecondFileAndCompare(String file, String fragListFile, String outFile) {
        System.out.println("\nReading the second file: " + file);

        try {
            FileWriter fwriter = new FileWriter("good_" + outFile, true);
            BufferedWriter bw = new BufferedWriter(fwriter);
            PrintWriter writer = new PrintWriter(bw);

            FileWriter fwriterOk = new FileWriter("ok_" + outFile, true);
            BufferedWriter bwOk = new BufferedWriter(fwriterOk);
            PrintWriter writerOk = new PrintWriter(bwOk);

            List<ReportedFragment> result =
                    IndvCloneFilter.getInstance().getClonePairs(file, minCloneSize);

            for (ReportedFragment rf : result) {
                goodOverlapWithFragmentMap(rf, writer);
                okOverlapWithFragmentMap(rf, writerOk);
            }

            writer.close();
            writerOk.close();

        } catch (IOException e) {
            e.printStackTrace();
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
                    System.out.println(val + "," + f.toString() + "," + frag.toString());
                    writer.print(val + "," + f.toString() + "," + frag.toString() + "\n");
                    returnStr += val + "," + f.toString() + "," + frag.toString() + "\n";
                }
            }
        }
        return returnStr;
    }

    private static String okOverlapWithFragmentMap(Fragment f, PrintWriter writer) {
        String key = f.getFirstFile() + ":" + f.getSecondFile();
        String returnStr = "";

        if (fragmentMap.containsKey(key)) {
            ArrayList<Fragment> flist = fragmentMap.get(key);
            for (Fragment frag: flist) {
                double val = FragmentComparator.getOk(frag, f);
                if (val >= p) {
                    // print out if it's a match
                    System.out.println(val + "," + f.toString() + "," + frag.toString());
                    writer.print(val + "," + f.toString() + "," + frag.toString() + "\n");
                    returnStr += val + "," + f.toString() + "," + frag.toString() + "\n";
                }
            }
        }
        return returnStr;
    }
}
