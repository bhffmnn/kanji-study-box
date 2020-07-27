/*
 * Copyright (c) 2020, Benjamin Hoffmann
 * All rights reserved.

 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree.
 */

package de.bhffmnn.controllers.menu;

import de.bhffmnn.App;
import de.bhffmnn.models.KanjiDictionary;
import de.bhffmnn.models.VocableDictionary;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Controller for the Settings view that lets the user change the settings of the application.
 */

public class SettingsController implements Initializable {
    @FXML
    private Label kanjiPathLabel;
    @FXML
    private Label vocPathLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        kanjiPathLabel.setText(App.settings.getKanjiDictionaryFilePath());
        vocPathLabel.setText(App.settings.getVocableDictionaryFilePath());
    }

    @FXML
    private void backButtonAction(ActionEvent actionEvent) throws Exception {
        Parent parent = FXMLLoader.load(App.class.getResource("fxml/menu/mainMenu.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void editKanjPathButtonAction(ActionEvent actionEvent) throws Exception {
        ButtonType loadButton = new ButtonType("Load");
        ButtonType createButton = new ButtonType("Create");
        ButtonType backupButton = new ButtonType("Backup");
        ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        Alert dictionaryDialogue = new Alert(Alert.AlertType.NONE,
                "Do you want to load a different dictionary file or create a new one or save the current one to a different location?",
                loadButton,
                createButton,
                backupButton,
                cancelButton);
        dictionaryDialogue.setTitle("Kanji Dictionary File Options");
        Optional<ButtonType> result = dictionaryDialogue.showAndWait();
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("KSB kanji dictionary files", "*.ksbk"));
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("All files", "*"));
        if (result.get() == loadButton) {
            File file = fileChooser.showOpenDialog(kanjiPathLabel.getScene().getWindow());
            try {
                KanjiDictionary newKanjiDictionary = new KanjiDictionary(file.getAbsolutePath());
                App.kanjiDictionary = newKanjiDictionary;
                App.settings.setKanjiDictionaryFilePath(file.getAbsolutePath());
                App.settings.save();
                kanjiPathLabel.setText(file.getAbsolutePath());
            }
            catch (IOException eTwo) {
                Alert failAlert = new Alert(Alert.AlertType.ERROR, "That didn't work.");
                failAlert.show();
                System.out.println(eTwo.getMessage());
            }
        }
        else if (result.get() == createButton) {
            fileChooser.setInitialFileName("kanji_dictionary.ksbk");
            File file = fileChooser.showSaveDialog(kanjiPathLabel.getScene().getWindow());
            try {
                KanjiDictionary newKanjiDictionary = new KanjiDictionary();
                newKanjiDictionary.save(file.getAbsolutePath());
                App.kanjiDictionary = newKanjiDictionary;
                App.settings.setKanjiDictionaryFilePath(file.getAbsolutePath());
                App.settings.save();
                kanjiPathLabel.setText(file.getAbsolutePath());
            }
            catch (IOException eTwo) {
                Alert failAlert = new Alert(Alert.AlertType.ERROR, "That didn't work.");
                failAlert.show();
                System.out.println(eTwo.getMessage());
            }
        }
        else if (result.get() == backupButton) {
            fileChooser.setInitialFileName("kanji_dictionary.ksbk");
            File file = fileChooser.showSaveDialog(kanjiPathLabel.getScene().getWindow());
            try {
                App.kanjiDictionary.save(file.getAbsolutePath());
                App.settings.setKanjiDictionaryFilePath(file.getAbsolutePath());
                App.settings.save();
                kanjiPathLabel.setText(file.getAbsolutePath());
            }
            catch (IOException eTwo) {
                Alert failAlert = new Alert(Alert.AlertType.ERROR, "That didn't work.");
                failAlert.show();
                System.out.println(eTwo.getMessage());
            }
        }
    }

    @FXML
    private void editVocPathButtonAction(ActionEvent actionEvent) throws Exception {
        ButtonType loadButton = new ButtonType("Load");
        ButtonType createButton = new ButtonType("Create");
        ButtonType backupButton = new ButtonType("Backup");
        ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        Alert dictionaryDialogue = new Alert(Alert.AlertType.NONE,
                "Do you want to load a different dictionary file or create a new one or save the current one to a different location?",
                loadButton,
                createButton,
                backupButton,
                cancelButton);
        dictionaryDialogue.setTitle("Vocable Dictionary File Path Options");
        Optional<ButtonType> result = dictionaryDialogue.showAndWait();
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("KSB vocabulary dictionary files", "*.ksbv"));
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("All files", "*"));
        if (result.get() == loadButton) {
            File file = fileChooser.showOpenDialog(vocPathLabel.getScene().getWindow());
            try {
                VocableDictionary newVocableDictionary = new VocableDictionary(file.getAbsolutePath());
                App.vocableDictionary = newVocableDictionary;
                App.settings.setVocableDictionaryFilePath(file.getAbsolutePath());
                App.settings.save();
                vocPathLabel.setText(file.getAbsolutePath());
            }
            catch (IOException eTwo) {
                Alert failAlert = new Alert(Alert.AlertType.ERROR, "That didn't work.");
                failAlert.show();
                System.out.println(eTwo.getMessage());
            }
        }
        else if (result.get() == createButton) {
            fileChooser.setInitialFileName("kanji_dictionary.ksbv");
            File file = fileChooser.showSaveDialog(vocPathLabel.getScene().getWindow());
            try {
                VocableDictionary newVocableDictionary = new VocableDictionary();
                newVocableDictionary.save(file.getAbsolutePath());
                App.settings.setVocableDictionaryFilePath(file.getAbsolutePath());
                App.settings.save();
                vocPathLabel.setText(file.getAbsolutePath());
            }
            catch (IOException eTwo) {
                Alert failAlert = new Alert(Alert.AlertType.ERROR, "That didn't work.");
                failAlert.show();
                System.out.println(eTwo.getMessage());
            }
        }
        else if (result.get() == backupButton) {
            fileChooser.setInitialFileName("voc_dictionary.ksbv");
            File file = fileChooser.showSaveDialog(vocPathLabel.getScene().getWindow());
            try {
                App.vocableDictionary.save(file.getAbsolutePath());
                App.settings.setVocableDictionaryFilePath(file.getAbsolutePath());
                App.settings.save();
                vocPathLabel.setText(file.getAbsolutePath());
            }
            catch (IOException eTwo) {
                Alert failAlert = new Alert(Alert.AlertType.ERROR, "That didn't work.");
                failAlert.show();
                System.out.println(eTwo.getMessage());
            }
        }
    }
}