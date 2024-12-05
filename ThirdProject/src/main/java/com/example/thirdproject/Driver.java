package com.example.thirdproject;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

public class Driver extends Application {

    private QOHash<MDate<Martyr>> hashTable = new QOHash<>(5);
    private TextArea dateTextArea = new TextArea();
    private TextArea martyrTextArea = new TextArea();
    private TableView<HNode> tableView = new TableView<>();

    private Stack martyrTreeNext = new Stack<>();
    private Stack martyrTreePrev = new Stack<>();
    private DatePicker datePickerInsert = new DatePicker();
    private DatePicker datePickerDelete= new DatePicker();
    private DatePicker datePickerUpdateOld= new DatePicker();
    private DatePicker datePickerUpdateNew= new DatePicker();
    private String changeTheFirstString ;
    private String changeTheSecondString ;
    private SLinkedList<String> districtSList = new SLinkedList<>();
    private AVLTree<District> districtNames ;
    private AVLTree<String> locationNames;
    ObservableList<String> locationComboBoxItems = FXCollections.observableArrayList();
    ObservableList<String> districtComboBoxItems = FXCollections.observableArrayList();
    private ComboBox<String> districtComboBoxInsert = new ComboBox<> ();
    private ComboBox<String> locationComboBoxInsert= new ComboBox<> ();
    private ComboBox<String> districtComboDelete= new ComboBox<> ();
    private ComboBox<String> locationComboDelete= new ComboBox<> ();
    private ComboBox<String> districtComboUpdateOld= new ComboBox<> ();
    private ComboBox<String> locationComboUpdateOld= new ComboBox<> ();
    private ComboBox<String> districtComboUpdateNew= new ComboBox<> ();
    private ComboBox<String> locationComboUpdateNew= new ComboBox<> ();
    private ComboBox<String> genderInsert;
    private ComboBox<String> genderDelete;

    private ComboBox<String> genderComboUpdateOld;
    private ComboBox<String> genderComboUpdateNew;
    private ComboBox<String> printHashCombo;
    private File file;
    private FileChooser fileChooserWrite;

    private int currentIndex = -1;
    Label loadLabel;
    Stage stage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.stage = primaryStage;
        Button loadButton = new Button("Load Records");

        loadButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open Resource File");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("CSV Files", "*.csv")
            );
            File selectedFile = fileChooser.showOpenDialog(primaryStage);
            if (selectedFile != null) {
                loadRecordsFromFile(selectedFile);
                martyrDateScreen();
            }
        });

        loadLabel = new Label();

        VBox vStart = new VBox();
        vStart.getChildren().addAll(loadButton, loadLabel);
        vStart.setSpacing(10);
        vStart.setAlignment(Pos.CENTER);


        Scene scene = new Scene(vStart, 300, 100);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Read File Screen");
        primaryStage.show();
        primaryStage.centerOnScreen();

    }

    private void loadRecordsFromFile(File file) {

        hashTable.clear();

        try (Scanner scanner = new Scanner(file)) {
            scanner.nextLine(); // Skip the header line
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",", -1);
                if (parts.length >= 6) {
                    String name = parts[0].isEmpty() ? "Unknown" : parts[0];
                    String date = parts[1].isEmpty() ? "Unknown" : parts[1];
                    int age = 0;
                    try {
                        age = parts[2].isEmpty() ? -1 : Integer.parseInt(parts[2]);
                    } catch (NumberFormatException ex) {
                        loadLabel.setText("Invalid age format for record: " + name + ". Setting age to -1.\n");
                        continue; // Skip invalid record
                    }
                    String location = parts[3].isEmpty() ? "Unknown" : parts[3];
                    String district = parts[4].isEmpty() ? "Unknown" : parts[4];
                    String gender = parts[5].equals("NA") ? "NA" : parts[5];

                    insertFile(district, location, date, name, age, gender);
                    if(districtSList.find(district) == false){
                        districtSList.insert(district);
                    }
                    System.out.println(districtSList.length());
                }
            }
            loadLabel.setText("Records loaded successfully.\n");
        } catch (IOException e) {
            loadLabel.setText("Failed to load records.\n");
        }
    }

    public void martyrDateScreen() {
        Button btnInsertDate = new Button("Insert New Date");
        Button btnUpdateDate = new Button("Update Date");
        Button btnDeleteDate = new Button("Delete Date");
        Button btnPrintDate = new Button("Print Dates");
        Button btnUpDate = new Button("UP Date");
        Button btnDownDate = new Button("Down Date");
        Button btnLoadDate = new Button("Load Date");



        printHashCombo = new ComboBox<>();
        printHashCombo.getItems().addAll("including", "excluding");


        VBox vBox = new VBox();
        vBox.getChildren().addAll(btnUpDate,btnDownDate,btnLoadDate);
        vBox.setSpacing(10);
        vBox.setAlignment(Pos.CENTER);


        BorderPane dBorder = new BorderPane();

        GridPane datePane = new GridPane();
        datePane.setAlignment(Pos.CENTER);
        datePane.setHgap(10);
        datePane.setVgap(10);


        datePane.add(btnInsertDate, 0, 1);
        datePane.add(datePickerInsert, 1, 1);

        datePane.add(btnUpdateDate, 0, 2);
        datePane.add(datePickerUpdateOld, 1, 2);
        datePane.add(datePickerUpdateNew, 2, 2);

        datePane.add(btnDeleteDate, 0, 3);
        datePane.add(datePickerDelete, 1, 3);

        datePane.add(vBox, 0, 4);


        datePane.add(dateTextArea, 1, 4);
        GridPane.setColumnSpan(dateTextArea, 2);
        dBorder.setCenter(datePane);



        HBox HPrint = new HBox();
        HPrint.getChildren().addAll(printHashCombo, btnPrintDate);
        HPrint.setSpacing(10);
        HPrint.setAlignment(Pos.CENTER);

        VBox VPrint = new VBox();
        VPrint.getChildren().addAll(HPrint, tableView);
        VPrint.setSpacing(10);
        VPrint.setAlignment(Pos.CENTER);
        dBorder.setRight(VPrint);


        btnInsertDate.setOnAction(event -> {
            handleInsertDate();
        });

        btnUpdateDate.setOnAction(event -> {
            // Get the old and new dates from the DatePicker and TextField
            LocalDate oldDateL = datePickerUpdateOld.getValue(); // Use DatePicker for old date
            Date oldDate = Date.from(oldDateL.atStartOfDay(ZoneId.systemDefault()).toInstant());

            LocalDate newDateL = datePickerUpdateNew.getValue(); // Use DatePicker for new date
            Date newDate = Date.from(newDateL.atStartOfDay(ZoneId.systemDefault()).toInstant());


            if (oldDateL == null || newDateL == null) {
                dateTextArea.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
                dateTextArea.setText("Please select an old date and enter a new date.");
                return;
            }

            // Display a confirmation dialog
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Confirmation");
            confirmAlert.setHeaderText("Update Date");
            confirmAlert.setContentText("Are you sure you want to update the date from " + oldDate + " to " + newDate + "?");

            // Show the confirmation dialog and wait for user's response
            if (confirmAlert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
                // Perform the update
                if (updateDateRecord(hashTable, oldDate, newDate)) {
                    dateTextArea.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
                    dateTextArea.setText("Date updated: " + oldDate + " -> " + newDate);
                } else {
                    dateTextArea.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
                    dateTextArea.setText("Date update failed: " + oldDate + " not found.");
                }
            }
            datePickerUpdateOld.setValue(null);
            datePickerUpdateNew.setValue(null);
        });

        btnDeleteDate.setOnAction(event -> {
            LocalDate dateToDeleteL = datePickerDelete.getValue();
            Date dateToDelete = Date.from(dateToDeleteL.atStartOfDay(ZoneId.systemDefault()).toInstant());


            if (dateToDeleteL == null) {
                dateTextArea.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
                dateTextArea.setText("Please select a date to delete.");
                return;
            }

            // Display a confirmation dialog
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Confirmation");
            confirmAlert.setHeaderText("Delete Date");
            confirmAlert.setContentText("Are you sure you want to delete the date: " + dateToDelete + "?");

            // Show the confirmation dialog and wait for user's response
            if (confirmAlert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
                // Perform the deletion
                if (deleteDateRecord(hashTable, dateToDelete)) {
                    dateTextArea.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
                    dateTextArea.setText("Date deleted: " + dateToDelete);
                } else {
                    dateTextArea.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
                    dateTextArea.setText("Failed to delete date: " + dateToDelete + ". Date not found.");
                }
            }
            datePickerDelete.setValue(null);
        });

        btnPrintDate.setOnAction(event -> {
            printHashTable();
        });

        btnUpDate.setOnAction(event -> {
            MDate<Martyr> nextDate = navigateNext();
            if (nextDate != null) {
                // Display statistics for the next date
                calculateStatistics(nextDate);
            }
        });

        btnDownDate.setOnAction(event -> {
            MDate<Martyr> previousDate = navigatePrevious();
            if (previousDate != null) {
                // Display statistics for the previous date
                calculateStatistics(previousDate);
            }
        });

        btnLoadDate.setOnAction(event -> {
            MDate<Martyr> currentDate = navigateCurrentDate();
            if (currentDate != null) {
                updateComboBoxes(currentDate);
                martyrScreen(currentDate); // Your method to display martyr screen
            } else {
                dateTextArea.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
                dateTextArea.setText("Error: No date selected to load.");
            }
        });




        Scene scene = new Scene(dBorder, 1200, 600);
        stage.setScene(scene);
        stage.setTitle("Date Screen");
        stage.centerOnScreen();

    }

    private void updateComboBoxes(MDate<Martyr> currentDate) {

        SNode<String> current = new SNode<>("");
        current = districtSList.getHead();
        while (current!= null) {
            districtComboBoxInsert.getItems().add(current.getData());
            districtComboUpdateOld.getItems().add(current.getData());
            districtComboUpdateNew.getItems().add(current.getData());
            districtComboDelete.getItems().add(current.getData());
            current = current.getNext();
        }
    }

    public void martyrScreen(MDate martyrDate) {

        Button btnInsertM = new Button("Insert New Martyr");
        Button btnUpdateM = new Button("Update Martyr");
        Button btnDeleteM = new Button("Delete Martyr");
        Button btnShowSH = new Button("Size & Height");
        Button btnPrintM = new Button("Level by Level");
        Button btnSort = new Button("Sort");
        Button btnWriteM = new Button("Write to File");


        Label labelName = new Label("Name:");
        Label labelAge = new Label("Age:");
        Label labelDistrict = new Label("District:");
        Label labelLocation = new Label("Location:");
        Label labelGender = new Label("Gender:");


        Label labelFirstName = new Label("Name:");
        Label labelAgeUP = new Label("Age:");
        Label labelDistrictUP = new Label("District:");
        Label labelLocationUP = new Label("Location:");
        Label labelGenderUP = new Label("Gender");

        Label labelNameUpdate = new Label("Name:");
        Label labelAgeUpdate = new Label("Age:");
        Label labelDistrictUpdate = new Label("District:");
        Label labelLocationUpdate = new Label("Location:");
        Label labelGenderUpdate = new Label("Gender");


        locationComboBoxInsert.setItems(locationComboBoxItems);

        Label labelNameD = new Label("Name:");
        Label labelAgeD = new Label("Age:");
        Label labelGenderD = new Label("Gender");
        Label labelDistrictD = new Label("District:");
        Label labelLocationD = new Label("Location:");


        genderInsert = new ComboBox<>();
        genderInsert.getItems().addAll("M", "F");


        genderComboUpdateOld = new ComboBox<>();
        genderComboUpdateOld.getItems().addAll("M", "F");




        genderComboUpdateNew = new ComboBox<>();
        genderComboUpdateNew.getItems().addAll("M", "F");

        genderDelete = new ComboBox<>();
        genderDelete.getItems().addAll("M", "F");


        TextField textName = new TextField();
        textName.setPromptText("Name");
        TextField textAge = new TextField();
        textAge.setPromptText("Age");


        TextField upNameField = new TextField();
        upNameField.setPromptText("Full Name");
        TextField upAgeField = new TextField();
        upAgeField.setPromptText("Age");



        TextField textNameUpdate = new TextField();
        TextField textAgeUpdate = new TextField();


        textNameUpdate.setPromptText("New Name");
        textAgeUpdate.setPromptText("New Age");

        TextField deleteFieldM = new TextField();
        deleteFieldM.setPromptText(" Martyr");
        TextField deleteAgeFieldM = new TextField();
        deleteAgeFieldM.setPromptText(" Age");



        HBox buttonBox = new HBox(labelName, textName, labelAge, textAge, labelDistrict, districtComboBoxInsert, labelLocation, locationComboBoxInsert, labelGender, genderInsert);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setSpacing(10);
        buttonBox.setPadding(new Insets(10, 10, 10, 10));

        HBox buttonBoxUPNew = new HBox(labelNameUpdate, textNameUpdate, labelAgeUpdate, textAgeUpdate, labelDistrictUP, districtComboUpdateOld, labelLocationUP, locationComboUpdateOld, labelGenderUpdate, genderComboUpdateOld);
        //buttonBoxUPNew.setAlignment(Pos.CENTER);
        buttonBoxUPNew.setSpacing(10);
        buttonBoxUPNew.setPadding(new Insets(10, 10, 10, 10));

        HBox buttonBox2 = new HBox(labelFirstName, upNameField, labelAgeUP, upAgeField, labelDistrictUpdate, districtComboUpdateNew, labelLocationUpdate, locationComboUpdateNew, labelGenderUP, genderComboUpdateNew);
        //buttonBox2.setAlignment(Pos.CENTER);
        buttonBox2.setSpacing(10);
        buttonBox2.setPadding(new Insets(10, 10, 10, 10));

        HBox buttonBox3 = new HBox(labelNameD, deleteFieldM, labelAgeD, deleteAgeFieldM,labelDistrictD,districtComboDelete,labelLocationD,locationComboDelete, labelGenderD, genderDelete);
        // buttonBox3.setAlignment(Pos.CENTER);
        buttonBox3.setSpacing(10);
        buttonBox3.setPadding(new Insets(10, 10, 10, 10));


        BorderPane martyrBorder = new BorderPane();

        GridPane martyrGrid = new GridPane();
        martyrGrid.setAlignment(Pos.CENTER);
        martyrGrid.setHgap(10);
        martyrGrid.setVgap(10);
        martyrGrid.add(btnInsertM, 0, 0);
        martyrGrid.add(buttonBox, 1, 0);

        martyrGrid.add(btnUpdateM, 0, 1);
        martyrGrid.add(buttonBox2, 1, 1);
        martyrGrid.add(buttonBoxUPNew, 1, 2);

        martyrGrid.add(btnDeleteM, 0, 3);
        martyrGrid.add(buttonBox3, 1, 3);


        martyrGrid.add(btnShowSH, 1, 4);
        martyrGrid.add(btnPrintM, 0, 4);
        martyrGrid.add(btnSort, 2, 4);
        martyrGrid.add(btnWriteM, 3, 4);

        martyrGrid.add(martyrTextArea, 0, 6);
        GridPane.setColumnSpan(martyrTextArea, 3);
        GridPane.setHalignment(martyrTextArea, HPos.CENTER);
        GridPane.setValignment(martyrTextArea, VPos.CENTER);

        martyrBorder.setCenter(martyrGrid);

        btnInsertM.setOnAction(event -> {
            String name = textName.getText().trim();
            String age = textAge.getText().trim();
            String gender = genderInsert.getValue();
            String district = districtComboBoxInsert.getValue();
            String location = locationComboBoxInsert.getValue();

            // Validate input (check for empty fields)
            if (name.isEmpty() || age.isEmpty() || gender.isEmpty() || district == null || location == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Missing Information");
                alert.setContentText("Please fill out all fields.");
                alert.showAndWait();
                return;
            }

            // Validate gender
            if (!gender.equalsIgnoreCase("M") && !gender.equalsIgnoreCase("F")) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Invalid Gender");
                alert.setContentText("Gender must be either M or F.");
                alert.showAndWait();
                return;
            }

            // Validate age (must be a valid integer)
            int martyrAge;
            try {
                martyrAge = Integer.parseInt(age);
                if (martyrAge < 0 || martyrAge > 150) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Invalid Age");
                    alert.setContentText("Age must be between 0 and 150.");
                    alert.showAndWait();
                    return;
                }
            } catch (NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Invalid Age");
                alert.setContentText("Age must be a valid integer.");
                alert.showAndWait();
                return;
            }
            MDate dateRecord = hashTable.search(new MDate<>(martyrDate.getDate()));

            // Create a new martyr instance
            Martyr newMartyr = new Martyr(name, martyrAge, location, district, gender);

            // Insert the new martyr into the AVL tree
            dateRecord.addMartyr(newMartyr);

            // Display success message
            martyrTextArea.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
            martyrTextArea.setText("New martyr record inserted successfully.");

            // Clear input fields
            textName.clear();
            textAge.clear();

        });

        btnUpdateM.setOnAction(event -> {
            // Retrieve input values from text fields and combo boxes
            String currentName = upNameField.getText().trim();
            String currentAge = upAgeField.getText().trim();
            String currentGender = genderComboUpdateNew.getValue();
            String currentDistrict = districtComboUpdateNew.getValue();
            String currentLocation = locationComboUpdateNew.getValue();

            String newName = textNameUpdate.getText().trim();
            String newAge = textAgeUpdate.getText().trim();
            String newGender = genderComboUpdateOld.getValue();
            String newDistrict = districtComboUpdateOld.getValue();
            String newLocation = locationComboUpdateOld.getValue();

            // Validate input (check for empty fields)
            if (currentName.isEmpty() || currentAge.isEmpty() || currentGender.isEmpty() || currentDistrict.isEmpty() || currentLocation.isEmpty() ||
                    newName.isEmpty() || newAge.isEmpty() || newGender.isEmpty() || newDistrict.isEmpty() || newLocation.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Missing Information");
                alert.setContentText("Please fill out all fields.");
                alert.showAndWait();
                return;
            }

            // Validate gender
            if (!currentGender.equalsIgnoreCase("M") && !currentGender.equalsIgnoreCase("F") ||
                    !newGender.equalsIgnoreCase("M") && !newGender.equalsIgnoreCase("F")) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Invalid Gender");
                alert.setContentText("Gender must be either M or F.");
                alert.showAndWait();
                return;
            }

            // Validate age (must be a valid integer)
            int martyrCurrentAge;
            int martyrNewAge;
            try {
                martyrCurrentAge = Integer.parseInt(currentAge);
                martyrNewAge = Integer.parseInt(newAge);
                if (martyrCurrentAge < 0 || martyrCurrentAge > 150 || martyrNewAge < 0 || martyrNewAge > 150) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Invalid Age");
                    alert.setContentText("Age must be between 0 and 150.");
                    alert.showAndWait();
                    return;
                }
            } catch (NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Invalid Age");
                alert.setContentText("Age must be a valid integer.");
                alert.showAndWait();
                return;
            }
            MDate dateRecord = hashTable.search(new MDate<>(martyrDate.getDate()));

            // Create the current martyr instance
            Martyr currentMartyr = new Martyr(currentName, martyrCurrentAge, currentLocation, currentDistrict, currentGender);

            // Create the new martyr instance
            Martyr newMartyr = new Martyr(newName, martyrNewAge, newLocation, newDistrict, newGender);

            // Check if the current martyr exists
            if (dateRecord.search(currentMartyr)) {
                // Update the martyr information

                dateRecord.removeMartyr(currentMartyr);
                dateRecord.addMartyr(newMartyr);
                // Display success message
                martyrTextArea.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
                martyrTextArea.setText("Martyr record updated successfully.");
            } else {
                martyrTextArea.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
                martyrTextArea.setText("Martyr not found.");
            }

            // Clear input fields
            upNameField.clear();
            upAgeField.clear();
            textNameUpdate.clear();
            textAgeUpdate.clear();
        });



        btnDeleteM.setOnAction(event -> {
            String martyrFullName = deleteFieldM.getText().trim();
            String martyrAge = deleteAgeFieldM.getText().trim();
            String martyrDistrict = districtComboDelete.getValue();
            String martyrLocation = locationComboDelete.getValue();
            String martyrGender = genderDelete.getValue();


            if (martyrFullName.isEmpty()) {
                martyrTextArea.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
                martyrTextArea.setText("Please enter the full name of the martyr to delete.");
                return;
            }
            if (martyrAge.isEmpty()) {
                martyrTextArea.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
                martyrTextArea.setText("Please enter the age of the martyr to delete.");
                return;
            }
            if (martyrDistrict.isEmpty()) {
                martyrTextArea.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
                martyrTextArea.setText("Please enter the district of the martyr to delete.");
                return;
            }
            if (martyrLocation.isEmpty()) {
                martyrTextArea.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
                martyrTextArea.setText("Please enter the location of the martyr to delete.");
                return;
            }
            if (genderDelete.getValue() == null) {
                martyrTextArea.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
                martyrTextArea.setText("Please enter the gender of the martyr to delete.");
                return;
            }
            // Display confirmation dialog before clearing input fields
            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
            confirmation.setTitle("Confirmation");
            confirmation.setHeaderText("Update Martyr Information");
            confirmation.setContentText("Are you sure you want to Delete this martyr's information?");
            confirmation.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    // Assuming you have a method to find the martyr's record
                    Martyr oldMartyr = findMartyr(martyrDate,martyrFullName, martyrDistrict, martyrLocation, Integer.parseInt(martyrAge), martyrGender);

                    martyrDate.getMartyrTree().delete(oldMartyr);

                    martyrTextArea.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
                    martyrTextArea.setText("Martyr information Deleted successfully.");


                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Martyr Not Found");
                    alert.setContentText("The specified martyr could not be found.");
                    alert.showAndWait();
                }

                // Clear input fields
                deleteFieldM.clear();
                deleteAgeFieldM.clear();

            });
        });

        btnShowSH.setOnAction(event -> {
            martyrTextArea.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
            martyrTextArea.setText("The height of the tree is: " + martyrDate.getMartyrTree().height() + "\n" + "The size of the tree is: " + martyrDate.getMartyrTree().size());

        });

        btnPrintM.setOnAction(event -> {
            traverseMartyrsLevelByLevel(martyrDate);
        });

        btnSort.setOnAction(event -> {
            MartyrCompareAge order = new MartyrCompareAge();
            Heap<Martyr> heap = new Heap<>();
            Martyr[] arrays = new Martyr[martyrDate.getMartyrTree().size()];
            int i = 0;
            for (int j = 0; j < martyrDate.getMartyrTree().size(); j++) {
                arrays[j] = (Martyr) martyrDate.getMartyrTree().get(j);
            }
            heap.heapSort(arrays ,order);

            martyrTextArea.clear();
            // Append each martyr's string representation to the text area
            for (Martyr martyr : arrays) {
                martyrTextArea.appendText(martyr.toString() + "\n");
            }

            // Apply text area styles
            martyrTextArea.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
        });

        btnWriteM.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save Records");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
            File file = fileChooser.showSaveDialog(stage);

            if (file != null) {
                try (FileWriter writer = new FileWriter(file)) {
                    writer.write("Name,event,Age,location,District,Gender\n");

                    for (int i = 0; i < hashTable.getM(); i++) {
                        HNode<MDate<Martyr>> entry = hashTable.getTable()[i];
                        MDate<Martyr> mDate = entry.getValue();
                        if (mDate != null) {
                            Date date = mDate.getDate();
                            AVLTree<Martyr> martyrTree = mDate.getMartyrTree();

                            TNode<Martyr> root = martyrTree.getRoot();
                            if (root != null) {
                                Queue<TNode<Martyr>> queue = new Queue<>();
                                queue.enqueue(root);

                                while (!queue.isEmpty()) {
                                    TNode<Martyr> node = queue.dequeue();
                                    Martyr martyr = node.getValue();
                                    SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
                                    String eventName = dateFormat.format(date);

                                    writer.write(martyr.getName() + "," + eventName + "," + martyr.getAge() + "," + martyr.getLocation() + "," + martyr.getDistrict() + "," + martyr.getGender() + "\n");

                                    if (node.getLeft() != null) queue.enqueue(node.getLeft());
                                    if (node.getRight() != null) queue.enqueue(node.getRight());
                                }
                            }
                        }
                    }

                    martyrTextArea.setText("Records saved successfully.");
                } catch (IOException e) {
                    e.printStackTrace();
                    martyrTextArea.setText("Failed to save records.");
                }
            } else {
                martyrTextArea.setText("File save operation canceled.");
            }
        });


        districtComboBoxInsert.setOnAction(e -> {
            locationComboBoxInsert.getItems().clear();
            setTheFirstChangeString(districtComboBoxInsert.getValue().trim());
            MDate date;
            for (int i = 0; i < hashTable.getTable().length; i++) {
                date = hashTable.getTable()[i].getValue();
                if (date != null) {
                    date.getMartyrTree().traverseInOrder();
                    Stack<TNode<Martyr>> martyrStack = date.getMartyrTree().getNextStack();
                    Martyr selectMartyr = new Martyr();
                    while (!martyrStack.isEmpty()) {
                        selectMartyr = martyrStack.pop().getValue();
                        if (selectMartyr.getDistrict().compareTo(getTheFirstChangeString().trim()) == 0
                                && !locationComboBoxInsert.getItems().contains(selectMartyr.getLocation())) {
                            locationComboBoxInsert.getItems().add(selectMartyr.getLocation());
                        }
                    }
                }
            }
        });
        locationComboBoxInsert.setOnAction(e -> {
            setTheSecondChangeString(locationComboBoxInsert.getValue());
        });
        districtComboDelete.setOnAction(event -> {
            locationComboDelete.getItems().clear();
            setTheFirstChangeString(districtComboDelete.getValue().trim());
            MDate date;
            for (int i = 0; i < hashTable.getTable().length; i++) {
                date = hashTable.getTable()[i].getValue();
                if (date != null) {
                    date.getMartyrTree().traverseInOrder();
                    Stack<TNode<Martyr>> martyrStack = date.getMartyrTree().getNextStack();
                    Martyr selectMartyr = new Martyr();
                    while (!martyrStack.isEmpty()) {
                        selectMartyr = martyrStack.pop().getValue();
                        if (selectMartyr.getDistrict().compareTo(getTheFirstChangeString().trim()) == 0
                                && !locationComboDelete.getItems().contains(selectMartyr.getLocation())) {
                            locationComboDelete.getItems().add(selectMartyr.getLocation());
                        }
                    }
                }
            }
        });
        locationComboDelete.setOnAction(event -> {
            setTheSecondChangeString(locationComboDelete.getValue());
        });
        districtComboUpdateOld.setOnAction(event -> {
            locationComboUpdateOld.getItems().clear();
            setTheFirstChangeString(districtComboUpdateOld.getValue().trim());
            MDate date;
            for (int i = 0; i < hashTable.getTable().length; i++) {
                date = hashTable.getTable()[i].getValue();
                if (date != null) {
                    date.getMartyrTree().traverseInOrder();
                    Stack<TNode<Martyr>> martyrStack = date.getMartyrTree().getNextStack();
                    Martyr selectMartyr = new Martyr();
                    while (!martyrStack.isEmpty()) {
                        selectMartyr = martyrStack.pop().getValue();
                        if (selectMartyr.getDistrict().compareTo(getTheFirstChangeString().trim()) == 0
                                && !locationComboUpdateOld.getItems().contains(selectMartyr.getLocation())) {
                            locationComboUpdateOld.getItems().add(selectMartyr.getLocation());
                        }
                    }
                }
            }
        });
        locationComboUpdateOld.setOnAction(event -> {
            setTheSecondChangeString(locationComboUpdateOld.getValue());
        });
        districtComboUpdateNew.setOnAction(event -> {
            locationComboUpdateNew.getItems().clear();
            setTheFirstChangeString(districtComboUpdateNew.getValue().trim());
            MDate date;
            for (int i = 0; i < hashTable.getTable().length; i++) {
                date = hashTable.getTable()[i].getValue();
                if (date != null) {
                    date.getMartyrTree().traverseInOrder();
                    Stack<TNode<Martyr>> martyrStack = date.getMartyrTree().getNextStack();
                    Martyr selectMartyr = new Martyr();
                    while (!martyrStack.isEmpty()) {
                        selectMartyr = martyrStack.pop().getValue();
                        if (selectMartyr.getDistrict().compareTo(getTheFirstChangeString().trim()) == 0
                                && !locationComboUpdateNew.getItems().contains(selectMartyr.getLocation())) {
                            locationComboUpdateNew.getItems().add(selectMartyr.getLocation());
                        }
                    }
                }
            }
        });
        locationComboUpdateNew.setOnAction(event -> {
            setTheSecondChangeString(locationComboUpdateNew.getValue());
        });


        Scene scene = new Scene(martyrBorder, 1500, 675);
        stage.setScene(scene);
        stage.setTitle("Martyr Screen");
        stage.centerOnScreen();

    }


    private District findDistrictByName(String districtName) {
        // Assuming districtNames is initialized elsewhere in your code
        if (districtNames == null) {
            return null; // Return null if districtNames is not initialized
        }

        // Implement the logic to find the district by name in districtNames AVLTree
        District target = new District(districtName);
        return districtNames.find(target);
    }


    private Martyr findMartyr(MDate martyrDate, String name, String district, String location, int age, String gender) {
        if (martyrDate == null || name == null || district == null || location == null || gender == null) {
            System.out.println("something not full");
            return null; // Return null if any required parameter is null
        }

        AVLTree<Martyr> martyrTree = martyrDate.getMartyrTree();
        if (martyrTree == null || martyrTree.isEmpty()) {
            System.out.println("Martyr tree null");

            return null; // Return null if the AVL tree is empty or not initialized
        }

        Martyr martyrRecord = new Martyr(name, age, location, district, gender);
        TNode<Martyr> foundNode = martyrTree.findNode(martyrRecord, martyrTree.getRoot());

        if (foundNode != null) {
            System.out.println("Martyr tree found");
            return foundNode.getValue(); // Return the martyr found in the AVL tree
        } else {
            System.out.println("Martyr tree not found");
            return null; // Return null if the martyr was not found in the AVL tree
        }
    }



    private void insertFile(String district, String location, String dateString, String name, int age, String gender) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
            Date date = dateFormat.parse(dateString);

            Martyr newMartyr = new Martyr(name, age, location, district, gender);
            MDate dateRecord = hashTable.search(new MDate<>(date));

            if (dateRecord == null) {
                dateRecord = new MDate(date);

                dateRecord.getMartyrTree().insert(newMartyr);

                hashTable.add(dateRecord);
                return;
            }
            System.out.println("1");

            dateRecord.getMartyrTree().insert(newMartyr);
        } catch (ParseException e) {
            System.err.println("Failed to parse date for record: " + name + ". Skipping the record.");
        }
    }

    private void printHashTable() {
        tableView.getColumns().clear(); // Clear existing columns

        TableColumn<HNode, Integer> indexColumn = new TableColumn<>("Index");
        indexColumn.setCellValueFactory(new PropertyValueFactory<>("index"));

        TableColumn<HNode, Character> flagColumn = new TableColumn<>("Flag");
        flagColumn.setCellValueFactory(new PropertyValueFactory<>("flag"));

        TableColumn<HNode, String> valueColumn = new TableColumn<>("Value");
        valueColumn.setCellValueFactory(new PropertyValueFactory<>("value"));

        tableView.getColumns().addAll(indexColumn, flagColumn, valueColumn);

        HNode<MDate<Martyr>>[] table = hashTable.getTable();

        // Clear any existing items in the table view
        tableView.getItems().clear();

        // Listener for combo box selection change
        printHashCombo.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            boolean includeEmptySpots = newValue.equals("including");

            tableView.getItems().clear(); // Clear existing items

            for (int i = 0; i < table.length; i++) {
                HNode<MDate<Martyr>> node = table[i];

                if (includeEmptySpots) {
                    // Include nodes with flags 'f', 'd', or 'e'
                    String value = node.getValue() != null ? node.getValue().toString() : "null";
                    tableView.getItems().add(new HNode(i, node.getFlag(), value));
                } else {
                    // Exclude empty spots, only include nodes with flag 'f' and non-null value
                    if (node.getFlag() == 'f' && node.getValue() != null) {
                        String value = node.getValue() != null ? node.getValue().toString() : "null";
                        tableView.getItems().add(new HNode(i, node.getFlag(), value));
                    }
                }
            }
        });

        // Set default selection to "including"
        printHashCombo.getSelectionModel().select("including");
    }

    private void handleInsertDate() {
        if (datePickerInsert.getValue() == null) {
            dateTextArea.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
            dateTextArea.setText("Please select a date.");
            return;
        }

        LocalDate newDateL = datePickerInsert.getValue();
        Date newDate = Date.from(newDateL.atStartOfDay(ZoneId.systemDefault()).toInstant());


        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        Date date;
        try {
            date = dateFormat.parse(dateFormat.format(newDate));
        } catch (ParseException e) {
            dateTextArea.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
            dateTextArea.setText("Invalid date format.");
            return;
        }


        // Check if the date already exists in the hash table
        MDate<Martyr> existingMDate = hashTable.search(new MDate<>(newDate, null));
        if (existingMDate != null) {
            dateTextArea.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
            dateTextArea.setText("Date " + newDate + " already exists in the hash table.");
            return;
        }

        // Create a new MDate and insert it into the hash table
        AVLTree<Martyr> martyrTree = new AVLTree<>();
        MDate<Martyr> newMDate = new MDate<Martyr>(newDate, martyrTree);
        hashTable.add(newMDate);

        dateTextArea.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
        dateTextArea.setText("Date " + newDate + " inserted successfully.");
        datePickerInsert.setValue(null);
    }

    private boolean updateDateRecord(QOHash<MDate<Martyr>> hashTable, Date oldDate, Date newDate) {
        // Create a temporary MDate object with the old date for searching
        MDate<Martyr> oldMDate = new MDate<>(oldDate, null);
        MDate<Martyr> foundMDate = hashTable.search(oldMDate);

        if (foundMDate == null) {
            return false; // Old date not found
        }

        // Delete the old date record
        hashTable.delete(oldMDate);

        // Create a new MDate with the new date, transferring the martyrTree
        MDate<Martyr> newMDate = new MDate<>(newDate, foundMDate.getMartyrTree());

        // Insert the new date record
        hashTable.add(newMDate);

        return true; // Update successful
    }
    private boolean deleteDateRecord(QOHash<MDate<Martyr>> hashTable, Date dateToDelete) {
        // Create a temporary MDate object with the date to delete
        MDate<Martyr> dateToDeleteObj = new MDate<>(dateToDelete, null);

        // Use the search method to find the MDate object
        MDate<Martyr> foundDate = hashTable.search(dateToDeleteObj);

        // If the MDate object is found, delete it
        if (foundDate != null) {
            hashTable.delete(dateToDeleteObj);
            return true; // Deletion successful
        } else {
            return false; // Date not found
        }
    }

    public MDate<Martyr> navigateNext() {
        if (currentIndex < hashTable.getM() - 1) {
            currentIndex++;
            while (currentIndex < hashTable.getM() && (hashTable.getTable()[currentIndex].getFlag() == 'e' || hashTable.getTable()[currentIndex].getFlag() == 'd')) {
                currentIndex++;
            }
            if (currentIndex < hashTable.getM()) {
                return hashTable.getTable()[currentIndex].getValue();
            }
        }
        return null;
    }

    public MDate<Martyr> navigatePrevious() {
        if (currentIndex > 0) {
            currentIndex--;
            while (currentIndex >= 0 && (hashTable.getTable()[currentIndex].getFlag() == 'e' || hashTable.getTable()[currentIndex].getFlag() == 'd')) {
                currentIndex--;
            }
            if (currentIndex >= 0) {
                return hashTable.getTable()[currentIndex].getValue();

            }
        }
        return null;
    }
    public MDate<Martyr> navigateCurrentDate() {
        if (currentIndex >= 0 && currentIndex < hashTable.getM()) {
            return hashTable.getTable()[currentIndex].getValue();
        } else {
            return null;
        }
    }

    public void traverseMartyrsLevelByLevel(MDate martyrDate) {
        if (martyrDate == null || martyrDate.getMartyrTree() == null || martyrDate.getMartyrTree().getRoot() == null) {
            return;
        }

        martyrTreeNext.clear();
        martyrTreePrev.clear();
        TNode<Martyr> root = martyrDate.getMartyrTree().getRoot();
        Queue<TNode<Martyr>> queue = new Queue<>();
        queue.enqueue(root); // Enqueue the root node

        while (!queue.isEmpty()) {
            int levelSize = queue.size();

            for (int i = 0; i < levelSize; i++) {
                TNode<Martyr> tempNode = queue.dequeue();
                martyrTreePrev.push(tempNode.getValue()); // Directly push to martyrTreePrev

                if (tempNode.getRight() != null) {
                    queue.enqueue(tempNode.getRight());
                }

                if (tempNode.getLeft() != null) {
                    queue.enqueue(tempNode.getLeft());
                }

            }
        }

        // Set the accumulated martyrs text to the martyrTextArea
        martyrTextArea.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
        martyrTextArea.clear(); // Clear the text area before adding new content

        Stack<Martyr> reverseStack = new Stack<>();
        while (!martyrTreePrev.isEmpty()) {
            reverseStack.push((Martyr) martyrTreePrev.pop());
        }

        while (!reverseStack.isEmpty()) {
            Martyr martyr = reverseStack.pop();
            martyrTextArea.appendText(martyr.toString() + "\n"); // Append martyr info directly to the text area
            martyrTreeNext.push(martyr);
        }
    }

    public static void writeRecordsToCSV(QOHash<MDate<Martyr>> hashTable, File file) {
        try (FileWriter writer = new FileWriter(file)) {
            writer.write("Name,event,Age,location,District,Gender\n");

            for (int i = 0; i < hashTable.getM(); i++) {
                HNode<MDate<Martyr>> entry = hashTable.getTable()[i];
                MDate<Martyr> mDate = entry.getValue();
                if (mDate != null) {
                    Date date = mDate.getDate();
                    AVLTree<Martyr> martyrTree = mDate.getMartyrTree();
                    writeMartyrs(writer, date, martyrTree);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void writeMartyrs(FileWriter writer, Date date, AVLTree<Martyr> martyrTree) throws IOException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        SimpleDateFormat eventFormat = new SimpleDateFormat("MM/dd/yyyy");

        TNode<Martyr> root = martyrTree.getRoot();
        if (root == null) return;

        Queue<TNode<Martyr>> queue = new Queue<>();
        queue.enqueue(root);

        while (!queue.isEmpty()) {
            TNode<Martyr> node = queue.dequeue();
            Martyr martyr = node.getValue();
            String eventName = eventFormat.format(date);
            String formattedDate = dateFormat.format(date);

            writer.write(martyr.getName() + "," + eventName + "," + martyr.getAge() + "," + martyr.getLocation() + "," + martyr.getDistrict() + "," + martyr.getGender() + "\n");

            if (node.getLeft() != null) queue.enqueue(node.getLeft());
            if (node.getRight() != null) queue.enqueue(node.getRight());
        }
    }


    public void calculateStatistics(MDate<Martyr> mDate) {
        Date date = mDate.getDate();
        int totalMartyrs = 0;
        double totalAge = 0;
        int maleCount = 0;
        int femaleCount = 0;

        // Track the count for each district and location
        String maxDistrict = "";
        int maxDistrictMartyrs = 0;
        String maxLocation = "";
        int maxLocationMartyrs = 0;

        // Traverse the AVL tree to calculate statistics and track unique districts and locations
        for (int i = 0; i < mDate.getMartyrTree().size(); i++) {
            Martyr martyr = mDate.getMartyrTree().get(i);
            if (martyr.getAge() != -1) { // Check if age is not -1
                totalMartyrs++;
                totalAge += martyr.getAge();
                if (martyr.getGender().equalsIgnoreCase("M")) {
                    maleCount++;
                } else if (martyr.getGender().equalsIgnoreCase("F")) {
                    femaleCount++;
                }

                // Update unique districts and find district with maximum martyrs
                String district = String.valueOf(martyr.getDistrict());
                int districtMartyrs = countMartyrs(mDate.getMartyrTree(), district);
                if (districtMartyrs > maxDistrictMartyrs) {
                    maxDistrictMartyrs = districtMartyrs;
                    maxDistrict = district;
                }

                // Update unique locations and find location with maximum martyrs
                String location = martyr.getLocation();
                int locationMartyrs = countMartyrs(mDate.getMartyrTree(), location);
                if (locationMartyrs > maxLocationMartyrs) {
                    maxLocationMartyrs = locationMartyrs;
                    maxLocation = location;
                }
            }
        }

        // Calculate average age
        double avgAge = totalMartyrs > 0 ? totalAge / totalMartyrs : 0;

        // Update the TextArea with the calculated statistics
        dateTextArea.setText("Date:" + date +"\n" +
                "Total Martyrs: " + totalMartyrs + "\n" +
                "Average Age: " + avgAge + "\n" +
                "Male Count: " + maleCount + "\n" +
                "Female Count: " + femaleCount + "\n" +
                "District with Maximum Martyrs: " + maxDistrict + "\n" +
                "Location with Maximum Martyrs: " + maxLocation);
    }

    private int     countMartyrs(AVLTree<Martyr> martyrTree, String value) {
        int count = 0;
        for (int i = 0; i < martyrTree.size(); i++) {
            Martyr martyr = martyrTree.get(i);
            if (martyr.getAge() != -1 && (martyr.getDistrict().equals(value) || martyr.getLocation().equals(value))) {
                count++;
            }
        }
        return count;
    }

    private ObservableList<String> locationNamesComboBox(District selectedDistrict) {
        ObservableList<String> locationNames = FXCollections.observableArrayList();

        if (selectedDistrict != null) {
            AVLTree<String> locationTree = selectedDistrict.getLocation();
            if (locationTree != null) {
                traverseInOrder(locationTree.getRoot(), locationNames);
            }
        }
        return locationNames;
    }

    private void traverseInOrder(TNode<String> node, ObservableList<String> locationNames) {
        if (node != null) {
            traverseInOrder(node.getLeft(), locationNames);
            locationNames.add(node.getValue());
            traverseInOrder(node.getRight(), locationNames);
        }
    }

    private MDate<Martyr> getCurrentMDate() {
        LocalDate selectedDate = datePickerInsert.getValue(); // Get the selected date from the DatePicker

        if (selectedDate == null) {
            return null; // No date selected
        }

        Date currentDate = Date.from(selectedDate.atStartOfDay(ZoneId.systemDefault()).toInstant()); // Convert LocalDate to Date

        // Assuming hashTable is a collection that stores MDate objects, and it has a method to search for an MDate by Date
        return hashTable.search(new MDate<>(currentDate, null)); // Modify this based on your actual implementation of hashTable
    }

    public String getTheFirstChangeString() {
        return changeTheFirstString;
    }

    public void setTheFirstChangeString(String changeString) {
            this.changeTheFirstString = changeString;
    }

    public String getTheSecondChangeString() {
        return changeTheSecondString;
    }

    public void setTheSecondChangeString(String changeString) {
        this.changeTheSecondString = changeString;
    }

}
