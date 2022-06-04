package enigma;

import static enigma.EnigmaException.*;

/** Superclass that represents a rotor in the enigma machine.
 *  @author Brendan Wong
 */
class Rotor {

    /** A rotor named NAME whose permutation is given by PERM. */
    Rotor(String name, Permutation perm) {
        _name = name;
        _permutation = perm;
        _setting = 0;
    }

    /** Return my name. */
    String name() {
        return _name;
    }

    /** Return my alphabet. */
    Alphabet alphabet() {
        return _permutation.alphabet();
    }

    /** Return my permutation. */
    Permutation permutation() {
        return _permutation;
    }

    /** Return the size of my alphabet. */
    int size() {
        return _permutation.size();
    }

    /** Return true iff I have a ratchet and can move. */
    boolean rotates() {
        return false;
    }

    /** Return true iff I reflect. */
    boolean reflecting() {
        return false;
    }

    /** Return my current setting. */
    int setting() {
        return _setting;
    }

    /** Set setting() to POSN.  */
    void set(int posn) {
        _setting = posn % alphabet().size();
    }

    /** Set setting() to character CPOSN. */
    void set(char cposn) {
        _setting = alphabet().toInt(cposn) % alphabet().size();
    }

    /** Return the conversion of P (an integer in the range 0..size()-1)
     *  according to my permutation. */
    int convertForward(int p) {
        char result = _permutation.permute(_permutation.alphabet().
                toChar((p + _setting) % _permutation.size()));
        if (Main.verbose()) {
            System.err.printf("%c -> ", alphabet().toChar(result));
        }
        int index = _permutation.alphabet().toInt(result) - _setting;
        if (index < 0) {
            return index + _permutation.alphabet().size();
        } else {
            return index;
        }
    }

    /** Return the conversion of E (an integer in the range 0..size()-1)
     *  according to the inverse of my permutation. */
    int convertBackward(int e) {
        char result = _permutation.invert(_permutation.alphabet().
                toChar((e + _setting) % _permutation.size()));
        if (Main.verbose()) {
            System.err.printf("%c -> ", alphabet().toChar(result));
        }
        int index = _permutation.alphabet().toInt(result) - _setting;
        if (index < 0) {
            return index + _permutation.alphabet().size();
        } else {
            return index;
        }
    }

    /** Returns the positions of the notches, as a string giving the letters
     *  on the ring at which they occur. */
    String notches() {
        return "";
    }

    /** Returns true iff I am positioned to allow the rotor to my left
     *  to advance. */
    boolean atNotch() {
        char cur = alphabet().toChar(_setting);
        return notches().contains("" + cur);
    }

    /** Advance me one position, if possible. By default, does nothing. */
    void advance() {
    }

    @Override
    public String toString() {
        return "Rotor " + _name;
    }

    /** My name. */
    private final String _name;

    /** The permutation implemented by this rotor in its 0 position. */
    private Permutation _permutation;
    /** The current setting of this rotor. */
    private int _setting;

}
