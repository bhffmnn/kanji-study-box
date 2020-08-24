/*
 * Copyright (c) 2020, Benjamin Hoffmann
 * All rights reserved.

 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree.
 */


package de.bhffmnn.controllers.menu;

import de.bhffmnn.App;
import de.bhffmnn.controllers.menu.EditKanjiDictionaryController.KanjiMenuItem;
import de.bhffmnn.controllers.menu.EditKanjiDictionaryController.KanjiMenuItemList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

/**
 * Controller for the EditKanjiMenuItemMultipe view that lets the user edit multipe EditKanjiMenuItem objects..
 */

public class EditKanjiMenuItemMultipleController implements Initializable {
    @FXML
    GridPane mainGrid;

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

    private KanjiMenuItemList kanjiMenuItemList;
    private int[] itemIndexes;

    /**
     * @param kanjiMenuItemList The list containing the item that should be edited
     * @param itemIndexes The indexes of the items that should be edited
     */
    public EditKanjiMenuItemMultipleController(KanjiMenuItemList kanjiMenuItemList, int[] itemIndexes) {
        this.kanjiMenuItemList = kanjiMenuItemList;
        this.itemIndexes = itemIndexes;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        charLvlSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, App.settings.getMaxLevel()));
        charLvlSpinner.getValueFactory().setValue(0);
        readLvlSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, App.settings.getMaxLevel()));
        readLvlSpinner.getValueFactory().setValue(0);
        meanLvlSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, App.settings.getMaxLevel()));
        meanLvlSpinner.getValueFactory().setValue(0);

        charDP.setValue(LocalDate.now().plusYears(100));
        readDP.setValue(LocalDate.now().plusYears(100));
        meanDP.setValue(LocalDate.now().plusYears(100));

        for (int i = 0; i < mainGrid.getRowCount(); i++) {
            CheckBox checkBox = (CheckBox) getFromGridPane(0, i);
            Node node = getFromGridPane(1, i);
            node.setDisable(true);
            checkBox.selectedProperty().addListener(((observableValue, oldValue, newValue) -> {
                node.setDisable(!newValue);
            }));
        }
    }

    @FXML
    private void saveButtonAction(ActionEvent actionEvent) {
        for (int i : itemIndexes) {
            KanjiMenuItem kmi = kanjiMenuItemList.get(i);
            if (!onField.isDisabled())
                kmi.setOnReading(onField.getText());
            if (!kunField.isDisabled())
                kmi.setKunReading(kunField.getText());
            if (!meanField.isDisabled())
                kmi.setMeaning(meanField.getText());
            if (!mnemField.isDisabled())
                kmi.setMnemonic(mnemField.getText());
            if (!charLvlSpinner.isDisabled())
                kmi.setCharacterLevel(charLvlSpinner.getValue());
            if (!readLvlSpinner.isDisabled())
                kmi.setReadingLevel(readLvlSpinner.getValue());
            if (!meanLvlSpinner.isDisabled())
                kmi.setMeaningLevel(meanLvlSpinner.getValue());
            if (!charDP.isDisabled())
                kmi.setCharacterDue(charDP.getValue());
            if (!readDP.isDisabled())
                kmi.setReadingDue(readDP.getValue());
            if (!meanDP.isDisabled())
                kmi.setMeaningDue(meanDP.getValue());
        }
        ((Stage) onField.getScene().getWindow()).close();
    }

    private Node getFromGridPane(int column, int row) {
        int count = 0;
        for (Node node : mainGrid.getChildren()) {
            System.out.println(count++);
            if (GridPane.getColumnIndex(node) == column && GridPane.getRowIndex(node) == row) {
                return node;
            }
        }
        return null;
    }
}