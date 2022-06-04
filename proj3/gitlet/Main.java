package gitlet;

/** Driver class for Gitlet, the tiny stupid version-control system.
 *  @author Brendan Wong
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND> .... */
    public static void main(String... args) {
        if (args.length == 0) {
            System.out.println("Please enter a command.");
            System.exit(0);
        }
        switch (args[0]) {
        case "init":
            initialize(args);
            break;
        case "add":
            addFile(args);
            break;
        case "commit":
            commit(args);
            break;
        case "rm":
            removeFile(args);
            break;
        case "log":
            logHistory(args);
            break;
        case "global-log":
            logGlobalHistory(args);
            break;
        case "find":
            findVersion(args);
            break;
        case "status":
            getStatus(args);
            break;
        case "checkout":
            checkout(args);
            break;
        case "branch":
            addBranch(args);
            break;
        case "rm-branch":
            removeBranch(args);
            break;
        case "reset":
            reset(args);
            break;
        case "merge":
            merge(args);
            break;
        case "print":
            print(args);
            break;
        default:
            System.out.println("No command with that name exists.");
            System.exit(0);
        }
    }

    public static void initialize(String[] args) {
        Project project = new Project();
        project.saveProject();
    }

    public static void print(String[] args) {
        Project project = Project.fromFile();
        project.printFiles();
        project.saveProject();
    }

    public static void addFile(String[] args) {
        Project project = Project.fromFile();
        project.stageAdd(args[1]);
        project.saveProject();
    }

    public static void commit(String[] args) {
        if (args.length == 1) {
            throw new GitletException("Please enter a commit message.");
        }
        Project project = Project.fromFile();
        project.commit(args[1]);
        project.saveProject();
    }

    public static void removeFile(String[] args) {
        Project project = Project.fromFile();
        project.stageRemove(args[1]);
        project.saveProject();
    }

    public static void logHistory(String[] args) {
        Project project = Project.fromFile();
        project.log();
        project.saveProject();
    }

    public static void logGlobalHistory(String[] args) {
        Project project = Project.fromFile();
        project.globalLog();
        project.saveProject();
    }

    public static void findVersion(String[] args) {
        Project project = Project.fromFile();
        project.find(args[1]);
        project.saveProject();
    }

    public static void getStatus(String[] args) {
        Project project = Project.fromFile();
        project.status();
        project.saveProject();
    }

    public static void checkout(String[] args) {
        Project project = Project.fromFile();
        if (args.length == 4) {
            if (!args[2].equals("--")) {
                System.out.println("Incorrect operands.");
                System.exit(0);
            }
            project.checkout(args[1], args[3]);
        } else if (args.length == 3) {
            project.checkout(args[2]);
        } else if (args.length == 2) {
            project.changeBranch(args[1]);
        }
        project.saveProject();
    }

    public static void addBranch(String[] args) {
        Project project = Project.fromFile();
        project.newBranch(args[1]);
        project.saveProject();
    }

    public static void removeBranch(String[] args) {
        Project project = Project.fromFile();
        project.removeBranch(args[1]);
        project.saveProject();
    }

    public static void reset(String[] args) {
        Project project = Project.fromFile();
        project.reset(args[1]);
        project.saveProject();
    }

    public static void merge(String[] args) {
        Project project = Project.fromFile();
        project.merge(args[1]);
        project.saveProject();
    }

}
