/*
 * Copyright (c) 2020, Benjamin Hoffmann
 * All rights reserved.

 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree.
 */

package de.bhffmnn.controllers.menu;

import de.bhffmnn.models.Kanji;
import de.bhffmnn.models.KanjiDictionary;
import de.bhffmnn.util.KandicReader;
import de.bhffmnn.util.KanjidicKanji;
import de.bhffmnn.controllers.menu.EditKanjiDictionaryController.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.ResourceBundle;

/**
 * Controller for the ImportKanji view that lets the user import kanji from either a kanji study box dictionary file or
 * a Kanjidic2 file.
 */

public class ImportKanjiController implements Initializable {
    @FXML
    ComboBox<String> fileTypeBox;
    @FXML
    ComboBox<SelectionPreset> presetBox;

    @FXML
    TextField kanjiSelectionField;
    @FXML
    TextField pathField;

    @FXML
    RadioButton radioAllKanji;
    @FXML
    RadioButton radioTheseKanji;
    @FXML
    RadioButton radioPresetKanji;
    @FXML
    RadioButton radioOverwriteExisting;

    private File dictFile;
    private KanjiMenuItemList kanjiMenuItemList;

    public ImportKanjiController(KanjiMenuItemList kanjiMenuItemList) {
        this.kanjiMenuItemList = kanjiMenuItemList;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //set up fileType box
        fileTypeBox.getItems().addAll("Kanji Study Box Dictionary", "KANJIDIC2");
        fileTypeBox.valueProperty().addListener((o, oldValue, newValue) -> {
            radioAllKanji.selectedProperty().set(true);
            switch (newValue) {
                case "Kanji Study Box Dictionary":
                    radioPresetKanji.setDisable(true);
                    presetBox.setDisable(true);
                    break;
                case "KANJIDIC2":
                    radioPresetKanji.setDisable(false);
                    presetBox.setDisable(false);
            }
        });
        fileTypeBox.getSelectionModel().select("Kanji Study Box Dictionary");

        for (SelectionPreset sp : SelectionPreset.values()) {
            presetBox.getItems().add(sp);
        }
        presetBox.getSelectionModel().select(0);
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
    public void importButtonAction() {
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
                if (radioTheseKanji.isSelected()) {
                    char[] specifiedChars = kanjiSelectionField.textProperty().get().toCharArray();
                    ArrayList<Kanji> removalList = new ArrayList<>();
                    for (char c : specifiedChars) {
                        for (Kanji k : importDictionary) {
                            if (k.getCharacter() != c) {
                                removalList.add(k);
                            }
                        }
                    }
                    importDictionary.removeAll(removalList);
                }
                for (Kanji k : importDictionary) {
                    if (!kanjiMenuItemList.addNewItem(k) && radioOverwriteExisting.isSelected()) {
                        kanjiMenuItemList.removeByKanji(k);
                        kanjiMenuItemList.addNewItem(k);
                    }
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
                ArrayList<KanjidicKanji> kanjiList = KandicReader.getAllKandicKanjiAsArrayList(dictFile.getAbsolutePath());

                //These Kanji Option
                if (radioTheseKanji.isSelected()) {
                    char[] specifiedChars = kanjiSelectionField.textProperty().get().toCharArray();
                    ArrayList<KanjidicKanji> removalList = new ArrayList<>();
                    for (KanjidicKanji kdk : kanjiList) {
                        boolean found = false;
                        for (char c : specifiedChars) {
                            if (kdk.getCharacter() == c) {
                                found = true;
                            }
                        }
                        if (!found) {
                            removalList.add(kdk);
                        }
                    }
                    kanjiList.removeAll(removalList);
                }

                //Preset Option
                else if (radioPresetKanji.isSelected()) {
                    ArrayList<KanjidicKanji> removalList = new ArrayList<>();

                    //Joyo Kanji Preset
                    if (presetBox.getValue().equals(SelectionPreset.JOYO)) {
                        for (KanjidicKanji kdk : kanjiList) {
                            boolean found = false;
                            for (char c : KandicReader.joyoKanji.toCharArray()) {
                                if (kdk.getCharacter() == c) {
                                    found = true;
                                }
                            }
                            if (!found) {
                                removalList.add(kdk);
                            }
                        }
                        kanjiList.removeAll(removalList);
                    }

                    //Old Joyo Kanji Preset
                    else if (presetBox.getValue().equals(SelectionPreset.JOYO_OLD)) {
                        for (KanjidicKanji kdk : kanjiList) {
                            boolean found = false;
                            for (char c : KandicReader.joyoKanjiOld.toCharArray()) {
                                if (kdk.getCharacter() == c) {
                                    found = true;
                                }
                            }
                            if (!found) {
                                removalList.add(kdk);
                            }
                        }
                        kanjiList.removeAll(removalList);
                    }

                    //JLPT Presets
                    else if (presetBox.getValue().toString().contains("JLPT")) {
                        int jlptLevel = presetBox.getValue().ordinal() - 1;
                        for (KanjidicKanji kdk : kanjiList) {
                            if (kdk.getJlpt() < jlptLevel) {
                                removalList.add(kdk);
                            }
                        }
                    }
                    kanjiList.removeAll(removalList);
                }

                //Put the imported kanji into the kanji dictionary
                for (KanjidicKanji kdk : kanjiList) {
                    Kanji k = kdk.toKanji();
                    if (!kanjiMenuItemList.addNewItem(k)) {
                        if (radioOverwriteExisting.isSelected()) {
                            kanjiMenuItemList.removeByKanji(k);
                            kanjiMenuItemList.addNewItem(k);
                        }
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