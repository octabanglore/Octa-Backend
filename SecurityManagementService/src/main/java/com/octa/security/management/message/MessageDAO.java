package com.octa.security.management.message;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.octa.security.management.message.entity.Message;
import com.octa.transaction.repo.BaseDAO;

import jakarta.persistence.TypedQuery;

@Repository
public class MessageDAO extends BaseDAO<Long, Message>{

	public List<Message> getMessages() {
		/*TypedQuery<Message> q = getEntityManager().createQuery("FROM Message WHERE active=1 ORDER BY id",Message.class);
		List<Message> vlcMsgList = q.getResultList();
		return vlcMsgList;*/
		return new ArrayList<Message>();
	}
}
