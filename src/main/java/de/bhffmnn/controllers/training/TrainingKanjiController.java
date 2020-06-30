/*
 * Copyright (c) 2020, Benjamin Hoffmann
 * All rights reserved.

 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree.
 */

package de.bhffmnn.controllers.training;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

import de.bhffmnn.App;
import de.bhffmnn.models.*;
import javafx.beans.property.*;
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

/**
 * Controller for the TrainingKanji view which lets the user study kanji. These kanji are determined by the global
 * variable App.kanjiStudyList
 */

public class TrainingKanjiController implements Initializable {
    //Kanji which are to be studied
    private KanjiDictionary kanjiStudyList;

    private int studyDirection;

    //Index for iterating over kanjiStudyList
    private IntegerProperty currentIndex;

    //Tracks whether answers are hidden
    private BooleanProperty answersHidden;

    //Dialog for saving and exiting
    private ChoiceDialog<String> endDialog;

    //KanjiDictionary tracking updated kanji
    KanjiDictionary notUpdatedKanji;

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
    private Label level;
    @FXML
    private Label due;
    @FXML
    private VBox vocabBox;

    //Progress bar
    @FXML
    private ProgressBar progressBar;
    @FXML
    private Label progressLabel;

    //Buttons
    @FXML
    private Button showHideButton;
    @FXML
    private Button setButton;
    @FXML
    private Button plusButton;
    @FXML
    private Button minusButton;
    @FXML
    private Button previousButton;
    @FXML
    private Button nextButton;

    //Text field for new level
    @FXML
    private TextField levelField;

    //Check mark for updated Kanji
    @FXML
    private Circle checkCircle;

    /**
     *
     * @param kanjiStudyList The kanji that are to be studied
     * @param studyDirection The study direction (0 = characters, 1 = readings, 2 = meanings)
     */
    public TrainingKanjiController(KanjiDictionary kanjiStudyList, int studyDirection) {
        this.kanjiStudyList = kanjiStudyList;
        this.studyDirection = studyDirection;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        notUpdatedKanji = kanjiStudyList.clone();

        //Shuffle for better effect :)
        kanjiStudyList.shuffle();

        //Initialize progress bar
        progressLabel.setText("1/" + kanjiStudyList.size());
        progressBar.progressProperty().set(1 / (kanjiStudyList.size()));

        //Set up currentIndex property which will do all the things when switching between kanji
        currentIndex = new SimpleIntegerProperty(0);
        currentIndex.addListener(
                ((observableValue, oldValue, newValue) -> {
                    //hide answers
                    if (!answersHidden.get()) {
                        showHideButton.fire();
                    }
                    //update labels
                    loadFeatures(newValue.intValue());

                    //update progress bar
                    progressBar.progressProperty().set((newValue.doubleValue() + 1) / (kanjiStudyList.size()));
                    progressLabel.setText((newValue.intValue() + 1) + "/" + kanjiStudyList.size());

                    //Update check circle
                    if (notUpdatedKanji.contains(kanjiStudyList.getByIndex(currentIndex.get()))) {
                        checkCircle.setFill(null);
                    }
                    else {
                        checkCircle.setFill(Color.GREEN);
                    }
                    System.out.println("currentIndex listener fired");
                })
        );

        //Set up answerHidden property
        answersHidden = new SimpleBooleanProperty();
        answersHidden.addListener(((observableValue, oldValue, newValue) -> {
            if (newValue.booleanValue()) {  //if answers hidden
                showHideButton.setText("Show");
                hideAnswers();
                disableUpdateButtons();
            }
            else {
                showHideButton.setText("Hide");
                showAnswers();
                enableUpdateButtons();
            }
        }));

        //Initialize property values
        answersHidden.setValue(true);
        currentIndex.setValue(0);

        //Property listeners won't fire yet so we initially do stuff manually here
        loadFeatures(0);
        disableUpdateButtons();
    }

