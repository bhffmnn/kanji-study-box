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

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class EditKanjiDictionaryController implements Initializable {

    @FXML
    TableView dictTable;

    private TableColumn<String, Kanji> charClmn;
    private TableColumn<String, Kanji> onClmn;
    private TableColumn<String, Kanji> kunClmn;
    private TableColumn<String, Kanji> meanClmn;
    private TableColumn<String, Kanji> mnemClmn;
    private TableColumn<String, Kanji> charLvlClmn;
    private TableColumn<String, Kanji> charDueClmn;
    private TableColumn<String, Kanji> readLvlClmn;
    private TableColumn<String, Kanji> readDueClmn;
    private TableColumn<String, Kanji> meanLvlClmn;
    private TableColumn<String, Kanji> meanDueClmn;

    //This exists so you can reset the dictionary
    private KanjiDictionary cloneDictionary;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cloneDictionary = App.kanjiDictionary.cloneDeep();

        charClmn = createColumn("character", "character");
        onClmn = createColumn("ON-Reading", "onReading");
        kunClmn = createColumn("kun-Reading", "kunReading");
        meanClmn = createColumn("Meaning", "meaning");
        mnemClmn = createColumn("Mnemonic", "mnemonic");

        charLvlClmn = createColumn("Character Level", "characterLevel");
        charDueClmn = createColumn("Character Due", "characterDue");
        readLvlClmn = createColumn("Reading Level", "readingLevel");
        readDueClmn = createColumn("Reading Due", "readingDue");
        meanLvlClmn = createColumn("Meaning Level", "meaningLevel");
        meanDueClmn = createColumn("Meaning Due", "meaningDue");

        dictTable.getColumns().add(charClmn);
        dictTable.getColumns().add(onClmn);
        dictTable.getColumns().add(kunClmn);
        dictTable.getColumns().add(meanClmn);
        dictTable.getColumns().add(mnemClmn);
        dictTable.getColumns().add(charLvlClmn);
        dictTable.getColumns().add(charDueClmn);
        dictTable.getColumns().add(readLvlClmn);
        dictTable.getColumns().add(readDueClmn);
        dictTable.getColumns().add(meanLvlClmn);
        dictTable.getColumns().add(meanDueClmn);

        for (Kanji k : cloneDictionary) {
            dictTable.getItems().add(k);
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
        FXMLLoader loader = new FXMLLoader(App.class.getResource("fxml/menu/addKanji.fxml"));

        AddKanjiController controller = new AddKanjiController(cloneDictionary);
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
            Kanji kanji = (Kanji) dictTable.getItems().get(position.getRow());
            //Kanji kanji = App.kanjiDictionary.getByIndex(0);
            FXMLLoader loader = new FXMLLoader(App.class.getResource("fxml/menu/editKanji.fxml"));

            EditKanjiController controller = new EditKanjiController(kanji);
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
            Kanji kanji = (Kanji) dictTable.getItems().get(position.getRow());
            cloneDictionary.remove(kanji);
            reloadTable();
        }

    }
    @FXML
    private void saveButtonAction(ActionEvent actionEvent) throws Exception {
        App.kanjiDictionary = cloneDictionary.cloneDeep();
        App.kanjiDictionary.save(App.settings.getKanjiDictionaryFilePath());
    }

    @FXML
    private void resetButtonAction(ActionEvent actionEvent) throws Exception {
        cloneDictionary = App.kanjiDictionary.cloneDeep();
        reloadTable();
    }

    private TableColumn<String, Kanji> createColumn(String title, String feature) {
        TableColumn<String, Kanji> newColumn = new TableColumn<>(title);
        newColumn.setCellValueFactory(new PropertyValueFactory<>(feature));
        return newColumn;
    }

    private void reloadTable() {
        dictTable.getItems().clear();
        for (Kanji k : cloneDictionary) {
            dictTable.getItems().add(k);
        }
    }
}