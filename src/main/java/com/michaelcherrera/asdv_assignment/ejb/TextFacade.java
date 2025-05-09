package com.michaelcherrera.asdv_assignment.ejb;

import com.michaelcherrera.asdv_assignment.entities.Problem;
import com.michaelcherrera.asdv_assignment.interfaces.TextFacadeLocal;
import com.michaelcherrera.asdv_assignment.entities.Text;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

import java.util.List;

/**
 * {@code @Stateless} session bean handling persistence logic for {@link Text} entities.
 *
 * @author michaelcherrera
 */
@SuppressWarnings({"unused", "unchecked"})
@Stateless
public class TextFacade extends AbstractFacade<Text> implements TextFacadeLocal {

    @PersistenceContext(unitName = "my_persistence_unit")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {

        return em;
    }

    public TextFacade() {

        super(Text.class);
    }

    /**
     * Return the Text associated with a specified Problem.
     *
     * @param problem the specified Problem
     * @return the Text
     */
    public Text findByProblem(Problem problem) {

        Query query = em.createNamedQuery("Text.findByProblem", Text.class);
        query.setParameter("problem", problem);
        List<Text> result = (List<Text>) query.getResultList();
        if (result.isEmpty())
            return null;
        return result.get(result.size() - 1);
    }
}
