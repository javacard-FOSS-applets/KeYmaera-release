


package java.lang;

import java.io.Serializable;
public final class Character implements Serializable, Comparable {
    public static class Subset {
        protected Subset(String name) {}
        public final boolean equals(Object o) {}
        public final int hashCode() {}
        public final String toString() {}
    }
    public static final class UnicodeBlock extends Subset {
        private UnicodeBlock(char start, char end, String name) {}
        public static UnicodeBlock of(char ch) {}
        public final static UnicodeBlock BASIC_LATIN = new UnicodeBlock(' ', '',
                         "BASIC_LATIN");
        public final static UnicodeBlock LATIN_1_SUPPLEMENT = new UnicodeBlock('�', '�',
                         "LATIN_1_SUPPLEMENT");
        public final static UnicodeBlock LATIN_EXTENDED_A = new UnicodeBlock('\u0100', '\u017f',
                         "LATIN_EXTENDED_A");
        public final static UnicodeBlock LATIN_EXTENDED_B = new UnicodeBlock('\u0180', '\u024f',
                         "LATIN_EXTENDED_B");
        public final static UnicodeBlock IPA_EXTENSIONS = new UnicodeBlock('\u0250', '\u02af',
                         "IPA_EXTENSIONS");
        public final static UnicodeBlock SPACING_MODIFIER_LETTERS = new UnicodeBlock('\u02b0', '\u02ff',
                         "SPACING_MODIFIER_LETTERS");
        public final static UnicodeBlock COMBINING_DIACRITICAL_MARKS = new UnicodeBlock('\u0300', '\u036f',
                         "COMBINING_DIACRITICAL_MARKS");
        public final static UnicodeBlock GREEK = new UnicodeBlock('\u0370', '\u03ff',
                         "GREEK");
        public final static UnicodeBlock CYRILLIC = new UnicodeBlock('\u0400', '\u04ff',
                         "CYRILLIC");
        public final static UnicodeBlock ARMENIAN = new UnicodeBlock('\u0530', '\u058f',
                         "ARMENIAN");
        public final static UnicodeBlock HEBREW = new UnicodeBlock('\u0590', '\u05ff',
                         "HEBREW");
        public final static UnicodeBlock ARABIC = new UnicodeBlock('\u0600', '\u06ff',
                         "ARABIC");
        public final static UnicodeBlock SYRIAC = new UnicodeBlock('\u0700', '\u074f',
                         "SYRIAC");
        public final static UnicodeBlock THAANA = new UnicodeBlock('\u0780', '\u07bf',
                         "THAANA");
        public final static UnicodeBlock DEVANAGARI = new UnicodeBlock('\u0900', '\u097f',
                         "DEVANAGARI");
        public final static UnicodeBlock BENGALI = new UnicodeBlock('\u0980', '\u09ff',
                         "BENGALI");
        public final static UnicodeBlock GURMUKHI = new UnicodeBlock('\u0a00', '\u0a7f',
                         "GURMUKHI");
        public final static UnicodeBlock GUJARATI = new UnicodeBlock('\u0a80', '\u0aff',
                         "GUJARATI");
        public final static UnicodeBlock ORIYA = new UnicodeBlock('\u0b00', '\u0b7f',
                         "ORIYA");
        public final static UnicodeBlock TAMIL = new UnicodeBlock('\u0b80', '\u0bff',
                         "TAMIL");
        public final static UnicodeBlock TELUGU = new UnicodeBlock('\u0c00', '\u0c7f',
                         "TELUGU");
        public final static UnicodeBlock KANNADA = new UnicodeBlock('\u0c80', '\u0cff',
                         "KANNADA");
        public final static UnicodeBlock MALAYALAM = new UnicodeBlock('\u0d00', '\u0d7f',
                         "MALAYALAM");
        public final static UnicodeBlock SINHALA = new UnicodeBlock('\u0d80', '\u0dff',
                         "SINHALA");
        public final static UnicodeBlock THAI = new UnicodeBlock('\u0e00', '\u0e7f',
                         "THAI");
        public final static UnicodeBlock LAO = new UnicodeBlock('\u0e80', '\u0eff',
                         "LAO");
        public final static UnicodeBlock TIBETAN = new UnicodeBlock('\u0f00', '\u0fff',
                         "TIBETAN");
        public final static UnicodeBlock MYANMAR = new UnicodeBlock('\u1000', '\u109f',
                         "MYANMAR");
        public final static UnicodeBlock GEORGIAN = new UnicodeBlock('\u10a0', '\u10ff',
                         "GEORGIAN");
        public final static UnicodeBlock HANGUL_JAMO = new UnicodeBlock('\u1100', '\u11ff',
                         "HANGUL_JAMO");
        public final static UnicodeBlock ETHIOPIC = new UnicodeBlock('\u1200', '\u137f',
                         "ETHIOPIC");
        public final static UnicodeBlock CHEROKEE = new UnicodeBlock('\u13a0', '\u13ff',
                         "CHEROKEE");
        public final static UnicodeBlock UNIFIED_CANADIAN_ABORIGINAL_SYLLABICS = new UnicodeBlock('\u1400', '\u167f',
                         "UNIFIED_CANADIAN_ABORIGINAL_SYLLABICS");
        public final static UnicodeBlock OGHAM = new UnicodeBlock('\u1680', '\u169f',
                         "OGHAM");
        public final static UnicodeBlock RUNIC = new UnicodeBlock('\u16a0', '\u16ff',
                         "RUNIC");
        public final static UnicodeBlock KHMER = new UnicodeBlock('\u1780', '\u17ff',
                         "KHMER");
        public final static UnicodeBlock MONGOLIAN = new UnicodeBlock('\u1800', '\u18af',
                         "MONGOLIAN");
        public final static UnicodeBlock LATIN_EXTENDED_ADDITIONAL = new UnicodeBlock('\u1e00', '\u1eff',
                         "LATIN_EXTENDED_ADDITIONAL");
        public final static UnicodeBlock GREEK_EXTENDED = new UnicodeBlock('\u1f00', '\u1fff',
                         "GREEK_EXTENDED");
        public final static UnicodeBlock GENERAL_PUNCTUATION = new UnicodeBlock('\u2000', '\u206f',
                         "GENERAL_PUNCTUATION");
        public final static UnicodeBlock SUPERSCRIPTS_AND_SUBSCRIPTS = new UnicodeBlock('\u2070', '\u209f',
                         "SUPERSCRIPTS_AND_SUBSCRIPTS");
        public final static UnicodeBlock CURRENCY_SYMBOLS = new UnicodeBlock('\u20a0', '\u20cf',
                         "CURRENCY_SYMBOLS");
        public final static UnicodeBlock COMBINING_MARKS_FOR_SYMBOLS = new UnicodeBlock('\u20d0', '\u20ff',
                         "COMBINING_MARKS_FOR_SYMBOLS");
        public final static UnicodeBlock LETTERLIKE_SYMBOLS = new UnicodeBlock('\u2100', '\u214f',
                         "LETTERLIKE_SYMBOLS");
        public final static UnicodeBlock NUMBER_FORMS = new UnicodeBlock('\u2150', '\u218f',
                         "NUMBER_FORMS");
        public final static UnicodeBlock ARROWS = new UnicodeBlock('\u2190', '\u21ff',
                         "ARROWS");
        public final static UnicodeBlock MATHEMATICAL_OPERATORS = new UnicodeBlock('\u2200', '\u22ff',
                         "MATHEMATICAL_OPERATORS");
        public final static UnicodeBlock MISCELLANEOUS_TECHNICAL = new UnicodeBlock('\u2300', '\u23ff',
                         "MISCELLANEOUS_TECHNICAL");
        public final static UnicodeBlock CONTROL_PICTURES = new UnicodeBlock('\u2400', '\u243f',
                         "CONTROL_PICTURES");
        public final static UnicodeBlock OPTICAL_CHARACTER_RECOGNITION = new UnicodeBlock('\u2440', '\u245f',
                         "OPTICAL_CHARACTER_RECOGNITION");
        public final static UnicodeBlock ENCLOSED_ALPHANUMERICS = new UnicodeBlock('\u2460', '\u24ff',
                         "ENCLOSED_ALPHANUMERICS");
        public final static UnicodeBlock BOX_DRAWING = new UnicodeBlock('\u2500', '\u257f',
                         "BOX_DRAWING");
        public final static UnicodeBlock BLOCK_ELEMENTS = new UnicodeBlock('\u2580', '\u259f',
                         "BLOCK_ELEMENTS");
        public final static UnicodeBlock GEOMETRIC_SHAPES = new UnicodeBlock('\u25a0', '\u25ff',
                         "GEOMETRIC_SHAPES");
        public final static UnicodeBlock MISCELLANEOUS_SYMBOLS = new UnicodeBlock('\u2600', '\u26ff',
                         "MISCELLANEOUS_SYMBOLS");
        public final static UnicodeBlock DINGBATS = new UnicodeBlock('\u2700', '\u27bf',
                         "DINGBATS");
        public final static UnicodeBlock BRAILLE_PATTERNS = new UnicodeBlock('\u2800', '\u28ff',
                         "BRAILLE_PATTERNS");
        public final static UnicodeBlock CJK_RADICALS_SUPPLEMENT = new UnicodeBlock('\u2e80', '\u2eff',
                         "CJK_RADICALS_SUPPLEMENT");
        public final static UnicodeBlock KANGXI_RADICALS = new UnicodeBlock('\u2f00', '\u2fdf',
                         "KANGXI_RADICALS");
        public final static UnicodeBlock IDEOGRAPHIC_DESCRIPTION_CHARACTERS = new UnicodeBlock('\u2ff0', '\u2fff',
                         "IDEOGRAPHIC_DESCRIPTION_CHARACTERS");
        public final static UnicodeBlock CJK_SYMBOLS_AND_PUNCTUATION = new UnicodeBlock('\u3000', '\u303f',
                         "CJK_SYMBOLS_AND_PUNCTUATION");
        public final static UnicodeBlock HIRAGANA = new UnicodeBlock('\u3040', '\u309f',
                         "HIRAGANA");
        public final static UnicodeBlock KATAKANA = new UnicodeBlock('\u30a0', '\u30ff',
                         "KATAKANA");
        public final static UnicodeBlock BOPOMOFO = new UnicodeBlock('\u3100', '\u312f',
                         "BOPOMOFO");
        public final static UnicodeBlock HANGUL_COMPATIBILITY_JAMO = new UnicodeBlock('\u3130', '\u318f',
                         "HANGUL_COMPATIBILITY_JAMO");
        public final static UnicodeBlock KANBUN = new UnicodeBlock('\u3190', '\u319f',
                         "KANBUN");
        public final static UnicodeBlock BOPOMOFO_EXTENDED = new UnicodeBlock('\u31a0', '\u31bf',
                         "BOPOMOFO_EXTENDED");
        public final static UnicodeBlock ENCLOSED_CJK_LETTERS_AND_MONTHS = new UnicodeBlock('\u3200', '\u32ff',
                         "ENCLOSED_CJK_LETTERS_AND_MONTHS");
        public final static UnicodeBlock CJK_COMPATIBILITY = new UnicodeBlock('\u3300', '\u33ff',
                         "CJK_COMPATIBILITY");
        public final static UnicodeBlock CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A = new UnicodeBlock('\u3400', '\u4db5',
                         "CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A");
        public final static UnicodeBlock CJK_UNIFIED_IDEOGRAPHS = new UnicodeBlock('\u4e00', '\u9fff',
                         "CJK_UNIFIED_IDEOGRAPHS");
        public final static UnicodeBlock YI_SYLLABLES = new UnicodeBlock('\ua000', '\ua48f',
                         "YI_SYLLABLES");
        public final static UnicodeBlock YI_RADICALS = new UnicodeBlock('\ua490', '\ua4cf',
                         "YI_RADICALS");
        public final static UnicodeBlock HANGUL_SYLLABLES = new UnicodeBlock('\uac00', '\ud7a3',
                         "HANGUL_SYLLABLES");
        public final static UnicodeBlock SURROGATES_AREA = new UnicodeBlock('\ud800', '\udfff',
                         "SURROGATES_AREA");
        public final static UnicodeBlock PRIVATE_USE_AREA = new UnicodeBlock('\ue000', '\uf8ff',
                         "PRIVATE_USE_AREA");
        public final static UnicodeBlock CJK_COMPATIBILITY_IDEOGRAPHS = new UnicodeBlock('\uf900', '\ufaff',
                         "CJK_COMPATIBILITY_IDEOGRAPHS");
        public final static UnicodeBlock ALPHABETIC_PRESENTATION_FORMS = new UnicodeBlock('\ufb00', '\ufb4f',
                         "ALPHABETIC_PRESENTATION_FORMS");
        public final static UnicodeBlock ARABIC_PRESENTATION_FORMS_A = new UnicodeBlock('\ufb50', '\ufdff',
                         "ARABIC_PRESENTATION_FORMS_A");
        public final static UnicodeBlock COMBINING_HALF_MARKS = new UnicodeBlock('\ufe20', '\ufe2f',
                         "COMBINING_HALF_MARKS");
        public final static UnicodeBlock CJK_COMPATIBILITY_FORMS = new UnicodeBlock('\ufe30', '\ufe4f',
                         "CJK_COMPATIBILITY_FORMS");
        public final static UnicodeBlock SMALL_FORM_VARIANTS = new UnicodeBlock('\ufe50', '\ufe6f',
                         "SMALL_FORM_VARIANTS");
        public final static UnicodeBlock ARABIC_PRESENTATION_FORMS_B = new UnicodeBlock('\ufe70', '\ufefe',
                         "ARABIC_PRESENTATION_FORMS_B");
        public final static UnicodeBlock HALFWIDTH_AND_FULLWIDTH_FORMS = new UnicodeBlock('\uff00', '\uffef',
                         "HALFWIDTH_AND_FULLWIDTH_FORMS");
        public final static UnicodeBlock SPECIALS = new UnicodeBlock('\ufff0', '\ufffd',
                         "SPECIALS");
    }
    public static final int MIN_RADIX = 2;
    public static final int MAX_RADIX = 36;
    public static final char MIN_VALUE = ' ';
    public static final char MAX_VALUE = '\uffff';
    public static final byte UPPERCASE_LETTER = 1;
    public static final byte LOWERCASE_LETTER = 2;
    public static final byte TITLECASE_LETTER = 3;
    public static final byte NON_SPACING_MARK = 6;
    public static final byte COMBINING_SPACING_MARK = 8;
    public static final byte ENCLOSING_MARK = 7;
    public static final byte DECIMAL_DIGIT_NUMBER = 9;
    public static final byte LETTER_NUMBER = 10;
    public static final byte OTHER_NUMBER = 11;
    public static final byte SPACE_SEPARATOR = 12;
    public static final byte LINE_SEPARATOR = 13;
    public static final byte PARAGRAPH_SEPARATOR = 14;
    public static final byte CONTROL = 15;
    public static final byte FORMAT = 16;
    public static final byte SURROGATE = 19;
    public static final byte PRIVATE_USE = 18;
    public static final byte UNASSIGNED = 0;
    public static final byte MODIFIER_LETTER = 4;
    public static final byte OTHER_LETTER = 5;
    public static final byte CONNECTOR_PUNCTUATION = 23;
    public static final byte DASH_PUNCTUATION = 20;
    public static final byte START_PUNCTUATION = 21;
    public static final byte END_PUNCTUATION = 22;
    public static final byte INITIAL_QUOTE_PUNCTUATION = 29;
    public static final byte FINAL_QUOTE_PUNCTUATION = 30;
    public static final byte OTHER_PUNCTUATION = 24;
    public static final byte MATH_SYMBOL = 25;
    public static final byte CURRENCY_SYMBOL = 26;
    public static final byte MODIFIER_SYMBOL = 27;
    public static final byte OTHER_SYMBOL = 28;
    public static final byte DIRECTIONALITY_UNDEFINED = -1;
    public static final byte DIRECTIONALITY_LEFT_TO_RIGHT = 0;
    public static final byte DIRECTIONALITY_RIGHT_TO_LEFT = 1;
    public static final byte DIRECTIONALITY_RIGHT_TO_LEFT_ARABIC = 2;
    public static final byte DIRECTIONALITY_EUROPEAN_NUMBER = 3;
    public static final byte DIRECTIONALITY_EUROPEAN_NUMBER_SEPARATOR = 4;
    public static final byte DIRECTIONALITY_EUROPEAN_NUMBER_TERMINATOR = 5;
    public static final byte DIRECTIONALITY_ARABIC_NUMBER = 6;
    public static final byte DIRECTIONALITY_COMMON_NUMBER_SEPARATOR = 7;
    public static final byte DIRECTIONALITY_NONSPACING_MARK = 8;
    public static final byte DIRECTIONALITY_BOUNDARY_NEUTRAL = 9;
    public static final byte DIRECTIONALITY_PARAGRAPH_SEPARATOR = 10;
    public static final byte DIRECTIONALITY_SEGMENT_SEPARATOR = 11;
    public static final byte DIRECTIONALITY_WHITESPACE = 12;
    public static final byte DIRECTIONALITY_OTHER_NEUTRALS = 13;
    public static final byte DIRECTIONALITY_LEFT_TO_RIGHT_EMBEDDING = 14;
    public static final byte DIRECTIONALITY_LEFT_TO_RIGHT_OVERRIDE = 15;
    public static final byte DIRECTIONALITY_RIGHT_TO_LEFT_EMBEDDING = 16;
    public static final byte DIRECTIONALITY_RIGHT_TO_LEFT_OVERRIDE = 17;
    public static final byte DIRECTIONALITY_POP_DIRECTIONAL_FORMAT = 18;
    static final char[] direction;
    static char readChar(char ch) {}
    public Character(char value) {}
    public char charValue() {}
    public int hashCode() {}
    public boolean equals(Object o) {}
    public String toString() {}
    public static String toString(char ch) {}
    public static boolean isLowerCase(char ch) {}
    public static boolean isUpperCase(char ch) {}
    public static boolean isTitleCase(char ch) {}
    public static boolean isDigit(char ch) {}
    public static boolean isDefined(char ch) {}
    public static boolean isLetter(char ch) {}
    public static boolean isLetterOrDigit(char ch) {}
    public static boolean isJavaLetter(char ch) {}
    public static boolean isJavaLetterOrDigit(char ch) {}
    public static boolean isJavaIdentifierStart(char ch) {}
    public static boolean isJavaIdentifierPart(char ch) {}
    public static boolean isUnicodeIdentifierStart(char ch) {}
    public static boolean isUnicodeIdentifierPart(char ch) {}
    public static boolean isIdentifierIgnorable(char ch) {}
    public static char toLowerCase(char ch) {}
    public static char toUpperCase(char ch) {}
    public static char toTitleCase(char ch) {}
    public static int digit(char ch, int radix) {}
    public static int getNumericValue(char ch) {}
    public static boolean isSpace(char ch) {}
    public static boolean isSpaceChar(char ch) {}
    public static boolean isWhitespace(char ch) {}
    public static boolean isISOControl(char ch) {}
    public static int getType(char ch) {}
    public static char forDigit(int digit, int radix) {}
    public static byte getDirectionality(char ch) {}
    public static boolean isMirrored(char ch) {}
    public int compareTo(Character anotherCharacter) {}
    public int compareTo(Object o) {}
}
