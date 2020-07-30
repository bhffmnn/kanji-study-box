/*
 * Copyright (c) 2020, Benjamin Hoffmann
 * All rights reserved.

 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree.
 */

package de.bhffmnn.models;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

/**
 * Represents a dictionary of Vocable objects. There can only be one Vocable object per form inside the dictionary.
 */
public class VocableDictionary extends AbstractCollection<Vocable> implements Cloneable {
    private ArrayList<Vocable> dictionary;

    public VocableDictionary() {
        dictionary = new ArrayList<>();
    }

    public VocableDictionary (String filePath) throws IOException {
        dictionary = new ArrayList<Vocable>();
        BufferedReader dictReader = new BufferedReader(new InputStreamReader(
                new FileInputStream(filePath), StandardCharsets.UTF_8));
        dictReader.readLine(); //skip header
        String row;
        while ((row = dictReader.readLine()) != null) {
            String[] fields = row.split("\t", -1);
            if (!dictionary.add(new Vocable(fields[0], fields[1], fields[2], fields[3]))) {
                System.out.print("Duplicate: " + fields);
            }
        }
        dictReader.close();
    }

    @Override
    public Iterator<Vocable> iterator() {
        return dictionary.iterator();
    }

    @Override
    public int size() {
        return dictionary.size();
    }

    /**
     * Get the Vocable object at a specified position inside the dictionary.
     * @param i The position of the Vocable object
     * @return The Vocable object at position i
     */
    public Vocable getByIndex(int i) {
        return dictionary.get(i);
    }

    /**
     * Resets the dictionary field and reloads it from a specified file path.
     * @param filePath Path to the dictionary file
     * @throws IOException
     */
    public void reload(String filePath) throws IOException{
        dictionary = new ArrayList<Vocable>();
        BufferedReader dictReader = new BufferedReader(new InputStreamReader(
                new FileInputStream(filePath), StandardCharsets.UTF_8));
        if ((dictReader.readLine()) != null) {
            //skip header
        }
        String row;
        while ((row = dictReader.readLine()) != null) {
            String[] fields = row.split("\t");
            if (!dictionary.add(new Vocable(fields[0], fields[1], fields[2], fields[3]))) {
                System.out.print("Duplicate: " + fields);
            }
        }
        dictReader.close();
    }

    /**
     * Returns a VocableDictionary starting and ending at the specified dictionary positions
     * @param start Staring index
     * @param stop Ending index (exclusive)
     * @return VocableDictionary object starting and ending at the specified dictionary positions
     */
    public VocableDictionary getByRange(int start, int stop) {
        VocableDictionary vocableDictionary = new VocableDictionary();
        for (int index = start; index < stop; index++) {
            vocableDictionary.add(dictionary.get(index));
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

    /**
     * Gets the vocables with a form containing the specified character
     * @param character Character with shall be contained in the vocables
     * @return VocableDictionary with vocables containing the specified character
     */
    public VocableDictionary getByCharacter(char character) {
        VocableDictionary vocableDictionary = new VocableDictionary();
        for (Vocable vocable : this.dictionary) {
            if (vocable.getForm().indexOf(character) != -1) {
                vocableDictionary.add(vocable);
            }
        }
        return vocableDictionary;
    }

    /**
     * Saves the dictionary to a specified file path
     * @param filePath The file path to which the dictionary shall be saved to
     * @throws IOException
     */
    public void save(String filePath) throws IOException {
        OutputStream outputStream = new FileOutputStream(filePath);
        BufferedWriter dictWriter = new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));
        dictWriter.write("form\treading\tmeaning\texample\tlevel\tdue\n");
        for (Vocable vocable : dictionary) {
            dictWriter.write(vocable.toString() + "\n");
        }
        dictWriter.flush();
        dictWriter.close();
    }

    /**
     * Adds a Vocable object to the dictionary. This will only add a Vocable if no other Vocable with the same form is
     * contained in the dictionary already.
     * @param vocable The vocable that shall be added to the dictionary
     * @return True if added
     */
    @Override
    public boolean add(Vocable vocable) {
        for (Vocable v : dictionary) {
            if (v.getForm().equals(vocable.getForm())) {
                return false;
            }
        }
        dictionary.add(vocable);
        return true;
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

    /**
     * Returns a deep clone of this object. Vocable objects in this deep clone can be altered without affecting the
     * original.
     *
     * @return Deep clone of this object
     */
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
                                            v.getExample()));
        }
        return cloneDictionary;
    }

    /**
     * Randomizes the order of the vocables in the dictionary.
     * This is not meant to be used on the global App.vocableDictionary, only for clones/subsets used in Training and
     * Learning views.
     */
    public void shuffle() {
        Collections.shuffle(dictionary);
    }
}