package com.alphadevs.sales.repository;
import com.alphadevs.sales.domain.UserGroups;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the UserGroups entity.
 */
@Repository
public interface UserGroupsRepository extends JpaRepository<UserGroups, Long>, JpaSpecificationExecutor<UserGroups> {

    @Query(value = "select distinct userGroups from UserGroups userGroups left join fetch userGroups.userPermissions",
        countQuery = "select count(distinct userGroups) from UserGroups userGroups")
    Page<UserGroups> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct userGroups from UserGroups userGroups left join fetch userGroups.userPermissions")
    List<UserGroups> findAllWithEagerRelationships();

    @Query("select userGroups from UserGroups userGroups left join fetch userGroups.userPermissions where userGroups.id =:id")
    Optional<UserGroups> findOneWithEagerRelationships(@Param("id") Long id);

}
