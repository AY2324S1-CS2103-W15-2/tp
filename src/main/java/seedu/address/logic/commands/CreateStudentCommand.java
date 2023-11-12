package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.ChildOperation;
import seedu.address.model.Model;
import seedu.address.model.path.AbsolutePath;
import seedu.address.model.profbook.Student;

/**
 * Adds a student within the specific group.
 */
public class CreateStudentCommand extends Command {

    public static final String COMMAND_WORD = "touch";

    public static final String MESSAGE_USAGE =
            "Usage: " + COMMAND_WORD + " <path> " + "-n <name> " + "[OPTION]... \n"
            + "\n"
            + "Create new student.\n"
            + "\n"
            + "Arguments: \n"
            + "    path                 Valid path to student\n"
            + "    -n, --name           Name of the student\n"
            + "\n"
            + "Option: \n"
            + "    -e, --email          Email of the student\n"
            + "    -p, --phone          Phone of the student\n"
            + "    -a, --address        Address of the student\n"
            + "    -h, --help           Show this help menu\n"
            + "\n"
            + "Examples: \n"
            + "touch grp-001/0001Y -n Mary -e mary@gmail.com -p 87652345 -a 4 Loyang Walk Loyang Industrial Estate";

    public static final String MESSAGE_SUCCESS = "New student added: %1$s";

    public static final String MESSAGE_DUPLICATE_STUDENT_ID =
            "StudentId %1$s has already been used by the student: %2$s";

    public static final String MESSAGE_INVALID_PATH = "Path provided should be a valid student path";

    public static final String MESSAGE_GROUP_NOT_FOUND = "Group %1$s does not exist in ProfBook";

    public static final CreateStudentCommand HELP_MESSAGE = new CreateStudentCommand() {
        @Override
        public CommandResult execute(Model model) throws CommandException {
            return new CommandResult(MESSAGE_USAGE);
        }
    };

    private final AbsolutePath path;
    private final Student student;

    /**
     * Creates an CreateStudentCommand to add the specified {@code Student}
     */
    public CreateStudentCommand(AbsolutePath path, Student student) {
        requireAllNonNull(path, student);
        this.path = path;
        this.student = student;
    }

    private CreateStudentCommand() {
        this.path = null;
        this.student = null;
    }

    /**
     * Executes an CreateStudentCommand to add the specified {@code Student} to a {@code Group}
     *
     * @return Command result which represents the outcome of the command execution.
     * @throws CommandException Exception thrown when error occurs during command execution.
     */
    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        assert model != null : "Model should not be null";

        checkIfPathIsStudentDirectory(path);
        checkIfGroupExistInProfBook(model);
        checkForDuplicateStudentId(model);

        ChildOperation<Student> target = model.groupChildOperation(path);

        target.addChild(this.student.getId(), this.student);
        model.updateList();

        return new CommandResult(String.format(MESSAGE_SUCCESS, Messages.format(student)));
    }

    /**
     * Checks if {@code Path} is a Student Directory
     *
     * @throws CommandException Exception thrown when error occurs during command execution.
     */
    private void checkIfPathIsStudentDirectory(AbsolutePath path) throws CommandException {
        if (!path.isStudentDirectory()) {
            throw new CommandException(MESSAGE_INVALID_PATH);
        }
    }

    /**
     * Checks if specified {@code Group} exist in ProfBook
     *
     * @throws CommandException Exception thrown when error occurs during command execution.
     */
    private void checkIfGroupExistInProfBook(Model model) throws CommandException {
        if (!model.hasGroup(path)) {
            throw new CommandException(String.format(MESSAGE_GROUP_NOT_FOUND, path.getGroupId()));
        }
    }

    /**
     * Checks if StudentId of {@code Student} to be added duplicates with other {@code Student} in ProfBook
     *
     * @throws CommandException Exception thrown when error occurs during command execution.
     */
    private void checkForDuplicateStudentId(Model model) throws CommandException {
        if (model.hasStudentWithId(student.getId())) {
            Student studentWithSameId = model.getStudentWithId(student.getId());
            throw new CommandException(String.format(
                    MESSAGE_DUPLICATE_STUDENT_ID, student.getId().toString(), Messages.format(studentWithSameId)));
        }
    }

    /**
     * Compares this {@code CreateStudentCommand} to another object to see if they are equal.
     *
     * @param other The other object to compare against this {@code CreateStudentCommand}.
     * @return True if the object is same as {@code CreateStudentCommand} and false otherwise.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof CreateStudentCommand)) {
            return false;
        }

        CreateStudentCommand otherCreateStudentCommand = (CreateStudentCommand) other;

        return student.equals(otherCreateStudentCommand.student)
                && this.path.equals(otherCreateStudentCommand.path);
    }

    /**
     * Returns a string representation of the {@code CreateStudentCommand}.
     *
     * @return String representation of the {@code CreateStudentCommand}.
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("toCreateStudent", student)
                .toString();
    }
}
