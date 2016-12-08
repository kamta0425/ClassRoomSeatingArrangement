package sample;


import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.Timer;
import java.util.TimerTask;

public class Main extends Application {
    TextField UserName,Passwd;
    Button btnlogin,btnadduser;
    GridPane grid;
    Stage window;
    Label nameLabel;
    DataAccesor dataacc;
    int paneIndex=0;
    Timer timer=null;

    Label lableuserinfo;
    Menu mainmenu;


    @Override
    public void start(Stage primaryStage) throws Exception {


        Text t2=new Text("Seating Arrangment System");
        t2.setId("heading5");
        GridPane.setConstraints(t2,10,20);
        Text t3=new Text("* Arrange Class Seats on Single click");

        t3.setId("heading6");

        GridPane.setConstraints(t3,10,21);
        Text t4=new Text("* Get Seating Arrangment/Attendance list pfd file");

        t4.setId("heading6");
        GridPane.setConstraints(t4,10,22);

        Text t5=new Text("* Search Student's Location by Roll no");

        t5.setId("heading6");
        GridPane.setConstraints(t5,10,23);

        Text t6=new Text("* Message Student their Seating Details");

        t6.setId("heading6");
        GridPane.setConstraints(t6,10,24);
        Text t7=new Text("* Provide Interface to get seat Detail on Mobile Using Wi-Fi");

        t7.setId("heading6");
        GridPane.setConstraints(t7,10,25);
        Label       lableuserinfo =new Label("") ;
          //
                                     lableuserinfo.setId("error-text1");

        Label lbheading = new Label("National Institute Of ");
        Label lbtechnology = new Label("Tech");
        GridPane.setConstraints(lbtechnology,9,0);

        lbtechnology.setId("text2");
        Label lbraipur=new Label("nology Raipur");
        GridPane.setConstraints(lbraipur,10,0);
        lbheading.setId("text2");
        lbraipur.setId("text2");

        window = primaryStage;
        window.setTitle("Seating Arrangement System");

        //GridPane with 10px padding around edge
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 20, 10, 10));
        grid.setVgap(8);
        grid.setHgap(0);
        grid.setAlignment(Pos.TOP_RIGHT);
        //Name Label - constrains use (child, column, row)
        nameLabel = new Label("Username:");

        GridPane.setConstraints(lbheading,8,0);

        GridPane.setConstraints(nameLabel, 9, 8);

        //Name Input
        TextField nameInput = new TextField();

        nameLabel.setId("bold-label");
        nameInput.setPromptText("Username");
        nameInput.setPrefWidth(80);
        GridPane.setConstraints(nameInput, 10, 8);

        //Password Label
        Label passLabel = new Label("Password:");
                    passLabel.setId("bold-label");
        GridPane.setConstraints(passLabel, 9, 9);

        //Password Input
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("password");
        passwordField.setPrefWidth(80);
        GridPane.setConstraints(passwordField, 10, 9);

        //Login
        //new user
        Button newuser = new Button("new User");
        newuser.setId("glass-grey");
        newuser.setOnAction(e -> {
            lableuserinfo.setText("");
            newuser();
        });

        Button loginButton = new Button("Log In");
        loginButton.setId("glass-grey");

        dataacc = new DataAccesor("com.mysql.jdbc.Driver", "jdbc:mysql://localhost:3306/vigyaan", "root", ""); // provide driverName, dbURL, user, password...
        loginButton.setOnAction(e -> {
            User user;

            try {

                if (dataacc.isUserExist(nameInput.getText().toString(), passwordField.getText().toString()) == true) {
                    user = dataacc.detail(nameInput.getText(), passwordField.getText());
                    System.out.println("Successfull Login");
                    MainView mainpag = new MainView(window);
                    mainpag.showpage();
                    lableuserinfo.setText("Login Successfull");
                } else {
                    System.out.println(" Login Unsuccesful ");
                    lableuserinfo.setText("Wrong Password/UserName");
                }
            } catch (SQLException e1) {
                e1.printStackTrace();
            } catch (ClassNotFoundException e1) {
                e1.printStackTrace();
            }

        });

        GridPane.setConstraints(loginButton, 9, 11);

        GridPane.setConstraints(newuser, 10, 11);
        GridPane.setConstraints(lableuserinfo,  10, 12);
        //Add everything to grid

        GridPane.setConstraints(passwordField, 10, 9);

        grid.getChildren().addAll(lbheading,lbtechnology,lbraipur,nameLabel, nameInput, passLabel, passwordField, loginButton, newuser, lableuserinfo,t2,t3,t4,t5,t6,t7);

        Scene scene = new Scene(grid, 1600, 700);

        grid.setId("pane");
        String[] panes = {"pane","pane1","pane2","pane3","pane4"};
        timer = new java.util.Timer();
        TimerTask timerTask =new TimerTask(){
            public void run() {
                Platform.runLater(new Runnable() {
                    public void run() {
                        paneIndex++;
                        if(paneIndex==panes.length)paneIndex=0;
                        grid.setId(panes[paneIndex]);
                    }
                });
            }
        };
        timer.scheduleAtFixedRate(timerTask, 3000, 4000);  //  ( , delay,duration)

        scene.getStylesheets().add(getClass().getResource("login.css").toExternalForm());
        //scene.getStylesheets().add("login.css");
        window.setScene(scene);
        window.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void newuser(){

        Label lbregister= new Label("Register");
        lbregister.setId("heading3");
        Stage window= new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("New User");

        window.setMinWidth(500);
        window.setMinHeight(400);
        Label label =new Label("");
        label.setId("error-text");
                label.setTextFill(Color.web("#0000ff"));
      // label.setId("bold-label1");

       // label.setText(Message);

        //GridPane with 10px padding around edge
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(8);
        grid.setHgap(10);
        grid.setAlignment(Pos.CENTER);
        //Name Label - constrains use (child, column, row)
        Label  lbuser  = new Label("Username:");
        GridPane.setConstraints(lbuser, 0, 3);
        lbuser.setId("bold-label1");

        //Name Input

        GridPane.setConstraints(lbregister,1,0);

        TextField tfuser = new TextField("");
        tfuser.setPromptText("Username");

        GridPane.setConstraints(tfuser, 1, 3);

        //Password Label
        Label lbpas = new Label("Password:");
        lbpas.setId("bold-label1");
        GridPane.setConstraints(lbpas, 0, 4);

        //Password Input
        PasswordField tfpass = new PasswordField();


        tfpass.setPromptText(" password");


        GridPane.setConstraints(tfpass, 1, 4);

        Label confirm = new Label("Confirm Password:       ");
        confirm.setId("bold-label1");
        GridPane.setConstraints(confirm, 0, 5);

        //Password Input
        PasswordField tfcpass = new PasswordField();

        tfcpass.setPromptText("confirm password");
                                         tfpass.setId("custom-text-field:focused");
        tfcpass.setPromptText("password");
        GridPane.setConstraints(tfcpass, 1, 5);
        Label email = new Label("Email:");
        email.setId("bold-label1");
        GridPane.setConstraints(email, 0, 6);

        TextField tfemail = new TextField("");

        //            bold-label1"

        tfemail.setPromptText("your email address");
                              tfemail.setId("custom-text-field");

        GridPane.setConstraints(tfemail, 1, 6);

        //ImageView iv1 = new ImageView();
        //Image image;
        //image = new Image("ph.png");
        //iv1.setImage(image);

         //GridPane.setConstraints(iv1,0,0);
        GridPane.setConstraints(label, 0, 8);
        Button  adduser = new Button("Add Details");

          adduser.setId("glass-grey");
        adduser.setOnAction(e ->{

           if(!(tfuser.getText().isEmpty() ||tfemail.getText().isEmpty()||tfpass.getText().isEmpty()))
           {

               if(!tfpass.getText().equals(tfcpass.getText()))
               {
                   label.setText("password Doesnot matched ");

               }

               else{
                   label.setText("");
                   String username = tfcpass.getText().toString();
                   String passwd = tfcpass.getText().toString();
                   String eml = tfcpass.getText().toString();

                   User newuser=new User(username,passwd,eml);
                   dataacc.adduser(newuser);
                   label.setText("User added");
                   timer.cancel();
                   window.close();
               }
           }
           else
               label.setText("Details are not Correct");


       });

        Button clear =new Button("Clear");
       clear.setId("glass-grey");
        clear.setOnAction(e->{

            this.newuser();
            window.close();

        });

        GridPane.setConstraints(adduser,2,9);

        GridPane.setConstraints(clear,2,10);
              grid.setId("pane1");
        grid.getChildren().addAll(lbregister,lbuser, tfuser,lbpas,tfpass,confirm,tfcpass,email,tfemail,adduser,label,clear);
        Scene scene =new Scene(grid,500,400);
        scene.getStylesheets().add(getClass().getResource("newuser.css").toExternalForm());
        window.setScene(scene);
        window.showAndWait();
    }
}
                  //     #F0F3FD