---
layout: default.md
title: Ching Ming Yuan's Project Portfolio Page
---

### Project: ProfBook

ProfBook is a desktop address book application used for easy management of tasks, group schedules, and notes for every
tutorial slot and group. The user interacts with it using a CLI, and it has a GUI created with JavaFX. It is tailor-made
to mimic the familiar terminal experience for our target users by introducing Linux-style commands, Dynamic commands and
a terminal like structure and interface. It is written in Java, and has about 10 kLoC.

Given below are my contributions to the project.

### **Code contributed**:

[RepoSense link](https://nus-cs2103-ay2324s1.github.io/tp-dashboard/?search=w15&sort=groupTitle&sortWithin=title&timeframe=commit&mergegroup=&groupSelect=groupByRepos&breakdown=true&checkedFileTypes=docs~functional-code~test-code&since=2023-09-22&tabOpen=true&tabType=authorship&tabAuthor=mingyuanc&tabRepo=AY2324S1-CS2103T-W15-2%2Ftp%5Bmaster%5D&authorshipIsMergeGroup=false&authorshipFileTypes=docs~functional-code~test-code&authorshipIsBinaryFileTypeChecked=false&authorshipIsIgnoredFilesChecked=false).

### Features Contribution

* **Designed the ProfBook folder structure**:
  * This folder structure allows our target users to organise their groups and students in an intuitive way
  * justification:
    * The folder structure closely mimics the natural hierarchy that our target user are familiar to.
    * Such a structure would allow us to create more tailor-fit features such as Dynamic command and terminal-like
      structure.
  * Highlights:
    * Deep understanding of OOP principals was needed to craft classes generic yet robust enough to capture the 
      behaviour of each hierarchy. This proved to be difficult as in-depth analysis of design alternative were required
    * Implementation was challenging as it was hard to integrate with our dynamic commands.

### Classes Contribution

* **Introduced `TaskListManager` Class**:
    * Manages the required operations of TaskList, reduces duplicated code.
    * Ensure the expected behaviour with regard to managing the tasks:
        * No Duplicated tasks.
        * Able to accept subclasses of Tasks, more extensible.
        * Other classes not being able to directly modify tasks.

* **Introduced `ChildrenManager` Class**
    * Manages the operation required to modify the children, reduces duplicated code.
    * Ensure the expected behaviour with regard to managing the children.
        * No Duplicate children across the whole ProfBook.
        * Children are identified by their Unique ID.
        * Other classes not being able to directly modify children.

* **Introduced `ChildrenAndTaskListManager` Class**
  * Wrapper class that manages the operations required for both TaskList and Children.

* **Introduced `Student` Class**
  * Encapsulates the required information to uniquely represent a student.

* **Introduced `Group` Class**
  * Encapsulates the required information to uniquely represent a group in a tutorial slot.

* **Introduced `Root` Class**
  * Encapsulates the required information to represent the whole application.

* **Introduced `TaskOperation` and `ChildOperation`  Class**
  * Provided an operation class to allow `Command` logic to manipulate the model without exposing internal details.
  * Justification:
    * Follows OOP principals more closely.
    * Allows for the future migration to an immutable model.
    * Adds another layer of safety by checking the validity and presence of the directory before performing modification.
  * Highlights:
    * Deep-understanding of what is required by `Command` class was required when designing this class.

* **Enhanced `ModelManager` Class**
  * Provides the logic to safely manipulate data stored in Student/Group/Root

<div style="page-break-after: always;"></div>

### Project Management:

* Managed Releases v1.2 on GitHub
* Created and assigned issues for V1.3, V1.4

## Documentations:

### User Guide Contributions
* **Non-Feature Aspects**
  * Transferred initial command description from Google documents into Markdown format.
  * Provided skeleton format for each command so that teammates are able to more efficiently populate their features.
  * Wrote the overview of the whole project.
  * Wrote the overview for all three of our main features.
  * Updated the command summary page.
* **Feature Aspects:**
  * `ls` command.

### Developer Guide Contributions
* **Non-Feature Aspects**
    * Migrated old documents to follow ProfBook new requirements and style.
    * Updated introduction paragraphs .
    * Proofread peer's contributions and performed changes for a more seamless reading experience.
    * Performed final checks before submission.
* **Feature Aspects:**
    * Wrote and create UML for `Model` component including it subpackages such as:
      * `ProfBook`
    * Wrote and created UML for the implementation of
      * Folder structure
      * Moving students
      * Adding a student/group
      * Editing a student/group
      * Adding tasks for all/specific student/group

* **Community**:
    * PRs reviewed (with non-trivial review comments):
      * **[Pull Request #71](https://github.com/AY2324S1-CS2103T-W15-2/tp/pull/71)**
      * **[Pull Request #87](https://github.com/AY2324S1-CS2103T-W15-2/tp/pull/87)**
      * **[Pull Request #113](https://github.com/AY2324S1-CS2103T-W15-2/tp/pull/113)**
      * **[Pull Request #115](https://github.com/AY2324S1-CS2103T-W15-2/tp/pull/115)**
      * **[Pull Request #117](https://github.com/AY2324S1-CS2103T-W15-2/tp/pull/117)**
      * **[Pull Request #118](https://github.com/AY2324S1-CS2103T-W15-2/tp/pull/118)**
      * **[Pull Request #128](https://github.com/AY2324S1-CS2103T-W15-2/tp/pull/128)**
      * **[Pull Request #286](https://github.com/AY2324S1-CS2103T-W15-2/tp/pull/286)**
      * **[Pull Request #289](https://github.com/AY2324S1-CS2103T-W15-2/tp/pull/289)**
    * Contributions to forum:
      * **[Shared my GUI](https://github.com/nus-cs2103-AY2324S1/forum/issues/101#issuecomment-1706285315)**
      * **[Help solved an issue](https://github.com/nus-cs2103-AY2324S1/forum/issues/110#issuecomment-1709913495)**
