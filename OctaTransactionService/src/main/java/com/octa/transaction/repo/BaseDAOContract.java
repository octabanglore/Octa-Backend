package com.octa.transaction.repo;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

public interface BaseDAOContract <Key extends Serializable, Entity>{
	public Entity get(Key key);
	public Entity save(Entity e);
	public Entity save(Entity e, boolean flushImmediately);
	public Entity update(Entity e);
	public void delete(Entity e);
	public int deleteById(Key key);
	public void saveAll(List<Entity> entities);
	public void updateAll(List<Entity> entities);
	public void deleteAll(List<Entity> entities);
	public int deleteAllById(Collection<Key> keys);
	public void flush();
	public void clear();
	public boolean isLoaded(Object entityOrCollection);
	List<Entity> findAll();
	List<Entity> findAll(boolean enableQueryCache);
}
