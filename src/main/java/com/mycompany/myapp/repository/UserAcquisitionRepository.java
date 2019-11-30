package com.mycompany.myapp.repository;
import com.mycompany.myapp.domain.UserAcquisition;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the UserAcquisition entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserAcquisitionRepository extends JpaRepository<UserAcquisition, Long> {

}
