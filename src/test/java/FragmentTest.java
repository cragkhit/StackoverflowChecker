import static org.junit.Assert.*;

/**
 * Created by Chaiyong on 6/19/16.
 */
public class FragmentTest {
    @org.junit.Test
    public void isMatch() throws Exception {
        Fragment f = new Fragment("A", 1 , 10, "B", 5, 20);
        assertTrue(f.isMatch(new Fragment("A", 1, 9, "B", 6, 18)));
    }

    @org.junit.Test
    public void isExactMatch() throws Exception {
        Fragment f = new Fragment("A", 1 , 10, "B", 5, 20);
        assertTrue(f.isMatch(new Fragment("A", 1, 10, "B", 5, 20)));
    }

    @org.junit.Test
    public void isNotMatch() throws Exception {
        Fragment f = new Fragment("A", 1 , 10, "B", 5, 20);
        assertTrue(f.isMatch(new Fragment("A", 1, 15, "B", 5, 20)));
        assertFalse(f.isMatch(new Fragment("A", 20, 35, "B", 50, 210)));
    }

    @org.junit.Test
    public void checkOverlap() throws Exception {
        double[] overlap = new double[2];
        Fragment cf1 = new Fragment("A.java", 1, 10, "B.java", 5, 20);
        Fragment cf2 = new Fragment("A.java", 1, 10, "B.java", 5, 20);
        overlap[0] = 11/11;
        overlap[1] = 16/16;
        assertEquals(overlap[0], cf1.getOverlap(cf2)[0], 0);
        assertEquals(overlap[1], cf1.getOverlap(cf2)[1], 0);

        Fragment cf3 = new Fragment("A.java", 1, 10, "B.java", 5, 20);
        Fragment cf4 = new Fragment("A.java", 5, 15, "B.java", 10, 25);
        overlap[0] = (double)(5+1)/15;
        overlap[1] = (double)(10+1)/21;
        assertEquals(overlap[0], cf3.getOverlap(cf4)[0], 0);
        assertEquals(overlap[1], cf3.getOverlap(cf4)[1], 0);

        Fragment cf5 = new Fragment("A.java", 1, 10, "B.java", 5, 20);
        Fragment cf6 = new Fragment("A.java", 5, 10, "B.java", 10, 20);
        overlap[0] = (double)(5+1)/10;
        overlap[1] = (double)(10+1)/16;
        assertEquals(overlap[0], cf5.getOverlap(cf6)[0], 0);
        assertEquals(overlap[1], cf5.getOverlap(cf6)[1], 0);
    }

    @org.junit.Test
    public void checkContained() throws Exception {
        double[] contained = new double[2];
        Fragment cf1 = new Fragment("A.java", 1, 10, "B.java", 5, 20);
        Fragment cf2 = new Fragment("A.java", 1, 10, "B.java", 5, 20);
        contained[0] = (double) 10 / (10);
        contained[1] = (double) (20 - 5 + 1) / (20 - 5 + 1);
        assertEquals(cf1.getContained(cf2)[0], contained[0], 0);
        assertEquals(cf1.getContained(cf2)[1], contained[1], 0);

        Fragment cf3 = new Fragment("A.java", 1, 10, "B.java", 5, 20);
        Fragment cf4 = new Fragment("A.java", 5, 15, "B.java", 10, 25);
        contained[0] = (double) (10 - 5 + 1) / (10);
        contained[1] = (double) (20 - 10 + 1) / (20 - 5 + 1);
        assertEquals(cf3.getContained(cf4)[0], contained[0], 0);
        assertEquals(cf3.getContained(cf4)[1], contained[1], 0);

        Fragment cf5 = new Fragment("A.java", 1, 10, "B.java", 5, 20);
        Fragment cf6 = new Fragment("A.java", 5, 10, "B.java", 10, 20);
        contained[0] = (double) (10 - 5 + 1) / (10);
        contained[1] = (double) (20 - 10 + 1) / (20 - 5 + 1);
        assertEquals(cf5.getContained(cf6)[0], contained[0], 0);
        assertEquals(cf5.getContained(cf6)[1], contained[1], 0);
    }
}
