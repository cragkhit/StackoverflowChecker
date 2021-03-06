package data;

public class ReportedFragment extends Fragment {

	private String notes = "";

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
	
	public ReportedFragment(String firstFile, int fStart, int fEnd, 
			String secondFile, int sStart, int sEnd, String isDuplicate, String notes) {
        this.firstFile = firstFile;
        this.secondFile = secondFile;
        this.fStart = fStart;
        this.fEnd = fEnd;
        this.sStart = sStart;
        this.sEnd = sEnd;
        this.isDuplicate = isDuplicate;
        this.notes = notes;
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

	public String getNotes() {
    	return notes;
	}
}
