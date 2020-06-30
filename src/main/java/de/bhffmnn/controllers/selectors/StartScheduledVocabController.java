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
 * Controller for the StartScheduled view hat lets the user choose an amount of due vocables. Those vocables are then
 * passed to the TrainingVocables view to study them.
 */
public class StartScheduledVocabController implements Initializable {
    private int vocableAmount;
    private int studyDirection;
    private VocableDictionary vocableStudyList;

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
        int vocableAmount = App.vocableDictionary.getDue().size();
        itemLabel.setText("vocables");
        itemCountLabel.setText(String.valueOf(vocableAmount));
        studyDirectionBox.getItems().addAll("Study forms", "Study readings");
        studyDirectionBox.getSelectionModel().select(0);
        if (vocableAmount == 0) {
            studyCountSpinner.setDisable(true);
            studyButton.setDisable(true);
        }
        else {
            studyCountSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, vocableAmount));
            studyCountSpinner.getValueFactory().setValue(Integer.parseInt(itemCountLabel.getText()));
        }
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