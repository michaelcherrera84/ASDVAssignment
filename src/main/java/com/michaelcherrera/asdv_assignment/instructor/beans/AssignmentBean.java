package com.michaelcherrera.asdv_assignment.instructor.beans;

import com.michaelcherrera.asdv_assignment.entities.Assignment;
import com.michaelcherrera.asdv_assignment.entities.Problem;
import com.michaelcherrera.asdv_assignment.interfaces.AssignmentFacadeLocal;
import com.michaelcherrera.asdv_assignment.interfaces.ProblemFacadeLocal;
import com.michaelcherrera.asdv_assignment.instructor.producers.ProblemBeanList;
import com.michaelcherrera.asdv_assignment.util.Utilities;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.ejb.EJB;
import jakarta.enterprise.inject.Instance;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The {@code AssignmentBean} is a CDI bean that serves as the backing bean for the instructor-facing assignment
 * creation page.
 * <p>
 * This bean manages the lifecycle of an assignment, including creating new problems, storing them in a list, and
 * persisting the complete exam.
 * <p>
 * The {@code AssignmentBean} maintains a list of {@code ProblemBean} instances, each representing an individual
 * question exam. Problems can be dynamically added or modified by the instructor, and the entire exam can be saved for
 * later delivery to students.
 */
@Named
@ViewScoped
public class AssignmentBean implements Serializable {

    /**
     * The current assignment
     */
    private Assignment assignment;
    /**
     * The list of problems for the current assignment
     */
    private List<Problem> problems;
    /**
     * The name of an assignment problem
     */
    private String problemName;
    /**
     * The point value of the assignment problem
     */
    private int problemPoints;
    /**
     * Name of a new problem
     */
    private String newProblemName;
    /**
     * Point value of a new problem
     */
    private Integer newProblemPoints;
    /**
     * The bean for the selected problem
     */
    private ProblemBean problemBean;

    /**
     * Session bean for the assignment entity class
     */
    @EJB
    private AssignmentFacadeLocal assignmentFacade;
    /**
     * Session bean for the problem entity class
     */
    @EJB
    private ProblemFacadeLocal problemFacade;

    /**
     * List of ProblemBeans
     */
    @Inject
    @ProblemBeanList
    private ArrayList<ProblemBean> problemBeanList;

    /**
     * Dynamically obtains instances of ProblemBeans
     */
    @Inject
    private Instance<ProblemBean> problemBeanInstance;

    @Inject
    private AssignmentsBean assignmentsBean;

