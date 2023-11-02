package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.testutil.Assert.assertThrows;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.id.GroupId;
import seedu.address.model.id.Id;
import seedu.address.model.path.AbsolutePath;
import seedu.address.model.path.exceptions.InvalidPathException;
import seedu.address.model.profbook.Group;
import seedu.address.model.profbook.Name;
import seedu.address.model.profbook.Root;
import seedu.address.model.profbook.Student;
import seedu.address.model.task.TaskListManager;

public class MoveStudentToGroupCommandTest {
    public static final String ERROR_MESSAGE_INCORRECT_DIRECTORY = "Directory is invalid";

    @Test
    public void constructor_nullSourcePathAndDestinationPath_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new MoveStudentToGroupCommand(null, null));
    }

    @Test
    public void execute_invalidPathForSourceGroup_throwCommandException() throws InvalidPathException {
        Map<Id, Group> children = new HashMap<>();
        Root root = new Root(children);
        Map<Id, Student> students = new HashMap<>();
        Group group = new Group(new TaskListManager(),
                students, new Name("Group1"), new GroupId("grp-001"));
        root.addChild(group.getId(), group);
        AbsolutePath currPath = new AbsolutePath("~/");
        AbsolutePath sourcePath = new AbsolutePath("~/");
        AbsolutePath destPath = new AbsolutePath("~/grp-002");
        Model model = new ModelManager(currPath, root, new UserPrefs());
        MoveStudentToGroupCommand moveStudentToGroupCommand =
                new MoveStudentToGroupCommand(sourcePath, destPath);

        assertThrows(CommandException.class, () -> moveStudentToGroupCommand.execute(model));
    }

    @Test
    public void equals() throws InvalidPathException {
        AbsolutePath sourcePath = new AbsolutePath("~/grp-001/0001Y");
        AbsolutePath destinationPath = new AbsolutePath("~/grp-002");
        MoveStudentToGroupCommand command1 = new MoveStudentToGroupCommand(sourcePath, destinationPath);
        MoveStudentToGroupCommand command2 = new MoveStudentToGroupCommand(sourcePath, destinationPath);
        assertEquals(command1, command2);
    }

    // @Test
    // public void testOutputString() throws InvalidPathException {
    //     RelativePath relativeSourcePath = new RelativePath("~/grp-001/0001Y");
    //     RelativePath relativeDestinationPath = new RelativePath("~/grp-002");
    //     MoveStudentToGroupCommand moveStudentToGroupCommand =
    //             new MoveStudentToGroupCommand(relativeSourcePath, relativeDestinationPath);
    //     String expected =
    //             "seedu.address.logic.newcommands.MoveStudentToGroupCommand{toMoveThisStudentToAnotherGroup=null}";
    //     assertEquals(expected, moveStudentToGroupCommand.toString());
    // }
}
