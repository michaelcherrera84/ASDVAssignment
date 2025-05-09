package com.michaelcherrera.asdv_assignment.interfaces;

import com.michaelcherrera.asdv_assignment.entities.Problem;
import com.michaelcherrera.asdv_assignment.entities.Text;
import jakarta.ejb.Local;
import java.util.List;

/**
 * Local EJB interface providing CRUD operations for {@link Text} entities.
 *
 * @author michaelcherrera
 */
@SuppressWarnings("unused")
@Local
public interface TextFacadeLocal {

    void create(Text text);

    void edit(Text text);

    void remove(Text text);

    Text find(Object id);

    List<Text> findAll();

    List<Text> findRange(int[] range);

    int count();

    /**
     * Return the Text associated with a specified Problem.
     *
     * @param problem the specified Problem
     * @return the Text
     */
    Text findByProblem(Problem problem);
}
