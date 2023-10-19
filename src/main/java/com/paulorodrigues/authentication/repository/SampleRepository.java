package com.paulorodrigues.sampledemo.test.sample.repository;

import com.paulorodrigues.sampledemo.test.sample.model.Sample;
import com.paulorodrigues.sampledemo.test.sample.model.SampleStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface SampleRepository extends JpaRepository<Sample, Long> {

    @Query("SELECT s "
            + " FROM Sample s "
            + " WHERE (:id IS NULL OR s.id = :id) "
            + " AND (:name IS NULL OR :name = '' OR LOWER(s.name) LIKE LOWER(CONCAT('%',:name,'%'))) "
            + " AND (:status IS NULL OR s.status = :status) "
            + " AND (:description IS NULL OR :description = '' OR s.description LIKE CONCAT('%',:description,'%')) "
            + " AND ((coalesce(:startDate, null) is null AND coalesce(:endDate, null) is null) OR (s.issueDate BETWEEN :startDate AND :endDate)) "
            + "")
    Page<Sample> findByFilterPageable(
            @Param("id") Long id,
            @Param("name") String name,
            @Param("status") SampleStatus status,
            @Param("description") String description,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            Pageable pageable);

    @Query("SELECT s "
            + " FROM Sample s "
            + " WHERE (:name IS NULL OR :name = '' OR LOWER(s.name) LIKE LOWER(CONCAT('%',:name,'%'))) "
            + "")
    List<Sample> findByName(String name);
}
