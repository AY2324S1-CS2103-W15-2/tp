package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static seedu.address.logic.Messages.MESSAGE_PATH_NOT_FOUND;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CreateDeadlineCommand.MESSAGE_ALL_CHILDREN_HAVE_TASK;
import static seedu.address.logic.commands.CreateDeadlineCommand.MESSAGE_INVALID_PATH_FOR_ALL_GROUP;
import static seedu.address.logic.commands.CreateDeadlineCommand.MESSAGE_INVALID_PATH_FOR_ALL_STU;
import static seedu.address.logic.commands.CreateDeadlineCommand.MESSAGE_SUCCESS_ALL_GROUPS_WITH_WARNING;
import static seedu.address.logic.commands.CreateDeadlineCommand.MESSAGE_SUCCESS_ALL_STUDENTS;
import static seedu.address.logic.commands.CreateDeadlineCommand.MESSAGE_SUCCESS_ALL_STUDENTS_WITH_WARNING;
import static seedu.address.logic.commands.CreateDeadlineCommand.MESSAGE_SUCCESS_GROUP;
import static seedu.address.logic.commands.CreateDeadlineCommand.MESSAGE_TASK_CREATION_FOR_ROOT;
import static seedu.address.logic.commands.CreateDeadlineCommand.MESSAGE_USAGE;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.ChildOperation;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.TaskOperation;
import seedu.address.model.UserPrefs;
import seedu.address.model.path.AbsolutePath;
import seedu.address.model.path.RelativePath;
import seedu.address.model.path.exceptions.InvalidPathException;
import seedu.address.model.profbook.Group;
import seedu.address.model.profbook.Root;
import seedu.address.model.profbook.Student;
import seedu.address.model.task.Deadline;
import seedu.address.testutil.TypicalGroups;
import seedu.address.testutil.TypicalRoots;
import seedu.address.testutil.TypicalStudents;
import seedu.address.testutil.TypicalTasks;

class CreateDeadlineCommandTest {
    private Model model;
    private Model expectedModel;
    private AbsolutePath rootPath = CommandTestUtil.getValidRootAbsolutePath();
    private Deadline toBeAdded = TypicalTasks.DEADLINE_1;

    @BeforeEach
    public void setup() {
        model = new ModelManager(rootPath, new Root(TypicalRoots.PROFBOOK_WITH_TWO_GROUPS), new UserPrefs());
        expectedModel = new ModelManager(rootPath, new Root(TypicalRoots.PROFBOOK_WITH_TWO_GROUPS), new UserPrefs());
    }

