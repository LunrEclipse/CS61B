package gitlet;

import java.io.File;
import java.io.FilenameFilter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/** Driver class for Gitlet, the tiny stupid version-control system.
 *  @author Brendan Wong
 */
public class Project implements Serializable {

    /** File representing the CWD.*/
    static final File WORKING_FOLDER = new File(".");

    /** Folder representing the .gitlet folder.*/
    static final File GITLET_FOLDER = Utils.join(WORKING_FOLDER, ".gitlet");

    /** File representing the serialized version of this project.*/
    static final File PROJECT_FILE = Utils.join(GITLET_FOLDER, "project");

    /** Mapping of Branch to String of head commit hash.*/
    private HashMap<String, String> recent;
    /** List of all Commit Hashes.*/
    private ArrayList<String> versionHashes;
    /** List of all Branches.*/
    private ArrayList<String> allBranches;
    /** Mapping of Hash to Version object.*/
    private transient HashMap<String, Version> allVersions;
    /** String representing the current branch.*/
    private String curBranch;

    Project() {
        if (!GITLET_FOLDER.exists()) {
            GITLET_FOLDER.mkdir();
            Version.COMMIT_FOLDER.mkdir();
            Version.STAGING_FOLDER.mkdir();
            curBranch = "master";
            HashMap<String, String> files = new HashMap<String, String>();
            Version first = new Version("initial commit", curBranch,
                    files, null, null, null);
            recent = new HashMap<>();
            versionHashes = new ArrayList<>();
            allVersions = new HashMap<>();

            recent.put("master", first.getHash());
            versionHashes.add(first.getHash());
            allVersions.put(first.getHash(), first);
            allBranches = new ArrayList<>();
            allBranches.add("master");
        } else {
            System.out.println("A Gitlet version-control system "
                    + "already exists in the current directory.");
            System.exit(0);
        }
    }

    public void status() {
        System.out.println("=== Branches ===");
        for (String branches : allBranches) {
            if (branches.equals(curBranch)) {
                System.out.println("*" + branches);
            } else {
                System.out.println(branches);
            }
        }
        System.out.println();

        ArrayList<String> allFiles = new ArrayList<>();

        List<String> temp = Utils.plainFilenamesIn(WORKING_FOLDER);
        if (temp != null) {
            for (int i = 0; i < temp.size(); i++) {
                allFiles.add(Utils.join(WORKING_FOLDER,
                        temp.get(i)).toString());
            }
        }

        String[] files = WORKING_FOLDER.list(DIRECTORIES);
        if (files != null && files.length > 0) {
            for (int i = 0; i < files.length; i++) {
                if (!files[i].equals(GITLET_FOLDER.getName())) {
                    fileBrowser(Utils.join(WORKING_FOLDER,
                            files[i]).toString(), allFiles);
                }
            }
        }

        getCurrentVersion().status(allFiles);
    }

    private void fileBrowser(String dir, ArrayList<String> allFiles) {
        List<String> temp = Utils.plainFilenamesIn(dir);
        if (temp != null) {
            for (int i = 0; i < temp.size(); i++) {
                allFiles.add(Utils.join(dir, temp.get(i)).toString());
            }
        }

        String[] files = new File(dir).list(DIRECTORIES);
        if (files != null && files.length > 0) {
            for (int i = 0; i < files.length; i++) {
                fileBrowser(Utils.join(dir, files[i]).toString(), allFiles);
            }
        }
    }



    public void commit(String message) {
        if (message.length() == 0) {
            System.out.println("Please enter a commit message.");
            System.exit(0);
        }
        Version commit = getCurrentVersion().commit(message, curBranch);
        versionHashes.add(commit.getHash());
        allVersions.put(commit.getHash(), commit);
        recent.put(commit.getBranch(), commit.getHash());
    }

    public void stageAdd(String file) {
        Version cur = getCurrentVersion();
        cur.stageAdd(Utils.join(WORKING_FOLDER, file));
    }

    public void stageRemove(String file) {
        Version cur = getCurrentVersion();
        cur.stageRemove(Utils.join(WORKING_FOLDER, file));
    }

