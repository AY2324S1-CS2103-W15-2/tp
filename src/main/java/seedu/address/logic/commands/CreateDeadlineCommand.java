package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;
import static seedu.address.logic.Messages.MESSAGE_PATH_NOT_FOUND;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.ChildOperation;
import seedu.address.model.Model;
import seedu.address.model.TaskOperation;
import seedu.address.model.path.AbsolutePath;
import seedu.address.model.profbook.Group;
import seedu.address.model.profbook.Student;
import seedu.address.model.task.Deadline;

/**
 * Adds a Deadline for a specified {@code Student} or {@code Group}.
 */
public class CreateDeadlineCommand extends Command {

    public static final String COMMAND_WORD = "deadline";

    public static final String MESSAGE_USAGE =
            "Usage: " + COMMAND_WORD + " [path]" + " -d <desc>" + " -dt <deadline>" + " [OPTION]... \n"
            + "\n"
            + "Create deadline task to the target path (the current directory by default).\n"
            + "\n"
            + "Argument: \n"
            + "    -d, --desc           Description of the deadline task\n"
            + "    -dt, --datetime      Deadline of the task\n"
            + "                         Format: yyyy-MM-dd HH:mm\n"
            + "                         'yyyy': year, 'MM': month, 'dd': day,\n"
            + "                         'HH': hour (24-hour format), 'mm': minutes.\n"
            + "\n"
            + "Option: \n"
            + "    path                 Valid path to group or student\n"
            + "    -al, --all           Bulk task assignment\n"
            + "                         Possible value: allStu, allGrp\n"
            + "    -h, --help           Show this help menu\n"
            + "\n"
            + "Examples: \n"
            + "deadline grp-001/0001Y -d Homework -dt 2023-11-11 11:59\n"
            + "deadline . -d Homework -dt 2023-11-11 11:59 -al allGrp\n"
            + "deadline ./grp-001 -d Homework -dt 2023-11-11 11:59 -al allStu";

    public static final String MESSAGE_SUCCESS_STUDENT = "New Deadline task added to student: %1$s\n%2$s";

    public static final String MESSAGE_SUCCESS_GROUP = "New Deadline task added to group: %1$s\n%2$s";

    public static final String MESSAGE_SUCCESS_ALL_STUDENTS =
            "New Deadline task added to all students in group: %1$s";

    public static final String MESSAGE_SUCCESS_ALL_STUDENTS_FOR_ROOT =
            "New Deadline task added to all students in root directory.";

    public static final String MESSAGE_SUCCESS_ALL_STUDENTS_WITH_WARNING =
            "Warning: Some student(s) already have the task. \n"
            + "New Deadline task has been added to the rest.";

    public static final String MESSAGE_SUCCESS_ALL_GROUPS =
            "New Deadline task added to all groups in root directory.";

    public static final String MESSAGE_SUCCESS_ALL_GROUPS_WITH_WARNING =
            "Warning: Some group(s) already have the task. \n"
            + "New Deadline task has been added to the rest.";

    public static final String MESSAGE_DUPLICATE_DEADLINE_TASK =
            "This Deadline task has already been allocated";

    public static final String MESSAGE_TASK_CREATION_FOR_ROOT = "Unable to create task for root directory.";

    public static final String MESSAGE_INVALID_PATH_FOR_ALL_STU = "AllStu flag is only allowed for root and group path";

    public static final String MESSAGE_INVALID_PATH_FOR_ALL_GROUP = "AllGrp flag is only allowed for root path";

    public static final String MESSAGE_ALL_CHILDREN_HAVE_TASK = "All %1$ss already have the task.";

    public static final CreateDeadlineCommand HELP_MESSAGE = new CreateDeadlineCommand() {
        @Override
        public CommandResult execute(Model model) {
            return new CommandResult(MESSAGE_USAGE);
        }
    };

    private final AbsolutePath path;

    private final Deadline deadline;

    private final Category category;

    /**
     * Creates an CreateDeadlineCommand to add the {@code Deadline} Task for a specified {@code Student}
     * or {@code Group}
     * User has inputted a category as well.
     */
    public CreateDeadlineCommand(AbsolutePath path, Deadline deadline, Category category) {
        requireAllNonNull(path, deadline, category);
        this.path = path;
        this.deadline = deadline;
        this.category = category;
    }

    private CreateDeadlineCommand() {
        this.path = null;
        this.deadline = null;
        this.category = null;
    }

    /**
     * Executes an CreateDeadlineCommand to allocate a {@code Deadline} task to a {@code Group} or {@code Student}
     *
     * @return Command result which represents the outcome of the command execution.
     * @throws CommandException Exception thrown when error occurs during command execution.
     */
    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        assert model != null : "Model should not be null";

        // Check if path exists in ProfBook
        if (!model.hasPath(path)) {
            throw new CommandException(String.format(MESSAGE_PATH_NOT_FOUND, path));
        }

