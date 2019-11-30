package com.mycompany.myapp.repository;
import com.mycompany.myapp.domain.Cohort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Cohort entity.
 */
@Repository
public interface CohortRepository extends JpaRepository<Cohort, Long> {

    @Query(value = "select distinct cohort from Cohort cohort left join fetch cohort.scenarios",
        countQuery = "select count(distinct cohort) from Cohort cohort")
    Page<Cohort> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct cohort from Cohort cohort left join fetch cohort.scenarios")
    List<Cohort> findAllWithEagerRelationships();

    @Query("select cohort from Cohort cohort left join fetch cohort.scenarios where cohort.id =:id")
    Optional<Cohort> findOneWithEagerRelationships(@Param("id") Long id);

}
