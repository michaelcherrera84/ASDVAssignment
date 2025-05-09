package com.michaelcherrera.asdv_assignment.interfaces;

import com.michaelcherrera.asdv_assignment.entities.Assignment;
import jakarta.ejb.Local;
import java.util.List;

/**
 * Local EJB interface providing CRUD operations for {@link Assignment} entities.
 *
 * @author michaelcherrera
 */
@SuppressWarnings("unused")
@Local
public interface AssignmentFacadeLocal {

    void create(Assignment assignment);

    void edit(Assignment assignment);

    void remove(Assignment assignment);

    Assignment find(Object id);

    List<Assignment> findAll();

    List<Assignment> findRange(int[] range);

    int count();
    
}
