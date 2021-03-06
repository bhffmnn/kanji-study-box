/*
 * Copyright (c) 2020, Benjamin Hoffmann
 * All rights reserved.

 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree.
 */

package de.bhffmnn.controllers.menu;

import de.bhffmnn.models.Kanji;
import de.bhffmnn.models.KanjiDictionary;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for the AddKanji view that lets the user add a kanji to a dictionary.
 */

public class AddKanjiController implements Initializable {
    @FXML
    TextField charField;
    @FXML
    TextField onField;
    @FXML
    TextField kunField;
    @FXML
    TextField meanField;
    @FXML
    TextField mnemField;

    private KanjiDictionary kanjiDictionary;

    /**
     *
     * @param kanjiDictionary The dictionary that the kanji should added to
     */
    public AddKanjiController(KanjiDictionary kanjiDictionary) {
        this.kanjiDictionary = kanjiDictionary;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    @FXML
    private void addButtonAction(ActionEvent actionEvent) {
        Kanji kanji = new Kanji(charField.getText().charAt(0), onField.getText(), kunField.getText(), meanField.getText(),
                                mnemField.getText());
        if(!kanjiDictionary.add(kanji)) {
            System.out.println("Did not work.");
        }
        ((Stage) charField.getScene().getWindow()).close();
    }
}