    public void printFiles() {
        Version cur = getCurrentVersion();
        HashMap<String, String> files = cur.getFiles();
        System.out.println("Branch: " + curBranch
                + ", Commit: " + cur.getHash());
        System.out.println("===");
        for (String key : files.keySet()) {
            System.out.println("Location: " + key
                    + ", Hash: " + files.get(key));
        }
    }

    public void log() {
        Version cur = getCurrentVersion();
        while (cur != null) {
            cur.log();
            ArrayList<String> parents = cur.getParents();
            if (parents == null || parents.size() == 0) {
                break;
            }
            cur = allVersions.get(parents.get(0));
        }
    }

    public void globalLog() {
        for (String key : allVersions.keySet()) {
            allVersions.get(key).log();
        }
    }

    public void find(String message) {
        int count = 0;
        for (String key : allVersions.keySet()) {
            if (allVersions.get(key).getMessage().equals(message)) {
                System.out.println(allVersions.get(key).getHash());
                count++;
            }
        }
        if (count == 0) {
            System.out.println("Found no commit with that message.");
            System.exit(0);
        }
    }

    public void newBranch(String branch) {
        if (recent.containsKey(branch)) {
            System.out.println("A branch with that name already exists.");
            System.exit(0);
        }
        Version cur = getCurrentVersion();
        recent.put(branch, cur.getHash());
        allBranches.add(branch);
    }

    private Version getCurrentVersion() {
        return allVersions.get(recent.get(curBranch));
    }

    public Version hashToVersion(String hash) {
        return allVersions.get(hash);
    }

    public static Project fromFile() {
        if (!GITLET_FOLDER.exists()) {
            System.out.println("Not in an initialized Gitlet directory.");
            System.exit(0);
        }
        File file = PROJECT_FILE;
        try {
            Project cur = Utils.readObject(file, Project.class);
            cur.loadVersions();
            return cur;
        } catch (Error e) {
            throw new IllegalArgumentException();
        }
    }

    public void saveProject() {
        File file = PROJECT_FILE;
        Utils.writeObject(file, this);
        for (String key : allVersions.keySet()) {
            allVersions.get(key).saveVersion();
        }
    }

    public void loadVersions() {
        allVersions = new HashMap<>();
        for (int i = 0; i < versionHashes.size(); i++) {
            Version temp = Version.fromFile(versionHashes.get(i));
            allVersions.put(versionHashes.get(i), temp);
        }
    }

    public void removeBranch(String branch) {
        if (branch.equals(curBranch)) {
            System.out.println("Cannot remove the current branch.");
            System.exit(0);
        }
        if (!recent.containsKey(branch)) {
            System.out.println("A branch with that name does not exist.");
            System.exit(0);
        }
        recent.remove(branch);
        allBranches.remove(branch);
    }

    public void changeBranch(String branch) {
        if (branch.equals(curBranch)) {
            System.out.println("No need to checkout the current branch.");
            System.exit(0);
        }
        if (!recent.containsKey(branch)) {
            System.out.println("No such branch exists.");
            System.exit(0);
        }

        if (hasTrackedFiles(curBranch)) {
            System.out.println("There is an untracked file in the way; "
                    + "delete it, or add and commit it first.");
            System.exit(0);
        }


        getCurrentVersion().clearStage();
        Version cur = allVersions.get(recent.get(branch));

        List<String> temp = Utils.plainFilenamesIn(WORKING_FOLDER);
        if (temp != null) {
            for (int i = 0; i < temp.size(); i++) {
                Utils.join(WORKING_FOLDER, temp.get(i)).delete();
            }
        }

        String[] files = WORKING_FOLDER.list(DIRECTORIES);
        if (files != null && files.length > 0) {
            for (int i = 0; i < files.length; i++) {
                if (!files[i].equals(GITLET_FOLDER.getName())) {
                    deleteHelper(files[i]);
                }
            }
        }

        cur.checkoutFiles();
        curBranch = branch;
    }

