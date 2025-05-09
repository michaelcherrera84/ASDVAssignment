package com.michaelcherrera.asdv_assignment.student.beans;

import com.michaelcherrera.asdv_assignment.entities.Answer;
import com.michaelcherrera.asdv_assignment.entities.Assignment;
import com.michaelcherrera.asdv_assignment.entities.Problem;
import com.michaelcherrera.asdv_assignment.interfaces.AnswerFacadeLocal;
import com.michaelcherrera.asdv_assignment.interfaces.AssignmentFacadeLocal;
import com.michaelcherrera.asdv_assignment.interfaces.ProblemFacadeLocal;
import com.michaelcherrera.asdv_assignment.student.producers.ExamProblemBeanList;
import com.michaelcherrera.asdv_assignment.util.Utilities;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * The {@code ExamResultBean} is the backing bean for exam results.
 *
 * @author Michael C. Herrera
 */
@Named
@RequestScoped
public class ExamResultBean implements Serializable {

    /**
     * The current assignment
     */
    private Assignment assignment;
    /**
     * The list of problems for the current assignment
     */
    private List<Problem> problems;
    /**
     * The point value of the assignment problem
     */
    private int problemPoints;
    /**
     * The bean for the selected problem.
     */
    private ExamBean examBean;
    /**
     * The total score for the exam
     */
    private String totalScore;
    /**
     * The total percentage correct for the exam
     */
    private String totalPercentage;

    /**
     * Session bean for the {@link Assignment} entity class
     */
    @EJB
    private AssignmentFacadeLocal assignmentFacade;
    /**
     * Session bean for the {@link Problem} entity class
     */
    @EJB
    private ProblemFacadeLocal problemFacade;
    /**
     * Session bean for the {@link Answer} entity class
     */
    @EJB
    private AnswerFacadeLocal answerFacade;

    /**
     * List of ExamProblemBeans
     */
    @Inject
    @ExamProblemBeanList
    private ArrayList<ExamProblemBean> examProblemBeanList;

    /**
     * Backing bean for exam problems and student answers
     */
    @Inject
    private Instance<ExamProblemBean> examProblemBeanInstance;

    @Inject
    private StudentAssignmentsBean StudentAssignmentsBean;

    /**
     * Initialize fields requiring dependency injection.
     */
    @PostConstruct
    public void init() {

        try {
            assignment = assignmentFacade.find(StudentAssignmentsBean.getSelectedAssignment().getId());
            problems = problemFacade.findByAssignment(assignment);
        } catch (Throwable t) {
            destroy();
            Utilities.fatalError(t);
            return;
        }
        // If there are problems in the problem table for this assignment, get ExamProblemBeans for each problem,
        // initialize the beans, and add them to the ExamProblemBeanList.
        if (!problems.isEmpty()) {
            for (Problem problem : problems) {
                ExamProblemBean examProblemBean = examProblemBeanInstance.get();
                examProblemBean.setBean(problem);
                examProblemBeanList.add(examProblemBean);
            }
        }
    }

    /**
     * Get the value of assignment.
     *
     * @return the value of assignment
     */
    public Assignment getAssignment() {return assignment;}

    /**
     * Set the value of assignment.
     *
     * @param assignment new value of assignment
     */
    public void setAssignment(Assignment assignment) {this.assignment = assignment;}

    /**
     * Get the value of problems.
     *
     * @return the value of problems
     */
    public List<Problem> getProblems() {return problems;}

    /**
     * Get the value of problemPoints.
     *
     * @return the value of problemPoints
     */
    public int getProblemPoints() {return problemPoints;}

    /**
     * Get the value of examProblemBeanList.
     *
     * @return the value of examProblemBeanList
     */
    public ArrayList<ExamProblemBean> getExamProblemBeanList() {return examProblemBeanList;}

    /**
     * Get the value of totalScore.
     *
     * @return the value of totalScore
     */
    public String getTotalScore() {

        double score = 0;
        double total = 0;
        // Get the possible points and earned points for all the problems.
        for (ExamProblemBean e : examProblemBeanList) {
            total += e.getProblemPoints();
            score += Double.parseDouble(e.getPointsEarned());
        }

        // Create a string to display the values.
        String s = "";
        if (score % 1 == 0)
            s += (int) score;
        else
            s += score;
        s += " out of ";
        if (total % 1 == 0)
            s += (int) total;
        else
            s += total;
        return s;
    }

    /**
     * Get the value of totalScore.
     *
     * @return the value of totalScore
     */
    public String getTotalPercentage() {

        double score = 0;
        double total = 0;
        // Get the possible points and earned points for all the problems.
        for (ExamProblemBean e : examProblemBeanList) {
            total += e.getProblemPoints();
            score += Double.parseDouble(e.getPointsEarned());
        }
        // Create a string to display the percentage score.
        String s = "Total Score: ";
        if (score % 1 == 0)
            s += (int) (score / total * 100);
        else
            s += (score / total * 100);
        s += "%";
        return s;
    }

    /**
     * Before this bean is destroyed, destroy the ExamProblemBeans in the examProblemBeanList.
     */
    @PreDestroy
    public void destroy() {

        for (ExamProblemBean examProblemBean : examProblemBeanList) {
            examProblemBeanInstance.destroy(examProblemBean);
        }
    }
}