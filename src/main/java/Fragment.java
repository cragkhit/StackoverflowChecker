/**
 * Created by Chaiyong on 6/19/16.
 */
public class Fragment {
    private String firstFile;
    private String secondFile;
    private int fStart;
    private int fEnd;
    private int sStart;
    private int sEnd;

    public Fragment() {

    }

    public Fragment(String firstFile, int fStart, int fEnd, String secondFile, int sStart, int sEnd) {
        this.firstFile = firstFile;
        this.secondFile = secondFile;
        this.fStart = fStart;
        this.fEnd = fEnd;
        this.sStart = sStart;
        this.sEnd = sEnd;
    }

    public boolean isMatch(Fragment f) {
//        System.out.println("Checking: " + f.toString() + "," + this.toString());
        if (this.firstFile.equals(f.getFirstFile()) && this.secondFile.equals(f.getSecondFile()))
                // 1st subsumes 2nd
                if ((isInRange(this.fStart, this.fEnd, f.fStart, f.fEnd) && isInRange(this.sStart, this.sEnd, f.sStart, f.sEnd))
                        ||
                // 2nd subsumes 1st
                (isInRange(f.fStart, f.fEnd, this.fStart, this.fEnd) && isInRange(f.sStart, f.sEnd, this.sStart, this.sEnd))) {
                    return true;
                }
        return false;
    }

    public boolean isInRange(int s1, int e1, int s2, int e2) {
        if (s2 >= s1 && e2 <= e1)
            return true;
        else
            return false;
    }

    public double[] getOverlap(Fragment cf2) {
        int minFStart, maxFStart, minFEnd, maxFEnd = -1;
        int minSStart, maxSStart, minSEnd, maxSEnd = -1;

        if (this.getfStart() <= cf2.getfStart()) {
            minFStart = this.getfStart();
            maxFStart = cf2.getfStart();
        } else {
            minFStart = cf2.getfStart();
            maxFStart = this.getfStart();
        }

        if (this.getfEnd() <= cf2.getfEnd()) {
            minFEnd = this.getfEnd();
            maxFEnd = cf2.getfEnd();
        } else {
            minFEnd = cf2.getfEnd();
            maxFEnd = this.getfEnd();
        }

        if (this.getsStart() <= cf2.getsStart()) {
            minSStart = this.getsStart();
            maxSStart = cf2.getsStart();
        } else {
            minSStart = cf2.getsStart();
            maxSStart = this.getsStart();
        }

        if (this.getsEnd() <= cf2.getsEnd()) {
            minSEnd = this.getsEnd();
            maxSEnd = cf2.getsEnd();
        } else {
            minSEnd = cf2.getsEnd();
            maxSEnd = this.getsEnd();
        }

        double[] overlap = new double[2];

//        System.out.println("minfstart=" + minFStart + ", minfend=" + minFEnd);
//        System.out.println("maxfstart=" + maxFStart + ", maxfend=" + maxFEnd);
//        System.out.println("minsstart=" + minSStart + ", minsend=" + minSEnd);
//        System.out.println("maxsstart=" + maxSStart + ", maxsend=" + maxSEnd);
//        System.out.println();
        // first fragment
        overlap[0] = (double) (minFEnd - maxFStart + 1) / (maxFEnd - minFStart + 1);
        // second fragment
        overlap[1] = (double) (minSEnd - maxSStart + 1) / (maxSEnd - minSStart + 1);
        return overlap;
    }

    public double[] getContained(Fragment cf2) {
        int minFStart, maxFStart, minFEnd, maxFEnd = -1;
        int minSStart, maxSStart, minSEnd, maxSEnd = -1;

        if (this.getfStart() <= cf2.getfStart()) {
            minFStart = this.getfStart();
            maxFStart = cf2.getfStart();
        } else {
            minFStart = cf2.getfStart();
            maxFStart = this.getfStart();
        }

        if (this.getfEnd() <= cf2.getfEnd()) {
            minFEnd = this.getfEnd();
            maxFEnd = cf2.getfEnd();
        } else {
            minFEnd = cf2.getfEnd();
            maxFEnd = this.getfEnd();
        }

        if (this.getsStart() <= cf2.getsStart()) {
            minSStart = this.getsStart();
            maxSStart = cf2.getsStart();
        } else {
            minSStart = cf2.getsStart();
            maxSStart = this.getsStart();
        }

        if (this.getsEnd() <= cf2.getsEnd()) {
            minSEnd = this.getsEnd();
            maxSEnd = cf2.getsEnd();
        } else {
            minSEnd = cf2.getsEnd();
            maxSEnd = this.getsEnd();
        }

        double[] contained = new double[2];

//        System.out.println("minfstart=" + minFStart + ", minfend=" + minFEnd);
//        System.out.println("maxfstart=" + maxFStart + ", maxfend=" + maxFEnd);
//        System.out.println("minsstart=" + minSStart + ", minsend=" + minSEnd);
//        System.out.println("maxsstart=" + maxSStart + ", maxsend=" + maxSEnd);
        // first fragment
        contained[0] = (double) (minFEnd - maxFStart + 1) / (this.getfEnd() - this.getfStart() + 1);
        // second fragment
        contained[1] = (double) (minSEnd - maxSStart + 1) / (this.getsEnd() - this.getsStart() + 1);
//        System.out.println("contain[0]=" + contained[0] + ", contained[1]=" + contained[1]);
//        System.out.println();
        return contained;
    }

    public String getFirstFile() {
        return firstFile;
    }

    public String getSecondFile() {
        return secondFile;
    }

    public int getfStart() {
        return fStart;
    }

    public int getfEnd() {
        return fEnd;
    }

    public int getsStart() {
        return sStart;
    }

    public int getsEnd() {
        return sEnd;
    }

    public void setFirstFile(String firstFile) {
        this.firstFile = firstFile;
    }

    public void setSecondFile(String secondFile) {
        this.secondFile = secondFile;
    }

    public void setfStart(int fStart) {
        this.fStart = fStart;
    }

    public void setfEnd(int fEnd) {
        this.fEnd = fEnd;
    }

    public void setsStart(int sStart) {
        this.sStart = sStart;
    }

    public void setsEnd(int sEnd) {
        this.sEnd = sEnd;
    }

    public String toString() {
        return this.firstFile + ", " + this.fStart + ", " + this.fEnd + "," + this.secondFile + "," + this.sStart + "," + this.sEnd;
    }
}
