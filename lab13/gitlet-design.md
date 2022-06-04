# Gitlet Design Document
author: Brendan Wong

## 1. Classes and Data Structures

###Project

**Fields**

1`HashMap<String, String> recent`: A map presenting the mapping from a branch name to the hash of the working head
2`ArrayList<String> versionHashes`: List of the hashes of all versions
3`HashMap<String, Version> allVersions`: A map representing a mapping from the hash to the version instance
4`String curBranch`: Name of the branch that currently working on

###Version

**Fields**

1. `String hash`: Hash of the version
2. `ArrayList<String> tags`: Tags denoted to the version
3. `String message`: Commit message on the version
4. `String timestamp`: Timestamp of commit
5. `String branch`: Name of the branch the version is in
6. `String nextBranch`: Name of the branch next commit will get pushed to
7. `ArrayList<String> parents`: List of the version's parent's hash
8. `HashMap<File, String> files`: Map of all files and their associated contents
9. `ArrayList<Blob> add`: Files staged to be added to the next version
10. `ArrayList<File> remove`: Files staged to be removed from the next version

###Blob

**Fields**

1. `String files`: content of the file
2. `File location`: location of the file


## 2. Algorithms

###Project
1. init - `new Project()`: Creates a new project and initializes needed directory such as `./gitlet` and sets the working branch to "Main" as well as creating Version 1 of the project
2. global-log - `globalLog()`: Loop through allVersions and print out the information including hash, timestamp, and commit message
3. find - `find(String _message)`: Loop through allVersions and print out the hash of all versions that have the same hash
4. status - `status()`: Loop through the keys in recent for branch names and prints the information of add, remove, and modified in the most recent version on the current branch
5. checkout - `changeBranch(String newBranch):` - Changes the branch to the newBranch if it exists
6. checkout - `checkout(String hash, String filename):` Loop through all Versions until you get to the hash and return the file from that version. Override (or create) the file in the working directory.
7. branch - `newBranch(String branch)`: Calls setNextBranch on the most recent version. Also add branch to recent and have it refer to the hash of the curDir
8. rm-branch - `removeBranch(String branch)`: Removes all Versions with said branch from allVersions. Also deletes key from recent.
9. reset - `reset(String hash)`: Unstage everything in your current version. Call changeBranch on the new commit id.
10. merge - `merge(String branch)`: Merges the curBranch with the provided branch in string. Finds common ancestor between curBranch and given branch name. Files changed in givenBranch, but not curBranch are overrided to what they are in the givenBranch. Changes made to the curBranch are left the same while other in givenBranch are staged. Anything else is considered in conflict and needs to be reviewd.

###Version
1. add - `stageAdd(String files)`: Create a blob out of the current file and saves it to the current Version
2. commit - `commit(String name)`: Creates a new Version instance  where we clone all the files in our current head and adds/removes the files provided in the add/remove array. Sets the parent to the current version. Removes the directory `./gitlet/[VersionHash]-additions` and clears add/remove list. Returns the new version.
3. rm - `stageRemove(String files)`: If file is in add array, remove it. Also remove from the staging directory. Else add it to the remove array to be removed during the next commit.
4. log - `log()`: Starting from the working head, recursively loop through parents and print their hash, timestamp, and commit message
5. status - `status()`: Print out all the files added in add array, files removed in remove array, and unstaged in modified array
6. branch - `setNextBranch(String branch):` Sets nextBranch to branch

## 3. Persistence

My `./gitlet` folder is structured into three separate categories. The first is the `project` file which represents the serialization of the project class which acts as an overarching controller of the commits and versions, managing things such as branch and current version.
After this is the `./gitlet/commit` folder which contains all the serializations of the versions. Project stores an internal Array of all the serializations of the versions, so when Project is deserialized, it loops through `./gitlet/commit` and serializes the versions. Versions also store additions and their contents in the from of blobs. Each version as their own `./gitlet/commit/[VersionHash]-additions` folder which stores the serialized versions of these staged blobs to be added.

## 4. Design Diagram

https://drive.google.com/file/d/1yL6wP5wzyZuYg9auUvZMmeEWWhNxCSVw/view?usp=sharing

