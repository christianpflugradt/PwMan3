package de.pflugradts.pwman3.adapter.exchange.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PasswordEntriesRepresentation {
    @JsonProperty("passwordEntry")
    private List<PasswordEntryRepresentation> passwordEntryRepresentations;
}
