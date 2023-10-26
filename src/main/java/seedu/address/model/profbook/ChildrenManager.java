package seedu.address.model.profbook;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import seedu.address.model.id.Id;
import seedu.address.model.profbook.exceptions.DuplicateChildException;
import seedu.address.model.profbook.exceptions.NoSuchChildException;

/**
 * Encapsulates the logic of a ProfBookModel that contains children
 * As of v1.2 it is only root and group class
 *
 * @param <T> to represent the children type, as of v1.2 only student and group
 */
public class ChildrenManager<T extends IChildElement> {
    /**
     * Maps the id to the children
     */
    private final Map<Id, T> children;

    /**
     * Construct a children manager with given task list and children map.
     */
    public ChildrenManager(Map<Id, T> children) {
        requireAllNonNull(children);
        this.children = children;
    }

    /**
     * Construct a new children manager.
     */
    public ChildrenManager() {
        super();
        children = new HashMap<>();
    }

    /**
     * Adds the child to list of children
     *
     * @param id    - Unique identifier of the child
     * @param child - The child in question
     * @throws DuplicateChildException If attempting to add child with the same ID
     */
    public void addChild(Id id, T child) throws DuplicateChildException {
        T currChild = this.children.get(id);
        if (currChild != null) {
            throw new DuplicateChildException(id.toString());
        }
        this.children.put(id, child);
    }

    /**
     * Deletes the child specified by the id
     *
     * @param id - Unique identifier of the child
     * @return The deleted Child
     * @throws NoSuchChildException If there is no such Child found
     */
    public T deleteChild(Id id) throws NoSuchChildException {
        T child = this.getChild(id);
        this.children.remove(id);
        return child;
    }

    /**
     * Checks if the child is present
     *
     * @param id - Unique identifier of the child
     * @return true if the child is present
     */
    public boolean hasChild(Id id) {
        return this.children.containsKey(id);
    }

    /**
     * Returns the child specified by the id
     *
     * @param id - Unique identifier of the child
     * @return The specified Child
     * @throws NoSuchChildException If there is no such Child found
     */
    public T getChild(Id id) throws NoSuchChildException {
        T child = this.children.get(id);
        if (child == null) {
            throw new NoSuchChildException(id.toString());
        }
        return child;
    }

    /**
     * Returns Number of current children
     *
     * @return The Number of current children
     */
    public int numOfChildren() {
        return this.children.size();
    }

    /**
     * Returns a list of all current children
     *
     * @return list of all current children
     */
    public List<T> getAllChildren() {
        return new ArrayList<>(this.children.values());
    }

    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder();
        for (Map.Entry<Id, T> entry : this.children.entrySet()) {
            ret.append(entry.getKey().toString()).append(": ").append(entry.getValue().toString()).append("\n");
        }
        return ret.toString();
    }

    public Map<Id, T> getChildren() {
        return new HashMap<>(this.children);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof ChildrenManager<?>)) {
            return false;
        }

        ChildrenManager<?> otherChildrenManger = (ChildrenManager<?>) other;
        return this.children.equals(otherChildrenManger.children);
    }
}