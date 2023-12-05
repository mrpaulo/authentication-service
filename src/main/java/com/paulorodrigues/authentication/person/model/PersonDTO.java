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

import com.paulorodrigues.authentication.address.model.AddressDTO;
import com.paulorodrigues.authentication.address.model.CityDTO;
import com.paulorodrigues.authentication.address.model.CountryDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 *
 * @author paulo.rodrigues
 */

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class PersonDTO  {

    private long id;
    private String firstName;
    private String lastName;
    private String nickName;
    private LocalDate birthdate;
    private Sex sex;
    private String email;
    private CityDTO birthCity;
    private CountryDTO birthCountry;
    private AddressDTO address;
    private String description;

    private LocalDate createAt;
    private String createBy;
    private LocalDate updateAt;
    private String updateBy;

    public String getName(){
        return firstName + " " + lastName;
    }

}
