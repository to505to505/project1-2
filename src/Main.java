import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javafx.scene.Node;
import java.util.stream.Collectors;

import javax.xml.stream.EventFilter;

import java.awt.Desktop;
import java.awt.Label;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Polyline;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import java.util.Arrays;
import javafx.scene.control.TableCell;

import java.io.BufferedReader;
import java.lang.reflect.Array;
import java.util.Random;

public class Main extends Application {

    //private final Desktop desktop = Desktop.getDesktop();

    public static int dataCount = 0; //used to count Data objects
    public static int removeCount = 1; //number of removed Data objects offset by 1
    public static ArrayList<Data> dataList = new ArrayList<Data>();
    public static ObservableList<String> fileList;
    public static ObservableList<String> columnList;

    final static int BUTTON_COUNT = 10;
    final static String[] BUTTON_TEXTS = {"Visualization Sandbox", "Step 1 Findings", "Bar Chart", "Histogram", "Pie Chart", "Scatter Plot", "Swarm Plot", "Step 2 Findings", "Steps 3 & 4 Findings", "PieChartsCum"};
    









    /* private void refreshColumnList(FlowPane filters){ //has a problem when the same column names appear more than once - considers them as different.
        ArrayList<String> temp = new ArrayList<String>();
        for(Data data : dataList)
            for(String s : data.columnNames)
                temp.add(s);
        columnList = FXCollections.observableArrayList(temp);
        ChoiceBox<String> xAxis = new ChoiceBox<String>(columnList);
        xAxis.setValue("------------");
        ChoiceBox<String> yAxis = new ChoiceBox<String>(columnList);
        yAxis.setValue("------------");
    }
 */
    private VBox PieChart(Stage stage) {
        VBox vBox = new VBox();

       

        ComboBox<String> filterComboBox = new ComboBox<>();
        filterComboBox.setItems(FXCollections.observableArrayList("GPA greater than 7.5", "No grades lower than 7"));
        filterComboBox.getSelectionModel().selectFirst();


        ComboBox<String> dataSelector = new ComboBox<>();
        dataSelector.setItems(FXCollections.observableArrayList("CurrentGrades", "GraduateGrades"));
        dataSelector.setValue("GraduateGrades"); 
        // Creating a Pie chart

        PieChart pieChart = new PieChart();


        filterComboBox.setOnAction(e -> updatePieChart(dataSelector.getValue(), pieChart, filterComboBox.getValue()));
        dataSelector.setOnAction(e -> updatePieChart(dataSelector.getValue(), pieChart, filterComboBox.getValue()));





        updatePieChart(dataSelector.getValue(), pieChart, filterComboBox.getValue());
        
        vBox.getChildren().addAll(dataSelector, filterComboBox, pieChart);
        return vBox;


    }
    private void updatePieChart(String selectedData, PieChart PieChart, String selectedFilter) {
        PieChart.getData().clear(); // Очистка предыдущих данных
        ArrayList<Integer> cum_students = DataFunc.cum(selectedData, selectedFilter);
        int all_students_length = DataFunc.data_size(selectedData);
        PieChart.Data slice1 = new PieChart.Data("Cum-Laude", cum_students.size());
        PieChart.Data slice2 = new PieChart.Data("Others", all_students_length-cum_students.size());
        PieChart.getData().add(slice1);
        PieChart.getData().add(slice2);
        PieChart.setLabelsVisible(true);
    }
        