    /**
     * Initialize the AssignmentBean.
     */
    @PostConstruct
    public void init() {

        try {
            assignment = assignmentFacade.find(assignmentsBean.getSelectedAssignment().getId());
            problems = problemFacade.findByAssignment(assignment);
        } catch (Throwable t) {
            destroy();
            Utilities.fatalError(t);
            return;
        }
        // If there are problems in the problem table for this assignment, get ProblemBeans for each problem,
        // initialize the beans, and add them to the ProblemBeanList.
        if (!problems.isEmpty()) {
            for (Problem problem : problems) {
                ProblemBean problemBean = problemBeanInstance.get();
                problemBean.setBean(problem);
                problemBeanList.add(problemBean);
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
     * Get the value of problemName.
     *
     * @return the value of problemName
     */
    public String getProblemName() {return problemName;}

    /**
     * Set the value of problemName.
     *
     * @param problemName new value of problemName
     */
    public void setProblemName(String problemName) {this.problemName = problemName;}

    /**
     * Get the value of problemPoints.
     *
     * @return the value of problemPoints
     */
    public int getProblemPoints() {return problemPoints;}

    /**
     * Set the value of problemPoints.
     *
     * @param problemPoints new value of problemPoints
     */
    public void setProblemPoints(int problemPoints) {this.problemPoints = problemPoints;}

    /**
     * Get the value of newProblemName.
     *
     * @return the value of newProblemName
     */
    public String getNewProblemName() {return newProblemName;}

    /**
     * Set the value of newProblemName.
     *
     * @param newProblemName new value of newProblemName
     */
    public void setNewProblemName(String newProblemName) {this.newProblemName = newProblemName;}

    /**
     * Get the value of newProblemPoints.
     *
     * @return the value of newProblemPoints
     */
    public Integer getNewProblemPoints() {return newProblemPoints;}

    /**
     * Set the value of newProblemPoints.
     *
     * @param newProblemPoints new value of newProblemPoints
     */
    public void setNewProblemPoints(Integer newProblemPoints) {this.newProblemPoints = newProblemPoints;}

    /**
     * Get the value of problems.
     *
     * @return the value of problems
     */
    public List<Problem> getProblems() {return problems;}

    /**
     * Set the value of problems.
     *
     * @param problems new value of problems
     */
    public void setProblems(List<Problem> problems) {this.problems = problems;}

    /**
     * Get the value of problemBeanList.
     *
     * @return the value of problemBeanList
     */
    public ArrayList<ProblemBean> getProblemBeanList() {return problemBeanList;}

    /**
     * Set the value of problemBean.
     *
     * @param problemBean new value of problemBean
     */
    public void setProblemBean(ProblemBean problemBean) {this.problemBean = problemBean;}

    /**
     * Get the value of problemBean.
     *
     * @return the value of problemBean
     */
    public ProblemBean getProblemBean() {return problemBean;}

    /**
     * Add a new problem to the assignment.
     */
    public void addProblem() throws IOException {

        try {
            if (problemFacade.findByName(newProblemName) != null) {
                Utilities.addMessage(FacesMessage.SEVERITY_ERROR, "Duplicate Problem",
                        "Multiple problems may not have the same name. Please try again.");
                newProblemName = null;
                newProblemPoints = null;
                return;
            }
            // Create a new problem for this assignment and add the problem to the problem table.
            Problem newProblem = new Problem();
            newProblem.setAssignment(assignment);
            newProblem.setName(newProblemName);
            newProblem.setPoints(newProblemPoints);
            problemFacade.create(newProblem);
            newProblem = problemFacade.findByName(newProblemName);

            // Create a new ProblemBean for this problem and add the bean to the ProblemBeanList.
            ProblemBean newProblemBean = problemBeanInstance.get();
            problemBeanList.add(newProblemBean);
            newProblemBean.setBean(newProblem);
            problems = problemFacade.findAll();

            // Reset the values of the new problem Inplace.
            newProblemName = null;
            newProblemPoints = null;
        } catch (Throwable t) {
            destroy();
            Utilities.fatalError(t);
        }
    }

    /**
     * Navigate to the editor for this problem.
     *
     * @param problemBean the CDI bean for the selected problem
     * @return the outcome of the navigation
     */
    public String viewProblem(ProblemBean problemBean) {

        this.problemBean = problemBean;
        return "instructor/problem_editor?faces-redirect=true";
    }

    /**
     * Save all current edits for the exam.
     */
    public void saveExam() {

        for (ProblemBean problemBean : problemBeanList) {
            problemBean.saveProblem();
        }
    }

    /**
     * Remove a problem from the problem table.
     *
     * @param problemBean the ProblemBean for the problem to be removed
     */
    public void removeProblem(ProblemBean problemBean) {

        try {
            // Remove the problem from the problem table.
            // This will also remove any files, text, or answers associated with the problem.
            problemFacade.remove(problemBean.getProblem());
            // Destroy the bean and remove its element from the problemBeanList.
            problemBeanInstance.destroy(problemBean);
            problemBeanList.remove(problemBean);
            // Reset the list of problems.
            problems = problemFacade.findAll();
        } catch (Throwable t) {
            Utilities.addMessage(FacesMessage.SEVERITY_ERROR, "Error removing problem. Please try again.",
                    "If the problem persists, contact an administrator.");
            Logger.getLogger(AssignmentBean.class.getName()).log(Level.SEVERE, t.getMessage(), t);
        }
    }

    /**
     * Before this bean is destroyed, destroy the ProblemBeans in the problemBeanList.
     */
    @PreDestroy
    public void destroy() {

        for (ProblemBean problemBean : problemBeanList) {
            problemBeanInstance.destroy(problemBean);
        }
    }
}
