package de.pflugradts.pwman3.application.commandhandling.command;

import de.pflugradts.pwman3.domain.model.transfer.Input;

public class CustomSetCommand extends AbstractSingleCharInputCommand {

    protected CustomSetCommand(final Input input) {
        super(input);
    }

}
