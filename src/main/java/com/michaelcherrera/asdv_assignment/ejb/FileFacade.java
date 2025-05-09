package com.michaelcherrera.asdv_assignment.ejb;

import com.michaelcherrera.asdv_assignment.entities.Problem;
import com.michaelcherrera.asdv_assignment.interfaces.FileFacadeLocal;
import com.michaelcherrera.asdv_assignment.entities.File;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.primefaces.model.file.UploadedFile;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * {@code @Stateless} session bean handling persistence logic for {@link File} entities.
 *
 * @author michaelcherrera
 */
@SuppressWarnings({"unused", "unchecked"})
@Stateless
public class FileFacade extends AbstractFacade<File> implements FileFacadeLocal {

    @PersistenceContext(unitName = "my_persistence_unit")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {return em;}

    public FileFacade() {super(File.class);}

    /**
     * Find a {@link File} in the file table by filename.
     *
     * @param filename the filename
     * @return the File
     */
    public File findByName(String filename) {

        Query query = em.createNamedQuery("File.findByName");
        query.setParameter("name", filename);
        return (File) query.getSingleResult();
    }

    /**
     * Return a list of all filenames.
     *
     * @return a list of all filenames
     */
    @Override
    public List<String> findAllNames() {

        Query query = em.createNamedQuery("File.findAllNames");
        return (List<String>) query.getResultList();
    }

    /**
     * Find all filenames for a given problem.
     *
     * @param problem the problem
     * @return a list of all filesname for the problem
     */
    @Override
    public List<String> findAllNamesByProblem(Problem problem) {

        Query query = em.createNamedQuery("File.findAllNamesByProblem");
        query.setParameter("problem", problem);
        return (List<String>) query.getResultList();
    }

    /**
     * Upload a {@link File} to the file table.
     *
     * @param file         the File
     * @param uploadedFile a file from a PrimeFaces component
     * @return the row count for the query
     * @throws IOException  if an I/O exception of some sort has occurred
     * @throws SQLException if a database access error has occurred.
     */
    public int create(File file, UploadedFile uploadedFile) throws IOException, SQLException {

        String sql = "INSERT INTO file (id, name, extension, size, data, problem) VALUES (DEFAULT, ?, ?, ?, ?, ?)";

        try (InputStream input = uploadedFile.getInputStream();
             Connection conn = em.unwrap(Connection.class);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, file.getName());
            ps.setString(2, file.getExtension());
            ps.setLong(3, file.getSize());
            ps.setBlob(4, uploadedFile.getInputStream());
            ps.setString(5, file.getProblem().getName());
            return ps.executeUpdate();
        }
    }
}
