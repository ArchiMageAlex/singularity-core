package com.nfcs.singularity.core.repos;

import com.nfcs.singularity.core.domain.BaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface BaseRepo<T extends BaseEntity, ID> extends JpaRepository<T, ID> {}