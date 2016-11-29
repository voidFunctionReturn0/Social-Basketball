package client.boards;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import client.AppMain;
import client.model.Board;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

public class boardsRoomController implements Initializable{
	@FXML private Button btnSlidingMenu;
	@FXML private Button btnSearch;
	//@FXML Button btnRefresh;	
	@FXML StackPane boardsRoom_stackPane;
	@FXML StackPane stackPane;
	@FXML private Button btnLeft;
	@FXML private Button btnRight;
	
	
	@FXML private ComboBox<String> cityCombo;
	@FXML private ComboBox<String> boroughCombo;
	
	private ObservableList<String> cityList;
	private ObservableList<String> boroughList;
	
	//private int pageNo=1;
	private int rowsPerPage=7;
	
	public static StackPane instance;
	public static BorderPane menuParent;
	
	
	//private ObservableList<Member> list;
	List<Board> roomList;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		instance = stackPane;
		
		try {
			ListView listView = (ListView) FXMLLoader.load(getClass().getResource("boardList.fxml"));
			boardsRoom_stackPane.getChildren().add(listView);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		btnSearch.setOnAction(e->handleBtnSearch(e));
		
		/*btnRefresh.setOnAction(e->handleBtnRefresh(e));*/
	
		
		btnLeft.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				handleBtnLeft(event);
			}
		});
		
		btnRight.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				handleBtnRight(event);
			}
		});
		
		//지역1 콤보박스에 글 넣기
		cityList = FXCollections.observableArrayList();
		cityList.add("서울시");
		cityList.add("부산시");
		cityList.add("인천시");
		cityCombo.setItems(cityList);
		// 지역1을 선택하면 지역2에 해당하는 지역의 글 넣기, 값 받아오기
				
		cityCombo.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				boroughList = FXCollections.observableArrayList();
				if(newValue.equals("서울시")){
					boroughCombo.setValue("");
					addSeoul();
				} else if(newValue.equals("인천시")){
					boroughCombo.setValue("");
					addIncheon();
				} else{
					boroughCombo.setValue("");
					addBusan();
				}
				boroughCombo.setItems(boroughList);
			}
		});		
		
		showButton();
	}

	public void handleAllListBtn(ActionEvent event){ //누르면 전체 목록이 나옴
		AppMain.pageNo=1;
		cityCombo.setValue("");
		boroughCombo.setValue("");
		AppMain.search_Area = null;
		try {
			ListView listView = (ListView) FXMLLoader.load(getClass().getResource("boardList.fxml"));
			boardsRoom_stackPane.getChildren().add(listView);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	

	private void handleBtnSearch(ActionEvent e) {
		AppMain.pageNo=1;
		String city = cityCombo.getValue();
		String borough = boroughCombo.getValue();
		
		if((city !=null) && (borough !=null)){
			city += " ";
			city += borough;
			AppMain.search_Area = city;
			try {
				ListView listView = (ListView) FXMLLoader.load(getClass().getResource("boardList.fxml"));
				boardsRoom_stackPane.getChildren().add(listView);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	

	public void handleBtnLeft(ActionEvent event) {
		try {
			if(AppMain.pageNo>1) { 
				AppMain.pageNo--; 			
				ListView listView = (ListView) FXMLLoader.load(getClass().getResource("boardList.fxml"));
				boardsRoom_stackPane.getChildren().add(listView);
				boardsRoom_stackPane.getChildren().remove(0);

			}
		}catch(Exception e) {}
		showButton();
	}	
	
	
	
	
	public void handleBtnRight(ActionEvent event) {
		try {
			if(AppMain.pageNo<AppMain.totalPage) {
				AppMain.pageNo++;
				ListView listView = (ListView) FXMLLoader.load(getClass().getResource("boardList.fxml"));
				boardsRoom_stackPane.getChildren().add(listView);
				boardsRoom_stackPane.getChildren().remove(0);
			}
		} catch(Exception e) {}
		showButton();
	}
	
	
	private void showButton() {
		if(AppMain.pageNo == 1) {
			btnLeft.setDisable(true);
		} else {
			btnLeft.setDisable(false);
		}
		if(AppMain.pageNo == AppMain.totalPage) {
			btnRight.setDisable(true);
		} else {
			btnRight.setDisable(false);
		}
	}
	
	
	
	private void handleBtnRefresh(ActionEvent e) {
		try {
			ListView listView = (ListView) FXMLLoader.load(getClass().getResource("boardList.fxml"));
			boardsRoom_stackPane.getChildren().add(listView);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}






	private void handleTableViewMouseClicked(MouseEvent event) {
		try {
			BorderPane borderPane = (BorderPane) FXMLLoader.load(getClass().getResource("detailBoard.fxml"));
			stackPane.getChildren().add(borderPane);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		
		
	}
	public void makeBoard() throws Exception{
		try {
			BorderPane borderPane = (BorderPane) FXMLLoader.load(getClass().getResource("makeBoard.fxml"));
			stackPane.getChildren().add(borderPane);
			stackPane.getChildren().get(0).setVisible(false);
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		
	}
	
	
	public void goMediaBoards() throws Exception {
		
	}
	

	public void handleEnterBtn(ActionEvent event) throws Exception {
		
		
		
	}
	
	public void handleSlidingMenu(ActionEvent event) {
		Platform.runLater(()->{
			try {
				// 슬라이딩메뉴 처리
				menuParent = FXMLLoader.load(getClass().getResource("../slidingMenu_main/slidingMenuBoards.fxml"));
				if(boardsRoomController.instance != null) {
					boardsRoomController.instance.getChildren().add(menuParent);
					Transition.slide(menuParent, -280, -110);
				}
			} catch(Exception e) {
				
			}
		});
	}
	
	
	/*private ListView<HBox> getList(int pageNo) {
		ListView<HBox> listView = new ListView<HBox>();
		
		
		return listView;
	}*/
	
	
	public void addBusan(){
		boroughList.add("중구");
		boroughList.add("동구");
		boroughList.add("영도구");
		boroughList.add("부산진구");
		boroughList.add("동래구");
		boroughList.add("남구");
		boroughList.add("북구");
		boroughList.add("강서구");
		boroughList.add("해운대구");
		boroughList.add("기장군");
		boroughList.add("사하구");
		boroughList.add("금정구");
		boroughList.add("연제구");
		boroughList.add("수영구");
		boroughList.add("사상구");
	}
	public void addIncheon(){
		boroughList.add("중구");
		boroughList.add("동구");
		boroughList.add("웅진군");
		boroughList.add("남구");
		boroughList.add("연수구");
		boroughList.add("남동구");
		boroughList.add("부평구");
		boroughList.add("계양구");
		boroughList.add("서구");
		boroughList.add("강화군");
	}
	
	public void addSeoul(){
		boroughList.add("강남구");
		boroughList.add("강동구");
		boroughList.add("강북구");
		boroughList.add("강서구");
		boroughList.add("관악구");
		boroughList.add("광진구");
		boroughList.add("구로구");
		boroughList.add("노원구");
		boroughList.add("도봉구");
		boroughList.add("동대문구");
		boroughList.add("동작구");
		boroughList.add("마포구");
		boroughList.add("서대문구");
		boroughList.add("서초구");
		boroughList.add("성동구");
		boroughList.add("강북구");
		boroughList.add("성북구");
		boroughList.add("송파구");
		boroughList.add("영등포구");
		boroughList.add("용산구");
		boroughList.add("은평구");
		boroughList.add("종로구");
		boroughList.add("중구");
		boroughList.add("중랑구");
	}
}