    private boolean hasTrackedFiles(String branch) {
        ArrayList<String> allFiles = new ArrayList<>();

        List<String> temp = Utils.plainFilenamesIn(WORKING_FOLDER);
        if (temp != null) {
            for (int i = 0; i < temp.size(); i++) {
                allFiles.add(Utils.join(WORKING_FOLDER,
                        temp.get(i)).toString());
            }
        }

        String[] files = WORKING_FOLDER.list(DIRECTORIES);
        if (files != null && files.length > 0) {
            for (int i = 0; i < files.length; i++) {
                if (!files[i].equals(GITLET_FOLDER.getName())) {
                    fileBrowser(Utils.join(WORKING_FOLDER,
                            files[i]).toString(), allFiles);
                }
            }
        }

        return allVersions.get(recent.get(branch)).untrackedExist(allFiles);
    }

    public void reset(String id) {

        if (hasTrackedFiles(curBranch)) {
            System.out.println("There is an untracked file in the way; "
                    + "delete it, or add and commit it first.");
            System.exit(0);
        }

        String found = null;
        id = id.substring(0, 6);
        for (int i = 0; i < versionHashes.size(); i++) {
            if (versionHashes.get(i).substring(0, 6).equals(id)) {
                found = versionHashes.get(i);
            }
        }
        if (found == null) {
            System.out.println("No commit with that id exists.");
            System.exit(0);
        }

        getCurrentVersion().clearStage();
        Version cur = allVersions.get(found);

        List<String> temp = Utils.plainFilenamesIn(WORKING_FOLDER);
        if (temp != null) {
            for (int i = 0; i < temp.size(); i++) {
                Utils.join(WORKING_FOLDER, temp.get(i)).delete();
            }
        }

        String[] files = WORKING_FOLDER.list(DIRECTORIES);
        if (files != null && files.length > 0) {
            for (int i = 0; i < files.length; i++) {
                if (!files[i].equals(GITLET_FOLDER.getName())) {
                    deleteHelper(Utils.join(WORKING_FOLDER,
                            files[i]).toString());
                }
            }
        }

        cur.checkoutFiles();

        recent.replace(curBranch, cur.getHash());


    }

    private void deleteHelper(String dir) {
        List<String> temp = Utils.plainFilenamesIn(dir);
        if (temp != null) {
            for (int i = 0; i < temp.size(); i++) {
                Utils.join(dir, temp.get(i)).delete();
            }
        }

        String[] files = new File(dir).list(DIRECTORIES);
        if (files != null && files.length > 0) {
            for (int i = 0; i < files.length; i++) {
                deleteHelper(Utils.join(dir, files[i]).toString());
            }
        }

        new File(dir).delete();
    }

    public void checkout(String id, String file) {
        String temp = null;
        id = id.substring(0, 6);
        for (int i = 0; i < versionHashes.size(); i++) {
            if (versionHashes.get(i).substring(0, 6).equals(id)) {
                temp = versionHashes.get(i);
            }
        }
        if (temp == null) {
            System.out.println("No commit with that id exists.");
            System.exit(0);
        }
        File fileLoc = Utils.join(WORKING_FOLDER, file);
        String hash = allVersions.get(temp).getFileHash(fileLoc.toString());
        getCurrentVersion().checkout(fileLoc, hash);
    }

    public void checkout(String file) {
        getCurrentVersion().selfCheckout(Utils.join(WORKING_FOLDER, file));
    }

    public void merge(String branch) {
        Version main = getCurrentVersion();
        Version given = allVersions.get(recent.get(branch));
        mergeCheckError(main, given, branch);
        Version split = findSplitPoint(branch);
        splitCheckError(main, given, split, branch);
        HashMap<String, String> givenFiles =
                allVersions.get(recent.get(branch)).getFiles();
        HashMap<String, String> currentFiles = main.getFiles();
        HashMap<String, String> splitFiles = split.getFiles();
        boolean conflictExists = checkCurrentBranch(splitFiles, currentFiles,
                givenFiles, main, false, branch);
        conflictExists = checkGivenBranch(splitFiles, currentFiles, givenFiles,
                main, conflictExists);
        mergeCommit(branch);
        if (conflictExists) {
            System.out.println("Encountered a merge conflict.");
        }
    }

