/*
 * Copyright (c) 2020, Benjamin Hoffmann
 * All rights reserved.

 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree.
 */

package de.bhffmnn.controllers.selectors;

import de.bhffmnn.App;
import de.bhffmnn.controllers.training.LearningVocablesController;
import de.bhffmnn.models.Kanji;
import de.bhffmnn.models.Vocable;
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
 * Controller for the StartNewVocab view. It lets the user choose a amount of new vocables to learn, and passes those
 * vocables to the LearningVocables view.
 */

public class StartNewVocabController implements Initializable {
    private int vocabAmount;
    private VocableDictionary vocableStudyList;

    @FXML
    private ComboBox<String> scopeComboBox;
    @FXML
    private Label vocabCount;
    @FXML
    private Spinner<Integer> studyCountSpinner;
    @FXML
    private Button studyButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        scopeComboBox.getItems().addAll("Vocabulary of started kanji", "All vocabulary");
        scopeComboBox.getSelectionModel().selectedItemProperty().addListener((o, oldValue, newValue) -> {
            if (newValue.equals("Vocabulary of started kanji")) {
                vocableStudyList = new VocableDictionary();
                for (Kanji kanji : App.kanjiDictionary.getStarted()) {
                    vocableStudyList.addAll(App.vocableDictionary.getByCharacter(kanji.getCharacter()));
                }
                VocableDictionary removeList = new VocableDictionary();
                for (Vocable v : vocableStudyList) {
                    for (char c : v.getForm().toCharArray()) {
                        Kanji k = App.kanjiDictionary.getKanjiByCharacter(String.valueOf(c));
                        if (!(k == null) && (k.getCharacterLevel() == 0)) { //if the kanji exists but isn't started yet
                            removeList.add(v);
                        }
                    }
                }
                vocableStudyList.removeAll(removeList);
            }
            else {
                vocableStudyList = App.vocableDictionary.clone();
            }
            vocableStudyList = vocableStudyList.getByLevel(0);
            vocabAmount = vocableStudyList.size();
            vocabCount.setText(String.valueOf(vocabAmount));
            studyCountSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, vocabAmount));
            studyCountSpinner.getValueFactory().setValue(Integer.parseInt(vocabCount.getText()));
            if (vocabAmount == 0) {
                studyCountSpinner.setDisable(true);
                studyButton.setDisable(true);
            }
            else {
                studyCountSpinner.setDisable(false);
                studyButton.setDisable(false);
            }
        });
        scopeComboBox.getSelectionModel().select("Vocabulary of started kanji");
        }

    @FXML
    private void studyButtonAction(ActionEvent actionEvent) throws Exception {
        vocableStudyList = vocableStudyList.getByRange(0, studyCountSpinner.getValue());
        FXMLLoader loader = new FXMLLoader(App.class.getResource("fxml/training/learningVocables.fxml"));
        LearningVocablesController controller = new LearningVocablesController(vocableStudyList);
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
        Stage stage = (Stage) (vocabCount.getScene().getWindow());

        stage.setScene(scene);
        stage.show();
    }
}