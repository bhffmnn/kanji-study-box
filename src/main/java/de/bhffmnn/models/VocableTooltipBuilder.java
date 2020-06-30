/*
 * Copyright (c) 2020, Benjamin Hoffmann
 * All rights reserved.

 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree.
 */

package de.bhffmnn.models;

import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.util.Duration;

//TODO
public class VocableTooltipBuilder {
    public static Tooltip vocableTooltip(Vocable vocable) {

        GridPane gridPane = new GridPane();
        gridPane.add(new Label("Form:"), 0, 0);
        gridPane.add(new Label("Reading:"), 0, 1);
        gridPane.add(new Label("Meaning:"), 0, 2);
        gridPane.add(new Label("Level:"), 0, 3);
        gridPane.add(new Label(vocable.getForm()), 1,0);
        gridPane.add(new Label(vocable.getReading()),1,1);
        gridPane.add(new Label(vocable.getMeaning()),1, 2);
        gridPane.add(new Label(vocable.getLevel().toString()), 1, 3);

        Tooltip tooltip = new Tooltip();
        tooltip.graphicProperty().setValue(gridPane);
        tooltip.setShowDuration(Duration.INDEFINITE);
        tooltip.setFont(Font.font(16));
        return tooltip;
    }
}