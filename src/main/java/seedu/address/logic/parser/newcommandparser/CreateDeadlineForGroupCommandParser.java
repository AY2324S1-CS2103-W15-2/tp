package seedu.address.logic.parser.newcommandparser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.OPTION_DATETIME;
import static seedu.address.logic.parser.CliSyntax.OPTION_DESC;

import java.time.LocalDateTime;

import seedu.address.logic.newcommands.CreateDeadlineForGroupCommand;
import seedu.address.logic.parser.ArgumentMultimap;
import seedu.address.logic.parser.ArgumentTokenizer;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.path.RelativePath;
import seedu.address.model.taskmanager.Deadline;

/**
 * Parses input arguments and creates a new CreateDeadlineForGroupCommand object
 */
public class CreateDeadlineForGroupCommandParser {
    //todo: only need one deadline command

    /**
     * Parses the given {@code String} of arguments in the context of the CreateDeadlineForGroupCommand
     * and returns an CreateDeadlineForGroupCommand object for execution.
     *
     * @throws ParseException if the user input does not conform the expected format
     */
    public CreateDeadlineForGroupCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, OPTION_DESC, OPTION_DATETIME);

        if (!ParserUtil.areOptionsPresent(argMultimap, OPTION_DESC, OPTION_DATETIME)
                || argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, "todo/usage_format"));
        }

        argMultimap.verifyNoDuplicateOptionsFor(OPTION_DESC, OPTION_DATETIME);

        RelativePath path = ParserUtil.parsePath(argMultimap.getPreamble());
        LocalDateTime by = ParserUtil.parseDateTime(argMultimap.getValue(OPTION_DATETIME).get());

        Deadline deadline = new Deadline(argMultimap.getValue(OPTION_DESC).get(), by);

        return new CreateDeadlineForGroupCommand(path, deadline);
    }
}