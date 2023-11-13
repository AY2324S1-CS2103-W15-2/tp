package seedu.address.model.field;

/**
 * An interface defining edit operations on a specific type.
 * @param <T> The type of the item being edited.
 */
public interface EditDescriptor<T> {
    /**
     * Checks if any field has been edited.
     *
     * @return {@code true} if any field has been edited.
     */
    boolean isAnyFieldEdited();

    /**
     * Creates a new instance by applying edits to an old instance.
     *
     * @param old The original instance to which edits will be applied.
     * @return A new instance with applied edits.
     */
    T applyEditsToOld(T old);
}
