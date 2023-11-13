//@@author mingyuanc
package seedu.address.model;

import java.util.List;

import seedu.address.model.task.Task;
import seedu.address.model.task.exceptions.NoSuchTaskException;

/**
 * Interface for classes that operations that involve a task list, ensures that all basic functions are present to
 * interact with TaskListManager instance
 */
public interface ITaskOperation {

    /**
     * Adds the tasks to the task list granted it does not result in a duplicate
     *
     * @param t The task in question
     */
    void addTask(Task t);

    /**
     * Check if index is between 0 and task list size.
     */
    boolean isValidIndex(int index);

    /**
     * Checks if current task is present
     *
     * @param t The task in question
     */
    boolean hasTask(Task t);

    /**
     * Return the current size of the task list.
     */
    int getTaskListSize();

    /**
     * Deletes the task at the specified index
     *
     * @param index - The index of the targeted class
     * @return The deleted class
     * @throws NoSuchTaskException if no task can be found by the index
     */
    Task deleteTask(int index) throws NoSuchTaskException;

    /**
     * Marks the task at the specified index as completed
     *
     * @param index - The index of the targeted class
     * @return The marked task
     * @throws NoSuchTaskException if no task can be found by the index
     */
    Task markTask(int index) throws NoSuchTaskException;

    /**
     * Marks the task at the specified index as not completed
     *
     * @param index - The index of the targeted class
     * @return The un-marked task
     * @throws NoSuchTaskException if no task can be found by the index
     */
    Task unmarkTask(int index) throws NoSuchTaskException;

    /**
     * Finds all matching task, compares by the task's description
     *
     * @param query - The String to match
     * @return A list of all matching Tasks
     */
    List<Task> findTask(String query) throws NoSuchTaskException;

    /**
     * Returns the task at the specified index
     *
     * @param index - The index of the targeted class
     * @return The specified task
     * @throws NoSuchTaskException if no task can be found by the index
     */
    Task getTask(int index) throws NoSuchTaskException;

    /**
     * Returns all current task
     *
     * @return A list of all Tasks
     */
    List<Task> getAllTasks() throws NoSuchTaskException;

}
