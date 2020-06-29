/*
 * Copyright (c) 2020, Benjamin Hoffmann
 * All rights reserved.

 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree.
 */

package de.bhffmnn.controllers.menu;

import de.bhffmnn.models.Kanji;
import de.bhffmnn.models.KanjiDictionary;
import de.bhffmnn.models.Vocable;
import de.bhffmnn.models.VocableDictionary;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for the AddVocable view that lets the user add a vocable to a dictionary.
 */

public class AddVocableController implements Initializable {
    @FXML
    TextField formField;
    @FXML
    TextField readField;
    @FXML
    TextField meanField;
    @FXML
    TextField exmplField;

    private VocableDictionary vocableDictionary;

    /**
     *
     * @param vocableDictionary The dictionary that the vocable should added to
     */
    public AddVocableController(VocableDictionary vocableDictionary) {
        this.vocableDictionary = vocableDictionary;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    @FXML
    private void addButtonAction(ActionEvent actionEvent) {
        Vocable vocable = new Vocable(formField.getText(), readField.getText(), meanField.getText(), exmplField.getText());
        if(!vocableDictionary.add(vocable)) {
            System.out.println("Did not work.");
        }
        ((Stage) formField.getScene().getWindow()).close();
    }
}