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
 * @author paulo.rodrigues
 */
package com.paulorodrigues.authentication.test

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.paulorodrigues.authentication.AuthenticationServiceApplication
import groovyx.net.http.RESTClient
import org.apache.http.HttpResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestPropertySource
import spock.lang.Shared
import spock.lang.Specification

import static groovyx.net.http.ContentType.JSON

@ActiveProfiles(["Test"])
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
@TestPropertySource(        
        properties = [                
                "server.ssl.enabled: false",
                "security.basic.enabled: false",
                "security.ignored: /**"                
        ]
)
@SpringBootTest(classes = AuthenticationServiceApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
abstract class AbstractAuthenticationServiceSpecification extends Specification {
	
    @LocalServerPort
    int port

    @Autowired
    ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule())

    @Shared
    def client

    @Shared
    def userId = 99999

    def setup() {
        def username = "client"
        def password = "teste"
        client = new RESTClient("http://localhost:${port}")        
        client.headers['Authorization'] = "Basic ${"$username:$password".bytes.encodeBase64()}"
        objectMapper = new ObjectMapper().registerModule(new JavaTimeModule())
    }

    def createItemOnDb(pathAPI, item) {
        postRestCall(pathAPI, item)
    }

    def getIdCreatedFromTest(pathAPI, nameToSearch) {
        def response = client.get(path : pathAPI + "/fetch/" + nameToSearch)
        return response.responseData.size > 0 ? response.responseData[0].id : null
    }

    HttpResponse deleteByIdRestCall(pathAPI, idToDelete) {
        client.delete(path : pathAPI + "/" + idToDelete)
    }

    HttpResponse getByNameRestCall (pathAPI, nameToSearch){
        return client.get(path : pathAPI + "/fetch/" + nameToSearch)
    }

    HttpResponse getByIdRestCall (pathAPI, id){
        return client.get(path : pathAPI + "/" + id)
    }

    HttpResponse getRestCall(pathAPI){
        return client.get(path : pathAPI)
    }

    HttpResponse postRestCall (pathAPI, item){
        return client.post(path : pathAPI,
                requestContentType : JSON,
                body : objectMapper.writeValueAsString(item)
        )
    }

    HttpResponse putWithIdRestCall(pathAPI, id, item){
        return client.put(path : pathAPI + "/" + id,
                requestContentType : JSON,
                body : objectMapper.writeValueAsString(item)
        )
    }

}

