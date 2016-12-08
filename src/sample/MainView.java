package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.*;

public class MainView {

    class SingleReference{
        TextField tfStart,tfEnd,tfSliderStart,tfSliderEnd;
        ChoiceBox<String> cbBranch;
        ChoiceBox<Integer> cbSem;
        Label message=null;

        public SingleReference(ChoiceBox<String> choiceBranch, ChoiceBox<Integer> choicesem, TextField tfstartingroll,
                               TextField tfendingroll,Label message,TextField tfSliderStart,TextField tfSliderEnd) {
            this.tfStart=tfstartingroll;
            this.tfEnd=tfendingroll;
            this.cbBranch=choiceBranch;
            this.cbSem=choicesem;
            this.message=message;
            this.tfSliderStart=tfSliderStart;
            this.tfSliderEnd=tfSliderEnd;
        }
    }

    int k = 0;
    DataAccesor dataaccessor;
    Stage window;
    BorderPane layout;
    GridPane grid;
    MenuBar mb;
    Button buttnaddstud;
    ClassDetail clssdetails;
    ClassBranch cb;
    List<ClassDetail> classlist;
    Label label= new Label("");

    boolean btn;

    public MainView(Stage window) {
        this.window = window;
    }

    public void showpage() throws SQLException, ClassNotFoundException {
        dataaccessor = new DataAccesor("com.mysql.jdbc.Driver", "jdbc:mysql://localhost:3306/vigyaan", "root", ""); // provide driverName, dbURL, user, password..
        classlist = dataaccessor.getclasslist();
        int r = classlist.size();
        Menu filemenu = new Menu("Classes Details");

        MenuItem newitem;

        for (int i = 0; i < r; i++) {
            String name = classlist.get(i).classname;
            newitem = new MenuItem(name);
            newitem.setOnAction(e -> {
                try {
                    System.out.println(name+" ");
                    String [][] a = Algo(name);
                    new ClassArranged(name).ShowArangment(a);
                } catch (SQLException e1) {
                    //e1.printStackTrace();
                    System.out.println("SQLException in showpage");
                } catch (ClassNotFoundException e1) {
                    //e1.printStackTrace();
                    System.out.println("ClassNotFoundException in showpage");
                }
            });

            filemenu.getItems().add(newitem);
        }


        Menu showFiles = new Menu("Show Files");

        MenuItem nitem;

        for (int i = 0; i < r; i++) {
            String name = classlist.get(i).classname;
            nitem = new MenuItem(name);
            nitem.setOnAction(e -> {
                try {
                    Word1 w=new Word1(name,Algo(name));
                    w.createPDFSeating();
                    w.pdfAttendence(name);
                    showPopupDeatil(name);
                } catch (SQLException e1) {
                    //e1.printStackTrace();
                    System.out.println("SQLException in menuItem Show Files");
                } catch (ClassNotFoundException e1) {
                    //e1.printStackTrace();
                    System.out.println("ClassNotFoundException in menuItem Show Files");
                }
            });
            showFiles.getItems().add(nitem);
        }

        Button btnserach = new Button("");
        Image searchimage = new Image(getClass().getResourceAsStream("search.png"));
        btnserach.setGraphic(new ImageView(searchimage));
        TextField tfsearch= new TextField("");
        tfsearch.setPromptText("Roll NO");
        Menu searchmenubtn= new Menu("");
        Label lbsearch= new Label("search:");

        Menu menulabel=new Menu();
        menulabel.setGraphic(lbsearch);
        Menu searchtfmenu= new Menu("");
        btnserach.visibleProperty().bind( tfsearch.textProperty().isEmpty().not() );

        btnserach.setOnAction(e->{
            int k=Integer.parseInt(tfsearch.getText());
            String classname=null;
            try {
                classname = dataaccessor.searchDetail(k);
                System.out.println("Class name found after search= "+classname);
                if(classname==null){
                    label.setText("Roll no not found");
                    label.setTextFill(Color.web("#ff0000"));
                }else {
                    String[][] a = Algo(classname);
                    ClassArranged ca = new ClassArranged(classname);
                    ca.showsearchresult(a, k);
                    System.out.print(k + "rollno ");
                    clearEverything();
                }
                //System.out.print(a);

            } catch (SQLException e1) {
                //e1.printStackTrace();
                System.out.println("SQLException in search button click");
                label.setText("Enter the correct RollNo.");
            } catch (ClassNotFoundException e1) {
                label.setText("Enter the correct RollNo.");
                System.out.println("ClassNotFoundException in search button click");
            }catch (NullPointerException e1) {
                label.setText("Enter the correct RollNo.");
                System.out.println("NullPointerException in search button click");
            }catch (Exception e1) {
                //e1.printStackTrace();
                System.out.println("Exception in search button click "+e1);
                label.setText("Enter the correct RollNo.");
            }
        });

        searchmenubtn.setGraphic(btnserach);
        searchtfmenu.setGraphic(tfsearch);
        Menu menuattendence = new Menu("Attendence List");
        MenuItem attendenceitem;

        for (int i = 0; i < r; i++) {
            String name = classlist.get(i).classname;
            attendenceitem = new MenuItem(name);            attendenceitem.setOnAction(e -> {
                //attendencelist(name);
                Word1 w= null;
                try {
                    w = new Word1(name,Algo(name));
                } catch (SQLException e1) {
                    //e1.printStackTrace();
                    System.out.println("SQLException in word object creating in Attendance List");
                }
                try {
                    w.createAttendanceDocx(name);
                    w.pdfAttendence(name);
                    Runtime.getRuntime().exec("explorer.exe /select," + "C:\\Users\\HP1\\Desktop\\" + name + "Attendance.pdf");
                } catch (SQLException e1) {
                    //e1.printStackTrace();
                    System.out.println("SQLException in Attendance List menu");
                } catch (ClassNotFoundException e1) {
                    //e1.printStackTrace();
                    System.out.println("ClassNotFoundException Attendance List menu");
                } catch (IOException e1) {
                    //e1.printStackTrace();
                    System.out.println("IOException in Attendance List menu");
                }
            });
            menuattendence.getItems().add(attendenceitem);
        }
        Menu deletemenu = new Menu("Delete");
        MenuItem item;

        for (int i = 0; i < r; i++) {
            String name = classlist.get(i).classname;
            item = new MenuItem(name);
            item.setOnAction(e -> {
                showConfirmDialog(name);
                clearEverything();
            });
            deletemenu.getItems().add(item);
        }

        Menu menuSend = new Menu("Send");
        menuSend.setId("menu");

        MenuItem itemEmail =new MenuItem("Email");
        menuSend.getItems().add(itemEmail);

        itemEmail.setOnAction(e->{
            sendEmail();
            String response = "Email menuItem called";
            System.out.println(response);
        });

        MenuItem itemMessage =new MenuItem("Message");
        menuSend.getItems().add(itemMessage);

        itemMessage.setOnAction(e->{
            //String response = SMSSender("kamta0425", "kamta0425", "8827768640", "Dear 14118029, your password is RoomNo-IT1RollNo-14118045", "WEBSMS", "0");//14118029 RoomNo-SN2RollNo-14118029
            String response = "Message menuItem called";
            System.out.println(response);
        });


        mb = new MenuBar();

        mb.setId("menu-bar");

        mb.getMenus().addAll(filemenu, showFiles, deletemenu,menulabel,searchtfmenu,searchmenubtn,menuattendence,menuSend);
        //mb.getMenus().addAll(filemenu, showFiles, deletemenu);
        classAdding();
         layout.setId("pane4");
        Scene scene = new Scene(layout, 1350, 750);

        scene.getStylesheets().add(getClass().getResource("mainview.css").toExternalForm());
        window.setScene(scene);
        window.setTitle("Class Room Allocation");
        window.show();
    }



