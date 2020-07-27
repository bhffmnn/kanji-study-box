/*
 * Copyright (c) 2020, Benjamin Hoffmann
 * All rights reserved.

 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree.
 */

package de.bhffmnn.controllers.menu;

import de.bhffmnn.controllers.menu.EditKanjiDictionaryController.KanjiMenuItemList;
import de.bhffmnn.models.Kanji;
import de.bhffmnn.models.KanjiDictionary;
import de.bhffmnn.models.Vocable;
import de.bhffmnn.models.VocableDictionary;
import de.bhffmnn.util.KandicReader;
import de.bhffmnn.util.KanjidicKanji;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Controller for the ImportVocabulary view that lets the user import vocabulary from either a kanji study box
 * vocabulary dictionary file or
 */

public class ImportVocabularyController implements Initializable {
    @FXML
    TextField pathField;
    @FXML
    RadioButton radioOverwriteExisting;

    private File dictFile;
    private VocableDictionary vocableDictionary;

    public ImportVocabularyController(VocableDictionary vocableDictionary) {
        this.vocableDictionary = vocableDictionary;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    @FXML
    public void chooseFileButtonAction() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("KSB vocabulary dictionary files", "*.ksbv"));
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("All files", "*"));
        dictFile = fileChooser.showOpenDialog(pathField.getScene().getWindow());
        if (dictFile != null) {
            pathField.setText(dictFile.getAbsolutePath());
        }
    }

    @FXML
    public void importButtonAction( ) {
        if (dictFile == null || !dictFile.isFile()) {
            Alert fileAlert = new Alert(Alert.AlertType.INFORMATION, "You have to choose a file first.");
            fileAlert.setHeaderText("");
            fileAlert.show();
            return;
        }
        try {
            VocableDictionary importDictionary = new VocableDictionary(dictFile.getAbsolutePath());
            if (radioOverwriteExisting.isSelected()) {
                for (Vocable v : importDictionary) {
                    if (!vocableDictionary.add(v)) {
                        Vocable removeMe = vocableDictionary.getByForm(v.getForm());
                        vocableDictionary.remove(removeMe);
                        vocableDictionary.add(v);
                    }
                }
            }
            else {
                vocableDictionary.addAll(importDictionary);
            }
        }
        catch (IOException e) {
            Alert fileAlert = new Alert(Alert.AlertType.ERROR, "Error while loading dictionary.");
            fileAlert.setHeaderText("");
            fileAlert.showAndWait();
        }
        ((Stage) radioOverwriteExisting.getScene().getWindow()).close();
    }
}