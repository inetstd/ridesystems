package com.ad.android.ridesystems.passengercounter.model.dao;

import java.io.Serializable;
import java.util.List;

import com.ad.android.ridesystems.passengercounter.model.entities.AEntity;

/**
 * CRUD Interface.
 *
 * @param <E> entity class
 * @param <ID> id class
 */
public interface IDAO <E extends AEntity, ID extends Serializable> {
	/**
	 * gets entity by id. 
	 * @param id id
	 * @return entity;
	 */
	public E get(ID id);
	
	/**
	 * By default use get(id). Need to be overwrite in ancestor 
	 * @param id
	 * @return
	 */
	public E getFull(ID id);
	
	/**
	 * Insert entity to DB   
	 * @param entity - instance 
	 * @return entity with id
	 */
	public E insert(E entity);
	
	/**
	 * Update entity in DB
	 * @param entity
	 */
	public void update(E entity);
	
	/**
	 * Delete entity from DB
	 * @param id
	 */
	public void delete(ID id);	
	
	/**
	 * Get all entities
	 * @return array of entities
	 */
	public List<E> getAll();
	
	/**
	 * Remove all entities
	 */
	public void deleteAll();

}
