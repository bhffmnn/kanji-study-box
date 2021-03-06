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
     * Global variable for the vocableDictionary
     */
    static public VocableDictionary vocableDictionary;

    /**
     * Global variable for the application settings
     */
    static public Settings settings;


    /**
     * The init method tries to load the settings file into the global settings variable. If the file does not exist a
     * new settings object is created instead.
     * @throws Exception
     */
    @Override
    public void init() throws Exception {
        settings = new Settings();
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
                    "Load a kanji dictionary from an existing file or create new one?",
                    loadButton,
                    createButton,
                    quitButton);
            dictionaryDialogue.setTitle("Loading / Creating Kanji Dictionary");
            Optional<ButtonType> result = dictionaryDialogue.showAndWait();
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("KSB kanji dictionary files", "*.ksbk"));
            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("All files", "*"));
            if (result.get() == loadButton) {
                File file = fileChooser.showOpenDialog(stage);
                try {
                    kanjiDictionary = new KanjiDictionary(settings.getKanjiDictionaryFilePath());
                    settings.setKanjiDictionaryFilePath(file.getAbsolutePath());
                }
                catch (IOException eTwo) {
                    Alert failAlert = new Alert(Alert.AlertType.ERROR, "Couldn't load dictionary file.");
                    failAlert.show();
                    System.out.println(eTwo.getMessage());
                    System.exit(0);
                }
            }
            else if (result.get() == createButton) {
                fileChooser.setInitialFileName("kanji_dictionary.ksbk");
                File file = fileChooser.showSaveDialog(stage);
                try {
                    kanjiDictionary = new KanjiDictionary();
                    kanjiDictionary.save(file.getAbsolutePath());
                    settings.setKanjiDictionaryFilePath(file.getAbsolutePath());
                }
                catch (IOException eTwo) {
                    Alert failAlert = new Alert(Alert.AlertType.ERROR, "Couldn't save dictionary file.");
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
                    "Load from an existing vocabulary file or create a new one?",
                    loadButton,
                    createButton,
                    quitButton);
            dictionaryDialogue.setTitle("Loading / Creating Vocabulary Dictionary");
            Optional<ButtonType> result = dictionaryDialogue.showAndWait();
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("KSB vocabulary dictionary files", "*.ksbv"));
            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("All files", "*"));
            if (result.get() == loadButton) {
                File file = fileChooser.showOpenDialog(stage);
                try {
                    vocableDictionary = new VocableDictionary(settings.getKanjiDictionaryFilePath());
                    settings.setVocableDictionaryFilePath(file.getAbsolutePath());
                }
                catch (IOException eTwo) {
                    Alert failAlert = new Alert(Alert.AlertType.ERROR, "Couldn't load dictionary file.");
                    failAlert.show();
                    System.out.println(eTwo.getMessage());
                    System.exit(0);
                }
            }
            else if (result.get() == createButton) {
                fileChooser.setInitialFileName("vocable_dictionary.ksbv");
                File file = fileChooser.showSaveDialog(stage);
                try {
                    vocableDictionary = new VocableDictionary();
                    vocableDictionary.save(file.getAbsolutePath());
                    settings.setVocableDictionaryFilePath(file.getAbsolutePath());
                }
                catch (IOException eTwo) {
                    Alert failAlert = new Alert(Alert.AlertType.ERROR, "Couldn't save dictionary file.");
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
    public void stop() {
    }

    public static void main(String[] args) {
        launch();
    }
}