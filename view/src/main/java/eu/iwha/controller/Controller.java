package eu.iwha.controller;

import com.sun.prism.impl.Disposer.Record;
import eu.iwha.config.ViewConfig;
import eu.iwha.dao.AnimalsDAO;
import eu.iwha.model.Animals;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Predicate;

public class Controller implements Initializable {

    private AnimalsDAO animalsDAO;

    @FXML
    AnchorPane primaryStage = new AnchorPane(); //main stage

    @FXML
    VBox vBox = new VBox(); //upper left pane

    @FXML
    TextField textFieldName = new TextField();
    @FXML
    TextField textFieldSpecie = new TextField();
    @FXML
    TextField textFieldAge = new TextField();
    @FXML
    TextField textFieldCage = new TextField();
    @FXML
    Button addButton = new Button();

    @FXML
    Label nameLabel = new Label();
    @FXML
    Label specieLabel = new Label();
    @FXML
    Label ageLabel = new Label();
    @FXML
    Label cageLabel = new Label();
    @FXML
    Label filteringLabel = new Label();

    @FXML
    StackPane imageStackPane = new StackPane(); //stackPane with an img

    Image img = new Image("zoo_logo.jpg");
    @FXML
    ImageView abc = new ImageView();

    @FXML
    StackPane labelStackPane = new StackPane();
    @FXML
    AnchorPane leftPane = new AnchorPane();
    @FXML
    AnchorPane rightPane = new AnchorPane();

    @FXML
    TableView<Animals> tableView = new TableView<>();
    @FXML
    TableColumn<Animals, String> nameColumn = new TableColumn<>();
    @FXML
    TableColumn<Animals, String> specieColumn = new TableColumn<>();
    @FXML
    TableColumn<Animals, String> ageColumn = new TableColumn<>();
    @FXML
    TableColumn<Animals, String> cageColumn = new TableColumn<>();
    @FXML
    TableColumn<Record, Boolean> actionColumn = new TableColumn<Record, Boolean>();
    @FXML
    TextField textFieldFiltering = new TextField();


    private ObservableList<Animals> data;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ApplicationContext context = new AnnotationConfigApplicationContext(ViewConfig.class);
        animalsDAO = context.getBean(AnimalsDAO.class);

        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        specieColumn.setCellValueFactory(new PropertyValueFactory<>("specie"));
        ageColumn.setCellValueFactory(new PropertyValueFactory<>("age"));

        cageColumn.setCellValueFactory(new PropertyValueFactory<>("cage"));

        data = FXCollections.observableArrayList(animalsDAO.findAll());

        tableView.setItems(data);
        tableView.getColumns().addAll(nameColumn,specieColumn,ageColumn,cageColumn);

        abc.setImage(img);


        FilteredList<Animals> filteredData = new FilteredList<>(data, e->true);

        textFieldFiltering.setOnKeyPressed((KeyEvent e) ->{
            textFieldFiltering.textProperty().addListener((observable, oldValue, newValue) -> {
                filteredData.setPredicate((Predicate<? super Animals>) animal -> {
                    if(newValue == null || newValue.isEmpty()){
                        return true;
                    }
                    String lowerCaseFilter = newValue.toLowerCase();

                    if(animal.getName().toLowerCase().contains(lowerCaseFilter)){
                        return true;
                    }else if(animal.getSpecie().toLowerCase().contains(lowerCaseFilter)){
                        return true;
                    }


                    return false;
                });
            });

            SortedList<Animals> sortedData = new SortedList<>(filteredData);
            sortedData.comparatorProperty().bind(tableView.comparatorProperty());
            tableView.setItems(sortedData);
        });


        actionColumn.setCellValueFactory(
                new Callback<TableColumn.CellDataFeatures<Record, Boolean>, ObservableValue<Boolean>>() {

                    @Override
                    public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<Record, Boolean> p) {
                        return new SimpleBooleanProperty(p.getValue() != null);
                    }
                });

        //Adding the Button to the cell
        actionColumn.setCellFactory(
                new Callback<TableColumn<Record, Boolean>, TableCell<Record, Boolean>>() {

                    @Override
                    public TableCell<Record, Boolean> call(TableColumn<Record, Boolean> p) {
                        return new ButtonCell();
                    }
                });
    }

    public void handleAddButtonAction(){

        Alert fail = new Alert(Alert.AlertType.INFORMATION);

        if (textFieldName.getText() == null ||textFieldName.getText().trim().isEmpty() ||
                textFieldSpecie.getText() == null ||textFieldSpecie.getText().trim().isEmpty() ||
                textFieldAge.getText() == null ||textFieldAge.getText().trim().isEmpty()||
                textFieldCage.getText() == null ||textFieldCage.getText().trim().isEmpty()) {

            fail.setHeaderText("Whoops..");
            fail.setContentText("You forgot to write something!");
            fail.showAndWait();

        }else if(!textFieldAge.getText().matches("[0-9]+") || !textFieldCage.getText().matches("[0-9]+")){
            fail.setHeaderText("Whoops..");
            fail.setContentText("Age and cage number must be numeric, cannot contain white spaces and must be integers.");
            fail.showAndWait();
        }
        else if(!textFieldName.getText().matches("[a-zA-z]+") || !textFieldSpecie.getText().matches("[a-zA-z]+") || textFieldSpecie.getText().length()<3 || textFieldSpecie.getText().length()<3){
            fail.setHeaderText("Whoops..");
            fail.setContentText("Name and specie must contain letters only (at least 3 characters) and cannot contain white spaces!");
            fail.showAndWait();
        }
        else{
            Animals newAnimal = new Animals(
                    textFieldName.getText(),
                    textFieldSpecie.getText(),
                    Integer.parseInt(textFieldAge.getText()),
                    Integer.parseInt(textFieldCage.getText())
            );
            animalsDAO.addAnimal(newAnimal);
            newAnimal.setId(animalsDAO.getAutoIncrementedId());
            data.add(newAnimal);


            textFieldName.clear();
            textFieldSpecie.clear();
            textFieldAge.clear();
            textFieldCage.clear();
        }
    }

    private class ButtonCell extends TableCell<Record, Boolean> {
        final Button cellButton = new Button("Delete");

        ButtonCell(){

            cellButton.setOnAction(new EventHandler<ActionEvent>(){

                @Override
                public void handle(ActionEvent t){

                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Confirmation Dialog");
                    alert.setHeaderText("Delete the entire row?");

                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.get() == ButtonType.OK){
                        Animals currentAnimal = (Animals) ButtonCell.this.getTableView().getItems().get(ButtonCell.this.getIndex());
                        data.remove(currentAnimal);
                        animalsDAO.deleteAnimalById(currentAnimal.getId());
                    }
                }
            });

        }
        //Display button if the row is not empty
        @Override
        protected void updateItem(Boolean t, boolean empty) {
            super.updateItem(t, empty);
            if(!empty){
                setGraphic(cellButton);

            } else {
                setGraphic(null);
            }
        }
    }

}
