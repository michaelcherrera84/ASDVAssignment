package com.michaelcherrera.asdv_assignment.entities;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;

/**
 * The {@code AnswerPK} class is an entity class representing the primary key of the Answer table.
 *
 * @author michaelcherrera
 */
@Embeddable
public class AnswerPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "user")
    private int user;
    @Basic(optional = false)
    @NotNull
    @Column(name = "assignment")
    private int assignment;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "problem")
    private String problem;

    public AnswerPK() {
    }

    public AnswerPK(int user, int assignment, String problem) {
        this.user = user;
        this.assignment = assignment;
        this.problem = problem;
    }

    public int getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user = user;
    }

    public int getAssignment() {
        return assignment;
    }

    public void setAssignment(int assignment) {
        this.assignment = assignment;
    }

    public String getProblem() {
        return problem;
    }

    public void setProblem(String problem) {
        this.problem = problem;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) user;
        hash += (int) assignment;
        hash += (problem != null ? problem.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AnswerPK))
            return false;
        AnswerPK other = (AnswerPK) object;
        if (this.user != other.user)
            return false;
        if (this.assignment != other.assignment)
            return false;
        if ((this.problem == null && other.problem != null) || (this.problem != null && !this.problem.equals(other.problem)))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "com.michaelcherrera.asdv_assignment.entities.AnswerPK[ user=" + user + ", assignment=" + assignment + ", problem=" + problem + " ]";
    }
    
}
