/*
 * Copyright (c) 2020, Benjamin Hoffmann
 * All rights reserved.

 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree.
 */

package de.bhffmnn.controllers.training;

import de.bhffmnn.App;
import de.bhffmnn.models.Kanji;
import de.bhffmnn.models.KanjiDictionary;
import de.bhffmnn.models.Vocable;
import de.bhffmnn.models.VocableDictionary;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;

public class LearningKanjiController implements Initializable {
    /*
    The following variable tracks the learning phase.
    Phase 0: All features are visible and the showAndCheckButton is disabled.
    Phase 1: Checking forms
    Phase 2: Checking readings
     */
    private IntegerProperty phase;

    //Index for iterating over kanjiStudyList
    private IntegerProperty currentIndex;

    //Tracks whether answers are hidden
    private BooleanProperty answersHidden;

    //Dialog for saving and exiting
    private ChoiceDialog<String> endDialog;

    //KanjiDictionary tracking updated kanji
    private KanjiDictionary uncheckedKanji;
    private KanjiDictionary studyKanji;

    //Character feature labels
    @FXML
    private Label character;
    @FXML
    private Label on;
    @FXML
    private Label kun;
    @FXML
    private Label meaning;
    @FXML
    private Label vocab;
    @FXML
    private Label mnemonic;

    //Progress bar
    @FXML
    private ProgressBar progressBar;
    @FXML
    private Label progressLabel;

    //Buttons
    @FXML
    private Button showAndCheckButton;
    @FXML
    private Button previousButton;
    @FXML
    private Button nextButton;

    //Check mark for updated Kanji
    @FXML
    private Circle checkCircle;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        studyKanji = App.kanjiStudyList.clone();
        uncheckedKanji = App.kanjiStudyList.clone();

        loadFeatures(0);

        //Initialize progress bar
        progressLabel.setText("1/" + App.kanjiStudyList.size());
        progressBar.progressProperty().set(1 / (App.kanjiStudyList.size()));

        //Set up currentIndex property
        currentIndex = new SimpleIntegerProperty(0);
        currentIndex.addListener(
                ((observableValue, oldValue, newValue) -> {
                    //update labels
                    loadFeatures(newValue.intValue());

                    //update progress bar
                    progressBar.progressProperty().set((newValue.doubleValue() + 1) / (studyKanji.size()));
                    progressLabel.setText((newValue.intValue() + 1) + "/" + studyKanji.size());

                    if (phase.get() > 0) {
                        //Update check circle
                        if (uncheckedKanji.contains(studyKanji.getByIndex(currentIndex.get()))) {
                            checkCircle.setFill(null);
                        }
                        else {
                            checkCircle.setFill(Color.GREEN);
                        }
                        answersHidden.set(true);
                    }
                })
        );

        //Set up phase property
        phase = new SimpleIntegerProperty(0);
        phase.addListener(((observableValue, oldValue, newValue) -> {
            if (newValue.intValue() > 0) {
                showAndCheckButton.setDisable(false);
            }
            studyKanji = App.kanjiStudyList.clone();
            uncheckedKanji = studyKanji.clone();
            currentIndex.set(0);
        }));

        //Set up answerHidden property
        answersHidden = new SimpleBooleanProperty();
        answersHidden.addListener(((observableValue, oldValue, newValue) -> {
            if (newValue) {  //if answers hidden
                showAndCheckButton.setText("Show");
                //hide features based on phase
                for (String feature : App.settings.getStudyDirectionsKanji()[phase.get() - 1]) {
                    hideFeature(feature);
                }
            }
            else {
                showAndCheckButton.setText("Check!");
                //show features based on phase
                for (String feature : App.settings.getStudyDirectionsKanji()[phase.get() - 1]) {
                    showFeature(feature);
                }
            }
        }));

