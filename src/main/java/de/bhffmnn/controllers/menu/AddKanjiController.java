/*
 * Copyright (c) 2020, Benjamin Hoffmann
 * All rights reserved.

 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree.
 */

package de.bhffmnn.controllers.menu;

import de.bhffmnn.App;
import de.bhffmnn.models.Kanji;
import de.bhffmnn.models.KanjiDictionary;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
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

    private KanjiDictionary cloneDictionary;

    /**
     *
     * @param cloneDictionary The dictionary that the kanji should added to
     */
    public AddKanjiController(KanjiDictionary cloneDictionary) {
        this.cloneDictionary = cloneDictionary;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    @FXML
    private void addButtonAction(ActionEvent actionEvent) {
        Kanji kanji = new Kanji(charField.getText(), onField.getText(), kunField.getText(), meanField.getText(),
                                mnemField.getText());
        if(!cloneDictionary.add(kanji)) {
            System.out.println("Did not work.");
        }
        ((Stage) charField.getScene().getWindow()).close();
    }
}