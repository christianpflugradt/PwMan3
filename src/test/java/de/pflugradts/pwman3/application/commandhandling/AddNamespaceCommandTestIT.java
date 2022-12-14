package de.pflugradts.pwman3.application.commandhandling;

import de.pflugradts.pwman3.application.UserInterfaceAdapterPort;
import de.pflugradts.pwman3.application.UserInterfaceAdapterPortFaker;
import de.pflugradts.pwman3.application.commandhandling.handler.namespace.AddNamespaceCommandHandler;
import de.pflugradts.pwman3.application.failurehandling.FailureCollector;
import de.pflugradts.pwman3.domain.model.namespace.Namespace;
import de.pflugradts.pwman3.domain.model.namespace.NamespaceSlot;
import de.pflugradts.pwman3.domain.model.transfer.Bytes;
import de.pflugradts.pwman3.domain.model.transfer.Input;
import de.pflugradts.pwman3.domain.model.transfer.Output;
import de.pflugradts.pwman3.domain.service.NamespaceServiceFake;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static de.pflugradts.pwman3.application.commandhandling.InputHandlerTestFactory.setupInputHandlerFor;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
class AddNamespaceCommandTestIT {

    private InputHandler inputHandler;
    @Spy
    private final NamespaceServiceFake namespaceServiceFake = new NamespaceServiceFake();
    @Mock
    private UserInterfaceAdapterPort userInterfaceAdapterPort;
    @Mock
    private FailureCollector failureCollector;
    @InjectMocks
    private AddNamespaceCommandHandler addNamespaceCommandHandler;

    @Captor
    private ArgumentCaptor<Output> captor;

    @BeforeEach
    private void setup() {
        inputHandler = setupInputHandlerFor(addNamespaceCommandHandler);
    }

    @Test
    void shouldHandleAddNamespaceCommand() {
        // given
        final var slotIndex = 1;
        final var givenInput = Bytes.of("n+" + slotIndex);
        final var slotFromInput = NamespaceSlot.at(slotIndex);
        final var referenceNamespace = Bytes.of("mynamespace");
        final var givenNamespace = Bytes.of("mynamespace");
        UserInterfaceAdapterPortFaker.faker()
            .forInstance(userInterfaceAdapterPort)
            .withTheseInputs(Input.of(givenNamespace)).fake();

        // when
        inputHandler.handleInput(Input.of(givenInput));

        // then
        then(userInterfaceAdapterPort).should(never()).send(any());
        assertNamespaceEquals(namespaceServiceFake.atSlot(slotFromInput), referenceNamespace);
        assertThat(givenNamespace).isNotNull().isNotEqualTo(referenceNamespace);
    }

    @Test
    void shouldHandleAddNamespaceCommand_UpdateExistingNamespace() {
        // given
        final var slotIndex = 1;
        final var input = Input.of(Bytes.of("n+" + slotIndex));
        final var slotFromInput = NamespaceSlot.at(slotIndex);
        final var referenceNamespace = Bytes.of("mynamespace");
        final var givenNamespace = Bytes.of("mynamespace");
        final var otherNamespace = Bytes.of("othernamespace");
        UserInterfaceAdapterPortFaker.faker()
            .forInstance(userInterfaceAdapterPort)
            .withTheseInputs(Input.of(givenNamespace)).fake();

        namespaceServiceFake.deploy(otherNamespace, slotFromInput);
        assertNamespaceEquals(namespaceServiceFake.atSlot(slotFromInput), otherNamespace);

        // when
        inputHandler.handleInput(input);

        // then
        then(userInterfaceAdapterPort).should(never()).send(any());
        assertNamespaceEquals(namespaceServiceFake.atSlot(slotFromInput), referenceNamespace);
        assertThat(givenNamespace).isNotNull().isNotEqualTo(referenceNamespace);
    }

    @Test
    void shouldHandleAddNamespaceCommand_EmptyInput() {
        // given
        final var slotIndex = 1;
        final var input = Input.of(Bytes.of("n+" + slotIndex));
        final var slotFromInput = NamespaceSlot.at(slotIndex);
        final var givenNamespace = Bytes.of("");
        UserInterfaceAdapterPortFaker.faker()
            .forInstance(userInterfaceAdapterPort)
            .withTheseInputs(Input.of(givenNamespace)).fake();

        // when
        inputHandler.handleInput(input);

        // then
        then(userInterfaceAdapterPort).should().send(captor.capture());
        assertThat(captor.getValue()).isNotNull()
            .extracting(Output::getBytes).isNotNull()
            .extracting(Bytes::asString).isNotNull()
            .asString().contains("Empty input");
        assertThat(namespaceServiceFake.atSlot(slotFromInput)).isNotPresent();
    }

    @Test
    void shouldHandleAddNamespaceCommand_DefaultSlot() {
        // given
        final var slotIndex = 0;
        final var input = Input.of(Bytes.of("n+" + slotIndex));
        final var slotFromInput = NamespaceSlot.at(slotIndex);

        // when
        inputHandler.handleInput(input);

        // then
        then(userInterfaceAdapterPort).should(never()).receive(any());
        then(userInterfaceAdapterPort).should().send(captor.capture());
        assertThat(captor.getValue()).isNotNull()
            .extracting(Output::getBytes).isNotNull()
            .extracting(Bytes::asString).isNotNull()
            .asString().contains("Default namespace cannot be replaced");
    }

    private static void assertNamespaceEquals(final Optional<Namespace> deployedNamespace, final Bytes expectedNamespaceBytes) {
        assertThat(deployedNamespace)
            .isNotEmpty().get()
            .extracting(Namespace::getBytes).isNotNull()
            .isEqualTo(expectedNamespaceBytes);
    }


}
