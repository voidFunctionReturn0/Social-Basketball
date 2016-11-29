package client.model;

import java.util.Date;

public class MediaBoard {
	private int mbno;
	private String mbtitle;
	private String mbcontent;
	private String  mbwriter;
	private int  mbhitcount;
	private Date mbdate;
	private String mbmedia;

	public MediaBoard(){}
	
	public MediaBoard(int mbno,String mbtitle,String mbcontent,String  mbwriter, int mbhitcount,Date mbdate, String mbmedia){
		this.mbno = mbno;
		this.mbtitle = mbtitle;
		this.mbcontent = mbcontent;
		this.mbwriter = mbwriter;
		this.mbhitcount = mbhitcount;
		this.mbdate = mbdate;
		this.mbmedia = mbmedia;
	}
	
	public int getMbno() {
		return mbno;
	}
	public void setMbno(int mbno) {
		this.mbno = mbno;
	}
	public String getMbtitle() {
		return mbtitle;
	}
	public void setMbtitle(String mbtitle) {
		this.mbtitle = mbtitle;
	}
	public String getMbcontent() {
		return mbcontent;
	}
	public void setMbcontent(String mbcontent) {
		this.mbcontent = mbcontent;
	}
	public String getMbwriter() {
		return mbwriter;
	}
	public void setMbwriter(String mbwriter) {
		this.mbwriter = mbwriter;
	}
	public int getMbhitcount() {
		return mbhitcount;
	}
	public void setMbhitcount(int mbhitcount) {
		this.mbhitcount = mbhitcount;
	}
	public Date getMbdate() {
		return mbdate;
	}
	public void setMbdate(Date mbdate) {
		this.mbdate = mbdate;
	}
	public String getMbmedia() {
		return mbmedia;
	}
	public void setMbmedia(String mbmedia) {
		this.mbmedia = mbmedia;
	}

	
}
