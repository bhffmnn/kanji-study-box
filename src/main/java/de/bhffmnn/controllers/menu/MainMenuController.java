/*
 * Copyright (c) 2020, Benjamin Hoffmann
 * All rights reserved.

 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree.
 */

package de.bhffmnn.controllers.menu;

import de.bhffmnn.App;
import de.bhffmnn.controllers.selectors.StartByLevelKanjiController;
import de.bhffmnn.controllers.selectors.StartScheduledKanjiController;
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

/**
 * Controller for the MainMenu view.
 */
public class MainMenuController implements Initializable {

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
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
        FXMLLoader loader = new FXMLLoader(App.class.getResource("fxml/selectors/startByLevel.fxml"));
        loader.setController(new StartByLevelKanjiController());
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

        stage.setScene(new Scene(loader.load()));
        stage.show();
    }

    @FXML
    private void aboutButtonAction(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(App.class.getResource("fxml/menu/about.fxml"));

        Stage childStage = new Stage();
        childStage.setScene(new Scene((loader.load())));
        childStage.show();
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