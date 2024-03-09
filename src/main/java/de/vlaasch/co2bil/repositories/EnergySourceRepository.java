package de.vlaasch.co2bil.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import de.vlaasch.co2bil.entities.EnergySource;

@Repository
public interface EnergySourceRepository extends JpaRepository<EnergySource, Long> {

}
