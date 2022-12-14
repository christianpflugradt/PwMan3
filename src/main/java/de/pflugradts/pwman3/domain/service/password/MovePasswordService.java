package de.pflugradts.pwman3.domain.service.password;

import com.google.inject.Inject;
import de.pflugradts.pwman3.domain.model.event.PasswordEntryNotFound;
import de.pflugradts.pwman3.domain.model.namespace.NamespaceSlot;
import de.pflugradts.pwman3.domain.model.password.KeyAlreadyExistsException;
import de.pflugradts.pwman3.domain.model.transfer.Bytes;
import de.pflugradts.pwman3.domain.service.eventhandling.EventRegistry;
import de.pflugradts.pwman3.domain.service.password.encryption.CryptoProvider;
import de.pflugradts.pwman3.domain.service.password.storage.PasswordEntryRepository;
import io.vavr.control.Try;

public class MovePasswordService implements CommonPasswordServiceCapabilities {

    @Inject
    private CryptoProvider cryptoProvider;
    @Inject
    private PasswordEntryRepository passwordEntryRepository;
    @Inject
    private EventRegistry eventRegistry;

    public Try<Void> movePassword(final Bytes keyBytes, final NamespaceSlot targetNamespace) {
        if (entryExists(cryptoProvider, passwordEntryRepository, keyBytes, targetNamespace)
                 .getOrElse(true)) {
            return Try.failure(new KeyAlreadyExistsException(keyBytes));
        } else {
            return encrypted(cryptoProvider, keyBytes).fold(
                Try::failure, encryptedKeyBytes ->
                    Try.run(() -> find(passwordEntryRepository, encryptedKeyBytes)
                        .ifPresentOrElse(
                            passwordEntry -> passwordEntry.updateNamespace(targetNamespace),
                            () -> eventRegistry.register(new PasswordEntryNotFound(encryptedKeyBytes))))
                        .andThen(() -> processEventsAndSync(eventRegistry, passwordEntryRepository)));
        }
    }

}
