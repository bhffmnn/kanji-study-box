/*
 * Copyright (c) 2020, Benjamin Hoffmann
 * All rights reserved.

 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree.
 */

package de.bhffmnn.controllers.selectors;

import de.bhffmnn.App;
import de.bhffmnn.controllers.training.LearningKanjiController;
import de.bhffmnn.models.KanjiDictionary;
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
 * Controller for the StartNewKanji view. It lets the user choose a amount of new kanji to learn, and passes those kanji
 * to the LearningKanji view.
 */
public class StartNewKanjiController implements Initializable {
    private int kanjiAmount;
    private KanjiDictionary kanjiStudyList;

    @FXML
    private Label kanjiCount;
    @FXML
    private Spinner<Integer> studyCountSpinner;
    @FXML
    private Button studyButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        kanjiStudyList = App.kanjiDictionary.getByCharacterLevel(0);
        kanjiAmount = kanjiStudyList.size();
        kanjiCount.setText(String.valueOf(kanjiAmount));
        if (kanjiAmount == 0) {
            studyCountSpinner.setDisable(true);
            studyButton.setDisable(true);
        }
        else {
            studyCountSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, kanjiAmount));
        }
    }

    @FXML
    private void studyButtonAction(ActionEvent actionEvent) throws Exception {
        kanjiStudyList = kanjiStudyList.getByRange(0, studyCountSpinner.getValue());
        FXMLLoader loader = new FXMLLoader(App.class.getResource("fxml/training/learningKanji.fxml"));
        LearningKanjiController controller = new LearningKanjiController(kanjiStudyList);
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
        Stage stage = (Stage) (kanjiCount.getScene().getWindow());

        stage.setScene(scene);
        stage.show();
    }
}