package data;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class OkPair implements Comparable<OkPair> {
    private Fragment f1;
    private Fragment f2;

    public OkPair() {
        super();
    }

    public OkPair(Fragment f1, Fragment f2) {
        this.f1 = f1;
        this.f2 = f2;
    }

    public double getOkVal(){
        return FragmentComparator.getOk(f1, f2);
    }

    public Fragment getF1() {
        return f1;
    }

    public void setF1(Fragment f1) {
        this.f1 = f1;
    }

    public Fragment getF2() {
        return f2;
    }

    public void setF2(Fragment f2) {
        this.f2 = f2;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(f1)
                .append(f2)
                .toHashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof OkPair) {
            OkPair okPair = (OkPair) o;
            return new EqualsBuilder()
                    .append(f1, f2)
                    .isEquals();
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return f1 + "," + f2 + "," + getOkVal();
    }

    @Override
    public int compareTo(OkPair o) {
        OkPair okPair = (OkPair) o;
        return new CompareToBuilder()
                .append(this.f1, okPair.f1)
                .append(this.getOkVal(), okPair.getOkVal())
                .append(this.f2, okPair.f2)
                .toComparison();
    }
}
