/*
 * Copyright (c) 2020, Benjamin Hoffmann
 * All rights reserved.

 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree.
 */

package de.bhffmnn.models;

import java.io.*;
import java.time.LocalDate;
import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class VocableDictionary extends AbstractCollection<Vocable> implements Cloneable {
    private ArrayList<Vocable> dictionary;

    public VocableDictionary() {
        dictionary = new ArrayList<>();
    }

    public VocableDictionary (String filePath) throws IOException {
        dictionary = new ArrayList<Vocable>();
        BufferedReader csvReader = new BufferedReader(new FileReader(filePath));
        csvReader.readLine(); //skip header
        String row;
        while ((row = csvReader.readLine()) != null) {
            String[] fields = row.split("\t");
            if (!dictionary.add(new Vocable(fields[0], fields[1], fields[2], fields[3], Integer.parseInt(fields[4]), LocalDate.parse(fields[5])))) {
                System.out.print("Duplicate: " + fields);
            }
        }
        csvReader.close();
    }

    @Override
    public Iterator<Vocable> iterator() {
        return dictionary.iterator();
    }

    @Override
    public int size() {
        return dictionary.size();
    }

    public Vocable getByIndex(int i) {
        return dictionary.get(i);
    }

    public void reload(String filePath) throws IOException{
        dictionary = new ArrayList<Vocable>();
        BufferedReader csvReader = new BufferedReader(new FileReader(filePath));
        if ((csvReader.readLine()) != null) {
            //skip header
        }
        String row;
        while ((row = csvReader.readLine()) != null) {
            String[] fields = row.split("\t");
            if (!dictionary.add(new Vocable(fields[0], fields[1], fields[2], fields[3], Integer.parseInt(fields[4]), LocalDate.parse(fields[5])))) {
                System.out.print("Duplicate: " + fields);
            }
        }
        csvReader.close();
    }

    public VocableDictionary getByRange(int start, int stop) {
        VocableDictionary vocableDictionary = new VocableDictionary();
        for (int index = start; index < stop; index++) {
            vocableDictionary.add(dictionary.get(index));
        }
        return vocableDictionary;
    }

    public VocableDictionary getDue() {
        VocableDictionary vocableDictionary = new VocableDictionary();
        for (Vocable vocable : dictionary) {
            if (vocable.isDue()) {
                vocableDictionary.add(vocable);
            }
        }
        return vocableDictionary;
    }

    public VocableDictionary getByLevel(int level) {
        VocableDictionary vocableDictionary = new VocableDictionary();
        for (Vocable vocable : dictionary) {
            if (vocable.getLevel() == level) {
                vocableDictionary.add(vocable);
            }
        }
        return vocableDictionary;
    }

    public Vocable getByForm(String form) {
        for (Vocable vocable : dictionary) {
            if (vocable.getForm().equals(form))
                return vocable;
        }
        return null;
    }

    public VocableDictionary getByCharacter(String character) {
        VocableDictionary vocableDictionary = new VocableDictionary();
        for (Vocable vocable : this.dictionary) {
            if (vocable.getForm().contains(character)) {
                vocableDictionary.add(vocable);
            }
        }
        return vocableDictionary;
    }

    public void save(String filePath) throws IOException {
        PrintWriter csvWriter = new PrintWriter(new FileOutputStream(filePath, false));
        StringBuilder csvStringBuilder = new StringBuilder();
        csvStringBuilder.append("form\treading\tmeaning\texample\tlevel\tdue\n");
        for (Vocable vocable : dictionary) {
            csvStringBuilder.append(vocable.toString() + "\n");
        }
        String csvString = csvStringBuilder.toString();
        System.out.println(csvString);
        csvWriter.print(csvString);
        csvWriter.close();
    }

    @Override
    public boolean add(Vocable vocable) {
        if (dictionary.contains(vocable)) {
            return false;
        }
        else {
            dictionary.add(vocable);
            return true;
        }
    }

    public VocableDictionary clone() {
        VocableDictionary cloneDictionary;
        try {
            cloneDictionary = (VocableDictionary) super.clone();
        }
        catch (CloneNotSupportedException e) {
            System.out.println(e.getMessage());
            return cloneDictionary = new VocableDictionary();
        }
        cloneDictionary.dictionary = new ArrayList<Vocable>();
        cloneDictionary.addAll(dictionary);
        return cloneDictionary;
    }

    public VocableDictionary cloneDeep() {
        VocableDictionary cloneDictionary;
        try {
            cloneDictionary = (VocableDictionary) super.clone();
        }
        catch (CloneNotSupportedException e) {
            System.out.println(e.getMessage());
            return cloneDictionary = new VocableDictionary();
        }
        cloneDictionary.dictionary = new ArrayList<Vocable>();
        for (Vocable v : this.dictionary) {
            cloneDictionary.add(new Vocable(v.getForm(), v.getReading(), v.getMeaning(),
                                            v.getExample(), v.getLevel(), v.getDue()));
        }
        return cloneDictionary;
    }
    public void shuffle() {
        Collections.shuffle(dictionary);
    }
}