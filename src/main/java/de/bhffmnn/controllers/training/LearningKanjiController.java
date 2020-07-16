/*
 * Copyright (c) 2020, Benjamin Hoffmann
 * All rights reserved.

 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree.
 */

package de.bhffmnn.controllers.training;

import de.bhffmnn.App;
import de.bhffmnn.controllers.misc.KanjiTooltipBuilder;
import de.bhffmnn.controllers.misc.VocableTooltipBuilder;
import de.bhffmnn.models.*;
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
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Controller for the LearningKanji view which lets the user learn new kanji.
 */

public class LearningKanjiController implements Initializable {
    //Kanji that are to be studied
    private KanjiDictionary kanjiStudyList;

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

    //KanjiDictionary tracking updated kanji
    private KanjiDictionary uncheckedKanji; //At the start of a phase all kanji are stored here
    private KanjiDictionary studyKanji; //Gets set to uncheckedKanji.clone() after reaching the end and reset to
                                        //App.kanjiStudyList.clone() at the start of a new phase

    //Stores vocables of current kanji for vocable tooltip
    ArrayList<Vocable> currentVocables;

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
    private Label mnemonic;
    @FXML
    private VBox vocabBox;

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

    /**
     *
     * @param kanjiStudyList The kanji that are to be learned.
     */
    public LearningKanjiController(KanjiDictionary kanjiStudyList) {
        this.kanjiStudyList = kanjiStudyList;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        studyKanji = kanjiStudyList.clone();
        uncheckedKanji = kanjiStudyList.clone();

        loadFeatures(0);

        //Initialize progress bar
        progressLabel.setText("1/" + kanjiStudyList.size());
        progressBar.progressProperty().set(1 / (kanjiStudyList.size()));

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
            studyKanji = kanjiStudyList.clone();
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

        //The buttons for the Alerts
        ButtonType yesButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
        ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.NO);
        ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        Alert endDialog = new Alert(Alert.AlertType.CONFIRMATION);
        endDialog.setTitle("Closing learning session");
        endDialog.setContentText("Do you want to save and schedule these kanji for studying?");
        endDialog.setHeaderText("");
        endDialog.getButtonTypes().setAll(yesButton, noButton, cancelButton);

        Optional<ButtonType> result = endDialog.showAndWait();
        if (result.isPresent()) {
            if (result.get().getButtonData().equals(ButtonBar.ButtonData.YES)) {
                for (Kanji kanji : kanjiStudyList) {
                    kanji.setCharacterLevel(1);
                    kanji.setCharacterDue(LocalDate.now());
                    kanji.setReadingLevel(1);
                    kanji.setReadingDue(LocalDate.now());
                    kanji.setMeaningLevel(1);
                    kanji.setMeaningDue(LocalDate.now());
                }
                App.kanjiDictionary.save(App.settings.getKanjiDictionaryFilePath());
                Parent trainingParent = FXMLLoader.load(App.class.getResource("fxml/menu/mainMenu.fxml"));
                Scene trainingScene = new Scene(trainingParent);
                Stage stage = (Stage) character.getScene().getWindow();
                stage.setScene(trainingScene);
                stage.show();
            }
            else if (result.get().getButtonData().equals(ButtonBar.ButtonData.NO)) {
                Parent trainingParent = FXMLLoader.load(App.class.getResource("fxml/menu/mainMenu.fxml"));
                Scene trainingScene = new Scene(trainingParent);
                Stage stage = (Stage) character.getScene().getWindow();
                stage.setScene(trainingScene);
                stage.show();
            }
        }
    }

    private void loadFeatures(int index) {
        Kanji currentKanji = studyKanji.getByIndex(index);

        character.setText(currentKanji.getCharacter());
        character.setTooltip(KanjiTooltipBuilder.kanjiTooltip(currentKanji));

        on.setText(currentKanji.getOnReading());
        kun.setText(currentKanji.getKunReading());
        meaning.setText(currentKanji.getMeaning());

        vocabBox.getChildren().clear();
        currentVocables = new ArrayList<>();
        currentVocables.addAll(App.vocableDictionary.getByCharacter(currentKanji.getCharacter()));
        for (Vocable v : currentVocables) {
            Label vocableLabel = new Label(v.getReading() + " - " + v.getMeaning());
            vocableLabel.setStyle("-fx-font-size:16");
            vocableLabel.setTooltip(VocableTooltipBuilder.vocableTooltip(v));
            vocabBox.getChildren().add(vocableLabel);
        }

        mnemonic.setText(currentKanji.getMnemonic());
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
                vocabBox.setVisible(true);
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
                vocabBox.setVisible(false);
                break;
            case "mnemonic":
                mnemonic.setVisible(false);
                break;
        }
    }
}