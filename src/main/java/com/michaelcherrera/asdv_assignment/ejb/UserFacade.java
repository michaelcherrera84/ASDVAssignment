package com.michaelcherrera.asdv_assignment.ejb;

import com.michaelcherrera.asdv_assignment.interfaces.UserFacadeLocal;
import com.michaelcherrera.asdv_assignment.entities.User;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

/**
 * {@code @Stateless} session bean handling persistence logic for {@link User} entities.
 *
 * @author michaelcherrera
 */
@SuppressWarnings("unused")
@Stateless
public class UserFacade extends AbstractFacade<User> implements UserFacadeLocal {

    @PersistenceContext(unitName = "my_persistence_unit")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {

        return em;
    }

    public UserFacade() {

        super(User.class);
    }

    /**
     * Find a User by username.
     *
     * @param username the username of the User
     * @return the User from the database
     */
    @Override
    public User findByUsername(String username) {

        Query query = em.createNamedQuery("User.findByUsername");
        query.setParameter("username", username);
        try {
            return (User) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (Throwable t) {
            throw t;
        }
    }
}
