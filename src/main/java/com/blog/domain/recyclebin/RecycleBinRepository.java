package com.blog.domain.recyclebin;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecycleBinRepository extends JpaRepository<RecycleBin, Long> {

}

