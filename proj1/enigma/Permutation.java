package enigma;

import java.util.HashMap;

import static enigma.EnigmaException.*;

/** Represents a permutation of a range of integers starting at 0 corresponding
 *  to the characters of an alphabet.
 *  @author Brendan Wong
 */
class Permutation {

    /** Set this Permutation to that specified by CYCLES, a string in the
     *  form "(cccc) (cc) ..." where the c's are characters in ALPHABET, which
     *  is interpreted as a permutation in cycle notation.  Characters in the
     *  alphabet that are not included in any cycle map to themselves.
     *  Whitespace is ignored. */
    Permutation(String cycles, Alphabet alphabet) {
        _alphabet = alphabet;
        while (cycles.contains("(")) {
            int start = cycles.indexOf("(");
            int end = cycles.indexOf(")");
            if (end == -1) {
                throw new EnigmaException("Bad Config");
            }
            addCycle(cycles.substring(start + 1, end));
            cycles = cycles.substring(end + 1);
        }
    }

    /** Add the cycle c0->c1->...->cm->c0 to the permutation, where CYCLE is
     *  c0c1...cm. */
    private void addCycle(String cycle) {
        char prev = cycle.charAt(0);
        char start = cycle.charAt(0);
        for (int i = 1; i < cycle.length(); i++) {
            char cur = cycle.charAt(i);
            permutations.put(prev, cur);
            prev = cur;
        }
        permutations.put(prev, start);
    }

    /** Return the value of P modulo the size of this permutation. */
    final int wrap(int p) {
        int r = p % size();
        if (r < 0) {
            r += size();
        }
        return r;
    }

    /** Returns the size of the alphabet I permute. */
    int size() {
        return _alphabet.size();
    }

    /** Return the result of applying this permutation to P modulo the
     *  alphabet size. */
    int permute(int p) {
        int index = wrap(p);
        Character result = permutations.get(alphabet().toChar(index));
        if (result == null) {
            return p;
        } else {
            return alphabet().toInt(result);
        }
    }

    /** Return the result of applying the inverse of this permutation
     *  to  C modulo the alphabet size. */
    int invert(int c) {
        int index = wrap(c);
        char result = invert(_alphabet.toChar(index));
        return _alphabet.toInt(result);
    }

    /** Return the result of applying this permutation to the index of P
     *  in ALPHABET, and converting the result to a character of ALPHABET. */
    char permute(char p) {
        Character result = permutations.get(p);
        if (result == null) {
            return p;
        } else {
            return result;
        }
    }

    /** Return the result of applying the inverse of this permutation to C. */
    char invert(char c) {
        char result = ' ';
        for (char i : permutations.keySet()) {
            if (permutations.get(i) != null && c == permutations.get(i)) {
                result = i;
            }
        }
        if (result == ' ') {
            return c;
        } else {
            return result;
        }
    }

    /** Return the alphabet used to initialize this Permutation. */
    Alphabet alphabet() {
        return _alphabet;
    }

    /** Return true iff this permutation is a derangement (i.e., a
     *  permutation for which no value maps to itself). */
    boolean derangement() {
        int count = 0;
        for (int i : permutations.keySet()) {
            if (i != permutations.get(i)) {
                count++;
            }
        }
        return permutations.size() == count;
    }

    /** Alphabet of this permutation. */
    private Alphabet _alphabet;
    /** Mapping of Character to it's expected output. */
    private HashMap<Character, Character> permutations =
            new HashMap<Character, Character>();
}
