package de.pflugradts.pwman3.application.commandhandling.command;

import de.pflugradts.pwman3.domain.model.transfer.Input;

public class SetCommand extends AbstractSingleCharInputCommand {

    protected SetCommand(final Input input) {
        super(input);
    }

}
