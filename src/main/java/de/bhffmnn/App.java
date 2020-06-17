/*
 * Copyright (c) 2020, Benjamin Hoffmann
 * All rights reserved.

 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree.
 */

package de.bhffmnn;

import de.bhffmnn.models.KanjiDictionary;
import de.bhffmnn.models.VocableDictionary;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

/**
 * JavaFX App
 */
public class App extends Application {
    /**
     * Global variable for the kanjiDictionary
     */
    static public KanjiDictionary kanjiDictionary;
    /**
     * Global variable for the kanji that are meant to be studied either in the training or learning views
     */
    static public KanjiDictionary kanjiStudyList;
    /**
     * Global variable for the vocableDictionary
     */
    static public VocableDictionary vocableDictionary;
    /**
     * Global variable for the vocables that are meant to be studied either in the training or learning views
     */
    static public VocableDictionary vocableStudyList;
    /**
     * Global variable for settings
     */
    static public Settings settings;
    /**
     * Global variable the studyDirection of the training or learning views
     */
    public static int studyDirection;

    /**
     * The init method tries to load the settings file into the global settings variable. If the file does not exist a
     * new settings object is created instead.
     * @throws Exception
     */
    @Override
    public void init() throws Exception {
        settings = Settings.loadSettings();
        if (settings == null) {
            settings = new Settings();
        }
    }

    /**
     * The start method tries to open the dictionaries from their paths specified in the settings. If this is not
     * successful, the user is asked to either load existing or create new dictionaries for vocables and kanji.
     * In the end the MainMenu view is loaded.
     * @param stage
     * @throws IOException
     */
    @Override
    public void start(Stage stage) throws IOException {
        try {
            kanjiDictionary = new KanjiDictionary(settings.getKanjiDictionaryFilePath());
        }
        catch (IOException e) {
            ButtonType loadButton = new ButtonType("Load");
            ButtonType createButton = new ButtonType("Create");
            ButtonType quitButton = new ButtonType("Quit", ButtonBar.ButtonData.CANCEL_CLOSE);
            Alert dictionaryDialogue = new Alert(Alert.AlertType.NONE,
                    "Kanji dictionary could not be loaded. Load from existing file or create new one?",
                    loadButton,
                    createButton,
                    quitButton);
            dictionaryDialogue.setTitle("Stop right there, criminal scum!");
            Optional<ButtonType> result = dictionaryDialogue.showAndWait();
            if (result.get() == loadButton) {
                FileChooser fileChooser = new FileChooser();
                File file = fileChooser.showOpenDialog(stage);
                try {
                    settings.setKanjiDictionaryFilePath(file.getAbsolutePath());
                    settings.save();
                    kanjiDictionary = new KanjiDictionary(settings.getKanjiDictionaryFilePath());
                }
                catch (IOException eTwo) {
                    Alert failAlert = new Alert(Alert.AlertType.ERROR, "That didn't work.");
                    failAlert.show();
                    System.out.println(eTwo.getMessage());
                    System.exit(0);
                }
            }
            else if (result.get() == createButton) {
                //TODO
                FileChooser fileChooser = new FileChooser();
                fileChooser.setInitialFileName("kanji_dictionary.csv");
                fileChooser.getExtensionFilters().add(
                        new FileChooser.ExtensionFilter("CSV files", "*.csv"));
                File file = fileChooser.showSaveDialog(stage);
                try {
                    kanjiDictionary = new KanjiDictionary();
                    kanjiDictionary.save(file.getAbsolutePath());
                    settings.setKanjiDictionaryFilePath(file.getAbsolutePath());
                    settings.save();
                }
                catch (IOException eTwo) {
                    Alert failAlert = new Alert(Alert.AlertType.ERROR, "That didn't work.");
                    failAlert.show();
                    System.out.println(eTwo.getMessage());
                    System.exit(0);
                }
            }
            else {
                System.exit(0);
            }
        }
        try {
            vocableDictionary = new VocableDictionary(settings.getVocableDictionaryFilePath());
        }
        catch (IOException e) {
            ButtonType loadButton = new ButtonType("Load");
            ButtonType createButton = new ButtonType("Create");
            ButtonType quitButton = new ButtonType("Quit", ButtonBar.ButtonData.CANCEL_CLOSE);
            Alert dictionaryDialogue = new Alert(Alert.AlertType.NONE,
                    "Vocabulary dictionary could not be loaded. Load from existing file or create new one?",
                    loadButton,
                    createButton,
                    quitButton);
            dictionaryDialogue.setTitle("Stop right there, criminal scum!");
            Optional<ButtonType> result = dictionaryDialogue.showAndWait();
            if (result.get() == loadButton) {
                FileChooser fileChooser = new FileChooser();
                File file = fileChooser.showOpenDialog(stage);
                try {
                    settings.setVocableDictionaryFilePath(file.getAbsolutePath());
                    settings.save();
                    kanjiDictionary = new KanjiDictionary(settings.getKanjiDictionaryFilePath());
                }
                catch (IOException eTwo) {
                    Alert failAlert = new Alert(Alert.AlertType.ERROR, "That didn't work.");
                    failAlert.show();
                    System.out.println(eTwo.getMessage());
                    System.exit(0);
                }
            }
            else if (result.get() == createButton) {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setInitialFileName("vocable_dictionary.csv");
                fileChooser.getExtensionFilters().add(
                        new FileChooser.ExtensionFilter("CSV files", "*.csv"));
                File file = fileChooser.showSaveDialog(stage);
                try {
                    vocableDictionary = new VocableDictionary();
                    vocableDictionary.save(file.getAbsolutePath());
                    settings.setVocableDictionaryFilePath(file.getAbsolutePath());
                    settings.save();
                }
                catch (IOException eTwo) {
                    Alert failAlert = new Alert(Alert.AlertType.ERROR, "That didn't work.");
                    failAlert.show();
                    System.out.println(eTwo.getMessage());
                    System.exit(0);
                }
            }
            else {
                System.exit(0);
            }
        }
        Parent root = FXMLLoader.load(getClass().getResource("fxml/menu/mainMenu.fxml"));
        stage.setScene(new Scene(root));
        stage.setTitle("Kanji Study Box");
        stage.getIcons().add(new Image(getClass().getResourceAsStream("images/kanjibox.png")));
        stage.show();
    }

