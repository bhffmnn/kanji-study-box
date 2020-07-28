/*
 * Copyright (c) 2020, Benjamin Hoffmann
 * All rights reserved.

 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree.
 */

package de.bhffmnn.controllers.menu;

import de.bhffmnn.models.Kanji;
import de.bhffmnn.models.KanjiDictionary;
import de.bhffmnn.controllers.menu.EditKanjiDictionaryController.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for the AddKanji view that lets the user add a kanji to a dictionary.
 */

public class AddKanjiMenuItemController implements Initializable {
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

    private KanjiMenuItemList kanjiMenuItemList;

    /**
     *
     * @param kanjiMenuItemList The list that the new item should be added to
     */
    public AddKanjiMenuItemController(KanjiMenuItemList kanjiMenuItemList) {
        this.kanjiMenuItemList = kanjiMenuItemList;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }


    @FXML
    private void addButtonAction(ActionEvent actionEvent) {
        if (charField.getText().length() == 0) {
            Kanji kanji = new Kanji(charField.getText().charAt(0), onField.getText(), kunField.getText(), meanField.getText(),
                    mnemField.getText());
            if (kanjiMenuItemList.addNewItem(kanji)) {
                ((Stage) charField.getScene().getWindow()).close();
            } else {
                Alert numberAlert = new Alert(Alert.AlertType.INFORMATION, "A kanji with this character already exists.");
                numberAlert.setHeaderText("");
                numberAlert.show();
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