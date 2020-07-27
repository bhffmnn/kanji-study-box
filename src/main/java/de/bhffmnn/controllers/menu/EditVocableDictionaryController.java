/*
 * Copyright (c) 2020, Benjamin Hoffmann
 * All rights reserved.

 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree.
 */

package de.bhffmnn.controllers.menu;

import de.bhffmnn.App;
import de.bhffmnn.models.Vocable;
import de.bhffmnn.models.VocableDictionary;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Controller for the EditVocableDictionary view that lets the user edit a VocableDictionary object. The object is
 * cloned and the save button has to be used in order to overwrite the original object.
 */

public class EditVocableDictionaryController implements Initializable {

    @FXML
    TableView dictTable;

    //Filter stuff
    @FXML
    TextField formFilterField;
    @FXML
    TextField readFilterField;
    @FXML
    TextField meanFilterField;

    @FXML
    CheckBox formCheckBox;
    @FXML
    CheckBox readCheckBox;
    @FXML
    CheckBox meanCheckBox;

    private VocableFilter vocableFilter;

    private TableColumn<String, Vocable> formClmn;
    private TableColumn<String, Vocable> readClmn;
    private TableColumn<String, Vocable> meanClmn;
    private TableColumn<String, Vocable> exmplClmn;

    //This exists so you can discard changes
    private VocableDictionary cloneDictionary;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cloneDictionary = App.vocableDictionary.cloneDeep();
        vocableFilter = new VocableFilter();

        formClmn = createColumn("Form", "form");
        readClmn = createColumn("Reading", "reading");
        meanClmn = createColumn("Meaning", "meaning");
        exmplClmn = createColumn("Example", "example");

        dictTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        dictTable.getColumns().add(formClmn);
        dictTable.getColumns().add(readClmn);
        dictTable.getColumns().add(meanClmn);
        dictTable.getColumns().add(exmplClmn);

        for (Vocable v : cloneDictionary) {
            dictTable.getItems().add(v);
        }

        //Filter part
        formFilterField.setDisable(true);
        readFilterField.setDisable(true);
        meanFilterField.setDisable(true);

