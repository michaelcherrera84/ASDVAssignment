package com.michaelcherrera.asdv_assignment.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;

import java.io.Serializable;
import java.util.Collection;

/**
 * The {@code Problem} class is an entity class representing a record in Problem table.
 *
 * @author michaelcherrera
 */
@SuppressWarnings("unused")
@Entity
@Table(name = "problem")
@XmlRootElement
@NamedQueries({
        @NamedQuery(name = "Problem.findAll", query = "SELECT p FROM Problem p"),
        @NamedQuery(name = "Problem.findByNum", query = "SELECT p FROM Problem p WHERE p.num = :num"),
        @NamedQuery(name = "Problem.findByName", query = "SELECT p FROM Problem p WHERE p.name = :name"),
        @NamedQuery(name = "Problem.findByPoints", query = "SELECT p FROM Problem p WHERE p.points = :points"),
        @NamedQuery(name = "Problem.findByAssignment", query = "SELECT p FROM Problem p WHERE p.assignment = " +
                ":assignment")})
public class Problem implements Serializable {

    @Basic(optional = false)
    @Column(name = "num")
    private int num;

    private static final long serialVersionUID = 1L;

    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "name")
    private String name;

    @Column(name = "points")
    private Integer points;

    @OneToMany(mappedBy = "problem")
    private Collection<MultipleChoice> multipleChoiceCollection;

    @OneToMany(mappedBy = "problem")
    private Collection<File> fileCollection;

    @JoinColumn(name = "assignment", referencedColumnName = "id")
    @ManyToOne
    private Assignment assignment;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "problem1")
    private Collection<Answer> answerCollection;

    @OneToMany(mappedBy = "problem")
    private Collection<Text> textCollection;

    public Problem() {}

    public Problem(String name) {this.name = name;}


    public String getName() {return name;}

    public void setName(String name) {this.name = name;}

    public Integer getPoints() {return points;}

    public void setPoints(Integer points) {this.points = points;}

    @XmlTransient
    public Collection<MultipleChoice> getMultipleChoiceCollection() {

        return multipleChoiceCollection;
    }

    public void setMultipleChoiceCollection(Collection<MultipleChoice> multipleChoiceCollection) {

        this.multipleChoiceCollection = multipleChoiceCollection;
    }

    @XmlTransient
    public Collection<File> getFileCollection() {return fileCollection;}

    public void setFileCollection(Collection<File> fileCollection) {

        this.fileCollection = fileCollection;
    }

    public Assignment getAssignment() {return assignment;}

    public void setAssignment(Assignment assignment) {this.assignment = assignment;}

    @XmlTransient
    public Collection<Answer> getAnswerCollection() {return answerCollection;}

    public void setAnswerCollection(Collection<Answer> answerCollection) {

        this.answerCollection = answerCollection;
    }

    @XmlTransient
    public Collection<Text> getTextCollection() {return textCollection;}

    public void setTextCollection(Collection<Text> textCollection) {

        this.textCollection = textCollection;
    }

    @Override
    public int hashCode() {

        int hash = 0;
        hash += (name != null ? name.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Problem other))
            return false;
        return (this.name != null || other.name == null) && (this.name == null || this.name.equals(other.name));
    }

    @Override
    public String toString() {

        return "com.michaelcherrera.asdv_assignment.Problem[ name=" + name + " ]";
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

}
