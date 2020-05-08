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
    public static Tooltip kanjiTooltip(String word) {
        ArrayList<Kanji> kanjiList = new ArrayList<>();

        for (String letter : word.split("")) {
            Kanji kanji;
            if ((kanji = App.kanjiDictionary.getKanjiByCharacter(letter)) != null) {
                kanjiList.add(kanji);
            }
        }
        GridPane gridPane = new GridPane();
        int y = 0;
        for (Kanji kanji : kanjiList) {
            gridPane.add(new Label("Character:"), y, 0);
            gridPane.add(new Label("ON-Reading:"), y, 1);
            gridPane.add(new Label("kun-Reading:"), y, 2);
            gridPane.add(new Label("Meaning:"), y, 3);
            gridPane.add(new Label("Level:"), y, 4);
            gridPane.add(new Label(kanji.getCharacter()), y + 1,0);
            gridPane.add(new Label(kanji.getOnReading()),  y + 1,1);
            gridPane.add(new Label(kanji.getKunReading()),y + 1, 2);
            gridPane.add(new Label(kanji.getMeaning()), y + 1, 3);
            gridPane.add(new Label(kanji.getCharacterLevel().toString()), y + 1, 4);
            y += 2;
        }

        Tooltip tooltip = new Tooltip();
        tooltip.graphicProperty().setValue(gridPane);
        tooltip.setShowDuration(Duration.INDEFINITE);
        tooltip.setFont(Font.font(16));
        return tooltip;
    }
}
