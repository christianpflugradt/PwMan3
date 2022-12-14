package de.pflugradts.pwman3.application.eventhandling;

import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import de.pflugradts.pwman3.application.UserInterfaceAdapterPort;
import de.pflugradts.pwman3.domain.model.event.PasswordEntryCreated;
import de.pflugradts.pwman3.domain.model.event.PasswordEntryDiscarded;
import de.pflugradts.pwman3.domain.model.event.PasswordEntryNotFound;
import de.pflugradts.pwman3.domain.model.event.PasswordEntryRenamed;
import de.pflugradts.pwman3.domain.model.event.PasswordEntryUpdated;
import de.pflugradts.pwman3.domain.model.transfer.Bytes;
import de.pflugradts.pwman3.domain.model.transfer.Output;
import de.pflugradts.pwman3.domain.service.eventhandling.EventHandler;
import de.pflugradts.pwman3.domain.service.password.encryption.CryptoProvider;

public class ApplicationEventHandler implements EventHandler {

    @Inject
    private CryptoProvider cryptoProvider;
    @Inject
    private UserInterfaceAdapterPort userInterfaceAdapterPort;

    @Subscribe
    private void handlePasswordEntryCreated(final PasswordEntryCreated passwordEntryCreated) {
        cryptoProvider
                .decrypt(passwordEntryCreated
                        .getPasswordEntry()
                        .viewKey())
                .onSuccess(key -> sendToUserInterface(
                        "PasswordEntry '%s' successfully created.", key));
    }

    @Subscribe
    private void handlePasswordEntryUpdated(final PasswordEntryUpdated passwordEntryUpdated) {
        cryptoProvider
                .decrypt(passwordEntryUpdated
                        .getPasswordEntry()
                        .viewKey())
                .onSuccess(key -> sendToUserInterface(
                        "PasswordEntry '%s' successfully updated.", key));
    }

    @Subscribe
    private void handlePasswordEntryRenamed(final PasswordEntryRenamed passwordEntryRenamed) {
        cryptoProvider
                .decrypt(passwordEntryRenamed
                        .getPasswordEntry()
                        .viewKey())
                .onSuccess(key -> sendToUserInterface(
                        "PasswordEntry '%s' successfully renamed.", key));
    }

    @Subscribe
    private void handlePasswordEntryDiscarded(final PasswordEntryDiscarded passwordEntryDiscarded) {
        cryptoProvider
                .decrypt(passwordEntryDiscarded
                        .getPasswordEntry()
                        .viewKey())
                .onSuccess(key -> sendToUserInterface(
                        "PasswordEntry '%s' successfully deleted.", key));
    }

    @Subscribe
    private void handlePasswordEntryNotFound(final PasswordEntryNotFound passwordEntryNotFound) {
        cryptoProvider
                .decrypt(passwordEntryNotFound
                        .getKeyBytes())
                .onSuccess(key -> sendToUserInterface(
                        "PasswordEntry '%s' not found.", key));
    }

    private void sendToUserInterface(final String template, final Bytes keyBytes) {
        userInterfaceAdapterPort.send(Output.of(Bytes.of(
                String.format(template, keyBytes.asString()))));
    }

}
