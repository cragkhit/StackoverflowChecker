/**
 * Created by Chaiyong on 6/27/16.
 */
public class FragmentComparator {
    public static double getGood(Fragment cf1, Fragment cf2) {
        double[] overlap = cf1.getOverlap(cf2);
        // return the minimum overlap as 'good' measure
        return Math.min(overlap[0], overlap[1]);
    }

    public static double getOk(Fragment cf1, Fragment cf2) {
        double[] contained1 = cf1.getContained(cf2);
        double[] contained2 = cf2.getContained(cf1);
        // System.out.println(contained1[0] + ", " + contained1[1]);
        // System.out.println(contained2[0] + ", " + contained2[1]);
        return Math.min(Math.max(contained1[0],contained2[0]), Math.max(contained1[1], contained2[1]));
    }
}
