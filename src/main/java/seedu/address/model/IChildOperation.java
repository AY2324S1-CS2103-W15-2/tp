//@@author mingyuanc
package seedu.address.model;

import java.util.List;

import seedu.address.model.field.EditDescriptor;
import seedu.address.model.id.Id;
import seedu.address.model.profbook.IChildElement;
import seedu.address.model.task.Task;

/**
 * Interface for classes that operations that involve children, ensures that all
 * basic functions are present to interact
 * with ChildManager instance.
 *
 * @param <T> Type of the child
 */
public interface IChildOperation<T extends IChildElement<T>> {

    /**
     * Adds the child to list of children.
     * Id must not be duplicate.
     *
     * @param id    - Unique identifier of the child
     * @param child - The child in question
     */
    void addChild(Id id, T child);

    /**
     * Checks if the child is present.
     *
     * @param id - Unique identifier of the child
     */
    boolean hasChild(Id id);

    /**
     * Deletes the child specified by the id.
     * Target child must exist.
     *
     * @param id - Unique identifier of the child
     * @return The deleted Child
     */
    T deleteChild(Id id);

    /**
     * Edits a specific child using the provided edit descriptor.
     * Target child must exist.
     *
     * @param id         The identifier of the child to be edited.
     * @param descriptor The edit descriptor defining operations for the item.
     * @return An edited instance of the child item within the group.
     */
    T editChild(Id id, EditDescriptor<T> descriptor);

    /**
     * Returns the child specified by the id.
     * Target child must exist.
     *
     * @param id - Unique identifier of the child
     * @return The specified Child
     */
    T getChild(Id id);

    /**
     * Replaces the target child with a new child.
     *
     * @param id    - Unique identifier of the child
     * @param child - The new child to replace old child
     */
    void updateChild(Id id, T child);

    /**
     * Returns a list of all current children
     *
     * @return list of all current children
     */
    List<T> getAllChildren();

    /**
     * Adds a task to children within the hierarchy up to a specified depth level.
     * e.g. if target is root and level is 2, task will be added to all student
     */
    void addTaskToAllChildren(Task task, int level);

    /**
     * Returns {@code true} if all children at {@code level} have the task.
     * The {@code level} must be level of task list manager.
     * e.g. Group level and student level.
     */
    boolean doAllChildrenHaveTasks(Task task, int level);

    /**
     * Returns {@code true} if at least one child at {@code level} has the task.
     * The {@code level} must be level of task list manager.
     * e.g. Group level and student level.
     */
    boolean doAnyChildrenHaveTasks(Task task, int level);

    /**
     * Returns Number of current children
     *
     * @return The Number of current children
     */
    int numOfChildren();

}
