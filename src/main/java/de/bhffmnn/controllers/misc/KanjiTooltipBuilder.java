/*
 * Copyright (c) 2020, Benjamin Hoffmann
 * All rights reserved.

 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree.
 */

package de.bhffmnn.controllers.misc;

import de.bhffmnn.App;
import de.bhffmnn.models.Kanji;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.util.Duration;

import java.io.IOException;
import java.io.InputStream;

public class KanjiTooltipBuilder {
    public static Tooltip kanjiTooltip(Kanji kanji) {
        Label strokeChar = new Label(kanji.getCharacter());
        try {
            InputStream fontStream = App.class.getResourceAsStream("fonts/KanjiStrokeOrders.ttf");
            if (fontStream != null) {
                Font strokeFont = Font.loadFont(fontStream, 200);
                fontStream.close();

                strokeChar.setFont(strokeFont);
            }
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
        }

        GridPane featureGrid = new GridPane();
        featureGrid.setAlignment(Pos.CENTER_LEFT);

        featureGrid.add(new Label("Character:"), 0, 0);
        featureGrid.add(new Label("ON-Reading:"), 0, 1);
        featureGrid.add(new Label("kun-Reading:"), 0, 2);
        featureGrid.add(new Label("Meaning:"), 0, 3);
        featureGrid.add(new Label("Level:"), 0, 4);
        featureGrid.add(new Label(kanji.getCharacter()), 1,0);
        featureGrid.add(new Label(kanji.getOnReading()),  1,1);
        featureGrid.add(new Label(kanji.getKunReading()),1, 2);
        featureGrid.add(new Label(kanji.getMeaning()), 1, 3);
        featureGrid.add(new Label(kanji.getCharacterLevel().toString()), 1, 4);

        HBox hBox = new HBox();
        hBox.getChildren().addAll(strokeChar, featureGrid);

        Tooltip tooltip = new Tooltip();
        tooltip.graphicProperty().setValue(hBox);
        tooltip.setShowDuration(Duration.INDEFINITE);
        tooltip.setFont(Font.font(16));
        return tooltip;
    }
}
