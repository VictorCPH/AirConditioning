package application;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.*;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Main extends Application {
	private int roomNum;
	private Label runningState;
	private Label currentTemperature;
	private Label runningMode;
	private Label currentTurboSpeed;
	private Label currentCost;
	private Label currentEnergyConsumption;
	private Label sumCost;
	private Label sumEnergyConsumption;
	private Spinner<Double> userTemperature;
	private Controller defaultController;
	private Text actiontarget;

	@Override
	public void start(Stage primaryStage) {
		Font.loadFont(getClass().getResourceAsStream("/msyh.ttf"), 14);
		LoginScene(primaryStage);
	}

	/**
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String[] args) {
		launch(args);
	}

	public void LoginScene(Stage thisStage) {
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(40));

		Text scenetitle = new Text("��ӭʹ��");
		grid.add(scenetitle, 0, 0, 2, 1);
		scenetitle.setId("welcome-text");

		Label userName = new Label("�����:");
		grid.add(userName, 0, 1, 1, 1);

		TextField userTextField = new TextField();
		grid.add(userTextField, 1, 1,9,1);
		
		Button customIP = new Button("�Զ�IP");
		HBox hbcustomIP = new HBox(10);
		hbcustomIP.setAlignment(Pos.BOTTOM_RIGHT);
		hbcustomIP.getChildren().add(customIP);
		grid.add(hbcustomIP, 0, 2, 2, 1);
		
		TextField IPField = new TextField();
		IPField.setText("127.0.0.1");
		IPField.setVisible(false);
		grid.add(IPField, 0, 2, 1, 1);
		
		TextField portField = new TextField();
		portField.setText("6666");
		portField.setVisible(false);
		grid.add(portField, 1, 2, 1, 1);
		
		customIP.setOnAction((ActionEvent e) -> {
			customIP.setVisible(false);
			IPField.setVisible(true);
			portField.setVisible(true);
		});

		Button btn = new Button("����");
		HBox hbBtn = new HBox(10);
		hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
		hbBtn.getChildren().add(btn);
		grid.add(hbBtn, 2, 2, 8, 1);

		final Text actiontarget = new Text();
		grid.add(actiontarget, 0, 3, 2, 1);
		actiontarget.setId("actiontarget");

		btn.setOnAction((ActionEvent e) -> {
			try {
				roomNum = Integer.parseInt(userTextField.getText());
				if (IPField.getText().equals("")) IPField.setText("127.0.0.1");
				if (portField.getText().equals("")) portField.setText("6666");
				defaultController = Controller.getInstance(roomNum,IPField.getText(),Integer.parseInt(portField.getText()));
				if (defaultController.getState() == 1)
					mainScene(thisStage);
				else
					actiontarget.setText("�������ػ�ʧ�ܣ��������Ա��ϵ");
			} catch (Exception e1) {
				e1.printStackTrace();
				actiontarget.setText("��������ȷ�ķ����");
			}
		});

		Scene scene = new Scene(grid, 300, 250);

		thisStage.setTitle("��¼");
		thisStage.setScene(scene);
		scene.getStylesheets().add(Main.class.getResource("application.css").toExternalForm());
		thisStage.show();
	}

	public void mainScene(Stage thisStage) {
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(70));

		Text scenetitle = new Text("�յ�ң��");
		grid.add(scenetitle, 0, 0, 2, 1);
		scenetitle.setId("welcome-text");

		Label roomNumber = new Label("�����:" + Integer.toString(roomNum));
		grid.add(roomNumber, 0, 1, 2, 1);

		runningState = new Label("����״̬:    ");
		grid.add(runningState, 2, 1, 4, 1);

		currentTemperature = new Label("��ǰ�¶�:     ");
		grid.add(currentTemperature, 6, 1, 4, 1);

		runningMode = new Label("����ģʽ:����");
		grid.add(runningMode, 0, 2, 4, 1);

		currentTurboSpeed = new Label("��ǰ����:    ");
		grid.add(currentTurboSpeed, 6, 2, 2, 1);
		
		sumCost = new Label("�ܷ���:");
		HBox hb6 = new HBox();
		hb6.setId("hbox");
		hb6.getChildren().add(sumCost);
		grid.add(hb6, 2, 3, 4, 1);

		currentCost = new Label("��ǰ����:");
		HBox hb7 = new HBox();
		hb7.setId("hbox");
		hb7.getChildren().add(currentCost);
		grid.add(hb7, 6, 3, 2, 1);

		sumEnergyConsumption = new Label("���ܺ�:");
		HBox hb8 = new HBox();
		hb8.setId("hbox");
		hb8.getChildren().add(sumEnergyConsumption);
		grid.add(hb8, 2, 4, 4, 1);
		
		currentEnergyConsumption = new Label("��ǰ�ܺ�:");
		HBox hb9 = new HBox();
		hb9.setId("hbox");
		hb9.getChildren().add(currentEnergyConsumption);
		grid.add(hb9, 6, 4, 2, 1);

		Label settings = new Label("�趨:");
		grid.add(settings, 1, 5, 4, 1);

		Label label1 = new Label("Ŀ���¶�:");
		grid.add(label1, 0, 6, 2, 1);

		userTemperature = new Spinner<Double>(18, 25, 25,0.5);
		grid.add(userTemperature, 2, 6, 4, 1);

		Label label2 = new Label("Ŀ�����:");
		grid.add(label2, 0, 7, 2, 1);

		ComboBox<String> userTurboSpeed = new ComboBox<String>();
		userTurboSpeed.getItems().addAll("���ٷ�", "���ٷ�", "���ٷ�");
		userTurboSpeed.setValue("���ٷ�");
		grid.add(userTurboSpeed, 2, 7, 4, 1);

		Button btn = new Button("ȷ��");
		HBox hbBtn = new HBox(10);
		hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
		hbBtn.getChildren().add(btn);
		grid.add(hbBtn, 5, 8);

		actiontarget = new Text();
		grid.add(actiontarget, 0, 8, 4, 1);
		actiontarget.setId("actiontarget");

		btn.setOnAction((ActionEvent e) -> {
			try {
				double temp1 = userTemperature.getValue();
				if (userTurboSpeed.getValue().equals("���ٷ�")) {
					if (!defaultController.sendDesiredTemperatureAndWind(temp1, Wind.LOW))
						actiontarget.setText("�������ػ�ʧ�ܣ��������Ա��ϵ");
					else
						actiontarget.setText("");
				} else if (userTurboSpeed.getValue().equals("���ٷ�")) {
					if (!defaultController.sendDesiredTemperatureAndWind(temp1, Wind.MEDIUM))
						actiontarget.setText("�������ػ�ʧ�ܣ��������Ա��ϵ");
					else
						actiontarget.setText("");
				} else if (userTurboSpeed.getValue().equals("���ٷ�")) {
					if (!defaultController.sendDesiredTemperatureAndWind(temp1, Wind.HIGH))
						actiontarget.setText("�������ػ�ʧ�ܣ��������Ա��ϵ");
					else
						actiontarget.setText("");
				} else
					actiontarget.setText("��ѡ�����");
			} catch (Exception e1) {
				actiontarget.setText("��������ȷ���¶�ֵ");
			}
		});

		Scene scene = new Scene(grid, 650, 500);

		thisStage.setTitle("������");
		thisStage.setScene(scene);
		scene.getStylesheets().add(Main.class.getResource("application.css").toExternalForm());
		thisStage.show();
		defaultController.registerObserver(this);
		thisStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
		    @Override
		    public void handle(WindowEvent event) {
		    	defaultController.closeConnection();
		    	Platform.exit();
		    	System.exit(0);
		    }
		});
	}

	public void update(Client client) {
		if (client.getState() == State.RUNNING)
			runningState.setText("����״̬:������");
		else if (client.getState() == State.WAITING)
			runningState.setText("����״̬:�ȴ���");
		else
			runningState.setText("����״̬:������");

		currentTemperature.setText(String.format("��ǰ�¶�:%.2f",client.getTemperature()));

		if (client.getMode() == Mode.COOL){
			if(runningMode.getText().equals("����ģʽ:����")){
				userTemperature.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(18,25,25,0.5));
			}
			runningMode.setText("����ģʽ:����");
			}
		else{
			if(runningMode.getText().equals("����ģʽ:����")){
				userTemperature.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(25,30,25,0.5));
			}
			runningMode.setText("����ģʽ:����");
			}

		if (client.getWind() == Wind.LOW)
			currentTurboSpeed.setText("��ǰ����:����");
		else if (client.getWind() == Wind.MEDIUM)
			currentTurboSpeed.setText("��ǰ����:����");
		else
			currentTurboSpeed.setText("��ǰ����:����");

		sumCost.setText(String.format("�ܷ���:%.1f",client.getSumFee()));

		sumEnergyConsumption.setText(String.format("���ܺ�:%.1f" ,client.getSumConsumption()));
		
		currentCost.setText(String.format("��ǰ����:%.1f" ,client.getFee()));

		currentEnergyConsumption.setText(String.format("��ǰ�ܺ�:%.1f" ,client.getConsumption()));
		if (!client.getPower()) actiontarget.setText("���ػ����ܵ��ߣ�");
		else if (actiontarget.getText().equals("���ػ����ܵ��ߣ�")) actiontarget.setText("");
	}
}
