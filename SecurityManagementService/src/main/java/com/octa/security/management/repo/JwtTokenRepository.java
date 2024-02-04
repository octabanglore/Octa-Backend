package com.octa.security.management.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.octa.security.management.entity.JwtAuthTkn;
import com.octa.transaction.entity.Tenant;
import com.octa.transaction.platform.OctaTransaction;
import com.octa.transaction.repo.BaseDAO;

import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;

@Repository
public class JwtTokenRepository extends BaseDAO<Long, JwtAuthTkn> {

	public Optional<JwtAuthTkn> findByToken(String token) {
		Query q = getEntityManager().createQuery("FROM JwtAuthTkn WHERE userToken =:token");
		q.setParameter("token", token);
		JwtAuthTkn result = (JwtAuthTkn) q.getSingleResult();
		return Optional.ofNullable(result);
	}

	public List<JwtAuthTkn> findAllValidTokenByUser(Long id) {
		TypedQuery<JwtAuthTkn> q = getEntityManager().createQuery("FROM JwtAuthTkn e WHERE e.user.id =:id", JwtAuthTkn.class);
		q.setParameter("id", id);
		List<JwtAuthTkn> list = q.getResultList();
		return list;

	}

}
