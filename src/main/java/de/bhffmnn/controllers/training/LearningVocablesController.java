/*
 * Copyright (c) 2020, Benjamin Hoffmann
 * All rights reserved.

 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree.
 */

package de.bhffmnn.controllers.training;

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
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Controller for the LearningVocables view which lets the user learn new vocables.
 */

public class LearningVocablesController implements Initializable {
    //Vocables which are to be studied
    private VocableDictionary vocableStudyList;

    /*
    The following variable tracks the learning phase.
    Phase 0: All features are visible and the showAndCheckButton is disabled.
    Phase 1: Checking forms
    Phase 2: Checking readings
     */
    private IntegerProperty phase;

    //Index for iterating over vocableStudyList
    private IntegerProperty currentIndex;

    //Tracks whether answers are hidden
    private BooleanProperty answersHidden;

    private VocableDictionary uncheckedVocab;
    private VocableDictionary studyVocab;

    //Dialog for saving and exiting
    private ChoiceDialog<String> endDialog;

    //Character feature labels
    @FXML
    private HBox formBox;
    @FXML
    private Label reading;
    @FXML
    private Label meaning;
    @FXML
    private Label example;

    //Progress bar
    @FXML
    private ProgressBar progressBar;
    @FXML
    private Label progressLabel;

    //Buttons
    @FXML
    private Button previousButton;
    @FXML
    private Button nextButton;
    @FXML
    private Button showAndCheckButton;

    //Check mark
    @FXML
    private Circle checkCircle;

    /**
     *
     * @param vocableStudyList The vocables that are to be learned
     */
    public LearningVocablesController(VocableDictionary vocableStudyList) {
        this.vocableStudyList = vocableStudyList;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        studyVocab = vocableStudyList.clone();
        uncheckedVocab = vocableStudyList.clone();

        loadFeatures(0);

        //Initialize progress bar
        progressLabel.setText("1/" + studyVocab.size());
        progressBar.progressProperty().set(1 / (studyVocab.size()));

        //Set up currentIndex property
        currentIndex = new SimpleIntegerProperty(0);
        currentIndex.addListener(
                ((observableValue, oldValue, newValue) -> {
                    //update labels
                    loadFeatures(newValue.intValue());

                    //update progress bar
                    progressBar.progressProperty().set((newValue.doubleValue() + 1) / (studyVocab.size()));
                    progressLabel.setText((newValue.intValue() + 1) + "/" + studyVocab.size());

                    if (phase.get() > 0) {
                        //Update check circle
                        if (uncheckedVocab.contains(studyVocab.getByIndex(currentIndex.get()))) {
                            checkCircle.setFill(null);
                        }
                        else {
                            checkCircle.setFill(Color.GREEN);
                        }
                        answersHidden.set(true);
                    }
                }));

        //Set up phase property
        phase = new SimpleIntegerProperty(0);
        phase.addListener(((observableValue, oldValue, newValue) -> {
            if (newValue.intValue() > 0) {
                showAndCheckButton.setDisable(false);
            }
            studyVocab = vocableStudyList.clone();
            uncheckedVocab = studyVocab.clone();
            currentIndex.set(0);
        }));

        //Set up answersHidden property
        answersHidden = new SimpleBooleanProperty(false);
        answersHidden.addListener(((observableValue, oldValue, newValue) -> {
            if (newValue) {  //if answers hidden
                showAndCheckButton.setText("Show");
                for (String feature : App.settings.getStudyDirectionsVocab()[phase.get() - 1]) {
                    hideFeature(feature);
                }
            }
            else {
                showAndCheckButton.setText("Check!");
                for (String feature : App.settings.getStudyDirectionsVocab()[phase.get() - 1]) {
                    showFeature(feature);
                }
            }
        }));

        //Set it all up
        showAndCheckButton.setDisable(true);
        loadFeatures(0);
        progressBar.progressProperty().set(1.0 / (studyVocab.size()));
        progressLabel.setText("1/" + studyVocab.size());
    }