    @Test
    public void constructor_nullAbsolutePath_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () ->
                new CreateDeadlineCommand(null, toBeAdded, Category.NONE));
    }

    @Test
    public void constructor_nullTodo_throwsNullPointerException() throws InvalidPathException {
        Group targetGroup = TypicalGroups.GROUP_ONE;

        RelativePath groupPath = new RelativePath(targetGroup.getId().toString());
        AbsolutePath absoluteTargetPath = rootPath.resolve(groupPath);

        assertThrows(NullPointerException.class, () ->
                new CreateDeadlineCommand(absoluteTargetPath, null, Category.ALLSTU));
    }

    @Test
    public void constructor_nullCategory_throwsNullPointerException() throws InvalidPathException {
        Group targetGroup = TypicalGroups.GROUP_ONE;

        RelativePath groupPath = new RelativePath(targetGroup.getId().toString());
        AbsolutePath absoluteTargetPath = rootPath.resolve(groupPath);

        assertThrows(NullPointerException.class, () ->
                new CreateDeadlineCommand(absoluteTargetPath, toBeAdded, null));
    }

    @Test
    public void constructor_nullArgs_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new CreateDeadlineCommand(null, null, null));
    }

    @Test
    public void execute_invalidPathForNoneCategory_throwsCommandException() throws InvalidPathException {
        // Test the case where the path doesn't exist
        AbsolutePath target = new AbsolutePath("~");
        CreateDeadlineCommand createDeadlineCommand = new CreateDeadlineCommand(target, toBeAdded, Category.NONE);
        String expectedMessage = MESSAGE_INVALID_PATH_FOR_ALL_STU;

        assertCommandFailure(createDeadlineCommand, model, expectedMessage);
    }

    @Test
    public void execute_invalidPathForAllStu_throwsCommandException() throws InvalidPathException {
        // Test the case where the path doesn't exist
        AbsolutePath target = new AbsolutePath("~/grp-001/0001Y");
        CreateDeadlineCommand createDeadlineCommand = new CreateDeadlineCommand(target, toBeAdded, Category.ALLSTU);
        String expectedMessage = MESSAGE_TASK_CREATION_FOR_ROOT;

        assertCommandFailure(createDeadlineCommand, model, expectedMessage);
    }

    @Test
    public void execute_invalidPathForAllGrp_throwsCommandException() throws InvalidPathException {
        // Test the case where the path doesn't exist
        AbsolutePath target = new AbsolutePath("~/grp-001");
        CreateDeadlineCommand createDeadlineCommand = new CreateDeadlineCommand(target, toBeAdded, Category.ALLGRP);
        String expectedMessage = MESSAGE_INVALID_PATH_FOR_ALL_GROUP;

        assertCommandFailure(createDeadlineCommand, model, expectedMessage);
    }

    @Test
    public void execute_childrenAlreadyHaveTaskForAllStuFromGroup_throwsCommandException()
            throws InvalidPathException {
        // Test the case where the path doesn't exist
        AbsolutePath target = new AbsolutePath("~/grp-001");
        String expectedMessage = MESSAGE_ALL_CHILDREN_HAVE_TASK;
        model.groupChildOperation(target).addTaskToAllChildren(toBeAdded, 1);

        CreateDeadlineCommand createDeadlineCommand = new CreateDeadlineCommand(target, toBeAdded, Category.ALLSTU);
        assertCommandFailure(createDeadlineCommand, model, expectedMessage);
    }

    @Test
    public void execute_childrenAlreadyHaveTaskForAllStuFromRoot_throwsCommandException()
            throws InvalidPathException {
        // Test the case where the path doesn't exist
        AbsolutePath target = new AbsolutePath("~");
        String expectedMessage = MESSAGE_ALL_CHILDREN_HAVE_TASK;
        model.rootChildOperation().addTaskToAllChildren(toBeAdded, 2);

        CreateDeadlineCommand createDeadlineCommand = new CreateDeadlineCommand(target, toBeAdded, Category.ALLSTU);
        assertCommandFailure(createDeadlineCommand, model, expectedMessage);
    }

    @Test
    public void execute_childrenAlreadyHaveTaskForAllGrpFromRoot_throwsCommandException()
            throws InvalidPathException {
        // Test the case where the path doesn't exist
        AbsolutePath target = new AbsolutePath("~");
        String expectedMessage = CreateTodoCommand.MESSAGE_ALL_CHILDREN_HAVE_TASK;
        model.rootChildOperation().addTaskToAllChildren(toBeAdded, 1);

        CreateDeadlineCommand createDeadlineCommand = new CreateDeadlineCommand(target, toBeAdded, Category.ALLGRP);
        assertCommandFailure(createDeadlineCommand, model, expectedMessage);
    }

    @Test
    public void execute_deadlineForStudentAccepted_addSuccessful() throws InvalidPathException {
        Student alice = TypicalStudents.ALICE;
        Group aliceGroup = TypicalGroups.GROUP_ONE;

        RelativePath groupPath = new RelativePath(aliceGroup.getId().toString());
        RelativePath alicePath = new RelativePath(alice.getId().toString());
        AbsolutePath absoluteTargetPath = rootPath.resolve(groupPath).resolve(alicePath);

        // Add task to expectedModel
        TaskOperation operation = expectedModel.taskOperation(absoluteTargetPath);
        operation.addTask(toBeAdded);

        CreateDeadlineCommand command = new CreateDeadlineCommand(absoluteTargetPath, toBeAdded, Category.NONE);
        String expectedMessage = String.format(CreateDeadlineCommand.MESSAGE_SUCCESS_STUDENT,
                absoluteTargetPath.getStudentId().get(),
                toBeAdded);

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_deadlineForAllStudentsInGroupAccepted_addSuccessful() throws InvalidPathException {
        Group targetGroup = TypicalGroups.GROUP_ONE;

        RelativePath groupPath = new RelativePath(targetGroup.getId().toString());
        AbsolutePath absoluteTargetPath = rootPath.resolve(groupPath);

        ChildOperation<Student> operation = expectedModel.groupChildOperation(absoluteTargetPath);
        operation.addTaskToAllChildren(toBeAdded, 1);

        CreateDeadlineCommand command = new CreateDeadlineCommand(absoluteTargetPath, toBeAdded, Category.ALLSTU);
        String expectedMessage = String.format(MESSAGE_SUCCESS_ALL_STUDENTS, targetGroup.getId());

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_deadlineForAllGroupsInRootAccepted_addSuccessful() {
        ChildOperation<Group> operation = expectedModel.rootChildOperation();
        operation.addTaskToAllChildren(toBeAdded, 1);

        CreateDeadlineCommand command = new CreateDeadlineCommand(rootPath, toBeAdded, Category.ALLGRP);
        String expectedMessage = CreateDeadlineCommand.MESSAGE_SUCCESS_ALL_GROUPS;

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_duplicateDeadline_throwsCommandException() throws InvalidPathException {
        Student alice = TypicalStudents.ALICE;
        Group aliceGroup = TypicalGroups.GROUP_ONE;

        RelativePath groupPath = new RelativePath(aliceGroup.getId().toString());
        RelativePath alicePath = new RelativePath(alice.getId().toString());
        AbsolutePath absoluteTargetPath = rootPath.resolve(groupPath).resolve(alicePath);

        // Add task to model first
        TaskOperation operation = model.taskOperation(absoluteTargetPath);
        operation.addTask(toBeAdded);

        CreateDeadlineCommand command = new CreateDeadlineCommand(absoluteTargetPath, toBeAdded, Category.NONE);
        String expectedMessage = CreateDeadlineCommand.MESSAGE_DUPLICATE_DEADLINE_TASK;

        assertCommandFailure(command, model, expectedMessage);
    }

    @Test
    public void execute_isHelpTrue_returnMessageUsage() throws CommandException, InvalidPathException {
        Student alice = TypicalStudents.ALICE;
        Group aliceGroup = TypicalGroups.GROUP_ONE;

        RelativePath groupPath = new RelativePath(aliceGroup.getId().toString());
        RelativePath alicePath = new RelativePath(alice.getId().toString());
        AbsolutePath absoluteTargetPath = rootPath.resolve(groupPath).resolve(alicePath);

        CreateDeadlineCommand command = new CreateDeadlineCommand(absoluteTargetPath, toBeAdded, Category.NONE);
        CommandResult result = command.HELP_MESSAGE.execute(model);

        CommandResult expectedResult = new CommandResult(MESSAGE_USAGE);

        assertEquals(result, expectedResult);
    }

    @Test
    public void execute_pathNotExists_throwCommandException() throws InvalidPathException {
        AbsolutePath absoluteTargetPath = new AbsolutePath("~/grp-003/0000A");

        CreateDeadlineCommand command = new CreateDeadlineCommand(absoluteTargetPath, toBeAdded, Category.NONE);
        String expectedMessage = String.format(MESSAGE_PATH_NOT_FOUND, absoluteTargetPath);

        assertCommandFailure(command, model, expectedMessage);
    }

    @Test
    public void execute_pathToGroupForCategoryNone_returnSuccessMessage() throws CommandException,
            InvalidPathException {
        Group groupOne = TypicalGroups.GROUP_ONE;

        RelativePath groupPath = new RelativePath(groupOne.getId().toString());
        AbsolutePath absoluteTargetPath = rootPath.resolve(groupPath);

        CreateDeadlineCommand command = new CreateDeadlineCommand(absoluteTargetPath, toBeAdded, Category.NONE);
        CommandResult expectedResult = new CommandResult(
                String.format(MESSAGE_SUCCESS_GROUP,
                        absoluteTargetPath.getGroupId().get(),
                        toBeAdded));

        CommandResult result = command.execute(model);

        assertEquals(result, expectedResult);
    }

    @Test
    public void execute_atLeastOnechildHasTaskAllStu_returnSuccessMessage() throws CommandException,
            InvalidPathException {
        TaskOperation taskOperation = model.taskOperation(new AbsolutePath("~/grp-001/0001Y"));
        taskOperation.addTask(toBeAdded);

        Group groupOne = TypicalGroups.GROUP_ONE;
        RelativePath groupPath = new RelativePath(groupOne.getId().toString());
        AbsolutePath absoluteTargetPath = rootPath.resolve(groupPath);

        CreateDeadlineCommand command = new CreateDeadlineCommand(absoluteTargetPath, toBeAdded, Category.ALLSTU);
        CommandResult expectedResult = new CommandResult(MESSAGE_SUCCESS_ALL_STUDENTS_WITH_WARNING);

        CommandResult result = command.execute(model);
        assertEquals(result, expectedResult);
    }

    @Test
    public void execute_atLeastOneChildHaveTask_returnSuccessMessageWithWarning() throws CommandException,
            InvalidPathException {
        TaskOperation taskOperation = model.taskOperation(new AbsolutePath("~/grp-001/0001Y"));
        taskOperation.addTask(toBeAdded);

        CreateDeadlineCommand command = new CreateDeadlineCommand(rootPath, toBeAdded, Category.ALLSTU);
        CommandResult expectedResult = new CommandResult(MESSAGE_SUCCESS_ALL_STUDENTS_WITH_WARNING);

        CommandResult result = command.execute(model);

        assertEquals(result, expectedResult);
    }

    @Test
    public void execute_atLeastOneGroupHaveTaskAllGrp_throwCommandException() throws CommandException,
            InvalidPathException {
        TaskOperation taskOperation = model.taskOperation(new AbsolutePath("~/grp-001"));
        taskOperation.addTask(toBeAdded);

        CreateDeadlineCommand command = new CreateDeadlineCommand(rootPath, toBeAdded, Category.ALLGRP);
        CommandResult expectedResult = new CommandResult(MESSAGE_SUCCESS_ALL_GROUPS_WITH_WARNING);

        CommandResult result = command.execute(model);

        assertEquals(result, expectedResult);
    }

    @Test
    public void equals() {
        CreateDeadlineCommand createDeadlineCommand1 = new CreateDeadlineCommand(
                rootPath, TypicalTasks.DEADLINE_1, Category.NONE);
        CreateDeadlineCommand createDeadlineCommand2 = new CreateDeadlineCommand(
                rootPath, TypicalTasks.DEADLINE_2, Category.NONE);

        // same object -> returns true
        assertEquals(createDeadlineCommand1, createDeadlineCommand1);

        // same values -> returns true
        CreateDeadlineCommand createDeadlineCommand1Copy = new CreateDeadlineCommand(
                rootPath, TypicalTasks.DEADLINE_1, Category.NONE);
        assertEquals(createDeadlineCommand1, createDeadlineCommand1Copy);

        // different types -> returns false
        assertNotEquals(1, createDeadlineCommand1);

        // null -> returns false
        assertNotEquals(null, createDeadlineCommand1);

        // different values
        assertNotEquals(createDeadlineCommand1, createDeadlineCommand2);
    }

    @Test
    public void toStringMethod() {
        CreateDeadlineCommand createDeadlineCommand = new CreateDeadlineCommand(
                rootPath, TypicalTasks.DEADLINE_1, Category.NONE);
        String expected = CreateDeadlineCommand.class.getCanonicalName()
            + "{toCreateDeadline=" + TypicalTasks.DEADLINE_1 + "}";
        assertEquals(expected, createDeadlineCommand.toString());
    }
}

