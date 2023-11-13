package seedu.address.model.field;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import seedu.address.commons.util.CollectionUtil;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.id.GroupId;
import seedu.address.model.id.Id;
import seedu.address.model.profbook.Group;
import seedu.address.model.profbook.Name;
import seedu.address.model.profbook.Student;
import seedu.address.model.task.ReadOnlyTaskList;
import seedu.address.model.task.TaskListManager;

/**
 * Represents the descriptor for editing the details of a group in ProfBook. The descriptor contains fields for
 * updating the group's name and id. It helps to track which fields have been edited by the user.
 * An instance of this class is used within the {@code EditCommand} to specify the details to be edited.
 */

public class EditGroupDescriptor implements EditDescriptor<Group> {
    private Name name;

    private GroupId id;

    public EditGroupDescriptor() {}

    /**
     * Copy constructor.
     * A defensive copy of {@code tags} is used internally.
     */
    public EditGroupDescriptor(EditGroupDescriptor toCopy) {
        setName(toCopy.name);
        setId(toCopy.id);
    }

    @Override
    public boolean isAnyFieldEdited() {
        return CollectionUtil.isAnyNonNull(name, id);
    }

    /**
     * Sets the name field in the descriptor.
     *
     * @param name The new name to set.
     */
    public void setName(Name name) {
        this.name = name;
    }

    /**
     * Gets the name field from the descriptor.
     *
     * @return An Optional containing the name if it's set, empty otherwise.
     */
    public Optional<Name> getName() {
        return Optional.ofNullable(name);
    }

    /**
     * Sets the id field in the descriptor.
     *
     * @param id The new id to set.
     */
    public void setId(GroupId id) {
        this.id = id;
    }

    /**
     * Gets the id field from the descriptor.
     *
     * @return An Optional containing the id if it's set, empty otherwise.
     */
    public Optional<GroupId> getId() {
        return Optional.ofNullable(id);
    }

    @Override
    public Group applyEditsToOld(Group old) {
        assert old != null;
        Name updatedName = getName().orElse(old.getName());
        GroupId updatedId = getId().orElse(old.getId());
        ReadOnlyTaskList taskList = new TaskListManager(old.getAllTasks());
        Map<Id, Student> students = old.getChildren();
        return new Group(taskList, students, updatedName, updatedId);
    }

    /**
     * Checks if this EditStudentDescriptor is equal to another object.
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
        if (!(other instanceof EditGroupDescriptor)) {
            return false;
        }

        EditGroupDescriptor otherEditGroupDescriptor = (EditGroupDescriptor) other;

        return Objects.equals(this.name, otherEditGroupDescriptor.name)
                && Objects.equals(this.id, otherEditGroupDescriptor.id);
    }

    /**
     * Returns a string representation of this EditStudentDescriptor.
     *
     * @return A string representation of the object.
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("name", name)
                .add("id", id)
                .toString();
    }
}
