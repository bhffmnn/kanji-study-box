/*
 * Copyright (c) 2020, Benjamin Hoffmann
 * All rights reserved.

 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree.
 */

package de.bhffmnn.controllers.training;

import de.bhffmnn.App;
import de.bhffmnn.models.KanjiDictionary;
import de.bhffmnn.models.StudyUnit;
import de.bhffmnn.models.Vocable;
import de.bhffmnn.models.VocableDictionary;
import javafx.beans.property.*;
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

public class LearningVocablesController implements Initializable {
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
    private Label form;
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        studyVocab = App.vocableStudyList.clone();
        uncheckedVocab = App.vocableStudyList.clone();

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
            studyVocab = App.vocableStudyList.clone();
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
        String[] choices = {"Save and finish", "Save and finish", "Finish without saving"};
        endDialog = new ChoiceDialog<>(choices[0], choices);
        endDialog.setTitle("End of list");
        endDialog.setHeaderText("How do you want to proceed?");
        Optional<String> choice = endDialog.showAndWait();
        if (choice.isPresent()) {
            if (choice.get().equals("Save and finish")) {
                for (Vocable vocable : App.vocableStudyList) {
                    vocable.setLevel(1);
                    vocable.setDue(LocalDate.now());
                }
                App.vocableDictionary.save(App.settings.getVocableDictionaryFilePath());
                Parent trainingParent = FXMLLoader.load(App.class.getResource("fxml/menu/mainMenu.fxml"));
                Scene trainingScene = new Scene(trainingParent);
                Stage stage = (Stage) form.getScene().getWindow();
                stage.setScene(trainingScene);
                stage.show();
            } else if (choice.get().equals("Finish without saving")) {
                Parent trainingParent = FXMLLoader.load(App.class.getResource("fxml/menu/mainMenu.fxml"));
                Scene trainingScene = new Scene(trainingParent);
                Stage stage = (Stage) form.getScene().getWindow();
                stage.setScene(trainingScene);
                stage.show();
            }
        }
    }

    private void loadFeatures(int index) {
        form.setText(studyVocab.getByIndex(index).getForm());
        reading.setText(studyVocab.getByIndex(index).getReading());
        meaning.setText(studyVocab.getByIndex(index).getMeaning());
        example.setText(studyVocab.getByIndex(index).getExample());
    }

    private void showFeature(String feature) {
        switch (feature) {
            case "form":
                form.setVisible(true);
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
                form.setVisible(false);
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
