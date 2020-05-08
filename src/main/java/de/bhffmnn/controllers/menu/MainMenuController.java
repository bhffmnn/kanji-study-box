/*
 * Copyright (c) 2020, Benjamin Hoffmann
 * All rights reserved.

 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree.
 */

package de.bhffmnn.controllers.menu;

import de.bhffmnn.App;
import de.bhffmnn.controllers.selectors.StartScheduledKanjiController;
import de.bhffmnn.controllers.selectors.StartScheduledVocabController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainMenuController implements Initializable {

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        App.kanjiStudyList = null;
        App.vocableStudyList = null;
        App.studyDirection = 0;
    }

    @FXML
    private void learnNewKanjiButtonAction(ActionEvent actionEvent) throws Exception {
        loadNewSceneStatic(actionEvent,"selectors/startNewKanji.fxml");
    }

    @FXML
    private void studyByScheduleButtonAction(ActionEvent actionEvent) throws Exception {
        FXMLLoader loader = new FXMLLoader(App.class.getResource("fxml/selectors/startScheduled.fxml"));
        loader.setController(new StartScheduledKanjiController());
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

        stage.setScene(new Scene(loader.load()));
        stage.show();
        //loadNewSceneStatic(actionEvent,"selectors/startScheduledKanji.fxml");
    }

    @FXML
    private void studyByLevelButtonAction(ActionEvent actionEvent) throws Exception {
        loadNewSceneStatic(actionEvent,"selectors/startKanjiByLevel.fxml");
    }

    @FXML
    private void learnNewVocabularyButtonAction(ActionEvent actionEvent) throws Exception {
        loadNewSceneStatic(actionEvent,"selectors/startNewVocab.fxml");
    }

    @FXML
    private void studyVocabByScheduleButtonAction(ActionEvent actionEvent) throws Exception {
        FXMLLoader loader = new FXMLLoader(App.class.getResource("fxml/selectors/startScheduled.fxml"));
        loader.setController(new StartScheduledVocabController());
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

        stage.setScene(new Scene(loader.load()));
        stage.show();
        //loadNewSceneStatic(actionEvent,"selectors/startScheduledVocab.fxml");
    }

    @FXML
    private void studyVocabByLevelButtonAction(ActionEvent actionEvent) throws Exception {
        loadNewSceneStatic(actionEvent,"selectors/startVocabByLevel.fxml");
    }

    @FXML
    private void settingsButtonAction(ActionEvent actionEvent) throws Exception {
        loadNewSceneStatic(actionEvent,"menu/settings.fxml");
    }

    @FXML
    private void kanjiDictionaryAction(ActionEvent actionEvent) throws Exception {
        loadNewSceneStatic(actionEvent,"menu/kanjiDictionaryEdit.fxml");
    }

    @FXML
    private void vocDictionaryAction(ActionEvent actionEvent) throws Exception {
        loadNewSceneStatic(actionEvent,"menu/vocableDictionaryEdit.fxml");
    }

    private void loadNewSceneStatic(ActionEvent actionEvent, String fxml) throws IOException {
        Parent parent = FXMLLoader.load(App.class.getResource("fxml/" + fxml));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

        stage.setScene(scene);
        stage.show();
    }
}