    private void showConfirmDialog(String name) {
        Stage window= new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Delete Class");
        window.setMinWidth(500);
        window.setMinHeight(100);
        Label label =new Label();
        label.setText("Are you sure want to delete");
        Button bCancel = new Button("Cancel");

        bCancel.setId("glass-grey");
        bCancel.setOnAction(e ->{  window.close();});

        Button bOK = new Button("OK");
        bOK.setId("glass-gray");
        bOK.setOnAction(e ->{
            try {
                dataaccessor.deleteclass(name);
                clearEverything();
            }catch (SQLException e1) {
                //e1.printStackTrace();
                System.out.println("SQLException onclick OK");
            }
            window.close();}
        );

        HBox layout1= new HBox(10);
        layout1.getChildren().addAll(label,bOK,bCancel);
        layout1.setAlignment(Pos.BOTTOM_CENTER);
        Scene scene =new Scene(layout1);
        scene.getStylesheets().add(getClass().getResource("mainview.css").toExternalForm());
        window.setScene(scene);
        window.showAndWait();
    }


    private void classAdding() {
        grid = new GridPane();
        grid.setPadding(new Insets(5, 10, 10, 2));
        grid.setVgap(8);
        grid.setHgap(20);
        grid.setAlignment(Pos.TOP_LEFT);

        Label labCls = new Label("CLASS INFORMATION:");

        GridPane.setConstraints(labCls, 1, 1);
        //classname
        Label strenthMessage = new Label("");
        Label strenthMessage1= new Label("");

        Label nameLabel = new Label("Class Name:");
        GridPane.setConstraints(nameLabel, 1, 3);

        TextField tfclassname = new TextField();
        GridPane.setConstraints(tfclassname, 2, 3);
        tfclassname.textProperty().addListener((observable, oldValue, newValue) -> {
            if(isClassAlloted(tfclassname.getText().toString())){

                strenthMessage.setTextFill(Color.web("#ff0000"));
                strenthMessage.setText("Class already allocated");
                label.setText("");
                strenthMessage1.setText("");

            }else{
                strenthMessage.setText("");
                strenthMessage.setTextFill(Color.web("#0000ff"));
            }
        });

        GridPane.setConstraints(strenthMessage,2,20);
        GridPane.setConstraints(strenthMessage1,1,20);
        strenthMessage.setTextFill(Color.web("#0000ff"));
        strenthMessage1.setTextFill(Color.web("#0000ff"));
        Label strengthLabel = new Label("Strength:");
        GridPane.setConstraints(strengthLabel, 3, 3);

        TextField tfclassstrenth = new TextField();
        GridPane.setConstraints(tfclassstrenth, 4, 3);
        tfclassstrenth.setPrefWidth(50);
		tfclassstrenth.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                strenthMessage1.setText("Student in each Section ");
                strenthMessage.setText("should be <= " + (Integer.parseInt(tfclassstrenth.getText().toString()) / 4));
            }catch(NumberFormatException e){
                System.out.println("NumberFormatExceptionin tfclassstrenth.textproperty addListener");
            }catch(Exception e){
                System.out.println("Exception in tfclassstrenth.textproperty addListener");
            }
		});

        Label Collumn = new Label("Collumn:");
        GridPane.setConstraints(Collumn, 5, 3);

        ChoiceBox<Integer> choiceBox = new ChoiceBox<>();
        choiceBox.getItems().add(6);
        choiceBox.getItems().add(8);
        choiceBox.getItems().add(10);
        choiceBox.getItems().add(12);
        choiceBox.setValue(6);
        GridPane.setConstraints(choiceBox,6,3);
        Label Studentinfo = new Label("Student Info");

        GridPane.setConstraints(label, 1, 20);

        ArrayList<SingleReference> list = new ArrayList<SingleReference>();

        buttnaddstud = new Button("Add");
        buttnaddstud.setId("glass-grey");
        buttnaddstud.setOnAction(e -> {
            String clsnmae = tfclassname.getText();
            String clsstrength = tfclassstrenth.getText();
            if(!clsstrength.isEmpty() && clsstrength!=null)
                strenthMessage1.setText("Student in each section");
                strenthMessage.setText(" should be <= "+(Integer.parseInt(clsstrength)/4));
            int clsscol = choiceBox.getValue();
            int strength=0,maxStudent=Integer.parseInt(clsstrength)/4;

            for(SingleReference ref : list){
                try {
                    if(ref.tfSliderEnd.getText().isEmpty() || ref.tfSliderStart.getText().isEmpty())
                        strength =  Integer.parseInt(ref.tfEnd.getText()) - Integer.parseInt(ref.tfStart.getText())+1 ;
                    else{
                        strength =  Integer.parseInt(ref.tfEnd.getText()) - Integer.parseInt(ref.tfStart.getText())+2
                                +Integer.parseInt(ref.tfSliderEnd.getText()) - Integer.parseInt(ref.tfSliderStart.getText()) ;
                    }
                } catch (NumberFormatException e1) {
                    //e1.printStackTrace();
                    System.out.println("NumberFormatException in classAdding");
                }
                if(strength>maxStudent){
                    ref.message.setTextFill(Color.web("#ff0000"));
                    ref.message.setText("Limit Overflowed");
                }else{
                    ref.message.setTextFill(Color.web("#000000"));
                    ref.message.setText("");
                }
            }

            if (clsnmae.isEmpty() == true || clsstrength.isEmpty() == true) {
                label.setTextFill(Color.web("#ff0000"));
                label.setText("Enter the class Details fields cannot be empty");
            }else if(isClassAlloted(tfclassname.getText().toString())){
                label.setTextFill(Color.web("#ff0000"));
                label.setText("class already allocated");
                strenthMessage1.setText("");
                strenthMessage.setText("");
            }
            else {
                label.setText("");
                if (k == 0) {
                    label.setText("");
                    clssdetails = new ClassDetail(clsnmae, Integer.parseInt(clsstrength), clsscol);
                }
                SingleReference ref = addstudents();
                ref.tfStart.textProperty().addListener((observable, oldValue, newValue) -> {   //Text Watcher
                    int strength1=0;
                    try {
                        if(ref.tfSliderEnd.getText().isEmpty())
                            strength1 =  Integer.parseInt(ref.tfEnd.getText()) - Integer.parseInt(ref.tfStart.getText())+1 ;
                        else{
                            strength1 =  Integer.parseInt(ref.tfEnd.getText()) - Integer.parseInt(ref.tfStart.getText())+2
                                    +Integer.parseInt(ref.tfSliderEnd.getText()) - Integer.parseInt(ref.tfSliderStart.getText()) ;
                        }
                    } catch (NumberFormatException e1) {
                        //e1.printStackTrace();
                        System.out.println("NumberFormatException in TextWatcher tfStart");
                    }
                    if(strength1>maxStudent){
                        ref.message.setTextFill(Color.web("#ff0000"));
                        ref.message.setText("Limit Overflowed");
                    }else{
                        ref.message.setTextFill(Color.web("#000000"));
                        ref.message.setText("");
                    }
                });
                ref.tfEnd.textProperty().addListener((observable, oldValue, newValue) -> { //Text Watcher
                    int strength2=0;
                    try {
                        if(ref.tfSliderEnd.getText().isEmpty())
                        strength2 =  Integer.parseInt(ref.tfEnd.getText()) - Integer.parseInt(ref.tfStart.getText())+1 ;
                        else{
                            strength2 =  Integer.parseInt(ref.tfEnd.getText()) - Integer.parseInt(ref.tfStart.getText())+2
                                    +Integer.parseInt(ref.tfSliderEnd.getText()) - Integer.parseInt(ref.tfSliderStart.getText()) ;
                        }
                    } catch (NumberFormatException e1) {
                        //e1.printStackTrace();
                        System.out.println("NumberFormatException in TextWatcher tfEnd");
                    }
                    if(strength2>maxStudent){
                        ref.message.setTextFill(Color.web("#ff0000"));
                        ref.message.setText("Limit Overflowed");
                    }else{
                        ref.message.setText("");
                    }
                });
                ref.tfSliderStart.textProperty().addListener((observable, oldValue, newValue) -> {     //Text Watcher
                    int strength3=0;
                    try {
                        if(ref.tfSliderEnd.getText().isEmpty())
                            strength3 =  Integer.parseInt(ref.tfEnd.getText()) - Integer.parseInt(ref.tfStart.getText())+1 ;
                        else{
                            strength3 =  Integer.parseInt(ref.tfEnd.getText()) - Integer.parseInt(ref.tfStart.getText())+2
                                    +Integer.parseInt(ref.tfSliderEnd.getText()) - Integer.parseInt(ref.tfSliderStart.getText()) ;
                        }
                    } catch (NumberFormatException e1) {
                        //e1.printStackTrace();
                        System.out.println("NumberFormatException in TextWatcher tfSliderStart");
                    }
                    if(strength3>maxStudent){
                        ref.message.setTextFill(Color.web("#ff0000"));
                        ref.message.setText("Limit Overflowed");
                    }else{
                        ref.message.setText("");
                    }
                });
                ref.tfSliderEnd.textProperty().addListener((observable, oldValue, newValue) -> { //Text Watcher
                    int strength4=0;
                    try {
                        if(ref.tfSliderEnd.getText().isEmpty())
                            strength4 =  Integer.parseInt(ref.tfEnd.getText()) - Integer.parseInt(ref.tfStart.getText())+1 ;
                        else{
                            strength4 =  Integer.parseInt(ref.tfEnd.getText()) - Integer.parseInt(ref.tfStart.getText())+2
                                    +Integer.parseInt(ref.tfSliderEnd.getText()) - Integer.parseInt(ref.tfSliderStart.getText()) ;
                        }
                    } catch (NumberFormatException e1) {
                        //e1.printStackTrace();
                        System.out.println("\nNumberFormatException in TextWatcher tfSliderEnd");
                    }
                    if(strength4>maxStudent){
                        ref.message.setTextFill(Color.web("#ff0000"));
                        ref.message.setText("Limit Overflowed");
                    }else{
                        ref.message.setText("");
                    }
                });
                list.add(ref);
            }
        });

        Button buttsubmit = new Button("Submit");

        buttsubmit.setId("glass-grey");
        buttsubmit.setOnAction(e -> {
            String clsnmae = tfclassname.getText();
            String clsstrength = tfclassstrenth.getText();

            int clsscol = choiceBox.getValue();
            if (clsnmae.isEmpty() == true || clsstrength.isEmpty() == true){
                label.setTextFill(Color.web("#ff0000"));
                label.setText("Enter the class Details fields cannot be empty");
                strenthMessage.setText("");
                strenthMessage1.setText("");
            }
            else {
                int flag=0;
                int strength=0,maxStudent=Integer.parseInt(clsstrength)/4;
                strenthMessage1.setTextFill(Color.web("#0000ff"));
                strenthMessage1.setText("Student in each Section");
                strenthMessage.setText("should be <= "+(Integer.parseInt(clsstrength)/4));
                for(SingleReference ref : list){
                    try {
                        strength =  Integer.parseInt(ref.tfEnd.getText()) - Integer.parseInt(ref.tfStart.getText())+1 ;
                    } catch (NumberFormatException e1) {
                        //e1.printStackTrace();
                        System.out.println("\nNumberFormatException in Submit button strength calculating");
                    }
                    if(strength>maxStudent){
                        ref.message.setTextFill(Color.web("#ff0000"));
                        ref.message.setText("Limit Overflowed");
                    }else{
                        flag++;
                        ref.message.setTextFill(Color.web("#000000"));
                        ref.message.setText("");
                    }
                }
                ArrayList<ClassBranch> classBranch =new ArrayList<ClassBranch>();
                if(flag==list.size()){
                    label.setText("");
                    for(SingleReference ref : list){
                        if( ref.tfStart.getText().isEmpty() ||  ref.tfEnd.getText().isEmpty()){
                            continue;
                        }else{
                            int start = 0,end = 0,sem = 0,sliderStart = 0,sliderEnd = 0;
                            String branch=null;
                            try {
                                start = Integer.parseInt(ref.tfStart.getText().toString());
                                end = Integer.parseInt(ref.tfEnd.getText().toString() );
                                sem = ref.cbSem.getValue();
                                branch=ref.cbBranch.getValue();
                                if(!(ref.tfSliderStart.getText().toString().isEmpty()))sliderStart = Integer.parseInt(ref.tfSliderStart.getText().toString());
                                if(!(ref.tfSliderEnd.getText().toString().isEmpty()))sliderEnd = Integer.parseInt(ref.tfSliderEnd.getText().toString());
                            } catch (NumberFormatException e1) {
                                //e1.printStackTrace();
                                System.out.println("NumberFormatException in Submit button strength calculating block");
                            }
                            classBranch.add(new ClassBranch(tfclassname.getText(),branch,sem,start,end,sliderStart,sliderEnd));
                        }
                    }
                    System.out.println("ArrayList<ClassBranch> size = "+classBranch.size());
                    submittion(clssdetails,classBranch);
                    try {
                        Word1 w=new Word1(clssdetails.classname,Algo(clssdetails.classname));
                        w.createWordFile();
                        w.createPDFSeating();
                        w.pdfAttendence(clssdetails.classname);
                        w.createAttendanceDocx(clssdetails.classname);
                    }
                    catch (SQLException e1) {
                        //e1.printStackTrace();
                        System.out.println("SQLException in Submit button");
                    } catch (ClassNotFoundException e1) {
                        //e1.printStackTrace();
                        System.out.println("ClassNotFoundException in Submit button");
                    }
                    showCompletionDialog();
                }
            }
        });

        Button bClear = new Button("Clear");
        bClear.setId("glass-grey");
        bClear.setOnAction(e -> {
            clearEverything();
        });

        GridPane.setConstraints(buttnaddstud, 8, 20);
        GridPane.setConstraints(buttsubmit, 9, 20);
        GridPane.setConstraints(bClear, 7, 20);
        grid.getChildren().addAll(labCls, nameLabel, strengthLabel, tfclassname, tfclassstrenth, Collumn, choiceBox, buttnaddstud, buttsubmit, bClear,strenthMessage,strenthMessage1);

        GridPane.setConstraints(Studentinfo, 1,4);
        grid.getChildren().addAll(label, Studentinfo);


        layout = new BorderPane();
        //layout.getChildren().add(mmb);
        layout.setTop(mb);

        // layout.setBottom(buttnaddclass);
        layout.setCenter(grid);
    }

    private boolean isClassAlloted(String name) {
        boolean status =false;
        for(ClassDetail detail: classlist){
            if(detail.classname.equals(name))return true;
        }
        return status;
    }

    private void showCompletionDialog() {
        Stage window= new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Class Add Status");
        window.setMinWidth(500);
        window.setMinHeight(100);
        Label label =new Label();
        label.setText("Class Added Successfully");

        Button bOK = new Button("OK");
        bOK.setOnAction(e ->{
            window.close();}
        );

        VBox layout1= new VBox(10);
        layout1.getChildren().addAll(label,bOK);
        layout1.setAlignment(Pos.BOTTOM_CENTER);
        Scene scene =new Scene(layout1);
        window.setScene(scene);
        window.showAndWait();
    }

    private void submittion(ClassDetail cl, List<ClassBranch> classBranch) {
        dataaccessor.addClass(cl);
        dataaccessor.addClass(classBranch);
        clearEverything();
    }

    private void clearEverything() {
        MainView mp = new MainView(window);
        try {
            mp.showpage();
        } catch (SQLException e1) {
            //e1.printStackTrace();
            System.out.println("SQLException in clearEverything");
        } catch (ClassNotFoundException e1) {
            //e1.printStackTrace();
            System.out.println("ClassNotFoundException in clearEverything");
        }
    }

    private SingleReference addstudents() {

        if (k == 13) {
            label.setText("Maximum Students Adding reached");
            buttnaddstud.setDisable(true);
        }


        Text tsliderstart = null;
        Text tsliderend = null;
        Text tsemester = null;
        Text tbranch = null;
        Text tend = null;
        Text tstart = null;


        if (k == 0) {

            tsliderstart = new Text("Start Roll");
            tsliderend = new Text("Ending Roll");

            tend = new Text("End Roll");

            tsemester = new Text("Semester");

            tbranch = new Text("Branch");

            tstart= new Text("Starting Roll");
            GridPane.setConstraints(tstart, 4, 6);
            GridPane.setConstraints(tend, 5, 6);

            GridPane.setConstraints(tbranch, 2, 6);
            GridPane.setConstraints(tsemester, 3, 6);

            GridPane.setConstraints(tsliderstart, 8, 6);
            GridPane.setConstraints(tsliderend, 9, 6);
        }


        k++;
        Label section = new Label("Section: " + (k ));


        ChoiceBox<String> choiceBranch = new ChoiceBox<>();

        choiceBranch.getItems().add("Architecture");
        choiceBranch.getItems().add("Bio Medical Engineering");
        choiceBranch.getItems().add("Bio Technology");

        choiceBranch.getItems().add("Chemical Enginering");
        choiceBranch.getItems().add("Civil Engineering");

        choiceBranch.getItems().add("Computetr Science & Engineering");
        choiceBranch.getItems().add("Electrical Engineering");
        choiceBranch.getItems().add("Electronics & Telicom. Engineering");
        choiceBranch.getItems().add("Information Technology");
        choiceBranch.getItems().add("Mechanical Engineering");
        choiceBranch.getItems().add("Mining");
        choiceBranch.getItems().add("Metallargical Engineering");
        choiceBranch.setValue("Architecture");
        GridPane.setConstraints(section, 1, 6 + k);
        GridPane.setConstraints(choiceBranch, 2, 6 + k);

        //semester
        ChoiceBox<Integer> choicesem = new ChoiceBox<>();

        //getItems returns the ObservableList object which you can add items to
        choicesem.getItems().add(1);
        choicesem.getItems().add(2);
        choicesem.getItems().add(3);
        choicesem.getItems().add(4);
        choicesem.getItems().add(5);
        choicesem.getItems().add(6);
        choicesem.getItems().add(7);
        choicesem.getItems().add(8);
        choicesem.setValue(1);

        GridPane.setConstraints(choicesem, 3, 6 + k);

        TextField tfstartingroll = new TextField();
        tfstartingroll.setPrefWidth(80);


        GridPane.setConstraints(tfstartingroll, 4, 6 + k);
        TextField tfendingroll = new TextField();
        tfendingroll.setPrefWidth(80);
        GridPane.setConstraints(tfendingroll, 5, 6 + k);

        TextField tfsdstart = new TextField();
        TextField tfsdend = new TextField();
        GridPane.setConstraints(tfsdstart, 8, 6 + k);

        GridPane.setConstraints(tfsdend, 9, 6 + k);
        Label message = new Label("");
        GridPane.setConstraints(message, 14, 6 + k);
        if (k == 1)
            grid.getChildren().addAll(section, tsemester, tbranch, tstart, tend, tsliderstart, tsliderend,choiceBranch, choicesem, tfstartingroll, tfendingroll, message, tfsdstart, tfsdend);
        else

            grid.getChildren().addAll(section,choiceBranch, choicesem, tfstartingroll, tfendingroll, message, tfsdstart, tfsdend);

        return new SingleReference(choiceBranch,choicesem,tfstartingroll,tfendingroll,message,tfsdstart,tfsdend);
    }
    private void showPopupDeatil(String name) throws SQLException, ClassNotFoundException {

        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Select File Type");
        window.setMinWidth(500);
        window.setMinHeight(100);
        Label label = new Label();
        label.setText("Choose one Option");
        final ToggleGroup group = new ToggleGroup();

        RadioButton rb1 = new RadioButton();

        final String pdf="pdf     ";
        final String docx="wordfFile      ";
        rb1.setText(pdf);
//A radio button with the specified label
        RadioButton rb2 = new RadioButton();

        rb2.setText(docx);

        rb1.setToggleGroup(group);
        rb2.setToggleGroup(group);
        rb1.setSelected(true);
        final String[] radio = new String[1];
        radio[0]=docx;

        group.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> ov, Toggle t, Toggle t1) {
                RadioButton chk = (RadioButton) t1.getToggleGroup().getSelectedToggle(); // Cast object to radio button
                radio[0] = chk.getText().toString();
                System.out.println("\n"+radio[0]+" radio button");
            }
        });
        Button bOK = new Button("OK");
        bOK.setOnAction(e -> {
                    try {
                        if (radio[0].equals(docx))
                            Runtime.getRuntime().exec("explorer.exe /select," + "C:\\Users\\HP1\\Desktop\\" + name + ".docx");
                        else
                            Runtime.getRuntime().exec("explorer.exe /select," + "C:\\Users\\HP1\\Desktop\\" + name + ".pdf");

                    } catch (IOException e1) {
                        //e1.printStackTrace();
                        System.out.println("IOException in onClick 'OK' button");
                    }
                    window.close();
                }
        );

        GridPane layout1 = new GridPane();
        layout1.setPrefSize(500, 100);

        //GridPane.setConstraints(label, 1, 0);
        GridPane.setConstraints(rb1, 0, 1);
        GridPane.setConstraints(rb2, 3, 1);
        GridPane.setConstraints(bOK, 5, 1);

        //  GridPane.setConstraints(bCancel, 4, 8);
        layout1.getChildren().addAll(rb1, rb2, bOK);
        layout1.setAlignment(Pos.BOTTOM_CENTER);
        Scene scene = new Scene(layout1);
        scene.getStylesheets().add(getClass().getResource("mainview.css").toExternalForm());
        window.setScene(scene);
        window.showAndWait();

    }

    public static String retval = "";
    public static String SMSSender(String user, String password, String msisdn, String msg, String sid, String fl) {
        String rsp = "";
        try {
            // Construct The Post Data
            String data = URLEncoder.encode("user", "UTF-8") + "=" + URLEncoder.encode(user, "UTF-8");
            data += "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");
            data += "&" + URLEncoder.encode("msisdn", "UTF-8") + "=" + URLEncoder.encode(msisdn, "UTF-8");
            data += "&" + URLEncoder.encode("msg", "UTF-8") + "=" + URLEncoder.encode(msg, "UTF-8");
            data += "&" + URLEncoder.encode("sid", "UTF-8") + "=" + URLEncoder.encode(sid, "UTF-8");
            data += "&" + URLEncoder.encode("fl", "UTF-8") + "=" + URLEncoder.encode(fl, "UTF-8");
            //data += "&" + URLEncoder.encode("gwid", "UTF-8") + "=" + URLEncoder.encode("2", "UTF-8"); // for transactional only
            //Push the HTTP Request
            URL url = new URL("http://cloud.smsindiahub.in/vendorsms/pushsms.aspx");
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(data);
            wr.flush();
            //Read The Response
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = rd.readLine()) != null) {
                // Process line...
                retval += line;
            }
            wr.close();
            rd.close();
            System.out.println(retval);
            rsp = retval;
        } catch (Exception e) {
            //e.printStackTrace();
            System.out.println("Exception in SMSSender = "+e);
        }
        return rsp;
    }

    public void sendEmail() {

        //String to="rohitgajendra.1296@gmail.com";//change accordingly
        //String to="kps7111@gmail.com";
        String to="akashverman007@gmail.com";

        //Get the session object
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");

        Session session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        //return new PasswordAuthentication("itbranch7@gmail.com","itbranch@4");//change accordingly
                        return new PasswordAuthentication("itbranch7@gmail.com","itbranch@4");//change accordingly
                    }
                });

        //compose message
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress("itbranch7@gmail.com"));//change accordingly
            message.addRecipient(Message.RecipientType.TO,new InternetAddress(to));
            message.setSubject("Exam Detail");
            message.setText("Dear student, your class Room is SN2 column = 3 row= 4");

            //send message
            Transport.send(message);

            System.out.println("Gmail message sent successfully");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    public String[][] Algo(String name) throws SQLException {

        ArrayList<ClassBranch> list = dataaccessor.getList(name);
        ClassDetail detail = dataaccessor.getSingle(name);

        int collum = detail.collumn;
        int row = (detail.strength % collum == 0) ? (detail.strength / collum) : (1 + (detail.strength / collum));

        String[][] a = new String[50][50];

        Collections.sort(list, new Comparator<ClassBranch>() {
            @Override
            public int compare(ClassBranch lhs, ClassBranch rhs) {
                int x = lhs.end - lhs.start;
                int y = rhs.end - rhs.start;
                return (x > y) ? 1 : 0;
            }
        });

        System.out.println(list);

        //int row=6,collum=8;
        boolean comeOut = false,flag=false;
        //int a[][]=new int[100][100];   // a[row+1][collum+1]
        int itemIndex = 0, i = 1, j = 1;

        ClassBranch cls = list.get(itemIndex);
        int amount = cls.end - cls.start + 1;
        int rollNo = cls.start;
        if(cls.sliderStart!=0) flag=true;

        while (j <= collum) {                     // for collum
            boolean ff = false;
            while (a[i][j] != null && j <= collum) {
                while (a[i][j] != null) {
                    if (j % 2 != 0) {
                        if (a[i][j + 1] == null) {
                            j = j + 1;
                            ff = true;
                            break;
                        }
                    } else {
                        if (a[i][j - 1] == null) {
                            j = j - 1;
                            ff = true;
                            break;
                        }
                    }
                    i++;
                    if (i > row) i = i % row;
                }
                if (ff) break;
                j += 2;
                if (j > collum) j = j % collum;
            }
            while (i <= row) {                    // for row
                a[i][j] = cls.branch+"("+cls.sem+")"+"-" + rollNo;
                rollNo++;
                //System.out.print("(" + i + "," + j + ") ");
                amount--;
                if (amount == 0) {
                    System.out.print("amount ="+amount);
                    ClassBranch x=list.get(itemIndex);
                    if(flag){
                        rollNo=x.sliderStart;
                        amount=x.sliderEnd-x.sliderStart+1;
                        flag=false;
                    }else {
                        itemIndex += 1;
                        if (itemIndex == list.size()) {
                            comeOut = true;
                            break;                             // goto outside
                        }
                        cls = list.get(itemIndex);
                        amount = cls.end - cls.start + 1;
                        rollNo = cls.start;                  //instead change next branch rollNo
                        if(cls.sliderStart!=0) flag=true;
                        else flag=false;
                    }
                }
                if (j % 2 == 0) j--;
                else j++;
                i += 2;
                if (i > row) {
                    i = i % row;
                    break;
                }
            }
            if (comeOut) break;
            j += 2;
            if (j > collum) j = j % collum;
        }

        System.out.println("\nrow = "+(row)+"  collumn= "+(collum));
        for (i = 1; i <= row; i++) {
            for (j = 1; j <= collum; j++) {
                System.out.print(a[i][j] + " ");
                if (j % 2 == 0) System.out.print("  ");
            }
            System.out.println();
        }
        return a;
    }

    public String[][] Algo2(String name) throws SQLException {

        ArrayList<ClassBranch> list = dataaccessor.getList(name);
        ClassDetail detail = dataaccessor.getSingle(name);

        int collum = detail.collumn;
        int row = (detail.strength % collum == 0) ? (detail.strength / collum) : (1 + (detail.strength / collum));

        String[][] a = new String[50][50];

        Collections.sort(list, new Comparator<ClassBranch>() {
            @Override
            public int compare(ClassBranch lhs, ClassBranch rhs) {
                int x = lhs.end - lhs.start;
                int y = rhs.end - rhs.start;
                return (x > y) ? 1 : 0;
            }
        });

        System.out.println(list);

        //int row=6,collum=8;
        boolean comeOut = false;
        //int a[][]=new int[100][100];   // a[row+1][collum+1]
        int itemIndex = 0, i = 1, j = 1;

        ClassBranch cls = list.get(itemIndex);
        int amount = cls.end - cls.start + 1;
        int rollNo = cls.start;

        while (j <= collum) {                     // for collum
            boolean ff = false;
            while (a[i][j] != null && j <= collum) {
                while (a[i][j] != null) {
                    if (j % 2 != 0) {
                        if (a[i][j + 1] == null) {
                            j = j + 1;
                            ff = true;
                            break;
                        }
                    } else {
                        if (a[i][j - 1] == null) {
                            j = j - 1;
                            ff = true;
                            break;
                        }
                    }
                    i++;
                    if (i > row) i = i % row;
                }
                if (ff) break;
                j += 2;
                if (j > collum) j = j % collum;
            }
            while (i <= row) {                    // for row
                a[i][j] = cls.branch+"("+cls.sem+")"+"-" + rollNo;
                rollNo++;
                System.out.print("(" + i + "," + j + ") ");
                amount--;
                if (amount == 0) {
                    itemIndex += 1;
                    if (itemIndex == list.size()) {
                        comeOut = true;
                        break;                             // goto outside
                    }
                    cls = list.get(itemIndex);
                    amount = cls.end - cls.start + 1;
                    rollNo = cls.start;                  //instead change next branch rollNo
                }
                if (j % 2 == 0) j--;
                else j++;
                i += 2;
                if (i > row) {
                    i = i % row;
                    break;
                }
            }
            if (comeOut) break;
            j += 2;
            if (j > collum) j = j % collum;
        }

        System.out.println("\nrow = "+(row)+"  collumn= "+(collum));
        for (i = 1; i <= row; i++) {
            for (j = 1; j <= collum; j++) {
                System.out.print(a[i][j] + " ");
                if (j % 2 == 0) System.out.print("  ");
            }
            System.out.println();
        }
        return a;
    }
}
//  +)*^$@<AJ><GN><SK>



