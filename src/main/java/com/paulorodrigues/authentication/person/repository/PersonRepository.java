package com.paulorodrigues.authentication.person.repository;


import com.paulorodrigues.authentication.person.model.Person;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {

    @Query("SELECT s "
            + " FROM Person s "
            + " WHERE (:id IS NULL OR s.id = :id) "
            + " AND (:name IS NULL OR :name = '' OR LOWER(s.firstName) LIKE LOWER(CONCAT('%',:name,'%'))) "
            + " AND (:description IS NULL OR :description = '' OR s.description LIKE CONCAT('%',:description,'%')) "
            + " AND ((coalesce(:startDate, null) is null AND coalesce(:endDate, null) is null) OR (s.birthdate BETWEEN :startDate AND :endDate)) "
            + "")
    Page<Person> findByFilterPageable(
            @Param("id") Long id,
            @Param("name") String name,
            @Param("description") String description,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            Pageable pageable);

    @Query("SELECT s "
            + " FROM Person s "
            + " WHERE (:name IS NULL OR :name = '' OR LOWER(s.firstName) LIKE LOWER(CONCAT('%',:name,'%'))) "
            + "")
    Page<Person> findByName(@Param("name") String name,
                            Pageable pageable);
}
