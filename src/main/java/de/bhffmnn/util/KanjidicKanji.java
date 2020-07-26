/*
 * Copyright (c) 2020, Benjamin Hoffmann
 * All rights reserved.

 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree.
 */
package de.bhffmnn.util;

import de.bhffmnn.models.Kanji;

import java.util.ArrayList;
import java.util.HashMap;

public class KanjidicKanji {
    private char character;
    private ArrayList<String> onReading;
    private ArrayList<String> kunReading;
    private ArrayList<String> meaning;
    private HashMap<DictionaryType, Integer> dictNumbers;
    private int jlpt;

    public KanjidicKanji(char character) {
        this.character = character;
    }

    public KanjidicKanji(char character, ArrayList<String> onReading, ArrayList<String> kunReading, ArrayList<String> meaning, int jlpt, HashMap<DictionaryType, Integer> dictNumbers) {
        this.character = character;
        this.onReading = onReading;
        this.kunReading = kunReading;
        this.meaning = meaning;
        this.jlpt = jlpt;
        this.dictNumbers = dictNumbers;
    }

    public int getJlpt() {
        return jlpt;
    }

    public Integer getDictionaryNumber(DictionaryType dictType) {
        return dictNumbers.get(dictType);
    }

    public void setCharacter(char character) {
        this.character = character;
    }

    public void setMeaning(ArrayList<String> meaning) {
        this.meaning = meaning;
    }

    public void setOnReading(ArrayList<String> onReading) {
        this.onReading = onReading;
    }

    public void setKunReading(ArrayList<String> kunReading) {
        this.kunReading = kunReading;
    }

    public void setJlpt(int jlpt) {
        this.jlpt = jlpt;
    }

    public char getCharacter() {
        return character;
    }

    public ArrayList<String> getKunReading() {
        return kunReading;
    }

    public ArrayList<String> getMeaning() {
        return meaning;
    }

    public Kanji toKanji() {
        return new Kanji(String.valueOf(this.character), arrayToString(this.onReading, "、"), arrayToString(this.kunReading, "、"),
                         arrayToString(this.meaning, ", "), "");
    }

    private String arrayToString(ArrayList<String> featureArray, String separator) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String s : featureArray) {
            if (stringBuilder.length() == 0) {
                stringBuilder.append(s);
            }
            else {
                stringBuilder.append(separator + s);
            }
        }
        return stringBuilder.toString();
    }


    @Override
    public String toString() {
        return character + "\t" +
                onReading + "\t" +
                kunReading + "\t" +
                meaning + "\t" +
                jlpt;
    }
}


