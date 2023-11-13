---
layout: default.md
title: Nereus Ng Wei Bin's Project Portfolio Page
---

## ProfBook

ProfBook is a desktop address book application used for easy management of tasks, group schedules, and notes for every
tutorial slot and group. The user interacts with it using a CLI, and it has a GUI created with JavaFX. It is written in
Java, and has about 10 kLoC.

#### [Link to RepoSense Report :link:](https://nus-cs2103-ay2324s1.github.io/tp-dashboard/?search=nereuswb922&breakdown=false&sort=groupTitle%20dsc&sortWithin=title&since=2023-09-22&timeframe=commit&mergegroup=&groupSelect=groupByRepos)

### Class Enhancements

1. **Introduced `Path` Class :**
    - Provided a structured way to manage and manipulate paths in our application.
    - This class assists other classes in providing functionality such as navigating
      between directories and executing commands against specific paths.
2. **Introduced `Id` class :**
    - Represent unique id of group and student.
3. **Introduced `Option` class :**
    - Similar to AB3 `Prefix` class, but with shorthand and long-hand names.
    - e.g. Name option can be passed using option `--name` or `-n`.
4. **Introduced `Displayable` interface :**
    - Classes that implement this interface can be displayed in the `ItemListPanel`.
    - e.g. `ToDo`, `Deadline`, `Student` and `Group`.
5. **Refactored `ArgumentTokenizer` class :**
    - Refactored `ArgumentTokenizer` class to adopt `Option` class.
6. **Added validation for options:**
    - Implemented validation to check for invalid options in every command.
    - An error will be thrown if the user provides an option that is not applicable for a specific command.

### New Features

1. **`cd` Command :** Allows users to navigate between different directories.
2. **`ls` Command :** Shows a list of directories under the current directory.
3. **`cat` Command :** Displays the task list under a specific path.
4. **`rmt` Command :** Deletes task with the given display index.
5. **`--help` Option :** Provides a detailed usage information for each command.

### GUI Enhancements

- **Added new icon** and **expanded the color palette** to enhance the project's visual appeal.
- Show the **current directory** and **display directory** for enhanced user orientation in our application.

### User Guide Contributions

coming soon

### Developer Guide Contributions

coming soon

### Team-Based Task Contributions

- Established our team's **organization** and **repo** on GitHub.
- Set up a **GitHub project** to manage issues related to user stories and tasks.
- Setup **project website deployment**.
- Integrated **Netlify** to enable automatic deployments of our PR previews.
- Integrated **Codecov** to automate code coverage assessment of PR.
- Wrapped up milestone v1.1 and released JAR file.
- Recorded video for v1.2 demo.
- Migrated documentation to **MarkBind** with initial site-wide configuration.

### Review and Mentoring Contributions

Here are the pull requests I reviewed and provided valuable feedback and suggestions:
<div class="pull-request-container">

 **[Pull Request #71](https://github.com/AY2324S1-CS2103T-W15-2/tp/pull/71)** |
 **[Pull Request #91](https://github.com/AY2324S1-CS2103T-W15-2/tp/pull/91)** |
 **[Pull Request #100](https://github.com/AY2324S1-CS2103T-W15-2/tp/pull/100)** |
 **[Pull Request #102](https://github.com/AY2324S1-CS2103T-W15-2/tp/pull/102)** |
 **[Pull Request #113](https://github.com/AY2324S1-CS2103T-W15-2/tp/pull/113)** |
 **[Pull Request #115](https://github.com/AY2324S1-CS2103T-W15-2/tp/pull/115)** |

</div>
