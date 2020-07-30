/*
 * Copyright (c) 2020, Benjamin Hoffmann
 * All rights reserved.

 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree.
 */

package de.bhffmnn.controllers.menu;

import de.bhffmnn.util.DictionaryType;
import de.bhffmnn.util.SortByDictNumber;
import de.bhffmnn.controllers.menu.EditKanjiDictionaryController.KanjiMenuItemList;
import de.bhffmnn.models.Kanji;
import de.bhffmnn.models.KanjiDictionary;
import de.bhffmnn.util.KandicReader;
import de.bhffmnn.util.KanjidicKanji;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Controller for the ImportKanji view that lets the user import kanji from either a kanji study box dictionary file or
 * a Kanjidic2 file.
 */

public class ChangeKanjiOrderController implements Initializable {
    @FXML
    ComboBox<String> fileTypeBox;
    @FXML
    ComboBox<DictionaryType> presetBox;

    @FXML
    TextField orderField;
    @FXML
    TextField pathField;

    @FXML
    RadioButton radioManualOrder;
    @FXML
    RadioButton radioPresetOrder;
    @FXML
    VBox fileVBox;
    @FXML
    HBox fileChooseBox;

    private File dictFile;
    private KanjiMenuItemList kanjiMenuItemList;

    public ChangeKanjiOrderController(KanjiMenuItemList kanjiMenuItemList) {
        this.kanjiMenuItemList = kanjiMenuItemList;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //set up fileType box
        fileTypeBox.getItems().addAll("Kanji Study Box Dictionary", "KANJIDIC2");
        fileTypeBox.valueProperty().addListener((o, oldValue, newValue) -> {
            switch (newValue) {
                case "Kanji Study Box Dictionary":
                    presetBox.setDisable(true);
                    break;
                case "KANJIDIC2":
                    presetBox.setDisable(false);
            }
        });
        fileTypeBox.getSelectionModel().select("Kanji Study Box Dictionary");

        for (DictionaryType dt : DictionaryType.values()) {
            presetBox.getItems().add(dt);
        }
        presetBox.getSelectionModel().select(0);

        //set up radio buttons
        radioManualOrder.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                fileVBox.setDisable(true);
                fileChooseBox.setDisable(true);
            }
            else {
                fileVBox.setDisable(false);
                fileChooseBox.setDisable(false);
            }
        });
        radioManualOrder.setSelected(true);
    }

    @FXML
    public void chooseFileButtonAction() {
        FileChooser fileChooser = new FileChooser();
        if (fileTypeBox.getValue().equals("KANJIDIC2")) {
            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("KANJIDIC2 file", "*.xml"));
        }
        else {
            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("KSB kanji dictionary files", "*.ksbk"));
        }
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("All files", "*"));
        dictFile = fileChooser.showOpenDialog(fileTypeBox.getScene().getWindow());
        if (dictFile != null) {
            pathField.setText(dictFile.getAbsolutePath());
        }
    }

    @FXML
    public void applyButtonAction() {
        if (radioManualOrder.isSelected()) {
            char[] sequence = orderField.getText().toCharArray();
            int index = 0;
            for (char c : sequence) {
                kanjiMenuItemList.changeIndexOf(kanjiMenuItemList.getByCharacter(c), index);
                index++;
            }
            ((Stage) fileTypeBox.getScene().getWindow()).close();
            return;
        }
        if (dictFile == null || !dictFile.isFile()) {
            Alert fileAlert = new Alert(Alert.AlertType.INFORMATION, "You have to choose a file first.");
            fileAlert.setHeaderText("");
            fileAlert.show();
            return;
        }
        //Kanji Study Box Dictionary import
        if (fileTypeBox.getValue().equals("Kanji Study Box Dictionary")) {
            try {
                KanjiDictionary importDictionary = new KanjiDictionary(dictFile.getAbsolutePath());
                int index = 0;
                for (Kanji k : importDictionary) {
                    kanjiMenuItemList.changeIndexOf(kanjiMenuItemList.getByCharacter(k.getCharacter()), index);
                }
                ((Stage) fileTypeBox.getScene().getWindow()).close();
            }
            catch (IOException eTwo) {
                Alert fileAlert = new Alert(Alert.AlertType.ERROR, "Error while loading dictionary file.");
                fileAlert.show();
                System.out.println(eTwo.getMessage());
            }
        }
        //KANJIDIC2 import
        else {
            try {
                DictionaryType dictType = presetBox.getValue();
                ArrayList<KanjidicKanji> kanjiList = KandicReader.getAllKandicKanjiAsArrayList(dictFile.getAbsolutePath());
                ArrayList<KanjidicKanji> removeList = new ArrayList<>();
                for (KanjidicKanji kdk : kanjiList) {
                    if (!kdk.hasDictionaryNumber(dictType)) {
                        removeList.add(kdk);
                    }
                }
                kanjiList.removeAll(removeList);
                kanjiList.sort(new SortByDictNumber(dictType));

                int index = 0;
                for (KanjidicKanji kdk : kanjiList) {
                    try {
                        kanjiMenuItemList.changeIndexOf(kanjiMenuItemList.getByCharacter(kdk.getCharacter()), index);
                        index++;
                    }
                    catch (IllegalArgumentException iae) {

                    }
                }
                ((Stage) fileTypeBox.getScene().getWindow()).close();
            }
            catch (XMLStreamException xmle) {
                Alert xmlAlert = new Alert(Alert.AlertType.ERROR, "Error while loading xml file.");
                xmlAlert.show();
            }
        }
    }
    private enum SelectionPreset {
        JOYO {
            @Override
            public String toString() {
                return "Joyo kanji";
            }
        },
        JOYO_OLD {
            @Override
            public String toString() {
                return "Joyo kanji (before 2010)";
            }
        },
        JLPT_1 {
            @Override
            public String toString() {
                return "JLPT N1";
            }
        },
        JLPT_2 {
            @Override
            public String toString() {
                return "JLPT N2";
            }
        },
        JLPT_3 {
            @Override
            public String toString() {
                return "JLPT N3";
            }
        },
        JLPT_4 {
            @Override
            public String toString() {
                return "JLPT N4";
            }
        },
        JLPT_5 {
            @Override
            public String toString() {
                return "JLPT N5";
            }
        }
    }
}