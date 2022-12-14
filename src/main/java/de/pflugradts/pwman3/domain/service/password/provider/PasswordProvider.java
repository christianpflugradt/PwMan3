package de.pflugradts.pwman3.domain.service.password.provider;

import de.pflugradts.pwman3.domain.model.password.PasswordRequirements;
import de.pflugradts.pwman3.domain.model.transfer.Bytes;

/**
 * A PasswordProvider generates new Passwords.
 */
public interface PasswordProvider {
    Bytes createNewPassword(PasswordRequirements passwordRequirements);
}
