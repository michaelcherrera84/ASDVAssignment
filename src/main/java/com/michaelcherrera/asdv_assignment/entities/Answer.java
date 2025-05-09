package com.michaelcherrera.asdv_assignment.entities;

import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serializable;

/**
 * The {@code Answer} class is an entity class representing a record in Answer table.
 *
 * @author michaelcherrera
 */
@Entity
@Table(name = "answer")
@XmlRootElement
@NamedQueries({
        @NamedQuery(name = "Answer.findAll", query = "SELECT a FROM Answer a"),
        @NamedQuery(name = "Answer.findByUser", query = "SELECT a FROM Answer a WHERE a.answerPK.user = :user"),
        @NamedQuery(name = "Answer.findByAssignment", query = "SELECT a FROM Answer a WHERE a.answerPK.assignment = " +
                ":assignment"),
        @NamedQuery(name = "Answer.findByProblem", query = "SELECT a FROM Answer a WHERE a.answerPK.problem = " +
                ":problem"),
        @NamedQuery(name = "Answer.findByAnswer", query = "SELECT a FROM Answer a WHERE a.answer = :answer"),
        @NamedQuery(name = "Answer.findByCorrect", query = "SELECT a FROM Answer a WHERE a.correct = :correct"),
        @NamedQuery(name = "Answer.findByIsComplete", query = "SELECT a FROM Answer a WHERE a.isComplete = " +
                ":isComplete"),
        @NamedQuery(name = "Answer.findByKey", query = "SELECT a FROM Answer a WHERE a.answerPK.user = :user AND a" +
                ".answerPK.assignment = :assignment AND a.answerPK.problem = :problem")})
public class Answer implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected AnswerPK answerPK;
    @Column(name = "answer")
    private Integer answer;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to
    // enforce field validation
    @Column(name = "correct")
    private Double correct;
    @Column(name = "isComplete")
    private Short isComplete;
    @JoinColumn(name = "assignment", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Assignment assignment1;
    @JoinColumn(name = "problem", referencedColumnName = "name", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Problem problem1;
    @JoinColumn(name = "user", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private User user1;

    public Answer() {

    }

    public Answer(AnswerPK answerPK) {

        this.answerPK = answerPK;
    }

    public Answer(int user, int assignment, String problem) {

        this.answerPK = new AnswerPK(user, assignment, problem);
    }

    public AnswerPK getAnswerPK() {

        return answerPK;
    }

    public void setAnswerPK(AnswerPK answerPK) {

        this.answerPK = answerPK;
    }

    public Integer getAnswer() {

        return answer;
    }

    public void setAnswer(Integer answer) {

        this.answer = answer;
    }

    public Double getCorrect() {

        return correct;
    }

    public void setCorrect(Double correct) {

        this.correct = correct;
    }

    public Short getIsComplete() {

        return isComplete;
    }

    public void setIsComplete(Short isComplete) {

        this.isComplete = isComplete;
    }

    public Assignment getAssignment1() {

        return assignment1;
    }

    public void setAssignment1(Assignment assignment1) {

        this.assignment1 = assignment1;
    }

    public Problem getProblem1() {

        return problem1;
    }

    public void setProblem1(Problem problem1) {

        this.problem1 = problem1;
    }

    public User getUser1() {

        return user1;
    }

    public void setUser1(User user1) {

        this.user1 = user1;
    }

    @Override
    public int hashCode() {

        int hash = 0;
        hash += (answerPK != null ? answerPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Answer))
            return false;
        Answer other = (Answer) object;
        if ((this.answerPK == null && other.answerPK != null) ||
                (this.answerPK != null && !this.answerPK.equals(other.answerPK)))
            return false;
        return true;
    }

    @Override
    public String toString() {

        return "com.michaelcherrera.asdv_assignment.entities.Answer[ answerPK=" + answerPK + " ]";
    }

}