        setUpCheckBox(formCheckBox, formFilterField);
        setUpCheckBox(readCheckBox, readFilterField);
        setUpCheckBox(meanCheckBox, meanFilterField);
    }


    @FXML
    private void backButtonAction(ActionEvent actionEvent) throws Exception {
        Parent parent = FXMLLoader.load(App.class.getResource("fxml/menu/mainMenu.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

        stage.setScene(scene);
        stage.show();
    }

    @FXML void addButtonAction(ActionEvent actionEvent) throws Exception {
        FXMLLoader loader = new FXMLLoader(App.class.getResource("fxml/menu/addVocable.fxml"));

        AddVocableController controller = new AddVocableController(cloneDictionary);
        loader.setController(controller);

        Stage childStage = new Stage();
        childStage.initModality(Modality.WINDOW_MODAL);
        childStage.initOwner(dictTable.getScene().getWindow());
        childStage.setScene(new Scene((loader.load())));
        childStage.showAndWait();

        reloadTable();
    }

    @FXML void editButtonAction(ActionEvent actionEvent) throws Exception {
        if (!dictTable.getSelectionModel().getSelectedCells().isEmpty()) {
            TablePosition position = (TablePosition) dictTable.getSelectionModel().getSelectedCells().get(0);
            Vocable vocable = (Vocable) dictTable.getItems().get(position.getRow());
            FXMLLoader loader = new FXMLLoader(App.class.getResource("fxml/menu/editVocable.fxml"));

            EditVocableController controller = new EditVocableController(vocable);
            loader.setController(controller);

            Stage childStage = new Stage();
            childStage.initModality(Modality.WINDOW_MODAL);
            childStage.initOwner(dictTable.getScene().getWindow());
            childStage.setScene(new Scene((loader.load())));
            childStage.showAndWait();

            reloadTable();
        }
    }

    @FXML
    void deleteButtonAction(ActionEvent actionEvent) throws Exception {
        //If nothing selected
        if (dictTable.getSelectionModel().getSelectedCells().isEmpty()) {
            return;
        }
        //If 1 row selected
        if (dictTable.getSelectionModel().getSelectedCells().size() == 1) {
            TablePosition position = (TablePosition) dictTable.getSelectionModel().getSelectedCells().get(0);
            Vocable vocable = (Vocable) dictTable.getItems().get(position.getRow());
            cloneDictionary.remove(vocable);
        }
        //If multiple rows selected
        else {
            for (int i = 0; i < dictTable.getSelectionModel().getSelectedCells().size(); i++) {
                TablePosition position = (TablePosition) dictTable.getSelectionModel().getSelectedCells().get(i);
                Vocable vocable = (Vocable) dictTable.getItems().get(position.getRow());
                cloneDictionary.remove(vocable);
            }
        }
        reloadTable();
    }

    @FXML
    private void saveButtonAction(ActionEvent actionEvent) throws Exception {
        App.vocableDictionary = cloneDictionary.cloneDeep();
        App.vocableDictionary.save(App.settings.getVocableDictionaryFilePath());
    }

    @FXML
    private void resetButtonAction(ActionEvent actionEvent) throws Exception {
        cloneDictionary = App.vocableDictionary.cloneDeep();
        reloadTable();
    }

    @FXML
    private void applyButtonAction(ActionEvent actionEvent) throws Exception {
        if (formFilterField.isDisabled()) {
            vocableFilter.setFormFilter(null);
        } else {
            vocableFilter.setFormFilter(formFilterField.getText());
        }

        if (readFilterField.isDisabled()) {
            vocableFilter.setReadFilter(null);
        } else {
            vocableFilter.setReadFilter(readFilterField.getText());
        }

        if (meanFilterField.isDisabled()) {
            vocableFilter.setMeanFilter(null);
        } else {
            vocableFilter.setMeanFilter(meanFilterField.getText());
        }

        reloadTable();
    }

    private TableColumn<String, Vocable> createColumn(String title, String feature) {
        TableColumn<String, Vocable> newColumn = new TableColumn<>(title);
        newColumn.setCellValueFactory(new PropertyValueFactory<>(feature));
        return newColumn;
    }

    private void reloadTable() {
        dictTable.getItems().clear();

        VocableDictionary filteredDictionary = vocableFilter.filter(cloneDictionary);

        for (Vocable v : filteredDictionary) {
            dictTable.getItems().add(v);
        }
    }

    private void setUpCheckBox(CheckBox checkBox, Node node) {
        checkBox.selectedProperty().addListener(((observableValue, oldValue, newValue) -> {
            if (newValue) {
                node.setDisable(false);
            }
            else {
                node.setDisable(true);
            }
        }));
    }

    private class VocableFilter {
        private String formFilter;
        private String readFilter;
        private String meanFilter;


        private VocableFilter() {
            formFilter = readFilter = meanFilter = null;
        }

        public String getFormFilter() {
            return formFilter;
        }
        public String getMeanFilter() {
            return meanFilter;
        }
        public String getReadFilter() {
            return readFilter;
        }

        public void setFormFilter(String formFilter) {
            this.formFilter = formFilter;
        }
        public void setMeanFilter(String meanFilter) {
            this.meanFilter = meanFilter;
        }
        public void setReadFilter(String readFilter) {
            this.readFilter = readFilter;
        }

        public VocableDictionary filter(VocableDictionary vocableDictionary) {
            VocableDictionary filteredDictionary = vocableDictionary.clone();
            if (!(formFilter == null)) {
                filteredDictionary = new VocableDictionary();
                filteredDictionary.add(vocableDictionary.getByForm(formFilter));
            }
            if (!(readFilter == null)) {
                ArrayList<Vocable> removalList = new ArrayList<>();
                for (Vocable v : filteredDictionary) {
                    if (!v.getReading().contains(readFilter)) {
                        removalList.add(v);
                    }
                }
                filteredDictionary.removeAll(removalList);
            }
            if (!(meanFilter == null)) {
                ArrayList<Vocable> removalList = new ArrayList<>();
                for (Vocable v : filteredDictionary) {
                    if (!v.getMeaning().contains(meanFilter)) {
                        removalList.add(v);
                    }
                }
                filteredDictionary.removeAll(removalList);
            }
            return filteredDictionary;
        }
    }

}