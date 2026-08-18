package com.enterprise.portal.repository;

import com.enterprise.portal.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findByUsername(String username);
    List<AppUser> findByRoleIn(Collection<Role> roles);
}
