/*
 * Copyright (c) 2020, Benjamin Hoffmann
 * All rights reserved.

 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree.
 */

package de.bhffmnn.controllers.menu;

import de.bhffmnn.App;
import de.bhffmnn.models.Kanji;
import de.bhffmnn.models.KanjiDictionary;
import de.bhffmnn.models.Vocable;
import de.bhffmnn.models.VocableDictionary;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class EditVocableDictionaryController implements Initializable {

    @FXML
    TableView dictTable;

    private TableColumn<String, Vocable> formClmn;
    private TableColumn<String, Vocable> readClmn;
    private TableColumn<String, Vocable> meanClmn;
    private TableColumn<String, Vocable> exmplClmn;
    private TableColumn<String, Vocable> lvlClmn;
    private TableColumn<String, Vocable> dueClmn;

    //This exists so you can discard changes
    private VocableDictionary cloneDictionary;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cloneDictionary = App.vocableDictionary.cloneDeep();

        formClmn = createColumn("Form", "form");
        readClmn = createColumn("Reading", "reading");
        meanClmn = createColumn("Meaning", "meaning");
        exmplClmn = createColumn("Example", "example");
        lvlClmn = createColumn("Level", "level");
        dueClmn = createColumn("Due", "due");

        dictTable.getColumns().add(formClmn);
        dictTable.getColumns().add(readClmn);
        dictTable.getColumns().add(meanClmn);
        dictTable.getColumns().add(exmplClmn);
        dictTable.getColumns().add(lvlClmn);
        dictTable.getColumns().add(dueClmn);

        for (Vocable v : cloneDictionary) {
            dictTable.getItems().add(v);
        }

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
        if (!dictTable.getSelectionModel().getSelectedCells().isEmpty()) {
            TablePosition position = (TablePosition) dictTable.getSelectionModel().getSelectedCells().get(0);
            Vocable vocable = (Vocable) dictTable.getItems().get(position.getRow());
            cloneDictionary.remove(vocable);
            reloadTable();
        }
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

    private TableColumn<String, Vocable> createColumn(String title, String feature) {
        TableColumn<String, Vocable> newColumn = new TableColumn<>(title);
        newColumn.setCellValueFactory(new PropertyValueFactory<>(feature));
        return newColumn;
    }

    private void reloadTable() {
        dictTable.getItems().clear();
        for (Vocable v : cloneDictionary) {
            dictTable.getItems().add(v);
        }
    }
}