    private boolean checkCurrentBranch(HashMap<String, String> splitFiles,
                                    HashMap<String, String> currentFiles,
                                    HashMap<String, String> givenFiles,
                                    Version main, boolean conflictExists,
                                       String branch) {
        for (String file : currentFiles.keySet()) {
            if (splitFiles.get(file) != null && givenFiles.get(file) != null) {
                if (splitFiles.get(file).equals(currentFiles.get(file))
                        && !splitFiles.get(file).equals(givenFiles.get(file))) {
                    unchangedCurrentChangedGiven(file,
                            main, givenFiles.get(file));
                } else if (!splitFiles.get(file).equals(currentFiles.get(file))
                        && splitFiles.get(file).equals(givenFiles.get(file))) {
                    continue;
                } else if (!(splitFiles.get(file).equals(givenFiles.get(file))
                        && givenFiles.get(file).equals(
                                currentFiles.get(file)))) {
                    conflictExists = true;
                    conflict(file, currentFiles.get(file),
                            givenFiles.get(file), main);
                }
            } else if (givenFiles.get(file) != null) {
                if (givenFiles.get(file).equals(currentFiles.get(file))) {
                    continue;
                } else {
                    conflictExists = true;
                    conflict(file, currentFiles.get(file),
                            givenFiles.get(file), main);
                }
            } else if (splitFiles.get(file) != null) {
                if (splitFiles.get(file).equals(currentFiles.get(file))) {
                    if (branch.equals("given")) {
                        conflictExists = true;
                        conflict(file, currentFiles.get(file), null, main);
                    } else {
                        main.stageRemove(new File(file));
                    }
                } else {
                    conflictExists = true;
                    conflict(file, currentFiles.get(file), null, main);
                }
            }
        }
        return conflictExists;
    }

    private boolean checkGivenBranch(HashMap<String, String> splitFiles,
                                       HashMap<String, String> currentFiles,
                                       HashMap<String, String> givenFiles,
                                       Version main, boolean conflictExists) {
        for (String file : givenFiles.keySet()) {
            if (splitFiles.get(file) != null && currentFiles.get(file) == null
                    && splitFiles.get(file).equals(givenFiles.get(file))) {
                continue;
            } else if (currentFiles.get(file) == null
                    && splitFiles.get(file) == null) {
                File temp = Utils.join(
                        Version.STAGING_FOLDER, givenFiles.get(file));
                String contents = Utils.readContentsAsString(temp);
                Utils.writeContents(new File(file), contents);
                main.stageAdd(new File(file));
            } else if (currentFiles.get(file) == null) {
                conflictExists = true;
                conflict(file, currentFiles.get(file),
                        givenFiles.get(file), main);
            }
        }
        return conflictExists;
    }

    private void mergeCommit(String branch) {
        String message = "Merged " + branch + " into " + curBranch + ".";
        ArrayList<String> parents = new ArrayList<>();
        parents.add(recent.get(curBranch));
        parents.add(recent.get(branch));
        Version commit = getCurrentVersion().mergeCommit(
                message, curBranch, parents);
        versionHashes.add(commit.getHash());
        allVersions.put(commit.getHash(), commit);
        recent.put(commit.getBranch(), commit.getHash());
    }

    private void unchangedCurrentChangedGiven(String file,
                                              Version current, String hash) {
        File temp = Utils.join(Version.STAGING_FOLDER, hash);
        String contents = Utils.readContentsAsString(temp);
        Utils.writeContents(new File(file), contents);
        current.stageAdd(new File(file));
    }

    private void splitCheckError(Version main, Version given,
                                 Version split, String branch) {
        if (split.getHash().equals(given.getHash())) {
            System.out.println(""
                    + "Given branch is an ancestor of the current branch.");
            System.exit(0);
        }
        if (split.getHash().equals(main.getHash())) {
            changeBranch(branch);
            System.out.println("Current branch fast-forwarded.");
            System.exit(0);
        }
    }

