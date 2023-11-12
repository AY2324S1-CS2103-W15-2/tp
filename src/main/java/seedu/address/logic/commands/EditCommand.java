package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.Map;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.ChildOperation;
import seedu.address.model.Model;
import seedu.address.model.field.EditGroupDescriptor;
import seedu.address.model.field.EditStudentDescriptor;
import seedu.address.model.id.GroupId;
import seedu.address.model.id.Id;
import seedu.address.model.id.StudentId;
import seedu.address.model.path.AbsolutePath;
import seedu.address.model.path.RelativePath;
import seedu.address.model.path.exceptions.InvalidPathException;
import seedu.address.model.profbook.Address;
import seedu.address.model.profbook.Email;
import seedu.address.model.profbook.Group;
import seedu.address.model.profbook.Name;
import seedu.address.model.profbook.Phone;
import seedu.address.model.profbook.Student;
import seedu.address.model.task.ReadOnlyTaskList;
import seedu.address.model.task.TaskListManager;

/**
 * EditCommand is a class representing a command to edit the details of a person (either a student or a group) in
 * ProfBook.
 * Depending on the context (whether it's a student or group), this command can edit different fields.
 */
public class EditCommand extends Command {

    public static final String COMMAND_WORD = "edit";

    public static final String ERROR_MESSAGE_UNSUPPORTED_PATH_OPERATION = "Path operation is not supported";

    public static final String ERROR_MESSAGE_NO_SUCH_GROUP = "Group does not exist in ProfBook.";

    public static final String MESSAGE_USAGE =
            "Usage: " + COMMAND_WORD + " <path> " + "[OPTION]... \n"
            + "\n"
            + "Edit the details of the group or student.\n"
            + "\n"
            + "Argument: \n"
            + "    path                 Valid path to group or student\n"
            + "\n"
            + "Option: (Provide at least one of the following fields for editing)\n"
            + "    -n, --name           Name of the group / student\n"
            + "    -i, --id             Id of the group / student\n"
            + "    -e, --email          Email of the student\n"
            + "    -p, --phone          Phone of the student\n"
            + "    -a, --address        Address of the student\n"
            + "    -h, --help           Show this help menu\n"
            + "\n"
            + "Examples: \n"
            + "edit grp-001 -n Perfect Group \n"
            + "edit grp-001 -i grp-002";
    public static final String MESSAGE_EDIT_GROUP_SUCCESS = "Field(s) of group has been edited successfully.";

    public static final String MESSAGE_EDIT_STUDENT_SUCCESS = "Field(s) of student has been edited successfully.";


    public static final String MESSAGE_INCORRECT_DIRECTORY_ERROR = "Root directory cannot be edited.";

    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided.";

    public static final String MESSAGE_NO_SUCH_PATH = "Path does not exist in ProfBook.";

    public static final String MESSAGE_NO_SUCH_STUDENT = "Student does not exist in ProfBook.";

    public static final String MESSAGE_DUPLICATE_STUDENT_ID =
            "StudentId %1$s has already been used by the student: %2$s";

    public static final String MESSAGE_DUPLICATE_GROUP_ID =
            "GroupId %1$s has already been used by the group: %2$s";

    public static final String MESSAGE_NO_CHANGES_MADE =
            "The value(s) you provided is the same as the current value(s). No changes have been made.";

    public static final EditCommand HELP_MESSAGE = new EditCommand() {
        @Override
        public CommandResult execute(Model model) throws CommandException {
            return new CommandResult(MESSAGE_USAGE);
        }
    };

    private final AbsolutePath target;
    private final EditGroupDescriptor editGroupDescriptor;
    private final EditStudentDescriptor editStudentDescriptor;

    /**
     * Constructs an EditCommand for editing a group's details.
     *
     * @param target              The path to the target group to be edited.
     * @param editGroupDescriptor The descriptor containing the details to edit.
     */
    public EditCommand(AbsolutePath target, EditGroupDescriptor editGroupDescriptor) {
        this.target = target;
        this.editGroupDescriptor = new EditGroupDescriptor(editGroupDescriptor);
        this.editStudentDescriptor = null;
    }

    /**
     * Constructs an EditCommand for editing a student's details.
     *
     * @param target                The path to the target student to be edited.
     * @param editStudentDescriptor The descriptor containing the details to edit.
     */
    public EditCommand(AbsolutePath target, EditStudentDescriptor editStudentDescriptor) {
        this.target = target;
        this.editStudentDescriptor = new EditStudentDescriptor(editStudentDescriptor);
        this.editGroupDescriptor = null;
    }

    private EditCommand() {
        this.target = null;
        this.editGroupDescriptor = null;
        this.editStudentDescriptor = null;
    }

    /**
     * Executes the EditCommand to edit a group or student's details.
     *
     * @param model The current model of the application.
     * @return A CommandResult indicating the result of the execution.
     * @throws CommandException If there's an error during command execution.
     */
    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        // Check path exists in ProfBook
        if (!model.hasPath(target)) {
            throw new CommandException(MESSAGE_NO_SUCH_PATH);
        }

        if (target.isGroupDirectory()) {
            return handleEditGroup(model);
        }

        if (target.isStudentDirectory()) {
            return handleEditStudent(model);
        }

