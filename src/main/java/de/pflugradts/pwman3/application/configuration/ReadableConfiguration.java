package de.pflugradts.pwman3.application.configuration;

import de.pflugradts.pwman3.domain.model.password.PasswordRequirements;

public interface ReadableConfiguration {

    String CONFIGURATION_SYSTEM_PROPERTY = "config";
    String CONFIGURATION_FILENAME = "PwMan3.yml";
    String KEYSTORE_FILENAME = "PwMan3.ks";
    String DATABASE_FILENAME = "PwMan3.pw";
    String EXCHANGE_FILENAME = "PwMan3.json";

    PasswordRequirements parsePasswordRequirements();
    boolean isTemplate();
    Application getApplication();
    Adapter getAdapter();

    interface Application {
        boolean isVerifyLicenseFilesExist();
        Password getPassword();
    }

    interface Password {
        int getLength();
        boolean isSpecialCharacters();
        boolean isPromptOnRemoval();
    }

    interface Adapter {
        Clipboard getClipboard();
        KeyStore getKeyStore();
        PasswordStore getPasswordStore();
        UserInterface getUserInterface();
    }

    interface Clipboard {
        ClipboardReset getReset();
    }

    interface ClipboardReset {
        boolean isEnabled();
        int getDelaySeconds();
    }

    interface PasswordStore {
        String getLocation();
        boolean isVerifySignature();
        boolean isVerifyChecksum();
    }

    interface KeyStore {
        String getLocation();
    }

    interface UserInterface {
        boolean isSecureInput();
    }

}
