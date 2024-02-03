package com.octa.security.management.repo;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.octa.security.management.entity.AdminListpage;
import com.octa.security.management.entity.AdminListpageGroup;
import com.octa.transaction.repo.BaseDAO;

import jakarta.persistence.TypedQuery;

@Repository
public class AdminListPageDao extends BaseDAO<Long, AdminListpage>{
	
	public List<AdminListpage> findAllByGroupId(Long listpageGroupId) {
        String query = "SELECT e FROM AdminListpage e WHERE e.listpageGroupId = :listpageGroupId";
        TypedQuery<AdminListpage> typedQuery = getEntityManager().createQuery(query, AdminListpage.class);
        typedQuery.setParameter("listpageGroupId", listpageGroupId);
        return typedQuery.getResultList();
    }

}
