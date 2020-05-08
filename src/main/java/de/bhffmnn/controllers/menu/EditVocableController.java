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
import de.bhffmnn.models.Vocable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class EditVocableController implements Initializable {
    @FXML
    TextField formField;
    @FXML
    TextField readField;
    @FXML
    TextField meanField;
    @FXML
    TextField exmplField;

    @FXML
    Spinner<Integer> lvlSpinner;

    @FXML
    DatePicker duePicker;

    private Vocable vocable;

    public EditVocableController(Vocable vocable) {
        this.vocable = vocable;
    }



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        formField.setText(vocable.getForm());
        readField.setText(vocable.getReading());
        meanField.setText(vocable.getMeaning());
        exmplField.setText(vocable.getExample());

        lvlSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10));
        lvlSpinner.getValueFactory().setValue(vocable.getLevel());

        duePicker.setValue(vocable.getDue());
    }

    @FXML
    private void saveButtonAction(ActionEvent actionEvent) {
        vocable.setForm(formField.getText());
        vocable.setReading(readField.getText());
        vocable.setMeaning(meanField.getText());
        vocable.setExample(exmplField.getText());

        vocable.setLevel(lvlSpinner.getValue());

        vocable.setDue(duePicker.getValue());

        ((Stage) formField.getScene().getWindow()).close();
    }
}

