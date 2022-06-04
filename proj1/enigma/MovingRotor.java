package enigma;

import static enigma.EnigmaException.*;

/** Class that represents a rotating rotor in the enigma machine.
 *  @author Brendan Wong
 */
class MovingRotor extends Rotor {

    /** A rotor named NAME whose permutation in its default setting is
     *  PERM, and whose notches are at the positions indicated in NOTCHES.
     *  The Rotor is initally in its 0 setting (first character of its
     *  alphabet).
     */
    MovingRotor(String name, Permutation perm, String notches) {
        super(name, perm);
        _notches = notches;
        _ognotches = notches;
    }

    @Override
    void advance() {
        set(setting() + 1);
    }

    @Override
    String notches() {
        return _notches;
    }

    /** Adjusts the notch according to the CHANGE character. */
    void adjustNotches(char change) {
        String result = "";
        for (int i = 0; i < _ognotches.length(); i++) {
            int start = alphabet().toInt(_ognotches.charAt(i));
            start = start - alphabet().toInt(change);
            if (start < 0) {
                start += alphabet().size();
            }
            char newNotch = alphabet().toChar(start);
            result += newNotch;
        }
        _notches = result;
    }

    /** Changes notch back to original position. */
    void resetNotch() {
        _notches = _ognotches;
    }

    /** String containing this classes' notches. */
    private String _notches;
    /** String containing this classes' original notches. */
    private String _ognotches;

}
