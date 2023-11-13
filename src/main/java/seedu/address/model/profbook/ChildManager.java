package seedu.address.model.profbook;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.ArrayList;
import java.util.Collections;
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
public class ChildManager<T extends IChildElement<T>> implements IChildManager<T> {

    /**
     * Maps the id to the children
     */
    private final Map<Id, T> children;

    /**
     * Constructs a children manager with given task list and children map.
     *
     * @param children - The map of the directory's current child
     */
    public ChildManager(Map<Id, T> children) {
        requireAllNonNull(children);
        Map<Id, T> tempMap = new HashMap<>();
        Id key;
        for (Id id : children.keySet()) {
            key = id;
            tempMap.put(key, children.get(key).deepCopy());
        }
        this.children = tempMap;
    }

    /**
     * Constructs a new children manager.
     */
    public ChildManager() {
        children = new HashMap<>();
    }

    /**
     * Constructs a {@code ChildManager} with the data in {@code toBeCopied}.
     *
     * @param toBeCopied - Data extracted from storage
     */
    public ChildManager(ChildManager<T> toBeCopied) {
        this(toBeCopied.children);
    }

    @Override
    public void addChild(Id id, T child) throws DuplicateChildException {
        T currChild = this.children.get(id);

        // Defensive programming
        if (currChild != null) {
            throw new DuplicateChildException(id.toString());
        }

        this.children.put(id, child);
    }

    @Override
    public T deleteChild(Id id) throws NoSuchChildException {
        T child = this.getChild(id);
        this.children.remove(id);
        return child;
    }

    @Override
    public boolean hasChild(Id id) {
        return this.children.containsKey(id);
    }

    @Override
    public T getChild(Id id) throws NoSuchChildException {
        T child = this.children.get(id);
        // Defensive programming
        if (child == null) {
            throw new NoSuchChildException(id.toString());
        }
        return child;
    }

    @Override
    public int numOfChildren() {
        return this.children.size();
    }

    @Override
    public List<T> getAllChildren() {
        List<T> childrenList = new ArrayList<>(this.children.values());
        Collections.sort(childrenList); // Important to sort as Map stores them randomly
        return childrenList;
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
        if (!(other instanceof ChildManager<?>)) {
            return false;
        }

        ChildManager<?> otherChildrenManger = (ChildManager<?>) other;
        return this.children.equals(otherChildrenManger.children);
    }
}
