package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static seedu.address.logic.Messages.MESSAGE_PATH_NOT_FOUND;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CreateTodoCommand.MESSAGE_ALL_CHILDREN_HAVE_TASK;
import static seedu.address.logic.commands.CreateTodoCommand.MESSAGE_DUPLICATE_TODO_TASK;
import static seedu.address.logic.commands.CreateTodoCommand.MESSAGE_INVALID_PATH_FOR_ALL_GROUP;
import static seedu.address.logic.commands.CreateTodoCommand.MESSAGE_INVALID_PATH_FOR_ALL_STU;
import static seedu.address.logic.commands.CreateTodoCommand.MESSAGE_SUCCESS_ALL_GROUPS;
import static seedu.address.logic.commands.CreateTodoCommand.MESSAGE_SUCCESS_ALL_GROUPS_WITH_WARNING;
import static seedu.address.logic.commands.CreateTodoCommand.MESSAGE_SUCCESS_ALL_STUDENTS;
import static seedu.address.logic.commands.CreateTodoCommand.MESSAGE_SUCCESS_ALL_STUDENTS_WITH_WARNING;
import static seedu.address.logic.commands.CreateTodoCommand.MESSAGE_SUCCESS_GROUP;
import static seedu.address.logic.commands.CreateTodoCommand.MESSAGE_SUCCESS_STUDENT;
import static seedu.address.logic.commands.CreateTodoCommand.MESSAGE_TASK_CREATION_FOR_ROOT;
import static seedu.address.logic.commands.CreateTodoCommand.MESSAGE_USAGE;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalTasks.TODO_1;
import static seedu.address.testutil.TypicalTasks.TODO_2;

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
import seedu.address.model.task.ToDo;
import seedu.address.testutil.TypicalGroups;
import seedu.address.testutil.TypicalRoots;
import seedu.address.testutil.TypicalStudents;

public class CreateTodoCommandTest {
    private Model model;
    private Model expectedModel;
    private AbsolutePath rootPath = CommandTestUtil.getValidRootAbsolutePath();
    private ToDo toBeAdded = TODO_1;
    @BeforeEach
    public void setup() {
        model = new ModelManager(rootPath, new Root(TypicalRoots.PROFBOOK_WITH_TWO_GROUPS), new UserPrefs());
        expectedModel = new ModelManager(rootPath, new Root(TypicalRoots.PROFBOOK_WITH_TWO_GROUPS), new UserPrefs());
    }

