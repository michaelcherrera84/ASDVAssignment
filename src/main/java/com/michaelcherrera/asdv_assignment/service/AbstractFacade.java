package com.michaelcherrera.asdv_assignment.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaQuery;

import java.util.List;

/**
 * AbstractFacade is a generic abstract class that provides common CRUD (Create, Read, Update, Delete) operations for
 * JPA-managed entities. It serves as a reusable base class for stateless session beans that expose entity management
 * functionality through a web service.
 *
 * <p>This class uses the Java Persistence API (JPA) to interact with the underlying database.
 * Subclasses must implement the {@code getEntityManager()} method to provide access to the appropriate
 * {@link jakarta.persistence.EntityManager} instance.</p>
 *
 * @param <T> the type of the entity managed by this facade
 * @author michaelcherrera
 */
public abstract class AbstractFacade<T> {

    private Class<T> entityClass;

    public AbstractFacade(Class<T> entityClass) {

        this.entityClass = entityClass;
    }

    protected abstract EntityManager getEntityManager();

    public void create(T entity) {

        getEntityManager().persist(entity);
    }

    public void edit(T entity) {

        getEntityManager().merge(entity);
    }

    public void remove(T entity) {

        getEntityManager().remove(getEntityManager().merge(entity));
    }

    public T find(Object id) {

        return getEntityManager().find(entityClass, id);
    }

    public List<T> findAll() {

        jakarta.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        return getEntityManager().createQuery(cq).getResultList();
    }

    public List<T> findRange(int[] range) {

        jakarta.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        jakarta.persistence.Query q = getEntityManager().createQuery(cq);
        q.setMaxResults(range[1] - range[0] + 1);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {

        jakarta.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        jakarta.persistence.criteria.Root<T> rt = cq.from(entityClass);
        cq.select(getEntityManager().getCriteriaBuilder().count(rt));
        jakarta.persistence.Query q = getEntityManager().createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }

    /**
     * Find All usernames.
     *
     * @return a List of all usernames
     */
    public List<String> findAllNames() {

        CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass).get("username"));
        return getEntityManager().createQuery(cq).getResultList();
    }

    /**
     * Find by username.
     *
     * @return the User
     */
    public T findByUsername(String username) {

        CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        cq.where(getEntityManager().getCriteriaBuilder().equal(cq.from(entityClass).get("username"), username));
        List<T> result = getEntityManager().createQuery(cq).getResultList();
        if (result.isEmpty())
            return null;
        return result.get(0);
    }

}
