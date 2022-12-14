package de.pflugradts.pwman3.application.commandhandling;

import de.pflugradts.pwman3.application.UserInterfaceAdapterPort;
import de.pflugradts.pwman3.application.commandhandling.handler.ExportCommandHandler;
import de.pflugradts.pwman3.application.exchange.ImportExportService;
import de.pflugradts.pwman3.domain.model.transfer.Bytes;
import de.pflugradts.pwman3.domain.model.transfer.Input;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static de.pflugradts.pwman3.application.commandhandling.InputHandlerTestFactory.setupInputHandlerFor;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class ExportCommandTestIT {

    private CommandBus commandBus;
    private InputHandler inputHandler;

    @Mock
    private ImportExportService importExportService;
    @Mock
    private UserInterfaceAdapterPort userInterfaceAdapterPort;
    @InjectMocks
    private ExportCommandHandler exportCommandHandler;

    @BeforeEach
    private void setup() {
        inputHandler = setupInputHandlerFor(exportCommandHandler);
    }

    @Test
    void shouldHandleExportCommand() {
        // given
        final var args = "tmp";
        final var bytes = Bytes.of("e" + args);
        final var reference = bytes.copy();

        // when
        assertThat(bytes).isEqualTo(reference);
        inputHandler.handleInput(Input.of(bytes));

        // then
        then(importExportService).should().exportPasswordEntries(args);
        assertThat(bytes).isNotEqualTo(reference);
    }

}
