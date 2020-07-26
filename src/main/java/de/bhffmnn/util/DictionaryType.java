/*
 * Copyright (c) 2020, Benjamin Hoffmann
 * All rights reserved.

 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree.
 */

package de.bhffmnn.util;

public enum DictionaryType {
    NELSON_C,
    //NELSON_N, is not directly convertable to Integer because of one single kanji
    HALPERN_NJECD,
    HALPERN_KKD,
    HALPERN_KKLD,
    HALPERN_KKLD_2ED,
    KODANSHA_COMPACT,
    GAKKEN,
    HEISIG,
    HEISIG6,
    //ONEILL_NAMES, is not directly convertable to Integer
    ONEILL_KK,
    //MORO, is not directly convertable to Integer
    HENSHALL,
    SH_KK,
    SH_KK2,
    SAKADE,
    JF_CARDS,
    HENSHALL3,
    TUTT_CARDS,
    CROWLEY,
    KANJI_IN_CONTEXT,
    //BUSY_PEOPLE, is not directly convertable to Integer
    MANIETTE
}
