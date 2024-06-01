package com.example.jp.repositories.Presentations;

import com.example.jp.model.Presentations.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoriesRepository extends JpaRepository<Category, Long> {
    boolean existsByName(String name);
    @Query("SELECT c FROM Category c WHERE c.presentations IS NOT EMPTY")
    List<Category> findAllWithPresentations();
}