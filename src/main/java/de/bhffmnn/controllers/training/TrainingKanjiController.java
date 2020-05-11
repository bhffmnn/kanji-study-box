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

public class TrainingKanjiController implements Initializable {
    //Index for iterating over kanjiStudyList
    private IntegerProperty currentIndex;

    //Tracks whether answers are hidden
    private BooleanProperty answersHidden;

    //Dialog for saving and exiting
    private ChoiceDialog<String> endDialog;

    //KanjiDictionary tracking updated kanji
    KanjiDictionary notUpdatedKanji = App.kanjiStudyList.clone();

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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        App.kanjiStudyList.shuffle();
        loadFeatures(0);

        disableUpdateButtons();

        //Initialize progress bar
        progressLabel.setText("1/" + App.kanjiStudyList.size());
        progressBar.progressProperty().set(1 / (App.kanjiStudyList.size()));

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
                    progressBar.progressProperty().set((newValue.doubleValue() + 1) / (App.kanjiStudyList.size()));
                    progressLabel.setText((newValue.intValue() + 1) + "/" + App.kanjiStudyList.size());

                    //Update check circle
                    if (notUpdatedKanji.contains(App.kanjiStudyList.getByIndex(currentIndex.get()))) {
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

        //When learning new kanji, answers must not be hidden
        answersHidden.setValue(true);

        //fire currentIndex listener
        currentIndex.setValue(1);
        currentIndex.setValue(0);
    }

    //Button actions
    @FXML
    private void nextButtonAction() throws IOException {
        if (currentIndex.get() != App.kanjiStudyList.size() - 1) {
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
        switch (App.studyDirection) {
            case 0:
                App.kanjiStudyList.getByIndex(currentIndex.get()).updateCharacterLevel(Integer.parseInt(levelField.getText()));
                level.setText(String.valueOf(App.kanjiStudyList.getByIndex(currentIndex.get()).getCharacterLevel()));
                due.setText(App.kanjiStudyList.getByIndex(currentIndex.get()).getCharacterDue().toString());
                break;
            case 1:
                App.kanjiStudyList.getByIndex(currentIndex.get()).updateReadingLevel(Integer.parseInt(levelField.getText()));
                level.setText(String.valueOf(App.kanjiStudyList.getByIndex(currentIndex.get()).getReadingLevel()));
                due.setText(App.kanjiStudyList.getByIndex(currentIndex.get()).getReadingDue().toString());
                break;
            case 2:
                App.kanjiStudyList.getByIndex(currentIndex.get()).updateMeaningLevel(Integer.parseInt(levelField.getText()));
                level.setText(String.valueOf(App.kanjiStudyList.getByIndex(currentIndex.get()).getMeaningLevel()));
                due.setText(App.kanjiStudyList.getByIndex(currentIndex.get()).getMeaningDue().toString());
                break;
        }
        notUpdatedKanji.remove(App.kanjiStudyList.getByIndex((currentIndex.get())));

        //update check circle
        checkCircle.setFill(Color.GREEN);
    }
    @FXML
    private void minusButtonAction() {
        if (Integer.parseInt(levelField.getText()) > 0) {
            levelField.setText(String.valueOf(Integer.parseInt(levelField.getText()) - 1));
        }
    }
    @FXML
    private void plusButtonAction() {
        if (Integer.parseInt(levelField.getText()) < 10) {
            levelField.setText(String.valueOf(Integer.parseInt(levelField.getText()) + 1));
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
                App.kanjiStudyList = notUpdatedKanji.clone();
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
        for (String feature : App.settings.getStudyDirectionsKanji()[App.studyDirection]) {
            showFeature(feature);
        }
    }
    private void hideAnswers() {
        for (String feature : App.settings.getStudyDirectionsKanji()[App.studyDirection]) {
            hideFeature(feature);
        }
    }
    private void loadFeatures(int index) {
        Kanji currentKanji = App.kanjiStudyList.getByIndex(index);

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
        on.setText(currentKanji.getOnReading());
        kun.setText(currentKanji.getKunReading());
        meaning.setText(currentKanji.getMeaning());
        mnemonic.setText(currentKanji.getMnemonic());
        switch (App.studyDirection) {
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

