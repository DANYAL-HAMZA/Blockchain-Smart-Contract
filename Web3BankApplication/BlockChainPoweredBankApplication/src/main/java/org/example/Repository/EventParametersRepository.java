package org.example.Repository;

import org.example.Entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventParametersRepository extends JpaRepository<Event,String> {
}
