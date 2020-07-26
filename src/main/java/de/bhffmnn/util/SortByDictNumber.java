/*
 * Copyright (c) 2020, Benjamin Hoffmann
 * All rights reserved.

 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree.
 */
package de.bhffmnn.util;

import java.util.Comparator;

public class SortByDictNumber implements Comparator<KanjidicKanji> {
    private DictionaryType dictType;

    public SortByDictNumber(DictionaryType dictType) {
        this.dictType = dictType;
    }

    @Override
    public int compare(KanjidicKanji a, KanjidicKanji b) {
        if (a.getDictionaryNumber(dictType) == null && b.getDictionaryNumber(dictType) == null) {
            return 0;
        }
        if (a.getDictionaryNumber(dictType) == null) {
            return 1;
        }
        if (b.getDictionaryNumber(dictType) == null) {
            return -1;
        }
        return a.getDictionaryNumber(dictType).compareTo(b.getDictionaryNumber(dictType));
    }
}