package com.alphadevs.sales.domain;
import io.swagger.annotations.ApiModel;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Extra User Entity.\n@author Mihindu Karunarathne.
 */
@ApiModel(description = "Extra User Entity.\n@author Mihindu Karunarathne.")
@Entity
@Table(name = "ex_user")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ExUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "user_key", nullable = false)
    private String userKey;

    @OneToOne
    @JoinColumn(unique = true)
    private User relatedUser;

    @OneToOne(optional = false)
    @NotNull
    @JoinColumn(unique = true)
    private Company company;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @NotNull
    @JoinTable(name = "ex_user_locations",
               joinColumns = @JoinColumn(name = "ex_user_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "locations_id", referencedColumnName = "id"))
    private Set<Location> locations = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "ex_user_user_groups",
               joinColumns = @JoinColumn(name = "ex_user_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "user_groups_id", referencedColumnName = "id"))
    private Set<UserGroups> userGroups = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "ex_user_user_permissions",
               joinColumns = @JoinColumn(name = "ex_user_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "user_permissions_id", referencedColumnName = "id"))
    private Set<UserPermissions> userPermissions = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove


    public ExUser() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserKey() {
        return userKey;
    }

    public ExUser userKey(String userKey) {
        this.userKey = userKey;
        return this;
    }

    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }

    public User getRelatedUser() {
        return relatedUser;
    }

    public ExUser relatedUser(User user) {
        this.relatedUser = user;
        return this;
    }

    public void setRelatedUser(User user) {
        this.relatedUser = user;
    }

    public Company getCompany() {
        return company;
    }

    public ExUser company(Company company) {
        this.company = company;
        return this;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Set<Location> getLocations() {
        return locations;
    }

    public ExUser locations(Set<Location> locations) {
        this.locations = locations;
        return this;
    }

    public ExUser addLocations(Location location) {
        this.locations.add(location);
        location.getUsers().add(this);
        return this;
    }

    public ExUser removeLocations(Location location) {
        this.locations.remove(location);
        location.getUsers().remove(this);
        return this;
    }

    public void setLocations(Set<Location> locations) {
        this.locations = locations;
    }

    public Set<UserGroups> getUserGroups() {
        return userGroups;
    }

    public ExUser userGroups(Set<UserGroups> userGroups) {
        this.userGroups = userGroups;
        return this;
    }

    public ExUser addUserGroups(UserGroups userGroups) {
        this.userGroups.add(userGroups);
        userGroups.getUsers().add(this);
        return this;
    }

    public ExUser removeUserGroups(UserGroups userGroups) {
        this.userGroups.remove(userGroups);
        userGroups.getUsers().remove(this);
        return this;
    }

    public void setUserGroups(Set<UserGroups> userGroups) {
        this.userGroups = userGroups;
    }

    public Set<UserPermissions> getUserPermissions() {
        return userPermissions;
    }

    public ExUser userPermissions(Set<UserPermissions> userPermissions) {
        this.userPermissions = userPermissions;
        return this;
    }

    public ExUser addUserPermissions(UserPermissions userPermissions) {
        this.userPermissions.add(userPermissions);
        userPermissions.getUserPermissions().add(this);
        return this;
    }

    public ExUser removeUserPermissions(UserPermissions userPermissions) {
        this.userPermissions.remove(userPermissions);
        userPermissions.getUserPermissions().remove(this);
        return this;
    }

    public void setUserPermissions(Set<UserPermissions> userPermissions) {
        this.userPermissions = userPermissions;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ExUser)) {
            return false;
        }
        return id != null && id.equals(((ExUser) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "ExUser{" +
            "id=" + getId() +
            ", userKey='" + getUserKey() + "'" +
            "}";
    }
}
