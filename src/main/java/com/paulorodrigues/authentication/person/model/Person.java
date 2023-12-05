/*
 * Copyright (C) 2023 paulo.rodrigues
 * Profile: <https://github.com/mrpaulo>
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
package com.paulorodrigues.authentication.person.model;

import com.paulorodrigues.authentication.address.model.Address;
import com.paulorodrigues.authentication.address.model.City;
import com.paulorodrigues.authentication.address.model.Country;
import com.paulorodrigues.authentication.exception.InvalidRequestException;
import com.paulorodrigues.authentication.util.ConstantsUtil;
import com.paulorodrigues.authentication.util.FormatUtil;
import com.paulorodrigues.authentication.util.MessageUtil;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

/**
 *
 * @author paulo.rodrigues
 */
@Entity
@Table(indexes = {    
    @Index(name = "idx_first_name_person", columnList = "firstName")
})
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Person implements Serializable {
    
    @Serial
    private static final long serialVersionUID = 1L;

    @SequenceGenerator(name = "SEQ_PERSON", allocationSize = 1, sequenceName = "person_id_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_PERSON")
    @Id
    private long id;

    @Column(length = ConstantsUtil.MAX_SIZE_NAME, nullable = false)
    private String firstName;

    @Column(length = ConstantsUtil.MAX_SIZE_NAME, nullable = false)
    private String lastName;

    @Column(length = ConstantsUtil.MAX_SIZE_NAME)
    private String nickName;

    private LocalDate birthdate;

    @Enumerated(EnumType.STRING)
    private Sex sex;

    @Column(length = ConstantsUtil.MAX_SIZE_NAME)
    private String email;

    @ManyToOne
    @JoinColumn(name = "BIRTH_CITY_ID", referencedColumnName = "ID", foreignKey = @ForeignKey(name = "BIRTH_CITY_PERSON"))
    private City birthCity;

    @ManyToOne
    @JoinColumn(name = "BIRTH_COUNTRY_ID", referencedColumnName = "ID", foreignKey = @ForeignKey(name = "BIRTH_COUNTRY_PERSON"))
    private Country birthCountry;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "ADDRESS_ID", referencedColumnName = "ID", foreignKey = @ForeignKey(name = "ADDRESS_PERSON"))
    private Address address;
    
    @Column(length = ConstantsUtil.MAX_SIZE_LONG_TEXT)
    private String description;
        
    private LocalDate createAt;
    private String createBy;
    private LocalDate updateAt;
    private String updateBy;

    public void validation() throws InvalidRequestException {
        if (FormatUtil.isEmpty(firstName)) {
            throw new InvalidRequestException(MessageUtil.getMessage("PERSON_NAME_NOT_INFORMED"));
        }
        if (FormatUtil.isEmpty(lastName)) {
            throw new InvalidRequestException(MessageUtil.getMessage("PERSON_NAME_NOT_INFORMED"));
        }
        if (firstName.length() > ConstantsUtil.MAX_SIZE_NAME || lastName.length() > ConstantsUtil.MAX_SIZE_NAME) {
            throw new InvalidRequestException(MessageUtil.getMessage("PERSON_NAME_OUT_OF_BOUND", ConstantsUtil.MAX_SIZE_NAME + ""));
        }
        if (!FormatUtil.isEmptyOrNull(description) && description.length() > ConstantsUtil.MAX_SIZE_LONG_TEXT) {
            throw new InvalidRequestException(MessageUtil.getMessage("PERSON_DESCRIPTION_OUT_OF_BOUND", ConstantsUtil.MAX_SIZE_LONG_TEXT + ""));
        }  
    }

    public String getName(){
        return firstName + " " + lastName;
    }

    public PersonDTO toDTO(){
        return PersonDTO.builder()
                .id(id)
                .firstName(firstName)
                .lastName(lastName)
                .sex(sex)
                .email(email)
                .address(address.toDTO())
                .birthdate(birthdate)
                .birthCity(birthCity.toDTO())
                .birthCountry(birthCountry.toDTO())

                .build();
    }

    public void persistAt() {
        if (createBy == null) {
            setCreateAt(LocalDate.now());
            setCreateBy(FormatUtil.getUsernameLogged());
        } else {
            setUpdateAt(LocalDate.now());
            setUpdateBy(FormatUtil.getUsernameLogged());
        }
    }
}
