import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by Chaiyong on 8/14/16.
 */
public class CheckEqualsMethodsAndGettersSetters {

    // public static String QPath = "QualitasCorpus-20130901r/projects_java_only_160816/";
    public static String QPath = "QualitasCorpus-20130901r/projects";
    public static void main(String[] args) {
        checkEqualsMethodsAndGettersSetters("/Users/Chaiyong/IdeasProjects/StackoverflowChecker/GOLD_indv_nicad_df_130901_checked_okpairs.csv", 1, -1, "/Users/Chaiyong/Downloads/stackoverflow/");
    }

    public static void checkEqualsMethodsAndGettersSetters(String file1, int start, int end, String path) {
        String cloneFile = file1;
        BufferedReader br = null;
        BufferedReader sF = null;
        BufferedReader qF = null;
        String line = "", sLine = "", qLine = "";
        String cvsSplitBy = ",";

        try {

            br = new BufferedReader(new FileReader(cloneFile));
            int count = 1;
            while ((line = br.readLine()) != null) {
                // use comma as separator
                if (end == -1 || (count >= start && count <= end)) {
                    String[] clone = line.split(cvsSplitBy);
                    boolean foundFirst = false, foundSecond = false, foundGetter = false;
                    String getterText = "";

                    sF = new BufferedReader(new FileReader(path + clone[0]));
                    int lineCount = 0;
                    while ((sLine = sF.readLine()) != null) {
                        lineCount++;
                        // start line of SO fragment

                        /*** CHECK EQUALS ***/
                        if (lineCount == Integer.parseInt(clone[1].trim())) {
                            if (sLine.contains("boolean equals ("))
                                foundFirst = true;
                            else if (sLine.contains("@Override")) {
                                // System.out.println("found override,");
                                // read another line
                                sLine = sF.readLine();
                                if (sLine.contains("boolean equals (")) {
                                    // System.out.println("found equals");
                                    foundFirst = true;
                                }
                            }
                            /*** Special case of equals
                             *
                             7     return result;
                             8 }
                             9 @Override
                             10 public boolean equals ( Object obj ) {
                             */
                            else if (sLine.contains("return result;")) {
                                // read another line
                                sLine = sF.readLine();
                                if (sLine.contains("}")) {
                                    // System.out.println("found equals");
                                    // read another line
                                    sLine = sF.readLine();
                                    if (sLine.contains("@Override")) {
                                        // read another line
                                        sLine = sF.readLine();
                                        if (sLine.contains("boolean equals (")) {
                                            foundFirst = true;
                                        }
                                    }
                                }
                            }
                            /*** CHECK getter/setter ***/
                            else if (sLine.matches("\\s*public.*get[A-Z].*().*\\{")) {
                                // System.out.println("MATCHES:" + sLine);
                                foundGetter = true;
                                getterText = sLine;
                            }
                            break;
                        }
                    }
                    sF.close();

                    qF = new BufferedReader(new FileReader(path + "/" + QPath + "/" + clone[3]));
                    lineCount = 0;
                    while ((qLine = qF.readLine()) != null) {
                        lineCount++;
                        // start line of SO fragment
                        if (lineCount == Integer.parseInt(clone[4].trim())) {
                            if (qLine.contains("boolean equals ("))
                                foundSecond = true;
                            else if (qLine.contains("@Override")) {
                                // System.out.println("found override,");
                                // read another line
                                qLine = qF.readLine();
                                if (qLine.contains("boolean equals (")) {
                                    // System.out.println("found equals");
                                    foundSecond = true;
                                }
                            }
                            /*** Special case of equals
                             *
                             7     return result;
                             8 }
                             9 @Override
                             10 public boolean equals ( Object obj ) {
                             */
                            else if (qLine.contains("return result;")) {
                                // read another line
                                qLine = qF.readLine();
                                if (qLine.contains("}")) {
                                    // System.out.println("found equals");
                                    // read another line
                                    qLine = qF.readLine();
                                    if (qLine.contains("@Override")) {
                                        // read another line
                                        qLine = qF.readLine();
                                        if (qLine.contains("boolean equals (")) {
                                            foundSecond = true;
                                        }
                                    }
                                }
                            }
                            break;
                        }
                    }
                    qF.close();

                    // both are equals() methods
                    if (foundFirst && foundSecond) {
                        System.out.println(line + ",D,similar equals() methods");
                    } else if (foundGetter) {
                        System.out.println(line + ",F,accidentally similar getter: " + getterText);
                    } else {
                        System.out.println(line);
                    }
                }
                count++;
            }
            br.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
