package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CreateStudentCommand.MESSAGE_DUPLICATE_STUDENT_ID;
import static seedu.address.logic.commands.CreateStudentCommand.MESSAGE_SUCCESS;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalGroups.GROUP_ONE;
import static seedu.address.testutil.TypicalRoots.PROFBOOK_WITH_TWO_GROUPS;
import static seedu.address.testutil.TypicalStudents.KAREN;
import static seedu.address.testutil.TypicalStudents.LEO;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.model.ChildOperation;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.path.AbsolutePath;
import seedu.address.model.path.RelativePath;
import seedu.address.model.path.exceptions.InvalidPathException;
import seedu.address.model.profbook.Group;
import seedu.address.model.profbook.Root;
import seedu.address.model.profbook.Student;

class CreateStudentCommandTest {
    private Model model;
    private Model expectedModel;
    private AbsolutePath rootPath = CommandTestUtil.getValidRootAbsolutePath();
    private Student toBeAdded = KAREN;
    private Group targetGroup = GROUP_ONE;
    private RelativePath groupPath;
    private RelativePath studentPath;
    private AbsolutePath targetAbsolutePath;


    @BeforeEach
    public void setup() throws InvalidPathException {
        Root root = PROFBOOK_WITH_TWO_GROUPS;
        model = new ModelManager(rootPath, new Root(root), new UserPrefs());
        expectedModel = new ModelManager(rootPath, new Root(root), new UserPrefs());

        groupPath = new RelativePath(targetGroup.getId().toString());
        studentPath = new RelativePath(toBeAdded.getId().toString());
        targetAbsolutePath = rootPath.resolve(groupPath).resolve(studentPath);
    }

    @Test
    public void constructor_nullArgs_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new CreateStudentCommand(null, null));
        assertThrows(NullPointerException.class, (
                ) -> new CreateStudentCommand(new AbsolutePath("~/grp-001/0001Y"), null));
        assertThrows(NullPointerException.class, () -> new CreateStudentCommand(null, KAREN));
    }

    @Test
    void execute_studentAcceptedByGroup_success() {
        ChildOperation<Student> operation = expectedModel.groupChildOperation(targetAbsolutePath);
        operation.addChild(toBeAdded.getId(), toBeAdded);

        CreateStudentCommand command = new CreateStudentCommand(targetAbsolutePath, toBeAdded);
        String expectedMessage = String.format(MESSAGE_SUCCESS, Messages.format(toBeAdded));

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_duplicateStudent_throwsCommandException() {
        ChildOperation<Student> operation = model.groupChildOperation(targetAbsolutePath);
        operation.addChild(toBeAdded.getId(), toBeAdded);

        CreateStudentCommand command = new CreateStudentCommand(targetAbsolutePath, toBeAdded);
        String expectedMessage = MESSAGE_DUPLICATE_STUDENT_ID;

        assertCommandFailure(command, model, expectedMessage);
    }

    @Test
    public void equals() {
        CreateStudentCommand createStudentCommand1 = new CreateStudentCommand(targetAbsolutePath, KAREN);
        CreateStudentCommand createStudentCommand2 = new CreateStudentCommand(targetAbsolutePath, LEO);

        // same object -> returns true
        assertEquals(createStudentCommand1, createStudentCommand1);

        // same values -> returns true
        CreateStudentCommand createStudentCommand1Copy = new CreateStudentCommand(targetAbsolutePath, KAREN);
        assertEquals(createStudentCommand1, createStudentCommand1Copy);

        // different types -> returns false
        assertNotEquals(1, createStudentCommand1);

        // null -> returns false
        assertNotEquals(null, createStudentCommand1);

        // different values
        assertNotEquals(createStudentCommand1, createStudentCommand2);
    }

    @Test
    public void toStringMethod() {
        CreateStudentCommand createStudentCommand = new CreateStudentCommand(targetAbsolutePath, KAREN);
        String expected = CreateStudentCommand.class.getCanonicalName()
            + "{toCreateStudent=" + KAREN + "}";
        assertEquals(expected, createStudentCommand.toString());
    }
}
