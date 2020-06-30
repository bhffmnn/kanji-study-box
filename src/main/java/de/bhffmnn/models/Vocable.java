/*
 * Copyright (c) 2020, Benjamin Hoffmann
 * All rights reserved.

 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree.
 */

package de.bhffmnn.models;

import java.time.LocalDate;

/**
 * Represents a vocable
 */

public class Vocable {
    private String form;
    private String reading;
    private String meaning;
    private String example;
    private Integer level;
    private LocalDate due;

    public Vocable(String form, String reading, String meaning, String example) {
        this.form = form;
        this.reading = reading;
        this.meaning = meaning;
        this.example = example;
        this.level = 0;
        this.due = LocalDate.now().plusYears(100);
    }

    public Vocable(String form, String reading, String meaning, String example, Integer level, LocalDate due) {
        this.form = form;
        this.reading = reading;
        this.meaning = meaning;
        this.example = example;
        this.level = level;
        this.due = due;
    }

    /**
     * Sets a new level for the vocable and updates its due time accordingly.
     * @param newLevel The new level for the vocable
     */
    public void updateLevel(int newLevel){
        if (newLevel == 1) {
            this.due = LocalDate.now().plusDays(1);
        }
        else if (newLevel == 2) {
            this.due = LocalDate.now().plusDays(2);
        }
        else if (newLevel == 3) {
            this.due = LocalDate.now().plusDays(4);
        }
        else if (newLevel == 4) {
            this.due = LocalDate.now().plusWeeks(1);
        }
        else if (newLevel == 5) {
            this.due = LocalDate.now().plusWeeks(2);
        }
        else if (newLevel == 6) {
            this.due = LocalDate.now().plusWeeks(4);
        }
        else if (newLevel == 7) {
            this.due = LocalDate.now().plusWeeks(8);
        }
        else if (newLevel == 8) {
            this.due = LocalDate.now().plusWeeks(16);
        }
        else if (newLevel == 9) {
            this.due = LocalDate.now().plusWeeks(32);
        }
        else if (newLevel == 10) {
            this.due = LocalDate.now().plusWeeks(64);
        }
        else {
            this.due = LocalDate.now().plusYears(100);
        }
        this.level = newLevel;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setForm(String form) {
        this.form = form;
    }

    public void setReading(String reading) {
        this.reading = reading;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }

    public void setExample(String example) {
        this.example = example;
    }

    public void setDue(LocalDate due) {
        this.due = due;
    }

    /**
     *
     * @return True if the vocable's due time is today or earlier
     */
    public boolean isDue() {
        if (this.due.compareTo(LocalDate.now()) <= 0) {
            return true;
        }
        else {
            return false;
        }
    }

    public Integer getLevel() {
        return level;
    }

    public String getForm() {
        return form;
    }

    public LocalDate getDue() {
        return due;
    }

    public String getExample() {
        return example;
    }

    public String getReading() {
        return reading;
    }

    public String getMeaning() {
        return meaning;
    }

    //TODO: This is semantically wrong and should be removed. For this, the add method of VocableDictionary has to be changed
    @Override
    public boolean equals(Object obj) {
        if (!obj.getClass().equals(this.getClass())) {
            return false;
        }
        return (form.equals(((Vocable) obj).getForm()));
    }

    /**
     *
     * @return Fields separated by tabs as String
     */
    @Override
    public String toString() {
        return form + "\t" + reading + "\t" + meaning + "\t" + example + "\t" + level.toString() + "\t" + due.toString();
    }
}
