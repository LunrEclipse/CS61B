package enigma;

import com.sun.xml.internal.xsom.impl.Ref;

import java.util.ArrayList;
import java.util.Collection;

import static enigma.EnigmaException.*;

/** Class that represents a complete enigma machine.
 *  @author Brendan Wong
 */
class Machine {

    /** A new Enigma machine with alphabet ALPHA, 1 < NUMROTORS rotor slots,
     *  and 0 <= PAWLS < NUMROTORS pawls.  ALLROTORS contains all the
     *  available rotors. */
    Machine(Alphabet alpha, int numRotors, int pawls,
            Collection<Rotor> allRotors) {
        _alphabet = alpha;
        _numRotors = numRotors;
        _pawls = pawls;
        _allRotors = new ArrayList<Rotor>(allRotors);
        _rotorSet = new ArrayList<Rotor>();
        _plugboard = new Permutation("", _alphabet);
    }

    /** Return the number of rotor slots I have. */
    int numRotors() {
        return _numRotors;
    }

    /** Return the number pawls (and thus rotating rotors) I have. */
    int numPawls() {
        return _pawls;
    }

    /** Return Rotor #K, where Rotor #0 is the reflector, and Rotor
     *  #(numRotors()-1) is the fast Rotor.  Modifying this Rotor has
     *  undefined results. */
    Rotor getRotor(int k) {
        return _rotorSet.get(k);
    }

    Alphabet alphabet() {
        return _alphabet;
    }

    /** Set my rotor slots to the rotors named ROTORS from my set of
     *  available rotors (ROTORS[0] names the reflector).
     *  Initially, all rotors are set at their 0 setting. */
    void insertRotors(String[] rotors) {
        _rotorSet = new ArrayList<Rotor>();
        for (int i = 0; i < rotors.length; i++) {
            boolean found = false;
            for (int j = 0; j < _allRotors.size(); j++) {
                if (rotors[i].equals(_allRotors.get(j).name())) {
                    if (_rotorSet.contains(_allRotors.get(j))) {
                        throw new EnigmaException("Repeat Rotor");
                    }
                    _rotorSet.add(_allRotors.get(j));
                    _allRotors.get(j).set(0);
                    if (_allRotors.get(j) instanceof MovingRotor) {
                        ((MovingRotor) _allRotors.get(j)).resetNotch();
                    }
                    found = true;
                    break;
                }
            }
            if (!found) {
                throw new EnigmaException("Rotor not found");
            }
        }
        if (!(_rotorSet.get(0) instanceof Reflector)) {
            throw new EnigmaException("First Rotor not Reflector");
        }
        checkMultipleReflectors();
    }

    /**Checks to see if a rotor set has multiple reflectors.*/
    private void checkMultipleReflectors() {
        int count = 0;
        for (int i = 0; i < _rotorSet.size(); i++) {
            if (_rotorSet.get(i) instanceof Reflector) {
                count++;
            }
        }
        if (count > 1) {
            throw new EnigmaException("Too many reflectors");
        }
    }

    /** Set my rotors according to SETTING, which must be a string of
     *  numRotors()-1 characters in my alphabet. The first letter refers
     *  to the leftmost rotor setting (not counting the reflector).  */
    void setRotors(String setting) {
        if (setting.length() <= _rotorSet.size() - 1) {
            for (int i = 1; i < _rotorSet.size(); i++) {
                if (!(_rotorSet.get(i) instanceof Reflector)) {
                    _rotorSet.get(i).set(
                            _alphabet.toInt(setting.charAt(i - 1)));
                }
            }
        } else {
            throw new EnigmaException("Settings too short");
        }
    }

    /** Set my rotors according to the RINGS setting.  */
    void setRings(String rings) {
        if (rings.length() == _rotorSet.size() - 1) {
            for (int i = 1; i < _rotorSet.size(); i++) {
                if (_rotorSet.get(i) instanceof MovingRotor) {
                    ((MovingRotor) _rotorSet.get(i))
                            .adjustNotches(rings.charAt(i - 1));
                }
                if (!(_rotorSet.get(i) instanceof Reflector)) {
                    int setting = _rotorSet.get(i).setting();
                    setting = setting - alphabet().toInt(rings.charAt(i - 1));
                    if (setting < 0) {
                        setting += alphabet().size();
                    }
                    _rotorSet.get(i).set(setting);
                }
            }
        } else {
            throw new EnigmaException("Rings too short");
        }
    }

    /** Return the current plugboard's permutation. */
    Permutation plugboard() {
        return _plugboard;
    }

    /** Set the plugboard to PLUGBOARD. */
    void setPlugboard(Permutation plugboard) {
        _plugboard = plugboard;
    }

    /** Returns the result of converting the input character C (as an
     *  index in the range 0..alphabet size - 1), after first advancing
     *  the machine. */
    int convert(int c) {
        advanceRotors();
        if (Main.verbose()) {
            System.err.printf("[");
            for (int r = 1; r < numRotors(); r += 1) {
                System.err.printf("%c",
                        alphabet().toChar(getRotor(r).setting()));
            }
            System.err.printf("] %c -> ", alphabet().toChar(c));
        }
        c = plugboard().permute(c);
        if (Main.verbose()) {
            System.err.printf("%c -> ", alphabet().toChar(c));
        }
        c = applyRotors(c);
        c = plugboard().permute(c);
        if (Main.verbose()) {
            System.err.printf("%c%n", alphabet().toChar(c));
        }
        return c;
    }

    /** Advance all rotors to their next position. */
    private void advanceRotors() {
        ArrayList<Rotor> advanced = new ArrayList<Rotor>();
        for (int i = _rotorSet.size() - numPawls();
             i < _rotorSet.size() - 1; i++) {
            if (_rotorSet.get(i + 1).atNotch()
                && !advanced.contains(_rotorSet.get(i))) {
                _rotorSet.get(i).advance();
                advanced.add(_rotorSet.get(i));
                if (i + 1 != _rotorSet.size() - 1) {
                    _rotorSet.get(i + 1).advance();
                    advanced.add(_rotorSet.get(i + 1));
                }
            }
        }
        _rotorSet.get(_rotorSet.size() - 1).advance();
    }

    /** Return the result of applying the rotors to the character C (as an
     *  index in the range 0..alphabet size - 1). */
    private int applyRotors(int c) {
        int cur = c;
        for (int i = _rotorSet.size() - 1; i >= 0; i--) {
            cur = _rotorSet.get(i).convertForward(cur);
        }
        for (int i = 1; i < _rotorSet.size(); i++) {
            cur = _rotorSet.get(i).convertBackward(cur);
        }
        return cur;
    }

    /** Returns the encoding/decoding of MSG, updating the state of
     *  the rotors accordingly. */
    String convert(String msg) {
        String result = "";
        for (int i = 0; i < msg.length(); i++) {
            result += alphabet().toChar
                    (convert(alphabet().toInt(msg.charAt(i))));
        }
        return result;
    }

    /** Common alphabet of my rotors. */
    private final Alphabet _alphabet;
    /** Number of rotors total. */
    private final int _numRotors;
    /** Number of moving rotors. */
    private int _pawls;
    /** List of all rotors. */
    private final ArrayList<Rotor> _allRotors;
    /** List of rotors used in machine. */
    private ArrayList<Rotor> _rotorSet;
    /** Plugboard. */
    private Permutation _plugboard;

}