        //Set it all up
        showAndCheckButton.setDisable(true);
        loadFeatures(0);
        progressBar.progressProperty().set(1.0 / (studyKanji.size()));
        progressLabel.setText("1/" + studyKanji.size());
    }

    //Button actions
    @FXML
    private void nextButtonAction() throws IOException {
        if (currentIndex.get() != studyKanji.size() - 1) {
            currentIndex.set(currentIndex.getValue() + 1);
        }
        else if (phase.get() == 0 || (phase.get() == 1 && uncheckedKanji.size() == 0)) {
            phase.set(phase.get() + 1);
            progressBar.progressProperty().set((1.0 / (studyKanji.size())));
            progressLabel.setText(("1/" + studyKanji.size()));
            checkCircle.setFill(null);
            answersHidden.set(true);
            loadFeatures(0);
        }
        else if (uncheckedKanji.size() > 0) {
            studyKanji = uncheckedKanji.clone();
            currentIndex.set(0);
        }
        else {
            showEndDialog();
        }
    }
    @FXML
    private void previousButtonAction() {
        if (currentIndex.get() != 0) {
            currentIndex.set(currentIndex.getValue() - 1);
        }
    }
    @FXML
    private void showAndCheckButtonAction() {
        if (answersHidden.get()) {
            answersHidden.set(false);
        }
        else {
            uncheckedKanji.remove(studyKanji.getByIndex(currentIndex.get()));
            checkCircle.setFill(Color.GREEN);
        }
    }

    @FXML
    private void xButtonAction() throws IOException {
        showEndDialog();
    }

    //Keyboard configuration
    @FXML
    private void handleKeyPress(KeyEvent keyEvent) {
        if (keyEvent.getCode().equals(KeyCode.A)) {
            previousButton.fire();
        }
        else if (keyEvent.getCode().equals(KeyCode.D)) {
            nextButton.fire();
        }
    }

    private void showEndDialog() throws IOException {
        //At the end of the dictionary / when exiting with xButton
        String[] choices = {"Save and finish", "Save and finish", "Finish without saving"};
        endDialog = new ChoiceDialog<>(choices[0], choices);
        endDialog.setTitle("End of list");
        endDialog.setHeaderText("How do you want to proceed?");
        Optional<String> choice = endDialog.showAndWait();
        if (choice.isPresent()) {
            if (choice.get().equals("Save and finish")) {
                for (Kanji kanji : App.kanjiStudyList) {
                    kanji.setCharacterLevel(1);
                    kanji.setCharacterDue(LocalDate.now());
                    kanji.setReadingLevel(1);
                    kanji.setReadingDue(LocalDate.now());
                    kanji.setMeaningLevel(1);
                    kanji.setMeaningDue(LocalDate.now());
                }
                App.vocableDictionary.save(App.settings.getVocableDictionaryFilePath());
                Parent trainingParent = FXMLLoader.load(App.class.getResource("fxml/menu/mainMenu.fxml"));
                Scene trainingScene = new Scene(trainingParent);
                Stage stage = (Stage) character.getScene().getWindow();
                stage.setScene(trainingScene);
                stage.show();
            } else if (choice.get().equals("Finish without saving")) {
                Parent trainingParent = FXMLLoader.load(App.class.getResource("fxml/menu/mainMenu.fxml"));
                Scene trainingScene = new Scene(trainingParent);
                Stage stage = (Stage) character.getScene().getWindow();
                stage.setScene(trainingScene);
                stage.show();
            }
        }
    }

    private void loadFeatures(int index) {
        character.setText(App.kanjiStudyList.getByIndex(index).getCharacter());
        on.setText(App.kanjiStudyList.getByIndex(index).getOnReading());
        kun.setText(App.kanjiStudyList.getByIndex(index).getKunReading());
        meaning.setText(App.kanjiStudyList.getByIndex(index).getMeaning());

        VocableDictionary kanjiVocables = App.vocableDictionary.getByCharacter(App.kanjiStudyList.getByIndex(index).getCharacter());
        StringBuilder vocables = new StringBuilder();
        for (Vocable v : kanjiVocables) {
            vocables.append(v.getReading() + " - " + v.getMeaning() + "\n");
        }

        vocab.setText(vocables.toString());
        mnemonic.setText(App.kanjiStudyList.getByIndex(index).getMnemonic());
    }

    private void showFeature(String feature) {
        switch (feature) {
            case "character":
                character.setVisible(true);
                break;
            case "on":
                on.setVisible(true);
                break;
            case "kun":
                kun.setVisible(true);
                break;
            case "meaning":
                meaning.setVisible(true);
                break;
            case "vocab":
                vocab.setVisible(true);
                break;
            case "mnemonic":
                mnemonic.setVisible(true);
                break;
        }
    }

    private void hideFeature(String feature) {
        switch (feature) {
            case "character":
                character.setVisible(false);
                break;
            case "on":
                on.setVisible(false);
                break;
            case "kun":
                kun.setVisible(false);
                break;
            case "meaning":
                meaning.setVisible(false);
                break;
            case "vocab":
                vocab.setVisible(false);
                break;
            case "mnemonic":
                mnemonic.setVisible(false);
                break;
        }
    }

    private void chooseNewVocab() throws IOException {
        ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.YES);
        ButtonType no = new ButtonType("No", ButtonBar.ButtonData.NO);
        Alert alert = new Alert(Alert.AlertType.WARNING,
                "Wanna start some fancy new vocabulary for these kanji?",
                yes,
                no);
        alert.setTitle("New vocabulary?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get().getButtonData().equals(ButtonBar.ButtonData.YES)) {
            Parent trainingParent = FXMLLoader.load(App.class.getResource("fxml/selectors/choosingVocabByKanji.fxml"));
            Scene trainingScene = new Scene(trainingParent);
            Stage stage = (Stage) character.getScene().getWindow();
            stage.setScene(trainingScene);
            stage.show();
        }
    }
}