/*
 * Copyright (c) 2020, Benjamin Hoffmann
 * All rights reserved.

 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree.
 */

package de.bhffmnn.controllers.menu;

import de.bhffmnn.App;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import de.bhffmnn.controllers.menu.EditKanjiDictionaryController.*;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for the EditKanjiMenuItem view that lets the user edit a EditKanjiMenuItem object.
 */

public class EditKanjiMenuItemController implements Initializable {
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
    Spinner<Integer> indexSpinner;
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

    private KanjiMenuItemList kanjiMenuItemList;
    private KanjiMenuItem kanjiMenuItem;

    /**
     * @param kanjiMenuItemList The list containing the item that should be edited
     * @param itemIndex The index of the item that should be edited
     */
    public EditKanjiMenuItemController(KanjiMenuItemList kanjiMenuItemList, int itemIndex) {
        this.kanjiMenuItemList = kanjiMenuItemList;
        this.kanjiMenuItem = kanjiMenuItemList.get(itemIndex);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        charField.setText(String.valueOf(kanjiMenuItem.getCharacter()));
        onField.setText(kanjiMenuItem.getOnReading());
        kunField.setText(kanjiMenuItem.getKunReading());
        meanField.setText(kanjiMenuItem.getMeaning());
        mnemField.setText(kanjiMenuItem.getMnemonic());

        indexSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, kanjiMenuItemList.size() - 1));
        indexSpinner.getValueFactory().setValue(kanjiMenuItem.getIndex());
        charLvlSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, App.settings.getMaxLevel()));
        charLvlSpinner.getValueFactory().setValue(kanjiMenuItem.getCharacterLevel());
        readLvlSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, App.settings.getMaxLevel()));
        readLvlSpinner.getValueFactory().setValue(kanjiMenuItem.getReadingLevel());
        meanLvlSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, App.settings.getMaxLevel()));
        meanLvlSpinner.getValueFactory().setValue(kanjiMenuItem.getMeaningLevel());

        charDP.setValue(kanjiMenuItem.getCharacterDue());
        readDP.setValue(kanjiMenuItem.getReadingDue());
        meanDP.setValue(kanjiMenuItem.getMeaningDue());
    }

    @FXML
    private void saveButtonAction(ActionEvent actionEvent) {
        if (charField.getText().length() == 1) {
            boolean characterExists = false;
            for (KanjiMenuItem kmi : kanjiMenuItemList) {
                if (!(kanjiMenuItem.getCharacter() == charField.getText().charAt(0)) //If character has been changed
                        && kmi.getCharacter() == charField.getText().charAt(0)) { //But already exists
                    characterExists = true;
                }
            }
            if (characterExists) {
                Alert numberAlert = new Alert(Alert.AlertType.INFORMATION, "A kanji with this character already exists.");
                numberAlert.setHeaderText("");
                numberAlert.show();
            } else {
                kanjiMenuItemList.changeIndexOf(kanjiMenuItem, indexSpinner.getValue());
                kanjiMenuItem.setCharacter(charField.getText().charAt(0));
                kanjiMenuItem.setOnReading(onField.getText());
                kanjiMenuItem.setKunReading(kunField.getText());
                kanjiMenuItem.setMeaning(meanField.getText());
                kanjiMenuItem.setMnemonic(mnemField.getText());

                kanjiMenuItem.setCharacterLevel(charLvlSpinner.getValue());
                kanjiMenuItem.setReadingLevel(readLvlSpinner.getValue());
                kanjiMenuItem.setMeaningLevel(meanLvlSpinner.getValue());

                kanjiMenuItem.setCharacterDue(charDP.getValue());
                kanjiMenuItem.setReadingDue(readDP.getValue());
                kanjiMenuItem.setMeaningDue(meanDP.getValue());

                ((Stage) charField.getScene().getWindow()).close();
            }
        }
        else {
            Alert charAlert = new Alert(Alert.AlertType.INFORMATION,
                    "You have either entered more than one character or a character outside of unicode's Basic" +
                            " Multilingual Plane.");
            charAlert.setHeaderText("");
            charAlert.show();
        }
    }
}