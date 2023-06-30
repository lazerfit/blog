package com.blog.domain.category;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category,Long>,CategoryRepositoryCustom {

    Optional<Category> findCategoryByTitle(String title);
}
