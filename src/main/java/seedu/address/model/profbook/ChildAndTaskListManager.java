package seedu.address.model.profbook;

import java.util.List;
import java.util.Map;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.id.Id;
import seedu.address.model.profbook.exceptions.DuplicateChildException;
import seedu.address.model.profbook.exceptions.NoSuchChildException;
import seedu.address.model.task.ITaskListManager;
import seedu.address.model.task.ReadOnlyTaskList;
import seedu.address.model.task.Task;
import seedu.address.model.task.TaskListManager;
import seedu.address.model.task.exceptions.NoSuchTaskException;

/**
 * A child element that is both children and task list Manager.
 */
public abstract class ChildAndTaskListManager<R, T extends IChildElement<T>>
        implements IChildElement<R>, IChildManager<T>, ITaskListManager {
    private final ChildManager<T> childrenManager;
    private final TaskListManager taskListManager;

    /**
     * Constructs a {@code ChildAndTaskListManager} with the data in {@code toBeCopied}.
     *
     * @param toBeCopied - data from storage
     */
    public ChildAndTaskListManager(ChildAndTaskListManager<R, T> toBeCopied) {
        this.childrenManager = new ChildManager<>(toBeCopied.childrenManager);
        this.taskListManager = new TaskListManager(toBeCopied.taskListManager);
    }

    /**
     * Constructs a new {@code ChildAndTaskListManager} with given children and task list.
     *
     * @param children - Map of current children this directory have
     * @param taskList - Arraylist of tasks assigned to this directory
     */
    public ChildAndTaskListManager(Map<Id, T> children, ReadOnlyTaskList taskList) {
        childrenManager = new ChildManager<>(children);
        taskListManager = new TaskListManager(taskList.getAllTasks());
    }

    /**
     * Constructs a new {@code ChildAndTaskListManager}.
     */
    public ChildAndTaskListManager() {
        this.childrenManager = new ChildManager<>();
        this.taskListManager = new TaskListManager();
    }

    /**
     * Returns the current task list Manager
     */
    public TaskListManager getTaskListManager() {
        return taskListManager;
    }

    //=========== Children Manager ==================================================================================

    @Override
    public void addChild(Id id, T child) throws DuplicateChildException {
        this.childrenManager.addChild(id, child);
    }

    @Override
    public T deleteChild(Id id) throws NoSuchChildException {
        return this.childrenManager.deleteChild(id);
    }

    @Override
    public boolean hasChild(Id id) {
        return this.childrenManager.hasChild(id);
    }

    @Override
    public T getChild(Id id) throws NoSuchChildException {
        return this.childrenManager.getChild(id);
    }

    @Override
    public int numOfChildren() {
        return this.childrenManager.numOfChildren();
    }

    @Override
    public List<T> getAllChildren() {
        return this.childrenManager.getAllChildren();
    }

    @Override
    public Map<Id, T> getChildren() {
        return this.childrenManager.getChildren();
    }

    //=========== TaskList Manager ==================================================================================

    @Override
    public void addTask(Task t) {
        this.taskListManager.addTask(t);
    }

    @Override
    public Task deleteTask(int index) throws NoSuchTaskException {
        return this.taskListManager.deleteTask(index);
    }

    @Override
    public Task markTask(int index) throws NoSuchTaskException {
        return this.taskListManager.markTask(index);
    }

    @Override
    public Task unmarkTask(int index) throws NoSuchTaskException {
        return this.taskListManager.unmarkTask(index);
    }

    @Override
    public List<Task> findTask(String query) throws NoSuchTaskException {
        return this.taskListManager.findTask(query);
    }

    @Override
    public boolean isValidIndex(int index) {
        return this.taskListManager.isValidIndex(index);
    }

    @Override
    public int size() {
        return this.taskListManager.size();
    }

    @Override
    public boolean isEmpty() {
        return this.taskListManager.isEmpty();
    }

    @Override
    public Task getTask(int index) throws NoSuchTaskException {
        return this.taskListManager.getTask(index);
    }

    @Override
    public boolean contains(Task t) {
        return this.taskListManager.contains(t);
    }

    @Override
    public List<Task> getAllTasks() {
        return this.taskListManager.getAllTasks();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("Task List", this.taskListManager)
                .add("Children List", this.childrenManager)
                .toString();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof ChildAndTaskListManager<?, ?>)) {
            return false;
        }

        ChildAndTaskListManager<?, ?> otherChildrenAndTaskListManager = (ChildAndTaskListManager<?, ?>) other;
        return this.childrenManager.equals(otherChildrenAndTaskListManager.childrenManager)
                && this.taskListManager.equals(otherChildrenAndTaskListManager.taskListManager);
    }
}
