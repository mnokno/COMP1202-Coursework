package utils;

import java.util.Dictionary;
import java.util.Hashtable;


/**
 * Tables is a static class used to store tables that would be stored in a database, or simple file
 * but are stored where because I want to avoid any I/O exception when reading file on other devices
 * due to system restriction or other problems.
 */
public final class Tables {

    public static final Dictionary<Integer, Integer[]> idToLoudness = new Hashtable<Integer, Integer[]>(){{
        put(41, new Integer[]{50, 100});
        put(43, new Integer[]{50, 100});
        put(1, new Integer[]{75, 150});
    }};

    public static final Dictionary<String, Integer> nameToID = new Hashtable<String, Integer>(){{
        put("Violin", 41);
        put("Cello", 43);
        put("Piano", 1);
    }};

    public static final Dictionary<String, Integer> tempoToLength = new Hashtable<String, Integer>(){{
        put("Larghissimo", 1500);
        put("Lento", 1000);
        put("Andante", 500);
        put("Moderato", 300);
        put("Allegro", 175);
        put("Presto", 150);
    }};

    /**
     * Don't worry, this code was generated using my custom .csv to java hashtable converter.
     */
    public static final Dictionary<String, Integer> noteNameToMIDI = new Hashtable<String, Integer>(){{
        put("G#9", 128);
        put("Ab9", 128);
        put("G9", 127);
        put("F#9", 126);
        put("Gb9", 126);
        put("F9", 125);
        put("E9", 124);
        put("D#9", 123);
        put("Eb9", 123);
        put("D9", 122);
        put("C#9", 121);
        put("Db9", 121);
        put("C9", 120);
        put("B8", 119);
        put("A#8", 118);
        put("Bb8", 118);
        put("A8", 117);
        put("G#8", 116);
        put("Ab8", 116);
        put("G8", 115);
        put("F#8", 114);
        put("Gb8", 114);
        put("F8", 113);
        put("E8", 112);
        put("D#8", 111);
        put("Eb8", 111);
        put("D8", 110);
        put("C#8", 109);
        put("Db8", 109);
        put("C8", 108);
        put("B7", 107);
        put("A#7", 106);
        put("Bb7", 106);
        put("A7", 105);
        put("G#7", 104);
        put("Ab7", 104);
        put("G7", 103);
        put("F#7", 102);
        put("Gb7", 102);
        put("F7", 101);
        put("E7", 100);
        put("D#7", 99);
        put("Eb7", 99);
        put("D7", 98);
        put("C#7", 97);
        put("Db7", 97);
        put("C7", 96);
        put("B6", 95);
        put("A#6", 94);
        put("Bb6", 94);
        put("A6", 93);
        put("G#6", 92);
        put("Ab6", 92);
        put("G6", 91);
        put("F#6", 90);
        put("Gb6", 90);
        put("F6", 89);
        put("E6", 88);
        put("D#6", 87);
        put("Eb6", 87);
        put("D6", 86);
        put("C#6", 85);
        put("Db6", 85);
        put("C6", 84);
        put("B5", 83);
        put("A#5", 82);
        put("Bb5", 82);
        put("A5", 81);
        put("G#5", 80);
        put("Ab5", 80);
        put("G5", 79);
        put("F#5", 78);
        put("Gb5", 78);
        put("F5", 77);
        put("E5", 76);
        put("D#5", 75);
        put("Eb5", 75);
        put("D5", 74);
        put("C#5", 73);
        put("Db5", 73);
        put("C5", 72);
        put("B4", 71);
        put("A#4", 70);
        put("Bb4", 70);
        put("A4", 69);
        put("G#4", 68);
        put("Ab4", 68);
        put("G4", 67);
        put("F#4", 66);
        put("Gb4", 66);
        put("F4", 65);
        put("E4", 64);
        put("D#4", 63);
        put("Eb4", 63);
        put("D4", 62);
        put("C#4", 61);
        put("Db4", 61);
        put("C4", 60);
        put("B3", 59);
        put("A#3", 58);
        put("Bb3", 58);
        put("A3", 57);
        put("G#3", 56);
        put("Ab3", 56);
        put("G3", 55);
        put("F#3", 54);
        put("Gb3", 54);
        put("F3", 53);
        put("E3", 52);
        put("D#3", 51);
        put("Eb3", 51);
        put("D3", 50);
        put("C#3", 49);
        put("Db3", 49);
        put("C3", 48);
        put("B2", 47);
        put("A#2", 46);
        put("Bb2", 46);
        put("A2", 45);
        put("G#2", 44);
        put("Ab2", 44);
        put("G2", 43);
        put("F#2", 42);
        put("Gb2", 42);
        put("F2", 41);
        put("E2", 40);
        put("D#2", 39);
        put("Eb2", 39);
        put("D2", 38);
        put("C#2", 37);
        put("Db2", 37);
        put("C2", 36);
        put("B1", 35);
        put("A#1", 34);
        put("Bb1", 34);
        put("A1", 33);
        put("G#1", 32);
        put("Ab1", 32);
        put("G1", 31);
        put("F#1", 30);
        put("Gb1", 30);
        put("F1", 29);
        put("E1", 28);
        put("D#1", 27);
        put("Eb1", 27);
        put("D1", 26);
        put("C#1", 25);
        put("Db1", 25);
        put("C1", 24);
        put("B0", 23);
        put("A#0", 22);
        put("Bb0", 22);
        put("A0", 21);
    }};
}
