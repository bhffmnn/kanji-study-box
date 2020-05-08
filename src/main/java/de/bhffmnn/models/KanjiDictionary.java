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

public class KanjiDictionary extends AbstractCollection<Kanji> implements Cloneable {
    private ArrayList<Kanji> dictionary;

    public KanjiDictionary() {
        dictionary = new ArrayList<Kanji>();
    }

    public KanjiDictionary (String filePath) throws IOException {
        dictionary = new ArrayList<Kanji>();
        BufferedReader csvReader = new BufferedReader(new FileReader(filePath));
        csvReader.readLine(); //skip header
        String row;
        while ((row = csvReader.readLine()) != null) {
            String[] fields = row.split("\t");
            if (!add(new Kanji(fields[0], fields[1], fields[2], fields[3],
                    fields[4], Integer.parseInt(fields[5]), LocalDate.parse(fields[6]),
                    Integer.parseInt(fields[7]), LocalDate.parse(fields[8]), Integer.parseInt(fields[9]), LocalDate.parse(fields[10])))) {
                System.out.print("Duplicate kanji: " + fields);
            }
        }
        csvReader.close();
    }

    @Override
    public Iterator<Kanji> iterator() {
        return dictionary.iterator();
    }

    @Override
    public int size() {
        return dictionary.size();
    }

    public Kanji getByIndex(int i) {
        return dictionary.get(i);
    }

    public void reload(String filePath) throws IOException {
        dictionary = new ArrayList<Kanji>();
        BufferedReader csvReader = new BufferedReader(new FileReader(filePath));
        if ((csvReader.readLine()) != null) {
            //skip header
        }
        String row;
        while ((row = csvReader.readLine()) != null) {
            String[] fields = row.split("\t");
            if (!dictionary.add(new Kanji(fields[0], fields[1], fields[2], fields[3],
                    fields[4], Integer.parseInt(fields[5]), LocalDate.parse(fields[6]),
                    Integer.parseInt(fields[7]), LocalDate.parse(fields[8]), Integer.parseInt(fields[9]), LocalDate.parse(fields[10])))) {
                System.out.print("Duplicate kanji: " + fields);
            }
        }
        csvReader.close();
    }

    public KanjiDictionary getByRange(int start, int stop) {
        KanjiDictionary kanjiDictionary = new KanjiDictionary();
        for (int index = start; index < stop; index++) {
            kanjiDictionary.add(dictionary.get(index));
        }
        return kanjiDictionary;
    }

    public KanjiDictionary getCharacterDue() {
        KanjiDictionary kanjiList = new KanjiDictionary();
        for (Kanji kanji : dictionary) {
            if (kanji.isCharacterDue()) {
                kanjiList.add(kanji);
            }
        }
        return kanjiList;
    }

    public KanjiDictionary getReadingDue() {
        KanjiDictionary kanjiList = new KanjiDictionary();
        for (Kanji kanji : dictionary) {
            if (kanji.isReadingDue()) {
                kanjiList.add(kanji);
            }
        }
        return kanjiList;
    }

    public KanjiDictionary getMeaningDue() {
        KanjiDictionary kanjiList = new KanjiDictionary();
        for (Kanji kanji : dictionary) {
            if (kanji.isMeaningDue()) {
                kanjiList.add(kanji);
            }
        }
        return kanjiList;
    }

    public KanjiDictionary getByCharacterLevel(int level) {
        KanjiDictionary kanjiList = new KanjiDictionary();
        for (Kanji kanji : dictionary) {
            if (kanji.getCharacterLevel() == level) {
                kanjiList.add(kanji);
            }
        }
        return kanjiList;
    }

    public KanjiDictionary getByReadingLevel(int level) {
        KanjiDictionary kanjiList = new KanjiDictionary();
        for (Kanji kanji : dictionary) {
            if (kanji.getReadingLevel() == level) {
                kanjiList.add(kanji);
            }
        }
        return kanjiList;
    }

    public KanjiDictionary getByMeaningLevel(int level) {
        KanjiDictionary kanjiList = new KanjiDictionary();
        for (Kanji kanji : dictionary) {
            if (kanji.getMeaningLevel() == level) {
                kanjiList.add(kanji);
            }
        }
        return kanjiList;
    }

    public Kanji getKanjiByCharacter(String character) {
        for (Kanji kanji : dictionary) {
            if (kanji.getCharacter().equals(character))
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

    @Override
    public boolean add(Kanji kanji) {
        for (Kanji k : dictionary) {
            if (k.getCharacter().equals(kanji.getCharacter())) {
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

    public void shuffle() {
        Collections.shuffle(dictionary);
    }
}
