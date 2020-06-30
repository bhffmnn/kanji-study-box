/*
 * Copyright (c) 2020, Benjamin Hoffmann
 * All rights reserved.

 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree.
 */

package de.bhffmnn.controllers.menu;

import de.bhffmnn.models.Kanji;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for the EditKanji view that lets the user edit a Kanji object.
 */

public class EditKanjiController implements Initializable {
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

    @FXML
    Spinner<Integer> charLvlSpinner;
    @FXML
    Spinner<Integer> readLvlSpinner;
    @FXML
    Spinner<Integer> meanLvlSpinner;

    @FXML
    DatePicker charDP;
    @FXML
    DatePicker readDP;
    @FXML
    DatePicker meanDP;

    private Kanji kanji;

    /**
     *
     * @param kanji The kanji that should be edited
     */
    public EditKanjiController(Kanji kanji) {
        this.kanji = kanji;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        charField.setText(kanji.getCharacter());
        onField.setText(kanji.getOnReading());
        kunField.setText(kanji.getKunReading());
        meanField.setText(kanji.getMeaning());
        mnemField.setText(kanji.getMnemonic());

        charLvlSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10));
        charLvlSpinner.getValueFactory().setValue(kanji.getCharacterLevel());
        readLvlSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10));
        readLvlSpinner.getValueFactory().setValue(kanji.getReadingLevel());
        meanLvlSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10));
        meanLvlSpinner.getValueFactory().setValue(kanji.getMeaningLevel());

        charDP.setValue(kanji.getCharacterDue());
        readDP.setValue(kanji.getReadingDue());
        meanDP.setValue(kanji.getMeaningDue());
    }

    @FXML
    private void saveButtonAction(ActionEvent actionEvent) {
        kanji.setCharacter(charField.getText());
        kanji.setOnReading(onField.getText());
        kanji.setKunReading(kunField.getText());
        kanji.setMeaning(meanField.getText());
        kanji.setMnemonic(mnemField.getText());

        kanji.setCharacterLevel(charLvlSpinner.getValue());
        kanji.setReadingLevel(readLvlSpinner.getValue());
        kanji.setMeaningLevel(meanLvlSpinner.getValue());

        kanji.setCharacterDue(charDP.getValue());
        kanji.setReadingDue(readDP.getValue());
        kanji.setMeaningDue(meanDP.getValue());
        
        ((Stage) charField.getScene().getWindow()).close();
    }
}