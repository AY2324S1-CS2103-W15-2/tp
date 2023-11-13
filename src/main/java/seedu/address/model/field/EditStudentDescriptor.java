package seedu.address.model.field;

import java.util.Objects;
import java.util.Optional;

import seedu.address.commons.util.CollectionUtil;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.id.StudentId;
import seedu.address.model.profbook.Address;
import seedu.address.model.profbook.Email;
import seedu.address.model.profbook.Name;
import seedu.address.model.profbook.Phone;
import seedu.address.model.profbook.Student;
import seedu.address.model.task.ReadOnlyTaskList;
import seedu.address.model.task.TaskListManager;

/**
 * Represents the descriptor for editing the details of a student in ProfBook. The descriptor contains fields for
 * updating the student's name, phone, email, address, and id. It helps to track which fields have been edited by
 * the user.
 * An instance of this class is used within the {@code EditCommand} to specify the details to be edited.
 */
public class EditStudentDescriptor implements EditDescriptor<Student> {
    private Name name;
    private Phone phone;
    private Email email;
    private Address address;
    private StudentId id;

    public EditStudentDescriptor() {}

    /**
     * Copy constructor.
     * A defensive copy of {@code tags} is used internally.
     */
    public EditStudentDescriptor(EditStudentDescriptor toCopy) {
        setName(toCopy.name);
        setPhone(toCopy.phone);
        setEmail(toCopy.email);
        setAddress(toCopy.address);
        setId(toCopy.id);
    }

    @Override
    public boolean isAnyFieldEdited() {
        return CollectionUtil.isAnyNonNull(name, phone, email, address, id);
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
     * Sets the phone field in the descriptor.
     *
     * @param phone The new phone to set.
     */
    public void setPhone(Phone phone) {
        this.phone = phone;
    }

    /**
     * Gets the phone field from the descriptor.
     *
     * @return An Optional containing the phone if it's set, empty otherwise.
     */
    public Optional<Phone> getPhone() {
        return Optional.ofNullable(phone);
    }

    /**
     * Sets the email field in the descriptor.
     *
     * @param email The new phone to set.
     */
    public void setEmail(Email email) {
        this.email = email;
    }

    /**
     * Gets the email field from the descriptor.
     *
     * @return An Optional containing the email if it's set, empty otherwise.
     */
    public Optional<Email> getEmail() {
        return Optional.ofNullable(email);
    }

    /**
     * Sets the address field in the descriptor.
     *
     * @param address The new address to set.
     */
    public void setAddress(Address address) {
        this.address = address;
    }

    /**
     * Gets the address field from the descriptor.
     *
     * @return An Optional containing the address if it's set, empty otherwise.
     */
    public Optional<Address> getAddress() {
        return Optional.ofNullable(address);
    }

    /**
     * Sets the id field in the descriptor.
     *
     * @param id The new id to set.
     */
    public void setId(StudentId id) {
        this.id = id;
    }

    /**
     * Gets the id field from the descriptor.
     *
     * @return An Optional containing the id if it's set, empty otherwise.
     */
    public Optional<StudentId> getId() {
        return Optional.ofNullable(id);
    }

    @Override
    public Student applyEditsToOld(Student old) {
        assert old != null;
        Name updatedName = getName().orElse(old.getName());
        Phone updatedPhone = getPhone().orElse(old.getPhone());
        Email updatedEmail = getEmail().orElse(old.getEmail());
        Address updatedAddress = getAddress().orElse(old.getAddress());
        StudentId updatedId = getId().orElse(old.getId());
        ReadOnlyTaskList taskList = new TaskListManager(old.getAllTasks());
        return new Student(taskList, updatedName, updatedEmail, updatedPhone, updatedAddress, updatedId);
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
        if (!(other instanceof EditStudentDescriptor)) {
            return false;
        }

        EditStudentDescriptor otherEditStudentDescriptor = (EditStudentDescriptor) other;
        return Objects.equals(this.name, otherEditStudentDescriptor.name)
                && Objects.equals(this.phone, otherEditStudentDescriptor.phone)
                && Objects.equals(this.email, otherEditStudentDescriptor.email)
                && Objects.equals(this.address, otherEditStudentDescriptor.address)
                && Objects.equals(this.id, otherEditStudentDescriptor.id);
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
                .add("phone", phone)
                .add("email", email)
                .add("address", address)
                .add("id", id)
                .toString();
    }
}
