package com.michaelcherrera.asdv_assignment.interfaces;

import com.michaelcherrera.asdv_assignment.entities.Answer;
import com.michaelcherrera.asdv_assignment.entities.AnswerPK;
import jakarta.ejb.Local;
import java.util.List;

/**
 * Local EJB interface providing CRUD operations for {@link Answer} entities.
 *
 * @author michaelcherrera
 */
@SuppressWarnings("unused")
@Local
public interface AnswerFacadeLocal {

    void create(Answer answer);

    void edit(Answer answer);

    void remove(Answer answer);

    Answer find(Object id);

    List<Answer> findAll();

    List<Answer> findRange(int[] range);

    int count();

    Answer findByKey(AnswerPK key);
}
