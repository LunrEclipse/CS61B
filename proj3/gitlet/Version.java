package gitlet;


import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;

/** Driver class for Gitlet, the tiny stupid version-control system.
 *  @author Brendan Wong
 */
public class Version implements Serializable {
    /** String representing hash of commit.*/
    private String hash;
    /** Message of commit.*/
    private String message;
    /** String representing the timestamp.*/
    private String timestamp;
    /** String representing the branch.*/
    private String branch;
    /** List of parents.*/
    private ArrayList<String> parents;

    /** Mapping of File to Hash of files in current commit.*/
    private HashMap<String, String> files;
    /** Mapping of File to Hash of files to add.*/
    private HashMap<String, String> add;
    /** List of Files to be removed at next commit.*/
    private ArrayList<String> remove;

    /** Static variable representing where the serialized commits are saved.*/
    static final File COMMIT_FOLDER = Utils.join(
            Project.GITLET_FOLDER, "commit");

    /** Static variable representing where the staged files are saved.*/
    static final File STAGING_FOLDER = Utils.join(COMMIT_FOLDER, "staging");

    Version(String newMessage, String newBranch,
            HashMap<String, String> newFiles,
            HashMap<String, String> newAdd, ArrayList<String> newRemove,
            ArrayList<String> newParents) {
        message = newMessage;

        SimpleDateFormat sdf = new SimpleDateFormat("E MMM dd HH:mm:ss yyyy Z");

        if (newParents == null) {
            timestamp = sdf.format(new Date(0));
        } else {
            timestamp = sdf.format(new Date());
        }

        branch = newBranch;
        parents = newParents;
        files = new HashMap<String, String>();
        if (newFiles != null) {
            for (String k : newFiles.keySet()) {
                files.put(k, newFiles.get(k));
            }
        }
        if (newAdd != null) {
            for (String cur : newAdd.keySet()) {
                if (files.containsKey(cur)) {
                    files.replace(cur, newAdd.get(cur));
                } else {
                    files.put(cur, newAdd.get(cur));
                }
            }
        }

        if (newRemove != null) {
            for (int i = 0; i < newRemove.size(); i++) {
                files.remove(newRemove.get(i));
            }
        }
        add = new HashMap<String, String>();
        remove = new ArrayList<>();
        hash = hash();
    }

    public String getBranch() {
        return branch;
    }

    public Version commit(String newMessage, String newBranch) {
        if (add.size() == 0 && remove.size() == 0) {
            System.out.println("No changes added to the commit.");
            System.exit(0);
        }
        ArrayList<String> parent = new ArrayList<>();
        parent.add(hash);
        Version commit =
                new Version(newMessage, newBranch, files, add, remove, parent);
        add.clear();
        remove.clear();
        return commit;
    }

    public Version mergeCommit(String newMessage, String newBranch,
                               ArrayList<String> newParents) {
        Version commit =
                new Version(newMessage, newBranch, files,
                        add, remove, newParents);
        add.clear();
        remove.clear();
        return commit;
    }

    public void stageAdd(File file) {
        if (!file.exists()) {
            System.out.println("File does not exist.");
            System.exit(0);
        }
        String fileHash = Utils.sha1(file.toString()
                + Utils.readContentsAsString(file));
        if (files.containsKey(file.toString())) {
            if (files.get(file.toString()).equals(fileHash)
                    && add.containsKey(file.toString())) {
                add.remove(file.toString());
            } else if (!files.get(file.toString()).equals(fileHash)) {
                File stagingLoc = Utils.join(STAGING_FOLDER, fileHash);
                Utils.writeContents(stagingLoc,
                        Utils.readContentsAsString(file));
                add.put(file.toString(), fileHash);
            }
        } else {
            File stagingLoc = Utils.join(STAGING_FOLDER, fileHash);
            Utils.writeContents(stagingLoc, Utils.readContentsAsString(file));
            add.put(file.toString(), fileHash);
        }

        if (remove.contains(file.toString())) {
            remove.remove(file.toString());
        }
    }

    public void stageRemove(File file) {
        if (add.containsKey(file.toString())) {
            add.remove(file.toString());
        } else if (files.containsKey(file.toString())) {
            remove.add(file.toString());
            file.delete();
        } else {
            System.out.println("No reason to remove the file.");
            System.exit(0);
        }
    }