    //Button actions
    @FXML
    private void nextButtonAction() throws IOException {
        if (currentIndex.get() != kanjiStudyList.size() - 1) {
            currentIndex.set(currentIndex.getValue() + 1);
        }
        //If at end of kanjiStudyList
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
    private void showHideButtonAction() {
        answersHidden.setValue(!answersHidden.get());
    }
    @FXML
    private void setButtonAction() {
        try {
            int newLevel = Integer.parseInt(levelField.getText());
            if (newLevel >= 0 && newLevel <= 10) {
                switch (studyDirection) {
                    case 0:
                        kanjiStudyList.getByIndex(currentIndex.get()).updateCharacterLevel(Integer.parseInt(levelField.getText()));
                        level.setText(String.valueOf(kanjiStudyList.getByIndex(currentIndex.get()).getCharacterLevel()));
                        due.setText(kanjiStudyList.getByIndex(currentIndex.get()).getCharacterDue().toString());
                        break;
                    case 1:
                        kanjiStudyList.getByIndex(currentIndex.get()).updateReadingLevel(Integer.parseInt(levelField.getText()));
                        level.setText(String.valueOf(kanjiStudyList.getByIndex(currentIndex.get()).getReadingLevel()));
                        due.setText(kanjiStudyList.getByIndex(currentIndex.get()).getReadingDue().toString());
                        break;
                    case 2:
                        kanjiStudyList.getByIndex(currentIndex.get()).updateMeaningLevel(Integer.parseInt(levelField.getText()));
                        level.setText(String.valueOf(kanjiStudyList.getByIndex(currentIndex.get()).getMeaningLevel()));
                        due.setText(kanjiStudyList.getByIndex(currentIndex.get()).getMeaningDue().toString());
                        break;
                }
                notUpdatedKanji.remove(kanjiStudyList.getByIndex((currentIndex.get())));

                //update check circle
                checkCircle.setFill(Color.GREEN);
            }
            else {
                //TODO: Sth that says number is not in range
            }
        }
        catch (NumberFormatException nfe) {
        //TODO: Sth that says it's not a valid number
    }
    }
    @FXML
    private void minusButtonAction() {
        try {
            int currentLevel = Integer.parseInt(levelField.getText());
            if (currentLevel > 0) {
                levelField.setText(String.valueOf(currentLevel - 1));
            }
            else {
                //if 0 or lower, set to 0
                levelField.setText("0");
            }
        }
        catch (NumberFormatException nfe) {
            //Reset level field to current level

            switch (studyDirection) {
                case 0:
                    level.setText(kanjiStudyList.getByIndex(currentIndex.get()).getCharacterLevel().toString());
                    break;
                case 1:
                    level.setText(kanjiStudyList.getByIndex(currentIndex.get()).getReadingLevel().toString());
                    break;
                case 2:
                    level.setText(kanjiStudyList.getByIndex(currentIndex.get()).getMeaningLevel().toString());
                    break;
            }
        }
    }
    @FXML
    private void plusButtonAction() {
        try {
            int currentLevel = Integer.parseInt(levelField.getText());
            if (currentLevel < 10) {
                levelField.setText(String.valueOf(currentLevel + 1));
            }
            else {
                //if 10 or higher, set to 10
                levelField.setText("10");
            }
        }
        catch (NumberFormatException nfe) {
            //Reset level field to current level
            switch (studyDirection) {
                case 0:
                    level.setText(kanjiStudyList.getByIndex(currentIndex.get()).getCharacterLevel().toString());
                    break;
                case 1:
                    level.setText(kanjiStudyList.getByIndex(currentIndex.get()).getReadingLevel().toString());
                    break;
                case 2:
                    level.setText(kanjiStudyList.getByIndex(currentIndex.get()).getMeaningLevel().toString());
                    break;
            }
        }
    }
    @FXML
    private void xButtonAction() throws IOException {
        showEndDialog();
    }

    //Keyboard configuration
    @FXML
    private void handleKeyPress(KeyEvent keyEvent) throws IOException {
        if (keyEvent.getCode().equals(KeyCode.A)) {
            previousButton.fire();
        }
        else if (keyEvent.getCode().equals(KeyCode.D)) {
            nextButton.fire();
        }
        else if (keyEvent.getCode().equals(KeyCode.S)) {
            minusButton.fire();
        }
        else if (keyEvent.getCode().equals(KeyCode.W)) {
            plusButton.fire();
        }
        else if (keyEvent.getCode().equals(KeyCode.B)) {
            if (setButton.isDisabled()) {
                showHideButton.fire();
            }
            else {
                setButton.fire();
            }
        }
    }

    private void showEndDialog() throws IOException {
        //At the end of the dictionary / when exiting with xButton
        String[] choices;
        if (notUpdatedKanji.size() > 0) {
            choices = new String[]{"Continue with not updated kanji", "Save and finish", "Finish without saving"};
        } else {
            choices = new String[]{"Save and finish", "Save and finish", "Finish without saving"};
        }
        endDialog = new ChoiceDialog<>(choices[0], choices);
        endDialog.setTitle("Closing session");
        endDialog.setHeaderText("How do you want to proceed?");
        Optional<String> choice = endDialog.showAndWait();
        if (choice.isPresent()) {
            if (choice.get().equals("Save and finish")) {
                App.kanjiDictionary.save(App.settings.getKanjiDictionaryFilePath());
                Parent trainingParent = FXMLLoader.load(App.class.getResource("fxml/menu/mainMenu.fxml"));
                Scene trainingScene = new Scene(trainingParent);
                Stage stage = (Stage) character.getScene().getWindow();
                stage.setScene(trainingScene);
                stage.show();
            } else if (choice.get().equals("Finish without saving")) {
                App.kanjiDictionary.reload(App.settings.getKanjiDictionaryFilePath());
                Parent trainingParent = FXMLLoader.load(App.class.getResource("fxml/menu/mainMenu.fxml"));
                Scene trainingScene = new Scene(trainingParent);
                Stage stage = (Stage) character.getScene().getWindow();
                stage.setScene(trainingScene);
                stage.show();
            } else if (choice.get().equals("Continue with not updated kanji")) {
                notUpdatedKanji.shuffle();
                kanjiStudyList = notUpdatedKanji.clone();
                currentIndex.set(0);
                loadFeatures(0);
                //In case there's only one kanji left
                if (!answersHidden.get()) {
                    answersHidden.setValue(true);
                }
            }
        }
    }
    private void showHints(int index) {
        character.setVisible(true);
    }
    private void showAnswers() {
        for (String feature : App.settings.getStudyDirectionsKanji()[studyDirection]) {
            showFeature(feature);
        }
    }
    private void hideAnswers() {
        for (String feature : App.settings.getStudyDirectionsKanji()[studyDirection]) {
            hideFeature(feature);
        }
    }
    private void loadFeatures(int index) {
        Kanji currentKanji = kanjiStudyList.getByIndex(index);

        vocabBox.getChildren().clear();
        currentVocables = new ArrayList<>();
        currentVocables.addAll(App.vocableDictionary.getByCharacter(currentKanji.getCharacter()));
        for (Vocable v : currentVocables) {
            Label vocableLabel = new Label(v.getReading() + " - " + v.getMeaning());
            vocableLabel.setStyle("-fx-font-size:16");
            vocableLabel.setTooltip(VocableTooltipBuilder.vocableTooltip(v));
            vocabBox.getChildren().add(vocableLabel);
        }

        character.setText(currentKanji.getCharacter());
        character.setTooltip(KanjiTooltipBuilder.kanjiTooltip(currentKanji));

        on.setText(currentKanji.getOnReading());
        kun.setText(currentKanji.getKunReading());
        meaning.setText(currentKanji.getMeaning());
        mnemonic.setText(currentKanji.getMnemonic());
        switch (studyDirection) {
            case 0:
                level.setText(currentKanji.getCharacterLevel().toString());
                due.setText(currentKanji.getCharacterDue().toString());
                break;
            case 1:
                level.setText(currentKanji.getReadingLevel().toString());
                due.setText(currentKanji.getReadingDue().toString());
                break;
            case 2:
                level.setText(currentKanji.getMeaningLevel().toString());
                due.setText(currentKanji.getMeaningDue().toString());
                break;
        }
    }

    private void enableUpdateButtons() {
        minusButton.setDisable(false);
        plusButton.setDisable(false);
        levelField.setDisable(false);
        setButton.setDisable(false);

        levelField.setText(level.getText());
    }

    private void disableUpdateButtons() {
        minusButton.setDisable(true);
        plusButton.setDisable(true);
        levelField.setDisable(true);
        setButton.setDisable(true);

        levelField.setText("");
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
            case "level":
                level.setVisible(true);
                break;
            case "due":
                due.setVisible(true);
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
            case "level":
                level.setVisible(false);
                break;
            case "due":
                due.setVisible(false);
                break;
        }
    }
}

