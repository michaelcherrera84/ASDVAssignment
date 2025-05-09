package com.michaelcherrera.asdv_assignment.instructor.beans;

import com.michaelcherrera.asdv_assignment.entities.*;
import com.michaelcherrera.asdv_assignment.interfaces.AssignmentFacadeLocal;
import com.michaelcherrera.asdv_assignment.util.Utilities;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.inject.Named;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * The {@code AssignmentsBeans} is the backing bean for instructor-side, assignment creation.
 * <p>
 * Utilizes EJB facades to persist changes to the underlying database.
 */
@Named
@SessionScoped
public class AssignmentsBean implements Serializable {

    /**
     * List of assignments
     */
    List<Assignment> assignments;
    /**
     * Selected assignment
     */
    Assignment selectedAssignment;
    /**
     * A new assignment to be created
     */
    Assignment newAssignment;

    /**
     * Session bean for the assignment entity class
     */
    @EJB
    private AssignmentFacadeLocal assignmentFacade;

    /**
     * Creates a new instance of StudentAssignmentsBean
     */
    public AssignmentsBean() {

        assignments = new ArrayList<>();
        selectedAssignment = new Assignment();
        newAssignment = new Assignment();
    }

    /**
     * Initialize the AssignmentsBean.
     */
    @PostConstruct
    public void init() {

        try {
            assignments = assignmentFacade.findAll();
        } catch (Throwable t) {
            Logger.getLogger(AssignmentsBean.class.getName()).severe(t.getMessage());
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
     * Get the value of newAssignment.
     *
     * @return the value of newAssignment
     */
    public Assignment getNewAssignment() {return newAssignment;}

    /**
     * Set the value of newAssignment.
     *
     * @param newAssignment new value of newAssignment
     */
    public void setNewAssignment(Assignment newAssignment) {this.newAssignment = newAssignment;}

    /**
     * Open the selected assignment for editing.
     *
     * @param assignment the selected assignment to be opened for editing.
     * @return the assignment editor page
     */
    public String openAssignment(Assignment assignment) {

        this.selectedAssignment = assignment;
        return "problems_table?faces-redirect=true";
    }

    /**
     * Create a new assignment.
     */
    public void addAssignment() {

        try {
            assignmentFacade.create(newAssignment);
            // Update the list of assignments.
            assignments = assignmentFacade.findAll();
        } catch (Throwable t) {
            Logger.getLogger(AssignmentsBean.class.getName()).severe(t.getMessage());
            Utilities.addMessage(FacesMessage.SEVERITY_ERROR, "Error creating assignment",
                    "If the problem persists, please contact the administrator.");
        }
    }
}