    private VBox Histogram(Stage stage) {
        VBox vBox = new VBox();


        ComboBox<String> filterComboBox = new ComboBox<>();
        filterComboBox.setItems(FXCollections.observableArrayList(MainFunc.all_courses));
        filterComboBox.getSelectionModel().selectFirst();
        filterComboBox.setItems(FXCollections.observableArrayList(DataFunc.all_courses));
        filterComboBox.getSelectionModel().selectFirst(); // Выбор первого элемента по умолчанию


        ComboBox<String> dataSelector = new ComboBox<>();
        dataSelector.setItems(FXCollections.observableArrayList("CurrentGrades", "GraduateGrades", "StudentInfo"));
        dataSelector.setValue("GraduateGrades");

        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        BorderPane borderPane;

        
        filterComboBox.setOnAction(e -> updateHistogram(dataSelector.getValue(), barChart, filterComboBox.getValue()));
        dataSelector.setOnAction(e -> {
            
            if(dataSelector.getValue().equals("StudentInfo")) {

                List<String> newItems = Arrays.asList("Suruna Value", "Hurni Level", "Volta ", "Lal Count");
            
                filterComboBox.setItems(FXCollections.observableArrayList(newItems));
                filterComboBox.getSelectionModel().selectFirst();
            } else {
                filterComboBox.setItems(FXCollections.observableArrayList(DataFunc.all_courses));
                filterComboBox.getSelectionModel().selectFirst();
            }
            updateHistogram(dataSelector.getValue(), barChart, filterComboBox.getValue());
        });
        
    
        updateHistogram(dataSelector.getValue(), barChart, filterComboBox.getValue());

        vBox.getChildren().addAll(dataSelector, filterComboBox, barChart);
        
    
        return vBox;
    }
    private void updateHistogram(String selectedData, BarChart<String, Number> barChart, String selectedFilter) {
        barChart.getData().clear();
        CategoryAxis xAxis = (CategoryAxis) barChart.getXAxis();
        xAxis.getCategories().clear();
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName(selectedData);




        //Getting data to draw hist
        Map<String, Integer> freqMap = DataFunc.raw_data_hist(selectedData, selectedFilter);
        ArrayList<String> all_categories= new ArrayList<>();
        if (selectedData.equals("GraduateGrades")) {
            List<String> dataToAdd = Arrays.asList("6", "7", "8", "9", "10");
            all_categories.addAll(dataToAdd);
            xAxis.setCategories(FXCollections.observableArrayList(dataToAdd));

        } else if (selectedData.equals("CurrentGrades")) {
            List<String> dataToAdd1 = Arrays.asList("-1", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10");
            all_categories.addAll(dataToAdd1);
            List<String> dataToAddtoShow = Arrays.asList("Missing values", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10");
            xAxis.setCategories(FXCollections.observableArrayList(dataToAddtoShow));
        } else if(selectedData.equals("StudentInfo"))
         {
            if(selectedFilter.equals("Lal Count")) {
                List<String> dataToAdd3 = new ArrayList<>(DataFunc.LalSorted(selectedData));
                all_categories.addAll(dataToAdd3);
                xAxis.setCategories(FXCollections.observableArrayList(dataToAdd3));
            } else {
            List<String> dataToAdd2 = new ArrayList<String>();
            for (Map.Entry<String, Integer> entry : freqMap.entrySet()) {
                dataToAdd2.add(get_value_names(entry.getKey(), selectedFilter));
                all_categories.add(entry.getKey());
            }
            xAxis.setCategories(FXCollections.observableArrayList(dataToAdd2));
         } }
        for (String category: all_categories) {
            Integer value = freqMap.get(category);
            if(selectedFilter.equals("Hurni Level") || selectedFilter.equals("Suruna Value") || selectedFilter.equals("Volta ")) {
                String categoryString = get_value_names(category, selectedFilter);
                series.getData().add(new XYChart.Data<>(categoryString, (value != null) ? value : 0));
            }
            if(category.equals("-1")) 
                series.getData().add(new XYChart.Data<>("Missing values", (value != null) ? value : 0));
            else
                series.getData().add(new XYChart.Data<>(category, (value != null) ? value : 0));
        
    }


        barChart.getData().add(series);
        barChart.layout();
        

}
public static String get_value_names(String value, String course_name) {
    switch (course_name) {
        case "Suruna Value":
            switch (value) {
                case "0":
                    return "nulp";
                case "1":
                    return "doot";
                case "2":
                    return "lobi"; 
                default:
                    return "unknown";

            }
        case "Hurni Level":
            switch (value) {
                case "0":
                    return "nothing";
                case "1":
                    return "low";
                case "2":
                    return "medium";
                case "3":
                    return "high";
                case "4":
                    return "full";
                default:
                    return "unknown";

            
            }
            
        case "Volta ":
            switch (value) {
                case "1":
                    return "1 star";
                case "2":
                    return "2 stars";
                case "3":
                    return "3 stars";
                case "4":
                    return "4 stars";
                case "5":
                    return "5 stars";
                default:
                    return "unknown"; }
            
        default:
            return "unknown";   
         }}


private VBox Scatter(Stage stage){

        VBox vBox = new VBox();

        ComboBox<String> filter1ComboBox = new ComboBox<>();
        filter1ComboBox.setItems(FXCollections.observableArrayList(DataFunc.all_courses));
        filter1ComboBox.getSelectionModel().selectFirst(); // selecting default value


        ComboBox<String> filter2ComboBox= new ComboBox<>();
        filter2ComboBox.setItems(FXCollections.observableArrayList(DataFunc.all_courses));
        filter2ComboBox.getSelectionModel().selectLast(); // selecting default value

        ComboBox<String> dataSelector = new ComboBox<>();
        dataSelector.setItems(FXCollections.observableArrayList("GraduateGrades", "CurrentGrades"));
        dataSelector.getSelectionModel().selectFirst(); // selecting default value


        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel(filter1ComboBox.getValue());
        yAxis.setLabel(filter2ComboBox.getValue());
        
        CustomScatterChart scatterChart = new CustomScatterChart(xAxis, yAxis);
        Data right_data = new Data();
            for (Data data : dataList) {
                if (data.name.equals(dataSelector.getValue())) {
                    right_data = data;
            }
            }
        int course_col_num1 = 0;
        int course_col_num2  = 0;
              
        for(int k =0; k<right_data.columnNames.length; k++) {
            if(right_data.columnNames[k].equals(filter2ComboBox.getValue())) {
                course_col_num2= k;
            }
            if(right_data.columnNames[k].equals(filter1ComboBox.getValue())) {
                course_col_num1 = k;
            }
        }


        updateScatter(dataSelector.getValue(), scatterChart, filter1ComboBox.getValue(), filter2ComboBox.getValue());


        filter1ComboBox.setOnAction(e -> updateScatter(dataSelector.getValue(), scatterChart, filter1ComboBox.getValue(), filter2ComboBox.getValue()));
        filter2ComboBox.setOnAction(e -> updateScatter(dataSelector.getValue(),scatterChart, filter1ComboBox.getValue(), filter2ComboBox.getValue()));
        dataSelector.setOnAction(e -> updateScatter(dataSelector.getValue(), scatterChart, filter1ComboBox.getValue(), filter2ComboBox.getValue()));
        
        vBox.getChildren().addAll(dataSelector, filter1ComboBox,filter2ComboBox, scatterChart);
        
        return vBox;
    } 
    @Override
    public void start(Stage stage) {

        Image icon = new Image("data/icon.png");
        stage.getIcons().add(icon);
        stage.setTitle("Data visualization");

        stage.setFullScreen(false);
        stage.setFullScreenExitHint("Press ESC when you get bored");
        stage.setWidth(1080);
        stage.setHeight(720); 
        //stage.setResizable(false);

        ///Root
        BorderPane root = new BorderPane();

        ///Menu Bar
        MenuBar menuBar = new MenuBar();
        Menu dataMenu = DataImport.dataMenu(stage);
            
        Menu preferencesMenu = new Menu("Preferences");
        Menu fontSMenu = new Menu("Font");
            MenuItem font1 = new MenuItem("font1");
        fontSMenu.getItems().addAll(font1);
        Menu sizeSMenu = new Menu("Size");
        Menu styleSMeun = new Menu("Style");
        preferencesMenu.getItems().addAll(fontSMenu, sizeSMenu, styleSMeun);

        menuBar.getMenus().addAll(dataMenu, preferencesMenu);
        //add menubar to root
        root.setTop(menuBar);
        ///END Menu Bar

        VBox startScreen = new VBox();
        startScreen.setStyle("-fx-background-color: #fff5ee");
        root.setCenter(startScreen);

        ///Sandbox
        BorderPane sandbox = new BorderPane();
        sandbox.setStyle("-fx-background-color: #fff5ee");
        //sandbox.setAlignment(graph, Pos.CENTER);
 
        //Sandbox Filter Bar FlowPane
        FlowPane filters = new FlowPane(10, 10);
        filters.setStyle("-fx-background-color: #997744");

        //X-Axis Data Selection
        VBox xAxisBox = new VBox();
        Text xCol = new Text("X-Axis Column:");
        ComboBox<String> xAxisCol = new ComboBox<String>();
        xAxisCol.setValue("----------");
        Text xFile = new Text("X-Axis File:");
        ComboBox<String> xAxisFile = new ComboBox<String>(fileList);
        xAxisFile.setValue("----------");
        xAxisFile.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e){
                for(Data d: dataList)
                    if(d.name.equals(xAxisFile.getValue())){
                        xAxisCol.getItems().clear();
                        xAxisCol.getItems().addAll(d.columnNames);
                    }
            }
        });
        xAxisBox.getChildren().addAll(xFile, xAxisFile, xCol, xAxisCol);
        filters.getChildren().add(xAxisBox);
        //Y-Axis Data Selection
        VBox yAxisBox = new VBox();
        Text yCol = new Text("Y-Axis Column:");
        ComboBox<String> yAxisCol = new ComboBox<String>();
        yAxisCol.setValue("----------");
        Text yFile = new Text("Y-Axis File:");
        ComboBox<String> yAxisFile = new ComboBox<String>(fileList);
        yAxisFile.setValue("----------");
        yAxisFile.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e){
                for(Data d: dataList)
                    if(d.name.equals(yAxisFile.getValue())){
                        yAxisCol.getItems().clear();
                        yAxisCol.getItems().addAll(d.columnNames);
                    }
            }
        });
        yAxisBox.getChildren().addAll(yFile, yAxisFile, yCol, yAxisCol);
        filters.getChildren().add(yAxisBox);
        //refreshColumnList(filters);

        sandbox.setTop(filters);
        ///END SandBox


        ///Side Menu
        VBox sideMenu = new VBox();
        sideMenu.setPrefWidth(200);
        sideMenu.setStyle("-fx-background-color: #333333;");

        Button[] buttons = new Button[BUTTON_COUNT];
        for(int i = 0; i < buttons.length; i++){
            buttons[i] = new Button(BUTTON_TEXTS[i]);
            buttons[i].setPrefWidth(sideMenu.getPrefWidth());
            buttons[i].setStyle("-fx-background-color: #444444; -fx-text-fill: white;");
        }

        EventHandler<ActionEvent> sideMenuHandler = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e){
                String but = ((Button)e.getTarget()).getText();
                int i = 0;
                for(;i < BUTTON_TEXTS.length; i++)
                    if(but.equals(BUTTON_TEXTS[i]))
                        break;
                buttons[i].setStyle("-fx-background-color: #666666; -fx-text-fill: white;");
                switch(i){
                    case 0:
                        root.setCenter(sandbox);
                        stage.show();   
                        break;
                    case 1:
                        root.setCenter(startScreen);
                        stage.show();
                        break;
                    case 2:
                        root.setCenter(startScreen);
                        stage.show();
                        break;
                    case 3:
                        VBox vBox2 = Histogram(stage);
                        root.setCenter(vBox2);
                        stage.show();
                        break;
                    case 4:
                        root.setCenter(startScreen);
                        stage.show();
                        break;
                    case 5:
                        VBox vBox3 = Scatter(stage);
                        root.setCenter(vBox3);
                        stage.show();
                        break;
                    case 6:
                        VBox vBox6 = SwarmPlot();
                        root.setCenter(vBox6);
                        stage.show();
                        break;
                    case 7:
                        VBox vBox7 = Table_draw();
                        root.setCenter(vBox7);
                        stage.show();
                        break;
                    case 8:
                        BorderPane g = new BorderPane();
                        g.setStyle("-fx-background-color: #333333");
                        root.setCenter(g);
                        stage.show();
                        break;
                    case 9:
                        VBox vBox1 = PieChart(stage);
                        root.setCenter(vBox1);
                        stage.show();
                        break;
                    case 10: 
                        root.setCenter(startScreen);
                        stage.show();
                        break;
                    default:
                }
                for(int j = 0; i < buttons.length; j++)
                    if(j!=i) buttons[j].setStyle("-fx-background-color: #444444; -fx-text-fill: white;");
            }
        };
        for(Button b : buttons) b.setOnAction(sideMenuHandler);
        sideMenu.getChildren().addAll(buttons);
        //add Side Menu to root
        root.setLeft(sideMenu);
        ///End Side Menu
        
        
        Scene scene = new Scene(root, 1080, 1480);
        stage.setScene(scene);
        stage.show();
    }
    


    private void openHistogramWindow(Stage stage, Scene scene) {
        HistogramChart histogram = new HistogramChart(scene);
        histogram.start(stage);
    }
    private void openPieChartWindow(Stage stage, Scene scene) {
        PieChartPlot pieChart = new PieChartPlot(scene);
        pieChart.start(stage); 
    }
    private void openScatterPlotWindow(Stage stage, Scene scene) {
        ScatterPlotChart scatterPlot = new ScatterPlotChart(scene);
        scatterPlot.start(stage);
    }

    private void updateScatter(String selectedData, CustomScatterChart scatterChart, String selectedFilter1, String selectedFilter2) {
        Data right_data = new Data();
            for (Data data : dataList) {
                if (data.name.equals(selectedData)) {
                    right_data = data;
            }
            }

        int course_col_num1 = 0;
        int course_col_num2  = 0;
              
        for(int k =0; k<right_data.columnNames.length; k++) {
            if(right_data.columnNames[k].equals(selectedFilter2)) {
                course_col_num2= k;
            }
            if(right_data.columnNames[k].equals(selectedFilter1)) {
                course_col_num1 = k;
            }
        }
        scatterChart.getData().clear();
        NumberAxis XAxis = (NumberAxis) scatterChart.getXAxis();
        NumberAxis YAxis = (NumberAxis) scatterChart.getYAxis();
        XAxis.autoRangingProperty().set(true);
        YAxis.autoRangingProperty().set(true);
        XAxis.setLabel(selectedFilter1);
        YAxis.setLabel(selectedFilter2);
    
        scatterChart.setAnimated(true);

        
            
        XYChart.Series<Number, Number> series =  DataFunc.getScatter(selectedFilter1, selectedFilter2,  selectedData);
        
        if(selectedData.equals("GraduateGrades")){
            XAxis.autoRangingProperty().set(false);
            XAxis.setUpperBound(11);
            XAxis.setLowerBound(5);
            XAxis.setTickUnit(1);

            YAxis.autoRangingProperty().set(false);
            YAxis.setUpperBound(11);
            YAxis.setLowerBound(5);
            YAxis.setTickUnit(1); 
        } else {
        XAxis.autoRangingProperty().set(false);
        XAxis.setUpperBound(11);
        XAxis.setLowerBound(4);
        XAxis.setTickUnit(1);

        YAxis.autoRangingProperty().set(false);
        YAxis.setUpperBound(11);
        YAxis.setLowerBound(4);
        YAxis.setTickUnit(1); 
        }
        
        double[] series_for_regress_line = Utility.linearRegressionLine(right_data, course_col_num1 , course_col_num2);
        scatterChart.addLine(series_for_regress_line);
        
        scatterChart.getData().add(series);

        scatterChart.layout(); 

    
}


