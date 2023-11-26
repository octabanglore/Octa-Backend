package com.octa.transaction.repo;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.stat.Statistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.cache.CacheManager;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaDelete;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

public class BaseDAO <Key extends Serializable, Entity> implements BaseDAOContract<Key, Entity>{

public static final String CACHE_KEY_FOR_FIND_ALL = "all";
	
	@Autowired
	EntityManager octaEntityManager;
	/*
	 * @Autowired CacheManager cacheManager;
	 */
	//@Value("${default.pagination.page.size}")
	private Integer defaultPageSize = 50;
	
	private Class<Entity> clazz;
	private boolean masterDataEntity;
	
	@SuppressWarnings("unchecked")
	public BaseDAO() {
		clazz = (Class<Entity>) ((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[1];
		/*
		 * if (clazz.isAnnotationPresent(MasterData.class)) { masterDataEntity = true; }
		 */
	}
	
	@Override
	public Entity get(Key key) {
		return octaEntityManager.find(clazz, key);
	}

	@Override
	public Entity save(Entity e) {
		save(e, false);
		//TODO: This should update the cache (across the cluster, push to redis pub sub?)
		return e;
	}
	
	@Override
	public Entity save(Entity e, boolean flushImmediately) {
		octaEntityManager.persist(e);
		if (flushImmediately) {
			octaEntityManager.flush();
		}
		//TODO: This should update the cache (across the cluster, push to redis pub sub?)
		return e;
	}

	@Override
	public Entity update(Entity e) {
		return octaEntityManager.merge(e);
		//TODO: This should update the cache (across the cluster, push to redis pub sub?)
	}

	@Override
	public void delete(Entity e) {
		octaEntityManager.remove(e);
		//TODO: This should update the cache (across the cluster, push to redis pub sub?)
	}
	
	@Override
	public int deleteById(Key key) {
		return octaEntityManager.createQuery("delete from " + clazz.getSimpleName() + " where id = " + key).executeUpdate();
		//TODO: This should update the cache (across the cluster, push to redis pub sub?)
	}

	@Override
	public void saveAll(List<Entity> entities) {
		for (Entity e : entities) {
			save(e);
		}
	}

	@Override
	public void updateAll(List<Entity> entities) {
		for (Entity e : entities) {
			update(e);
		}
	}

	@Override
	public void deleteAll(List<Entity> entities) {
		for (Entity e : entities) {
			delete(e);
		}
	}

	@Override
	public int deleteAllById(Collection<Key> keys) {
		CriteriaDelete<Entity> deleteCriteria = octaEntityManager.getCriteriaBuilder().createCriteriaDelete(clazz);
		Root<Entity> root = deleteCriteria.from(clazz);
		deleteCriteria.where(root.get("id").in(keys));
		return octaEntityManager.createQuery(deleteCriteria).executeUpdate();
		//TODO: This should update the cache (across the cluster, push to redis pub sub?)
	}

	@Override
	public void flush() {
		octaEntityManager.flush();
	}
	
	protected EntityManager getEntityManager() {
		return octaEntityManager;
	}
	
	@Override
	public void clear() {
		octaEntityManager.clear();
	}
	
	@Override
	public List<Entity> findAll() {
		return findAll(false);
	}
	
	@Override
	public List<Entity> findAll(boolean enableQueryCache) {
		if (masterDataEntity) {
			List<Entity> entities = getEntitiesFromCache();
			if (entities != null) {
				return entities;
			}
		}
		CriteriaQuery<Entity> query = octaEntityManager.getCriteriaBuilder().createQuery(clazz);
		Root<Entity> root = query.from(clazz);
		TypedQuery<Entity> t = octaEntityManager.createQuery(query.select(root));
		if (enableQueryCache) {
			t.setHint("org.hibernate.cacheable", Boolean.TRUE);
//			t.setHint("org.hibernate.cacheMode", "NORMAL");
		}
		if (masterDataEntity) {
			t.setHint("org.hibernate.readOnly", Boolean.TRUE);
		}
		List<Entity> entities = t.getResultList();
		if (masterDataEntity) {
			putInCache(entities);
		}
		return entities;
	}

	private void putInCache(List<Entity> entities) {
		/*
		 * Cache cache = cacheManager.getCache(clazz.getName());
		 * cache.put(CACHE_KEY_FOR_FIND_ALL, entities);
		 */	
	}

	private List<Entity> getEntitiesFromCache() {
		//Cache cache = cacheManager.getCache(clazz.getName());
		//ValueWrapper wrapper = cache.get(CACHE_KEY_FOR_FIND_ALL);
		//return (wrapper != null) ? (List<Entity>) wrapper.get() : null;
		return null;
	}

	@Override
	public boolean isLoaded(Object entityOrCollection) {
		return octaEntityManager.getEntityManagerFactory().getPersistenceUnitUtil().isLoaded(entityOrCollection);
	}
	
	public List<Entity> findEntitiesWithPagination(TypedQuery<Entity> query, int pageIndex, int pageSize) {
		return query.setFirstResult(pageIndex * pageSize).setMaxResults(pageSize).getResultList();
	}
	
	public List<Entity> findEntitiesWithPagination(TypedQuery<Entity> query, int pageIndex) {
		return findEntitiesWithPagination(query, pageIndex * defaultPageSize, defaultPageSize);
	}
	
	protected Entity findOneEntityByAttribute(String attributeName, Object attributeValue) {
		List<Entity> entities = getEntityManager().createQuery(
				new StringBuilder().append("from ").append(clazz.getSimpleName())
						.append(" where ").append(attributeName).append(" = :").append(attributeName)
						.toString(), clazz).setParameter(attributeName, attributeValue).getResultList();
		return entities.isEmpty() ? null : entities.get(0);
	}
	
	protected List<Entity> findEntitiesByAttribute(String attributeName, Object attributeValue) {
		List<Entity> entities = getEntityManager().createQuery(
				new StringBuilder().append("from ").append(clazz.getSimpleName())
						.append(" where ").append(attributeName).append(" = :").append(attributeName)
						.toString(), clazz).setParameter(attributeName, attributeValue).getResultList();
		return entities;
	}
	
	public Entity findOne() {
		List<Entity> list = findAll(true);
		return (list.isEmpty()) ? null : list.get(0);
	}
	
	public Statistics getStatistics() {
		return getEntityManager().unwrap(Session.class).getSessionFactory().getStatistics();
	}

}
