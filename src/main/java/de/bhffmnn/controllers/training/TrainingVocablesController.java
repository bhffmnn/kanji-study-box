/*
 * Copyright (c) 2020, Benjamin Hoffmann
 * All rights reserved.

 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree.
 */

package de.bhffmnn.controllers.training;

import de.bhffmnn.App;
import de.bhffmnn.models.*;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Border;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Controller for the TrainingVocables view which lets the user study vocables. These vocables are determined by the
 * global variable App.vocableStudyList
 */


public class TrainingVocablesController implements Initializable {
    //Index for iterating over kanjiStudyList
    private IntegerProperty currentIndex;

    //Tracks whether answers are hidden
    private BooleanProperty answersHidden;

    //Dialog for saving and exiting
    private ChoiceDialog<String> endDialog;

    //KanjiDictionary tracking updated kanji
    private VocableDictionary notUpdatedVocab = App.vocableStudyList.clone();

    //Character feature labels
    @FXML
    private HBox formBox;
    @FXML
    private Label reading;
    @FXML
    private Label meaning;
    @FXML
    private Label example;
    @FXML
    private Label level;
    @FXML
    private Label due;
    @FXML
    private GridPane gridPane;

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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Shuffle for better effect :)
        App.vocableStudyList.shuffle();

        //Initialize progress bar
        progressLabel.setText("1/" + App.vocableStudyList.size());
        progressBar.progressProperty().set(1 / (App.vocableStudyList.size()));

        //Set up currentIndex property
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
                    progressBar.progressProperty().set((newValue.doubleValue() + 1) / (App.vocableStudyList.size()));
                    progressLabel.setText((newValue.intValue() + 1) + "/" + App.vocableStudyList.size());

                    //Update check circle
                    if (notUpdatedVocab.contains(App.vocableStudyList.getByIndex(currentIndex.get()))) {
                        checkCircle.setFill(null);
                    }
                    else {
                        checkCircle.setFill(Color.GREEN);
                    }
                })
        );

        //Set up answerHidden property
        answersHidden = new SimpleBooleanProperty();
        answersHidden.addListener(((observableValue, oldValue, newValue) -> {
            if (newValue) {  //if answers hidden
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
        if (currentIndex.get() != App.vocableStudyList.size() - 1) {
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
                App.vocableStudyList.getByIndex(currentIndex.get()).updateLevel(Integer.parseInt(levelField.getText()));
                level.setText(String.valueOf(App.vocableStudyList.getByIndex(currentIndex.get()).getLevel()));
                due.setText(App.vocableStudyList.getByIndex(currentIndex.get()).getDue().toString());
                notUpdatedVocab.remove(App.vocableStudyList.getByIndex((currentIndex.get())));
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
            levelField.setText(App.vocableStudyList.getByIndex(currentIndex.get()).getLevel().toString());
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
            levelField.setText(App.vocableStudyList.getByIndex(currentIndex.get()).getLevel().toString());
        }
    }
    @FXML
    private void xButtonAction() throws IOException {
        showEndDialog();
    }
    /*
    @FXML
    private void infoButtonAction(ActionEvent actionEvent) {
        Kanji kanji;
        for (String character : form.getText().split("")) {
            if ((kanji = App.kanjiDictionary.getKanjiByCharacter(character)) != null) {
                //creat thing
            }
        }
    }
     */

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
        else if (keyEvent.getCode().equals(KeyCode.H)) {
            if (setButton.isDisabled()) {
                showHideButton.fire();
            }
            else {
                setButton.fire();
            }
        }
        else if (keyEvent.getCode().equals(KeyCode.M)) {
            Vocable currentVocable = App.vocableStudyList.getByIndex(currentIndex.get());
            if (currentVocable.getLevel() == 10) {
                currentVocable.updateLevel(10);
            }
            else {
                currentVocable.updateLevel(currentVocable.getLevel() + 1);
            }
            notUpdatedVocab.remove(App.vocableStudyList.getByIndex((currentIndex.get())));
            nextButton.fire();
        }
        else if (keyEvent.getCode().equals(KeyCode.B)) {
            Vocable currentVocable = App.vocableStudyList.getByIndex(currentIndex.get());
            if (currentVocable.getLevel() <= 1) {
                currentVocable.updateLevel(currentVocable.getLevel());
            }
            else {
                currentVocable.updateLevel(1);
            }
            notUpdatedVocab.remove(App.vocableStudyList.getByIndex((currentIndex.get())));
            nextButton.fire();
        }
        else if (keyEvent.getCode().equals(KeyCode.N)) {
            Vocable currentVocable = App.vocableStudyList.getByIndex(currentIndex.get());
            currentVocable.updateLevel(currentVocable.getLevel());
            notUpdatedVocab.remove(App.vocableStudyList.getByIndex((currentIndex.get())));
            nextButton.fire();
        }
    }

    private void showEndDialog() throws IOException {
        //At the end of the dictionary / when exiting with xButton
        String[] choices;
        if (notUpdatedVocab.size() > 0) {
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
                App.vocableDictionary.save(App.settings.getVocableDictionaryFilePath());
                Parent trainingParent = FXMLLoader.load(App.class.getResource("fxml/menu/mainMenu.fxml"));
                Scene trainingScene = new Scene(trainingParent);
                Stage stage = (Stage) formBox.getScene().getWindow();
                stage.setScene(trainingScene);
                stage.show();
            } else if (choice.get().equals("Finish without saving")) {
                App.vocableDictionary.reload(App.settings.getVocableDictionaryFilePath());
                Parent trainingParent = FXMLLoader.load(App.class.getResource("fxml/menu/mainMenu.fxml"));
                Scene trainingScene = new Scene(trainingParent);
                Stage stage = (Stage) formBox.getScene().getWindow();
                stage.setScene(trainingScene);
                stage.show();
            } else if (choice.get().equals("Continue with not updated kanji")) {
                notUpdatedVocab.shuffle();
                App.vocableStudyList = notUpdatedVocab.clone();
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
        formBox.setVisible(true);
    }
    private void showAnswers() {
        for (String feature : App.settings.getStudyDirectionsVocab()[App.studyDirection]) {
            showFeature(feature);
        }
    }
    private void hideAnswers() {
        for (String feature : App.settings.getStudyDirectionsVocab()[App.studyDirection]) {
            hideFeature(feature);
        }
    }
    private void loadFeatures(int index) {
        formBox.getChildren().clear();
        for (String character : App.vocableStudyList.getByIndex(index).getForm().split("")) {
            Label charLabel = new Label(character);
            System.out.println(charLabel.getLayoutX());
            charLabel.setStyle("-fx-font-size:50; -fx-label-padding: -6;");
            Kanji kanji = App.kanjiDictionary.getKanjiByCharacter(character);
            if (kanji != null) {
                charLabel.setTooltip(KanjiTooltipBuilder.kanjiTooltip(kanji));
            }
            formBox.getChildren().add(charLabel);
        }

        reading.setText(App.vocableStudyList.getByIndex(index).getReading());
        meaning.setText(App.vocableStudyList.getByIndex(index).getMeaning());
        example.setText(App.vocableStudyList.getByIndex(index).getExample());
        level.setText(String.valueOf(App.vocableStudyList.getByIndex(index).getLevel()));
        due.setText(App.vocableStudyList.getByIndex(index).getDue().toString());
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
            case "level":
                level.setVisible(false);
                break;
            case "due":
                due.setVisible(false);
                break;
        }
    }
}

