package exec;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.*;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Chaiyong on 8/14/16.
 */
public class FindDuplicates {

    public static void main(String args[]) {
        String csvFile = "/Users/Chaiyong/IdeasProjects/StackoverflowChecker/ok_160814.csv";
        HashMap<String, ArrayList<String>> cloneMap = new HashMap<>();
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";

        int sdndsdnf = 0;
        int sfndsfnf = 0;
        int sdnfsfnf = 0;

        try {

            br = new BufferedReader(new FileReader(csvFile));
            while ((line = br.readLine()) != null) {

                // use comma as separator
                String[] clone = line.split(cvsSplitBy);
                String key = "";
                for (int i = 2; i < clone.length; i++) {
                    key += clone[i].trim();
                }

                // System.out.println(key);

                if (!cloneMap.containsKey(key)) {
                    ArrayList<String> dupClones = new ArrayList<>();
                    dupClones.add(line);
                    cloneMap.put(key, dupClones);
                } else {
                    // System.out.println(line);
                    ArrayList<String> dupClone = cloneMap.get(key);
                    dupClone.add(line);
                }
            }

            Iterator it = cloneMap.entrySet().iterator();
            String[] setting = new String[2];

            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry)it.next();
                // System.out.println(pair.getKey() + " = " + pair.getValue());
                ArrayList<String> dupClone = (ArrayList<String>)pair.getValue();
                if (dupClone.size() > 1) {
                    // System.out.println("*** " + pair.getKey().toString());
                    int count = 0;
                    for (String c: dupClone) {
                        System.out.println(c);
                        String[] clone = c.split(cvsSplitBy);
                        setting[count] = clone[0];
                        count++;
                    }

                    if (setting[0].equals("sdnd") && setting[1].equals("sdnf"))
                        sdndsdnf++;
                    else if (setting[0].equals("sfnd") && setting[1].equals("sfnf"))
                        sfndsfnf++;
                    else if (setting[0].equals("sdnf") && setting[1].equals("sfnf"))
                        sdnfsfnf++;
                    else
                        System.out.println("Found sth else!!!");

                    System.out.println();
                }
                it.remove(); // avoids a ConcurrentModificationException
            }

            System.out.println("sdnd-sdnf: " + sdndsdnf);
            System.out.println("sfnd-sfnf: " + sfndsfnf);
            System.out.println("sdnf-sfnf: " + sdnfsfnf);

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
