package com.octa.security.management.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.octa.security.management.entity.User;
import com.octa.transaction.repo.BaseDAO;

import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;

@Repository
public class UserRepository extends BaseDAO<Long, User> {
	
	@Override
	public User findOneEntityByAttribute(String attributeName, Object attributeValue) {
		return super.findOneEntityByAttribute(attributeName, attributeValue);
	}
	
	public Optional<User> findByPrimaryEmailId(String loginName){
		try {
			Query q = getEntityManager().createQuery("FROM User WHERE loginName =:loginName");
			q.setParameter("loginName", loginName);
			return Optional.of((User)q.getSingleResult());
		}catch(NoResultException noResultException ) {
			return Optional.empty();
		}
	}
	
	public User findByUserName(String userName){
		Query q = getEntityManager().createQuery("select u FROM User u WHERE loginName =:loginName");
		q.setParameter("loginName", userName);
		List l = q.getResultList();
		return l.isEmpty() ? null : (User) l.get(0);
	}
	
	public User findByName(String firstName){
		Query q = getEntityManager().createQuery("select u FROM User u left join fetch u.tenant t WHERE u.userFirstName =:username", User.class);
		q.setParameter("username", firstName);
		List l = q.getResultList();
		return l.isEmpty() ? null : (User) l.get(0);
	}
	
	public User findByPasswordToken(String passwordToken){
		Query q = getEntityManager().createQuery("select u FROM User u WHERE resetPasswordToken =:resetPasswordToken", User.class);
		q.setParameter("resetPasswordToken", passwordToken);
		List l = q.getResultList();
		return l.isEmpty() ? null : (User) l.get(0);
	}
}
