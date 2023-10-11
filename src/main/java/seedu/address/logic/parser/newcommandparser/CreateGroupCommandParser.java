package seedu.address.logic.parser.newcommandparser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.OPTION_NAME;

import java.util.ArrayList;
import java.util.HashMap;

import seedu.address.logic.newcommands.CreateGroupCommand;
import seedu.address.logic.parser.ArgumentMultimap;
import seedu.address.logic.parser.ArgumentTokenizer;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.id.GroupId;
import seedu.address.model.path.RelativePath;
import seedu.address.model.profbook.Group;
import seedu.address.model.profbook.Name;
import seedu.address.model.taskmanager.TaskList;

public class CreateGroupCommandParser implements Parser<CreateGroupCommand> {
    private static final String INVALID_PATH_MESSAGE = "Destination path provided is not a group directory.";

    public CreateGroupCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, OPTION_NAME);

        if (!ParserUtil.areOptionsPresent(argMultimap, OPTION_NAME) || argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, "todo/usage_format"));
        }

        argMultimap.verifyNoDuplicateOptionsFor(OPTION_NAME);

        RelativePath path = ParserUtil.parsePath(argMultimap.getPreamble());
        
        //todo: is possible to create student without provide id -> will auto generate id
        if (!path.isGroupDirectory()) {
            throw new ParseException(INVALID_PATH_MESSAGE);
        }
        
        Name name = ParserUtil.parseName(argMultimap.getValue(OPTION_NAME).get());
        GroupId id = ParserUtil.parseGroupId(path);

        Group group = new Group(new TaskList(new ArrayList<>()), new HashMap<>(), name, id);

        return new CreateGroupCommand(path, group);
    }
}