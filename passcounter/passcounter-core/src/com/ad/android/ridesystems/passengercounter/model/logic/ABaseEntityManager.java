package com.ad.android.ridesystems.passengercounter.model.logic;

import java.io.Serializable;
import java.util.List;

import com.ad.android.ridesystems.passengercounter.model.dao.IDAO;
import com.ad.android.ridesystems.passengercounter.model.entities.AEntity;

/**
 * Base class for managers uses DAO.  
 * 
 *
 * @param <E> Entity class
 * @param <ID> Id class
 */
abstract public class ABaseEntityManager<E extends AEntity, ID extends Serializable> extends ABaseManager {
	
	/**
	 * DAO instance.
	 */
	protected IDAO<E, ID> dao = null;
	
	/**
	 * Constructor. 
	 * @param dao - DAO instance
	 */
	public ABaseEntityManager(IDAO<E, ID> dao) {
		this.dao = dao;
	}
	
	/**
	 * Insert entity
	 * @param entity - entity to insert
	 * @return id in DB
	 */
	public E insert(E entity) {
		return dao.insert(entity);
	}
	
	/**
	 * Insert all
	 * @param entities - array of entities to insert
	 */
	public void insertAll(List<E> entities) {
		for (E entity : entities) {
			dao.insert(entity);
		}		
	}
	
	/**
	 * Updates entity
	 * @param entity - entity to update
	 */
	public void update(E entity) {
		dao.update(entity);
	}
	
	/**
	 * 
	 * Delete entity
	 * @param id - id of entity to delete 
	 */
	public void delete(ID id) {
		dao.delete(id);
	}
	
	/**
	 * Delete entity
	 * @param entity - entity to delete
	 */
	@SuppressWarnings("unchecked")
	public void delete(AEntity entity) {
		dao.delete((ID) entity.getId());
	}
	
	
	/**
	 * Get entity by id 
	 * @return entity
	 */
	public E get(ID id) {
		return dao.get(id);
	}
	
	/**
	 * Get full by id (with joins etc). To override if needed
	 * @param id id
	 * @return entity
	 */
	public E getFull(ID id) {
		return dao.getFull(id);
	}
	
	/**
	 * Get all entities
	 * @return array of entities
	 */
	public List<E> getAll() {
		return dao.getAll();
	}
	
	/**
	 * Delete all entities 
	 */
	public void deleteAll() {
		dao.deleteAll();
	}
	
	/**
	 * Delete all entities and insert new.
	 * @param stops Array of stops
	 */
	public void syncAll(List<E> entities) {
		this.deleteAll();
		this.insertAll(entities);
	}

}
