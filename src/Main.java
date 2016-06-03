import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
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
	public static String basePath = "/Users/Chaiyong/Downloads";
	public static String pathToRemove = "/dev/shm/gbianco";
	public static String pathSo = "extracted_data";
	public static String pathQualitas = "QualitasCorpus-20130901r/compressed_src";
	public static void main(String[] args) {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		try {
			builder = factory.newDocumentBuilder();
			Document doc = builder.parse("/Users/Chaiyong/Documents/stackoverflow/results/simian_filtered_results/all.xml");
			XPathFactory xPathfactory = XPathFactory.newInstance();
			XPath xpath = xPathfactory.newXPath();
			
			BufferedReader br = null;
			try {
				String sCurrentLine;
				br = new BufferedReader(new FileReader("/Users/Chaiyong/Downloads/34tocheck.txt"));
				while ((sCurrentLine = br.readLine()) != null) {
					FileWriter fwriter = new FileWriter(basePath + "/" + sCurrentLine+".csv", true);
					BufferedWriter bw = new BufferedWriter(fwriter);
				    PrintWriter writer = new PrintWriter(bw);
				    ArrayList<String> pair = new ArrayList<String>();
					XPathExpression expr = xpath.compile(
							"//FRAGMENT_LOG[@filePath=\"/dev/shm/gbianco/extracted_data_full/" + sCurrentLine + "\"]");
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
								String soPath = "/dev/shm/gbianco/extracted_data_full/";
								String fragPath = x.getAttributes().item(1).getNodeValue();
								if (!fragPath.startsWith(soPath)) {
									System.out.println(x.getAttributes().item(2).getNodeValue() + ","
											+ x.getAttributes().item(0).getNodeValue() + "|" + n.getAttributes().item(2).getNodeValue() + ","
											+ n.getAttributes().item(0).getNodeValue());
									System.out.println("vim -c \":e "+n.getAttributes().item(1).getNodeValue().replace(pathToRemove + "/extracted_data_full", pathSo)+"|:"+n.getAttributes().item(2).getNodeValue()
											+"|:vsplit "+x.getAttributes()
											.item(1).getNodeValue().replace(pathToRemove + "/src", pathQualitas)+"|:"+x.getAttributes().item(2).getNodeValue()+"\"");
									
//									Scanner in = new Scanner(System.in);
//									System.out.println("TP/FP, Classification, Type, Notes: ");
//									String input = in.nextLine();
//									String input = "";
									writer.println(n.getAttributes().item(1).getNodeValue().replace(pathToRemove + "/extracted_data_full", pathSo) + "," 
											+ n.getAttributes().item(2).getNodeValue() + ","
											+ n.getAttributes().item(0).getNodeValue() + "," + x.getAttributes()
											.item(1).getNodeValue().replace(pathToRemove + "/src", pathQualitas) + "," 
											+ x.getAttributes().item(2).getNodeValue() + ","
											+ x.getAttributes().item(0).getNodeValue() /* + "," + input */);
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
			
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
	}

}
