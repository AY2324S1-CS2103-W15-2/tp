//@@author mingyuanc
package seedu.address.model;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;
import static seedu.address.logic.Messages.MESSAGE_INTERNAL_ERROR;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.id.GroupId;
import seedu.address.model.id.StudentId;
import seedu.address.model.path.AbsolutePath;
import seedu.address.model.profbook.Group;
import seedu.address.model.profbook.Root;
import seedu.address.model.profbook.Student;
import seedu.address.ui.Displayable;

/**
 * Represents the in-memory model of the ProfBook data.
 */
public class ModelManager implements Model {
    public static final String MESSAGE_GROUP_INFO_NOT_FOUND = "Path must have group information.";
    public static final String MESSAGE_REQUIRE_CHILDREN_MANAGER_PATH = "Path must be children manager.";
    public static final String MESSAGE_GROUP_ID_NOT_FOUND = "Group Id must exist in ProfBook.";
    public static final String MESSAGE_STUDENT_PATH_NOT_NAVIGABLE = "Student path is not navigable.";
    public static final String MESSAGE_UNEXPECTED_ERROR = "An unexpected error occurred.";
    public static final String MESSAGE_REQUIRE_TASK_LIST_MANAGER_PATH = "Path must be task list manager.";
    public static final String MESSAGE_REQUIRE_STUDENT_PATH = "Path must be student directory.";
    public static final String MESSAGE_PATH_NOT_FOUND = "Path must exist in ProfBook.";
    public static final String MESSAGE_STUDENT_NOT_FOUND = "Student must exist in ProfBook.";
    public static final String MESSAGE_STUDENT_ID_NOT_FOUND = "Student Id must exist in ProfBook.";
    public static final String MESSAGE_GROUP_NOT_FOUND = "Group must exist in ProfBook.";
    private static final Logger logger = LogsCenter.getLogger(Model.class);
    private final ObservableList<Displayable> displayList = FXCollections.observableArrayList();

    private final UserPrefs userPrefs;
    private Root root;
    private boolean showTaskList = false;
    private AbsolutePath currentPath;
    private AbsolutePath displayPath;

    /**
     * Constructs a new model manager with no data.
     */
    public ModelManager() {
        this.currentPath = AbsolutePath.ROOT_PATH;
        this.displayPath = AbsolutePath.ROOT_PATH;
        this.root = new Root();
        this.userPrefs = new UserPrefs();
        updateList();
    }

    /**
     * Construct a model manager with only current path, root (ProfBook) and userPrefs.
     */
    public ModelManager(AbsolutePath currentPath, Root root, ReadOnlyUserPrefs userPrefs) {
        requireAllNonNull(currentPath, root, userPrefs);
        this.userPrefs = new UserPrefs(userPrefs);
        this.displayPath = currentPath;
        this.currentPath = currentPath;
        this.root = new Root(root);
        updateList();
    }

    /**
     * Constructs a model manager with all fields.
     */
    public ModelManager(AbsolutePath currPath, Root root, ReadOnlyUserPrefs usePrefs,
                        AbsolutePath displayPath, boolean showTaskList) {
        this(currPath, root, usePrefs);
        requireAllNonNull(displayPath, showTaskList);
        this.displayPath = displayPath;
        this.showTaskList = showTaskList;
        updateList();
    }


    //=========== UserPrefs ===================================================================================

    /**
     * Updates the current storage file path
     *
     * @param addressBookFilePath The new Storage file Path
     */
    public void setProfBookFilePath(Path addressBookFilePath) {
        requireNonNull(addressBookFilePath);
        logger.info("Updating storage file path");
        userPrefs.setAddressBookFilePath(addressBookFilePath);
    }

    /**
     * Updates the current GUI preference with a new instance
     *
     * @param guiSettings the updated GUI preference
     */
    public void setGuiSettings(GuiSettings guiSettings) {
        requireNonNull(guiSettings);
        logger.info("Updating GUI preference");
        userPrefs.setGuiSettings(guiSettings);
    }

