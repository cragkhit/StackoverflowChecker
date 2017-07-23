import java.io.IOException;
import java.util.*;

/**
 * Created by GiusWhite on 15/03/2016.
 */
public class SimianLog {
    public int id;
    public List<LogFragment> fragmentList;
    private static HashMap<String, Integer> projectMap;

    public SimianLog() {
        this.fragmentList = new ArrayList<>();
        this.projectMap = new HashMap<String, Integer>();
    }

    public LogFragment getEmptyFragment() {
        this.fragmentList.add(new LogFragment());
        return this.fragmentList.get(this.fragmentList.size() - 1);
    }

    public boolean isUseful() {
        boolean result = false;
        boolean thereIsStackOverflowFile = false;
        boolean thereIsSrcFile = false;
        for (LogFragment logFragment : this.fragmentList) {
            if (logFragment.getFilePath().contains(CommonUtils.stackoverflow_path)) {
                thereIsStackOverflowFile = true;
            } else if (logFragment.getFilePath().contains(CommonUtils.qualitas_path)) {
                thereIsSrcFile = true;
            }
        }
        result = thereIsSrcFile && thereIsStackOverflowFile;

        return result;
    }

    @Override
    public String toString() {
        return "SimianLog{" +
                "id=" + id +
                ", fragmentList=" + fragmentList +
                '}';
    }

    public static List<ReportedFragment> filterSimianClones(List<SimianLog> simianLogs, int minCloneSize) {
        HashMap<String, SimianStackoverflowFragment> result = new HashMap<>();
        ArrayList<ReportedFragment> selectedPairs = new ArrayList<ReportedFragment>();

        System.out.println("Reading from simian fragments ...");
        int count = 1;
        int pairCount = 0;

        for (SimianLog s : simianLogs) {
            System.out.println(count + "/" + simianLogs.size());
            count++;
            for (LogFragment lF : s.fragmentList) {
                /* Check if it exists in the result list.*/
                if (lF.isStackoverflowFragment) {
                    boolean exist = false;
                    SimianStackoverflowFragment simianStackoverflowFragment = new SimianStackoverflowFragment();

                    if (result.containsKey(lF.filePath)) {
                        // exist = true;
                        simianStackoverflowFragment = result.get(lF.filePath);
                        // even it exists, still need to update clone lines anyway
                        simianStackoverflowFragment.setCloneLines(lF.getStart(), lF.getEnd());
                    } else {
                        /* If it is not in the list, add it */
                        simianStackoverflowFragment.fragmentName = lF.filePath;
                        try {
                            simianStackoverflowFragment.setSLOC(lF.filePath);
                            simianStackoverflowFragment.setCloneLines(lF.getStart(), lF.getEnd());
                        } catch (IOException e) {
                            System.err.println("Error: can't find the fragment file.");
                            e.printStackTrace();
                        }
                        result.put(lF.filePath, simianStackoverflowFragment);
                    }

                    // add the projects that use this fragment
                    for (LogFragment lF2 : s.fragmentList) {
                        if (!lF2.isStackoverflowFragment) {
                            /* COLLECT THE NUMBER OF QUALITAS PROJECTS HAVING CLONES */

                            // check if not the 11 erroneous pairs
                            if (!CommonUtils.isError(lF.filePath)) {
                                pairCount++;
                                ReportedFragment f = new ReportedFragment(
                                        lF.filePath,
                                        lF.getStart(),
                                        lF.getEnd(),
                                        lF2.filePath,
                                        lF2.getStart(),
                                        lF2.getEnd(),
                                        "");

                                // add only when their clone size is at least the minimum clone size
                                if (f.getMinCloneSize() >= minCloneSize)
                                    selectedPairs.add(f);
                            }
                        }
                    }
                }
            }

        }
        System.out.println("No. of pairs: " + pairCount);
        return selectedPairs;
    }

    public static HashMap<String, Integer> getProjectMap() {
        return projectMap;
    }

    public static List<SimianStackoverflowFragment> sortByUsage(List<SimianStackoverflowFragment> list) {
        List<SimianStackoverflowFragment> result = list;
        Collections.sort(result, new SimianLogComparator(SimianLogComparator.USAGE));
        return result;
    }

    public static List<SimianStackoverflowFragment> sortByProjects(List<SimianStackoverflowFragment> list) {
        List<SimianStackoverflowFragment> result = list;
        Collections.sort(result, new SimianLogComparator(SimianLogComparator.PROJECTS));
        return result;
    }

    public static Map<Object, Object> getDistributionBy(List<SimianStackoverflowFragment> list, int by) {
        Map<Object, Object> result = new HashMap<>();
        for (SimianStackoverflowFragment s : list) {
            if (by == SimianLogComparator.PROJECTS) {
                int projectsNumber = s.projectsWhereIsUsed.size();
                if (result.containsKey(projectsNumber)) {
                    int current = (int) result.get(projectsNumber);
                    result.put(projectsNumber, current + 1);
                } else {
                    result.put(projectsNumber, 1);
                }
            } else {
                int projectsNumber = s.numberOfTimeIsUsed;
                if (result.containsKey(projectsNumber)) {
                    int current = (int) result.get(projectsNumber);
                    result.put(projectsNumber, current + 1);
                } else {
                    result.put(projectsNumber, 1);
                }
            }

        }
        return result;
    }

    public static Map<Object, Object> getSimianLogLOCStats(List<SimianLog> simianLogs) {
        Map<Object, Object> result = new HashMap<>();
        for (SimianLog s : simianLogs) {
            for (LogFragment lF : s.fragmentList) {
                if (lF.isStackoverflowFragment) {
                    if (result.containsKey(lF.getNumberOfUsedLOC())) {
                        int current = (int) result.get(lF.getNumberOfUsedLOC());
                        result.put(lF.getNumberOfUsedLOC(), current + 1);
                    } else {
                        result.put(lF.getNumberOfUsedLOC(), 1);
                    }
                }
            }

        }
        return result;
    }

    public static Map<Object, Object> getSimianLogProjectsStats(List<SimianLog> simianLogs) {
        Map<Object, Object> result = new HashMap<>();
        for (SimianLog s : simianLogs) {
            for (LogFragment lF : s.fragmentList) {
                if (!lF.isStackoverflowFragment) {
                    if (result.containsKey(lF.filePath)) {
                        int current = (int) result.get(lF.filePath);
                        result.put(lF.filePath, current + 1);
                    } else {
                        result.put(lF.filePath, 1);
                    }
                }
            }

        }
        return result;
    }

    public class LogFragment {
        public String filePath;
        public int start;
        public int end;
        public boolean isStackoverflowFragment;

        public LogFragment() {
        }

        public String getFilePath() {
            return filePath;
        }

        public void setFilePath(String filePath) {
            this.filePath = filePath;
        }

        public int getStart() {
            return start;
        }

        public void setStart(int start) {
            this.start = start;
        }

        public int getEnd() {
            return end;
        }

        public void setEnd(int end) {
            this.end = end;
        }

        public int getNumberOfUsedLOC() {
            return this.end - this.start + 1;
        }

        @Override
        public String toString() {
            return "LogFragment{" +
                    "filePath='" + filePath + '\'' +
                    ", start=" + start +
                    ", end=" + end +
                    '}';
        }
    }
}
