/*
 * Copyright (c) 2020, Benjamin Hoffmann
 * All rights reserved.

 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree.
 */

package de.bhffmnn.util;

public enum DictionaryType {
    DEFAULT {
        @Override
        public String toString() {
            return "KANJIDIC2 Default";
        }
    },
    NELSON_C {
        @Override
        public String toString() {
            return "Modern Reader's Japanese-English Character Dictionary";
        }
    },
    //NELSON_N, is not directly convertable to Integer because of one single kanji
    HALPERN_NJECD {
        @Override
        public String toString() {
            return "New Japanese-English Character Dictionary";
        }
    },
    HALPERN_KKD {
        @Override
        public String toString() {
            return "Kodansha Kanji Dictionary";
        }
    },
    HALPERN_KKLD {
        @Override
        public String toString() {
            return "Kanji Learners Dictionary";
        }
    },
    HALPERN_KKLD_2ED {
        @Override
        public String toString() {
            return "Kanji Learners Dictionary, 2nd Ed.";
        }
    },
    KODANSHA_COMPACT {
        @Override
        public String toString() {
            return "Kodansha Compact Kanji Guide";
        }
    },
    GAKKEN {
        @Override
        public String toString() {
            return "A  New Dictionary of Kanji Usage";
        }
    },
    HEISIG {
        @Override
        public String toString() {
            return "Remembering The  Kanji";
        }
    },
    HEISIG6 {
        @Override
        public String toString() {
            return "Remembering The  Kanji, 6th Ed.";
        }
    },
    //ONEILL_NAMES, is not directly convertable to Integer
    ONEILL_KK {
        @Override
        public String toString() {
            return "Essential Kanji";
        }
    },
    //MORO, is not directly convertable to Integer
    HENSHALL {
        @Override
        public String toString() {
            return "A Guide To Remembering Japanese Characters";
        }
    },
    SH_KK {
        @Override
        public String toString() {
            return "Kanji and Kana";
        }
    },
    SH_KK2 {
        @Override
        public String toString() {
            return "Kanji and Kana, 2011";
        }
    },
    SAKADE {
        @Override
        public String toString() {
            return "A Guide To Reading and Writing Japanese";
        }
    },
    JF_CARDS {
        @Override
        public String toString() {
            return "Japanese Kanji Flashcards";
        }
    },
    HENSHALL3 {
        @Override
        public String toString() {
            return "A Guide To Reading and Writing Japanese, 3rd Ed.";
        }
    },
    TUTT_CARDS {
        @Override
        public String toString() {
            return "Tuttle Kanji Cards";
        }
    },
    CROWLEY {
        @Override
        public String toString() {
            return "The Kanji Way to Japanese Language Power";
        }
    },
    KANJI_IN_CONTEXT {
        @Override
        public String toString() {
            return "Kanji in Context";
        }
    },
    //BUSY_PEOPLE, is not directly convertable to Integer
    MANIETTE {
        @Override
        public String toString() {
            return "Les Kanjis dans la tete";
        }
    }
}
