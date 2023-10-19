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
package com.paulorodrigues.authentication.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.paulo.rodrigues.librarybookstore.address.model.Country;
import com.paulo.rodrigues.librarybookstore.address.model.Address;
import com.paulo.rodrigues.librarybookstore.address.model.City;
import com.paulo.rodrigues.librarybookstore.book.model.Book;
import com.paulo.rodrigues.librarybookstore.utils.InvalidRequestException;
import com.paulo.rodrigues.librarybookstore.utils.ConstantsUtil;
import com.paulo.rodrigues.librarybookstore.utils.FormatUtils;
import com.paulo.rodrigues.librarybookstore.utils.MessageUtil;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author paulo.rodrigues
 */
@Entity
@Table(indexes = {    
    @Index(name = "idx_name_person", columnList = "name")
})
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Person implements Serializable {
    
    private static final long serialVersionUID = 1L;

    @SequenceGenerator(name = "SEQ_PERSON", allocationSize = 1, sequenceName = "person_id_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_PERSON")
    @Id
    private long id;

    @NotNull
    @Column(length = ConstantsUtil.MAX_SIZE_NAME)
    private String name;

    @Column(length = ConstantsUtil.MAX_SIZE_NAME)
    private String nickName;

    private LocalDate birthdate;
    
    @Column(length = 1)
    private String sex;

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
        
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date createAt;
    private String createBy;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date updateAt;
    private String updateBy;

    public void validation() throws InvalidRequestException {
        if (FormatUtils.isEmpty(name)) {
            throw new InvalidRequestException(MessageUtil.getMessage("PERSON_NAME_NOT_INFORMED"));
        }
        if (name.length() > ConstantsUtil.MAX_SIZE_NAME) {
            throw new InvalidRequestException(MessageUtil.getMessage("PERSON_NAME_OUT_OF_BOUND", ConstantsUtil.MAX_SIZE_NAME + ""));
        }                       
        if (!FormatUtils.isEmptyOrNull(sex) && (sex.length() > 1 || (!sex.equals("M") && !sex.equals("F") && !sex.equals("O") && !sex.equals("N")))) {
            throw new InvalidRequestException(MessageUtil.getMessage("PERSON_SEX_INVALID"));
        }
        if (!FormatUtils.isEmptyOrNull(description) && description.length() > ConstantsUtil.MAX_SIZE_LONG_TEXT) {
            throw new InvalidRequestException(MessageUtil.getMessage("PERSON_DESCRIPTION_OUT_OF_BOUND", ConstantsUtil.MAX_SIZE_LONG_TEXT + ""));
        }  
    }

    public void persistAt() {
        if (createBy == null) {
            setCreateAt(new Date());
            setCreateBy(FormatUtils.getUsernameLogged());
        } else {
            setUpdateAt(new Date());
            setUpdateBy(FormatUtils.getUsernameLogged());
        }
    }
}
