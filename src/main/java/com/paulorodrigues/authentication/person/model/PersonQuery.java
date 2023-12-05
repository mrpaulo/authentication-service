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

import com.paulorodrigues.authentication.address.model.CityDTO;
import com.paulorodrigues.authentication.address.model.CountryDTO;
import com.paulorodrigues.authentication.util.PageableQuery;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author paulo.rodrigues
 */

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class PersonQuery extends PageableQuery {

    private String firstName;
    private String lastName;
    private String nickName;
    private String sex;
    private String email;
    private CityDTO birthCity;
    private CountryDTO birthCountry;
    private String description;
}
