package seedu.address.model.profbook;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.id.StudentId;
import seedu.address.model.task.ReadOnlyTaskList;
import seedu.address.model.task.TaskListManager;
import seedu.address.ui.StudentCard;

/**
 * Encapsulates logic for a student's data
 */
public class Student extends TaskListManager implements IChildElement<Student> {

    // Identity field
    /**
     * Name of the group
     */
    private final Name name;
    private final Email email;
    private final Phone phone;

    // Data fields
    private final Address address;

    /**
     * Unique identifier of the student
     */
    private final StudentId id;

    /**
     * Constructs a Student instance with all fields.
     *
     * @param taskList - The task list associated with this student
     * @param name     - The group name
     * @param email    - The email of the student
     * @param phone    - Student's Phone number
     * @param address  - Students address
     * @param id       - Unique identifier of the group
     */
    public Student(ReadOnlyTaskList taskList, Name name, Email email, Phone phone, Address address, StudentId id) {
        super(taskList);
        requireAllNonNull(name, phone, email, address, id);
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.id = id;
    }

    /**
     * Constructs a new Student without task list.
     *
     * @param name    - The group name
     * @param email   - The email of the student
     * @param phone   - Student's Phone number
     * @param address - Students address
     * @param id      - Unique identifier of the group
     */
    public Student(Name name, Email email, Phone phone, Address address, StudentId id) {
        super();
        requireAllNonNull(name, phone, email, address, id);
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.id = id;
    }

    /**
     * Constructs a new instance with the data in toBeCopied.
     *
     * @param toBeCopied - Data retrieved from storage
     */
    public Student(Student toBeCopied) {
        super(toBeCopied);
        this.name = toBeCopied.name;
        this.email = toBeCopied.email;
        this.phone = toBeCopied.phone;
        this.address = toBeCopied.address;
        this.id = toBeCopied.id;
    }

    /**
     * Returns the student's name
     */
    public Name getName() {
        return name;
    }

    /**
     * Returns the student's id
     */
    public StudentId getId() {
        return id;
    }

    /**
     * Returns the student's email
     */
    public Email getEmail() {
        return email;
    }

    /**
     * Returns the student's phone number
     */
    public Phone getPhone() {
        return phone;
    }

    /**
     * Returns the student's address
     */
    public Address getAddress() {
        return address;
    }

    @Override
    public StudentCard getDisplayCard(int displayedIndex) {
        return new StudentCard(this, displayedIndex);
    }

    @Override
    public Student deepCopy() {
        return new Student(this);
    }

    @Override
    public int compareTo(Student other) {
        if (this.id.toString().compareTo(other.id.toString()) != 0) {
            return this.id.toString().compareTo(other.id.toString());
        } else {
            return this.name.fullName.compareTo(other.name.fullName);
        }
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("Student Id", id)
                .add("name", name)
                .add("phone", phone)
                .add("email", email)
                .add("address", address)
                .add("Task List", super.toString())
                .toString();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Student)) {
            return false;
        }

        Student otherStudent = (Student) other;
        return super.equals(otherStudent)
                && this.address.equals(otherStudent.address)
                && this.email.equals(otherStudent.email)
                && this.id.equals(otherStudent.id)
                && this.name.equals(otherStudent.name)
                && this.phone.equals(otherStudent.phone);
    }
}
