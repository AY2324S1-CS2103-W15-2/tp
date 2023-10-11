package seedu.address.testutil;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import seedu.address.model.id.GroupId;
import seedu.address.model.id.Id;
import seedu.address.model.profbook.Group;
import seedu.address.model.profbook.Name;
import seedu.address.model.profbook.Student;
import seedu.address.model.taskmanager.Deadline;
import seedu.address.model.taskmanager.Task;
import seedu.address.model.taskmanager.TaskList;

/**
 * A utility class to help with building Group objects.
 */
public class GroupBuilder {
    public static final Task DEFAULT_TASK = new Deadline("Assignment 1", LocalDateTime.parse("2023-12-03T23:59"));
    public static final String DEFAULT_NAME = "Group ProfBook";
    public static final String DEFAULT_ID = "grp-001";

    private TaskList taskList;
    private Map<Id, Student> students;
    private Id id;
    private Name name;

    /**
     * Creates a {@code GroupBuilder} with the default details.
     */
    public GroupBuilder() {
        List<Task> defaultTaskList = new ArrayList<>();
        defaultTaskList.add(DEFAULT_TASK);
        taskList = new TaskList(defaultTaskList);
        students = new HashMap<>();
        Student stu = new StudentBuilder().build();
        students.put(stu.getId(), stu);
        name = new Name(DEFAULT_NAME);
        id = new GroupId(DEFAULT_ID);
    }

    public Group build() {
        return new Group(taskList, students, name, id);
    }
}
