package com.mycompany.myapp.repository;
import com.mycompany.myapp.domain.Scenario;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Scenario entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ScenarioRepository extends JpaRepository<Scenario, Long> {

}
