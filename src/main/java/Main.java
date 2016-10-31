import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
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
    private static String settings1 = "fse13";

    // choose mode between old, good, ok
    // private static String mode = "ok";
    private static double p = 0.7;

    private static String tool2 = "nicad";
    private static String settings2 = "fse13";
    private static String ending = "_130901_pt2";

    private static HashMap<String, ArrayList<Fragment>> fragmentMap = new HashMap<>();

	public static void main(String[] args) {
        readFirstFile("/Users/Chaiyong/IdeasProjects/StackAnalyzer/fragments_" + tool1 + "_" + settings1 + ending + "-3.xml"
                , "/Users/Chaiyong/IdeasProjects/StackAnalyzer/fragment_list_" + tool1 + "_" + settings1 + ".txt"
                , tool1 + "_fragments_pairs_" + settings1 + ".csv");

        readSecondFileAndCompare("/Users/Chaiyong/IdeasProjects/StackAnalyzer/fragments_" + tool2 + "_" + settings2 + ending + ".xml"
                , "/Users/Chaiyong/IdeasProjects/StackAnalyzer/fragment_list_" + tool2 + "_" + settings2 + ending + ".txt"
                , "common_pairs_" + tool1 + settings1 + "-" + tool2 + settings2 + "-" + p + ending + ".csv");
	}

    public static void readFirstFile(String file, String fragListFile, String outFile) {
        System.out.println("Reading the first file: " + file);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
            Document doc = builder.parse(file);
            XPathFactory xPathfactory = XPathFactory.newInstance();
            XPath xpath = xPathfactory.newXPath();

            BufferedReader br = null;
            try {
                String sCurrentLine;
                int lineCount = 1;
                br = new BufferedReader(new FileReader(fragListFile));
                while ((sCurrentLine = br.readLine()) != null) {

                    System.out.print(lineCount + ", ");
                    lineCount++;

                    FileWriter fwriter = new FileWriter(outFile, true);
                    BufferedWriter bw = new BufferedWriter(fwriter);
                    PrintWriter writer = new PrintWriter(bw);

                    ArrayList<String> pair = new ArrayList<String>();
                    XPathExpression expr = xpath.compile(
                            "//FRAGMENT_LOG[@filePath=\"stackoverflow_formatted/" + sCurrentLine + "\"]");
                    NodeList nl = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
                    for (int i = 0; i < nl.getLength(); i++) {
                        Node n = nl.item(i);

                        Node parent = n.getParentNode();
                        // get all the child nodes
                        NodeList children = parent.getChildNodes();
                        for (int j = 1; j < children.getLength(); j += 2) {
                            // System.out.println(j);
                            Node x = children.item(j);
                            if (x.getNodeName() != null) {
                                String soPath = pathSo;
                                String fragPath = x.getAttributes().item(1).getNodeValue();
                                if (!fragPath.startsWith(soPath)) {
                                    writer.println(n.getAttributes().item(1).getNodeValue() + ","
                                            + n.getAttributes().item(2).getNodeValue() + ","
                                            + n.getAttributes().item(0).getNodeValue() + "," + x.getAttributes()
                                            .item(1).getNodeValue() + ","
                                            + x.getAttributes().item(2).getNodeValue() + ","
                                            + x.getAttributes().item(0).getNodeValue() /* + "," + input */);

                                    // create a new fragment
                                    Fragment f = new Fragment(n.getAttributes().item(1).getNodeValue()
                                            , Integer.valueOf(n.getAttributes().item(2).getNodeValue())
                                            , Integer.valueOf(n.getAttributes().item(0).getNodeValue())
                                            , x.getAttributes().item(1).getNodeValue()
                                            , Integer.valueOf(x.getAttributes().item(2).getNodeValue())
                                            , Integer.valueOf(x.getAttributes().item(0).getNodeValue()));

                                    // add to the map
                                    addToFragmentMap(f);
                                }
                            }
                            // System.out.println(x.getAttributes().getNamedItem("filePath"));
                        }
                    }

                    writer.close();
                }
            } catch (IOException e) {
                System.out.println("Error: " + e.getMessage());
            }
            // System.out.println("Map size: " + fragmentMap.size());
        } catch (ParserConfigurationException | SAXException | XPathExpressionException | IOException e) {
            e.printStackTrace();
        }
    }

    private static void readSecondFileAndCompare(String file, String fragListFile, String outFile) {
        System.out.println("\nReading the second file: " + file);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
            Document doc = builder.parse(file);
            XPathFactory xPathfactory = XPathFactory.newInstance();
            XPath xpath = xPathfactory.newXPath();

            BufferedReader br = null;
            try {
                String sCurrentLine;
                int lineCount = 1;
                br = new BufferedReader(new FileReader(fragListFile));
                while ((sCurrentLine = br.readLine()) != null) {

                    System.out.print(lineCount + ", ");
                    lineCount++;

                    FileWriter fwriter = new FileWriter("good_" + outFile, true);
                    BufferedWriter bw = new BufferedWriter(fwriter);
                    PrintWriter writer = new PrintWriter(bw);

                    FileWriter fwriterOk = new FileWriter("ok_" + outFile, true);
                    BufferedWriter bwOk = new BufferedWriter(fwriterOk);
                    PrintWriter writerOk = new PrintWriter(bwOk);

                    ArrayList<String> pair = new ArrayList<String>();
                    XPathExpression expr = xpath.compile(
                            "//FRAGMENT_LOG[@filePath=\"stackoverflow_formatted/" + sCurrentLine + "\"]");
                    NodeList nl = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
                    for (int i = 0; i < nl.getLength(); i++) {
                        Node n = nl.item(i);

                        Node parent = n.getParentNode();
                        // get all the child nodes
                        NodeList children = parent.getChildNodes();
                        for (int j = 1; j < children.getLength(); j += 2) {
                            // System.out.println(j);
                            Node x = children.item(j);
                            if (x.getNodeName() != null) {
                                String soPath = pathSo;
                                String fragPath = x.getAttributes().item(1).getNodeValue();
                                if (!fragPath.startsWith(soPath)) {
                                    // create a new fragment
                                    Fragment f = new Fragment(n.getAttributes().item(1).getNodeValue()
                                            , Integer.valueOf(n.getAttributes().item(2).getNodeValue())
                                            , Integer.valueOf(n.getAttributes().item(0).getNodeValue())
                                            , x.getAttributes().item(1).getNodeValue()
                                            , Integer.valueOf(x.getAttributes().item(2).getNodeValue())
                                            , Integer.valueOf(x.getAttributes().item(0).getNodeValue()));

                                    // add to the map
                                    // if (mode.equals("old"))
                                    //    writer.print(matchWithFragmentMap(f));
                                    // else if (mode.equals("good"))
                                        writer.print(goodOverlapWithFragmentMap(f));
                                    // else if (mode.equals("ok"))
                                        writerOk.print(okOverlapWithFragmentMap(f));
                                    // else
                                    //    System.out.println("Wrong mode!");
                                }
                            }
                            // System.out.println(x.getAttributes().getNamedItem("filePath"));
                        }
                    }

                    writer.close();
                    writerOk.close();
                }
            } catch (IOException e) {
                System.out.println("Error: " + e.getMessage());
            }
            // System.out.println("Map size: " + fragmentMap.size());
        } catch (ParserConfigurationException | SAXException | XPathExpressionException | IOException e) {
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
                    System.out.println(f.toString() + ", " + frag.toString());
                    returnStr += f.toString() + ", " + frag.toString() + "\n";
                }
            }
        }
        return returnStr;
    }

    private static String goodOverlapWithFragmentMap(Fragment f) {
        String key = f.getFirstFile() + ":" + f.getSecondFile();
        String returnStr = "";

        if (fragmentMap.containsKey(key)) {
            ArrayList<Fragment> flist = fragmentMap.get(key);
            for (Fragment frag: flist) {
                double val = FragmentComparator.getGood(frag, f);
                if (val >= p) {
                    // print out if it's a match
                    System.out.println(val + "," + f.toString() + ", " + frag.toString());
                    returnStr += val + "," + f.toString() + ", " + frag.toString() + "\n";
                }
            }
        }
        return returnStr;
    }

    private static String okOverlapWithFragmentMap(Fragment f) {
        String key = f.getFirstFile() + ":" + f.getSecondFile();
        String returnStr = "";

        if (fragmentMap.containsKey(key)) {
            ArrayList<Fragment> flist = fragmentMap.get(key);
            for (Fragment frag: flist) {
                double val = FragmentComparator.getOk(frag, f);
                if (val >= p) {
                    // print out if it's a match
                    System.out.println(val + "," + f.toString() + ", " + frag.toString());
                    returnStr += val + "," + f.toString() + ", " + frag.toString() + "\n";
                }
            }
        }
        return returnStr;
    }
}
