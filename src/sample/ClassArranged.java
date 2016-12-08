package sample;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.sql.SQLException;
import java.util.Arrays;

/**
 * Created by vicky on 31/12/14.
 */
public class ClassArranged {

    String classname;

    public ClassArranged(String classname) {
        this.classname = classname;
    }


    public void ShowArangment(String[][] a) throws SQLException, ClassNotFoundException {

        DataAccesor dataAccessor = new DataAccesor("com.mysql.jdbc.Driver", "jdbc:mysql://localhost:3306/vigyaan", "root", "");
        ClassDetail detail = dataAccessor.getSingle(classname);

        int collum = detail.collumn;
        int row1 = (detail.strength % collum == 0) ? (detail.strength / collum) : (1 + (detail.strength % collum));

        Stage primaryStage = new Stage();

        primaryStage.setTitle(classname + " Seating Arrangement Details");
        StackPane root = new StackPane();
        ObservableList<String[]> data = FXCollections.observableArrayList();
        data.addAll(Arrays.asList(a));
        data.remove(0);//remove titles from data

        TableView<String[]> table = new TableView<>();
        TableColumn[] colArray = new TableColumn[collum];

        /*for(int i=0;i<collum;i++){
            colArray[i]=new TableColumn("column "+(i+1));
        }
        table.getColumns().addAll(colArray);*/

        for (int i = 1; i <= collum; i++) {             /// changes here
            if (i == 1) {
                TableColumn tc1 = new TableColumn("      ");
                table.getColumns().addAll(tc1);
            }
            TableColumn tc = new TableColumn("column " + (i));
            //TableColumn tc = new TableColumn(a[0][i]);
            final int colNo = i;
            tc.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<String[], String>, ObservableValue<String>>() {
                @Override
                public ObservableValue<String> call(TableColumn.CellDataFeatures<String[], String> p) {
                    return new SimpleStringProperty((p.getValue()[colNo]));
                }
            });
            tc.setPrefWidth(90);
            table.getColumns().addAll(tc);
            //table.getColumns().add(tc);
            if (i % 2 == 0) {
                tc = new TableColumn("      ");
                table.getColumns().addAll(tc);
            }
        }
        table.setItems(data);
        root.getChildren().add(table);
        primaryStage.setScene(new Scene(root, 1500, 700));
        primaryStage.getScene().getStylesheets().add(getClass().getResource("tab.css").toExternalForm());
        primaryStage.showAndWait();
    }

    public void showsearchresult(String[][] a, int k) throws SQLException, ClassNotFoundException {

        DataAccesor dataAccessor = new DataAccesor("com.mysql.jdbc.Driver", "jdbc:mysql://localhost:3306/vigyaan", "root", "");
        ClassDetail detail = dataAccessor.getSingle(classname);

        int collum = detail.collumn;
        int row1 = (detail.strength % collum == 0) ? (detail.strength / collum) : (1 + (detail.strength % collum));

        Stage primaryStage = new Stage();

        String x = "";
        int f = 0, g = 0, flag = 0;
        for (f = 1; f <= row1; f++) {                  //loop only for search
            for (g = 1; g <= collum; g++) {
                try {
                    if (a[f][g].equals("") || a[f][g] == "") continue;
                    x = a[f][g].substring(a[f][g].indexOf("-") + 1);
                    if (x.equals(k + "")) {
                        flag = 1;
                        break;
                    }
                } catch (NullPointerException e) {
                    //e.printStackTrace();
                    System.out.println("NullPointerException in showsearchresult f=" + f + " g=" + g);
                }
            }
            if (flag == 1) break;
        }

        primaryStage.setTitle(classname + " Row = " + f + "   column = " + g);
        StackPane root = new StackPane();
        ObservableList<String[]> data = FXCollections.observableArrayList();
        data.addAll(Arrays.asList(a));
        data.remove(0);//remove titles from data

        TableView<String[]> table = new TableView<>();
        TableColumn[] colArray = new TableColumn[collum];

        /*for(int i=0;i<collum;i++){
            colArray[i]=new TableColumn("column "+(i+1));
        }
        table.getColumns().addAll(colArray);*/

        for (int i = 1; i <= collum; i++) {             /// for print both
            if (i == 1) {
                TableColumn tc1 = new TableColumn("      ");
                table.getColumns().addAll(tc1);
            }
            TableColumn tc = new TableColumn("column " + (i));
            //TableColumn tc = new TableColumn(a[0][i]);
            final int colNo = i;
            tc.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<String[], String>, ObservableValue<String>>() {
                @Override
                public ObservableValue<String> call(TableColumn.CellDataFeatures<String[], String> p) {
                    return new SimpleStringProperty((p.getValue()[colNo]));
                }
            });
            tc.setPrefWidth(90);
            table.getColumns().addAll(tc);
            //table.getColumns().add(tc);
            if (i % 2 == 0) {
                tc = new TableColumn("      ");
                table.getColumns().addAll(tc);
            }
        }

        table.setItems(data);
        root.getChildren().add(table);
        primaryStage.setScene(new Scene(root, 1500, 700));
        primaryStage.getScene().getStylesheets().add(getClass().getResource("tab.css").toExternalForm());
        primaryStage.showAndWait();
    }
}