/*
 * Copyright (c) 2020, Benjamin Hoffmann
 * All rights reserved.

 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree.
 */

package de.bhffmnn.controllers.menu;

import de.bhffmnn.models.Vocable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for the EditVocable view that lets the user edit a Vocable object.
 */

public class EditVocableController implements Initializable {
    @FXML
    TextField formField;
    @FXML
    TextField readField;
    @FXML
    TextField meanField;
    @FXML
    TextField exmplField;

    private Vocable vocable;

    /**
     *
     * @param vocable The vocable that should be edited
     */
    public EditVocableController(Vocable vocable) {
        this.vocable = vocable;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        formField.setText(vocable.getForm());
        readField.setText(vocable.getReading());
        meanField.setText(vocable.getMeaning());
        exmplField.setText(vocable.getExample());
    }

    @FXML
    private void saveButtonAction(ActionEvent actionEvent) {
        vocable.setForm(formField.getText());
        vocable.setReading(readField.getText());
        vocable.setMeaning(meanField.getText());
        vocable.setExample(exmplField.getText());

        ((Stage) formField.getScene().getWindow()).close();
    }
}

