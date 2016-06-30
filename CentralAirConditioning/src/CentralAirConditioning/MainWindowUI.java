package CentralAirConditioning;

import javax.swing.JScrollPane;
import javax.swing.JTable;
  
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.*;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.util.Calendar;
import java.util.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.LinkedList;
import java.util.concurrent.locks.Lock;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class MainWindowUI extends Application{
	private ServerController serverController;
	
	private Label RunningMode;
	private Label Refresh_Rate;
	private Label Up_limit;
	private Label Schedule;
	private Label Rate;
	
	private Label Current_Time;
	private Label Current_Room;
	private Label current_Temperature;
	private Label current_TurboSpeed;
	
	private Label W_Current_Room;
	private Label W_current_Temperature;
	private Label W_current_TurboSpeed;
	
	@SuppressWarnings("rawtypes")
	private final TableView<Data> table_room = new TableView<Data>();
	private final TableView<ReportData> Daily = new TableView<ReportData>();
	private final TableView<ReportData> Week = new TableView<ReportData>();
	private final TableView<ReportData> Month = new TableView<ReportData>();
	
	
	Stage room_stage = new Stage();
	private final ObservableList<Data> roomdata = FXCollections.observableArrayList();
	
	SimpleDateFormat df_day = new SimpleDateFormat("yyyy-MM-dd ");//�����ʽ
	Timestamp d = new Timestamp(System.currentTimeMillis());
    String date_now = df_day.format(d);
    String year_now = date_now.substring(0, 4);
    String month_now = date_now.substring(5, 7);
    String day_now = date_now.substring(8, 10);
	
	Stage day_stage = new Stage();
	private Label rnum_d;
	private final ObservableList<ReportData> daydata = FXCollections.observableArrayList();
	private Label sumfeeLabel_d;
	private Label sumfee_d;
	private Label timesLabel_d;
	private Label times_d;
	final HBox hb_day = new HBox();
	
	Stage week_stage = new Stage();
	private Label rnum_w;
	private final ObservableList<ReportData> weekdata = FXCollections.observableArrayList();
	private Label sumfeeLabel_w;
	private Label sumfee_w;
	private Label timesLabel_w;
	private Label times_w;
	final HBox hb_week = new HBox();
	
	Stage month_stage = new Stage();
	private Label rnum_m;
	private final ObservableList<ReportData> monthdata = FXCollections.observableArrayList();
	private Label sumfeeLabel_m;
	private Label sumfee_m;
	private Label timesLabel_m;
	private Label times_m;
	final HBox hb_month = new HBox();
	
	Stage choose_stage_d = new Stage();
	private TextField userTextField_d;
	private Spinner<Integer> nian_d;
	private ComboBox<String> yue_d;
	private ComboBox<String> ri_d;
	
	Stage choose_stage_w = new Stage();
	private TextField userTextField_w;
	private Spinner<Integer> nian_w;
	private ComboBox<String> yue_w;
	private ComboBox<String> zhou_w;
	
	Stage choose_stage = new Stage();
	private TextField userTextField_m;
	private Spinner<Integer> nian;
	private ComboBox<String> yue;
	
	@Override
	public void start(Stage primaryStage) 
	{
		 mainServer(primaryStage);
	}
	
	public MainWindowUI(Stage thisStage) {
		// TODO Auto-generated constructor stub
        initialize();
        serverController = new ServerController();//����������
		mainServer(thisStage);
		
		serverController.timeObsever(this);
		serverController.timeUpdate(this);
		serverController.timeUpdate_m(this);
	}
	
	private void initialize() {
		
	}
	
	public void mainServer(Stage thisStage)
	{
		GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10,10,10,10));
        
        
        
        select_Day(choose_stage_d);
        select_Month(choose_stage);
        select_Week(choose_stage_w);
        
        RunningMode = new Label("����ģʽ:    ");
        grid.add(RunningMode, 0, 0, 4, 1);
        
        Refresh_Rate = new Label("ˢ�¼��:     ");
        grid.add(Refresh_Rate, 4, 0, 4, 1);
        
        Up_limit = new Label("��������:    ");
        grid.add(Up_limit, 8, 0, 4, 1);
        
        Schedule = new Label("�����㷨:    ");
        grid.add(Schedule, 12, 0, 4, 1);
        
        Rate = new Label("�۸�:    ");
        grid.add(Rate, 16, 0, 4, 1);
        
        /*Button over = new Button("�������");
        grid.add(over, 0, 2);
        
        over.setOnAction((ActionEvent e) -> {
        	try {
        			Stage stage = new Stage();
                	check_out(stage);
        	    }
        		catch(Exception e1)
        		{
        		
        		}
        	});*/
        
        Current_Time = new Label("��ǰʱ�䣺    ");
        grid.add(Current_Time, 5, 2, 8, 1);
        
        Button baobiao = new Button("�鿴����");
        grid.add(baobiao, 6, 9);

        daily_R(day_stage);
        Week_R(week_stage);
        Month_R(month_stage);
        
        baobiao.setOnAction((ActionEvent e) -> {
        	try {
        			Stage stage = new Stage();
                	catalogue(stage);
        	    }
        		catch(Exception e1)
        		{
        		
        		}
        	});
        
        Current_Room = new Label("������:    ");
        grid.add(Current_Room, 5, 3, 8, 1);
        
        W_Current_Room = new Label("�ȴ�����:    ");
        grid.add(W_Current_Room, 5, 6, 8, 1);
        
        Button reset = new Button("��������");
        grid.add(reset, 12, 9);
        
        reset.setOnAction((ActionEvent e) -> {
        	try {
        			Stage stage = new Stage();
                	reset(stage);
        	    }
        		catch(Exception e1)
        		{
        		
        		}
        	});
        
        current_Temperature = new Label("Ŀ���¶�:    ");
        grid.add(current_Temperature, 5, 4, 8, 1);
        
        W_current_Temperature = new Label("Ŀ���¶�:    ");
        grid.add(W_current_Temperature, 5, 7, 8, 1);
        
        Button situation = new Button("�������");
        grid.add(situation, 18, 9);
        setRooms(room_stage);
        situation.setOnAction((ActionEvent e) -> {
        	try {
        			
                	
                	room_stage.show();
        	    }
        		catch(Exception e1)
        		{
        		
        		}
        	});
        
        current_TurboSpeed = new Label("Ŀ�����:    ");
        grid.add(current_TurboSpeed, 5, 5, 8, 1);
        
        W_current_TurboSpeed = new Label("Ŀ�����:    ");
        grid.add(W_current_TurboSpeed, 5, 8, 8, 1);
        
        Scene scene = new Scene(grid, 650, 650);
        
        thisStage.setTitle("���ػ�����");
        thisStage.setHeight(500);
        thisStage.setWidth(650);
        thisStage.setScene(scene);
        scene.getStylesheets().add
         (MainWindowUI.class.getResource("application.css").toExternalForm());
        thisStage.show();
        
        thisStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
		    @Override
		    public void handle(WindowEvent event) {
		    	Platform.exit();
		    	System.exit(0);
		    }
		});
	}
	
	public void update()
	{
		if(serverController.getMode() == Mode.COOL)
		{
			RunningMode.setText("����ģʽ:����  ");
		}
		else
		{
			RunningMode.setText("����ģʽ:����  ");
		}
		
		Refresh_Rate.setText("ˢ�¼��:" + Integer.toString(serverController.getFrequency()) + "s ");
		Up_limit.setText("��������:" + Integer.toString(serverController.getMaxLoad()) + " ");
		
		if(serverController.getStrategy() == Strategy.FIFO)
		{
			Schedule.setText("�����㷨:�����ȷ���  ");
		}
		else if(serverController.getStrategy() == Strategy.HWF)
		{
			Schedule.setText("�����㷨:�߷�������  ");
		}
		else
		{
			Schedule.setText("�����㷨:��ת����    ");
		}
	        
	    Rate.setText("�۸�:" + Double.toString(serverController.getChargeRate()) + "Ԫ/kw");
	    
	    String tmp;
	    tmp = "������:";
	    for(Request request: (serverController.getScheduler().getExecuteList()))
	    {
	    	tmp = String.format("%s%-10d",tmp,request.getRoomNumber());
	    	System.out.println(Integer.toString(request.getRoomNumber()));
	    }
	    //System.out.println(tmp);
	    Current_Room.setText(tmp);
	    
	    tmp = "Ŀ���¶�:";
	    for(Request request: (serverController.getScheduler().getExecuteList()))
	    {
	    	tmp = String.format("%s%-7s",tmp,Double.toString(request.getGoalTemperature()));
	    }
	    
	    current_Temperature.setText(tmp);
	    
	    tmp = "Ŀ�����:";
	    for(Request request: (serverController.getScheduler().getExecuteList()))
	    {
	    	if(request.getWind() == Wind.HIGH)
	    		tmp = String.format("%s%-10s",tmp,"��");
	    	else if(request.getWind() == Wind.MEDIUM)
	    		tmp = String.format("%s%-10s",tmp,"��");
	    	else
	    		tmp = String.format("%s%-10s",tmp,"��");
	    }
	    current_TurboSpeed.setText(tmp);
	    
	    tmp = "�ȴ�����:";
	    for(Request request: (serverController.getScheduler().getWaitList()))
	    {
	    	tmp = String.format("%s%-10d",tmp,request.getRoomNumber());
	    	System.out.println(Integer.toString(request.getRoomNumber()));
	    }
	    
	    //System.out.println(tmp);
	    W_Current_Room.setText(tmp);
	    
	    tmp = "Ŀ���¶�:";
	    for(Request request: (serverController.getScheduler().getWaitList()))
	    {
	    	tmp = String.format("%s%-7s",tmp,Double.toString(request.getGoalTemperature()));
	    }
	    
	    W_current_Temperature.setText(tmp);
	    
	    tmp = "Ŀ�����:";
	    for(Request request: (serverController.getScheduler().getWaitList()))
	    {
	    	if(request.getWind() == Wind.HIGH)
	    		tmp = String.format("%s%-10s",tmp,"��");
	    	else if(request.getWind() == Wind.MEDIUM)
	    		tmp = String.format("%s%-10s",tmp,"��");
	    	else
	    		tmp = String.format("%s%-10s",tmp,"��");
	    }
	    
	    W_current_TurboSpeed.setText(tmp);
	}
	
	public void updateTime()
	{
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//�������ڸ�ʽ
	    //System.out.println(df.format(new Date()));// new Date()Ϊ��ȡ��ǰϵͳʱ��
	    Current_Time.setText("��ǰʱ��:" + df.format(new Date()));
	    show_rooms(room_stage);
	    d = new Timestamp(System.currentTimeMillis());
	    date_now = df_day.format(d);
	    year_now = date_now.substring(0, 4);
	    month_now = date_now.substring(5, 7);
	    day_now = date_now.substring(8, 10);
	}
	
	public void updateDate_m()
	{
		String nian_temp = Integer.toString(nian.getValue());
		String choose_m = yue.getValue();
		int yue_temp = Integer.parseInt(month_now);
		if(nian_temp.equals(year_now))
		{
			yue.getItems().clear();
			for(int i = 1; i<= yue_temp; i++)
			{
				yue.getItems().add(Integer.toString(i));
			}
		}
		else
		{
			yue.getItems().clear();
			for(int i = 1; i<= 12; i++)
			{
				yue.getItems().add(Integer.toString(i));
			}
		}
		yue.setValue(choose_m);
	}
	
	public void updateDate_w()
	{
		String nian_temp = Integer.toString(nian_w.getValue());
		String choose_m = yue_w.getValue();
		int yue_temp = Integer.parseInt(month_now);
		if(nian_temp.equals(year_now))
		{
			yue_w.getItems().clear();
			for(int i = 1; i<= yue_temp; i++)
			{
				yue_w.getItems().add(Integer.toString(i));
			}
		}
		else
		{
			yue_w.getItems().clear();
			for(int i = 1; i<= 12; i++)
			{
				yue_w.getItems().add(Integer.toString(i));
			}
		}
		yue_w.setValue(choose_m);
		
		String choose_w = zhou_w.getValue();
		String day_temp = "1";
		String date_day = nian_temp+ "-" + choose_m + "-" + day_temp;
		String week_show =  convertWeekByDate(date_day);
		zhou_w.getItems().clear();
		zhou_w.getItems().add(week_show);
		day_temp =  getNextDate(day_temp,choose_m,nian_temp);
		while(day_temp != "")
		{
			date_day = nian_temp+ "-" + choose_m + "-" + day_temp;
			week_show =  convertWeekByDate(date_day);
			zhou_w.getItems().add(week_show);
			day_temp =  getNextDate(day_temp,choose_m,nian_temp);
		}
		
		zhou_w.setValue(choose_w);
	}
	
	public void updateDate_d()
	{
		String nian_temp = Integer.toString(nian_d.getValue());
		int yue_temp = Integer.parseInt(month_now);
		String choose_m = yue_d.getValue();
		String choose_d = ri_d.getValue();
		if(nian_temp.equals(year_now))
		{
			yue_d.getItems().clear();
			for(int i = 1; i<= yue_temp; i++)
			{
				yue_d.getItems().add(Integer.toString(i));
			}
		}
		else
		{
			yue_d.getItems().clear();
			for(int i = 1; i<= 12; i++)
			{
				yue_d.getItems().add(Integer.toString(i));
			}
		}
		yue_d.setValue(choose_m);
		int day_temp = Integer.parseInt(day_now);
		String month_temp = yue_d.getValue();
		
		if(month_temp.equals(month_now))
		{
			ri_d.getItems().clear();
			for(int i=0; i<= day_temp; i++)
			{
				ri_d.getItems().add(Integer.toString(i));
			}
		}
		else
		{
			if(month_temp.equals("02"))
			{
				int year_temp = nian_d.getValue();
				if(year_temp%4 == 0 && year_temp%400 != 0)
				{
					ri_d.getItems().clear();
					for(int i=0; i<= 29; i++)
					{
						ri_d.getItems().add(Integer.toString(i));
					}
				}
				else
				{
					ri_d.getItems().clear();
					for(int i=0; i<= 28; i++)
					{
						ri_d.getItems().add(Integer.toString(i));
					}
				}
			}
			else if(month_temp.equals("04") || month_temp.equals("06") || month_temp.equals("09") || month_temp.equals("11"))
			{
				ri_d.getItems().clear();
				for(int i=0; i<= 30; i++)
				{
					ri_d.getItems().add(Integer.toString(i));
				}
			}
			else
			{
				ri_d.getItems().clear();
				for(int i=0; i<= 31; i++)
				{
					ri_d.getItems().add(Integer.toString(i));
				}
			}
		}
		
		ri_d.setValue(choose_d);
	}
	
	public void check_out(Stage thisStage)
	{
		 	GridPane grid = new GridPane();
	        grid.setAlignment(Pos.CENTER);
	        grid.setHgap(10);
	        grid.setVgap(10);
	        grid.setPadding(new Insets(10, 10, 10, 10));
	        
	        Text scenetitle = new Text("��������");
	        grid.add(scenetitle, 0, 0, 2, 1);
	        scenetitle.setId("welcome-text");

	        Label userName = new Label("�����:");
	        grid.add(userName, 0, 1);
	        
	        TextField userTextField = new TextField();
	        grid.add(userTextField, 1, 1);
	        
	        Button ok_tuifang = new Button("�˷�");
	        grid.add(ok_tuifang, 1, 4);
	        
	        ok_tuifang.setOnAction((ActionEvent e) -> {
	        	try{
	        			//Stage stage = new Stage();
	                	bill(thisStage);
	                
	        	}catch(Exception e1){
	        		//actiontarget.setText("��������ȷ�ķ����");
	        	}
	        });
	        
	        
	        Scene scene = new Scene(grid, 350, 250);
	        
	        thisStage.setTitle("�˷�");
	        thisStage.setScene(scene);
	        scene.getStylesheets().add
	         (MainWindowUI.class.getResource("application.css").toExternalForm());
	        thisStage.show();
	}
	public void bill(Stage thisStage)
	{
		GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10, 10, 10, 10));
        
		Text scenetitle = new Text("�˵�");
        grid.add(scenetitle, 0, 0, 2, 1);
        scenetitle.setId("welcome-text");
        
        Text room_num = new Text("������룺      ");
        grid.add(room_num, 2, 1, 6, 1);
        scenetitle.setId("label-text");
        
        Text time_ru = new Text("��סʱ�䣺      ");
        grid.add(time_ru, 2, 2, 6,1);
        scenetitle.setId("label-text");
        
        Text time_chu = new Text("�˷�ʱ�䣺     ");
        grid.add(time_chu, 2, 3, 6, 1);
        scenetitle.setId("label-text");
        
        Text NengHao = new Text("�ۼ��ܺģ�������");
        grid.add(NengHao, 2, 4, 6, 1);
        scenetitle.setId("label-text");
        
        Text fee = new Text("�ۼƷ��ã�����");
        grid.add(fee, 2, 5, 6, 1);
        scenetitle.setId("label-text");
        
        Scene scene = new Scene(grid, 350, 250);
        
        thisStage.setTitle("��ϸ��Ϣ");
        thisStage.setScene(scene);
        scene.getStylesheets().add
         (MainWindowUI.class.getResource("application.css").toExternalForm());
        thisStage.show();
	}

	public void catalogue(Stage thisStage)
	{
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10, 10, 10, 10));
        
		Text scenetitle = new Text("��ѡ�񱨱�����");
        grid.add(scenetitle, 0, 0, 2, 1);
        scenetitle.setId("welcome-text");
        
        Button daily = new Button("�ձ���");
        grid.add(daily, 0, 1);
        daily.setOnAction((ActionEvent e) -> {
        	try {
        		choose_stage_d.show();
    			//ShowDayReport(day_stage,roomnum,start,end);
        	    }
        		catch(Exception e1)
        		{
        		
        		}
        	});
        
        Button Week = new Button("�ܱ���");
        grid.add(Week, 0, 2);
        Week.setOnAction((ActionEvent e) -> {
        	try {
        		choose_stage_w.show();
    			//ShowWeekReport(week_stage,roomnum,start,end);
        			
        	    }
        		catch(Exception e1)
        		{
        		
        		}
        	});
        
        
        Button Month = new Button("�±���");
        grid.add(Month, 0, 3);
        Month.setOnAction((ActionEvent e) -> {
        	try {
        		choose_stage.show();
        	    }
        		catch(Exception e1)
        		{
        			
        		}
        	});
        
        Scene scene = new Scene(grid, 350, 250);
        
        thisStage.setTitle("��������");
        thisStage.setScene(scene);
        scene.getStylesheets().add
         (MainWindowUI.class.getResource("application.css").toExternalForm());
        thisStage.show();
	}
	
	public void daily_R(Stage stage)
	{
		Scene scene = new Scene(new Group());  
        stage.setTitle("�ձ���");  
        stage.setWidth(600);  
        stage.setHeight(500);  
        
        Daily.setMaxWidth(562);
        
        rnum_d = new Label("�����");
        rnum_d.setFont(new Font("Arial",20));
   
        //TableColumn roomNum = new TableColumn("�����");  
        TableColumn start = new TableColumn("��ʼʱ��");  
        start.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        start.setMinWidth(130);
        TableColumn end = new TableColumn("����ʱ��"); 
        end.setMinWidth(130);
        end.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        TableColumn start_T = new TableColumn("��ʼʱ�¶�"); 
        start_T.setCellValueFactory(new PropertyValueFactory<>("startTemperature"));
        start_T.setMinWidth(75);
        TableColumn end_T = new TableColumn("����ʱ�¶�");  
        end_T.setCellValueFactory(new PropertyValueFactory<>("endTemperature"));
        end_T.setMinWidth(75);
        TableColumn wind = new TableColumn("����"); 
        wind.setCellValueFactory(new PropertyValueFactory<>("wind"));
        wind.setMinWidth(75);
        TableColumn fee = new TableColumn("����"); 
        fee.setCellValueFactory(new PropertyValueFactory<>("fee"));
        fee.setMinWidth(65);
          
        Daily.setItems(daydata);
        Daily.getColumns().addAll(start, end,start_T,end_T,wind,fee);  
   
        sumfeeLabel_d = new Label("�ܷ���");
        sumfeeLabel_d.setMinWidth(100);
        sumfee_d = new Label("0.00");
        sumfee_d.setMinWidth(100);
        timesLabel_d = new Label("���ػ�����");
        timesLabel_d.setMinWidth(100);
        times_d = new Label("0");
        times_d.setMinWidth(100);
        hb_day.getChildren().addAll(sumfeeLabel_d, sumfee_d, timesLabel_d, times_d);
        
        final VBox vbox = new VBox();  
        vbox.setSpacing(6);  
        vbox.setPadding(new Insets(10, 0, 0, 10));  
        vbox.getChildren().addAll(rnum_d,Daily,hb_day);  
   
        ((Group) scene.getRoot()).getChildren().addAll(vbox);  
   
        stage.setScene(scene);  
        //stage.show(); 
	}
	
	public void ShowDayReport(Stage stage,int roomnum,Timestamp start,Timestamp end)
	{
		LinkedList<Fee> infos = serverController.getReport(start, end, roomnum);
		daydata.clear();
        rnum_d.setText("�����" + Integer.toString(roomnum));
        Timestamp temp_dstart;
        Timestamp temp_dend;
    	double temp_tstart;
    	double temp_tend;
    	int temp_wrun;
    	double temp_fee;
    	double temp_sumFee = 0;
        for(Fee dayInfo: infos)
        {
        	temp_dstart = dayInfo.getStartTime();
        	temp_dend = dayInfo.getEndTime();
        	temp_tstart = dayInfo.getStartTempterature();
        	temp_tend = dayInfo.getEndTempterature();
        	temp_wrun = dayInfo.getWind();
        	temp_fee = dayInfo.getFee();
        	
        	temp_sumFee += temp_fee;
        	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//�����ʽ������ʾ����
        	//Timestamp now = new Timestamp(System.currentTimeMillis());//��ȡϵͳ��ǰʱ��
        	String temp1 = df.format(temp_dstart);
        	String temp2 = df.format(temp_dend);
        	String temp3;
        	if(temp_wrun == 0)
        	{
        		temp3 = "���ٷ�";
        	}
        	else if(temp_wrun == 1)
        	{
        		temp3 = "���ٷ�";
        	}
        	else
        	{
        		temp3 = "���ٷ�";
        	}
        	
        	String temp4 = String.format("%.1f", temp_tstart);
        	String temp5 = String.format("%.1f", temp_tend);
        	String temp6 = String.format("%.2f", temp_fee);
        	daydata.add(new ReportData(temp1, temp2, temp4, temp5, temp3,temp6));
        	
        }
        String temp6 = String.format("%.2f", temp_sumFee);
        int startTimes = serverController.getStartUpTimes(start, end, roomnum);
        sumfee_d.setText(temp6);
        times_d.setText(Integer.toString(startTimes));
         
        stage.show(); 
	}
	
	public void Week_R(Stage stage)
	{
		Scene scene = new Scene(new Group());  
        stage.setTitle("�ܱ���");  
        stage.setWidth(600);  
        stage.setHeight(500);  
        
        Week.setMaxWidth(562);
        rnum_w = new Label("�����");
        rnum_w.setFont(new Font("Arial",20));
   
        TableColumn start = new TableColumn("��ʼʱ��");  
        start.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        start.setMinWidth(130);
        TableColumn end = new TableColumn("����ʱ��"); 
        end.setMinWidth(130);
        end.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        TableColumn start_T = new TableColumn("��ʼʱ�¶�"); 
        start_T.setCellValueFactory(new PropertyValueFactory<>("startTemperature"));
        start_T.setMinWidth(75);
        TableColumn end_T = new TableColumn("����ʱ�¶�");  
        end_T.setCellValueFactory(new PropertyValueFactory<>("endTemperature"));
        end_T.setMinWidth(75);
        TableColumn wind = new TableColumn("����"); 
        wind.setCellValueFactory(new PropertyValueFactory<>("wind"));
        wind.setMinWidth(75);
        TableColumn fee = new TableColumn("����"); 
        fee.setCellValueFactory(new PropertyValueFactory<>("fee"));
        fee.setMinWidth(65);
          
        Week.setItems(weekdata);
        Week.getColumns().addAll(start, end,start_T,end_T,wind,fee);  
   
        sumfeeLabel_w = new Label("�ܷ���");
        sumfeeLabel_w.setMinWidth(100);
        sumfee_w = new Label("0.00");
        sumfee_w.setMinWidth(100);
        timesLabel_w = new Label("���ػ�����");
        timesLabel_w.setMinWidth(100);
        times_w = new Label("0");
        times_w.setMinWidth(100);
        hb_week.getChildren().addAll(sumfeeLabel_w, sumfee_w, timesLabel_w, times_w);
        
        final VBox vbox = new VBox();  
        vbox.setSpacing(6);  
        vbox.setPadding(new Insets(10, 0, 0, 10));  
        vbox.getChildren().addAll(rnum_w,Week,hb_week);  
   
        ((Group) scene.getRoot()).getChildren().addAll(vbox);  
   
        stage.setScene(scene);  
        //stage.show(); 
	}
	
	public void ShowWeekReport(Stage stage,int roomnum,Timestamp start,Timestamp end)
	{
		LinkedList<Fee> infos = serverController.getReport(start, end, roomnum);
		weekdata.clear();
        rnum_w.setText("�����" + Integer.toString(roomnum));
        Timestamp temp_dstart;
        Timestamp temp_dend;
    	double temp_tstart;
    	double temp_tend;
    	int temp_wrun;
    	double temp_fee;
    	double temp_sumFee = 0;
        for(Fee dayInfo: infos)
        {
        	temp_dstart = dayInfo.getStartTime();
        	temp_dend = dayInfo.getEndTime();
        	temp_tstart = dayInfo.getStartTempterature();
        	temp_tend = dayInfo.getEndTempterature();
        	temp_wrun = dayInfo.getWind();
        	temp_fee = dayInfo.getFee();
        	
        	temp_sumFee += temp_fee;
        	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//�����ʽ������ʾ����
        	//Timestamp now = new Timestamp(System.currentTimeMillis());//��ȡϵͳ��ǰʱ��
        	String temp1 = df.format(temp_dstart);
        	String temp2 = df.format(temp_dend);
        	String temp3;
        	if(temp_wrun == 0)
        	{
        		temp3 = "���ٷ�";
        	}
        	else if(temp_wrun == 1)
        	{
        		temp3 = "���ٷ�";
        	}
        	else
        	{
        		temp3 = "���ٷ�";
        	}
        	
        	String temp4 = String.format("%.1f", temp_tstart);
        	String temp5 = String.format("%.1f", temp_tend);
        	String temp6 = String.format("%.2f", temp_fee);
        	weekdata.add(new ReportData(temp1, temp2, temp4, temp5, temp3,temp6));
        	
        }
        String temp6 = String.format("%.2f", temp_sumFee);
        int startTimes = serverController.getStartUpTimes(start, end, roomnum);
        sumfee_w.setText(temp6);
        times_w.setText(Integer.toString(startTimes));
         
        stage.show(); 
	}
	
	public void Month_R(Stage stage)
	{
		Scene scene = new Scene(new Group());  
        stage.setTitle("�±���");  
        stage.setWidth(600);  
        stage.setHeight(500);  
        
        Month.setMaxWidth(562);
        rnum_m = new Label("�����");
        rnum_m.setFont(new Font("Arial",20));
   
        TableColumn start = new TableColumn("��ʼʱ��");  
        start.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        start.setMinWidth(130);
        TableColumn end = new TableColumn("����ʱ��"); 
        end.setMinWidth(130);
        end.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        TableColumn start_T = new TableColumn("��ʼʱ�¶�"); 
        start_T.setCellValueFactory(new PropertyValueFactory<>("startTemperature"));
        start_T.setMinWidth(75);
        TableColumn end_T = new TableColumn("����ʱ�¶�");  
        end_T.setCellValueFactory(new PropertyValueFactory<>("endTemperature"));
        end_T.setMinWidth(75);
        TableColumn wind = new TableColumn("����"); 
        wind.setCellValueFactory(new PropertyValueFactory<>("wind"));
        wind.setMinWidth(75);
        TableColumn fee = new TableColumn("����"); 
        fee.setCellValueFactory(new PropertyValueFactory<>("fee"));
        fee.setMinWidth(65);
          
        Month.setItems(monthdata);
        Month.getColumns().addAll(start, end,start_T,end_T,wind,fee);  
   
        sumfeeLabel_m = new Label("�ܷ���");
        sumfeeLabel_m.setMinWidth(100);
        sumfee_m = new Label("0.00");
        sumfee_m.setMinWidth(100);
        timesLabel_m = new Label("���ػ�����");
        timesLabel_m.setMinWidth(100);
        times_m = new Label("0");
        times_m.setMinWidth(100);
        hb_month.getChildren().addAll(sumfeeLabel_m, sumfee_m, timesLabel_m, times_m);
        
        final VBox vbox = new VBox();  
        vbox.setSpacing(6);  
        vbox.setPadding(new Insets(10, 0, 0, 10));  
        vbox.getChildren().addAll(rnum_m,Month,hb_month);  
   
        ((Group) scene.getRoot()).getChildren().addAll(vbox);  
   
        stage.setScene(scene);  
        //stage.show(); 
	}
	
	public void ShowMonthReport(Stage stage,int roomnum,Timestamp start,Timestamp end)
	{
		LinkedList<Fee> infos = serverController.getReport(start, end, roomnum);
		monthdata.clear();
        rnum_m.setText("�����" + Integer.toString(roomnum));
        Timestamp temp_dstart;
        Timestamp temp_dend;
    	double temp_tstart;
    	double temp_tend;
    	int temp_wrun;
    	double temp_fee;
    	double temp_sumFee = 0;
        for(Fee dayInfo: infos)
        {
        	temp_dstart = dayInfo.getStartTime();
        	temp_dend = dayInfo.getEndTime();
        	temp_tstart = dayInfo.getStartTempterature();
        	temp_tend = dayInfo.getEndTempterature();
        	temp_wrun = dayInfo.getWind();
        	temp_fee = dayInfo.getFee();
        	
        	temp_sumFee += temp_fee;
        	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//�����ʽ������ʾ����
        	//Timestamp now = new Timestamp(System.currentTimeMillis());//��ȡϵͳ��ǰʱ��
        	String temp1 = df.format(temp_dstart);
        	String temp2 = df.format(temp_dend);
        	String temp3;
        	if(temp_wrun == 0)
        	{
        		temp3 = "���ٷ�";
        	}
        	else if(temp_wrun == 1)
        	{
        		temp3 = "���ٷ�";
        	}
        	else
        	{
        		temp3 = "���ٷ�";
        	}
        	
        	String temp4 = String.format("%.1f", temp_tstart);
        	String temp5 = String.format("%.1f", temp_tend);
        	String temp6 = String.format("%.2f", temp_fee);
        	monthdata.add(new ReportData(temp1, temp2, temp4, temp5, temp3,temp6));
        	
        }
        String temp6 = String.format("%.2f", temp_sumFee);
        int startTimes = serverController.getStartUpTimes(start, end, roomnum);
        sumfee_m.setText(temp6);
        times_m.setText(Integer.toString(startTimes));
         
        stage.show(); 
	}
	
	public void reset(Stage thisStage)
	{
		GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10, 10, 10, 10));
        
        Text schedule = new Text("����ģʽ��      ");
        grid.add(schedule, 2, 1, 6, 1);
        schedule.setId("label-text");
        
        ComboBox<String> modeTextField = new ComboBox<String>();
        modeTextField.getItems().addAll("����","����");
        modeTextField.setValue("����");
        grid.add(modeTextField, 8, 1);
        
        Text mode = new Text("����ģʽ��      ");
        //grid.add(mode, 2, 2, 6, 1);
        mode.setId("label-text");
        
        ComboBox<String> scheduleTextField = new ComboBox<String>();
        scheduleTextField.getItems().addAll("�����ȷ���", "�߷�������", "��ת����");
        scheduleTextField.setValue("�����ȷ���");
        //grid.add(scheduleTextField, 8, 2);
        
        Text rate = new Text("ˢ�¼����      ");
        grid.add(rate, 2, 2, 6,1);
        rate.setId("label-text");
        
        Spinner<Integer> rateTextField = new Spinner<Integer>(1, 30, 1,1);
        grid.add(rateTextField, 8, 2);
        
        Text limit = new Text("�������ޣ�     ");
        grid.add(limit, 2, 3, 6, 1);
        limit.setId("label-text");
        
        Spinner<Integer> limitTextField = new Spinner<Integer>(1, 30, 3,1);
        grid.add(limitTextField, 8, 3);
        
        Text price = new Text("�۸����ã�������");
        grid.add(price, 2, 4, 6, 1);
        price.setId("label-text");
        
        Spinner<Double> priceTextField = new Spinner<Double>(1, 30, 5,0.1);
        grid.add(priceTextField, 8, 4);
        
        Button btn = new Button("ȷ��");
        grid.add(btn, 10, 5);
        
        btn.setOnAction((ActionEvent e) -> {
        	try{
        		Strategy temp_stra;
        		String temp1 = scheduleTextField.getValue();
                if(temp1.equals("�����ȷ���"))
                {
                	temp_stra = Strategy.FIFO;
                }
                else if(temp1.equals("�߷�������"))
                {
                	temp_stra = Strategy.HWF;
                }
                else
                {
                	temp_stra = Strategy.RR;
                }
                Mode temp_mode;
        		String temp2 = modeTextField.getValue();
                if(temp2.equals("����"))
                {
                	temp_mode = Mode.COOL;
                }
                else
                {
                	temp_mode = Mode.HEAT;
                }
                int temp_rate = rateTextField.getValue();
                int temp_load = limitTextField.getValue();
                double temp_charge = priceTextField.getValue();
                serverController.setAttributes(temp_mode, temp_rate, temp_load, temp_stra, temp_charge);
        		thisStage.hide();
        		}
        		catch(Exception e1)
        		{
        		
        		}
        	});
        
        Scene scene = new Scene(grid, 350, 250);
        
        thisStage.setTitle("����");
        thisStage.setScene(scene);
        scene.getStylesheets().add
         (MainWindowUI.class.getResource("application.css").toExternalForm());
        thisStage.show();
	}
	
	public void show_rooms(Stage stage)
	{
		       
        roomdata.clear();
        int temp_room;
        State temp_state;
    	double temp_temperature;
    	Wind temp_wind;
    	//double temp_fee;
    	//double temp_consumption;
    	double temp_sumFee;
    	double temp_sumConsumption;
    	//Lock temp_lock;
        //roomdata.add(new Data("1","2","3","4","5","6"));
        for(RoomInfo roomInfo: (serverController.getServer().getRoomlist().getRooms()))
        {
        	temp_room = roomInfo.getRoomNumber();
        	temp_state = roomInfo.getState();
        	temp_temperature = roomInfo.getTemperature();
        	temp_wind = roomInfo.getWind();
        	//temp_fee = roomInfo.getFee();
        	//temp_consumption = roomInfo.getSumConsumption();
        	temp_sumFee = roomInfo.getSumFee();
        	temp_sumConsumption = roomInfo.getSumConsumption();
        	//temp_lock = roomInfo.getLock();
        	String temp1;
        	if(temp_state == State.RUNNING)
        	{
        		temp1 = "RUNNING";
        	}
        	else if(temp_state == State.STANDBY)
        	{
        		temp1 = "STANDBY";
        	}
        	else
        	{
        		temp1 = "WAITING";
        	}
        	String temp2;
        	if(temp_wind == Wind.HIGH)
        	{
        		temp2 = "���ٷ�";
        	}
        	else if(temp_wind == Wind.MEDIUM)
        	{
        		temp2 = "���ٷ�";
        	}
        	else
        	{
        		temp2 = "���ٷ�";
        	}
        	String temp3 = String.format("%.1f", temp_temperature);
        	String temp4 = String.format("%.2f", temp_sumFee);
        	roomdata.add(new Data(Integer.toString(temp_room), temp1, temp3, 
        			temp2, Double.toString(temp_sumConsumption), temp4));
        	
        }
         
        //stage.show(); 
	}
	
	public void setRooms(Stage stage)
	{
		
		Scene scene = new Scene(new Group());  
        stage.setTitle("��ǰ������Ϣ�б�");  
        stage.setWidth(520);  
        stage.setHeight(480);  
        
        table_room.setMaxWidth(482);
        
        TableColumn roomNum = new TableColumn("�����");
        roomNum.setMinWidth(80);
        roomNum.setCellValueFactory(new PropertyValueFactory<>("roomNumber"));
        TableColumn roomState = new TableColumn("״̬");  
        roomState.setMinWidth(80);
        roomState.setCellValueFactory(new PropertyValueFactory<>("state"));
        TableColumn roomTemp = new TableColumn("�¶�"); 
        roomTemp.setMinWidth(80);
        roomTemp.setCellValueFactory(new PropertyValueFactory<>("temperature"));
        TableColumn roomWind = new TableColumn("����");  
        roomWind.setMinWidth(80);
        roomWind.setCellValueFactory(new PropertyValueFactory<>("wind"));
        TableColumn room_con = new TableColumn("����");  
        room_con.setMinWidth(80);
        room_con.setCellValueFactory(new PropertyValueFactory<>("consumption"));
        TableColumn roomSum = new TableColumn("����"); 
        roomSum.setMinWidth(80);
        roomSum.setCellValueFactory(new PropertyValueFactory<>("fee"));
        
        //table_room.setItems(roomdata);
          
        roomdata.clear();
        int temp_room;
        State temp_state;
    	double temp_temperature;
    	Wind temp_wind;
    	//double temp_fee;
    	//double temp_consumption;
    	double temp_sumFee;
    	double temp_sumConsumption;
    	//Lock temp_lock;
        
        table_room.setItems(roomdata);
        table_room.getColumns().addAll(roomNum, roomState, roomTemp,roomWind,room_con,roomSum);
        
        final VBox vbox = new VBox();  
        vbox.setSpacing(5);  
        vbox.setPadding(new Insets(10, 0, 0, 10));  
        vbox.getChildren().addAll(table_room);  
   
        ((Group) scene.getRoot()).getChildren().addAll(vbox);  
   
        stage.setScene(scene);  
        //stage.show(); 

	}
	
	public void select_Day(Stage thisStage)
	{
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10, 10, 10, 10));
        thisStage.setTitle("ѡ������");  
        
        Label Year = new Label("���:");
        grid.add(Year, 0, 1);
        
        nian_d = new Spinner<Integer>(1990, Integer.parseInt(year_now), 2016,1); 
        grid.add(nian_d, 1, 1);
        
        Label Month = new Label("�·�:");
        grid.add(Month, 0, 2);
        
        yue_d = new ComboBox<String>();
        yue_d.getItems().addAll("01", "02", "03","04","05","06","07","08","09","10","11","12");
        yue_d.setValue(month_now);
        grid.add(yue_d, 1, 2);
        
        Label Day = new Label("����:");
        grid.add(Day, 0, 3);
        
        ri_d = new ComboBox<String>();
        ri_d.getItems().addAll("01", "02", "03","04","05","06","07","08","09","10","11","12","13","14","15"
        		,"16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31");
        ri_d.setValue(day_now);
        grid.add(ri_d, 1, 3);
        
        Label userName = new Label("�����:");
        grid.add(userName, 0, 4);
        
        userTextField_d = new TextField();
        grid.add(userTextField_d, 1, 4);
        
        Button daily_ok = new Button("ȷ��");
        grid.add(daily_ok, 2, 5);
        daily_ok.setOnAction((ActionEvent e) -> {
        	try {
        		String year_choose = Integer.toString(nian_d.getValue());
        		int year = Integer.parseInt(year_choose);
        		String month_choose = yue_d.getValue();
        		//month_choose.substring(0, month_choose.length()-2);
        		String day_choose = ri_d.getValue();
        		String roomnum_choose = userTextField_d.getText();
        		String time = year_choose + "-" + month_choose + "-" + day_choose + " 00:00:00";
    			Timestamp start = Timestamp.valueOf(time);
    			
    			//time = "2016-06-08 23:59:59";
    			time = year_choose + "-" + month_choose + "-" + day_choose + " 23:59:59";
    			Timestamp end = Timestamp.valueOf(time);
    			int roomnum = Integer.parseInt(roomnum_choose);
        		ShowDayReport(day_stage,roomnum,start,end);
        	    }
        		catch(Exception e1)
        		{
        		
        		}
        	});
        Scene scene = new Scene(grid, 450, 350);
        thisStage.setScene(scene);
        scene.getStylesheets().add
         (MainWindowUI.class.getResource("application.css").toExternalForm());
        //thisStage.show();
	}
	
	public void select_Month(Stage thisStage)
	{
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10, 10, 10, 10));
        thisStage.setTitle("ѡ���·�");  
        
        Label Year = new Label("���:");
        grid.add(Year, 0, 1);
        
        nian = new Spinner<Integer>(1990, Integer.parseInt(year_now), 2016,1);
        //nian = new Spinner<Integer>(1990, 2016, 2016,1);
        grid.add(nian, 1, 1);
        
        Label Month = new Label("�·�:");
        grid.add(Month, 0, 2);
        
        //ChoiceBox yue = new ChoiceBox(FXCollections.observableArrayList("1��", "2��", "3��","4��","5��","6��","7��","8��","9��","10��","11��","12��")); 
        yue = new ComboBox<String>();
        yue.getItems().addAll("01", "02", "03","04","05","06","07","08","09","10","11","12");
        yue.setValue(month_now);
        grid.add(yue, 1, 2);
        
        Label userName = new Label("�����:");
        grid.add(userName, 0, 3);
        
        userTextField_m = new TextField();
        grid.add(userTextField_m, 1, 3);
        
        Button daily = new Button("ȷ��");
        grid.add(daily, 2, 4);
        daily.setOnAction((ActionEvent e) -> {
        	try {
        		String year_choose = Integer.toString(nian.getValue());
        		int year = Integer.parseInt(year_choose);
        		String month_choose = yue.getValue();
        		//month_choose.substring(0, month_choose.length()-2);
        		String roomnum_choose = userTextField_m.getText();
        		String time = year_choose + "-" + month_choose + "-01 00:00:00";
    			Timestamp start = Timestamp.valueOf(time);
    			if(month_choose == "2")
        		{
        			if(year%4 == 0 && year%400 != 0)
        			{
        				time = year_choose + "-" + month_choose + "-" + "29 23:59:59";
        			}
        			else
        			{
        				time = year_choose + "-" + month_choose + "-" + "28 23:59:59";
        			}
        		}
    			else if(month_choose == "4" || month_choose == "6" || month_choose == "9" || month_choose == "11")
    			{
    				time = year_choose + "-" + month_choose + "-" + "30 23:59:59";
    			}
    			else
    			{
    				time = year_choose + "-" + month_choose + "-" + "31 23:59:59";
    			}
    			//time = "2016-06-08 23:59:59";
    			Timestamp end = Timestamp.valueOf(time);
    			int roomnum = Integer.parseInt(roomnum_choose);
    			ShowMonthReport(month_stage,roomnum,start,end);
        	    }
        		catch(Exception e1)
        		{
        		
        		}
        	});
        Scene scene = new Scene(grid, 450, 350);
        thisStage.setScene(scene);
        scene.getStylesheets().add
         (MainWindowUI.class.getResource("application.css").toExternalForm());
        //thisStage.show();
	}
	
	
	
	public void select_Week(Stage thisStage)
	{
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10, 10, 10, 10));
        thisStage.setTitle("ѡ���ܴ�");  
        
        Label Year = new Label("���:");
        grid.add(Year, 0, 1);
        
        nian_w = new Spinner<Integer>(1990, Integer.parseInt(year_now), 2016,1);
        grid.add(nian_w, 1, 1);
        
        Label Month = new Label("�·�:");
        grid.add(Month, 0, 2);
        
        yue_w = new ComboBox<String>();
        yue_w.getItems().addAll("01", "02", "03","04","05","06","07","08","09","10","11","12");
        yue_w.setValue(month_now);
        grid.add(yue_w, 1, 2);
        
        Label Day = new Label("�ܴ�:");
        grid.add(Day, 0, 3);
        
        zhou_w = new ComboBox<String>();
        grid.add(zhou_w, 1, 3);
        
        Label userName = new Label("�����:");
        grid.add(userName, 0, 4);
        
        userTextField_w = new TextField();
        grid.add(userTextField_w, 1, 4);
        
        Button daily = new Button("ȷ��");
        grid.add(daily, 2, 5);
        daily.setOnAction((ActionEvent e) -> {
        	try {
        		String year_choose = Integer.toString(nian_w.getValue());
        		int year = Integer.parseInt(year_choose);
        		String month_choose = yue_w.getValue();
        		//month_choose.substring(0, month_choose.length()-2);
        		String week_choose = zhou_w.getValue();
        		String roomnum_choose = userTextField_w.getText();
        		String day_choose = week_choose.substring(8,10);
        		String time = year_choose + "-" + month_choose + "-" + day_choose + " 00:00:00";
    			Timestamp start = Timestamp.valueOf(time);
    			
    			day_choose = week_choose.substring(20,22);
    			time = year_choose + "-" + month_choose + "-" + day_choose + " 23:59:59";
    			Timestamp end = Timestamp.valueOf(time);
    			int roomnum = Integer.parseInt(roomnum_choose);
        		ShowWeekReport(week_stage,roomnum,start,end);
        	    }
        		catch(Exception e1)
        		{
        		
        		}
        	});
        Scene scene = new Scene(grid, 450, 350);
        thisStage.setScene(scene);
        scene.getStylesheets().add
         (MainWindowUI.class.getResource("application.css").toExternalForm());
        //thisStage.show();
	}
	
	private String convertWeekByDate(String time_temp) {  
		 Date time = new Date();   
		 DateFormat sd = new SimpleDateFormat("yyyy-MM-dd");   
	        try {   
	            time = sd.parse(time_temp);   
	            //System.out.println(date.toString());   
	        } catch (Exception e) {   
	            e.printStackTrace();   
	        } 
		 SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd"); //����ʱ���ʽ
		 Calendar cal = Calendar.getInstance();  
		 cal.setTime(time);  
		//�ж�Ҫ����������Ƿ������գ���������һ����������ģ����������⣬���㵽��һ��ȥ��
		 int dayWeek = cal.get(Calendar.DAY_OF_WEEK);//��õ�ǰ������һ�����ڵĵڼ���
		 if(1 == dayWeek) {  
			 cal.add(Calendar.DAY_OF_MONTH, -1);  
		 }
		 //System.out.println("Ҫ��������Ϊ:"+sdf.format(cal.getTime())); //���Ҫ��������  
		 cal.setFirstDayOfWeek(Calendar.MONDAY);//����һ�����ڵĵ�һ�죬���й���ϰ��һ�����ڵĵ�һ��������һ  
		 int day = cal.get(Calendar.DAY_OF_WEEK);//��õ�ǰ������һ�����ڵĵڼ���
		 cal.add(Calendar.DATE, cal.getFirstDayOfWeek()-day);//���������Ĺ��򣬸���ǰ���ڼ�ȥ���ڼ���һ�����ڵ�һ��Ĳ�ֵ
		 String imptimeBegin = sdf.format(cal.getTime());  
		 //System.out.println("����������һ�����ڣ�"+imptimeBegin);  
		 cal.add(Calendar.DATE, 6);  
		 String imptimeEnd = sdf.format(cal.getTime());  
		 //System.out.println("�����������յ����ڣ�"+imptimeEnd);  
		 return imptimeBegin + "--" + imptimeEnd + " ";
	 }
	
	public String getNextDate(String day, String month, String year)
	{
		int temp = Integer.parseInt(day);
		temp += 7;
		int temp_year = Integer.parseInt(year);
		if(month == "2")
		{
			if(temp_year%4 == 0 && temp_year%400 != 0)
			{
				if(temp > 29)
				{
					return "";
				}
			}
			else
			{
				if(temp > 28)
				{
					return "";
				}
			}
		}
		else if(month == "4" || month == "6" || month == "9" || month == "11")
		{
			if(temp > 30)
			{
				return "";
			}
		}
		else
		{
			if(temp > 31)
			{
				return "";
			}
		}
		if(year.equals(year_now) && Integer.parseInt(month) == Integer.parseInt(month_now) && temp > (Integer.parseInt(day_now)+6) )
		{
			return "";
		}
		return Integer.toString(temp);
	}
}

