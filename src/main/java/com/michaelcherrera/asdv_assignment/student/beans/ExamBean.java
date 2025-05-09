package com.michaelcherrera.asdv_assignment.student.beans;

import com.michaelcherrera.asdv_assignment.entities.*;
import com.michaelcherrera.asdv_assignment.interfaces.AnswerFacadeLocal;
import com.michaelcherrera.asdv_assignment.interfaces.AssignmentFacadeLocal;
import com.michaelcherrera.asdv_assignment.interfaces.ProblemFacadeLocal;
import com.michaelcherrera.asdv_assignment.student.producers.ExamProblemBeanList;
import com.michaelcherrera.asdv_assignment.util.Utilities;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.ejb.EJB;
import jakarta.enterprise.inject.Instance;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DialogFrameworkOptions;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * The {@code ExamBean} is a CDI bean that serves as the backing bean for the student-facing assignment completion
 * page.
 * <p>
 * The {@code ExamBean} maintains a list of {@code ExamProblemBean} instances, each representing an individual exam
 * question.
 *
 * @author Michael C. Herrera
 */
@SuppressWarnings("unused")
@Named
@ViewScoped
public class ExamBean implements Serializable {

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

        // If the exam is complete, redirect to the exam result page.
        try {
            if (examProblemBeanList.get(0).isCompleted())
                FacesContext.getCurrentInstance().getExternalContext().redirect("exam_result.xhtml");
        } catch (IOException e) {
            ((HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false)).invalidate();
            PrimeFaces.current().executeScript("location.href='index.xhtml'");
            Logger.getLogger(ExamBean.class.getName()).warning(e.getMessage());
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
     * Submit the Assignment.
     */
    public String saveExam() {

        boolean isCompleted = true;
        boolean isEdit = false;
        for (ExamProblemBean examProblemBean : examProblemBeanList) {
            examProblemBean.onSubmit();
            // Check for unsaved answers or unanswered questions.
            if (examProblemBean.getCorrectAnswer().equals("null")) {
                isCompleted = false;
                break;
            }
            if (examProblemBean.isAnswerChanged()) {
                isEdit = true;
            }
        }
        // If there are unsaved answers or unanswered questions, confirm the submission with a dialog. Others,
        // navigate to the results page.
        if (!isCompleted) {
            confirmSubmit(0);
            return "";
        } else if (isEdit) {
            confirmSubmit(1);
            return "";
        } else
            return "exam_result?faces-redirect=true";
    }

    /**
     * Show a dynamic dialog to confirm submission.
     */
    private void confirmSubmit(int i) {

        Map<String, List<String>> params = new HashMap();
        List<String> list = new ArrayList<>();

        // Build the dynamic confirmation dialog.
        DialogFrameworkOptions options = DialogFrameworkOptions.builder()
                .resizeObserver(true)
                .resizeObserverCenter(true)
                .closable(false)
                .draggable(false)
                .modal(true)
                .includeViewParams(true)
                .build();
        // If i is 0, the exam isn't complete. Pass a parameter to the dialog to indicate an incomplete exam so that
        // a case-specific message is displayed.
        if (i == 0) {
            list.add("false");
            params.put("complete", list);
            PrimeFaces.current().dialog().openDynamic("/templates/submit_exam_dialog", options, params);
        }
        // A different message is displayed if an answer has been changed but all answers have been
        // previously saved.
        else {
            list.add("true");
            params.put("complete", list);
            PrimeFaces.current().dialog().openDynamic("/templates/submit_exam_dialog", options, params);
        }
    }

    /**
     * Close the confirmation dialog.
     *
     * @param resultCode integer indicating which option was selected
     */
    public void dialogResult(int resultCode) {

        if (resultCode == 1)
            PrimeFaces.current().dialog().closeDynamic(1);
        else
            PrimeFaces.current().dialog().closeDynamic(2);
    }

    /**
     * Process the confirmation dialog selection.
     *
     * @param event the selected option
     */
    public void onConfirmSubmit(SelectEvent<Integer> event) {

        // If the 2nd button is pressed, traverse the list of beans and save the answers. The saveAnswer() method
        // will not be executed if there are no unsaved answers.
        if (event.getObject() == 2) {
            for (ExamProblemBean examProblemBean : examProblemBeanList) {
                if (examProblemBean.isAnswerChanged()) {
                    examProblemBean.saveAnswer(false);
                }
            }
            try {  // Navigate to the results page.
                FacesContext.getCurrentInstance().getExternalContext().redirect("exam_result.xhtml");
            } catch (IOException ignored) {}
        }
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
