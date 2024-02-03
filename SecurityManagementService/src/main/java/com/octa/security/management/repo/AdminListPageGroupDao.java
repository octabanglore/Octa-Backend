package com.octa.security.management.repo;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.octa.security.management.entity.AdminListpageGroup;
import com.octa.transaction.repo.BaseDAO;

import jakarta.persistence.TypedQuery;

@Repository
public class AdminListPageGroupDao extends BaseDAO<Long, AdminListpageGroup>{
	
	public List<AdminListpageGroup> findAllByModuleId(Long moduleId) {
        String query = "SELECT e FROM AdminListpageGroup e WHERE e.moduleId = :moduleId";
        TypedQuery<AdminListpageGroup> typedQuery = getEntityManager().createQuery(query, AdminListpageGroup.class);
        typedQuery.setParameter("moduleId", moduleId);
        return typedQuery.getResultList();
    }


}
