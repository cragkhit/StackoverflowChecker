
public class ReportedFragment extends Fragment {
	
	public ReportedFragment(String firstFile, int fStart, int fEnd, 
			String secondFile, int sStart, int sEnd, String isDuplicate) {
        this.firstFile = firstFile;
        this.secondFile = secondFile;
        this.fStart = fStart;
        this.fEnd = fEnd;
        this.sStart = sStart;
        this.sEnd = sEnd;
        this.isDuplicate = isDuplicate;
    }
	
	public ReportedFragment() {
		super();
		isDuplicate = "";
	}
	
	protected String isDuplicate;
	
    public int getMinCloneSize() {
		return Math.min(this.fEnd - this.fStart + 1, this.sEnd - this.sStart + 1);
    }

	public String getIsDuplicate() {
		return isDuplicate;
	}

	public void setIsDuplicate(String isDuplicate) {
		this.isDuplicate = isDuplicate;
	}
}
