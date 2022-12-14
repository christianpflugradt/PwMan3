package de.pflugradts.pwman3.domain.service.eventhandling;

import de.pflugradts.pwman3.domain.model.ddd.AggregateRoot;
import de.pflugradts.pwman3.domain.model.ddd.DomainEvent;

public interface EventRegistry {

    void register(AggregateRoot aggregateRoot);

    void register(DomainEvent domainEvent);

    void deregister(AggregateRoot aggregateRoot);

    void processEvents();

}