    @Override
    public void stop() throws IOException {
        /*
        if (kanjiStudyList != null) {
            ButtonType yesSave = new ButtonType("Save", ButtonBar.ButtonData.YES);
            ButtonType noQuit = new ButtonType("Quit", ButtonBar.ButtonData.NO);
            Alert alert = new Alert(Alert.AlertType.WARNING,
                    "There might be unsaved changes to your kanji dictionary. Do you want to save?",
                    yesSave,
                    noQuit);
            alert.setTitle("Stop right there!");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get().getButtonData().equals(ButtonBar.ButtonData.YES)) {
                kanjiDictionary.save(settings.getKanjiDictionaryFilePath());
            }
        }
        else if (vocableStudyList != null) {
            ButtonType yesSave = new ButtonType("Save", ButtonBar.ButtonData.YES);
            ButtonType noQuit = new ButtonType("Quit", ButtonBar.ButtonData.NO);
            Alert alert = new Alert(Alert.AlertType.WARNING,
                    "There might be unsaved changes to your vocable dictionary. Do you want to save?",
                    yesSave,
                    noQuit);
            alert.setTitle("Stop right there!");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get().getButtonData().equals(ButtonBar.ButtonData.YES)) {
                vocableDictionary.save(settings.getVocableDictionaryFilePath());
            }
        }
        
         */
    }

    public static void main(String[] args) {
        launch();
    }
}