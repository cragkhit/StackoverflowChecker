import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;

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
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class IndvCloneFilter {
    public static String basePath = "/Users/Chaiyong/IdeasProjects/StackOverflowAnalyzer";
    public static String pathToRemove = "";
    public static String pathSo = "stackoverflow_formatted";
    public static String pathQualitas = "QualitasCorpus-20130901r";
    public static String tool = "simian";
    public static String settings = "df";
    public static String timestamp = "130901_rerun";

    public static void main(String[] args) {

        String outfilePath = basePath + "/indv_" + tool + "_" + settings + "_" + timestamp + ".csv";
        File outfile = new File(outfilePath);
        // delete if the output file exists
        try {
            Files.deleteIfExists(outfile.toPath());
            FileWriter fwriter = new FileWriter(outfilePath, true);
            BufferedWriter bw = new BufferedWriter(fwriter);
            PrintWriter writer = new PrintWriter(bw);

            // extract clone pairs
            ArrayList<ReportedFragment> extractedPairs = extractClonePairs(10);
            for (ReportedFragment f: extractedPairs) {
                writer.println(f.getFirstFile()
                        + "," + f.getfStart()
                        + "," + f.getfEnd()
                        + "," + f.getSecondFile()
                        + "," + f.getsStart()
                        + "," + f.getsEnd()
                        + "," + f.getMinCloneSize());
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /***
     * Extract clone pairs with a specified minimum clone lines
     * @param minSizeT number of lines to be considered as clones
     * @return list of clone pairs
     */
    public static ArrayList<ReportedFragment> extractClonePairs(int minSizeT) {

        // array lists to store so fragments and Q files that have been seen already
        ArrayList<ReportedFragment> selectedPairs = new ArrayList<ReportedFragment>();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
            Document doc = builder.parse(basePath + "/fragments_" + tool + "_"
                    + settings + "_" + timestamp + ".xml");
            XPathFactory xPathfactory = XPathFactory.newInstance();
            XPath xpath = xPathfactory.newXPath();

            BufferedReader br = null;
            try {
                String sCurrentLine;
                br = new BufferedReader(new FileReader(basePath + "/fragment_list_" + tool + "_"
                        + settings + "_" + timestamp + ".txt"));
                int count = 1;
                while ((sCurrentLine = br.readLine()) != null) {

                    System.out.println(count + ":" + sCurrentLine);
                    count++;

                    ArrayList<String> pair = new ArrayList<String>();
                    XPathExpression expr = xpath
                            .compile("//FRAGMENT_LOG[@filePath=\"stackoverflow_formatted/" + sCurrentLine + "\"]");
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
                                String soPath = "stackoverflow_formatted";
                                String fragPath = x.getAttributes().item(1).getNodeValue();

                                if (!fragPath.startsWith(soPath)) {
                                    int fStart = Integer.valueOf(n.getAttributes().item(2).getNodeValue());
                                    int fEnd = Integer.valueOf(n.getAttributes().item(0).getNodeValue());
                                    int sStart = Integer.valueOf(x.getAttributes().item(2).getNodeValue());
                                    int sEnd = Integer.valueOf(x.getAttributes().item(0).getNodeValue());

                                    int minCloneSize = Math.min((fEnd - fStart + 1), (sEnd - sStart + 1));

                                    if (minCloneSize >= minSizeT) {
                                        if (tool.equals("simian") || tool.equals("nicad")) {
                                            // only check different fragment or print again if we find higher minCloneSize cloned lines.
                                            ReportedFragment f = new ReportedFragment(
                                                    n.getAttributes().item(1).getNodeValue(),
                                                    fStart,
                                                    fEnd,
                                                    x.getAttributes().item(1).getNodeValue(),
                                                    sStart,
                                                    sEnd,
                                                    "");
                                            selectedPairs.add(f);
                                        } else
                                            System.out.println("wrong tool");
                                    }
                                }
                            }
                        }
                    }
                }
            } catch (IOException e) {
                System.out.println("Error: " + e.getMessage());
            }
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }

        return selectedPairs;
    }

    public static HashMap<String, ReportedFragment> checkDuplicates(boolean deleteDuplicate) {
        // a map to store selected pairs
        HashMap<String, ReportedFragment> selectedPairs = new HashMap<String, ReportedFragment>();
        // array lists to store so fragments and Q files that have been seen
        // already
        ArrayList<String> soArr = new ArrayList<>();
        ArrayList<String> qArr = new ArrayList<>();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
            Document doc = builder.parse(basePath + "/fragments_" + tool + "_"
                    + settings + "_" + timestamp + ".xml");
            XPathFactory xPathfactory = XPathFactory.newInstance();
            XPath xpath = xPathfactory.newXPath();

            BufferedReader br = null;
            try {
                String sCurrentLine;
                br = new BufferedReader(new FileReader(basePath + "/" + tool + "_"
                        + settings + "_indv_check_" + timestamp + ".txt"));
                int count = 1;
                while ((sCurrentLine = br.readLine()) != null) {

                    System.out.println(count + ":" + sCurrentLine);
                    count++;

                    ArrayList<String> pair = new ArrayList<String>();
                    XPathExpression expr = xpath
                            .compile("//FRAGMENT_LOG[@filePath=\"stackoverflow_formatted/" + sCurrentLine + "\"]");
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
                                String soPath = "stackoverflow_formatted";
                                String fragPath = x.getAttributes().item(1).getNodeValue();

                                if (!fragPath.startsWith(soPath)) {
                                    int fStart = Integer.valueOf(n.getAttributes().item(2).getNodeValue());
                                    int fEnd = Integer.valueOf(n.getAttributes().item(0).getNodeValue());
                                    int sStart = Integer.valueOf(x.getAttributes().item(2).getNodeValue());
                                    int sEnd = Integer.valueOf(x.getAttributes().item(0).getNodeValue());

                                    int minCloneSize = Math.min((fEnd - fStart + 1), (sEnd - sStart + 1));
                                    double threshold = 10;

                                    if (minCloneSize >= threshold) {
                                        if (tool.equals("simian") || tool.equals("nicad")) {
                                            // only check different fragment or print again if we find higher minCloneSize cloned lines.
                                            String key = n.getAttributes().item(1).getNodeValue() + ","
                                                    + x.getAttributes().item(1).getNodeValue();
                                            ReportedFragment f = new ReportedFragment(n.getAttributes().item(1).getNodeValue(), fStart,
                                                    fEnd, x.getAttributes().item(1).getNodeValue(), sStart, sEnd, "");

                                            // never has this pair before and also never seen this SO or Q before
                                            // if (!selectedPairs.containsKey(key)) {
                                            //		if (!soArr.contains(n.getAttributes().item(1).getNodeValue())
                                            //				&& !qArr.contains(x.getAttributes().item(1).getNodeValue())) {
                                            selectedPairs.put(key, f);
                                            soArr.add(n.getAttributes().item(1).getNodeValue());
                                            qArr.add(x.getAttributes().item(1).getNodeValue());
                                            //		}
                                            // } else {
                                            //	// check if the size is larger
//												ReportedFragment existingF = selectedPairs.get(key);
//												if (minCloneSize > existingF.getMinCloneSize()) {
//													// if larger, insert the new one
//													selectedPairs.put(key, f);
//												}
//											}
                                        } else
                                            System.out.println("wrong tool");
                                    }
                                }
                            }
                        }
                    }
                }
            } catch (IOException e) {
                System.out.println("Error: " + e.getMessage());
            }
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
        return selectedPairs;
    }
}
