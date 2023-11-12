---
layout: default.md
title: "Developer Guide"
pageNav: 3
---

# ProfBook Developer Guide

<!-- * Table of Contents -->
<page-nav-print />

## **Introduction**

ProfBook is a tool tailored to helping specifically CS2103T professors and Teaching Assistants(TA) in manage
their tutorial slots and groups. As our target users are technophile, we designed and optimised a CLI-based application
specifically for them. It is currently optimised for CS2103T professors and TA with plans to expand to other Computer
Science Modules. Please refer to our [_User Guide_](https://ay2324s1-cs2103t-w15-2.github.io/tp/UserGuide.html) for more
information on ProfBook.

--------------------------------------------------------------------------------------------------------------------

## **Overview**

This guide is intended for future developers, current contributors and users. This guide mainly aims to explain the
implementation of ProfBook to future developers and deepen their knowledge in software development. By the end of this
guide, you can expect to get an overview of the design architecture of ProfBook and comprehensive details of some of its
core features, backed up by UML diagrams.

--------------------------------------------------------------------------------------------------------------------

## **Acknowledgements**

This project is based on
the [AddressBook-Level3](https://github.com/nus-cs2103-AY2122S1/tp) ([UG](https://se-education.org/addressbook-level3/UserGuide.html),
[DG](https://se-education.org/addressbook-level3/DeveloperGuide.html)) project created by
the [SE-EDU initiative](https://se-education.org).

--------------------------------------------------------------------------------------------------------------------

## **Setting up, getting started**

Refer to the guide [_Setting up and getting started_](SettingUp.md).

--------------------------------------------------------------------------------------------------------------------

## **Design**

### Before you read

<box type="info" seamless>

**Note:** The lifeline for many of our diagram should end at the destroy marker (X) but due to a limitation of
PlantUML, the lifeline reaches the end of diagram.
</box>

### Architecture

<puml src="diagrams/ArchitectureDiagram.puml" width="280" />

The ***Architecture Diagram*** given above explains the high-level design of the App.

Given below is a quick overview of main components and how they interact with each other.

**Main components of the architecture**

**`Main`** (consisting of
classes [`Main`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/Main.java)
and [`MainApp`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/MainApp.java)) is
in charge of the app launch and shut down.

* At app launch, it initializes the other components in the correct sequence, and connects them up with each other.
* At shut down, it shuts down the other components and invokes cleanup methods where necessary.

The bulk of the app's work is done by the following four components:

* [**`UI`**](#ui-component): The UI of the App.
* [**`Logic`**](#logic-component): The command executor.
* [**`Model`**](#model-component): Holds the data of the App in memory.
* [**`Storage`**](#storage-component): Reads data from, and writes data to, the hard disk.

[**`Commons`**](#common-classes) represents a collection of classes used by multiple other components.

**How the architecture components interact with each other**

The *Sequence Diagram* below shows how the components interact with each other for the scenario where the user issues
the command `delete 1`.

<puml src="diagrams/ArchitectureSequenceDiagram.puml" width="574" />

Each of the four main components (also shown in the diagram above),

* defines its *API* in an `interface` with the same name as the Component.
* implements its functionality using a concrete `{Component Name}Manager` class (which follows the corresponding
  API `interface` mentioned in the previous point.

For example, the `Logic` component defines its API in the `Logic.java` interface and implements its functionality using
the `LogicManager.java` class which follows the `Logic` interface. Other components interact with a given component
through its interface rather than the concrete class (reason: to prevent outside component's being coupled to the
implementation of a component), as illustrated in the (partial) class diagram below.

<puml src="diagrams/ComponentManagers.puml" width="300" />

The sections below give more details of each component.

### UI component

The **API** of this component is specified
in [`Ui.java`](https://github.com/AY2324S1-CS2103T-W15-2/tp/blob/master/src/main/java/seedu/address/ui/Ui.java)

<puml src="diagrams/UiClassDiagram.puml" alt="Structure of the UI Component"/>

The UI consists of a `MainWindow` that is made up of parts
e.g.`CommandBox`, `ResultDisplay`, `PersonListPanel`, `StatusBarFooter` etc. All these, including the `MainWindow`,
inherit from the abstract `UiPart` class which captures the commonalities between classes that represent parts of the
visible GUI.

The `UI` component uses the JavaFx UI framework. The layout of these UI parts are defined in matching `.fxml` files that
are in the `src/main/resources/view` folder. For example, the layout of
the [`MainWindow`](https://github.com/AY2324S1-CS2103T-W15-2/tp/blob/master/src/main/java/seedu/address/ui/MainWindow.java)
is specified
in [`MainWindow.fxml`](https://github.com/AY2324S1-CS2103T-W15-2/tp/blob/master/src/main/resources/view/MainWindow.fxml)

The `UI` component,

* executes user commands using the `Logic` component.
* listens for changes to `Model` data so that the UI can be updated with the modified data.
* keeps a reference to the `Logic` component, because the `UI` relies on the `Logic` to execute commands.
* depends on some classes in the `Model` component, as it
  displays [`Displayable`](https://github.com/AY2324S1-CS2103T-W15-2/tp/blob/master/src/main/java/seedu/address/ui/Displayable.java)
  object residing in the `Model`.

### Logic component

**API
** : [`Logic.java`](https://github.com/AY2324S1-CS2103T-W15-2/tp/tree/master/src/main/java/seedu/address/logic/Logic.java)

Here's a (partial) class diagram of the `Logic` component:

<puml src="diagrams/LogicClassDiagram.puml" width="550"/>

The sequence diagram below illustrates the interactions within the `Logic` component, taking `execute("rmt 1")` API call
as an example.
`rmt` is the command word for DeleteTaskCommand class. By executing the command `rmt 1`, tha task with index number 1
will be deleted.

<puml src="diagrams/DeleteTaskSequenceDiagram.puml" alt="Interactions Inside the Logic Component for the `rmt 1` Command" />

How the `Logic` component works:

1. When `Logic` is called upon to execute a command, it is passed to an `ProfBookParser` object which in turn creates a
   parser that matches the command (e.g., `DeleteTaskCommandParser`) and uses it to parse the command.
1. This results in a `Command` object (more precisely, an object of one of its subclasses e.g., `DeleteTaskCommand`)
   which is executed by the `LogicManager`.
1. The command can communicate with the `Model` when it is executed (e.g. to delete a task).
1. The result of the command execution is encapsulated as a `CommandResult` object which is returned back from `Logic`.

Here are the other classes in `Logic` (omitted from the class diagram above) that are used for parsing a user command:

<puml src="diagrams/ParserClasses.puml" width="600"/>

How the parsing works:

* When called upon to parse a user command, the `ProfBookParser` class creates an `XYZCommandParser` (`XYZ` is a
  placeholder for the specific command name e.g., `Mark`) which uses the other classes shown above to parse the user
  command and create a `XYZCommand` object (e.g., `MarkCommand`) which the `ProfBookParser` returns back as a `Command`
  object.
* All `XYZCommandParser` classes (e.g., `EditCommandParser`, `HelpCommandParser`, ...) inherit from the `Parser`
  interface so that they can be treated similarly where possible e.g, during testing.

### Model component

**API
** : [`Model.java`](https://github.com/AY2324S1-CS2103T-W15-2/tp/tree/master/src/main/java/seedu/address/model/Model.java)

<puml src="diagrams/ModelClassDiagram.puml" width="550" />


The `Model` component,

* stores the ProfBook data i.e., all `Root, Group and Student` objects (which are contained in a
  hierarchical structure in the ProfBook package).
* stores the currently 'selected' `Displayable` objects (e.g., selected task list, children in current folder)
  as a separate _filtered_ list which is exposed to outsiders as an unmodifiable `ObservableList<Displayable>` that can
  be 'observed' e.g. the UI can be bound to this list so that the UI automatically updates when the data in the list
  change.
* stores a `UserPref` object that represents the user’s preferences. This is exposed to the outside as
  a `ReadOnlyUserPref` objects.
* does not depend on any of the other three components (as the `Model` represents data entities of the domain, they
  should make sense on their own without depending on other components)

<puml src="diagrams/ProfBookClassDiagram.puml" width="650" />

The diagram above shows how the folder structure is implemented in ProfBook,

* The hierarchy is as such: `Root` -> `Group` -> `Student`
* As many of the operations are repeated (e.g., tasks operations and children operation), we decided to abstract out
  these logic into their own classes which is represented by `TaskListManager` and `ChildrenManager` respectively.
* `ChildrenManager` manages the children which is of type `IChildElement`
* We also created a wrapper class (e.g. `ChildrenAndTaskListManager`) for classes that require both of those
  aforementioned functionalities (e.g, `Group` and potentially in the future `TutorialSlot`)

**API
** : [`TaskListManager.java`](https://github.com/AY2324S1-CS2103T-W15-2/tp/blob/master/src/main/java/seedu/address/model/profbook/TaskListManager.java)

<puml src="diagrams/TaskListClassDiagram.puml" width="300" />

How the `Task` component work:

* As mentioned earlier, `TaskListManager` encapsulates the logic required to maintain a TaskList, these logic is
  utilised heavily in the ProfBook component.
* All tasks extend from the abstract class `Task`, we did this so for extensibility. So in the future, should we decide
  to
  implement a new type of task, all we have to do is extend from `Task`.

The sequence diagram below illustrates the interactions within the `Model` component, taking an execution of a
`CreateTodoClass` as example.

<puml src="diagrams/AddTaskSequence.puml" width="550" />

How the `Model` component works:

* Depending on the nature of the command, a static method is called to generate a `TaskOperation` or
  a `ChildrenOperation` Object that acts as an interface Command object to manipulate the Model
* In this case, a `TaskOperation` object is created. This object would store all the necessary information to make
  changes directly on the correct state.
* The command object calls the required method in the `TaskOperation` object which results in the `TaskOperation`
  object adding the Todo task to the group
* Our `TaskOperation` and `ChildOperation` classes follow
  the [facade pattern](https://nus-cs2103-ay2324s1.github.io/website/se-book-adapted/chapters/designPatterns.html#facade-pattern)

<box type="info" seamless>

**Note:** For ChildrenOperation, ModelManager provides more specific static factory methods (e.g., GroupChildOperation,
RootChildOperation) to generate the `ChildOperation` object. It is implemented this way so that ModelManager is able
to check that the Operation required matches with the intended effect of the Command object's Execution

</box>

#### Path package
The `path` package in `model` package serves as a fundamental representation of the hierachical structure within the application. It delineates the navigational paths essential for traversal and dynamic command execution within the system.

Here is a class diagram for the path package:

<puml src="diagrams/PathClassDiagram.puml" width="550" />

1. The `PathElement` class forms the building blocks for constructing paths within ProfBook. 
2. `PathElementType` enum defines the type of elements within a  path:
   * `ROOT`: Represents the root element in the hierarchy.
   * `GROUPID`: Represents the element corresponding to a group in the hierachy.
   * `STUDENTID`: Represents the element corresponding to a student in the hierachy.
   * `PARENT`: Corresponds to the `..` notation, indicating the parent directory.
   * `CURRENT`: Corresponds to the `.` notation, indicating the current directory.
3. `AbsolutePath` represents an absolute path within the system and strictly commences with the `~` element.
   * The `resolve` method is crucial to resolve a `RelativePath` and return the resolved path in `AbsolutePath` type.
   * e.g. Consider an `AbsolutePath` represents `~/grp-001/0001A`. If the `resolve` method is called with the `RelativePath` representing `../grp-002`, the resolve method will return the `AbsolutePath` representing the path `~/grp-002`.

### Storage component

**API
** : [`ProfBookStorageManager.java`](https://github.com/AY2324S1-CS2103T-W15-2/tp/blob/master/src/main/java/seedu/address/storage/ProfBookStorageManager.java)

<puml src="diagrams/StorageClassDiagram.puml" width="550" />

The `Storage` component,

* can save both ProfBook data and user preference data in JSON format, and read them back into corresponding objects.
* inherits from both `ProfBookStorage` and `UserPrefStorage`, which means it can be treated as either one (if only the
  functionality of only one is needed).
* depends on some classes in the `Model` component (because the `ProfBookStorageManager` component's job is to
  save/retrieve objects that belong to the `Model`)

### Common classes

Classes used by multiple components are in the `seedu.addressbook.commons` package.

--------------------------------------------------------------------------------------------------------------------

## **Implementation**

This section describes some noteworthy details on how certain features are implemented.

### Folder structure

#### Implementation

Due to the nature of the application that we aim to make, designing a robust folder structure to represent the hierarchy
is crucial to our success. We strive to design a structure that is generic enough to be extended while also being able
to capture unique functionality of the different parties at play.

In our current hierarchy, `Root` -> `Group` -> `Student`, `Student` and `Group` are required to managed tasks
whereas `Root` and `Group` are required to manage children. The `Model` component briefly mentioned this implementation,
but I will delve into it more comprehensively.

We first created interfaces to represent the required logic for each of the manager, namely `IChildrenManager`
and `ITaskListManager`. Then we created concrete classes such as `ChildrenManager` and `TaskListManager` to encapsulate
the aforementioned logic. The purpose of these classes was so that should a folder type, e.g. `Student`, require a
Manager functionality, we could just extend from said Manager thus reducing on repeated code. Due to the limitation of
Java, classes are not able to extend from multiple classes. To remedy this, we created a wrapper
class, `ChildrenAndTaskListManager`.

It is important to note that `ChildrenManager` is a generic class that accepts classes that implements
the `IChildElement` interface. This was done to reduce repeated code while introducing a degree of polymorphism.

In our implementation, only the parents have reference to the child. This reference is stored by using
a `Map<Id, Student>` and `Map<Id, Group>` instance.

To further illustrate our folder structure, we have prepared this diagram

<puml src="diagrams/AddInitialState.puml" width="550" />

**Aspect: How do we represent the hierarchy**

* **Alternative 1 (current implementation):** Tree representation.
    * Pros: Models the hierarchy closely.
    * Cons: It results in a more rigid hierarchy, harder to extend upon.
* **Alternative 2**: Flat structure.
    * Pros: Easier to implement relatively to the tree representation.
    * Cons: Harder to maintain the hierarchy, search for items and load items from storage

**Aspect: How store reference to children**

* **Alternative 1 (current implementation):** HashMap.
    * Pros: Able to check/find if a student/group is present efficiently, mapping objects by their Id also makes
      executing
      commands in a folder-like structure easier.
    * Cons: Not really any.
* **Alternative 2**: Array.
    * Pros: Very easy to implement
    * Cons: Finding a student/group is very inefficient, updating references is also a hassle.

### Adding a student/group

#### Implementation

Implementation for Creating a student and a group is very similar, so in this guide, I would go through the
implementation for the harder one, which is creating a student. Should you have any questions do feel free to contact
us.

Most of the logic for creating a student is encapsulated in the `CreateStudentCommand` class, this class utilise
the `GroupChildOperation` facade class to add the student to the group and the `Model` class to check for duplicates.
The following methods of `ModelManager` and `GroupChildOperation` are used:

1. `ModelManager::groupChildOperation` - To generate a facade class specific to the current group, it also checks for
   the validity and presence of the specified group.
2. `ModelManager::hasStudentWithId` - To check if the new student id is unique
3. `GroupChildOperation::addChild` - To add the current student into the group

Given below is an example usage scenario on how an existing user can create a student.

1. When the user launches the application, existing information is read from the data file `profbook.json`. The initial
   state would look something like this.

   <puml src="diagrams/AddInitialState.puml" width="550" />

2. Suppose the user is still in the root directory and wants to add a new student to group 1, the user would execute the
   following
   command: `touch ~/grp-001/2000Y --name Bob --email bobby@example.com --phone 92929292 --address blk 258 Toa Payoh`.
   Do note that the only required fields are `--name`
3. The parser would retrieve all the relevant information from the input and encapsulates it in a `CreateStudentCommand`
4. This command would first do these checks:
    * checks if the specified path is a valid student path. This is done via the `Path::isStudentDirectory` method
    * checks if adding the student would result in a duplicate within whole of ProfBook, ie if the student id is
      already taken. This is done via the `ModelManager::hasStudentWithId` method.
5. In this case, if the input was `touch ~/grp-001/1234Y ...` or `touch ~/grp-001/9876A ...` a `CommandException` will
   be thrown.
6. If all checks out, the command would create a new student and add the student to the `Model`. This addition is done
   through getting a `GroupChildOperation` facade class from the `Model::groupChildOperation` method. This would ensure
   the path to the group is present and valid. The student is added through the `GroupChildOperation::addChild` method.
7. It should look something like this.

   <puml src="diagrams/AddFinalState.puml" width="550" />

This sequence diagram shows the general flow of the CreateStudentCommand, for more information on each specific
component, do head over to their respective documentation.

<puml src="diagrams/CreateStudentCommandSequenceDiagram.puml" width="550" />


Below is an activity diagram showing the general activity of the add student command.

<puml src="diagrams/CreateStudentActivityDiagram.puml" width="550" />

#### Design Consideration

**Aspect: Types of fields **

* **Alternative 1 (current implementation):** Fields can be required or optional.
    * Pros: Allows the user to create groups and students without complete information.
    * Cons: Harder to implement features that depends on those optional field.
* **Alternative 2**: Fields must be required.
    * Pros: Easier implementation, there is less need for checking optional field.
    * Cons: All information about a student/group must be readily present before creating it, which is not always the
      case

### Creating a task

#### Implementation

Creating and adding a task is one of the key feature of ProfBook. Currently, we support two types of tasks,
namely `ToDo` and `Deadline` Tasks. Both this tasks extends from the abstract `Task` class which add to its
extensibility. It is important to note that currently, you can only add tasks to Group and Students. Needless to say,
the information for these tasks are encapsulated withing their respective `Task`
instance.

As the implementation for creating a Todo and Deadline task is very similar, I would be bringing you through
the implementation that we found to be more confusing. I would be going through creating a Deadline task and adding it
to *all students in a group*. Illustration in the form of a sequence diagram for creating a Todo and adding a singular
tasks can be found at the `Model` component. For more information you could reach out to us, we would be more than happy
to help.

Most of the logic for creating a task is encapsulated in the `CreateDeadlineCommand` class, this class utilise
the `GroupChildOperation` facade class to add the Deadline to the group and check for duplicates.
The following methods of `ModelManager` and `GroupChildOperation` are used:

1. `ModelManager::groupChildOperation` - To generate a facade class specific to the current group, it also checks for
   the validity and presence of the specified group.
2. `GroupChildOperation::addAllTasks` - To add the tasks to all student within a group, it also checks if it is a
   duplicate task before adding.
3. `GroupChildOperation::checkIfAllChildrenHaveTask` - To check if all children within a group already has the task.

It is important to note that for adding a task to a singular group/student, the facade class `TaskOperation` is used
instead, a sequence diagram illustrating this can be found in the model component.

Given below is an example usage scenario on how an existing user can add Deadline to all students

1. When the user launches the application, existing information is read from the data file `profbook.json`. In our case,
   let us narrow our focus to a specific group with 2 students.

   <puml src="diagrams/DeadlineInitialState.puml" width="550" />

2. Suppose the user is still in the root directory and wants to add a deadline to all students in group1, the user would
   execute the following
   command: `deadline ~/grp-001 --desc Assignment 1 --datetime 2023-12-12 23:59`. More information on the flags could be
   fond in the User Guide.
3. The parser would retrieve all the relevant information from the input and encapsulates it in
   a `CreateDeadlineCommand`
4. This command would first
    * check if the specified path is a valid and present Group path. This is done via `Path::isGroupDirectory` method.
    * check if all students in the group already has the task. This is done
      via `GroupChildOperation::checkIfAllChildrenHaveTask` method.
5. If all checks out, the command would create a new deadline instance and add the deadline to all student that do not
   already have the aforementioned task. This is done
   through getting a `GroupChildOperation` facade class from the `Model::groupChildOperation` method. The tasks is then
   added through the `GroupChildOperation::addTaskToAllStudent` method. For each student, the method would check if the
   task is already present, if not it would add the task.
6. It should look something like this.

   <puml src="diagrams/DeadlineFinalState.puml" width="550" />

7. In the above diagram, Jerry already has the exact deadline and adding it would result in a duplicate. Therefore, the
   task is only added to student Ben.

This sequence diagram illustrates the general flow when adding the deadline task to *all* students, the sequence
diagram for adding a deadline task to a *single* student can be found in the `Model` component.

<puml src="diagrams/CreateDeadlineCommandSequenceDiagram.puml" width="550" />


This is an activity diagram showing the general activity of the add deadline command.

<puml src="diagrams/CreateDeadlineActivityDiagram.puml" width="550" />

#### Design Consideration

**Aspect: Should groups have their own task list as well**

* **Alternative 1 (current implementation):** Yes.
    * Pros: Closely resembles the dynamics in real life
    * Cons: Harder to implement, more confusing for users
* **Alternative 2**: No.
    * Pros: Easier to implement
    * Cons: Users may not be able to keep track of group related tasks

**Aspect: how should we implement adding of bulk tasks**

* **Alternative 1:** Add Tasks one by one to each student.
    * Pros: Quick and easy to implement
    * Cons: keeping track of group tasks is a hassle, any action done for one task must be done for all
* **Alternative 2**: Allow groups to have their own task lists.
    * Pros: Quick and easy to implement
    * Cons: Adding of student tasks must be done manually
* **Alternative 3 (current implementation)**: Implement both of the aforementioned features.
    * Pros: Best of both worlds.
    * Cons: Harder to implement, user command is also more complex and bulky.

### Editing information

#### Implementation

Due to dynamic need of our target users, professors and TAs, there is a need for our edit command to be equally dynamic.
Our edit command need to be general enough to allow the users to edit both students and groups. This is done by checking
the type of directory that was passed in. This is done through the `Path::isGroupDirectory`
and `Path::isStudentDirectory` method. More information on how this is done can be found in the documentation
for `Path` component. This then allows parser to check for the validity of the given flags.

As the implementation for editing students and groups is similar, for simplicity, I would be going through
implementation of editing a group.

The following methods of `ModelManager`, `Path` and `RootChildOperation` are used:

1. `ModelManager::rootChildOperation` - To generate a facade class with logic specific to the current root.
2. `ModelManager::hasGroupWithId` - To check if editing results in a duplicate.
3. `RootChildOperation::editChild` - To edit the group with the values extracted from parser.
4. `Path::isGroupDirectory` - To check if the path leads to a group directory
5. `Path::isStudentDirectory` - To check if the path leads to a student directory

Given below is an example usage scenario on how an existing user can edit the name of a group

1. When the user launches the application, existing information is read from the data file `profbook.json`. The initial
   state should look something like this.
   <puml src="diagrams/AddInitialState.puml" width="550" />
2. Suppose the user is still in the root directory and wants to edit the id of group 1 from `grp-001` to `grp-003`, the
   user would execute the following command: `edit ~/grp-001 -i grp-003`.
3. When parsing the command, from the path, the parser would be able to deduce that we are editing a group. It then
   checks the flags to ensure only required flags are present.
4. The fields to be edited is then stored in an `EditGroupDescriptor` instance. (For student it would be stored in
   an `EditStudentDescriptor`)
5. If the id is being edited, `ModelManager::hasGroupWithId` is called to ensure it does not result in a duplicate.
6. The `RootChildOperation::editChild` then makes a copy of the existing group while updating the values found in
   the `EditGroupDescriptor`.

   <puml src="diagrams/EditIntermediateState.puml" width="550" />

6. It then deletes the old key-value pair in root's `Map<id, group>` and adds the new key-value pair.

   <puml src="diagrams/EditFinalState.puml" width="550" />

This is illustrated by the following sequence diagram. It shows the general flow of the edit through editing a `Group`
instance, this example should be general enough for you to understand how the edit command works on other classes such
as `Student`.

<puml src="diagrams/EditCommandSequenceDiagram.puml" width="550" />

This is an activity diagram showing the general activity of the edit command.

<puml src="diagrams/EditGroupActivityDiagram.puml" width="550" />

#### Design Consideration

**Aspect: How to ensure `edit` is able to handle editing students and group**

* **Alternative 1 (current implementation):** Implement an generic `edit` which acts differently depending on the input
  path.
    * Pros: Closely follows OOP principals and makes `edit` more extensible
    * Cons: Harder to implement, more confusing for users
* **Alternative 2**: Create multiple `edit` commands.
    * Pros: Easier to implement, it is also more intuitive for users
    * Cons: More commands for user to remember, future additions would prove to be a hassle

### Moving students between groups

#### Implementation

Initially, implementing this feature seemed like a daunting task. However, after finalising our `Model` hierarchy, we
realised that implementing move was quite straight forward. Moving a student can be easily done by removing the
student's reference from its current group by removing its key-value pair from the group's `Map<Id, Student>` field.
Then to complete the move, the student is added to the target group by adding it into the target
group's `Map<Id, Student>` field. All of this operation is facilitated by the `GroupChildOperation` facade class.

Given below is an example usage scenario whereby a student is moved from group1 to group2

1. When the user launches the application, existing information is read from the data file `profbook.json`.
2. Suppose the user is still in the root directory and wants to move student `1234Y` from `grp-001` to `grp-002`, the
   user would execute the following command: `mv ~/grp-001/1234Y ~/grp-002`.
3. The parser would extract the relevant information and creates a `MoveStudentCommand` instance.
4. The command would check that path to the student and target group is valid and present.
5. Command would then add the student to the target group via the `GroupChildOperation::addChild` method. The old
   reference is removed via the `GroupChildOperation::deleteChild` method.
6. As uniqueness of student is validated before each student is added, there is no need to check for clashes when
   executing

<puml src="diagrams/MoveStudentSequenceDiagram.puml" width="700" />

--------------------------------------------------------------------------------------------------------------------

## Proposed future features

### Editing tasks

Currently, the only way to edit tasks is by manually deleting and then adding it again. This creates extra hassle for
the user and possible more inconveniences as doing so might change the task's index resulting in the user having
to `cat` again to figure out its new index. We plan to edit the task manually for the user by creating a new command
that deletes and then creates a new task with the edited information while keeping the index the same. Implementing this
is just a combination of deleting and creating, but it improves the user's quality of life greatly.

### Phone number validation

Currently, our application only checks if the phone number is more than 3 characters long. Our current validation is
lacking as users are able to enter a `123` as a phone number or a phone number that is infinitely long. We plan to
improve this validation by enforcing a tighter validation. This can be achieved by creating a `map<String, Integer>` of
common phone extensions to their length and then enforcing that the phone number be of that length. This allows our
users to have the peace of mind that the phone number is validated and robust enough to handle international numbers.

### Better marking and un-marking validation (// TODO low priority, remove when needed )

Currently, our application does not check if the tasks is marked or unmarked before any operation. This resulted in
users being able to mark/unmark tasks infinitely, this is not intuitive and may mislead some users. Hence, we plan to
only allow users to unmark tasks that are marked and vice versa. Also, we plan to add a more descriptive error message
to highlight to the user of the current state of the task.

### More robust duplicate checking

Currently, our application only checks for duplicates by their id, meaning two students/group are considered the same if
and only if their ids are identical. This means that two students with identical number and email but differing ids are
considered different in ProfBook, needless to say this does not reflect requirements in the real world. Therefore, we
plan to revamp our duplication checking for students by checking for equality between their phone number and email.

### More descriptive error message (// TODO low priority, remove when needed )

Currently, while our application tries to output a descriptive and apt message for each error, we have received feedback
that some of our error message could be more descriptive. One such example is trying to edit the root `~/` directory or
trying to edit a directory that does not exist. In both cases, the error message given
is `Path does not exist in ProfBook.`. In this example, we could have mention that you are unable to edit the root
directory for the prior and that the Path does not lead to a student/group for the latter. This is just one example, we
plan to revamp our error message to be more descriptive and user friendly


--------------------------------------------------------------------------------------------------------------------

## **Documentation, logging, testing, configuration, dev-ops**

* [Documentation guide](Documentation.md)
* [Testing guide](Testing.md)
* [Logging guide](Logging.md)
* [Configuration guide](Configuration.md)
* [DevOps guide](DevOps.md)

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Requirements**

### Product scope

**Target user profile**:

* has a need to manage a significant number of students
* has a need to manage various tutorial groups
* has to keep track of deadlines and assign tasks to groups as well as individual students
* prefer and is used to using CLI and Linux commands
* can type fast
* prefers typing to mouse interactions

**Value proposition**: Keep track of groups and students and their deadlines and tasks efficiently with an interface that
will be faster than a typical mouse/GUI driven app

### User stories

Priorities: High (must have) - `* * *`, Medium (nice to have) - `* *`, Low (unlikely to have) - `*`

| Priority level | As a …​          | I want to …​                                                                                                  | So that I can…​                                                               |
|----------------|------------------|---------------------------------------------------------------------------------------------------------------|-------------------------------------------------------------------------------|
| `* * *`        | new user         | see usage instructions                                                                                        | refer to instructions when I forget how to use the App                        |
| `* * *`        | new user         | take advantage of existing Linux skills                                                                       | be more efficient in using the application                                    |
| `* * *`        | new user         | create deadlines for student                                                                                  | keep track of when assignments are due                                        |
| `* * *`        | new user         | create todo task for student                                                                                  | keep track of which label they are at                                         |
| `* * *`        | new user         | create deadline for a group                                                                                   | keep track of when group specific assignments are due                         |
| `* * *`        | new user         | create todo for a group                                                                                       | keep track of the progress of each group relative to others                   |
| `* * *`        | new user         | create deadline for a tutorial group                                                                          | keep track of when tutorial specific assignments are due                      |
| `* * *`        | new user         | create todo tasks for a tutorial group                                                                        | keep track of the progress of each tutorial group relative to others          |
| `* * *`        | new user         | add time to a task                                                                                            | i can record when a task needs to be done                                     |
| `* * *`        | new user         | set alerts and notification                                                                                   | I can receive the notifications of the task                                   |
| `* * *`        | new user         | see the pending task that has the next earliest deadline                                                      | i can know what do do next                                                    |
| `* * *`        | new user         | add the profile picture of students                                                                           | I can better remember them                                                    |
| `* * *`        | new user         | add the matriculation number of students                                                                      | I can update their grade based on the matriculation number                    |
| `* * *`        | new user         | create student profile                                                                                        | manage information of a specific student                                      |
| `* * *`        | new user         | delete a student                                                                                              | remove students that I no longer need                                         |
| `* * *`        | new user         | create group                                                                                                  | manage information of a specific group                                        |
| `* * *`        | new user         | create tutorial slot                                                                                          | manage information fo a specific tutorial slot                                |
| `* * *`        | Experienced user | search for a student/tutorial group by name                                                                   | pull up students/tutorial groups without having to go through the entire list |
| `* * *`        | Experienced user | mark task done for every student in a group                                                                   | I do not need to mark each task manually                                      |
| `* * *`        | Experienced user | add tasks for every student in the book                                                                       | I do not need to add tasks manually                                           |
| `* * *`        | Experienced user | add tasks for every student in a tutorial group                                                               | I do not need to add tasks manually                                           |
| `* * *`        | Experienced user | add tasks for every student in a group                                                                        | I do not need to add tasks manually                                           |
| `* * `         | Experienced user | be able to see an brief overview of last week                                                                 | Can see deadlines that have passed etc                                        |
| `* * `         | Experienced user | easily edit the student/tutorial groups                                                                       | Information is applicable throughout time                                     |
| `* * `         | Experienced user | move students around, edit which group they are in                                                            | I can be flexible with the groupings                                          |
| `* * `         | Expert user      | check last week's deadline have been met or not                                                               | I would not miss any deadline                                                 |
| `* * `         | Expert user      | upload text file contains tutorial groups and students                                                        | I do not need to manually add students and tutorial groups                    |
| `*`            | Experienced user | sync with Google calendar                                                                                     | To keep information easily accessible                                         |
| `* `           | Experienced user | sync with NUSMods                                                                                             | To make it easier to take note of tutorial venues                             |
| `*`            | Experienced user | use ProfBook to track attendance for each tutorial.                                                           | I can easily write down attendance in one area                                |
| `*`            | Expert user      | view a dashboard that provides the overview of the progress of each tutorial group and their respective tasks | I can easily and quickly focus on areas that require my attention             |
| `*`            | Expert user      | let the program generate a detailed report on the performance of each tutorial group                          | I can easily identify the areas of strengths and weaknesses of each students  |
| `*`            | Expert user      | incorporate my own scripts to customise my own experience                                                     | I can improve productivity and tailor fit the software for myself             |
| `*`            | Expert user      | make the program support customizable templates for tutorials and student profiles                            | I can make it more aesthetically pleasing                                     |
| `*`            | Expert user      | make use of the feature to archive certain task                                                               | I can keep track of the history and not focus on unimportant task.            |


### Use cases

(For all use cases below, the **System** is the `ProfBook` and the **Professor** is the `user`, unless specified
otherwise)

**Use case: help**

**MSS**

1. User requests for help.
2. ProfBook displays help message, informs user how to input help with additional information specific to command.

   Use case ends.

**Extensions**

* 2a. User inputs help with specific command.

    * 2a1. ProfBook displays help message specific to input command

      Use case ends.


* 2b. User inputs invalid command together with help.

    * 2b1. ProfBook shows an error message.

      Use case resumes at step 1.

**Use case: Cd into group**

**MSS**

1. User requests to cd into a specific group.
2. ProfBook moves into the directory, display updates to show students within specified group.

   Use case ends.

**Extensions**

* 2a. The path of the group is invalid.

    * 2a1. ProfBook shows an error message.

      Use case resumes at step 1.
  

* 2b. No path provided.
  
  * 2b1. ProfBook shows an error message.

      Use case resumes at step 1.

**Use case: ls to view children**

**MSS**

1. User requests to view children either within current directory or specified path.
2. ProfBook displays list of children.

   Use case ends.

**Extensions**

* 2a. Provided path is invalid.

    * 2a1. ProfBook shows an error message.

      Use case resumes at step 1.

* 2b. Command has no arguments.

    * 2b1. ProfBook shows list of children under current directory.

      Use case ends.

**Use case: View tasks**

**MSS**

1. User requests to view tasks under specified path or current path if not specified.
2. ProfBook displays task list of path, either task list of group or student.

   Use case ends.

**Extensions**

* 2a. The given path is invalid.

    * 2a1. ProfBook shows an error message.

      Use case resumes at step 1.

* 2b1. No path specified.

    * 2b1. Profbook displays task list of current path.

      Use case ends.

**Use case: Clear all entries**

**MSS**

1. User requests to delete all data stored in ProfBook.
2. ProfBook deletes all data stored and displays updates to be empty.

   Use case ends.

**Use case: Exit ProfBook**

**MSS**

1. User requests to exit ProfBook.
2. ProfBook stores all data and closes application.

   Use case ends.

**Use case: Add a student**

**MSS**

1. User requests to add a student.
2. ProfBook deletes the person.

   Use case ends.

**Extensions**

* 2a. The given parameters are invalid.

    * 2a1. ProfBook shows an error message.

      Use case resumes at step 1.

**Use case: Edit a student's details**

**MSS**

1. User requests to edit a student's details.
2. ProfBook edits the students details.

   Use case ends.

**Extensions**

* 2a. The specified path is invalid.

    * 2a1. ProfBook shows an error message.

      Use case resumes at step 1.

* 2b. The given parameter is invalid.

    * 2b1. ProfBook shows an error message.

      Use case resumes at step 1.

**Use case: Edit a group's details**

**MSS**

1. User requests to edit a group's details.
2. ProfBook edits the groups details.

   Use case ends.

**Extensions**


* 2a. The parameter to be changed is invalid.

    * 2a1. ProfBook shows an error message.

      Use case resumes at step 1.

**Use case: Delete a group or student**

**MSS**

1. User requests to delete a group or student.
2. ProfBook removes group or student.

   Use case ends.

**Extensions**


* 2a. The given path is invalid.

    * 2a1. ProfBook shows an error message.

      Use case resumes at step 1.

**Use case: Delete a task**

**MSS**

1. User requests to delete a task with specific id.
2. ProfBook deletes the task.

   Use case ends.

**Extensions**

* 2a. The given id is invalid.

    * 2a1. ProfBook shows an error message.

      Use case resumes at step 1.

**Use case: Move student into/out of group**

**MSS**

1. User requests to move a specific student from a source group to destination group with the path.
2. ProfBook moves the student from a source group to destination group.

   Use case ends.

**Extensions**

* 2a. The given Student path is invalid.
    * 2a1. ProfBook shows an error message.

      Use case resumes at step 1.


* 2b. The given group path is invalid.

    * 2b1. ProfBook shows an error message.

      Use case resumes at step 1.

**Use case: Create a group**

**MSS**

1. User requests to create a group with path and name.
2. ProfBook creates the group.

   Use case ends.

**Extensions**

* 2a. The given name of group is invalid.

    * 2a1. ProfBook shows an error message.

      Use case resumes at step 1.

* 2b. The given path is invalid.

  * 2b1. ProfBook shows an error message.

    Use case resumes at step 1.

**Use case: Create a ToDo**

**MSS**

1. User requests to create a ToDo task.
2. ProfBook creates the Task under specified path/current path.

   Use case ends.

**Extensions**

* 2a. The given path is invalid.

    * 2a1. ProfBook shows an error message.

      Use case resumes at step 1.

* 2b. The given description is invalid.

    * 2b1. ProfBook shows an error message.

       Use case resumes at step 1.
  
* 2c. No path specified, user is in group directory.

    * 2c1. ProfBook creates ToDo for the group.
  
      Use case ends.

* 2d. No path specified, user in root directory.

    * 2d1. ProfBook shows an error message.

      Use case resumes at step 1.

* 2e. User specifies `--all allStu` or `--all allGrp`.

    * 2e1. ProfBook creates Deadline for either all students within the group, or all groups within ProfBook.

      Use case ends.

**Use case: Create a Deadline**

**MSS**

1. User requests to create a Deadline task.
2. ProfBook creates the Task under specified path/current path.

   Use case ends.

**Extensions**

* 2a. The given path is invalid.

    * 2a1. ProfBook shows an error message.

      Use case resumes at step 1.

* 2b. The given description is invalid.

    * 2b1. ProfBook shows an error message.

      Use case resumes at step 1.

* 2c. The given deadline date is invalid.

    * 2c1. ProfBook shows an error message.

      Use case resumes at step 1.

* 2d. No path specified, user in root directory.

    * 2d1. ProfBook creates Deadline for the group.

      Use case ends.

* 2e. No path specified, user in root directory.

    * 2e1. ProfBook shows an error message.

      Use case resumes at step 1.

* 2f. User specifies `--all allStu` or `--all allGrp`.

    * 2f1. ProfBook creates Deadline for either all students within the group, or all groups within ProfBook.

      Use case ends.

**Use case: Mark task**

**MSS**

1. User requests to mark a specific task.
2. ProfBook marks task based on given index, display updates to show as marked.

   Use case ends.

**Extensions**

* 2a. The Index provided is invalid.
    * 2a1. ProfBook shows an error message.

      Use case resumes at step 1.

**Use case: Unmark task**

**MSS**

1. User requests to unmark a specific task.
2. ProfBook unmarks task based on given index, display updates to show as unmarked.

   Use case ends.

**Extensions**

* 2a. The Index provided is invalid.
    * 2a1. ProfBook shows an error message.

      Use case resumes at step 1.

**Use case: Delete task**

**MSS**

1. User requests to delete a specific task.
2. ProfBook delete task based on given index.

   Use case ends.

**Extensions**

* 2a. The Index provided is invalid.
    * 2a1. ProfBook shows an error message.

      Use case resumes at step 1.

### Non-Functional Requirements

**Technical requirements:**
1. The application should be platform-independent and should run on all major operating systems as long as it has
   Java `11` or above installed.
2. The system should work on both 32-bit and 64-bit environments.
3. ProfBook Jar size should not exceed 100MB.
4. ProfBook should be capable of running on various local environments without dependencies on external servers or services.

**Performance requirements:**
1. Should be able to hold up to 1000 Students/Groups without a noticeable sluggishness in performance for typical usage.
2. ProfBook should be able to respond in less than 0.5 seconds.

**Reliability Requirements**:
1. Should not lose any data if application is closed through external means (i.e. not using exit command or exit button).

**Quality requirements:**
1. A user with above average typing speed for regular English text (i.e. not code, not system admin commands) should be
   able to accomplish most of the tasks faster using commands than using the mouse.
2. The system can be used for basic function without reading the user guide.

**Constraints:**
1. Should work without an internet connection.
2. Should be able to support frequent updating of data.

### Glossary

* **Mainstream OS**: Windows, Linux, Unix, OS-X.
* **MSS**: Main success scenario.
* **Extensions**: Variations or alternative flows in a use case that may occur based on certain conditions.
* **Java**: A high level, classed based, object-oriented programming language. Java 11 can be downloaded [here](https://www.oracle.com/java/technologies/downloads/#java11). 
* **Gradle**: Gradle is a build automation tool for multi-language software development. Installation [here](https://gradle.org/install/).
* **CLI**: Command Line Interface, a text-based interface for interacting with a computer program.
* **Architecture Diagram**: A high-level diagram explaining the design architecture of the ProfBook application.
* ***Folder structure***: The organization of directories and subdirectories within ProfBook to represent the hierarchy.
* **UI component**: Manages the user interface of the application, interacting with the Logic component. 
* **Logic component**: Executes user commands, communicates with the Model, and manages the application's logic. 
* **Model component**: Stores and manages ProfBook data, including information about root, groups, and students. 
* **Parser component**: Handles the interpretation of the users input.
* **Storage component**: Handles the storage and retrieval of ProfBook data, interacting with the Model component.
* **Common classes**: Classes shared among multiple components in the ProfBook application.
* **Root**: Directory which contains all groups.
* **Group**: Directory which contains Students within the specific group.
* **Task**: A piece of work or activity to be completed.
* **Path**: A location or address within ProfBook's file hierarchy.
* **Deadline Task**: A type of task with a specific deadline or due date.
* **ToDo Task**: A type of task with no specific deadline, representing things to do.
* **Task Index**: Index of task shown on display of ProfBook after using `cat` command.


--------------------------------------------------------------------------------------------------------------------

## **Appendix: Instructions for manual testing**

Given below are instructions to test the app manually.

<box type="info" seamless>

**Note:** These instructions only provide a starting point for testers to work on;
testers are expected to do more *exploratory* testing.

</box>

### Launch and shutdown

1. Initial launch

    1. Download the jar file and copy into an empty folder

    1. Double-click the jar file Expected: Shows the GUI with a set of sample contacts. The window size may not be
       optimum.

1. Saving window preferences

    1. Resize the window to an optimum size. Move the window to a different location. Close the window.

    1. Re-launch the app by double-clicking the jar file.<br>
       Expected: The most recent window size and location is retained.

1. _{ more test cases …​ }_

### Deleting a person

1. Deleting a person while all persons are being shown

    1. Prerequisites: List all persons using the `list` command. Multiple persons in the list.

    1. Test case: `delete 1`<br>
       Expected: First contact is deleted from the list. Details of the deleted contact shown in the status message.
       Timestamp in the status bar is updated.

    1. Test case: `delete 0`<br>
       Expected: No person is deleted. Error details shown in the status message. Status bar remains the same.

    1. Other incorrect delete commands to try: `delete`, `delete x`, `...` (where x is larger than the list size)<br>
       Expected: Similar to previous.

1. _{ more test cases …​ }_

### Saving data

1. Dealing with missing/corrupted data files

    1. _{explain how to simulate a missing/corrupted file, and the expected behavior}_

1. _{ more test cases …​ }_

<br>

--------------------------------------------------------------------------------------------------------------------

<br>

## **Appendix: Effort**

### **Project Overview**

Our project, ProfBook, underwent a significant transformation with the primary objective of providing robust **student
organization** and **task management** features. The project also aimed to create a **terminal-like application**,
complete with a terminal-like user interface and the ability to navigate through a hierarchical structure.

### **Difficulty Level <span class="badge bg-danger">Challenging</span>**

The project was classified as **challenging** due to:

- **Terminal-Like Functionality:** Creating a terminal-like interface and functionality from scratch was a complex task,
  requiring a deep understanding of terminal behavior and navigation.

- **Hierarchical Structure:** Our application features a hierarchical structure, with ProfBook serving as the root
  entity. Under ProfBook, there are groups, and each group contains students. Managing this hierarchical structure added
  complexity to the project.

- **Diverse Classes:** Unlike AB3, our project needed to handle a more extensive range of classes, including Student,
  Group, and Task. This expansion added complexity as we had to provide functionality for student organization and task
  management within the same application.

### **Effort Required**

The project demanded an estimated total effort of **approximately one month**. The effort was distributed across various
project phases:

- **Design and Architecture:** This phase focused on designing the terminal-like user interface, defining terminal
  behavior, and integrating the hierarchical structure. Additionally, it involved accommodating and ensuring the smooth
  interaction of multiple entity types within the application.

- **Implementation and Coding:** The implementation phase was dedicated to building custom components and
  functionalities essential for realizing terminal behavior, hierarchical structure navigation, and handling diverse
  classes.

- **Testing and Quality Assurance:** This critical phase aimed to ensure the terminal-like interface worked seamlessly,
  the hierarchical structure navigation functionality was error-free, and the application effectively managed the
  different entity types.

- **Documentation:** Preparing comprehensive documentation was essential for guiding both users and developers in
  understanding and utilizing the terminal-like application.

### **Effort Savings through Reuse**

A notable aspect of our project was the efficient use of custom components, which contributed to a significant reduction
in the overall effort.

- **Path Component:** We introduced the `Path` component, which includes subclasses for managing both **absolute** and *
  *relative** paths. This component played a crucial role in managing navigation and executing dynamic commands within
  our application.

- **ChildrenManager Component:** The component was instrumental in representing the hierarchical structure in our
  application. We successfully leveraged this component to perform operations related to child entities, optimizing the
  handling of students within groups and groups within the ProfBook.

- **TaskListManager Component:** This component streamlines task management and allocation by providing a consistent and
  unified interface for handling tasks throughout the application.

Reusing these components enhanced project efficiency and maintainability.