        switch(category) {
        case NONE:
            return handleNone(model);
        case ALLSTU:
            return handleAllStu(model);
        default:
            return handleAllGrp(model);
        }
    }

    /**
     * Allocates a {@code Deadline} task to a {@code Group} or {@code Student}
     *
     * @return Command result which represents the outcome of the command execution.
     * @throws CommandException Exception thrown when error occurs during command execution.
     */
    private CommandResult handleNone(Model model) throws CommandException {
        // Check if target path is root
        if (path.isRootDirectory()) {
            throw new CommandException(MESSAGE_TASK_CREATION_FOR_ROOT);
        }

        TaskOperation target = model.taskOperation(path);

        // Check duplicate deadline
        if (target.hasTask(this.deadline)) {
            throw new CommandException(MESSAGE_DUPLICATE_DEADLINE_TASK);
        }

        target.addTask(this.deadline);
        model.updateList();

        if (path.isGroupDirectory()) {
            return new CommandResult(String.format(MESSAGE_SUCCESS_GROUP, path.getGroupId().get(), this.deadline));
        }
        return new CommandResult(String.format(MESSAGE_SUCCESS_STUDENT, path.getStudentId().get(), this.deadline));
    }

    /**
     * Handles the situation where a {@code Deadline} task is allocated to all {@code Student}
     * or {@code Root}
     *
     * @return Command result which represents the outcome of the command execution.
     * @throws CommandException Exception thrown when error occurs during command execution.
     */
    private CommandResult handleAllStu(Model model) throws CommandException {
        if (path.isStudentDirectory()) {
            throw new CommandException(MESSAGE_INVALID_PATH_FOR_ALL_STU);
        }

        if (path.isGroupDirectory()) {
            return addTaskToAllStuInGrp(model);
        }

        return addTaskToAllStuInRoot(model);
    }

    /**
     * Adds a {@code Deadline} task to all {@code Student} in a {@code Group}
     *
     * @return Command result which represents the outcome of the command execution.
     * @throws CommandException Exception thrown when error occurs during command execution.
     */
    private CommandResult addTaskToAllStuInGrp(Model model) throws CommandException {
        ChildOperation<Student> groupOper = model.groupChildOperation(path);

        // Check whether all children already have the task
        if (groupOper.doAllChildrenHaveTasks(deadline, 1)) {
            throw new CommandException(String.format(MESSAGE_ALL_CHILDREN_HAVE_TASK, "student"));
        }

        // Check whether at least one of the children has the task
        boolean warning = groupOper.doAnyChildrenHaveTasks(deadline, 1);

        groupOper.addTaskToAllChildren(deadline, 1);
        model.updateList();
        return new CommandResult(
                warning ? MESSAGE_SUCCESS_ALL_STUDENTS_WITH_WARNING
                        : String.format(MESSAGE_SUCCESS_ALL_STUDENTS, path.getGroupId().get()));
    }

    /**
     * Adds a {@code Deadline} task to all {@code Student} in a {@code Root}
     *
     * @return Command result which represents the outcome of the command execution.
     * @throws CommandException Exception thrown when error occurs during command execution.
     */
    private CommandResult addTaskToAllStuInRoot(Model model) throws CommandException {
        ChildOperation<Group> operation = model.rootChildOperation();

        // Check whether all children already have the task
        if (operation.doAllChildrenHaveTasks(deadline, 2)) {
            throw new CommandException(String.format(MESSAGE_ALL_CHILDREN_HAVE_TASK, "student"));
        }

        // Check whether at least one of the children has the task
        boolean warning = operation.doAnyChildrenHaveTasks(deadline, 2);

        operation.addTaskToAllChildren(deadline, 2);
        model.updateList();
        return new CommandResult(
                warning ? MESSAGE_SUCCESS_ALL_STUDENTS_WITH_WARNING
                        : MESSAGE_SUCCESS_ALL_STUDENTS_FOR_ROOT);
    }

    /**
     * Handles the situation where a {@code Deadline} task is allocated to all {@code Group} in {@code Root}
     *
     * @return Command result which represents the outcome of the command execution.
     * @throws CommandException Exception thrown when error occurs during command execution.
     */
    private CommandResult handleAllGrp(Model model) throws CommandException {
        if (!path.isRootDirectory()) {
            throw new CommandException(MESSAGE_INVALID_PATH_FOR_ALL_GROUP);
        }

        return addTaskToAllGrpInRoot(model);
    }

    /**
     * Adds a {@code Deadline} task to all {@code Group} in {@code Root}
     *
     * @return Command result which represents the outcome of the command execution.
     * @throws CommandException Exception thrown when error occurs during command execution.
     */
    private CommandResult addTaskToAllGrpInRoot(Model model) throws CommandException {
        ChildOperation<Group> rootOper = model.rootChildOperation();

        // Check whether all children already have the task
        if (rootOper.doAllChildrenHaveTasks(deadline, 1)) {
            throw new CommandException(String.format(MESSAGE_ALL_CHILDREN_HAVE_TASK, "group"));
        }

        // Check whether at least one of the children has the task
        boolean warning = rootOper.doAnyChildrenHaveTasks(deadline, 1);

        rootOper.addTaskToAllChildren(deadline, 1);
        model.updateList();
        return new CommandResult(warning ? MESSAGE_SUCCESS_ALL_GROUPS_WITH_WARNING : MESSAGE_SUCCESS_ALL_GROUPS);
    }

    /**
     * Compares this {@code CreateDeadlineCommand} to another object to see if they are equal.
     *
     * @param other The other object to compare against this {@code CreateDeadlineCommand}.
     * @return True if the object is same as {@code CreateDeadlineCommand} and false otherwise.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof CreateDeadlineCommand)) {
            return false;
        }

        CreateDeadlineCommand otherCreateDeadlineCommand =
                (CreateDeadlineCommand) other;
        return this.deadline.equals(otherCreateDeadlineCommand.deadline)
                && this.path.equals(otherCreateDeadlineCommand.path);
    }

    /**
     * Returns a string representation of the {@code CreateDeadlineCommand}.
     *
     * @return String representation of the {@code CreateDeadlineCommand}.
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("toCreateDeadline", this.deadline)
                .toString();
    }
}