    @Test
    public void constructor_nullAbsolutePathAndTodoAndCategory_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new CreateTodoCommand(null, null, null));
    }

    @Test
    public void constructor_nullAbsolutePath_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () ->
                new CreateTodoCommand(null, toBeAdded, Category.NONE));
    }

    @Test
    public void constructor_nullTodo_throwsNullPointerException() throws InvalidPathException {
        Group targetGroup = TypicalGroups.GROUP_ONE;

        RelativePath groupPath = new RelativePath(targetGroup.getId().toString());
        AbsolutePath absoluteTargetPath = rootPath.resolve(groupPath);

        assertThrows(NullPointerException.class, () ->
                new CreateTodoCommand(absoluteTargetPath, null, Category.ALLSTU));
    }

    @Test
    public void constructor_nullCategory_throwsNullPointerException() throws InvalidPathException {
        Group targetGroup = TypicalGroups.GROUP_ONE;

        RelativePath groupPath = new RelativePath(targetGroup.getId().toString());
        AbsolutePath absoluteTargetPath = rootPath.resolve(groupPath);

        assertThrows(NullPointerException.class, () ->
                new CreateTodoCommand(absoluteTargetPath, toBeAdded, null));
    }

    @Test
    public void execute_invalidPath_throwsCommandException() throws InvalidPathException {
        // Test the case where the path doesn't exist
        AbsolutePath target = new AbsolutePath("~/grp-123");
        CreateTodoCommand createTodoCommand = new CreateTodoCommand(target, toBeAdded, Category.NONE);
        String expectedMessage = String.format(MESSAGE_PATH_NOT_FOUND, target);

        assertCommandFailure(createTodoCommand, model, expectedMessage);
    }

    @Test
    public void execute_invalidPathForAllStu_throwsCommandException() throws InvalidPathException {
        // Test the case where the path doesn't exist
        AbsolutePath target = new AbsolutePath("~/grp-001/0099A");
        CreateTodoCommand createTodoCommand = new CreateTodoCommand(target, toBeAdded, Category.NONE);
        String expectedMessage = MESSAGE_INVALID_PATH_FOR_ALL_STU;

        assertCommandFailure(createTodoCommand, model, expectedMessage);
    }

    @Test
    public void execute_invalidPathForAllGrp_throwsCommandException() throws InvalidPathException {
        // Test the case where the path doesn't exist
        AbsolutePath target = new AbsolutePath("~/grp-001");
        CreateTodoCommand createTodoCommand = new CreateTodoCommand(target, toBeAdded, Category.ALLGRP);
        String expectedMessage = MESSAGE_INVALID_PATH_FOR_ALL_GROUP;

        assertCommandFailure(createTodoCommand, model, expectedMessage);
    }

    @Test
    public void execute_childrenAlreadyHaveTaskForAllStuFromGroup_throwsCommandException()
            throws InvalidPathException {
        // Test the case where the path doesn't exist
        AbsolutePath target = new AbsolutePath("~/grp-001");
        String expectedMessage = MESSAGE_ALL_CHILDREN_HAVE_TASK;
        model.groupChildOperation(target).addTaskToAllChildren(toBeAdded, 1);

        CreateTodoCommand createTodoCommand = new CreateTodoCommand(target, toBeAdded, Category.ALLSTU);
        assertCommandFailure(createTodoCommand, model, expectedMessage);
    }

    @Test
    public void execute_childrenAlreadyHaveTaskForAllStuFromRoot_throwsCommandException()
            throws InvalidPathException {
        // Test the case where the path doesn't exist
        AbsolutePath target = new AbsolutePath("~/grp-001");
        String expectedMessage = MESSAGE_ALL_CHILDREN_HAVE_TASK;
        model.rootChildOperation().addTaskToAllChildren(toBeAdded, 2);

        CreateTodoCommand createTodoCommand = new CreateTodoCommand(target, toBeAdded, Category.ALLSTU);
        assertCommandFailure(createTodoCommand, model, expectedMessage);
    }

    @Test
    public void execute_childrenAlreadyHaveTaskForAllGrp_throwsCommandException() throws InvalidPathException {
        // Test the case where the path doesn't exist
        AbsolutePath target = new AbsolutePath("~");
        String expectedMessage = MESSAGE_ALL_CHILDREN_HAVE_TASK;
        model.rootChildOperation().addTaskToAllChildren(toBeAdded, 1);

        CreateTodoCommand createTodoCommand = new CreateTodoCommand(target, toBeAdded, Category.ALLGRP);
        assertCommandFailure(createTodoCommand, model, expectedMessage);
    }

    @Test
    public void execute_todoForAllStudentsInGroupAccepted_addSuccessful()
            throws InvalidPathException {
        Group targetGroup = TypicalGroups.GROUP_ONE;

        RelativePath groupPath = new RelativePath(targetGroup.getId().toString());
        AbsolutePath absoluteTargetPath = rootPath.resolve(groupPath);

        ChildOperation<Student> operation = expectedModel.groupChildOperation(absoluteTargetPath);
        operation.addTaskToAllChildren(toBeAdded, 1);

        CreateTodoCommand command = new CreateTodoCommand(absoluteTargetPath, toBeAdded, Category.ALLSTU);
        String expectedMessage = String.format(MESSAGE_SUCCESS_ALL_STUDENTS, targetGroup.getId());

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_deadlineForAllGroupsInRootAccepted_addSuccessful() {
        ChildOperation<Group> operation = expectedModel.rootChildOperation();
        operation.addTaskToAllChildren(toBeAdded, 1);

        CreateTodoCommand command = new CreateTodoCommand(rootPath, toBeAdded, Category.ALLGRP);
        String expectedMessage = MESSAGE_SUCCESS_ALL_GROUPS;

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_createTodoTask_success() throws InvalidPathException {
        Student alice = TypicalStudents.ALICE;
        Group aliceGroup = TypicalGroups.GROUP_ONE;

        RelativePath groupPath = new RelativePath(aliceGroup.getId().toString());
        RelativePath alicePath = new RelativePath(alice.getId().toString());
        AbsolutePath absoluteTargetPath = rootPath.resolve(groupPath).resolve(alicePath);

        // Add task to expectedModel
        TaskOperation operation = expectedModel.taskOperation(absoluteTargetPath);
        operation.addTask(toBeAdded);

        CreateTodoCommand command = new CreateTodoCommand(absoluteTargetPath, toBeAdded, Category.NONE);
        String expectedMessage = String.format(MESSAGE_SUCCESS_STUDENT,
                absoluteTargetPath.getStudentId().get(), toBeAdded);

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_duplicateTodoTask_throwCommandException() throws InvalidPathException {
        Student alice = TypicalStudents.ALICE;
        Group aliceGroup = TypicalGroups.GROUP_ONE;

        RelativePath groupPath = new RelativePath(aliceGroup.getId().toString());
        RelativePath alicePath = new RelativePath(alice.getId().toString());
        AbsolutePath absoluteTargetPath = rootPath.resolve(groupPath).resolve(alicePath);

        // Add task to model first
        TaskOperation operation = model.taskOperation(absoluteTargetPath);
        operation.addTask(toBeAdded);

        CreateTodoCommand command = new CreateTodoCommand(absoluteTargetPath, toBeAdded, Category.NONE);
        String expectedMessage = MESSAGE_DUPLICATE_TODO_TASK;

        assertCommandFailure(command, model, expectedMessage);
    }

    @Test
    public void execute_targetIsRootDirectoryNone_throwCommandException() {
        CreateTodoCommand command = new CreateTodoCommand(rootPath, toBeAdded, Category.NONE);
        String expectedMessage = MESSAGE_TASK_CREATION_FOR_ROOT;

        assertCommandFailure(command, model, expectedMessage);
    }

    @Test
    public void execute_targetIsGroupDirectoryNone_returnCommandResult() throws CommandException, InvalidPathException {
        Group aliceGroup = TypicalGroups.GROUP_ONE;
        RelativePath groupPath = new RelativePath(aliceGroup.getId().toString());
        AbsolutePath absoluteTargetPath = rootPath.resolve(groupPath);

        CreateTodoCommand command = new CreateTodoCommand(absoluteTargetPath, toBeAdded, Category.NONE);
        CommandResult expectedResult = new CommandResult(String.format(
                MESSAGE_SUCCESS_GROUP,
                absoluteTargetPath.getGroupId().get(),
                toBeAdded));

        CommandResult result = command.execute(model);

        assertEquals(expectedResult, result);
    }

    @Test
    public void execute_targetIsStudentDirectoryAllStu_throwCommandException() throws InvalidPathException {
        Student alice = TypicalStudents.ALICE;
        Group aliceGroup = TypicalGroups.GROUP_ONE;

        RelativePath groupPath = new RelativePath(aliceGroup.getId().toString());
        RelativePath alicePath = new RelativePath(alice.getId().toString());
        AbsolutePath absoluteTargetPath = rootPath.resolve(groupPath).resolve(alicePath);

        CreateTodoCommand command = new CreateTodoCommand(absoluteTargetPath, toBeAdded, Category.ALLSTU);
        String expectedMessage = MESSAGE_INVALID_PATH_FOR_ALL_STU;

        assertCommandFailure(command, model, expectedMessage);
    }

    @Test
    public void execute_childrenAlreadyHaveTask_throwCommandException() throws CommandException, InvalidPathException {
        ChildOperation<Group> operation = model.rootChildOperation();
        operation.addTaskToAllChildren(toBeAdded, 2);

        CreateTodoCommand command = new CreateTodoCommand(rootPath, toBeAdded, Category.ALLSTU);
        String expectedMessage = String.format(MESSAGE_ALL_CHILDREN_HAVE_TASK, "student");

        assertCommandFailure(command, model, expectedMessage);
    }

    @Test
    public void execute_isHelpTrue_returnMessageUsage() throws CommandException, InvalidPathException {
        Student alice = TypicalStudents.ALICE;
        Group aliceGroup = TypicalGroups.GROUP_ONE;

        RelativePath groupPath = new RelativePath(aliceGroup.getId().toString());
        RelativePath alicePath = new RelativePath(alice.getId().toString());
        AbsolutePath absoluteTargetPath = rootPath.resolve(groupPath).resolve(alicePath);

        CreateTodoCommand command = new CreateTodoCommand(absoluteTargetPath, toBeAdded, Category.NONE);
        CommandResult result = command.HELP_MESSAGE.execute(model);

        CommandResult expectedResult = new CommandResult(MESSAGE_USAGE);

        assertEquals(result, expectedResult);
    }

    @Test
    public void execute_atLeastOneChildrenAlreadyHaveTaskAllStu_successMessageWithWarning() throws CommandException,
            InvalidPathException {
        TaskOperation taskOperation = model.taskOperation(new AbsolutePath("~/grp-001/0001Y"));
        taskOperation.addTask(toBeAdded);

        CreateTodoCommand command = new CreateTodoCommand(rootPath, toBeAdded, Category.ALLSTU);
        CommandResult expectedResult = new CommandResult(MESSAGE_SUCCESS_ALL_STUDENTS_WITH_WARNING);

        CommandResult result = command.execute(model);

        assertEquals(expectedResult, result);
    }

    @Test
    public void execute_atLeastOneChildrenAlreadyHaveTaskAllGroup_successMessageWithWarning() throws CommandException,
            InvalidPathException {
        TaskOperation taskOperation = model.taskOperation(new AbsolutePath("~/grp-001"));
        taskOperation.addTask(toBeAdded);

        CreateTodoCommand command = new CreateTodoCommand(rootPath, toBeAdded, Category.ALLGRP);
        CommandResult expectedResult = new CommandResult(MESSAGE_SUCCESS_ALL_GROUPS_WITH_WARNING);

        CommandResult result = command.execute(model);

        assertEquals(expectedResult, result);
    }

    @Test
    public void equals() {
        CreateTodoCommand createTodoCommand1 = new CreateTodoCommand(rootPath, TODO_1, Category.NONE);
        CreateTodoCommand createTodoCommand2 = new CreateTodoCommand(rootPath, TODO_2, Category.NONE);

        // same object -> returns true
        assertEquals(createTodoCommand1, createTodoCommand1);

        // same values -> returns true
        CreateTodoCommand createDeadlineCommand1Copy = new CreateTodoCommand(rootPath, TODO_1, Category.NONE);
        assertEquals(createTodoCommand1, createDeadlineCommand1Copy);

        // different types -> returns false
        assertNotEquals(1, createTodoCommand1);

        // null -> returns false
        assertNotEquals(null, createTodoCommand1);

        // different values
        assertNotEquals(createTodoCommand1, createTodoCommand2);
    }

    @Test
    public void toStringMethod() {
        CreateTodoCommand createTodoCommand = new CreateTodoCommand(rootPath, TODO_1, Category.NONE);
        String expected = CreateTodoCommand.class.getCanonicalName()
            + "{toCreateTodo=" + TODO_1 + "}";
        assertEquals(expected, createTodoCommand.toString());
    }
}
