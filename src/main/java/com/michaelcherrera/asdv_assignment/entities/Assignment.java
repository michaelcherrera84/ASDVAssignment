package com.michaelcherrera.asdv_assignment.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.Collection;

/**
 * The {@code Assignment} class is an entity class representing a record in Assignment table.
 *
 * @author michaelcherrera
 */
@SuppressWarnings("unused")
@Entity
@Table(name = "assignment")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Assignment.findAll", query = "SELECT a FROM Assignment a"),
    @NamedQuery(name = "Assignment.findById", query = "SELECT a FROM Assignment a WHERE a.id = :id"),
    @NamedQuery(name = "Assignment.findByName", query = "SELECT a FROM Assignment a WHERE a.name = :name")})
public class Assignment implements Serializable {

    @Size(max = 100)
    @Column(name = "name")
    private String name;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @OneToMany(mappedBy = "assignment")
    private Collection<Problem> problemCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "assignment1")
    private Collection<Answer> answerCollection;

    public Assignment() {
    }

    public Assignment(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    @XmlTransient
    public Collection<Problem> getProblemCollection() {
        return problemCollection;
    }

    public void setProblemCollection(Collection<Problem> problemCollection) {
        this.problemCollection = problemCollection;
    }

    @XmlTransient
    public Collection<Answer> getAnswerCollection() {
        return answerCollection;
    }

    public void setAnswerCollection(Collection<Answer> answerCollection) {
        this.answerCollection = answerCollection;
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
        if (!(object instanceof Assignment other))
            return false;
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }

    @Override
    public String toString() {
        return "com.michaelcherrera.asdv_assignment.Assignment[ id=" + id + " ]";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
}
