package de.pflugradts.pwman3.domain.service.password;

import de.pflugradts.pwman3.application.eventhandling.PwMan3EventRegistry;
import de.pflugradts.pwman3.application.security.CryptoProviderFaker;
import de.pflugradts.pwman3.domain.model.password.PasswordEntryFaker;
import de.pflugradts.pwman3.domain.model.password.PasswordEntryRepositoryFaker;
import de.pflugradts.pwman3.domain.model.transfer.Bytes;
import de.pflugradts.pwman3.domain.service.password.encryption.CryptoProvider;
import de.pflugradts.pwman3.domain.service.password.storage.PasswordEntryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class RenamePasswordServiceTest {

    @Mock
    private CryptoProvider cryptoProvider;
    @Mock
    private PasswordEntryRepository passwordEntryRepository;
    @Mock
    private PwMan3EventRegistry pwMan3EventRegistry;
    @InjectMocks
    private RenamePasswordService passwordService;

    @Test
    void shouldRenamePasswordEntry() {
        // given
        final var oldKey = Bytes.of("key123");
        final var newKey = Bytes.of("keyABC");
        final var givenPasswordEntry = PasswordEntryFaker.faker()
            .fakePasswordEntry()
            .withKeyBytes(oldKey).fake();
        CryptoProviderFaker.faker()
            .forInstance(cryptoProvider)
            .withMockedEncryption().fake();
        PasswordEntryRepositoryFaker.faker()
            .forInstance(passwordEntryRepository)
            .withThesePasswordEntries(givenPasswordEntry).fake();

        // when
        final var actual = passwordService.renamePasswordEntry(oldKey, newKey);

        // then
        assertThat(actual).isNotNull();
        assertThat(actual.isSuccess()).isTrue();
        assertThat(givenPasswordEntry.viewKey())
            .isNotNull().isEqualTo(newKey).isNotEqualTo(oldKey);
    }

    @Test
    void shouldRenamePasswordEntry_DoNothingIfEntryDoesNotExist() {
        // given
        final var oldKey = Bytes.of("key123");
        final var newKey = Bytes.of("keyABC");
        final var givenPasswordEntry = PasswordEntryFaker.faker()
            .fakePasswordEntry()
            .withKeyBytes(oldKey).fake();
        final var existingPasswordEntry = PasswordEntryFaker.faker()
            .fakePasswordEntry().fake();
        CryptoProviderFaker.faker()
            .forInstance(cryptoProvider)
            .withMockedEncryption().fake();
        PasswordEntryRepositoryFaker.faker()
            .forInstance(passwordEntryRepository)
            .withThesePasswordEntries(existingPasswordEntry).fake();

        // when
        final var actual = passwordService.renamePasswordEntry(oldKey, newKey);

        // then
        assertThat(actual).isNotNull();
        assertThat(actual.isSuccess()).isTrue();
        assertThat(givenPasswordEntry.viewKey())
            .isNotNull().isEqualTo(oldKey).isNotEqualTo(newKey);
    }

}
