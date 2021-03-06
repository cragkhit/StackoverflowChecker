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
public class FindUniques {

    public static void main(String args[]) {
        String csvFile = "/Users/Chaiyong/Desktop/ok_pairs.csv";
        HashMap<String, String> cloneMap = new HashMap<>();
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";

        try {
            int count = 0;
            br = new BufferedReader(new FileReader(csvFile));
            while ((line = br.readLine()) != null) {
                count++;
                // use comma as separator
                String[] clone = line.split(cvsSplitBy);
                String key = "";
                for (int i = 2; i <= 13; i++) {
                    key += clone[i].trim();
                }

                // System.out.println(key);

                if (!cloneMap.containsKey(key)) {
                    cloneMap.put(key, line);
                } else {
                    System.out.println("dup," + line);
                }
            }

            System.out.println("line: " + count + ", size: " + cloneMap.size());

            Iterator it = cloneMap.entrySet().iterator();
            String[] setting = new String[2];

            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry)it.next();
                String dupClone = (String)pair.getValue();

                System.out.println(dupClone);
            }
            it.remove(); // avoids a ConcurrentModificationException
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
