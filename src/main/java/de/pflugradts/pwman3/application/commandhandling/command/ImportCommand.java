package de.pflugradts.pwman3.application.commandhandling.command;

import de.pflugradts.pwman3.domain.model.transfer.Input;

public class ImportCommand extends AbstractFilenameCommand {

    protected ImportCommand(final Input input) {
        super(input);
    }

}
