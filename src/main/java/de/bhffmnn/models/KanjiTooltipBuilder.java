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

public class KanjiTooltipBuilder {
    public static Tooltip kanjiTooltip(Kanji kanji) {
        GridPane gridPane = new GridPane();
        gridPane.add(new Label("Character:"), 0, 0);
        gridPane.add(new Label("ON-Reading:"), 0, 1);
        gridPane.add(new Label("kun-Reading:"), 0, 2);
        gridPane.add(new Label("Meaning:"), 0, 3);
        gridPane.add(new Label("Level:"), 0, 4);
        gridPane.add(new Label(kanji.getCharacter()), 1,0);
        gridPane.add(new Label(kanji.getOnReading()),  1,1);
        gridPane.add(new Label(kanji.getKunReading()),1, 2);
        gridPane.add(new Label(kanji.getMeaning()), 1, 3);
        gridPane.add(new Label(kanji.getCharacterLevel().toString()), 1, 4);

        Tooltip tooltip = new Tooltip();
        tooltip.graphicProperty().setValue(gridPane);
        tooltip.setShowDuration(Duration.INDEFINITE);
        tooltip.setFont(Font.font(16));
        return tooltip;
    }
}
