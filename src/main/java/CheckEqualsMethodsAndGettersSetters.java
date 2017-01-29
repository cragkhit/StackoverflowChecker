import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by Chaiyong on 8/14/16.
 */
public class CheckEqualsMethodsAndGettersSetters {

    // public static String QPath = "QualitasCorpus-20130901r/projects_java_only_160816/";
    public static String QPath = "QualitasCorpus-20130901r/projects_130901r_pt1+2+3";
    public static String HOMEDIR = "/Users/Chaiyong";
    public static String projectLocation = HOMEDIR + "/IdeasProjects/";
    public static String SO_DIR = HOMEDIR + "/Downloads/stackoverflow/";

    public static void main(String[] args) {
        checkEqualsMethodsAndGettersSetters(projectLocation + "StackoverflowChecker/PLATINUM_FINAL_indv_nicad_df_130901_pt1+2+3+4.csv"
                // checkEqualsMethodsAndGettersSetters("/Users/Chaiyong/Desktop/a.csv"
                , 141, 181, 1
                , SO_DIR);
    }

    public static void checkEqualsMethodsAndGettersSetters(String file1, int start, int end, int so_starting_index, String path) {
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
                    boolean foundFirst = false, foundSecond = false, foundGetter = false, foundSetter = false;
                    boolean foundFirstGetter = false, foundSecondGetter = false, foundFirstHashCode = false, foundSecondHashCode = false;
                    String getterText = "";

                    sF = new BufferedReader(new FileReader(path + clone[so_starting_index]));
                    int lineCount = 0;
                    while ((sLine = sF.readLine()) != null) {
                        lineCount++;
                        // start line of SO fragment


                        if (lineCount == Integer.parseInt(clone[so_starting_index + 1].trim())) {
                            /*** CHECK EQUALS ***/
                            if (sLine.contains("boolean equals ("))
                                foundFirst = true;
                            else if (sLine.contains("@Override")) {
                                // System.out.println("found override,");
                                // read another line
                                sLine = sF.readLine();
                                if (sLine.contains("boolean equals (")) {
                                    // System.out.println("found equals");
                                    foundFirst = true;
                                } else if (sLine.contains("int hashCode()")) {
                                    foundFirstHashCode = true;
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
                            /*** Special case of equals */
                            else if (sLine.contains("this.name = name;")) {
                                // read another line
                                sLine = sF.readLine();
                                if (sLine.contains("}")) {
                                    // System.out.println("found equals");
                                    // read another line
                                    sLine = sF.readLine();
                                    if (sLine.contains("getId() {")) {
                                        // read another line
                                        sLine = sF.readLine();
                                        if (sLine.contains("return id;")) {
                                            foundFirstGetter = true;
                                        }
                                    }
                                }
                            }

                            /*** hashCode generated by IDE */
                            else if (sLine.matches("\\s*result\\s*=\\s*prime\\s*\\*\\s*result\\s*\\+\\s*\\(\\s*\\(\\s*[a-zA-z0-9]*\\s*==\\s*null\\s*\\)\\s*\\?\\s*0\\s*:\\s*[a-zA-Z0-9]*\\.hashCode\\(\\)\\s*\\);")) {
                                // read another line
                                sLine = sF.readLine();
                                if (sLine.contains("return result;")) {
                                    foundFirstHashCode = true;
                                }
                            } else if (sLine.contains("result = prime * result + ( ( name == null ) ? 0 : name.hashCode() );")) {
                                // read another line
                                sLine = sF.readLine();
                                if (sLine.contains("return result;")) {
                                    foundFirstHashCode = true;
                                }
                            }
                            /*** getter and setter */
                            else if (sLine.contains("this.id = id;")) {
                                // read another line
                                sLine = sF.readLine();
                                if (sLine.contains("}")) {
                                    foundFirstGetter = true;
                                }
                            }
                            // Getter that starts with get<Sth>
                            else if (sLine.matches("\\s*public\\s*String\\s*get[A-Z]?[a-zA-Z0-9]*\\(\\)\\s*\\{\\s*")) {
                                // read another line
                                sLine = sF.readLine();
                                if (sLine.matches("\\s*return\\s*[a-zA-Z0-9]*;")) {
                                    foundFirstGetter = true;
                                }

                            }
                            /*** CHECK getter ***/
                            else if (sLine.matches("\\s*public.*get[A-Z].*().*\\{")) {
                                // System.out.println("MATCHES:" + sLine);
                                foundGetter = true;
                                getterText = sLine;
                            } /*** CHECK setter ***/
                            else if (sLine.matches("\\s*return\\s*.*;.*")) {
                                // read another line
                                sLine = sF.readLine();
                                if (sLine.contains("}")) {
                                    // read another line
                                    sLine = sF.readLine();
                                    if (sLine.matches("\\s*public.*set[A-Z].*().*\\{")) {
                                        // System.out.println("MATCHES:" + sLine);
                                        foundSetter = true;
                                        getterText = sLine;
                                    }
                                }
                            }

                            break;
                        }
                    }
                    sF.close();

                    qF = new BufferedReader(new FileReader(path + "/" + QPath + "/" + clone[so_starting_index+3]));
                    lineCount = 0;
                    while ((qLine = qF.readLine()) != null) {
                        lineCount++;
                        // start line of SO fragment
                        if (lineCount == Integer.parseInt(clone[so_starting_index+4].trim())) {
                            if (qLine.contains("boolean equals ("))
                                foundSecond = true;
                            else if (qLine.contains("@Override")) {
                                // System.out.println("found override,");
                                // read another line
                                qLine = qF.readLine();
                                if (qLine.contains("boolean equals (")) {
                                    // System.out.println("found equals");
                                    foundSecond = true;
                                }/*** CHECK hashCode ***/
                                else if (qLine.contains("int hashCode()")) {
                                        foundSecondHashCode = true;
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

                            /*** hashCode generated by IDE */
                            // result = prime * result + ( ( id == null ) ? 0 : id.hashCode() );
                            else if (qLine.contains("result = prime * result + ( ( name == null ) ? 0 : name.hashCode() );")) {
                                // read another line
                                qLine = qF.readLine();
                                if (qLine.contains("return result;")) {
                                    foundSecondHashCode = true;
                                }
                            }
                            else if (qLine.matches("\\s*result\\s*=\\s*prime\\s*\\*\\s*result\\s*\\+\\s*\\(\\s*\\(\\s*[a-zA-z0-9]*\\s*==\\s*null\\s*\\)\\s*\\?\\s*0\\s*:\\s*[a-zA-Z0-9]*\\.hashCode\\(\\)\\s*\\);")) {
                                // read another line
                                qLine = qF.readLine();
                                if (qLine.contains("return result;")) {
                                    foundSecondHashCode = true;
                                }
                            }
                            /*** getter and setter */
                            else if (qLine.contains("this.id = id;")) {
                                // read another line
                                qLine = qF.readLine();
                                if (qLine.contains("}")) {
                                    foundSecondGetter = true;
                                }
                            }
                            // Getter that starts with get<Sth>
                            else if (qLine.matches("\\s*public\\s*String\\s*get[A-Z]?[a-zA-Z0-9]*\\(\\)\\s*\\{\\s*")) {
                                // read another line
                                qLine = qF.readLine();
                                if (qLine.matches("\\s*return\\s*[a-zA-Z0-9]*;")) {
                                    foundSecondGetter = true;
                                }

                            }
                            /*** Special case of equals */
                            else if (qLine.contains("this.name = name;")) {
                                // read another line
                                qLine = qF.readLine();
                                if (qLine.contains("}")) {
                                    // read another line
                                    qLine = qF.readLine();
                                    if (qLine.contains("getId() {")) {
                                        // read another line
                                        qLine = qF.readLine();
                                        if (qLine.contains("return id;")) {
                                            foundSecondGetter = true;
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
                    } else if (foundFirstGetter && foundSecondGetter) {
                        System.out.println(line + ",D,similar getters & setters methods");
                    } else if (foundGetter) {
                        System.out.println(line + ",F,accidentally similar getter");
                    } else if (foundSetter) {
                        System.out.println(line + ",F,accidentally similar setter");
                    } else if (foundFirstHashCode && foundSecondHashCode) {
                        System.out.println(line + ",F,accidentally similar hashCode() and equals()");
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
