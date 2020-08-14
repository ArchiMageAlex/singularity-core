package com.nfcs.singularity.core.repos;

import com.nfcs.singularity.core.domain.News;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsRepo extends BaseRepo<News, Long> {
}
