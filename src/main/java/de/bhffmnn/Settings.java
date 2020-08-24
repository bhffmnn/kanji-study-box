/*
 * Copyright (c) 2020, Benjamin Hoffmann
 * All rights reserved.

 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree.
 */

package de.bhffmnn;

import java.io.*;
import java.util.prefs.Preferences;


/**
 * Class containing the settings of the application. That includes the file paths to the dictionaries.
 */

public class Settings {
    private int[] levels;
    private Preferences preferences;
    private String[][] studyDirectionsKanji; //currently hard coded
    private String[][] studyDirectionsVocab; //currently hard coded

    public Settings() {
        levels = new int[]{1, 2, 3, 5, 8, 13, 21, 34, 55, 89, 144, 233, 377, 610};
        String[] answersCharacter = {"character", "vocab", "mnemonic", "level", "due"};
        String[] answersReadingKanji = {"on", "kun", "meaning", "vocab", "mnemonic", "level", "due"};
        String[] answersMeaningKanji = {"on", "kun", "meaning", "vocab", "mnemonic", "level", "due"};

        preferences = Preferences.userRoot().node(Settings.class.getName());
        studyDirectionsKanji = new String[][]{answersCharacter, answersReadingKanji, answersMeaningKanji};
    }

    public int getSpacingByLevel(int level) {
        return levels[level - 1];
    }

    public int getMaxLevel() {
        return levels.length;
    }

    public void setKanjiDictionaryFilePath(String filePath) {
        preferences.put("kanjiDictionaryFilePath", filePath);
    }


    public void setVocableDictionaryFilePath(String filePath) {
        preferences.put("vocableDictionaryFilePath", filePath);
    }

    public String[][] getStudyDirectionsKanji() {
        return studyDirectionsKanji;
    }

    public String getKanjiDictionaryFilePath() {
        return preferences.get("kanjiDictionaryFilePath", "");
    }

    public String getVocableDictionaryFilePath() {
        return preferences.get("vocableDictionaryFilePath", "");
    }
}
