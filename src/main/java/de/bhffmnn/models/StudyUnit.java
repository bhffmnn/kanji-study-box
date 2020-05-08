/*
 * Copyright (c) 2020, Benjamin Hoffmann
 * All rights reserved.

 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree.
 */

package de.bhffmnn.models;

import java.time.LocalDate;

public abstract class StudyUnit {
    private String form;
    private String meaning;
    private Integer level;
    private LocalDate due;

    public StudyUnit(String form, String meaning, Integer level, LocalDate due) {
        this.form = form;
        this.meaning = meaning;
        this.level = level;
        this.due = due;
    }

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

    @Override
    public boolean equals(Object obj) {
        if (!obj.getClass().equals(this.getClass())) {
            return false;
        }
        return this.form.equals((this.getClass().cast(obj).getForm()));
    }

    @Override
    public abstract String toString();
}

