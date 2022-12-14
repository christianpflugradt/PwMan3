package de.pflugradts.pwman3.application.commandhandling.handler;

import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import de.pflugradts.pwman3.application.UserInterfaceAdapterPort;
import de.pflugradts.pwman3.application.commandhandling.command.ViewCommand;
import de.pflugradts.pwman3.application.failurehandling.FailureCollector;
import de.pflugradts.pwman3.domain.model.transfer.Output;
import de.pflugradts.pwman3.domain.service.password.PasswordService;

public class ViewCommandHandler implements CommandHandler {

    @Inject
    private FailureCollector failureCollector;
    @Inject
    private PasswordService passwordService;
    @Inject
    private UserInterfaceAdapterPort userInterfaceAdapterPort;

    @Subscribe
    private void handleViewCommand(final ViewCommand viewCommand) {
        passwordService.viewPassword(viewCommand.getArgument()).ifPresent(result -> result
                .onFailure(throwable -> failureCollector
                        .collectPasswordEntryFailure(viewCommand.getArgument(), throwable))
                .onSuccess(passwordBytes -> userInterfaceAdapterPort.send(Output.of(passwordBytes))));
        viewCommand.invalidateInput();
        userInterfaceAdapterPort.sendLineBreak();
    }

}
