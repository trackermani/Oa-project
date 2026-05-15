package com.seanergy.oa.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.seanergy.oa.domain.ManagedCompany;

public interface ManagedCompanyRepository extends JpaRepository<ManagedCompany, Integer> {

    Optional<ManagedCompany> findByManagedCompanyIdAndIsActivatedTrue(Integer managedCompanyId);
}
