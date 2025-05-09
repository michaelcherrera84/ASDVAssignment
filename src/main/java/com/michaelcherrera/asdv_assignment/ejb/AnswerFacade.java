package com.michaelcherrera.asdv_assignment.ejb;

import com.michaelcherrera.asdv_assignment.entities.AnswerPK;
import com.michaelcherrera.asdv_assignment.interfaces.AnswerFacadeLocal;
import com.michaelcherrera.asdv_assignment.entities.Answer;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

/**
 * {@code @Stateless} session bean handling persistence logic for {@link Answer} entities.
 *
 * @author michaelcherrera
 */
@SuppressWarnings("unused")
@Stateless
public class AnswerFacade extends AbstractFacade<Answer> implements AnswerFacadeLocal {

    @PersistenceContext(unitName = "my_persistence_unit")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AnswerFacade() {
        super(Answer.class);
    }

    @Override
    public Answer findByKey(AnswerPK key) {

        Query query = em.createNamedQuery("Answer.findByKey");
        query.setParameter("user", key.getUser());
        query.setParameter("problem", key.getProblem());
        query.setParameter("assignment", key.getAssignment());
        try {
            Answer answer = (Answer) query.getSingleResult();
            return answer;
        } catch (Exception e) {
            return null;
        }
    }
}
