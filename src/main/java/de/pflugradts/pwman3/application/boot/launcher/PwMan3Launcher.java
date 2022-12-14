package de.pflugradts.pwman3.application.boot.launcher;

import com.google.inject.Inject;
import de.pflugradts.pwman3.application.UserInterfaceAdapterPort;
import de.pflugradts.pwman3.application.boot.Bootable;
import de.pflugradts.pwman3.application.boot.main.ApplicationModule;
import de.pflugradts.pwman3.application.boot.setup.SetupModule;
import de.pflugradts.pwman3.application.configuration.ReadableConfiguration;
import de.pflugradts.pwman3.application.license.LicenseManager;
import de.pflugradts.pwman3.application.util.GuiceInjector;
import de.pflugradts.pwman3.application.util.SystemOperation;
import de.pflugradts.pwman3.domain.model.transfer.Bytes;
import de.pflugradts.pwman3.domain.model.transfer.Output;
import java.io.File;
import static de.pflugradts.pwman3.application.configuration.ReadableConfiguration.KEYSTORE_FILENAME;

public class PwMan3Launcher implements Bootable {

    @Inject
    private ReadableConfiguration configuration;
    @Inject
    private UserInterfaceAdapterPort userInterfaceAdapterPort;
    @Inject
    private SystemOperation systemOperation;
    @Inject
    private LicenseManager licenseManager;

    @SuppressWarnings("PMD.ImmutableField")
    private GuiceInjector guiceInjector = new GuiceInjector();

    @Override
    public void boot() {
        sendLicenseNotice();
        sendBanner();
        if (configuration.getApplication().isVerifyLicenseFilesExist()) {
            licenseManager.verifyLicenseFilesExist();
        }
        guiceInjector.create(keystoreExists()
                ? new ApplicationModule()
                : new SetupModule()
        ).getInstance(Bootable.class).boot();
    }

    private boolean keystoreExists() {
        return getKeyStoreLocation().length() > 0
                && !systemOperation.getPath(getKeyStoreLocation())
                        .map(path -> path.resolve(KEYSTORE_FILENAME).toFile())
                        .filter(File::exists)
                        .isEmpty();
    }

    private void sendBanner() {
        userInterfaceAdapterPort.sendLineBreak();
        userInterfaceAdapterPort.send(Output.of(Bytes.of(banner())));
        userInterfaceAdapterPort.send(Output.of(Bytes.of("\t" + getClass().getPackage().getImplementationVersion())));
        userInterfaceAdapterPort.sendLineBreak();
    }

    private String getKeyStoreLocation() {
        return configuration.getAdapter().getKeyStore().getLocation();
    }

    private void sendLicenseNotice() {
        userInterfaceAdapterPort.sendLineBreak();
        userInterfaceAdapterPort.send(Output.of(Bytes.of(
                "\tCopyright 2020 - 2022 Christian Pflugradt")));
        userInterfaceAdapterPort.send(Output.of(Bytes.of(
                "\tThis software is licensed under the Apache License, Version 2.0 (APLv2)\n"
                        + "\tYou may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0")));
    }

    @SuppressWarnings("checkstyle:MagicNumber")
    private byte[] banner() {
        return new byte[]{0x9, 0x20, 0x5f, 0x5f, 0x5f, 0x5f, 0x5f, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20,
            0x20, 0x20, 0x5f, 0x5f, 0x20, 0x20, 0x5f, 0x5f, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20,
            0x20, 0x20, 0x20, 0x20, 0x5f, 0x5f, 0x5f, 0x5f, 0xa, 0x9, 0x7c, 0x20, 0x20, 0x5f, 0x5f, 0x20, 0x5c,
            0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x7c, 0x20, 0x20, 0x5c, 0x2f, 0x20, 0x20, 0x7c,
            0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x7c, 0x5f, 0x5f, 0x5f, 0x20, 0x5c,
            0xa, 0x9, 0x7c, 0x20, 0x7c, 0x5f, 0x5f, 0x29, 0x20, 0x7c, 0x5f, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20,
            0x5f, 0x7c, 0x20, 0x5c, 0x20, 0x20, 0x2f, 0x20, 0x7c, 0x20, 0x5f, 0x5f, 0x20, 0x5f, 0x20, 0x5f, 0x20,
            0x5f, 0x5f, 0x20, 0x20, 0x20, 0x5f, 0x5f, 0x29, 0x20, 0x7c, 0xa, 0x9, 0x7c, 0x20, 0x20, 0x5f, 0x5f,
            0x5f, 0x2f, 0x5c, 0x20, 0x5c, 0x20, 0x2f, 0x5c, 0x20, 0x2f, 0x20, 0x2f, 0x20, 0x7c, 0x5c, 0x2f, 0x7c,
            0x20, 0x7c, 0x2f, 0x20, 0x5f, 0x60, 0x20, 0x7c, 0x20, 0x27, 0x5f, 0x20, 0x5c, 0x20, 0x7c, 0x5f, 0x5f,
            0x20, 0x3c, 0xa, 0x9, 0x7c, 0x20, 0x7c, 0x20, 0x20, 0x20, 0x20, 0x20, 0x5c, 0x20, 0x56, 0x20, 0x20,
            0x56, 0x20, 0x2f, 0x7c, 0x20, 0x7c, 0x20, 0x20, 0x7c, 0x20, 0x7c, 0x20, 0x28, 0x5f, 0x7c, 0x20, 0x7c,
            0x20, 0x7c, 0x20, 0x7c, 0x20, 0x7c, 0x5f, 0x5f, 0x5f, 0x29, 0x20, 0x7c, 0xa, 0x9, 0x7c, 0x5f, 0x7c,
            0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x5c, 0x5f, 0x2f, 0x5c, 0x5f, 0x2f, 0x20, 0x7c, 0x5f, 0x7c, 0x20,
            0x20, 0x7c, 0x5f, 0x7c, 0x5c, 0x5f, 0x5f, 0x2c, 0x5f, 0x7c, 0x5f, 0x7c, 0x20, 0x7c, 0x5f, 0x7c, 0x5f,
            0x5f, 0x5f, 0x5f, 0x2f, 0xa};
    }

}
