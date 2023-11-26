package com.octa.transaction.repo;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.octa.transaction.entity.Tenant;

import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
@Repository
public class TenantDAO extends BaseDAO<Long, Tenant>{
	
	public Optional<Tenant> findLoginUrl(String loginUrl){
		try {
			TypedQuery<Tenant> q = getEntityManager().createQuery("FROM Tenant WHERE loginUrl =:loginUrl",Tenant.class);
			q.setParameter("loginUrl", loginUrl);
			return Optional.ofNullable(q.getSingleResult());
		}catch(NoResultException noResultException ) {
			return Optional.empty();
		}
	}

}
