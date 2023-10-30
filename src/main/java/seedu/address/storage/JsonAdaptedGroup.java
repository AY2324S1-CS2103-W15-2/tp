package seedu.address.storage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.id.GroupId;
import seedu.address.model.id.Id;
import seedu.address.model.profbook.Group;
import seedu.address.model.profbook.Name;
import seedu.address.model.profbook.Student;
import seedu.address.model.task.Deadline;
import seedu.address.model.task.ReadOnlyTaskList;
import seedu.address.model.task.Task;
import seedu.address.model.task.ToDo;

/**
 * A class to adapt a Group object into a format suitable for JSON storage.
 */
public class JsonAdaptedGroup {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Group's %s field is missing!";
    /**
     * Name of the group
     */
    private final String name;

    /**
     * Unique identifier of the group
     */
    private final String id;

    private final Set<JsonAdaptedStudent> students = new HashSet<>();
    private final Set<JsonAdaptedTasks> tasks = new HashSet<>();

    /**
     * Constructs a {@code JsonAdaptedGroup} with the given person details.
     */
    @JsonCreator
    public JsonAdaptedGroup(@JsonProperty("name") String name, @JsonProperty("id") String id,
                            @JsonProperty("students") List<JsonAdaptedStudent> students,
                            @JsonProperty("tasks") List<JsonAdaptedTasks> tasks) {
        this.name = name;
        this.id = id;
        if (students != null) {
            this.students.addAll(students);
        }
        if (tasks != null) {
            this.tasks.addAll(tasks);
        }

    }

    /**
     * Converts a given {@code Group} into this class for Jackson use.
     */
    public JsonAdaptedGroup(Group source) {
        name = source.getName().fullName;
        id = source.getId().toString();
        students.addAll(source.getAllChildren().stream().map(JsonAdaptedStudent::new).collect(Collectors.toList()));
        tasks.addAll(source.getAllTask().stream()
                .map(task -> (task instanceof ToDo)
                        ? new JsonAdaptedToDo((ToDo) task)
                        : new JsonAdaptedDeadline((Deadline) task))
                .collect(Collectors.toList()));
    }

    /**
     * Converts this Jackson-friendly adapted group object into the model's Group object.
     *
     * @return The Group object that corresponds to this JsonAdaptedGroup.
     * @throws IllegalValueException If there were any data constraints violated in the adapted group.
     */
    public Group toModelType() throws IllegalValueException {
        final List<Task> taskList = new ArrayList<>();

        Map<Id, Student> studentMap = new HashMap<>();
        for (JsonAdaptedStudent studJson : students) {
            Student student = studJson.toModelType();
            studentMap.put(student.getId(), student);
        }
        for (JsonAdaptedTasks task : tasks) {

            taskList.add(task.toModelType());
        }
        if (name == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    Name.class.getSimpleName()));
        }
        if (!Name.isValidName(name)) {
            throw new IllegalValueException(Name.MESSAGE_CONSTRAINTS);
        }
        final Name modelName = new Name(name);

        if (id == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    GroupId.class.getSimpleName()));
        }
        if (!GroupId.isValidGroupId(id)) {
            throw new IllegalValueException(GroupId.MESSAGE_CONSTRAINTS);
        }
        final GroupId grpId = new GroupId(id);

        final ReadOnlyTaskList modelTList = new ReadOnlyTaskList(taskList);

        return new Group(modelTList, studentMap, modelName, grpId);
    }
}
