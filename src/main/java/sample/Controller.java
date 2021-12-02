package sample;

import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.util.Duration;

import java.io.File;
import java.net.InetAddress;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {


    public static Controller C;
    public static ServerState serverState = ServerState.STOPPED;
    static int ok = 1;
    static int maintenance = 1;
    public int port;





    @FXML
    private Label LabelServerAddress;

    @FXML
    private Label LabelServerState;

    @FXML
    private Label LabelServerPort;

    @FXML
    private TextField TextFieldPort;

    @FXML
    public TextField TextFieldDirectory;

    @FXML
    public TextField TextFieldMaintenanceFile;

    @FXML
    public Text messageText;

    @FXML
    private Button start_button;

    @FXML
    private Label ServerStateLabel;

    @FXML
    private Button maintenance_button;

    @FXML
    private Button btn_root;

    @FXML
    private Button btn_maintenance;

    @FXML
    void ButtonMaintenanceFile(ActionEvent event) {
        FileChooser fc = new FileChooser();

        fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("HTML Files", "*.html"));
        File selectedFile = fc.showOpenDialog(null);
        if(selectedFile != null){
            TextFieldMaintenanceFile.setText(selectedFile.getAbsolutePath());
        }
        else
        {
            messageText.setFill(Paint.valueOf("red"));
            messageText.setText("file is not valid");

            PauseTransition p = new PauseTransition(Duration.seconds(2));
            p.setOnFinished(e->messageText.setText(""));
            p.play();
        }

    }

    @FXML
    void ButtonRoot(ActionEvent event) {

        DirectoryChooser dc = new DirectoryChooser();
        File selectedFile = dc.showDialog(null);

        if(selectedFile != null){
            TextFieldDirectory.setText(selectedFile.getAbsolutePath());
        }
        else
        {
            messageText.setFill(Paint.valueOf("red"));
            messageText.setText("directory is not valid");

            PauseTransition p = new PauseTransition(Duration.seconds(2));
            p.setOnFinished(e->messageText.setText(""));
            p.play();
        }
    }

    @FXML
    void MaintenanceButton(ActionEvent event) throws Exception {

        /*if(TextFieldMaintenanceFile.getText().isEmpty() || TextFieldMaintenanceFile.getText() == null)
        {
            messageText.setFill(Paint.valueOf("red"));
            messageText.setText("please enter the maintenance file");

            PauseTransition p = new PauseTransition(Duration.seconds(2));
            p.setOnFinished(e->messageText.setText(""));
            p.play();
        }

        else*/

        if(maintenance == 1) {
            ServerStateLabel.setStyle("-fx-background-color:  #ffd700;");
            ServerStateLabel.setText("M  A  I  N  T  E  N  A  N  C  E");
            ServerStateLabel.setAlignment(Pos.CENTER);

            ImageView iv1 = new ImageView("/fxml/start.png");
            iv1.setFitHeight(100);
            iv1.setFitWidth(100);
            start_button.setGraphic(iv1);
            start_button.setShape(new Circle());

            ImageView iv2 = new ImageView("/fxml/stop.png");
            iv2.setFitHeight(100);
            iv2.setFitWidth(100);
            maintenance_button.setGraphic(iv2);
            maintenance_button.setShape(new Circle());

            serverState = ServerState.MAINTENANCE;
            setValueForLabels();


            Thread starting = new Thread() {


                public void run() {
                    try {
                        StartServer ss = new StartServer();
                        ss.Start_Server(port, TextFieldDirectory.getText(), TextFieldMaintenanceFile.getText());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }
            };

            starting.start();

            ok = 1;
            maintenance = 0;
        }
        else{
            maintenance_button.setVisible(false);
            ImageView iv1 = new ImageView("/fxml/start.png");
            iv1.setFitHeight(100);
            iv1.setFitWidth(100);
            start_button.setGraphic(iv1);
            start_button.setShape(new Circle());

            ServerStateLabel.setStyle("-fx-background-color: #ff0000;");
            ServerStateLabel.setText("S  T  O  P  P  E  D ");
            ServerStateLabel.setAlignment(Pos.CENTER);
            serverState = ServerState.STOPPED;
            setValueForLabels();
            maintenance = 1;
        }

    }

    @FXML
   public void StartButton() {

        if(TextFieldDirectory.getText().isEmpty() || TextFieldDirectory.getText() == null ||
           TextFieldMaintenanceFile.getText().isEmpty() || TextFieldMaintenanceFile.getText() == null)
        {
            messageText.setFill(Paint.valueOf("red"));
            messageText.setText("please enter the root directory and maintenance file");

            PauseTransition p = new PauseTransition(Duration.seconds(2));
            p.setOnFinished(e->messageText.setText(""));
            p.play();
        }

        else
            if (CheckForListeningPort() == 2) {

            if (ok == 1) {

                ImageView iv1 = new ImageView("/fxml/maintenance.png");
                iv1.setFitHeight(100);
                iv1.setFitWidth(100);
                maintenance_button.setGraphic(iv1);
                maintenance_button.setShape(new Circle());

                maintenance_button.setVisible(true);
                ServerStateLabel.setStyle("-fx-background-color:  #00ff00;");
                ServerStateLabel.setText("R  U  N  N  I  N  G");
                ServerStateLabel.setAlignment(Pos.CENTER);
                ServerStateLabel.setVisible(true);

                serverState = ServerState.RUNNING;
                setValueForLabels();
                try {



                    Thread starting = new Thread(){


                        public void run() {
                            try {
                                StartServer ss = new StartServer();
                                ss.Start_Server(port, TextFieldDirectory.getText(), TextFieldMaintenanceFile.getText());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }



                        }
                    };

                    starting.start();




                }catch (Exception ex){

                    System.err.println("Something went wrong");
                }
                maintenance = 1;
                ok = 0;


            } else {                             // stop
                maintenance_button.setVisible(false);
                ServerStateLabel.setStyle("-fx-background-color: #ff0000;");
                ServerStateLabel.setText("S  T  O  P  P  E  D ");
                ServerStateLabel.setAlignment(Pos.CENTER);


                ImageView iv2 = new ImageView("/fxml/start.png");
                iv2.setFitHeight(100);
                iv2.setFitWidth(100);
                start_button.setGraphic(iv2);
                start_button.setShape(new Circle());
                serverState = ServerState.STOPPED;
                setValueForLabels();

                ok = 1;

                System.out.println(ok);
            }
        }

    }



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        C = this;
        maintenance_button.setVisible(false);
        ServerStateLabel.setVisible(true);
        TextFieldMaintenanceFile.setDisable(false);
        TextFieldDirectory.setDisable(false);
        btn_root.setDisable(false);
        btn_maintenance.setDisable(false);

        setValueForLabels();


        ImageView iv2 = new ImageView("/fxml/start.png");
        iv2.setFitHeight(100);
        iv2.setFitWidth(100);
        start_button.setGraphic(iv2);
        start_button.setShape(new Circle());
    }


    private int CheckForListeningPort()
    {
        if(TextFieldPort.getText() == null || TextFieldPort.getText().isEmpty())
        {
            messageText.setFill(Paint.valueOf("red"));
            messageText.setText("please enter the server listening port");

            PauseTransition p = new PauseTransition(Duration.seconds(2));
            p.setOnFinished(e->messageText.setText(""));
            p.play();
            return 1;
        }
        else
        {
            try {

                 port = Integer.parseInt(TextFieldPort.getText());
                ImageView iv2 = new ImageView("/fxml/stop.png");
                iv2.setFitHeight(100);
                iv2.setFitWidth(100);
                start_button.setGraphic(iv2);
                start_button.setShape(new Circle());




            }catch (Exception ex){
                messageText.setFill(Paint.valueOf("red"));
                messageText.setText("invalid");

                PauseTransition p = new PauseTransition(Duration.seconds(2));
                p.setOnFinished(e->messageText.setText(""));
                p.play();
                return 0;
            }
        }
        return 2;
    }


    public void setValueForLabels()
    {
        InetAddress ip;
        String hostname = "";

        try{
            ip = InetAddress.getLocalHost();
            hostname = ip.getHostAddress();

        }catch (Exception ex){

        }

        if(serverState == ServerState.RUNNING)
        {
             LabelServerState.setText("RUNNING");
             LabelServerPort.setText(String.valueOf(port));
             LabelServerAddress.setText(hostname);
             TextFieldPort.setDisable(true);

            TextFieldMaintenanceFile.setDisable(false);
            TextFieldDirectory.setDisable(true);
            btn_root.setDisable(true);
            btn_maintenance.setDisable(false);

        }
        else
            if(serverState == ServerState.MAINTENANCE)
            {
                LabelServerState.setText("MAINTENANCE");
                LabelServerPort.setText(String.valueOf(port));
                LabelServerAddress.setText(hostname);
                TextFieldPort.setDisable(true);

                TextFieldMaintenanceFile.setDisable(true);
                TextFieldDirectory.setDisable(false);
                btn_root.setDisable(false);
                btn_maintenance.setDisable(true);

            }
            else
            {
                LabelServerState.setText("NOT RUNNING");
                LabelServerPort.setText("NOT RUNNING");
                LabelServerAddress.setText("NOT RUNNING");
                TextFieldPort.setDisable(false);
                TextFieldMaintenanceFile.setDisable(false);
                TextFieldDirectory.setDisable(false);
                btn_root.setDisable(false);
                btn_maintenance.setDisable(false);


            }

    }

    /*public  Controller getInstance()
    {
        C = this;
        return this;
    }*/

}