        throw new CommandException(MESSAGE_INCORRECT_DIRECTORY_ERROR);
    }

    private CommandResult handleEditGroup(Model model) throws CommandException {
        if (!model.hasGroup(target)) {
            throw new CommandException(ERROR_MESSAGE_NO_SUCH_GROUP);
        }

        GroupId groupId = target.getGroupId().get();

        ChildOperation<Group> rootOperation = model.rootChildOperation();
        Group groupToEdit = rootOperation.getChild(groupId);
        Group editedGroup = createEditedGroup(groupToEdit, this.editGroupDescriptor);

        // Check whether group is actually edited
        if (editedGroup.equals(groupToEdit)) {
            throw new CommandException(MESSAGE_NO_CHANGES_MADE);
        }

        // Check if Id is edited, if is edited check whether new id has already been used.
        boolean idIsEdited = !editedGroup.getId().equals(groupId);
        if (idIsEdited && model.hasGroupWithId(editedGroup.getId())) {
            Group groupWithSameId = model.getGroupWithId(editedGroup.getId());
            throw new CommandException(String.format(
                MESSAGE_DUPLICATE_GROUP_ID, editedGroup.getId(), Messages.format(groupWithSameId)));
        }

        rootOperation.deleteChild(groupId);
        rootOperation.addChild(editedGroup.getId(), editedGroup);

        // If edited group is current path, need to redirect with new Id.
        if (target.equals(model.getCurrPath())) {
            try {
                model.changeDirectory(model.getCurrPath().resolve(RelativePath.PARENT));
                model.changeDirectory(model.getCurrPath().resolve(new RelativePath(editedGroup.getId().toString())));
            } catch (InvalidPathException e) {
                throw new IllegalArgumentException("Internal Error: " + e.getMessage());
            }
        }

        model.updateList();

        return new CommandResult(MESSAGE_EDIT_GROUP_SUCCESS);
    }

    private CommandResult handleEditStudent(Model model) throws CommandException {
        if (!model.hasStudent(target)) {
            throw new CommandException(MESSAGE_NO_SUCH_STUDENT);
        }

        StudentId studentId = target.getStudentId().get();

        ChildOperation<Student> groupOperation = model.groupChildOperation(target);
        Student studentToEdit = groupOperation.getChild(studentId);
        Student editedStudent = createEditedStudent(studentToEdit, this.editStudentDescriptor);

        // Check whether student is actually edited
        if (editedStudent.equals(studentToEdit)) {
            throw new CommandException(MESSAGE_NO_CHANGES_MADE);
        }

        // Check if Id is edited, if is edited check whether new id has already been used.
        boolean idIsEdited = !editedStudent.getId().equals(studentId);
        if (idIsEdited && model.hasStudentWithId(editedStudent.getId())) {
            Student studentWithSameId = model.getStudentWithId(editedStudent.getId());
            throw new CommandException(String.format(
                MESSAGE_DUPLICATE_STUDENT_ID, editedStudent.getId(), Messages.format(studentWithSameId)));
        }

        groupOperation.deleteChild(studentId);
        groupOperation.addChild(editedStudent.getId(), editedStudent);

        model.updateList();

        return new CommandResult(MESSAGE_EDIT_STUDENT_SUCCESS);
    }

    /**
     * Creates and returns a {@code Student} with the details of {@code studentToEdit}
     * edited with {@code editStudentDescriptor}.
     */
    private static Student createEditedStudent(Student studentToEdit, EditStudentDescriptor editStudentDescriptor) {
        assert studentToEdit != null;
        Name updatedName = editStudentDescriptor.getName().orElse(studentToEdit.getName());
        Phone updatedPhone = editStudentDescriptor.getPhone().orElse(studentToEdit.getPhone());
        Email updatedEmail = editStudentDescriptor.getEmail().orElse(studentToEdit.getEmail());
        Address updatedAddress = editStudentDescriptor.getAddress().orElse(studentToEdit.getAddress());
        StudentId updatedId = editStudentDescriptor.getId().orElse(studentToEdit.getId());
        ReadOnlyTaskList taskList = new TaskListManager(studentToEdit.getAllTasks());
        return new Student(taskList, updatedName, updatedEmail, updatedPhone, updatedAddress, updatedId);
    }

    /**
     * Creates and returns a {@code Group} with the details of {@code groupToEdit}
     * edited with {@code editGroupDescriptor}.
     */
    private static Group createEditedGroup(Group groupToEdit, EditGroupDescriptor editGroupDescriptor) {
        assert groupToEdit != null;
        Name updatedName = editGroupDescriptor.getName().orElse(groupToEdit.getName());
        GroupId updatedId = editGroupDescriptor.getId().orElse(groupToEdit.getId());
        ReadOnlyTaskList taskList = new TaskListManager(groupToEdit.getAllTasks());
        Map<Id, Student> students = groupToEdit.getChildren();
        return new Group(taskList, students, updatedName, updatedId);
    }


    /**
     * Checks if this EditCommand is equal to another object.
     *
     * @param other The object to compare with.
     * @return True if the objects are equal, false otherwise.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof EditCommand)) {
            return false;
        }

        EditCommand otherEditCommand = (EditCommand) other;
        if (this.editStudentDescriptor == null) {
            return this.target.equals(otherEditCommand.target)
                    && this.editGroupDescriptor.equals(otherEditCommand.editGroupDescriptor);
        } else {
            return this.target.equals(otherEditCommand.target)
                    && this.editStudentDescriptor.equals(otherEditCommand.editStudentDescriptor);
        }
    }

    /**
     * Returns a string representation of this EditCommand.
     *
     * @return A string representation of the object.
     */
    @Override
    public String toString() {
        if (this.editStudentDescriptor != null) {
            return new ToStringBuilder(this)
                    .add("toEdit", this.editStudentDescriptor)
                    .toString();
        }
        if (this.editGroupDescriptor != null) {
            return new ToStringBuilder(this)
                    .add("toEdit", this.editGroupDescriptor)
                    .toString();
        }
        return null;
    }
}
