package de.pflugradts.pwman3.application.commandhandling.command;

import de.pflugradts.pwman3.domain.model.transfer.Input;

public class ExportCommand extends AbstractFilenameCommand {

    protected ExportCommand(final Input input) {
        super(input);
    }

}