    public void status(ArrayList<String> allFiles) {
        ArrayList<String> staged = new ArrayList<>();
        ArrayList<String> modified  = new ArrayList<>();
        HashMap<String, Boolean> modificationStatus = new HashMap<>();
        ArrayList<String> untracked = new ArrayList<>();

        for (int i = 0; i < allFiles.size(); i++) {
            File cur = new File(allFiles.get(i));
            if (!remove.contains(cur.toString())) {
                if (add.containsKey(cur.toString())) {
                    if (add.get(cur.toString()).equals(
                            Utils.sha1(cur.toString()
                                    + Utils.readContentsAsString(cur)))) {
                        staged.add(cur.getName());
                    } else {
                        modified.add(cur.getName());
                        modificationStatus.put(cur.getName(), true);
                    }
                } else if (files.containsKey(cur.toString())) {
                    if (!files.get(cur.toString()).equals(
                            Utils.sha1(cur.toString()
                                    + Utils.readContentsAsString(cur)))) {
                        modified.add(cur.getName());
                        modificationStatus.put(cur.getName(), true);
                    }
                } else {
                    untracked.add(cur.getName());
                }
            }
        }

        for (String cur : files.keySet()) {
            if (!allFiles.contains(cur) && !remove.contains(cur)) {
                File temp = new File(cur);
                modified.add(temp.getName());
                modificationStatus.put(temp.getName(), false);
            }
        }

        Collections.sort(staged);
        Collections.sort(modified);
        Collections.sort(untracked);

        printStatus(staged, modified, untracked, modificationStatus);

    }

    private void printStatus(ArrayList<String> staged,
                             ArrayList<String> modified,
                             ArrayList<String> untracked,
                             HashMap<String, Boolean> modificationStatus) {
        System.out.println("=== Staged Files ===");
        for (String name : staged) {
            System.out.println(name);
        }
        System.out.println();

        System.out.println("=== Removed Files ===");
        for (String name : remove) {
            File temp = new File(name);
            System.out.println(temp.getName());
        }
        System.out.println();

        System.out.println("=== Modifications Not Staged For Commit ===");
        for (String name : modified) {
            System.out.print(name);
            if (modificationStatus.get(name)) {
                System.out.print(" (modified)");
            } else {
                System.out.print(" (deleted)");
            }
            System.out.println();
        }
        System.out.println();

        System.out.println("=== Untracked Files ===");
        for (String name : untracked) {
            System.out.println(name);
        }
        System.out.println();
    }

    public boolean untrackedExist(ArrayList<String> allFiles) {
        for (int i = 0; i < allFiles.size(); i++) {
            File cur = new File(allFiles.get(i));
            if (!remove.contains(cur.toString())
                && !add.containsKey(cur.toString())
                    && !files.containsKey(cur.toString())) {
                return true;
            }
        }

        return false;
    }

    public ArrayList<String> getParents() {
        return parents;
    }

    public void clearStage() {
        add.clear();
        remove.clear();
    }

    public void log() {
        String result = "===\n";
        result += "commit " + hash + "\n";
        result += "Date: " + timestamp + "\n";
        result += message + "\n";
        System.out.println(result);

    }

    public static Version fromFile(String filename) {
        File file = Utils.join(COMMIT_FOLDER, filename);
        try {
            return Utils.readObject(file, Version.class);
        } catch (Error e) {
            throw new IllegalArgumentException();
        }
    }

    public void saveVersion() {
        File file = Utils.join(COMMIT_FOLDER, hash);
        Utils.writeObject(file, this);
    }

    public void checkout(File file, String commitHash) {
        String cur = file.toString();
        if (add.containsKey(cur)) {
            add.remove(cur);
        }
        if (remove.contains(cur)) {
            remove.remove(cur);
        }
        String content = Utils.readContentsAsString(Utils.join(
                STAGING_FOLDER, commitHash));
        Utils.writeContents(file, content);
    }

    public void selfCheckout(File file) {
        String cur = file.toString();
        if (!files.containsKey(cur)) {
            System.out.println("File does not exist in that commit.");
            System.exit(0);
        }
        if (add.containsKey(cur)) {
            add.remove(cur);
        }
        if (remove.contains(cur)) {
            remove.remove(cur);
        }
        String fileHash = files.get(cur);
        String content = Utils.readContentsAsString(Utils.join(
                STAGING_FOLDER, fileHash));
        Utils.writeContents(file, content);
    }

    public String getHash() {
        return hash;
    }

    public String getMessage() {
        return message;
    }

    private String hash() {
        String parentString = "0";
        if (parents != null) {
            for (String i : parents) {
                parentString += i;
            }
        }
        String concat = message + timestamp.toString() + branch + parentString;
        return Utils.sha1(concat);
    }

    public HashMap<String, String> getFiles() {
        return files;
    }

    public String getFileHash(String loc) {
        if (files.containsKey(loc)) {
            return files.get(loc);
        } else {
            System.out.println("File does not exist in that commit.");
            System.exit(0);
            return null;
        }
    }

    public void checkoutFiles() {
        for (String loc : files.keySet()) {
            File cur = new File(loc);
            cur.getParentFile().mkdirs();
            String content = Utils.readContentsAsString(Utils.join(
                    STAGING_FOLDER, files.get(loc)));
            Utils.writeContents(cur, content);
        }
    }

    public boolean filesStaged() {
        return add.size() > 0 || remove.size() > 0;
    }
}

