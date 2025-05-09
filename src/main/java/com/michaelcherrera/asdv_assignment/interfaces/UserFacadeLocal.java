package com.michaelcherrera.asdv_assignment.interfaces;

import com.michaelcherrera.asdv_assignment.entities.User;
import jakarta.ejb.Local;

import java.util.List;

/**
 * Local EJB interface providing CRUD operations for {@link User} entities.
 *
 * @author michaelcherrera
 */
@SuppressWarnings("unused")
@Local
public interface UserFacadeLocal {

    void create(User user);

    void edit(User user);

    void remove(User user);

    User find(Object id);

    List<User> findAll();

    List<User> findRange(int[] range);

    int count();

    /**
     * Find a User by username.
     *
     * @param username the username of the User
     * @return the User from the database
     */
    User findByUsername(String username);
}