    /**
     * Gets the current storage file path
     *
     * @return The path instance
     */
    public Path getProfBookFilePath() {
        return userPrefs.getProfBookFilePath();
    }

    /**
     * Updates the current user preference with a new instance
     *
     * @param userPrefs the updated user preference
     */
    public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
        requireNonNull(userPrefs);
        logger.info("Updating user preference");
        this.userPrefs.resetData(userPrefs);
    }

    /**
     * Gets the user preference
     */
    public ReadOnlyUserPrefs getUserPrefs() {
        return this.userPrefs;
    }

    /**
     * Gets the GUI settings from user preference
     */
    public GuiSettings getGuiSettings() {
        return this.userPrefs.getGuiSettings();
    }

    //=========== ProfBook Model ================================================================================
    @Override
    public void setRoot(Root root) {
        requireNonNull(root);
        logger.info("Resetting to root directory");
        this.root = root;
        this.currentPath = AbsolutePath.ROOT_PATH;
        this.displayPath = AbsolutePath.ROOT_PATH;
        this.showTaskList = false;
        this.updateList();
    }

    @Override
    public boolean hasStudentWithId(StudentId id) {
        logger.info("finding student with id: " + id);
        for (Group group : this.root.getAllChildren()) {
            if (group.hasChild(id)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean hasChildrenListInCurrentPath() {
        return hasChildrenListInPath(currentPath);
    }

    @Override
    public boolean hasTaskListInCurrentPath() {
        return hasTaskListInPath(currentPath);
    }

    @Override
    public boolean isShowTaskList() {
        return this.showTaskList;
    }

    @Override
    public AbsolutePath getDisplayPath() {
        return this.displayPath;
    }

    @Override
    public AbsolutePath getCurrPath() {
        return this.currentPath;
    }

    @Override
    public boolean hasGroupWithId(GroupId id) {
        return this.root.hasChild(id);
    }

    @Override
    public Group getGroupWithId(GroupId id) {
        checkArgument(hasGroupWithId(id),
                String.format(MESSAGE_INTERNAL_ERROR, MESSAGE_GROUP_ID_NOT_FOUND));
        return this.root.getChild(id);
    }

    @Override
    public Student getStudentWithId(StudentId id) {
        checkArgument(hasStudentWithId(id),
                String.format(MESSAGE_INTERNAL_ERROR, MESSAGE_STUDENT_ID_NOT_FOUND));
        logger.info("Finding student with id: " + id);

        for (Group group : this.root.getAllChildren()) { // for each group, check if group has student
            if (group.hasChild(id)) {
                return group.getChild(id); // if student is present, return student
            }
        }
        // If student is not present, throw error as user should have check if present
        logger.severe("Unable to find student with id: " + id);
        throw new IllegalArgumentException(String.format(MESSAGE_INTERNAL_ERROR, MESSAGE_UNEXPECTED_ERROR));
    }


    @Override
    public boolean hasGroup(AbsolutePath path) {
        requireNonNull(path);
        checkArgument(!path.isRootDirectory(),
                String.format(MESSAGE_INTERNAL_ERROR, MESSAGE_GROUP_INFO_NOT_FOUND));
        logger.info("Finding group at " + path);

        // defensive programming
        if (path.getGroupId().isEmpty()) {
            logger.severe("Invalid path: " + path);
            throw new IllegalArgumentException(String.format(MESSAGE_INTERNAL_ERROR, MESSAGE_UNEXPECTED_ERROR));
        }

        GroupId grpId = path.getGroupId().get();
        return root.hasChild(grpId);
    }

    @Override
    public boolean hasStudent(AbsolutePath path) {
        requireNonNull(path);
        checkArgument(path.isStudentDirectory(),
                String.format(MESSAGE_INTERNAL_ERROR, MESSAGE_REQUIRE_STUDENT_PATH));
        logger.info("Finding student at " + path);

        if (!hasGroup(path)) {
            return false;
        }

        // defensive programming
        if (path.getStudentId().isEmpty()) {
            logger.severe("Invalid path: " + path);
            throw new IllegalArgumentException(String.format(MESSAGE_INTERNAL_ERROR, MESSAGE_UNEXPECTED_ERROR));
        }

        StudentId stuId = path.getStudentId().get();
        Group grp = getGroupFromPath(path);
        return grp.hasChild(stuId);
    }

    @Override
    public boolean hasPath(AbsolutePath path) {
        requireNonNull(path);
        logger.info("Checking if path is present, path: " + path);

        if (path.isRootDirectory()) {
            return true;
        }

        if (path.isGroupDirectory()) {
            return hasGroup(path);
        }

        return hasStudent(path);
    }

    @Override
    public void changeDirectory(AbsolutePath path) {
        requireNonNull(path);
        checkArgument(hasPath(path),
                String.format(MESSAGE_INTERNAL_ERROR, MESSAGE_PATH_NOT_FOUND));
        checkArgument(!path.isStudentDirectory(),
                String.format(MESSAGE_INTERNAL_ERROR, MESSAGE_STUDENT_PATH_NOT_NAVIGABLE));
        this.currentPath = path;
        this.displayPath = path;
        logger.fine("Change directory to " + currentPath);
        showChildrenList();
    }

    //=========== Display Panel Settings =============================================================
    @Override
    public ObservableList<Displayable> getDisplayList() {
        return displayList;
    }

    @Override
    public void updateList() {
        logger.info("Updating display list");
        List<? extends Displayable> temp = new ArrayList<>();

        if (this.showTaskList) { // If showing task list should get all current tasks
            TaskOperation taskOperation = taskOperation(this.displayPath);
            temp = new ArrayList<>(taskOperation.getAllTasks());
        } else if (this.displayPath.isRootDirectory()) { // If showing root, get all groups under root
            ChildOperation<Group> childOperation = rootChildOperation();
            temp = new ArrayList<>(childOperation.getAllChildren());
        } else if (this.displayPath.isGroupDirectory()) { // If showing group, get all student under root
            ChildOperation<Student> childOperation = groupChildOperation(this.displayPath);
            temp = new ArrayList<>(childOperation.getAllChildren());
        }
        this.displayList.clear();
        this.displayList.setAll(temp);
    }

    @Override
    public void setDisplayPath(AbsolutePath path) {
        requireNonNull(path);
        checkArgument(hasPath(path),
                String.format(MESSAGE_INTERNAL_ERROR, MESSAGE_PATH_NOT_FOUND));

        logger.info("Changing display path");
        this.displayPath = path;
    }

    @Override
    public boolean hasTaskListInDisplayPath() {
        return hasTaskListInPath(this.displayPath);
    }

    @Override
    public void showTaskList() {
        checkArgument(hasTaskListInDisplayPath(),
                String.format(MESSAGE_INTERNAL_ERROR, MESSAGE_REQUIRE_TASK_LIST_MANAGER_PATH));
        this.showTaskList = true;
        updateList();
    }

    @Override
    public void showChildrenList() {
        checkArgument(hasChildrenListInDisplayPath(),
                String.format(MESSAGE_INTERNAL_ERROR, MESSAGE_REQUIRE_CHILDREN_MANAGER_PATH));
        this.showTaskList = false;
        updateList();
    }

    @Override
    public boolean hasChildrenListInDisplayPath() {
        return hasChildrenListInPath(this.displayPath);
    }


    //=========== Model Management Operation =============================================================
    @Override
    public ChildOperation<Group> rootChildOperation() {
        logger.info("New GroupChildOperation at root");
        return new ChildOperation<>(this.root);
    }

    @Override
    public ChildOperation<Student> groupChildOperation(AbsolutePath path) {
        requireNonNull(path);
        checkArgument(path.isGroupDirectory() || path.isStudentDirectory(),
                String.format(MESSAGE_INTERNAL_ERROR, MESSAGE_GROUP_INFO_NOT_FOUND));
        checkArgument(hasGroup(path),
                String.format(MESSAGE_INTERNAL_ERROR, MESSAGE_GROUP_NOT_FOUND));

        logger.info("New GroupChildOperation at group: " + path);
        return new ChildOperation<>(getGroupFromPath(path));
    }

    @Override
    public TaskOperation taskOperation(AbsolutePath path) {
        requireNonNull(path);
        checkArgument(hasTaskListInPath(path),
                String.format(MESSAGE_INTERNAL_ERROR, MESSAGE_REQUIRE_TASK_LIST_MANAGER_PATH));
        checkArgument(hasPath(path),
                String.format(MESSAGE_INTERNAL_ERROR, MESSAGE_PATH_NOT_FOUND));
        logger.info("New TaskOperation");

        if (path.isGroupDirectory()) {
            return new TaskOperation(getGroupFromPath(path));
        }

        return new TaskOperation(getStudentFromPath(path));
    }

    //=========== Helper Method =============================================================
    @Override
    public boolean hasTaskListInPath(AbsolutePath path) {
        requireNonNull(path);
        return path.isGroupDirectory() || path.isStudentDirectory();
    }

    @Override
    public boolean hasChildrenListInPath(AbsolutePath path) {
        requireNonNull(path);
        return path.isRootDirectory() || path.isGroupDirectory();
    }

    /**
     * Return the group of the given path.
     *
     * @param path - must point to a valid and present group
     */
    private Group getGroupFromPath(AbsolutePath path) {
        requireNonNull(path);
        checkArgument(path.isGroupDirectory() || path.isStudentDirectory(),
                String.format(MESSAGE_INTERNAL_ERROR, MESSAGE_GROUP_INFO_NOT_FOUND));
        checkArgument(hasGroup(path),
                String.format(MESSAGE_INTERNAL_ERROR, MESSAGE_GROUP_NOT_FOUND));

        // defensive programming
        if (path.getGroupId().isEmpty()) {
            logger.severe("Invalid path: " + path);
            throw new IllegalArgumentException(String.format(MESSAGE_INTERNAL_ERROR, MESSAGE_UNEXPECTED_ERROR));
        }

        GroupId grpId = path.getGroupId().get();

        return root.getChild(grpId);
    }

    /**
     * Return the group of the given path.
     *
     * @param path - must point to a valid and present group
     */
    private Student getStudentFromPath(AbsolutePath path) {
        requireNonNull(path);
        checkArgument(path.isStudentDirectory(),
                String.format(MESSAGE_INTERNAL_ERROR, MESSAGE_REQUIRE_STUDENT_PATH));
        checkArgument(hasStudent(path),
                String.format(MESSAGE_INTERNAL_ERROR, MESSAGE_STUDENT_NOT_FOUND));

        // defensive programming
        if (path.getGroupId().isEmpty() || path.getStudentId().isEmpty()) {
            logger.severe("Invalid path: " + path);
            throw new IllegalArgumentException(String.format(MESSAGE_INTERNAL_ERROR, MESSAGE_UNEXPECTED_ERROR));
        }

        GroupId grpId = path.getGroupId().get();
        StudentId stuId = path.getStudentId().get();

        return root.getChild(grpId).getChild(stuId);
    }

    /**
     * Return the Root of ProfBook.
     */
    @Override
    public Root getRoot() {
        return this.root;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof ModelManager)) {
            return false;
        }

        ModelManager otherStateManager = (ModelManager) other;
        return this.showTaskList == otherStateManager.showTaskList
                && this.currentPath.equals(otherStateManager.currentPath)
                && this.displayList.equals(otherStateManager.displayList)
                && this.displayPath.equals(otherStateManager.displayPath)
                && this.root.equals(otherStateManager.root)
                && this.userPrefs.equals(otherStateManager.userPrefs);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("showTaskList", showTaskList)
                .add("Current Path", currentPath)
                .add("Display List", displayList)
                .add("Display Path", displayPath)
                .add("root", root)
                .add("userPrefs", userPrefs)
                .toString();
    }
}
