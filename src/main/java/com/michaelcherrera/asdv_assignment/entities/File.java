package com.michaelcherrera.asdv_assignment.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * The {@code File} class is an entity class representing a record in File table.
 *
 * @author michaelcherrera
 */
@SuppressWarnings("unused")
@Entity
@Table(name = "file")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "File.findAll", query = "SELECT f FROM File f"),
    @NamedQuery(name = "File.findById", query = "SELECT f FROM File f WHERE f.id = :id"),
    @NamedQuery(name = "File.findByName", query = "SELECT f FROM File f WHERE f.name = :name"),
    @NamedQuery(name = "File.findAllNames", query = "SELECT f.name FROM File f"),
    @NamedQuery(name = "File.findAllNamesByProblem", query = "SELECT f.name FROM File f WHERE f.problem = :problem"),
    @NamedQuery(name = "File.findByExtension", query = "SELECT f FROM File f WHERE f.extension = :extension"),
    @NamedQuery(name = "File.findBySize", query = "SELECT f FROM File f WHERE f.size = :size")})
public class File implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 1000)
    @Column(name = "name")
    private String name;
    @Size(max = 100)
    @Column(name = "extension")
    private String extension;
    @Column(name = "size")
    private Long size;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Column(name = "data")
    private byte[] data;
    @JoinColumn(name = "problem", referencedColumnName = "name")
    @ManyToOne
    private Problem problem;

    public File() {
    }

    public File(Integer id) {
        this.id = id;
    }

    public File(Integer id, byte[] data) {
        this.id = id;
        this.data = data;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
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
        if (!(object instanceof File other))
            return false;
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }

    @Override
    public String toString() {
        return "com.michaelcherrera.asdv_assignment.File[ id=" + id + " ]";
    }
    
}
