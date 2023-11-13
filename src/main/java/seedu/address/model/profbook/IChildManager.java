package seedu.address.model.profbook;

import java.util.List;
import java.util.Map;

import seedu.address.model.id.Id;
import seedu.address.model.profbook.exceptions.DuplicateChildException;
import seedu.address.model.profbook.exceptions.NoSuchChildException;

/**
 * Encapsulates the required logic for IChildManager
 *
 * @param <T> type of child being stored
 */
public interface IChildManager<T extends IChildElement<T>> {

    /**
     * Adds the child to list of children
     *
     * @param id    - Unique identifier of the child
     * @param child - The child in question
     * @throws DuplicateChildException If attempting to add child with the same ID
     */
    void addChild(Id id, T child) throws DuplicateChildException;

    /**
     * Deletes the child specified by the id
     *
     * @param id - Unique identifier of the child
     * @return The deleted Child
     * @throws NoSuchChildException If there is no such Child found
     */
    T deleteChild(Id id) throws NoSuchChildException;

    /**
     * Checks if the child is present
     *
     * @param id - Unique identifier of the child
     * @return true if the child is present
     */
    boolean hasChild(Id id);

    /**
     * Returns the child specified by the id
     *
     * @param id - Unique identifier of the child
     * @return The specified child
     * @throws NoSuchChildException If there is no such Child found
     */
    T getChild(Id id) throws NoSuchChildException;

    /**
     * Returns number of current children
     */
    int numOfChildren();

    /**
     * Returns a list of all current children
     */
    List<T> getAllChildren();

    /**
     * Returns children map.
     */
    Map<Id, T> getChildren();
}
