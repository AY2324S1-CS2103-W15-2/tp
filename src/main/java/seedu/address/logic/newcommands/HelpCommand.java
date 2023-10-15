package seedu.address.logic.newcommands;

import seedu.address.logic.newcommands.CommandResult;
import seedu.address.model.statemanager.State;
import seedu.address.logic.newcommands.exceptions.CommandException;

public class HelpCommand extends Command {

    public static final String COMMAND_WORD = "help";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Shows program usage instructions.\n"
            + "Example: " + COMMAND_WORD;

    public static final String SHOWING_HELP_MESSAGE = "Opened help window.";

    @Override
    public CommandResult execute(State state) throws CommandException {
        return new CommandResult(SHOWING_HELP_MESSAGE, true, false);
    }
}
