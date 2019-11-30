package com.mycompany.myapp.repository;
import com.mycompany.myapp.domain.Churn;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Churn entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ChurnRepository extends JpaRepository<Churn, Long> {

}