    //Button actions
    @FXML
    private void nextButtonAction() throws IOException {
        if (currentIndex.get() != studyVocab.size() - 1) {
            currentIndex.set(currentIndex.getValue() + 1);
        }
        else if (phase.get() == 0 || (phase.get() == 1 && uncheckedVocab.size() == 0)) {
            phase.set(phase.get() + 1);
            progressBar.progressProperty().set((1.0 / (studyVocab.size())));
            progressLabel.setText(("1/" + studyVocab.size()));
            checkCircle.setFill(null);
            answersHidden.set(true);
            loadFeatures(0);
        }
        else if (uncheckedVocab.size() > 0) {
            studyVocab = uncheckedVocab.clone();
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
            uncheckedVocab.remove(studyVocab.getByIndex(currentIndex.get()));
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

        //The buttons for the Alert
        ButtonType yesButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
        ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.NO);
        ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        Alert endDialog = new Alert(Alert.AlertType.CONFIRMATION);
        endDialog.setTitle("Closing learning session");
        endDialog.setContentText("Do you want to save and schedule these vocables for studying?");
        endDialog.setHeaderText("");
        endDialog.getButtonTypes().setAll(yesButton, noButton, cancelButton);

        Optional<ButtonType> choice = endDialog.showAndWait();
        if (choice.isPresent()) {
            if (choice.get().getButtonData().equals(ButtonBar.ButtonData.YES)) {
                for (Vocable vocable : vocableStudyList) {
                    vocable.setLevel(1);
                    vocable.setDue(LocalDate.now());
                }
                App.vocableDictionary.save(App.settings.getVocableDictionaryFilePath());
                Parent trainingParent = FXMLLoader.load(App.class.getResource("fxml/menu/mainMenu.fxml"));
                Scene trainingScene = new Scene(trainingParent);
                Stage stage = (Stage) formBox.getScene().getWindow();
                stage.setScene(trainingScene);
                stage.show();
            }
            else if (choice.get().equals(choice.get().getButtonData().equals(ButtonBar.ButtonData.NO))) {
                Parent trainingParent = FXMLLoader.load(App.class.getResource("fxml/menu/mainMenu.fxml"));
                Scene trainingScene = new Scene(trainingParent);
                Stage stage = (Stage) formBox.getScene().getWindow();
                stage.setScene(trainingScene);
                stage.show();
            }
        }
    }

    private void loadFeatures(int index) {
        formBox.getChildren().clear();
        for (String character : studyVocab.getByIndex(index).getForm().split("")) {
            Label charLabel = new Label(character);
            System.out.println(charLabel.getLayoutX());
            charLabel.setStyle("-fx-font-size:50; -fx-label-padding: -6;");
            Kanji kanji = App.kanjiDictionary.getKanjiByCharacter(character);
            if (kanji != null) {
                charLabel.setTooltip(KanjiTooltipBuilder.kanjiTooltip(kanji));
            }
            formBox.getChildren().add(charLabel);
        }
        reading.setText(studyVocab.getByIndex(index).getReading());
        meaning.setText(studyVocab.getByIndex(index).getMeaning());
        example.setText(studyVocab.getByIndex(index).getExample());
    }

    private void showFeature(String feature) {
        switch (feature) {
            case "form":
                formBox.setVisible(true);
                break;
            case "reading":
                reading.setVisible(true);
                break;
            case "meaning":
                meaning.setVisible(true);
                break;
            case "example":
                example.setVisible(true);
                break;
        }
    }

    private void hideFeature(String feature) {
        switch (feature) {
            case "form":
                formBox.setVisible(false);
                break;
            case "reading":
                reading.setVisible(false);
                break;
            case "meaning":
                meaning.setVisible(false);
                break;
            case "example":
                example.setVisible(false);
                break;
        }
    }
}
