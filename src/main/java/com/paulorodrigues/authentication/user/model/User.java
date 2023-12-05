/*
 * Copyright (C) 2023 paulo.rodrigues
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
package com.paulorodrigues.authentication.user.model;

import com.paulorodrigues.authentication.address.model.Address;
import com.paulorodrigues.authentication.exception.InvalidRequestException;
import com.paulorodrigues.authentication.util.ConstantsUtil;
import com.paulorodrigues.authentication.util.FormatUtil;
import com.paulorodrigues.authentication.util.MessageUtil;
import groovyjarjarantlr4.v4.runtime.misc.NotNull;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
/**
 *
 * @author paulo.rodrigues
 */
@Entity
@Data
@Builder
@AllArgsConstructor
@Table(name="lbs_user", uniqueConstraints = {
        @UniqueConstraint(name = "unique_cpf", columnNames = "cpf"),
        @UniqueConstraint(name = "unique_username", columnNames = "username"),
        @UniqueConstraint(name = "unique_email", columnNames = "email")})
public class User {
    
    @Id
    @SequenceGenerator(name = "SEQ_USER", allocationSize = 1, sequenceName = "user_id_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_USER")
    private Long id;
        
    @Column(length = ConstantsUtil.MAX_SIZE_NAME)
    private String name;
   
    @NotNull
    @Column(unique = true, length = ConstantsUtil.MAX_SIZE_NAME)
    private String username;
    
    @Column(unique = true, length = ConstantsUtil.MAX_SIZE_CPF)
    private String cpf;
    
    @Column(unique = true, length = ConstantsUtil.MAX_SIZE_NAME)
    private String email;
    
//    @JsonIgnore
    @Column(length = ConstantsUtil.MAX_SIZE_NAME)
    private String password;
    
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "ADDRESS_ID", referencedColumnName = "ID", foreignKey = @ForeignKey(name = "ADDRESS_USER"))
    private Address address;
    
    private LocalDate birthdate;
    
    @Column(length = 1)
    private String sex;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name="user_role",
            joinColumns=@JoinColumn(name="user_id"),
            inverseJoinColumns=@JoinColumn(name="role_id")
    )
    private List<Role> roles;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date createAt;
    private String createBy;
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateAt;
    private String updateBy;

    public User() {
    }

    public User(String name, String email) {
        super();
        this.name = name;
        this.email = email;
    }
    public User(User user) {
        super();
        this.name = user.getName();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.roles = user.getRoles();
        this.id = user.getId();
    }
    public User(String name, String email, String password, List<Role> roles) {
        super();
        this.name = name;
        this.email = email;
        this.roles = roles;
        this.password = password;
    }
    

    public void validation() throws InvalidRequestException {

        if (!FormatUtil.isEmptyOrNull(name) && name.length() > ConstantsUtil.MAX_SIZE_NAME) {
            throw new InvalidRequestException(MessageUtil.getMessage("USER_NAME_OUT_OF_BOUND", ConstantsUtil.MAX_SIZE_NAME + ""));
        }
        if (FormatUtil.isEmptyOrNull(username)) {
            throw new InvalidRequestException(MessageUtil.getMessage("USERNAME_NOT_INFORMED"));
        }
        if (!FormatUtil.isEmptyOrNull(username) && username.length() > ConstantsUtil.MAX_SIZE_NAME) {
            throw new InvalidRequestException(MessageUtil.getMessage("USERNAME_OUT_OF_BOUND", ConstantsUtil.MAX_SIZE_NAME + ""));
        }
        if (FormatUtil.isEmptyOrNull(password)) {
            throw new InvalidRequestException(MessageUtil.getMessage("USER_PASSWORD_NOT_INFORMED"));
        }
        if (!FormatUtil.isEmptyOrNull(password) && password.length() > ConstantsUtil.MAX_SIZE_NAME) {
            throw new InvalidRequestException(MessageUtil.getMessage("USER_PASSWORD_OUT_OF_BOUND", ConstantsUtil.MAX_SIZE_NAME + ""));
        }
        if (!FormatUtil.isEmptyOrNull(sex) && (sex.length() > 1 || (!sex.equals("M") && !sex.equals("F") && !sex.equals("O") && !sex.equals("N")))) {
            throw new InvalidRequestException(MessageUtil.getMessage("USER_SEX_INVALID"));
        }
        if (!FormatUtil.isEmptyOrNull(email) && email.length() > ConstantsUtil.MAX_SIZE_NAME) {
            throw new InvalidRequestException(MessageUtil.getMessage("USER_EMAIL_OUT_OF_BOUND", ConstantsUtil.MAX_SIZE_NAME + ""));
        }
        String nuCpf = FormatUtil.removeFormatCPF(cpf);
        if (nuCpf != null && !nuCpf.isEmpty()) {
            if( !FormatUtil.isCPF(nuCpf)) {
                throw new InvalidRequestException(MessageUtil.getMessage("USER_CPF_INVALID"));
            }
            cpf = nuCpf;
        }
    }

    public void persistAt() {
        if (createAt == null) {
            setCreateAt(new Date());
            setCreateBy(FormatUtil.getUsernameLogged());
        } else {
            setUpdateAt(new Date());
            setUpdateBy(FormatUtil.getUsernameLogged());
        }
        
    }
}
