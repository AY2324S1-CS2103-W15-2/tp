//@@author mingyuanc
package seedu.address.model;

import static seedu.address.commons.util.AppUtil.checkArgument;

import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.util.JsonUtil;
import seedu.address.model.task.ITaskListManager;
import seedu.address.model.task.Task;
import seedu.address.model.task.exceptions.NoSuchTaskException;

/**
 * Encapsulates the required logic for task operation
 */
public class TaskOperation implements ITaskOperation {

    private static final String MESSAGE_DUPLICATE_TASK = "Task must not exist in task list.";
    private static final String MESSAGE_TASK_NOT_FOUND = "Task not found in task list.";
    private final Logger logger = LogsCenter.getLogger(JsonUtil.class);
    private final ITaskListManager baseDir;

    /**
     * Constructs a TaskOperation instance
     *
     * @param baseDir - The current directory to enact task operations
     */
    public TaskOperation(ITaskListManager baseDir) {
        this.baseDir = baseDir;
    }

    private void stateLogger(String log) {
        this.logger.info(log);
    }

    /**
     * Checks if current task is present
     *
     * @param task The task in question
     */
    @Override
    public boolean hasTask(Task task) {
        return this.baseDir.contains(task);
    }

    /**
     * Adds a new tasks to the task list.
     * Task must not be duplicated class.
     *
     * @param task The task in question
     */
    @Override
    public void addTask(Task task) {
        checkArgument(!hasTask(task), MESSAGE_DUPLICATE_TASK);
        this.baseDir.addTask(task);
        this.stateLogger("Adding" + task.toString());
    }

    /**
     * Check if index is between 0 and task list size.
     */
    @Override
    public boolean isValidIndex(int index) {
        return this.baseDir.isValidIndex(index);
    }

    /**
     * Return the size of the task list.
     */
    @Override
    public int getTaskListSize() {
        return this.baseDir.size();
    }

    /**
     * Deletes the task at the specified index
     *
     * @param index - The index of the targeted class
     * @return The deleted class
     * @throws NoSuchTaskException if no task can be found by the index
     */
    @Override
    public Task deleteTask(int index) {
        checkArgument(isValidIndex(index), MESSAGE_TASK_NOT_FOUND);
        this.stateLogger("deleting " + index);
        return this.baseDir.deleteTask(index);
    }

    /**
     * Marks the task at the specified index as completed
     *
     * @param index - The index of the targeted class
     * @return The marked task
     * @throws NoSuchTaskException if no task can be found by the index
     */
    @Override
    public Task markTask(int index) {
        checkArgument(isValidIndex(index), MESSAGE_TASK_NOT_FOUND);
        this.stateLogger("marking " + index);
        return this.baseDir.markTask(index);
    }

    /**
     * Marks the task at the specified index as not completed
     *
     * @param index - The index of the targeted class
     * @return The un-marked task
     * @throws NoSuchTaskException if no task can be found by the index
     */
    @Override
    public Task unmarkTask(int index) {
        checkArgument(isValidIndex(index), MESSAGE_TASK_NOT_FOUND);
        this.stateLogger("un marking " + index);
        return this.baseDir.unmarkTask(index);
    }

    /**
     * Finds all matching task, compares by the task's description
     *
     * @param query - The String to match
     * @return A list of all matching Tasks
     */
    @Override
    public List<Task> findTask(String query) {
        this.stateLogger("finding " + query);
        return this.baseDir.findTask(query);
    }

    /**
     * Returns the task at the specified index
     *
     * @param index - The index of the targeted class
     * @return The specified task
     * @throws NoSuchTaskException if no task can be found by the index
     */
    @Override
    public Task getTask(int index) {
        checkArgument(isValidIndex(index), MESSAGE_TASK_NOT_FOUND);
        this.stateLogger("getting " + index);
        return this.baseDir.getTask(index);
    }

    /**
     * Returns all current task
     *
     * @return A list of all Tasks
     */
    @Override
    public List<Task> getAllTasks() {
        this.stateLogger("getting all ");
        return this.baseDir.getAllTasks();
    }

    @Override
    public String toString() {
        return this.baseDir.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TaskOperation that = (TaskOperation) o;
        return Objects.equals(baseDir, that.baseDir);
    }

    @Override
    public int hashCode() {
        return Objects.hash(baseDir);
    }
}
