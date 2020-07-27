/*
 * Copyright (c) 2020, Benjamin Hoffmann
 * All rights reserved.

 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree.
 */

package de.bhffmnn.models;

import java.io.*;
import java.time.LocalDate;
import java.util.*;

/**
 * Represents a dictionary of Kanji objects. There can only be one Kanji object per character inside the dictionary.
 */
public class KanjiDictionary extends AbstractCollection<Kanji> implements Cloneable {
    private ArrayList<Kanji> dictionary;


    public KanjiDictionary() {
        dictionary = new ArrayList<Kanji>();
    }

    public KanjiDictionary (String filePath) throws IOException {
        dictionary = new ArrayList<Kanji>();
        try {
            BufferedReader csvReader = new BufferedReader(new FileReader(filePath));
            csvReader.readLine(); //skip header
            String row;
            while ((row = csvReader.readLine()) != null) {
                String[] fields = row.split("\t");
                if (!add(new Kanji(fields[0].charAt(0), fields[1], fields[2], fields[3],
                        fields[4], Integer.parseInt(fields[5]), LocalDate.parse(fields[6]),
                        Integer.parseInt(fields[7]), LocalDate.parse(fields[8]), Integer.parseInt(fields[9]), LocalDate.parse(fields[10])))) {
                    System.out.print("Duplicate kanji: " + fields);
                }
            }
            csvReader.close();
        }
        catch (ArrayIndexOutOfBoundsException e) {
            throw new IOException(e);
        }
    }

    public ArrayList<Kanji> getDictionaryAsArrayList() {
        return dictionary;
    }


    @Override
    public Iterator<Kanji> iterator() {
        return dictionary.iterator();
    }

    @Override
    public int size() {
        return dictionary.size();
    }

    /**
     * Get the Kanji object at a specified position inside the dictionary.
     * @param i The position of the Kanji object
     * @return The Kanji object at position i
     */
    public Kanji getByIndex(int i) {
        return dictionary.get(i);
    }

    /**
     * Resets the dictionary field and reloads it from a specified file path.
     * @param filePath Path to the dictionary file
     * @throws IOException
     */
    public void reload(String filePath) throws IOException {
        dictionary = new ArrayList<Kanji>();
        BufferedReader csvReader = new BufferedReader(new FileReader(filePath));
        if ((csvReader.readLine()) != null) {
            //skip header
        }
        String row;
        while ((row = csvReader.readLine()) != null) {
            String[] fields = row.split("\t");
            if (!dictionary.add(new Kanji(fields[0].charAt(0), fields[1], fields[2], fields[3],
                    fields[4], Integer.parseInt(fields[5]), LocalDate.parse(fields[6]),
                    Integer.parseInt(fields[7]), LocalDate.parse(fields[8]), Integer.parseInt(fields[9]), LocalDate.parse(fields[10])))) {
                System.out.print("Duplicate kanji: " + fields);
            }
        }
        csvReader.close();
    }

    /**
     * Returns a KanjiDictionary starting and ending at the specified dictionary positions
     * @param start Staring index
     * @param stop Ending index (exclusive)
     * @return KanjiDictionary object starting and ending at the specified dictionary positions
     */
    public KanjiDictionary getByRange(int start, int stop) {
        KanjiDictionary kanjiDictionary = new KanjiDictionary();
        for (int index = start; index < stop; index++) {
            kanjiDictionary.add(dictionary.get(index));
        }
        return kanjiDictionary;
    }

    /**
     * Gets all kanji for which characterDue is true and returns them as a KanjiDictionary object.
     * @return KanjiDictionary object containing all kanji for which characterDue is true
     */
    public KanjiDictionary getCharacterDue() {
        KanjiDictionary kanjiList = new KanjiDictionary();
        for (Kanji kanji : dictionary) {
            if (kanji.isCharacterDue()) {
                kanjiList.add(kanji);
            }
        }
        return kanjiList;
    }

    /**
     * Gets all kanji for which readingDue is true and returns them as a KanjiDictionary object.
     * @return KanjiDictionary object containing all kanji for which readingDue is true
     */
    public KanjiDictionary getReadingDue() {
        KanjiDictionary kanjiList = new KanjiDictionary();
        for (Kanji kanji : dictionary) {
            if (kanji.isReadingDue()) {
                kanjiList.add(kanji);
            }
        }
        return kanjiList;
    }

    /**
     * Gets all kanji for which meaningDue is true and returns them as a KanjiDictionary object.
     * @return KanjiDictionary object containing all kanji for which meaningDue is true
     */
    public KanjiDictionary getMeaningDue() {
        KanjiDictionary kanjiList = new KanjiDictionary();
        for (Kanji kanji : dictionary) {
            if (kanji.isMeaningDue()) {
                kanjiList.add(kanji);
            }
        }
        return kanjiList;
    }

    /**
     * Gets all kanji with a characterLevel at the specified level and returns them as a KanjiDictionary object.
     * @param level characterLevel of the kanji that are to be returned
     * @return KanjiDictionary containing all kanji with the specified characterLevel
     */
    public KanjiDictionary getByCharacterLevel(int level) {
        KanjiDictionary kanjiList = new KanjiDictionary();
        for (Kanji kanji : dictionary) {
            if (kanji.getCharacterLevel() == level) {
                kanjiList.add(kanji);
            }
        }
        return kanjiList;
    }

