/*
 * Copyright (C) 2022 paulo.rodrigues
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

/**
 *
 * @sample paulo.rodrigues
 */
package com.paulorodrigues.authentication.test

import com.paulorodrigues.authentication.address.model.*
import com.paulorodrigues.authentication.person.model.Person
import com.paulorodrigues.authentication.person.model.PersonDTO
import com.paulorodrigues.authentication.person.model.PersonQuery
import com.paulorodrigues.authentication.person.model.Sex
import com.paulorodrigues.authentication.util.ConstantsUtil
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import spock.lang.Specification

import java.time.LocalDate

class ObjectMother extends Specification {

    /*
    Generic
    */
    static def nameForTest = "GroovySpockTest_gfkgjoemf48"
    static def testEmailAddress = "test@test.com"

    static <T> T applyProperties(props, T object) {
        if (props) {
            props.each { k, v -> object[k] = v }
        }
        object
    }

    static buildRandomString ( length){
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = length;
        Random random = new Random();

        return random.ints(leftLimit, rightLimit + 1)
                .limit(targetStringLength)

                .collect({ -> new StringBuilder() },
                        { sb, codePoint -> sb.append((char) codePoint) },
                        { sb1, sb2 -> sb1.append(sb2.toString()) })
                .toString()
    }

    static buildPastDate () {
        return LocalDate.parse("1999-12-30");
    }

    static buildForStartDate () {
        return LocalDate.parse("1999-12-29");
    }

    static buildForFinalDate () {
        return LocalDate.parse("1999-12-31");
    }

    static buildPageable () {
        return PageRequest.of(1, 1);
    }

    static genericId = 99

    /*
     Address
     */
    static buildCountry (props = null) {
        applyProperties(props, new Country(
                id: 2,
                name: "USA"
        ))
    }

    static buildCountryDTO (props = null) {
        applyProperties(props, new CountryDTO(
                id: 2,
                name: "USA"
        ))
    }

    static buildState (props = null) {
        applyProperties(props, new StateCountry(
                id: 3,
                name: "California",
                country: buildCountry()
        ))
    }

    static buildStateDTO (props = null) {
        applyProperties(props, new StateDTO(
                id: 3,
                name: "California"
        ))
    }

    static buildCity (props = null) {
        applyProperties(props, new City(
                id: 6,
                name: "San Francisco",
                state: buildState(),
                country: buildCountry()
        ))
    }

    static buildCityDTO (props = null) {
        applyProperties(props, new CityDTO(
                id: 6,
                name: "San Francisco"
        ))
    }

    static buildAddress (props = null) {
        applyProperties(props, new Address(
                id: genericId,
                logradouro: TipoLogradouro.AVENUE,
                name: nameForTest,
                number: "123",
                neighborhood: "Downtown",
                cep: "123-123",
                zipCode: "123-123",
                city: buildCity(),
                coordination: buildRandomString(ConstantsUtil.MAX_SIZE_ADDRESS_COORDINATION),
                referentialPoint: buildRandomString(ConstantsUtil.MAX_SIZE_SHORT_TEXT)
        ))
    }

    static buildAddressDTO (props = null) {
        applyProperties(props, new AddressDTO(
                id: genericId,
                logradouro: TipoLogradouro.AVENUE,
                name: nameForTest,
                number: "321",
                neighborhood: "Downtown",
                cep: "321-321",
                zipCode: "321-321",
                city: buildCityDTO()
        ))
    }

    /*
    Person
    */
    static buildPersonQuery (props = null) {
        applyProperties(props, new PersonQuery(
                currentPage: 1,
                rowsPerPage: 10,
                sortColumn: 'name',
                sort: 'asc',
                offset: 0,
                id: null,
                name: nameForTest,
                startDate: buildForStartDate(),
                endDate: buildForFinalDate(),
        ))
    }

    static buildPeople (props = null) {
        applyProperties(props, Arrays.asList(buildPerson()))
    }

    static buildPeoplePage (props = null) {
        applyProperties(props, new PageImpl<>(buildPeople(), buildPageable (), 1l))
    }

    static buildPerson (props = null) {
        applyProperties(props, new Person(
                id: genericId,
                firstName: nameForTest,
                lastName: nameForTest,
                birthdate: buildPastDate(),
                sex: Sex.M,
                email: testEmailAddress,
                birthCity: buildCity(),
                birthCountry: buildCountry(),
                address: buildAddress(),
                description: buildRandomString(ConstantsUtil.MAX_SIZE_LONG_TEXT),
                createAt: buildPastDate(),
                createBy: testEmailAddress,
                updateAt: buildPastDate(),
                updateBy: testEmailAddress
        ))
    }

    static buildPeoplesDTO (props = null) {
        applyProperties(props, Arrays.asList(buildPersonDTO()))
    }

    static buildPersonDTO(props = null) {
        applyProperties(props, new PersonDTO(
                id: genericId,
                firstName: nameForTest,
                lastName: nameForTest,
                birthdate: buildPastDate(),
                sex: Sex.M,
                email: testEmailAddress,
                birthCity: buildCityDTO(),
                birthCountry: buildCountryDTO(),
                address: buildAddressDTO(),
                description: buildRandomString(ConstantsUtil.MAX_SIZE_LONG_TEXT),
                createAt: buildPastDate(),
                createBy: testEmailAddress,
                updateAt: buildPastDate(),
                updateBy: testEmailAddress
        ))
    }
}
