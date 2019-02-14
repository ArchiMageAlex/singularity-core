package com.nfcs.singularity.core.repos;

import com.nfcs.singularity.core.domain.BaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

@NoRepositoryBean
public interface BaseRepo<T extends BaseEntity, ID extends Serializable> extends JpaRepository<T, ID> {
}