    /**
     * Gets all kanji with a readingLevel at the specified level and returns them as a KanjiDictionary object.
     * @param level readingLevel of the kanji that are to be returned
     * @return KanjiDictionary containing all kanji with the specified readingLevel
     */
    public KanjiDictionary getByReadingLevel(int level) {
        KanjiDictionary kanjiList = new KanjiDictionary();
        for (Kanji kanji : dictionary) {
            if (kanji.getReadingLevel() == level) {
                kanjiList.add(kanji);
            }
        }
        return kanjiList;
    }

    /**
     * Gets all kanji with a meaningLevel at the specified level and returns them as a KanjiDictionary object.
     * @param level meaningLevel of the kanji that are to be returned
     * @return KanjiDictionary containing all kanji with the specified meaningLevel
     */
    public KanjiDictionary getByMeaningLevel(int level) {
        KanjiDictionary kanjiList = new KanjiDictionary();
        for (Kanji kanji : dictionary) {
            if (kanji.getMeaningLevel() == level) {
                kanjiList.add(kanji);
            }
        }
        return kanjiList;
    }

    /**
     * Gets the Kanji object with the specified character
     * @param character Character of the kanji
     * @return Kanji object with the specified character
     */
    public Kanji getKanjiByCharacter(char character) {
        for (Kanji kanji : dictionary) {
            if (kanji.getCharacter() == character)
                return kanji;
        }
        return null;
    }

    public KanjiDictionary getStarted() {
        KanjiDictionary startedKanji = new KanjiDictionary();
        startedKanji.addAll(dictionary);
        startedKanji.removeAll(this.getByCharacterLevel(0));
        return startedKanji;
    }

    @Override
    public boolean addAll(Collection<? extends Kanji> c) {
        return dictionary.addAll(c);
    }

    /**
     * Saves the dictionary to a specified file path
     * @param filePath The file path to which the dictionary shall be saved to
     * @throws IOException
     */
    public void save(String filePath) throws IOException {
        PrintWriter csvWriter = new PrintWriter(new FileOutputStream(filePath, false));
        StringBuilder csvStringBuilder = new StringBuilder();
        csvStringBuilder.append("character\ton\tkun\tmeaning\tmnemonic\tcharacterLevel\tcharacterDue\t" +
                                "readingLevel\treadingDue\tmeaningLevel\tmeaningDue\n");
        for (Kanji kanji : dictionary) {
            csvStringBuilder.append(kanji.toString() + "\n");
        }
        String csvString = csvStringBuilder.toString();
        System.out.println(csvString);
        csvWriter.print(csvString);
        csvWriter.close();
    }

    /**
     * Adds a Kanji object to the dictionary. This will only add a Kanji if no other Kanji with the same character
     * is contained in the dictionary already.
     * @param kanji The kanji that shall be added to the dictionary
     * @return True if added
     */
    @Override
    public boolean add(Kanji kanji) {
        for (Kanji k : dictionary) {
            if (k.getCharacter() == kanji.getCharacter()) {
                return false;
            }
        }
        dictionary.add(kanji);
        return true;
    }

    public KanjiDictionary clone() {
        KanjiDictionary cloneDictionary;
        try {
            cloneDictionary = (KanjiDictionary) super.clone();
        }
        catch (CloneNotSupportedException e) {
            System.out.println(e.getMessage());
            return cloneDictionary = new KanjiDictionary();
        }
        cloneDictionary.dictionary = new ArrayList<Kanji>();
        cloneDictionary.addAll(dictionary);
        return cloneDictionary;
    }

    /**
     * Returns a deep clone of this object. Kanji objects in this deep clone can be altered without affecting the
     * original.
     *
     * @return Deep clone of this object
     */
    public KanjiDictionary cloneDeep() {
        KanjiDictionary cloneDictionary;
        try {
            cloneDictionary = (KanjiDictionary) super.clone();
        }
        catch (CloneNotSupportedException e) {
            System.out.println(e.getMessage());
            return cloneDictionary = new KanjiDictionary();
        }
        cloneDictionary.dictionary = new ArrayList<Kanji>();
        for (Kanji k : this.dictionary) {
            Kanji cloneKanji =
                    new Kanji(k.getCharacter(), k.getOnReading(), k.getKunReading(), k.getMeaning(), k.getMnemonic(),
                              k.getCharacterLevel(), k.getCharacterDue(),
                              k.getReadingLevel(), k.getReadingDue(),
                              k.getMeaningLevel(), k.getMeaningDue());
            cloneDictionary.add(cloneKanji);
        }
        return cloneDictionary;
    }

    /**
     * Randomizes the order of the kanji in the dictionary.
     * This is not meant to be used on the global App.kanjiDictionary, only for clones/subsets used in Training and
     * Learning views.
     */
    public void shuffle() {
        Collections.shuffle(dictionary);
    }
}
