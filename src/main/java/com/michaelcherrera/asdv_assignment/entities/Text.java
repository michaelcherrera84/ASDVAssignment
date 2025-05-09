package com.michaelcherrera.asdv_assignment.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * The {@code Text} class is an entity class representing a record in Text table.
 *
 * @author michaelcherrera
 */
@SuppressWarnings("unused")
@Entity
@Table(name = "text")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Text.findAll", query = "SELECT t FROM Text t"),
    @NamedQuery(name = "Text.findById", query = "SELECT t FROM Text t WHERE t.id = :id"),
    @NamedQuery(name = "Text.findByProblem", query = "SELECT t FROM Text t WHERE t.problem = :problem")})
public class Text implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Lob
    @Size(max = 2147483647)
    @Column(name = "text")
    private String text;
    @JoinColumn(name = "problem", referencedColumnName = "name")
    @ManyToOne
    private Problem problem;

    public Text() {
    }

    public Text(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
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
        if (!(object instanceof Text other))
            return false;
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }

    @Override
    public String toString() {
        return "com.michaelcherrera.asdv_assignment.Text[ id=" + id + " ]";
    }
    
}
