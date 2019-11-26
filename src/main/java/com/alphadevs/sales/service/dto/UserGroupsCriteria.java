package com.alphadevs.sales.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.alphadevs.sales.domain.UserGroups} entity. This class is used
 * in {@link com.alphadevs.sales.web.rest.UserGroupsResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /user-groups?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class UserGroupsCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter groupName;

    private LongFilter userPermissionsId;

    private LongFilter usersId;

    public UserGroupsCriteria(){
    }

    public UserGroupsCriteria(UserGroupsCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.groupName = other.groupName == null ? null : other.groupName.copy();
        this.userPermissionsId = other.userPermissionsId == null ? null : other.userPermissionsId.copy();
        this.usersId = other.usersId == null ? null : other.usersId.copy();
    }

    @Override
    public UserGroupsCriteria copy() {
        return new UserGroupsCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getGroupName() {
        return groupName;
    }

    public void setGroupName(StringFilter groupName) {
        this.groupName = groupName;
    }

    public LongFilter getUserPermissionsId() {
        return userPermissionsId;
    }

    public void setUserPermissionsId(LongFilter userPermissionsId) {
        this.userPermissionsId = userPermissionsId;
    }

    public LongFilter getUsersId() {
        return usersId;
    }

    public void setUsersId(LongFilter usersId) {
        this.usersId = usersId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final UserGroupsCriteria that = (UserGroupsCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(groupName, that.groupName) &&
            Objects.equals(userPermissionsId, that.userPermissionsId) &&
            Objects.equals(usersId, that.usersId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        groupName,
        userPermissionsId,
        usersId
        );
    }

    @Override
    public String toString() {
        return "UserGroupsCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (groupName != null ? "groupName=" + groupName + ", " : "") +
                (userPermissionsId != null ? "userPermissionsId=" + userPermissionsId + ", " : "") +
                (usersId != null ? "usersId=" + usersId + ", " : "") +
            "}";
    }

}
