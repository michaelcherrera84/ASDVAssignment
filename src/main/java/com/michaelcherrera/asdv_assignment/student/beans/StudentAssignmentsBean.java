package com.michaelcherrera.asdv_assignment.student.beans;

import com.michaelcherrera.asdv_assignment.entities.Assignment;
import com.michaelcherrera.asdv_assignment.interfaces.AssignmentFacadeLocal;
import com.michaelcherrera.asdv_assignment.util.Utilities;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.inject.Named;
import jakarta.enterprise.context.SessionScoped;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * The {@code StudentAssignmentsBeans} is the backing bean for student-side, assignment selection.
 *
 * @author Michael C. Herrera
 */
@Named
@SessionScoped
public class StudentAssignmentsBean implements Serializable {

    /**
     * List of assignments
     */
    List<Assignment> assignments;
    /**
     * Selected assignment
     */
    Assignment selectedAssignment;

    /**
     * Session bean for the assignment entity class
     */
    @EJB
    private AssignmentFacadeLocal assignmentFacade;

    /**
     * Creates a new instance of StudentAssignmentsBean
     */
    public StudentAssignmentsBean() {

        assignments = new ArrayList<>();
        selectedAssignment = new Assignment();
    }

    /**
     * Initialize the AssignmentsBean.
     */
    @PostConstruct
    public void init() {

        try {
            assignments = assignmentFacade.findAll();
        } catch (Throwable t) {
            Logger.getLogger(StudentAssignmentsBean.class.getName()).severe(t.getMessage());
            Utilities.fatalError(t);
        }
    }

    /**
     * Get the value of assignments.
     *
     * @return the value of assignments
     */
    public List<Assignment> getAssignments() {return assignments;}

    /**
     * Get the value of selectedAssignment.
     *
     * @return the value of selectedAssignment
     */
    public Assignment getSelectedAssignment() {return selectedAssignment;}

    /**
     * Open the selected assignment for editing.
     *
     * @param assignment the selected assignment to be opened for editing.
     * @return the assignment editor page
     */
    public String openAssignment(Assignment assignment) {

        this.selectedAssignment = assignment;
        return "exam?faces-redirect=true";
    }
}
