/*
 * Copyright (c) 2020, Benjamin Hoffmann
 * All rights reserved.

 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree.
 */

package de.bhffmnn;

import java.io.*;

public class Settings {
    private String kanjiDictionaryFilePath; //loaded from settings
    private String vocableDictionaryFilePath;
    private String[][] studyDirectionsKanji; //currently hard coded
    private String[][] studyDirectionsVocab; //currently hard coded

    public Settings() {
        String[] answersCharacter = {"character", "vocab", "mnemonic", "level", "due"};
        String[] answersReadingKanji = {"on", "kun", "meaning", "vocab", "mnemonic", "level", "due"};
        String[] answersMeaningKanji = {"on", "kun", "meaning", "vocab", "mnemonic", "level", "due"};

        String[] answersFormVocab = {"form", "example", "level", "due"};
        String[] answersReadingVocab = {"reading", "example", "meaning", "level", "due"};

        kanjiDictionaryFilePath = "";
        vocableDictionaryFilePath = "";
        studyDirectionsKanji = new String[][]{answersCharacter, answersReadingKanji, answersMeaningKanji};
        studyDirectionsVocab = new String[][]{answersFormVocab, answersReadingVocab};
    }

    static Settings loadSettings() {
        Settings settings = new Settings();
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader("settings"));
            settings.kanjiDictionaryFilePath = bufferedReader.readLine();
            settings.vocableDictionaryFilePath = bufferedReader.readLine();
        }
        catch(Exception e) {
            System.out.println(e);
            return null;
        }
        return settings;
    }

    public void save() throws IOException {
        PrintWriter writer = new PrintWriter(new FileOutputStream("settings", false));
        writer.print(kanjiDictionaryFilePath + "\n" + vocableDictionaryFilePath);
        writer.close();
    }

    public void setKanjiDictionaryFilePath(String filePath) {
        kanjiDictionaryFilePath = filePath;
    }

    public void setVocableDictionaryFilePath(String vocableDictionaryFilePath) {
        this.vocableDictionaryFilePath = vocableDictionaryFilePath;
    }

    public String[][] getStudyDirectionsKanji() {
        return studyDirectionsKanji;
    }

    public String[][] getStudyDirectionsVocab() {
        return studyDirectionsVocab;
    }

    public String getKanjiDictionaryFilePath() {
        return kanjiDictionaryFilePath;
    }

    public String getVocableDictionaryFilePath() {
        return vocableDictionaryFilePath;
    }
}
