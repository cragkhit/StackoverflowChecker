import static org.junit.Assert.*;
/**
 * Created by Chaiyong on 6/27/16.
 */
public class FragmentComparatorTest {
    @org.junit.Test
    public void checkGood() throws Exception {
        Fragment cf1 = new Fragment("A.java", 1, 10, "B.java", 5, 20);
        Fragment cf2 = new Fragment("A.java", 1, 10, "B.java", 5, 20);
        double good = Math.min(10/(double)10, 16/(double)16);
        assertEquals(FragmentComparator.getGood(cf1,cf2), good, 0);

        double[] overlap = new double[2];
        Fragment cf3 = new Fragment("A.java", 1, 10, "B.java", 5, 20);
        Fragment cf4 = new Fragment("A.java", 5, 15, "B.java", 10, 25);
        overlap[0] = (double)(5+1)/15;
        overlap[1] = (double)(10+1)/21;
        assertEquals(FragmentComparator.getGood(cf3,cf4), Math.min(overlap[0], overlap[1]), 0);

        Fragment cf5 = new Fragment("A.java", 1, 10, "B.java", 5, 20);
        Fragment cf6 = new Fragment("A.java", 5, 10, "B.java", 10, 20);
        overlap[0] = (double)(5+1)/10;
        overlap[1] = (double)(10+1)/16;
        assertEquals(FragmentComparator.getGood(cf5,cf6), Math.min(overlap[0], overlap[1]), 0);
    }

    @org.junit.Test
    public void checkOk() throws Exception {
        double[] contained1 = new double[2];
        double[] contained2 = new double[2];
        Fragment cf1 = new Fragment("A.java", 1, 10, "B.java", 5, 20);
        Fragment cf2 = new Fragment("A.java", 1, 10, "B.java", 5, 20);
        contained1[0] = (double) 10 / (10);
        contained1[1] = (double) (20 - 5 + 1) / (20 - 5 + 1);
        contained2[0] = (double) 10 / (10);
        contained2[1] = (double) (20 - 5 + 1) / (20 - 5 + 1);
        assertEquals(
                Math.min(Math.max(contained1[0],contained2[0]),Math.max(contained1[1],contained2[1])),
                FragmentComparator.getOk(cf1,cf2),
                0);

        Fragment cf3 = new Fragment("A.java", 1, 10, "B.java", 5, 20);
        Fragment cf4 = new Fragment("A.java", 5, 15, "B.java", 10, 25);
        contained1[0] = (double) (10 - 5 + 1) / (10); // 6/10
        contained1[1] = (double) (20 - 10 + 1) / (20 - 5 + 1); // 11/16
        contained2[0] = (double) (10 - 5 + 1) / (15 - 5 + 1); // 6/16
        contained2[1] = (double) (20 - 10 + 1) / (25 - 10 + 1); // 11/16
        assertEquals(
                Math.min(Math.max(contained1[0],contained2[0]),Math.max(contained1[1],contained2[1])),
                FragmentComparator.getOk(cf3,cf4),
                0);

        Fragment cf5 = new Fragment("A.java", 1, 10, "B.java", 5, 20);
        Fragment cf6 = new Fragment("A.java", 5, 10, "B.java", 10, 20);
        contained1[0] = (double) (10 - 5 + 1) / (10);
        contained1[1] = (double) (20 - 10 + 1) / (20 - 5 + 1);
        contained2[0] = (double) (10 - 5 + 1) / (10 - 5 + 1);
        contained2[1] = (double) (20 - 10 + 1) / (20 - 10 + 1);
        assertEquals(
                Math.min(Math.max(contained1[0],contained2[0]),Math.max(contained1[1],contained2[1])),
                FragmentComparator.getOk(cf5,cf6),
                0);
    }
}
