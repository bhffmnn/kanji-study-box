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

    public Vocable(String form, String reading, String meaning, String example) {
        this.form = form;
        this.reading = reading;
        this.meaning = meaning;
        this.example = example;
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

    public String getForm() {
        return form;
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

    /**
     *
     * @return Fields separated by tabs as String
     */
    @Override
    public String toString() {
        return form + "\t" + reading + "\t" + meaning + "\t" + example;
    }
}
