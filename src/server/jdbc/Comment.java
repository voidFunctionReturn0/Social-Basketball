package server.jdbc;

public class Comment {
	private int cno;
	private String cwriter;
	private String Ccontent;
	private String cwritedate;
	private int bno;
	
	public int getCno() {
		return cno;
	}
	public void setCno(int cno) {
		this.cno = cno;
	}
	public String getCwriter() {
		return cwriter;
	}
	public void setCwriter(String cwriter) {
		this.cwriter = cwriter;
	}
	public String getCcontent() {
		return Ccontent;
	}
	public void setCcontent(String ccontent) {
		Ccontent = ccontent;
	}
	public String getCwritedate() {
		return cwritedate;
	}
	public void setCwritedate(String cwritedate) {
		this.cwritedate = cwritedate;
	}
	public int getBno() {
		return bno;
	}
	public void setBno(int bno) {
		this.bno = bno;
	}
	

}
