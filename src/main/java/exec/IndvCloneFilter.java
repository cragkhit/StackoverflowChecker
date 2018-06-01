package exec;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import javax.xml.parsers.*;

import data.ReportedFragment;
import data.SimianLog;
import data.UsefulSimianFragmentHandler;
import exception.SaxTerminatorException;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.List;

public class IndvCloneFilter {
    public static String basePath = "/Users/Chaiyong/IdeasProjects/StackOverflowAnalyzer/results";
    public static String pathToRemove = "";
    public static String pathSo = "stackoverflow_formatted";
    public static String pathQualitas = "QualitasCorpus-20130901r";
    public static String tool = "scc";
    public static String settings = "df";
    public static String timestamp = "130817";
    public static int NUMBER_OF_POSTS = 0;
    public static String outfilePath = basePath + "/indv_" + tool + "_" + settings + "_" + timestamp + ".csv";
    public static int minCloneSize = 10;

    private static IndvCloneFilter instance = null;

    private SAXParser saxParser;
    private DefaultHandler handler;

    public IndvCloneFilter() {
        try {
            SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
            this.saxParser = saxParserFactory.newSAXParser();
        } catch (ParserConfigurationException | SAXException e) {
            e.printStackTrace();
        }
    }

    public static IndvCloneFilter getInstance() {
        if (instance == null) {
            instance = new IndvCloneFilter();
        }
        return instance;
    }

    public static void main(String[] args) {

        File outfile = new File(outfilePath);

        try {
            Files.deleteIfExists(outfile.toPath());
            FileWriter fwriter = new FileWriter(outfilePath, true);
            BufferedWriter bw = new BufferedWriter(fwriter);
            PrintWriter writer = new PrintWriter(bw);

            List<ReportedFragment> result =
                    IndvCloneFilter.getInstance().getClonePairs(
                            basePath + "/fragments_" + tool + "_"
                                    + settings + "_" + timestamp + ".xml", minCloneSize);

            System.out.println("No. of filtered pairs: " + result.size());

            for (ReportedFragment f : result) {
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

    public List<ReportedFragment> getClonePairs(String file, int minCloneSize) {
        List<ReportedFragment> result = new ArrayList<>();
        try {
            List<SimianLog> simianLogs = new ArrayList<>();
            File inputFile = new File(file);
            System.out.println("File: " + inputFile.getName());
            this.handler = new UsefulSimianFragmentHandler(simianLogs);
            this.saxParser.parse(inputFile, handler);
            result = SimianLog.filterSimianClones(simianLogs, minCloneSize);
            System.out.println("Log size: " + result.size());
        } catch (SAXException | IOException e) {
            e.printStackTrace();
        } catch (SaxTerminatorException allDone) {
            System.out.println("TERMINATED");
        }

        return result;
    }

    public List<ReportedFragment> getCSVClonePairs(String file, int minCloneSize) {
        List<ReportedFragment> result = new ArrayList<>();
        File inputFile = new File(file);
        System.out.println("File: " + inputFile.getName());

        try (
                Reader reader = Files.newBufferedReader(Paths.get(file));
                CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT);
        ) {
            for (CSVRecord csvRecord : csvParser) {
                // Accessing Values by Column Index
                String sofile = csvRecord.get(0);
                int sostart = Integer.parseInt(csvRecord.get(1));
                int soend = Integer.parseInt(csvRecord.get(2));
                String qfile = csvRecord.get(3);
                int qstart = Integer.parseInt(csvRecord.get(4));
                int qend = Integer.parseInt(csvRecord.get(5));
                ReportedFragment rf = new ReportedFragment(sofile, sostart, soend, qfile, qstart, qend, "");
                result.add(rf);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
