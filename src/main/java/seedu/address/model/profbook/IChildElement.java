package seedu.address.model.profbook;

import seedu.address.model.id.Id;
import seedu.address.ui.Displayable;

/**
 * Encapsulate required logic for child element of ProfBok Model
 */
public interface IChildElement<T> extends Displayable, Comparable<T> {

    /**
     * Returns the unique id of the child.
     */
    Id getId();

    /**
     * Creates a deep copy of the current element, this is to achieve immutability.
     *
     * @return The clone of the IChildElement
     */
    T deepCopy();

}
