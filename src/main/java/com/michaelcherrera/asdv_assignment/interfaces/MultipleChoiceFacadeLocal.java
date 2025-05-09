package com.michaelcherrera.asdv_assignment.interfaces;

import com.michaelcherrera.asdv_assignment.entities.MultipleChoice;
import com.michaelcherrera.asdv_assignment.entities.Problem;
import jakarta.ejb.Local;

import java.util.List;

/**
 * Local EJB interface providing CRUD operations for {@link MultipleChoice} entities.
 *
 * @author michaelcherrera
 */
@SuppressWarnings("unused")
@Local
public interface MultipleChoiceFacadeLocal {

    void create(MultipleChoice multipleChoice);

    void edit(MultipleChoice multipleChoice);

    void remove(MultipleChoice multipleChoice);

    MultipleChoice find(Object id);

    List<MultipleChoice> findAll();

    List<MultipleChoice> findRange(int[] range);

    int count();

    /**
     * Find the multiple-choice answers of a problem by problem and answer number.
     *
     * @param problem the problem
     * @param num     the answer number
     * @return the multiple-choice answer
     */
    MultipleChoice findByProbAndNum(Problem problem, int num);
}
