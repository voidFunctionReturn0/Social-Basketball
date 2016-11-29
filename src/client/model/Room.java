package client.model;

import java.util.List;

public class Room {
	private int roomNum;
	private String roomTitle;
	private List<Member> people;
	public static int count=0;
	
	public Room(int roomNum, String roomTitle , List<Member> people){
		this.roomNum=roomNum;
		this.roomTitle=roomTitle;
		this.people=people;
		
	}
	public int getRoomNum() {
		return roomNum;
	}
	public void setRoomNum(int roomNum) {
		this.roomNum = roomNum;
	}
	public String getRoomTitle() {
		return roomTitle;
	}
	public void setRoomTitle(String roomTitle) {
		this.roomTitle = roomTitle;
	}
	public List<Member> getPeople() {
		return people;
	}
	public void setPeople(List<Member> people) {
		this.people = people;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	String area;
	
	
}
