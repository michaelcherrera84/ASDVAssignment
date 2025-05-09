package com.michaelcherrera.asdv_assignment.ejb;

import com.michaelcherrera.asdv_assignment.entities.Problem;
import com.michaelcherrera.asdv_assignment.interfaces.MultipleChoiceFacadeLocal;
import com.michaelcherrera.asdv_assignment.entities.MultipleChoice;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

/**
 * {@code @Stateless} session bean handling persistence logic for {@link MultipleChoice} entities.
 *
 * @author michaelcherrera
 */
@SuppressWarnings("unused")
@Stateless
public class MultipleChoiceFacade extends AbstractFacade<MultipleChoice> implements MultipleChoiceFacadeLocal {

    @PersistenceContext(unitName = "my_persistence_unit")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {

        return em;
    }

    public MultipleChoiceFacade() {

        super(MultipleChoice.class);
    }

    /**
     * Find the multiple-choice answers of a problem by problem and answer number.
     *
     * @param problem the problem
     * @param num     the answer number
     * @return the multiple-choice answer
     */
    @Override
    public MultipleChoice findByProbAndNum(Problem problem, int num) {

        Query query = em.createNamedQuery("MultipleChoice.findByProbAndNum");
        query.setParameter("problem", problem);
        query.setParameter("num", num);
        try {
            return (MultipleChoice) query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
}
