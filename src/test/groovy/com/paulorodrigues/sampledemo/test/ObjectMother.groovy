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
package com.paulorodrigues.sampledemo.test

import com.paulorodrigues.sampledemo.test.sample.model.SampleStatus
import com.paulorodrigues.sampledemo.test.util.ConstantsUtil
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import spock.lang.Specification

import java.time.LocalDate

class ObjectMother extends Specification {

    /*
    Generic
    */
    static def nameForTest = "GroovySpockTest_gfkgjoemf48"

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
    Sample
    */
    static buildSampleFilter (props = null) {
        applyProperties(props, new SampleFilter(
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

    static buildSamples (props = null) {
        applyProperties(props, Arrays.asList(buildSample()))
    }

    static buildSamplesPage (props = null) {
        applyProperties(props, new PageImpl<>(buildSamples(), buildPageable (), 1l))
    }

    static buildSample (props = null) {
        applyProperties(props, new Sample(
                id: genericId,
                name: nameForTest,
                status: SampleStatus.NEW,
                issueDate: buildPastDate(),
                description: buildRandomString(ConstantsUtil.MAX_SIZE_LONG_TEXT)
        ))
    }

    static buildSamplesDTO (props = null) {
        applyProperties(props, Arrays.asList(buildSampleDTO()))
    }

    static buildSampleDTO (props = null) {
        applyProperties(props, new SampleDTO(
                id: genericId,
                name: nameForTest,
                status: SampleStatus.NEW,
                issueDate: buildPastDate(),
                description: buildRandomString(ConstantsUtil.MAX_SIZE_LONG_TEXT)
        ))
    }

   }
