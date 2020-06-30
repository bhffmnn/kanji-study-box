/*
 * Copyright (c) 2020, Benjamin Hoffmann
 * All rights reserved.

 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree.
 */

package de.bhffmnn.controllers.selectors;

import de.bhffmnn.App;
import de.bhffmnn.controllers.training.TrainingVocablesController;
import de.bhffmnn.models.VocableDictionary;
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
 * Controller for the StartByLevel view that lets the user choose vocables by their level. Those vocables are then
 * passed to the TrainingVocables view to study them.
 */

public class StartByLevelVocabController implements Initializable {
    private int vocableAmount;
    private int studyDirection;
    private VocableDictionary vocableStudyList;

    @FXML
    private ComboBox<String> studyDirectionBox;
    @FXML
    private Label itemCountLabel;
    @FXML
    private Spinner<Integer> levelSpinner;
    @FXML
    private Spinner<Integer> studyCountSpinner;
    @FXML
    private Button studyButton;
    @FXML
    private Label itemLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        itemLabel.setText("vocables");

        //Set up levelSpinner
        levelSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10));
        levelSpinner.valueProperty().addListener((o, oldValue, newValue) -> {
            vocableStudyList = App.vocableDictionary.getByLevel(newValue);
            vocableAmount = vocableStudyList.size();
            itemCountLabel.setText(String.valueOf(vocableAmount));
            if (vocableAmount == 0) {
                studyCountSpinner.setDisable(true);
                studyButton.setDisable(true);
            }
            else {
                studyCountSpinner.setDisable(false);
                studyButton.setDisable(false);
                studyCountSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, vocableAmount));
                studyCountSpinner.getValueFactory().setValue(Integer.parseInt(itemCountLabel.getText()));
            }


        });
        levelSpinner.getValueFactory().setValue(1);

        studyDirectionBox.getItems().addAll("Study characters", "Study readings", "Study meanings");
        studyDirectionBox.getSelectionModel().select("Study characters");
    }

    @FXML
    private void studyButtonAction(ActionEvent actionEvent) throws Exception {
        vocableStudyList = App.vocableDictionary.getDue().getByRange(0, studyCountSpinner.getValue());
        if (studyDirectionBox.getValue().equals("Study forms")) {
            studyDirection = 0;
        }
        else if (studyDirectionBox.getValue().equals("Study readings")) {
           studyDirection = 1;
        }
        vocableStudyList = vocableStudyList.getByRange(0, studyCountSpinner.getValue());
        FXMLLoader loader = new FXMLLoader(App.class.getResource("fxml/training/trainingVocables.fxml"));
        TrainingVocablesController controller = new TrainingVocablesController(vocableStudyList, studyDirection);
        loader.setController(controller);

        Parent parent = loader.load();
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