    private void mergeCheckError(Version main, Version given, String branch) {
        if (branch.equals(curBranch)) {
            System.out.println("Cannot merge a branch with itself.");
            System.exit(0);
        }
        if (!recent.containsKey(branch)) {
            System.out.println("A branch with that name does not exist.");
            System.exit(0);
        }
        if (main.filesStaged() || given.filesStaged()) {
            System.out.println("You have uncommitted changes.");
            System.exit(0);
        }
        if (branch.equals(curBranch)) {
            System.out.println("A branch with that name does not exist.");
            System.exit(0);
        }

        if (hasTrackedFiles(curBranch)) {
            System.out.println("There is an untracked file in the way; "
                    + "delete it, or add and commit it first.");
            System.exit(0);
        }
    }

    private void conflict(String loc, String hashCur,
                          String hashGiv, Version current) {
        File fileLoc = new File(loc);
        String contents = "<<<<<<< HEAD\n";
        if (hashCur != null) {
            contents = contents
                    + Utils.readContentsAsString(
                            Utils.join(Version.STAGING_FOLDER, hashCur));
            contents += "=======\n";
        } else {
            contents += "=======\n";
        }
        if (hashGiv != null) {
            contents = contents
                    + Utils.readContentsAsString(
                            Utils.join(Version.STAGING_FOLDER, hashGiv));
            contents += ">>>>>>>\n";
        } else {
            contents += ">>>>>>>\n";
        }
        Utils.writeContents(fileLoc, contents);
        current.stageAdd(new File(loc));
    }

    private Version findSplitPoint(String branch) {
        Version main = getCurrentVersion();
        ArrayList<String> mainParents = new ArrayList<>();
        mainParents.add(main.getHash());
        getParents(main, mainParents);

        Version cur = allVersions.get(recent.get(branch));

        HashMap<String, Integer> hashToLevel = new HashMap<>();
        String res = null;
        checkParents(cur, mainParents, hashToLevel, 1);
        int lowest = Integer.MAX_VALUE;
        for (String keys : hashToLevel.keySet()) {
            if (hashToLevel.get(keys) < lowest) {
                res = keys;
                lowest = hashToLevel.get(keys);
            }
        }
        if (res == null) {
            return null;
        } else {
            return allVersions.get(res);
        }
    }

    private void getParents(Version ver, ArrayList<String> parents) {
        if (ver != null && ver.getParents() != null) {
            if (ver.getParents().size() == 2) {
                Version parent1 = allVersions.get(ver.getParents().get(0));
                Version parent2 = allVersions.get(ver.getParents().get(1));
                parents.add(parent1.getHash());
                parents.add(parent2.getHash());
                getParents(parent1, parents);
                getParents(parent2, parents);
            } else {
                Version parent1 = allVersions.get(ver.getParents().get(0));
                parents.add(parent1.getHash());
                getParents(parent1, parents);
            }
        }
    }

    private void checkParents(Version ver, ArrayList<String> parents,
                              HashMap<String, Integer> hashToLevel, int level) {
        if (ver != null && ver.getParents() != null) {
            if (parents.contains(ver.getHash())) {
                hashToLevel.put(ver.getHash(), level);
            } else if (ver.getParents().size() == 2) {
                Version parent1 = allVersions.get(ver.getParents().get(0));
                Version parent2 = allVersions.get(ver.getParents().get(1));
                checkParents(parent1, parents, hashToLevel, level++);
                checkParents(parent2, parents, hashToLevel, level++);
            } else {
                Version parent1 = allVersions.get(ver.getParents().get(0));
                checkParents(parent1, parents, hashToLevel, level++);
            }
        } else if (ver != null) {
            if (parents.contains(ver.getHash())) {
                hashToLevel.put(ver.getHash(), level);
            }
        }
    }

    /** Filer used to get all directories in a directory.*/
    private static final FilenameFilter DIRECTORIES =
            new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    return new File(dir, name).isDirectory();
                }
            };
}
