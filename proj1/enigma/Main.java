package enigma;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.NoSuchElementException;

import ucb.util.CommandArgs;

import static enigma.EnigmaException.*;

/** Enigma simulator.
 *  @author Brendan Wong
 */
public final class Main {

    /** Process a sequence of encryptions and decryptions, as
     *  specified by ARGS, where 1 <= ARGS.length <= 3.
     *  ARGS[0] is the name of a configuration file.
     *  ARGS[1] is optional; when present, it names an input file
     *  containing messages.  Otherwise, input comes from the standard
     *  input.  ARGS[2] is optional; when present, it names an output
     *  file for processed messages.  Otherwise, output goes to the
     *  standard output. Exits normally if there are no errors in the input;
     *  otherwise with code 1. */
    public static void main(String... args) {
        try {
            CommandArgs options =
                new CommandArgs("--verbose --=(.*){1,3}", args);
            if (!options.ok()) {
                throw error("Usage: java enigma.Main [--verbose] "
                            + "[INPUT [OUTPUT]]");
            }

            _verbose = options.contains("--verbose");
            new Main(options.get("--")).process();
            return;
        } catch (EnigmaException excp) {
            System.err.printf("Error: %s%n", excp.getMessage());
        }
        System.exit(1);
    }

    /** Open the necessary files for non-option arguments ARGS (see comment
      *  on main). */
    Main(List<String> args) {
        _config = getInput(args.get(0));

        if (args.size() > 1) {
            _input = getInput(args.get(1));
        } else {
            _input = new Scanner(System.in);
        }

        if (args.size() > 2) {
            _output = getOutput(args.get(2));
        } else {
            _output = System.out;
        }
    }

    /** Return a Scanner reading from the file named NAME. */
    private Scanner getInput(String name) {
        try {
            return new Scanner(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Return a PrintStream writing to the file named NAME. */
    private PrintStream getOutput(String name) {
        try {
            return new PrintStream(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Configure an Enigma machine from the contents of configuration
     *  file _config and apply it to the messages in _input, sending the
     *  results to _output. */
    private void process() {
        try {
            Machine enigma = readConfig();
            boolean setup = false;
            while (_input.hasNextLine()) {
                String cur = _input.nextLine();
                if (cur.length() > 0 && cur.charAt(0) == '*') {
                    setUp(enigma, cur);
                    setup = true;
                } else if (setup) {
                    cur = cur.replaceAll(" ", "");
                    String msg = enigma.convert(cur);
                    printMessageLine(msg);
                } else {
                    throw new EnigmaException("No Config");
                }
            }
        } catch (Error e) {
            throw new EnigmaException("Input Error");
        }
    }

    /** Return an Enigma machine configured from the contents of configuration
     *  file _config. */
    private Machine readConfig() {
        try {
            String cur = _config.nextLine();
            _alphabet = new Alphabet(cur);
            int numRotors = _config.nextInt();
            int pawls = _config.nextInt();
            _config.nextLine();
            ArrayList<Rotor> rotors = new ArrayList<Rotor>();
            while (_config.hasNextLine()) {
                Rotor temp = readRotor();
                if (temp != null) {
                    rotors.add(temp);
                }
            }
            return new Machine(_alphabet, numRotors, pawls, rotors);
        } catch (NoSuchElementException excp) {
            throw error("configuration file truncated");
        }
    }

    /** Return a rotor, reading its description from _config. */
    private Rotor readRotor() {
        try {
            if (_config.hasNext()) {
                String name = _config.next();
                String notches = _config.next();
                String cycles = _config.nextLine();
                if (notches.charAt(0) == 'R') {
                    String temp = cycles;
                    temp = temp.replaceAll(" ", "");
                    temp = temp.replaceAll("\\(", "");
                    temp = temp.replaceAll("\\)", "");
                    if (temp.length() < _alphabet.size()) {
                        cycles = cycles + _config.nextLine();
                    }
                    Permutation permutation
                            = new Permutation(cycles, _alphabet);
                    return new Reflector(name, permutation);
                } else if (notches.charAt(0) == 'M') {
                    if (_config.hasNext("\\(.*\\)")) {
                        cycles += _config.nextLine();
                    }
                    Permutation permutation
                            = new Permutation(cycles, _alphabet);
                    return new MovingRotor
                    (name, permutation, notches.substring(1));
                } else {
                    if (_config.hasNext("\\(.*\\)")) {
                        cycles += _config.nextLine();
                    }
                    Permutation permutation =
                            new Permutation(cycles, _alphabet);
                    return new FixedRotor(name, permutation);
                }
            } else {
                _config.nextLine();
                return null;
            }
        } catch (NoSuchElementException excp) {
            throw error("bad rotor description");
        }
    }

    /** Set M according to the specification given on SETTINGS,
     *  which must have the format specified in the assignment. */
    private void setUp(Machine M, String settings) {
        StringTokenizer st = new StringTokenizer(settings, " ");
        st.nextToken();
        String[] rotors = new String[M.numRotors()];
        for (int i = 0; i < M.numRotors(); i++) {
            String temp = st.nextToken();
            rotors[i] = temp;
        }
        M.insertRotors(rotors);
        String temp = st.nextToken();
        M.setRotors(temp);
        if (st.hasMoreTokens()) {
            String rings = st.nextToken();
            if (rings.indexOf('(') == -1) {
                M.setRings(rings);
            }
        }
        if (settings.indexOf('(') != -1) {
            M.setPlugboard(new Permutation(settings.substring
                    (settings.indexOf('(')), _alphabet));
        }
    }

    /** Return true iff verbose option specified. */
    static boolean verbose() {
        return _verbose;
    }

    /** Print MSG in groups of five (except that the last group may
     *  have fewer letters). */
    private void printMessageLine(String msg) {
        while (msg.length() > 5) {
            String temp = msg.substring(0, 5);
            _output.print(temp + " ");
            msg = msg.substring(5);
        }
        _output.println(msg);
    }

    /** Alphabet used in this machine. */
    private Alphabet _alphabet;

    /** Source of input messages. */
    private Scanner _input;

    /** Source of machine configuration. */
    private Scanner _config;

    /** File for encoded/decoded messages. */
    private PrintStream _output;

    /** True if --verbose specified. */
    private static boolean _verbose;
}
