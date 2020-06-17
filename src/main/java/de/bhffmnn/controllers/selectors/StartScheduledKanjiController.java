/*
 * Copyright (c) 2020, Benjamin Hoffmann
 * All rights reserved.

 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree.
 */

package de.bhffmnn.controllers.selectors;

import de.bhffmnn.App;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for the StartScheduled view that lets the user choose an amount of due kanji to study. It saves the chosen
 * kanji into the global variable App.kanjiStudyList opens the TrainingKanji view to study them.
 */
public class StartScheduledKanjiController implements Initializable {
    private int kanjiAmount;

    @FXML
    private ComboBox<String> studyDirectionBox;
    @FXML
    private Label itemCountLabel;
    @FXML
    private Spinner<Integer> studyCountSpinner;
    @FXML
    private Button studyButton;
    @FXML
    private Label itemLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        itemLabel.setText("kanji");
        studyDirectionBox.getItems().addAll("Study characters", "Study readings", "Study meanings");
        studyDirectionBox.valueProperty().addListener((o, oldValue, newValue) -> {
            switch (newValue) {
                case "Study characters":
                    App.studyDirection = 0;
                    kanjiAmount = App.kanjiDictionary.getCharacterDue().size();
                    break;
                case "Study readings":
                    App.studyDirection = 1;
                    kanjiAmount = App.kanjiDictionary.getReadingDue().size();
                    break;
                case "Study meanings":
                    App.studyDirection = 2;
                    kanjiAmount = App.kanjiDictionary.getMeaningDue().size();
                    break;
            }
            itemCountLabel.setText(String.valueOf(kanjiAmount));
            if (kanjiAmount == 0) {
                studyCountSpinner.setDisable(true);
                studyButton.setDisable(true);
            }
            else {
                studyCountSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, kanjiAmount));
                studyCountSpinner.getValueFactory().setValue(Integer.parseInt(itemCountLabel.getText()));
                studyCountSpinner.setDisable(false);
                studyButton.setDisable(false);
            }
        });
        studyDirectionBox.getSelectionModel().select("Study characters");
    }

    @FXML
    private void studyButtonAction(ActionEvent actionEvent) throws Exception {
        switch (App.studyDirection) {
            case 0:
                App.kanjiStudyList = App.kanjiDictionary.getCharacterDue().getByRange(0, studyCountSpinner.getValue());
                break;
            case 1:
                App.kanjiStudyList = App.kanjiDictionary.getReadingDue().getByRange(0, studyCountSpinner.getValue());
                break;
            case 2:
                App.kanjiStudyList = App.kanjiDictionary.getMeaningDue().getByRange(0, studyCountSpinner.getValue());
                break;
        }
        

        Parent parent = FXMLLoader.load(App.class.getResource("fxml/training/trainingKanji.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void backButtonAction() throws IOException {
        Parent parent = FXMLLoader.load(App.class.getResource("fxml/menu/mainMenu.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) (itemCountLabel.getScene().getWindow());

        stage.setScene(scene);
        stage.show();
    }
}