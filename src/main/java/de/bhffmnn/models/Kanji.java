/*
 * Copyright (c) 2020, Benjamin Hoffmann
 * All rights reserved.

 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree.
 */

package de.bhffmnn.models;

import de.bhffmnn.App;

import java.time.LocalDate;

/**
 * Represents a kanji
 */

public class Kanji {
    private char character;
    private String onReading;
    private String kunReading;
    private String meaning;
    private String mnemonic;

    private Integer characterLevel;
    private Integer readingLevel;
    private Integer meaningLevel;

    private LocalDate characterDue;
    private LocalDate readingDue;
    private LocalDate meaningDue;

    public Kanji(char character, String onReading, String kunReading, String meaning,
                 String mnemonic) {
        this.character = character;
        this.onReading = onReading;
        this.kunReading = kunReading;
        this.meaning = meaning;
        this.mnemonic = mnemonic;
        this.characterLevel = 0;
        this.readingLevel = 0;
        this.meaningLevel = 0;
        this.characterDue = LocalDate.now().plusYears(100);
        this.readingDue = LocalDate.now().plusYears(100);
        this.meaningDue = LocalDate.now().plusYears(100);
    }

    public Kanji(char character, String onReading, String kunReading, String meaning,
                 String mnemonic, Integer characterLevel, LocalDate characterDue,
                 Integer readingLevel, LocalDate readingDue, Integer meaningLevel, LocalDate meaningDue) {
        this.character = character;
        this.onReading = onReading;
        this.kunReading = kunReading;
        this.meaning = meaning;
        this.mnemonic = mnemonic;
        this.characterLevel = characterLevel;
        this.readingLevel = readingLevel;
        this.meaningLevel = meaningLevel;
        this.characterDue = characterDue;
        this.readingDue = readingDue;
        this.meaningDue = meaningDue;
    }

    public void setCharacter(char character) {
        this.character = character;
    }

    public void setCharacterLevel(Integer characterLevel) {
        this.characterLevel = characterLevel;
    }

    public void setKunReading(String kunReading) {
        this.kunReading = kunReading;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }

    public void setMnemonic(String mnemonic) {
        this.mnemonic = mnemonic;
    }

    public void setOnReading(String onReading) {
        this.onReading = onReading;
    }

    /**
     * Sets a new level for the character and updates its due time accordingly.
     * @param newLevel The new level for the character
     */
    public void updateCharacterLevel(int newLevel){
        if (newLevel > 0 && newLevel <= App.settings.getMaxLevel()) {
            this.characterDue = LocalDate.now().plusDays(App.settings.getSpacingByLevel(newLevel));
            this.characterLevel = newLevel;
        }
    }

    /**
     * Sets a new level for the reading and updates its due time accordingly.
     * @param newLevel The new level for the reading
     */
    public void updateReadingLevel(int newLevel){
        if (newLevel > 0 && newLevel <= App.settings.getMaxLevel()) {
            this.readingDue = LocalDate.now().plusDays(App.settings.getSpacingByLevel(newLevel));
            this.readingLevel = newLevel;
        }
    }

    /**
     * Sets a new level for the meaning and updates its due time accordingly.
     * @param newLevel The new level for the meaning
     */
    public void updateMeaningLevel(int newLevel){
        if (newLevel > 0 && newLevel <= App.settings.getMaxLevel()) {
            this.meaningDue = LocalDate.now().plusDays(App.settings.getSpacingByLevel(newLevel));
            this.meaningLevel = newLevel;
        }
    }

    /**
     *
     * @return True if the character's due time is today or earlier
     */
    public boolean isCharacterDue() {
        if (this.characterDue.compareTo(LocalDate.now()) <= 0) {
            return true;
        }
        else {
            return false;
        }
    }

    /**
     *
     * @return True if the reading's due time is today or earlier
     */
    public boolean isReadingDue() {
        if (this.readingDue.compareTo(LocalDate.now()) <= 0) {
            return true;
        }
        else {
            return false;
        }
    }

    /**
     *
     * @return True if the meaning's due time is today or earlier
     */
    public boolean isMeaningDue() {
        if (this.meaningDue.compareTo(LocalDate.now()) <= 0) {
            return true;
        }
        else {
            return false;
        }
    }

    public Integer getCharacterLevel() {
        return characterLevel;
    }

    public char getCharacter() {
        return character;
    }

    public Integer getReadingLevel() {
        return readingLevel;
    }

    public Integer getMeaningLevel() {
        return meaningLevel;
    }

    public LocalDate getCharacterDue() {
        return characterDue;
    }

    public LocalDate getReadingDue() {
        return readingDue;
    }

    public LocalDate getMeaningDue() {
        return meaningDue;
    }

    public String getKunReading() {
        return kunReading;
    }

    public String getMeaning() {
        return meaning;
    }

    public String getMnemonic() {
        return mnemonic;
    }

    public String getOnReading() {
        return onReading;
    }

    public void setCharacterLevel(int characterLevel) {
        this.characterLevel = characterLevel;
    }

    public void setReadingLevel(Integer readingLevel) {
        this.readingLevel = readingLevel;
    }

    public void setMeaningLevel(Integer meaningLevel) {
        this.meaningLevel = meaningLevel;
    }

    public void setCharacterDue(LocalDate characterDue) {
        this.characterDue = characterDue;
    }

    public void setReadingDue(LocalDate readingDue) {
        this.readingDue = readingDue;
    }

    public void setMeaningDue(LocalDate meaningDue) {
        this.meaningDue = meaningDue;
    }

    /**
     *
     * @return Fields separated by tabs as String
     */
    @Override
    public String toString() {
        return character + "\t" +
                onReading + "\t" +
                kunReading + "\t" +
                meaning + "\t" +
                mnemonic + "\t" +
                characterLevel.toString() + "\t" +
                characterDue.toString() + "\t" +
                readingLevel.toString() + "\t" +
                readingDue.toString() + "\t" +
                meaningLevel.toString() + "\t" +
                meaningDue.toString();
    }
}
