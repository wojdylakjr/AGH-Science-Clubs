package com.project.app.repository;

import com.project.app.domain.ExtraUser;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ExtraUser entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ExtraUserRepository extends JpaRepository<ExtraUser, Long> {
    Optional<ExtraUser> findOneById(Long id);
}
