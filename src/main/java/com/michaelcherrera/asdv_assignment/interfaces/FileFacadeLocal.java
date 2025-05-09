package com.michaelcherrera.asdv_assignment.interfaces;

import com.michaelcherrera.asdv_assignment.entities.File;
import com.michaelcherrera.asdv_assignment.entities.Problem;
import jakarta.ejb.Local;
import org.primefaces.model.file.UploadedFile;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Local EJB interface providing CRUD operations for {@link File} entities.
 *
 * @author michaelcherrera
 */
@SuppressWarnings("unused")
@Local
public interface FileFacadeLocal {

    void create(File file);

    /**
     * Upload a {@link File} to the file table.
     *
     * @param file         the File
     * @param uploadedFile a file from a PrimeFaces component
     * @return the row count for the query
     * @throws IOException  if an I/O exception of some sort has occurred
     * @throws SQLException if a database access error has occurred.
     */
    int create(File file, UploadedFile uploadedFile) throws IOException, SQLException;

    void edit(File file);

    void remove(File file);

    File find(Object id);

    /**
     * Find a {@link File} in the file table by filename.
     *
     * @param filename the filename
     * @return the File
     */
    File findByName(String filename);

    /**
     * Return a list of all filenames.
     *
     * @return a list of all filenames
     */
    List<String> findAllNames();

    /**
     * Find all filenames for a given problem.
     *
     * @param problem the problem
     * @return a list of all filesname for the problem
     */
    List<String> findAllNamesByProblem(Problem problem);

    List<File> findAll();

    List<File> findRange(int[] range);

    int count();
}
