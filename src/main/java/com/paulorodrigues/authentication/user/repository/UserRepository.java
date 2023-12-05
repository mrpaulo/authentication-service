/*
 * Copyright (C) 2021 paulo.rodrigues
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.paulorodrigues.authentication.user.repository;

import com.paulorodrigues.authentication.user.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author paulo.rodrigues
 */
public interface UserRepository extends JpaRepository<User, Long> {
    
    public User findByEmail(String email);
    
    @Query("SELECT c "
            + " FROM User c "
            + " WHERE (:name IS NULL OR :name = '' OR LOWER(c.name) LIKE LOWER(CONCAT('%',:name,'%'))) "
            + "")
    public List<User> findByName(String name);
    
    @Query("SELECT c "
            + " FROM User c "
            + " WHERE (:id IS NULL OR c.id = :id) "
            + " AND (:name IS NULL OR :name = '' OR LOWER(c.name) LIKE LOWER(CONCAT('%',:name,'%'))) "
            + " AND (:cpf IS NULL OR :cpf = '' OR c.cpf LIKE CONCAT('%',:cpf,'%')) "
            + " AND ((coalesce(:startDate, null) is null AND coalesce(:finalDate, null) is null) OR (c.birthdate BETWEEN :startDate AND :finalDate)) "
            + "")
    public Page<User> findPageable(
            @Param("id") Long id,
            @Param("name") String name,
            @Param("cpf") String cpf,                        
            @Param("startDate") LocalDate startDate,
            @Param("finalDate") LocalDate finalDate,
            Pageable page);   

    public User findByUsername(String username);

    @Modifying
    @Query(value = "UPDATE "
            + " lbs_user "
            + " SET  address_id = null"
            + " WHERE address_id = :addressId ",
            nativeQuery = true)
    public void deleteAddressReference(Long addressId);

}