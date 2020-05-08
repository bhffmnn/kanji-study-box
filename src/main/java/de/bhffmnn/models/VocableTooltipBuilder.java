/*
 * Copyright (c) 2020, Benjamin Hoffmann
 * All rights reserved.

 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree.
 */

package de.bhffmnn.models;

import de.bhffmnn.App;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.util.Duration;

import java.util.ArrayList;
//TODO
public class VocableTooltipBuilder {
    /*
    public static Tooltip vocableTooltip(String word) {
        ArrayList<Vocable> vocableList = new ArrayList<>();

        for (String letter : word.split("")) {
            Kanji vocable;
            if ((vocable = App.vocableDictionary.(letter)) != null) {
                vocableList.add(vocable);
            }
        }
        GridPane gridPane = new GridPane();
        int y = 0;
        for (Vocable vocable : vocableList) {
            gridPane.add(new Label("Form:"), y, 0);
            gridPane.add(new Label("Reading:"), y, 1);
            gridPane.add(new Label("Meaning:"), y, 2);
            gridPane.add(new Label("Level:"), y, 3);
            gridPane.add(new Label(vocable.getForm()), y + 1,0);
            gridPane.add(new Label(vocable.getReading()),  y + 1,1);
            gridPane.add(new Label(vocable.getMeaning()),y + 1, 2);
            gridPane.add(new Label(vocable.getLevel().toString()), y + 1, 3);
            y += 2;
        }

        Tooltip tooltip = new Tooltip();
        tooltip.graphicProperty().setValue(gridPane);
        tooltip.setShowDuration(Duration.INDEFINITE);
        tooltip.setFont(Font.font(16));
        return tooltip;
    }

     */
}
