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
 * UserPermissions Entity.\n@author Mihindu Karunarathne.
 */
@ApiModel(description = "UserPermissions Entity.\n@author Mihindu Karunarathne.")
@Entity
@Table(name = "user_permissions")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class UserPermissions implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_perm_key")
    private String userPermKey;

    @Column(name = "user_perm_description")
    private String userPermDescription;

    @Column(name = "is_active")
    private Boolean isActive;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @NotNull
    @JoinTable(name = "user_permissions_menu_items",
               joinColumns = @JoinColumn(name = "user_permissions_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "menu_items_id", referencedColumnName = "id"))
    private Set<MenuItems> menuItems = new HashSet<>();

    @ManyToMany(mappedBy = "userPermissions")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JsonIgnore
    private Set<ExUser> userPermissions = new HashSet<>();

    @ManyToMany(mappedBy = "userPermissions")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JsonIgnore
    private Set<UserGroups> userGroups = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserPermKey() {
        return userPermKey;
    }

    public UserPermissions userPermKey(String userPermKey) {
        this.userPermKey = userPermKey;
        return this;
    }

    public void setUserPermKey(String userPermKey) {
        this.userPermKey = userPermKey;
    }

    public String getUserPermDescription() {
        return userPermDescription;
    }

    public UserPermissions userPermDescription(String userPermDescription) {
        this.userPermDescription = userPermDescription;
        return this;
    }

    public void setUserPermDescription(String userPermDescription) {
        this.userPermDescription = userPermDescription;
    }

    public Boolean isIsActive() {
        return isActive;
    }

    public UserPermissions isActive(Boolean isActive) {
        this.isActive = isActive;
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Set<MenuItems> getMenuItems() {
        return menuItems;
    }

    public UserPermissions menuItems(Set<MenuItems> menuItems) {
        this.menuItems = menuItems;
        return this;
    }

    public UserPermissions addMenuItems(MenuItems menuItems) {
        this.menuItems.add(menuItems);
        menuItems.getUserPermissions().add(this);
        return this;
    }

    public UserPermissions removeMenuItems(MenuItems menuItems) {
        this.menuItems.remove(menuItems);
        menuItems.getUserPermissions().remove(this);
        return this;
    }

    public void setMenuItems(Set<MenuItems> menuItems) {
        this.menuItems = menuItems;
    }

    public Set<ExUser> getUserPermissions() {
        return userPermissions;
    }

    public UserPermissions userPermissions(Set<ExUser> exUsers) {
        this.userPermissions = exUsers;
        return this;
    }

    public UserPermissions addUserPermissions(ExUser exUser) {
        this.userPermissions.add(exUser);
        exUser.getUserPermissions().add(this);
        return this;
    }

    public UserPermissions removeUserPermissions(ExUser exUser) {
        this.userPermissions.remove(exUser);
        exUser.getUserPermissions().remove(this);
        return this;
    }

    public void setUserPermissions(Set<ExUser> exUsers) {
        this.userPermissions = exUsers;
    }

    public Set<UserGroups> getUserGroups() {
        return userGroups;
    }

    public UserPermissions userGroups(Set<UserGroups> userGroups) {
        this.userGroups = userGroups;
        return this;
    }

    public UserPermissions addUserGroups(UserGroups userGroups) {
        this.userGroups.add(userGroups);
        userGroups.getUserPermissions().add(this);
        return this;
    }

    public UserPermissions removeUserGroups(UserGroups userGroups) {
        this.userGroups.remove(userGroups);
        userGroups.getUserPermissions().remove(this);
        return this;
    }

    public void setUserGroups(Set<UserGroups> userGroups) {
        this.userGroups = userGroups;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserPermissions)) {
            return false;
        }
        return id != null && id.equals(((UserPermissions) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "UserPermissions{" +
            "id=" + getId() +
            ", userPermKey='" + getUserPermKey() + "'" +
            ", userPermDescription='" + getUserPermDescription() + "'" +
            ", isActive='" + isIsActive() + "'" +
            "}";
    }
}