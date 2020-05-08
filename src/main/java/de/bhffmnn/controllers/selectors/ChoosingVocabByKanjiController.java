/*
 * Copyright (c) 2020, Benjamin Hoffmann
 * All rights reserved.

 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree.
 */

package de.bhffmnn.controllers.selectors;

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
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;

public class ChoosingVocabByKanjiController  implements Initializable {
    //Index for iterating over kanjiStudyList
    private IntegerProperty currentIndex;

    //Dialog for saving and exiting
    private ChoiceDialog<String> endDialog;

    //VocableDictionarys for the respective kanji
    private VocableDictionary startedKanjiVocables;
    private VocableDictionary notStartedKanjiVocables;

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
    private VBox startedVocabVBox;
    @FXML
    private VBox availableVocabVBox;

    //Progress bar
    @FXML
    private ProgressBar progressBar;
    @FXML
    private Label progressLabel;

    //Buttons
    @FXML
    private Button addButton;
    @FXML
    private Button previousButton;
    @FXML
    private Button nextButton;

    //Check mark for updated Kanji
    @FXML
    private Circle checkCircle;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        App.vocableStudyList = new VocableDictionary();
        loadFeatures(0);

        //Set up currentIndex property
        currentIndex = new SimpleIntegerProperty(0);
        currentIndex.addListener(
                ((observableValue, oldValue, newValue) -> {
                    //update labels
                    loadFeatures(newValue.intValue());

                    //update progress bar
                    progressBar.progressProperty().set((newValue.doubleValue() + 1) / (App.kanjiStudyList.size()));
                    progressLabel.setText((newValue.intValue() + 1) + "/" + App.kanjiStudyList.size());
                })
        );

        //Initialize stuff
        loadFeatures(0);
        progressBar.progressProperty().set(1.0 / (App.kanjiStudyList.size()));
        progressLabel.setText("1/" + App.kanjiStudyList.size());
    }

    //Button actions
    @FXML
    private void nextButtonAction() throws IOException {
        if (currentIndex.get() != App.kanjiStudyList.size() - 1) {
            currentIndex.set(currentIndex.getValue() + 1);
        }
        else {
            showEndDialog();
        }
        addButton.fire();
    }
    @FXML
    private void previousButtonAction() {
        /*
        if (currentIndex.get() != 0) {
            currentIndex.set(currentIndex.getValue() - 1);
        }
         */
    }
    @FXML
    private void addButtonAction() {
        int index = 0;
        for(Node node: availableVocabVBox.getChildren()) {
            if (((CheckBox) node).isSelected()) {
                App.vocableStudyList.add(notStartedKanjiVocables.getByIndex(index));
            }
            index++;
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
        String[] choices = {"Learn selected", "Learn selected", "Nothing"};
        endDialog = new ChoiceDialog<>(choices[0], choices);
        endDialog.setTitle("End of list");
        endDialog.setHeaderText("How do you want to proceed?");
        Optional<String> choice = endDialog.showAndWait();
        if (choice.isPresent()) {
            if (choice.get().equals("Learn selected")) {
                Parent trainingParent = FXMLLoader.load(App.class.getResource("fxml/training/learningKanji.fxml"));
                Scene trainingScene = new Scene(trainingParent);
                Stage stage = (Stage) character.getScene().getWindow();
                stage.setScene(trainingScene);
                stage.show();
            } else if (choice.get().equals("Nothing")) {
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

        startedVocabVBox.getChildren().setAll();
        availableVocabVBox.getChildren().setAll();

        VocableDictionary kanjiVocables = new VocableDictionary();

        notStartedKanjiVocables = App.vocableDictionary.getByLevel(0);
        startedKanjiVocables = App.vocableDictionary.getByCharacter(App.kanjiStudyList.getByIndex(index).getCharacter());
        startedKanjiVocables.removeAll(notStartedKanjiVocables);

        for (Vocable vocable : startedKanjiVocables) {
            startedVocabVBox.getChildren().add(new Label(vocable.getForm() + " - " + vocable.getReading()));
        }

        for (Vocable vocable : kanjiVocables.getByLevel(0)) {
            availableVocabVBox.getChildren().add(new Label(vocable.getForm() + " - " + vocable.getReading()));
        }
    }
}
