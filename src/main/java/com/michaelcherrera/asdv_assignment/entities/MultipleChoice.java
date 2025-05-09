package com.michaelcherrera.asdv_assignment.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * The {@code MultipleChoice} class is an entity class representing a record in MultipleChoice table.
 *
 * @author michaelcherrera
 */
@SuppressWarnings("unused")
@Entity
@Table(name = "multiple_choice")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MultipleChoice.findAll", query = "SELECT m FROM MultipleChoice m"),
    @NamedQuery(name = "MultipleChoice.findById", query = "SELECT m FROM MultipleChoice m WHERE m.id = :id"),
    @NamedQuery(name = "MultipleChoice.findByNum", query = "SELECT m FROM MultipleChoice m WHERE m.num = :num"),
    @NamedQuery(name = "MultipleChoice.findByText", query = "SELECT m FROM MultipleChoice m WHERE m.text = :text"),
    @NamedQuery(name = "MultipleChoice.findByAnswer", query = "SELECT m FROM MultipleChoice m WHERE m.answer = :answer"),
    @NamedQuery(name = "MultipleChoice.findByProbAndNum", query = "SELECT m FROM MultipleChoice m WHERE m.problem = " +
            ":problem AND m.num = :num")})
public class MultipleChoice implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "num")
    private Integer num;
    @Size(max = 1000)
    @Column(name = "text")
    private String text;
    @Column(name = "answer")
    private Integer answer;
    @JoinColumn(name = "problem", referencedColumnName = "name")
    @ManyToOne
    private Problem problem;

    public MultipleChoice() {
    }

    public MultipleChoice(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getAnswer() {
        return answer;
    }

    public void setAnswer(Integer answer) {
        this.answer = answer;
    }

    public Problem getProblem() {
        return problem;
    }

    public void setProblem(Problem problem) {
        this.problem = problem;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MultipleChoice other))
            return false;
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }

    @Override
    public String toString() {
        return "com.michaelcherrera.asdv_assignment.MultipleChoice[ id=" + id + " ]";
    }
    
}
