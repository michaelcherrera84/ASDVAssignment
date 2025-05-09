package com.michaelcherrera.asdv_assignment.ejb;

import com.michaelcherrera.asdv_assignment.interfaces.AssignmentFacadeLocal;
import com.michaelcherrera.asdv_assignment.entities.Assignment;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

/**
 * {@code @Stateless} session bean handling persistence logic for {@link Assignment} entities.
 *
 * @author michaelcherrera
 */
@SuppressWarnings("unused")
@Stateless
public class AssignmentFacade extends AbstractFacade<Assignment> implements AssignmentFacadeLocal {

    @PersistenceContext(unitName = "my_persistence_unit")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AssignmentFacade() {
        super(Assignment.class);
    }
    
}
