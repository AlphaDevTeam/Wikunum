package com.alphadevs.sales.domain;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * UserGroups Entity.\n@author Mihindu Karunarathne.
 */
@ApiModel(description = "UserGroups Entity.\n@author Mihindu Karunarathne.")
@Entity
@Table(name = "user_groups")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class UserGroups implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "group_name", nullable = false)
    private String groupName;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @NotNull
    @JoinTable(name = "user_groups_user_permissions",
               joinColumns = @JoinColumn(name = "user_groups_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "user_permissions_id", referencedColumnName = "id"))
    private Set<UserPermissions> userPermissions = new HashSet<>();

    @ManyToMany(mappedBy = "userGroups")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JsonIgnore
    private Set<ExUser> users = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGroupName() {
        return groupName;
    }

    public UserGroups groupName(String groupName) {
        this.groupName = groupName;
        return this;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Set<UserPermissions> getUserPermissions() {
        return userPermissions;
    }

    public UserGroups userPermissions(Set<UserPermissions> userPermissions) {
        this.userPermissions = userPermissions;
        return this;
    }

    public UserGroups addUserPermissions(UserPermissions userPermissions) {
        this.userPermissions.add(userPermissions);
        userPermissions.getUserGroups().add(this);
        return this;
    }

    public UserGroups removeUserPermissions(UserPermissions userPermissions) {
        this.userPermissions.remove(userPermissions);
        userPermissions.getUserGroups().remove(this);
        return this;
    }

    public void setUserPermissions(Set<UserPermissions> userPermissions) {
        this.userPermissions = userPermissions;
    }

    public Set<ExUser> getUsers() {
        return users;
    }

    public UserGroups users(Set<ExUser> exUsers) {
        this.users = exUsers;
        return this;
    }

    public UserGroups addUsers(ExUser exUser) {
        this.users.add(exUser);
        exUser.getUserGroups().add(this);
        return this;
    }

    public UserGroups removeUsers(ExUser exUser) {
        this.users.remove(exUser);
        exUser.getUserGroups().remove(this);
        return this;
    }

    public void setUsers(Set<ExUser> exUsers) {
        this.users = exUsers;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserGroups)) {
            return false;
        }
        return id != null && id.equals(((UserGroups) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "UserGroups{" +
            "id=" + getId() +
            ", groupName='" + getGroupName() + "'" +
            "}";
    }
}
