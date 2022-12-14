package de.pflugradts.pwman3.domain.model.transfer;

import lombok.RequiredArgsConstructor;
import lombok.Value;

/**
 * An Output represents data sent to the user through the
 * {@link de.pflugradts.pwman3.application.UserInterfaceAdapterPort UserInterface}.
 */
@RequiredArgsConstructor(staticName = "of")
@Value
@SuppressWarnings("checkstyle:VisibilityModifier")
public class Output {

    Bytes bytes;

    public static Output empty() {
        return Output.of(Bytes.of(new byte[0]));
    }

    public static Output of(final byte... bytes) {
        return Output.of(Bytes.of(bytes));
    }

}