/// tables
    public VBox Table_draw() {
        VBox vBox = new VBox();

        ComboBox<String> filter1ComboBox = new ComboBox<>();
        filter1ComboBox.setItems(FXCollections.observableArrayList("Correlation", "Cosine similarity", "Euclidean distance"));
        filter1ComboBox.getSelectionModel().selectFirst(); // selecting default value


        ComboBox<String> dataSelector = new ComboBox<>();
        dataSelector.setItems(FXCollections.observableArrayList("GraduateGrades", "CurrentGrades"));
        dataSelector.getSelectionModel().selectFirst(); // selecting default value

        TableView tbv = new TableView();
        tbv.setEditable(true);

        updateTable(dataSelector.getValue(), tbv, filter1ComboBox.getValue());
        filter1ComboBox.setOnAction(e -> updateTable(dataSelector.getValue(), tbv, filter1ComboBox.getValue()));
        dataSelector.setOnAction(e -> updateTable(dataSelector.getValue(), tbv, filter1ComboBox.getValue()));



        vBox.getChildren().addAll(filter1ComboBox, dataSelector, tbv);
        return vBox;
      
       
        


    }
    public void updateTable(String selectedData, TableView tbv, String selectedFilter) {
        tbv.getItems().clear();
        tbv.getColumns().clear();
        tbv.setStyle("-fx-font-size: 10px;"); // Уменьшение до 10px
        tbv.setMinSize(1080, 1080); // Минимальная ширина 200px, Минимальная высота 100px

        Data right_data = new Data();
            for (Data data : dataList) {
                if (data.name.equals(selectedData)) {
                    right_data = data;
            }
            }

    ArrayList<String> all_courses_corr = new ArrayList<>(Arrays.asList("JTE_234", "ATE_003", "TGL_013", "PPL_239", "WDM_974", "GHL_823", "HLU_200", "MON_014", "FEA_907", "LPG_307", "TSO_010", "LDE_009", "JJP_001", "MTE_004", "LUU_003", "LOE_103", "PLO_132", "BKO_800", "SLE_332", "BKO_801", "DSE_003", "DSE_005", "ATE_014", "JTW_004", "ATE_008", "DSE_007", "ATE_214", "JHF_101", "KMO_007", "WOT_104"));
        
        TableColumn<String, CorrelationClass> cl1 = new TableColumn<>("Feauture name"); 
            cl1.setCellValueFactory(new PropertyValueFactory<>("course_name"));
            cl1.setPrefWidth(50);
            tbv.getColumns().add(cl1);
        for(String course: all_courses_corr) {
            TableColumn<CorrelationClass, Double> cl = new TableColumn<>(course); 
            cl.setCellValueFactory(new PropertyValueFactory<>(course));
            if (selectedFilter.equals("Correlation")) {
                cl.setCellFactory(column -> new ColorChangingCell_Corr());
            } else if(selectedFilter.equals("Cosine similarity")) {
                cl.setCellFactory(column -> new ColorChangingCell_COS());
            } else {
                cl.setCellFactory(column -> new ColorChangingCellEuclid());
            }
            
            cl.setPrefWidth(48.5);
            tbv.getColumns().add(cl);
        }
        

        
         
        ArrayList<CorrelationClass> all_corrs = new ArrayList<>();
        
        for(String course: all_courses_corr) {
            CorrelationClass feature_corr = new CorrelationClass(selectedData, course, selectedFilter);
            all_corrs.add(feature_corr);
        }
        
        for (CorrelationClass corr_solo : all_corrs){
            tbv.getItems().addAll(corr_solo);
        }
        
    }
    /// swarm plot
    public VBox SwarmPlot() {
        VBox vBox = new VBox();

        ComboBox<String> filter1ComboBox = new ComboBox<>();
        filter1ComboBox.setItems(FXCollections.observableArrayList(DataFunc.all_courses));
        filter1ComboBox.getSelectionModel().selectFirst(); // selecting default value


        ComboBox<String> filter2ComboBox= new ComboBox<>();
        filter2ComboBox.setItems(FXCollections.observableArrayList(DataFunc.all_courses));
        filter2ComboBox.getSelectionModel().selectLast(); // selecting default value

        ComboBox<String> dataSelector = new ComboBox<>();
        dataSelector.setItems(FXCollections.observableArrayList("GraduateGrades", "CurrentGrades", "Current+StudentInfo"));
        dataSelector.getSelectionModel().selectFirst(); // selecting default value


        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel(filter1ComboBox.getValue());
        yAxis.setLabel(filter2ComboBox.getValue());
        
        ScatterChart<Number, Number> swarmChart = new CustomScatterChart(xAxis, yAxis);
        
        updateSwarm(dataSelector.getValue(), swarmChart, filter1ComboBox.getValue(), filter2ComboBox.getValue(), filter2ComboBox);


        filter1ComboBox.setOnAction(e -> updateSwarm(dataSelector.getValue(), swarmChart, filter1ComboBox.getValue(), filter2ComboBox.getValue(), filter2ComboBox));
        filter2ComboBox.setOnAction(e -> updateSwarm(dataSelector.getValue(),swarmChart, filter1ComboBox.getValue(), filter2ComboBox.getValue(), filter2ComboBox));
        dataSelector.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            updateFilter2(filter2ComboBox, newValue);
            updateSwarm(dataSelector.getValue(), swarmChart, filter1ComboBox.getValue(), filter2ComboBox.getValue(), filter2ComboBox);
        });
        
            
            
        vBox.getChildren().addAll(dataSelector, filter1ComboBox,filter2ComboBox, swarmChart);
        
        return vBox;
    }
    public void updateFilter2(ComboBox filter2ComboBox, String selectedData) {
        if(selectedData.equals("Current+StudentInfo")) {
            List<String> newItems = Arrays.asList("Suruna Value", "Hurni Level", "Volta ", "Lal Count");
            filter2ComboBox.setItems(FXCollections.observableArrayList(newItems));
            filter2ComboBox.getSelectionModel().selectFirst();
        } else {
            filter2ComboBox.setItems(FXCollections.observableArrayList(DataFunc.all_courses));
            filter2ComboBox.getSelectionModel().selectFirst();
        }
    }
    public void updateSwarm(String selectedData, ScatterChart<Number, Number> scatterChart, String selectedFilter1, String selectedFilter2, ComboBox filter2ComboBox) {
        
        Data right_data = new Data();
                for (Data data : dataList) {
                    if (data.name.equals(selectedData)) {
                        right_data = data;
                }
                }

            int course_col_num1 = 0;
            int course_col_num2  = 0;
                    
            for(int k =0; k<right_data.columnNames.length; k++) {
                if(right_data.columnNames[k].equals(selectedFilter2)) {
                    course_col_num2= k;
                }
                if(right_data.columnNames[k].equals(selectedFilter1)) {
                    course_col_num1 = k;
                }
            }
            
            scatterChart.getData().clear();
            NumberAxis XAxis = (NumberAxis) scatterChart.getXAxis();
            NumberAxis YAxis = (NumberAxis) scatterChart.getYAxis();
            XAxis.autoRangingProperty().set(true);
            YAxis.autoRangingProperty().set(true);
            XAxis.setLabel(selectedFilter2);
            YAxis.setLabel(selectedFilter1);
            scatterChart.setAnimated(true);

            DS ds = DecisionStumpFactory.createDecisionStumpBasic(right_data.data, selectedFilter1, selectedFilter2, right_data.columnNames);
            XYChart.Series<Number, Number> series =  DataFunc.getSwarm(ds.get_users_for_plots(), ds.get_property_name());



            
    

            YAxis.autoRangingProperty().set(false);
            YAxis.setUpperBound(11);
            YAxis.setLowerBound(4);
            YAxis.setTickUnit(1); 
            

            XAxis.autoRangingProperty().set(false);
            XAxis.setUpperBound(ds.get_users_for_plots().size()+2);
            XAxis.setLowerBound(1);
            XAxis.setTickUnit(1);
            
            if(ds instanceof DSCat) {
                XAxis.setTickLabelFormatter(new CatTickLabelFormatter(XAxis, right_data.columnNames, course_col_num2, ds));
            } else {
                XAxis.setTickLabelFormatter(new NumTickLabelFormatter(XAxis, right_data.columnNames, course_col_num1, ds));
            }
           
            
            
    
            
            scatterChart.getData().add(series);

            scatterChart.layout(); 


        }



    public static void main(String[] args) {
        dataInit();
        System.out.println(dataList.get(0).name);
        launch(args);
    }

    private static void swarmPlot(){
        VBox swormBox = new VBox();
        FlowPane filters = new FlowPane();
        Group swarmPlot = new Group();
        swormBox.getChildren().addAll(filters, swarmPlot);
    }

    private static void refreshColumnList(FlowPane filters){ //has a problem when the same column names appear more than once - considers them as different.
        ArrayList<String> temp = new ArrayList<String>();
        for(Data data : Main.dataList)
            for(String s : data.columnNames)
                temp.add(s);
        Main.columnList = FXCollections.observableArrayList(temp);
        System.out.println(Main.columnList);
        ChoiceBox<String> xAxis = new ChoiceBox<String>(Main.columnList);
        xAxis.setValue("------------");
        ChoiceBox<String> yAxis = new ChoiceBox<String>(Main.columnList);
        yAxis.setValue("------------");
        filters.getChildren().remove(0);
        filters.getChildren().add(0, xAxis);
        filters.getChildren().remove(1);
        filters.getChildren().add(1, yAxis);
    }
    private static void dataInit(){
    
        ///Statically import data
        Data data_current = new Data("src/data/CurrentGrades.csv");
        data_current.name = "CurrentGrades";
        dataList.add(data_current);

        Data data_graduate = new Data("src/data/bugData.csv");
        data_graduate.name = "GraduateGrades";
        dataList.add(data_graduate);

        
        Data data_student_info = new Data("src/data/StudentInfo.csv");
        data_student_info.name = "Student Info";
        dataList.add(data_student_info);

        Data combinedData = new Data(new AggregateData("src/data/CurrentGrades.csv", "src/data/StudentInfo.csv"));
        combinedData.name = "Current+StudentInfo";
        dataList.add(combinedData);
    }
}
