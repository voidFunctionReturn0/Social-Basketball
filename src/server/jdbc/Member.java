package server.jdbc;

public class Member {
	String mid;
	String mpassword;
	String mheight;
	String msex;
	String mposition;
	String marea;
	String mlevel;
	String mimage;
	
	public Member() {
	}

	public Member(String mid, String mpassword, String mheight, String msex, String mposition, String marea, String mlevel,
			String mimage) {
		super();
		this.mid = mid;
		this.mpassword = mpassword;
		this.mheight = mheight;
		this.msex = msex;
		this.mposition = mposition;
		this.marea = marea;
		this.mlevel = mlevel;
		this.mimage = mimage;
	}

	public String getMid() {
		return mid;
	}

	public void setMid(String mid) {
		this.mid = mid;
	}

	public String getMpassword() {
		return mpassword;
	}

	public void setMpassword(String mpassword) {
		this.mpassword = mpassword;
	}

	public String getMheight() {
		return mheight;
	}

	public void setMheight(String mheight) {
		this.mheight = mheight;
	}

	public String getMsex() {
		return msex;
	}

	public void setMsex(String msex) {
		this.msex = msex;
	}

	public String getMposition() {
		return mposition;
	}

	public void setMposition(String mposition) {
		this.mposition = mposition;
	}

	public String getMarea() {
		return marea;
	}

	public void setMarea(String marea) {
		this.marea = marea;
	}

	public String getMlevel() {
		return mlevel;
	}

	public void setMlevel(String mlevel) {
		this.mlevel = mlevel;
	}

	public String getMimage() {
		return mimage;
	}

	public void setMimage(String mimage) {
		this.mimage = mimage;
	}
	
	
}
