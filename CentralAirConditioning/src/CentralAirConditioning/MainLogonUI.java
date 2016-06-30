package CentralAirConditioning;

import javax.swing.JScrollPane;
import javax.swing.JTable;
  
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.*;
import javafx.stage.Stage;

@SuppressWarnings("serial")
public class MainLogonUI extends Application{
	private String UserName;
	private String PassWord;
	
	private LogonController logonController;
	
	public MainLogonUI() {
			
	}
	
	@Override
	public void start(Stage primaryStage) 
	{
		 Login(primaryStage);
	}
	
	public void Login(Stage thisStage)
	{
		DBConnection.getInstance().createConnection();//建立数据库连接
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10, 10, 10, 10));
        
        Text scenetitle = new Text("请完成登陆");
        grid.add(scenetitle, 0, 0, 2, 1);
        scenetitle.setId("welcome-text");
        
        Label userName = new Label("用户名:");
        grid.add(userName, 0, 1);
        
        TextField userTextField = new TextField();
        grid.add(userTextField, 1, 1);
        
        Label pass = new Label("密码:");
        grid.add(pass, 0, 2);
   
        final PasswordField passTextField = new PasswordField();
        grid.add(passTextField, 1, 2);
        
        Button btn = new Button("确定");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(btn);
        grid.add(hbBtn, 1, 4);
        
        final Text actiontarget = new Text();
        grid.add(actiontarget, 1, 6);
        actiontarget.setId("actiontarget");
        initialize();
		logonController = new LogonController();
		
		

		
		//boolean eligible = logonController.logon("root", "123456");
        
        btn.setOnAction((ActionEvent e) -> {
        	try{
        		UserName=userTextField.getText();
        		PassWord=passTextField.getText();
        		//System.out.println(UserName+" "+PassWord);
        		boolean eligible = logonController.logon(UserName, PassWord);
        		
        		if (eligible == true){
        			//thisStage.hide();
        			new MainWindowUI(thisStage);
        		}
                else
                    actiontarget.setText("用户名或密码错误");
        		}
        		catch(Exception e1)
        		{
        		actiontarget.setText("error");
        		}
        	});
        
        Scene scene = new Scene(grid, 650, 450);
        
        thisStage.setTitle("管理员登录");
        thisStage.setScene(scene);
        scene.getStylesheets().add
         (MainLogonUI.class.getResource("application.css").toExternalForm());
        thisStage.show();
	}
	
	private void initialize() {

	
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
