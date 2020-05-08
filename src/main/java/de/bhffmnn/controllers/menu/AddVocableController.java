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

public class AddVocableController implements Initializable {
    @FXML
    TextField formField;
    @FXML
    TextField readField;
    @FXML
    TextField meanField;
    @FXML
    TextField exmplField;

    private VocableDictionary cloneDictionary;

    public AddVocableController(VocableDictionary cloneDictionary) {
        this.cloneDictionary = cloneDictionary;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    @FXML
    private void addButtonAction(ActionEvent actionEvent) {
        Vocable vocable = new Vocable(formField.getText(), readField.getText(), meanField.getText(), exmplField.getText());
        if(!cloneDictionary.add(vocable)) {
            System.out.println("Did not work.");
        }
        ((Stage) formField.getScene().getWindow()).close();
    }
}