package com.octa.security.management.message;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.octa.security.management.message.entity.Labels;
import com.octa.transaction.repo.BaseDAO;

import jakarta.persistence.TypedQuery;

@Repository
public class LabelsDao extends BaseDAO<Long, Labels> {
	public List<Labels> getLabels() {
		TypedQuery<Labels> q = getEntityManager().createQuery("FROM Labels WHERE active=1 ORDER BY id", Labels.class);
		List<Labels> vlcMsgList = q.getResultList();
		return vlcMsgList;
	}
}
