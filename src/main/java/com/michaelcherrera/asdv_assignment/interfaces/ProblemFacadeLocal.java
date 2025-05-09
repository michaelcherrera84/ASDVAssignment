package com.michaelcherrera.asdv_assignment.interfaces;

import com.michaelcherrera.asdv_assignment.entities.Assignment;
import com.michaelcherrera.asdv_assignment.entities.Problem;
import jakarta.ejb.Local;

import java.util.List;

/**
 * Local EJB interface providing CRUD operations for {@link Problem} entities.
 *
 * @author michaelcherrera
 */
@SuppressWarnings("unused")
@Local
public interface ProblemFacadeLocal {

    void create(Problem problem);

    void edit(Problem problem);

    void remove(Problem problem);

    Problem find(Object id);

    List<Problem> findAll();

    List<Problem> findRange(int[] range);

    int count();

    void update(Problem oldProblem, Problem newProblem);

    /**
     * Return the problem with the given name.
     *
     * @param name the name of the problem
     * @return the {@link Problem} entity
     */
    Problem findByName(String name);

    /**
     * Return a list of all Problems for the given assignment.
     *
     * @param assignment the {@link Assignment}
     * @return a list of all {@link Problem} for the given assignment
     */
    List<Problem> findByAssignment(Assignment assignment);
}
