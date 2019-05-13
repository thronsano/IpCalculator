package com.ipCalculator.entity.db;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.Ordering;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.EAGER;

@Entity
@Table(name = "users")
public class User {

    @Id
    @NotBlank
    @Size(max = 100)
    @Email
    @Column
    private String email;

    @NotBlank
    @Size(max = 100)
    @Column
    private String password;

    @NotBlank
    @Size(max = 50)
    @Column
    private String username;

    @Column
    @OneToMany(fetch = EAGER, cascade = ALL, orphanRemoval = true)
    private Set<Network> networks = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user", orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<Authority> authorities = new HashSet<>();

    public User(String email, String password, String username) {
        this.email = email;
        this.password = password;
        this.username = username;
    }

    public User() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Set<Authority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<Authority> authorities) {
        this.authorities = authorities;
    }

    public Set<Network> getNetworks() {
        return networks;
    }

    public void setNetworks(Set<Network> networks) {
        this.networks = networks;
    }

    public ImmutableList<Network> getSortedNetworks() {
        Ordering<Network> wageOrdering = Ordering.natural().reverse().onResultOf(Network::getId);
        return ImmutableSortedSet.orderedBy(wageOrdering).addAll(getNetworks()).build().asList();
    }
}
