package com.michaelcherrera.asdv_assignment.ejb;

import com.michaelcherrera.asdv_assignment.entities.Assignment;
import com.michaelcherrera.asdv_assignment.interfaces.ProblemFacadeLocal;
import com.michaelcherrera.asdv_assignment.entities.Problem;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

import java.util.List;

/**
 * {@code @Stateless} session bean handling persistence logic for {@link Problem} entities.
 *
 * @author michaelcherrera
 */
@SuppressWarnings({"unused", "unchecked"})
@Stateless
public class ProblemFacade extends AbstractFacade<Problem> implements ProblemFacadeLocal {

    @PersistenceContext(unitName = "my_persistence_unit")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {

        return em;
    }

    public ProblemFacade() {

        super(Problem.class);
    }

    /**
     * Update a Problem with a new name.
     *
     * @param oldProblem the Problem with the old name
     * @param newProblem the Problem with the new name
     */
    @Override
    public void update(Problem oldProblem, Problem newProblem) {

        String sql = "UPDATE problem SET num = ?, name = ?, points = ?, assignment = ? WHERE name = ?";
        Query query = em.createNativeQuery(sql);
        query.setParameter(1, newProblem.getNum());
        query.setParameter(2, newProblem.getName());
        query.setParameter(3, newProblem.getPoints());
        query.setParameter(4, newProblem.getAssignment().getId());
        query.setParameter(5, oldProblem.getName());
        query.executeUpdate();
    }

    /**
     * Return the problem with the given name.
     *
     * @param name the name of the problem
     * @return the {@link Problem} entity
     */
    @Override
    public Problem findByName(String name) {

        Query query = em.createNamedQuery("Problem.findByName");
        query.setParameter("name", name);
        try {
            return (Problem) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (Throwable t) {
            throw t;
        }
    }

    /**
     * Return a list of all Problems for the given assignment.
     *
     * @param assignment the {@link Assignment}
     * @return a list of all {@link Problem} for the given assignment
     */
    @Override
    public List<Problem> findByAssignment(Assignment assignment) {
        try {
            Query query = em.createNamedQuery("Problem.findByAssignment");
            query.setParameter("assignment", assignment);
            return (List<Problem>) query.getResultList();
        } catch (Throwable t) {
            throw t;
        }
    }
}
