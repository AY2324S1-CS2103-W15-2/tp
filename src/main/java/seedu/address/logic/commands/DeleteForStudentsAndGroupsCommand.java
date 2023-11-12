package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_PATH_NOT_FOUND;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.ChildOperation;
import seedu.address.model.Model;
import seedu.address.model.id.GroupId;
import seedu.address.model.id.StudentId;
import seedu.address.model.path.AbsolutePath;
import seedu.address.model.path.exceptions.InvalidPathException;
import seedu.address.model.profbook.Group;
import seedu.address.model.profbook.Student;
import seedu.address.model.profbook.exceptions.NoSuchChildException;

/**
 * Deletes a {@code Student} or {@code Group} according to the targeted path.
 */
public class DeleteForStudentsAndGroupsCommand extends Command {

    public static final String COMMAND_WORD = "rm";

    public static final String MESSAGE_USAGE =
            "Usage: " + COMMAND_WORD + " <path>\n"
            + "\n"
            + "Delete a student or group.\n"
            + "\n"
            + "Argument: \n"
            + "    path                 Valid path to group or student\n"
            + "\n"
            + "Option: \n"
            + "    -h, --help           Show this help menu\n"
            + "\n"
            + "Examples: \n"
            + "rm grp-001 \n"
            + "rm grp-001/0001Y";

    public static final String MESSAGE_SUCCESS_FOR_STUDENT = "Student removed: %1$s";

    public static final String MESSAGE_SUCCESS_FOR_GROUP = "Group removed: %1$s";

    public static final String MESSAGE_INCORRECT_DIRECTORY_ERROR = "Unable to remove root directory.";

    public static final String MESSAGE_DELETE_CURRENT_PATH = "Current path cannot be removed.";

    public static final String MESSAGE_DELETE_DISPLAY_PATH = "Current display path cannot be removed.";

    public static final DeleteForStudentsAndGroupsCommand HELP_MESSAGE = new DeleteForStudentsAndGroupsCommand() {
        @Override
        public CommandResult execute(Model model) throws CommandException {
            return new CommandResult(MESSAGE_USAGE);
        }
    };

    private final AbsolutePath toBeDeleted;

    /**
     * Creates an DeleteForStudentsAndGroupsCommand to specified {@code Student} or {@code Group}
     */
    public DeleteForStudentsAndGroupsCommand(AbsolutePath toBeDeleted) { //path will specify which grp/student
        requireNonNull(toBeDeleted);
        this.toBeDeleted = toBeDeleted;
    }

    private DeleteForStudentsAndGroupsCommand() {
        this.toBeDeleted = null;
    }

    /**
     * Executes an DeleteForStudentsAndGroupsCommand to delete a {@code Student} or {@code Group}
     *
     * @return Command result which represents the outcome of the command execution.
     * @throws CommandException                  Exception thrown when error occurs during command execution.
     * @throws InvalidPathException              Exception thrown when error occurs due to invalid path.
     * @throws NoSuchChildException              Exception thrown when child specified does not exist.
     */
    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        assert model != null : "Model should not be null";

        checkIfToBeDeletedIsRootDirectory();
        checkIfToBeDeletedPathIsCurrentPath(model);
        checkIfToBeDeletedPathIsDisplayPath(model);
        checkIfPathToDeleteExistInProfBook(model);

        if (toBeDeleted.isStudentDirectory()) {
            return deleteStudent(model);
        }

        if (toBeDeleted.isGroupDirectory()) {
            return deleteGroup(model);
        }

        throw new CommandException(MESSAGE_INCORRECT_DIRECTORY_ERROR);
    }

    /**
     * Checks if toBeDeleted is a Root Directory
     *
     * @throws CommandException Exception thrown when error occurs during command execution.
     */
    private void checkIfToBeDeletedIsRootDirectory() throws CommandException {
        if (toBeDeleted.isRootDirectory()) {
            throw new CommandException(MESSAGE_INCORRECT_DIRECTORY_ERROR);
        }
    }

    /**
     * Checks if toBeDeleted path is current path
     *
     * @throws CommandException Exception thrown when error occurs during command execution.
     */
    private void checkIfToBeDeletedPathIsCurrentPath(Model model) throws CommandException {
        if (toBeDeleted.equals(model.getCurrPath())) {
            throw new CommandException(MESSAGE_DELETE_CURRENT_PATH);
        }
    }

    /**
     * Checks if toBeDeleted path is display path
     *
     * @throws CommandException Exception thrown when error occurs during command execution.
     */
    private void checkIfToBeDeletedPathIsDisplayPath(Model model) throws CommandException {
        if (toBeDeleted.equals(model.getDisplayPath())) {
            throw new CommandException(MESSAGE_DELETE_DISPLAY_PATH);
        }
    }

    /**
     * Checks if toBeDeleted path exists in ProfBook
     *
     * @throws CommandException Exception thrown when error occurs during command execution.
     */
    private void checkIfPathToDeleteExistInProfBook(Model model) throws CommandException {
        if (!model.hasPath(toBeDeleted)) {
            throw new CommandException(String.format(MESSAGE_PATH_NOT_FOUND, toBeDeleted));
        }
    }

    /**
     * Deletes a student
     *
     * @return Command result which represents the outcome of the command execution.
     */
    private CommandResult deleteStudent(Model model) {
        ChildOperation<Student> target = model.groupChildOperation(toBeDeleted);
        StudentId studentId = toBeDeleted.getStudentId().get();
        Student stu = target.getChild(studentId);
        target.deleteChild(studentId);
        model.updateList();
        return new CommandResult(String.format(MESSAGE_SUCCESS_FOR_STUDENT, Messages.format(stu)));
    }

    /**
     * Deletes a group
     *
     * @return Command result which represents the outcome of the command execution.
     */
    private CommandResult deleteGroup(Model model) {
        ChildOperation<Group> target = model.rootChildOperation();
        GroupId groupId = toBeDeleted.getGroupId().get();
        Group grp = target.getChild(groupId);
        target.deleteChild(groupId);
        model.updateList();
        return new CommandResult(String.format(MESSAGE_SUCCESS_FOR_GROUP, Messages.format(grp)));
    }


    /**
     * Compares this {@code DeleteForStudentsAndGroupsCommand} to another object
     * to see if they are equal.
     *
     * @param other The other object to compare against this {@code DeleteForStudentsAndGroupsCommand}.
     * @return True if the object is same as {@code DeleteForStudentsAndGroupsCommand} and false otherwise.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        // instanceof handles nulls
        if (!(other instanceof DeleteForStudentsAndGroupsCommand)) {
            return false;
        }

        DeleteForStudentsAndGroupsCommand otherDeleteForStudentsAndGroupsCommand =
                (DeleteForStudentsAndGroupsCommand) other;
        return this.toBeDeleted.equals(otherDeleteForStudentsAndGroupsCommand.toBeDeleted);
    }

    /**
     * Returns a string representation of the {@code DeleteForStudentsAndGroupsCommand}.
     *
     * @return String representation of the {@code DeleteForStudentsAndGroupsCommand}.
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("toDeleteStudentOrGroup", toBeDeleted)
                .toString();
    }
}
