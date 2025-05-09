package com.michaelcherrera.asdv_assignment.instructor.beans;

import com.michaelcherrera.asdv_assignment.entities.File;
import com.michaelcherrera.asdv_assignment.entities.MultipleChoice;
import com.michaelcherrera.asdv_assignment.entities.Problem;
import com.michaelcherrera.asdv_assignment.entities.Text;
import com.michaelcherrera.asdv_assignment.interfaces.FileFacadeLocal;
import com.michaelcherrera.asdv_assignment.interfaces.MultipleChoiceFacadeLocal;
import com.michaelcherrera.asdv_assignment.interfaces.ProblemFacadeLocal;
import com.michaelcherrera.asdv_assignment.interfaces.TextFacadeLocal;
import com.michaelcherrera.asdv_assignment.util.Utilities;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.Dependent;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.AjaxBehaviorEvent;
import jakarta.inject.Named;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.DialogFrameworkOptions;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.file.UploadedFile;
import org.primefaces.util.SerializableSupplier;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The {@code ProblemBean} is the backing bean for instructor-side, problem creation and editing in an ASDV assignment.
 * <p>
 * This bean supports full CRUD functionality for a {@link Problem} entity, including associated {@link Text},
 * {@link MultipleChoice}, and {@link File} records. It also handles UI interaction events via PrimeFaces components.
 * <p>
 * Utilizes EJB facades to persist changes to the underlying database.
 */
@Dependent
@Named
public class ProblemBean implements Serializable {

    /**
     * This problem
     */
    private Problem problem;
    private String problemName;
    private Integer problemPoints;
    /**
     * List of download links for files associated with the Problem
     */
    private List<String> links;
    /**
     * Text for this problem
     */
    private String text;
    /**
     * A file submitted to the PrimeFaces FileUpload component
     */
    private UploadedFile uploadedFile;
    /**
     * The number files submitted to the PrimeFaces FileUpload component
     */
    private static int attemptCount;
    /**
     * The number of files successfully uploaded
     */
    private int successCount;
    /**
     * A file to download
     */
    private File downloadFile;
    /**
     * {@code true} if the problem has been changed
     */
    private boolean isProblemChanged;
    /**
     * {@code true} if the text in the text editor has been changed
     */
    private boolean isTextChanged;
    /**
     * {@code true} if an answer has been changed
     */
    private boolean isAnswerChanged;
    /**
     * Count of files waiting to be uploaded
     */
    private static int filesWaiting;

    /**
     * Answers to the problem
     */
    private String answer1;
    private String answer2;
    private String answer3;
    private String answer4;
    /**
     * The correct answer radio option
     */
    private String correctAnswer;

    /**
     * Database facade for the problem table
     */
    @EJB
    ProblemFacadeLocal problemFacade;
    /**
     * Database facade for the text table
     */
    @EJB
    TextFacadeLocal textFacade;
    /**
     * Database facade for the file table
     */
    @EJB
    FileFacadeLocal fileFacade;
    /**
     * Database facade for the multiple_choice table
     */
    @EJB
    MultipleChoiceFacadeLocal multipleChoiceFacade;

    /**
     * Construct a ProblemBean.
     */
    public ProblemBean() {links = new ArrayList<>();}

    /**
     * Initialize the fields of this bean based on a Problem
     *
     * @param problem the Problem
     */
    public void setBean(Problem problem) {

        try {
            this.problem = problem;
            problemName = problem.getName();
            problemPoints = problem.getPoints();
            links = fileFacade.findAllNamesByProblem(problem);

            Text t = textFacade.findByProblem(problem);
            if (t != null)
                text = t.getText();

            setAnswers();
        } catch (Throwable t) {
            Logger.getLogger(ProblemBean.class.getName()).log(Level.SEVERE, t.getMessage(), t);
            Utilities.fatalError(t);
        }
    }

    /**
     * Get the value of problem.
     *
     * @return the value of problem
     */
    public Problem getProblem() {return problem;}

    /**
     * Set the value of problem.
     *
     * @param problem new value of problem
     */
    public void setProblem(Problem problem) {this.problem = problem;}

    /**
     * Get the value of problemName.
     *
     * @return the value of problemName
     */
    public String getProblemName() {return problemName;}

    /**
     * Set the value of problemName.
     *
     * @param problemName new value of problemName
     */
    public void setProblemName(String problemName) {this.problemName = problemName;}

    /**
     * Get the value of problemPoints.
     *
     * @return the value of problemPoints
     */
    public Integer getProblemPoints() {return problemPoints;}

    /**
     * Set the value of problemPoints.
     *
     * @param problemPoints new value of problemPoints
     */
    public void setProblemPoints(Integer problemPoints) {this.problemPoints = problemPoints;}

    /**
     * Get the value of links.
     *
     * @return the value of links
     */
    public List<String> getLinks() {return links;}

    /**
     * Get the value of text.
     *
     * @return the value of text
     */
    public String getText() {return text;}

    /**
     * Set the value of text.
     *
     * @param text new value of text
     */
    public void setText(String text) {this.text = text;}

    /**
     * Get the value of downloadFile.
     *
     * @return the value of downloadFile
     */
    public File getDownloadFile() {return downloadFile;}

    /**
     * Set the value of downloadFile.
     *
     * @param filename the filename
     */
    public void setDownloadFile(String filename) {

        try {
            this.downloadFile = fileFacade.findByName(filename);
        } catch (Throwable t) {
            Logger.getLogger(ProblemBean.class.getName()).log(Level.SEVERE, t.getMessage(), t);
            Utilities.addMessage(FacesMessage.SEVERITY_ERROR, "Error downloading " + filename,
                    "If the problem persists, please contact the administrator.");
        }
    }

    public void onProblemChanged(AjaxBehaviorEvent event) {isProblemChanged = true;}

    /**
     * Set the value of isTextChanged.
     */
    public void onTextChanged(AjaxBehaviorEvent event) {isTextChanged = true;}

    /**
     * Set the value of isAnswerChanged.
     */
    public void onAnswerChanged() {isAnswerChanged = true;}

    /**
     * Set the value of filesWaiting.
     */
    public void setFilesWaiting() {
        // Get parameters passed from JavaScript to the RemoteCommand.
        Map<String, String> params =
                FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        filesWaiting = Integer.parseInt(params.get("filesWaiting"));
    }

    /**
     * Get the value of answer1.
     *
     * @return the value of answer1
     */
    public String getAnswer1() {return answer1;}

    /**
     * Set the value of answer1.
     *
     * @param answer1 new value of answer1
     */
    public void setAnswer1(String answer1) {this.answer1 = answer1;}

    /**
     * Get the value of answer2.
     *
     * @return the value of answer2
     */
    public String getAnswer2() {return answer2;}

    /**
     * Set the value of answer2.
     *
     * @param answer2 new value of answer2
     */
    public void setAnswer2(String answer2) {this.answer2 = answer2;}

    /**
     * Get the value of answer3.
     *
     * @return the value of answer3
     */
    public String getAnswer3() {return answer3;}

    /**
     * Set the value of answer3.
     *
     * @param answer3 new value of answer3
     */
    public void setAnswer3(String answer3) {this.answer3 = answer3;}

    /**
     * Get the value of answer4.
     *
     * @return the value of answer4
     */
    public String getAnswer4() {return answer4;}

    /**
     * Set the value of answer4.
     *
     * @param answer4 new value of answer4
     */
    public void setAnswer4(String answer4) {this.answer4 = answer4;}

    /**
     * Get the value of correctAnswer.
     *
     * @return the value of correctAnswer
     */
    public String getCorrectAnswer() {return correctAnswer;}

    /**
     * Set the value of correctAnswer.
     *
     * @param correctAnswer new value of correctAnswer
     */
    public void setCorrectAnswer(String correctAnswer) {this.correctAnswer = correctAnswer;}

    /**
     * Set the number of files being uploaded.
     */
    public void setAttemptCount() {

        Map<String, String> request =
                FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        attemptCount = Integer.parseInt(request.get("count"));
    }

    /**
     * Set the multiple choice answers to the values from the multiple_choice table or empty strings if the answers a
     * not yet created.
     */
    private void setAnswers() {

        MultipleChoice multipleChoice = multipleChoiceFacade.findByProbAndNum(problem, 1);
        answer1 = multipleChoice != null ? multipleChoice.getText() : "";
        multipleChoice = multipleChoiceFacade.findByProbAndNum(problem, 2);
        answer2 = multipleChoice != null ? multipleChoice.getText() : "";
        multipleChoice = multipleChoiceFacade.findByProbAndNum(problem, 3);
        answer3 = multipleChoice != null ? multipleChoice.getText() : "";
        multipleChoice = multipleChoiceFacade.findByProbAndNum(problem, 4);
        answer4 = multipleChoice != null ? multipleChoice.getText() : "";
        correctAnswer = multipleChoice != null ? multipleChoice.getAnswer().toString() : "1";
    }

    /**
     * Process and upload the files submitted via the PrimeFaces component.
     * <p>
     * A {@link File} object is uploaded to the database with the file, a unique filename, the associated problem, the
     * file extension (if present), and the file's size in kilobytes.
     * <p>
     * Confirmation of a successful upload is provided via the PrimeFaces Growl component.
     *
     * @param event FileUploadEvent from the PrimeFaces component.
     */
    public void uploadFile(FileUploadEvent event) {

        if (links.size() >= 5) {
            Utilities.addMessage(FacesMessage.SEVERITY_WARN, "File limit exceeded",
                    "You may not upload more than 5 files");
            return;
        }

        uploadedFile = event.getFile();
        try {
            File file = getFile();
            successCount += fileFacade.create(file, uploadedFile);
            // If the number of files uploaded is equal to the number of files submitted via the PrimeFaces
            // component, the upload is successful.
            if (successCount == attemptCount) {
                Utilities.addMessage(FacesMessage.SEVERITY_INFO, "Upload Successful",
                        successCount + " file(s) uploaded");
                // Reset the successful upload count.
                successCount = 0;
                attemptCount = 0;
            }

            // Update the download links and reset the attempted upload count.
            links = fileFacade.findAllNamesByProblem(problem);
        } catch (Throwable e) {
            Utilities.addMessage(FacesMessage.SEVERITY_ERROR, "Error uploading " + event.getFile().getFileName(),
                    "If the problem persists, please contact the administrator.");
            Logger.getLogger(ProblemBean.class.getName()).log(Level.SEVERE, e.getMessage(), e);
        }
    }

    /**
     * Return a File entity for upload.
     *
     * @return a File entity
     */
    private File getFile() {

        File file = new File();
        file.setProblem(problem);
        // If a file already exists with the same name, append a number to the end of the file name.
        String fileName = getFileName();
        file.setName(fileName);
        // If the file has a file extension, set the file extension.
        if (fileName.lastIndexOf(".") != -1)
            file.setExtension(fileName.substring(fileName.lastIndexOf(".") + 1));
        file.setSize(uploadedFile.getSize() / 1024);
        return file;
    }

    /**
     * Get the filename of the current file being uploaded. If a file with the same name already exists, append a
     * counter-number to the filename.
     * <pre><code>
     * Example: "filename.java" would become "filename (1).java"
     * </code></pre>
     *
     * @return a unique filename
     */
    private String getFileName() {

        String filename = uploadedFile.getFileName();

        // If there is a filename found with the same name, add incrementing counter-numbers to the filename until a
        // unique name is created.
        if (checkFilename(filename)) {
            int i = 1;
            if (filename.lastIndexOf(".") != -1)
                filename = filename.substring(0, filename.lastIndexOf("."))
                        .concat(" (" + i++ + ")" + filename.substring(filename.lastIndexOf(".")));
            else  // The file does not have an extension.
                filename = filename + " (" + i++ + ")";
            while (checkFilename(filename))
                if (filename.lastIndexOf(".") != -1)
                    filename = filename.substring(0, filename.lastIndexOf("("))
                            .concat("(" + i++ + ")" + filename.substring(filename.lastIndexOf(".")));
                else  // The file does not have an extension.
                    filename = filename.substring(0, filename.lastIndexOf("(")).concat("(" + i++ + ")");
        }
        return filename;
    }

    /**
     * Check for a preexisting file with the same filename as the current file being uploaded.
     *
     * @param filename filename of the current file being uploaded
     * @return {@code true} if there is a file with the same name or {@code false} if not.
     */
    private boolean checkFilename(String filename) {

        List<String> filenames = fileFacade.findAllNames();
        for (String name : filenames) {
            if (filename.equals(name))
                return true;
        }
        return false;
    }

    /**
     * Remove the selected file from the database.
     *
     * @param filename filename of the file
     */
    public void deleteFile(String filename) {

        try {
            fileFacade.remove(fileFacade.findByName(filename));
            // Reset the download links.
            links = fileFacade.findAllNamesByProblem(problem);
        } catch (Throwable t) {
            Logger.getLogger(ProblemBean.class.getName()).log(Level.SEVERE, t.getMessage(), t);
            Utilities.addMessage(FacesMessage.SEVERITY_ERROR, "Error removing " + filename,
                    "If the problem persists, please contact the administrator.");
        }
    }

    /**
     * Download the requested file.
     *
     * @return the requested file
     */
    public StreamedContent downloadFile() {

        try {
            InputStream inputStream = new ByteArrayInputStream(downloadFile.getData());
            return DefaultStreamedContent.builder().name(downloadFile.getName()).stream(
                    (SerializableSupplier<InputStream>) () -> inputStream).build();
        } catch (Throwable t) {
            Logger.getLogger(ProblemBean.class.getName()).log(Level.SEVERE, t.getMessage(), t);
            return null;
        }
    }

    /**
     * Update the Text record for this problem.
     */
    private void uploadText() {

        // If the text in the editor has not been changed, no update is necessary.
        if (!isTextChanged)
            return;

        // Create a Text entity to upload.
        Text t = new Text();
        t.setProblem(problem);
        t.setText(text);

        try {
            // If the editor is not empty...
            if (!text.isEmpty()) {
                // and there is a Text record in the text table for this problem, update the record.
                if (textFacade.findByProblem(problem) != null) {
                    t.setId(textFacade.findByProblem(problem).getId());
                    textFacade.edit(t);
                } else // and there is not a Text record in the text table for this problem, create a new record.
                    textFacade.create(t);
            } else { // If the edit is empty...
                Text temp = textFacade.findByProblem(problem);
                // and there is a Text record in the text table for this problem, delete the record.
                if (temp != null)
                    textFacade.remove(temp);
            }

            Utilities.addMessage(FacesMessage.SEVERITY_INFO, "Submission Successful", null);

            // Reset after Text record has been updated.
            isTextChanged = false;
        } catch (Throwable e) {
            Logger.getLogger(ProblemBean.class.getName()).log(Level.SEVERE, e.getMessage(), e);
            Utilities.addMessage(FacesMessage.SEVERITY_ERROR, "Upload failed. The text was not saved.",
                    "If the problem persists, please contact the administrator.");
        }
    }

    /**
     * Upload the answers to this problem.
     */
    private void uploadAnswers() {

        try {
            // No update is necessary if answers have not been entered or the correct answer is not selected.
            if (!isAnswerChanged && multipleChoiceFacade.findByProbAndNum(problem, 1) != null &&
                    correctAnswer.equals(multipleChoiceFacade.findByProbAndNum(problem, 1).getAnswer().toString()))
                return;

            // The new MultipleChoice will be uploaded. The temp will be used to determine this transaction is
            // updating a
            // previous record.
            MultipleChoice upload = new MultipleChoice();
            MultipleChoice temp;

            // Upload answer 1.
            upload.setProblem(problem);
            upload.setNum(1);
            upload.setText(answer1);
            upload.setAnswer(Integer.valueOf(correctAnswer));
            temp = multipleChoiceFacade.findByProbAndNum(problem, upload.getNum());
            // If the temp MultipleChoice is not null, then update the record with the new data. Otherwise, create a new
            // record.
            if (temp != null) {
                upload.setId(temp.getId());
                multipleChoiceFacade.edit(upload);
            } else
                multipleChoiceFacade.create(upload);

            // Upload answer 2.
            upload = new MultipleChoice();
            upload.setProblem(problem);
            upload.setNum(2);
            upload.setText(answer2);
            upload.setAnswer(Integer.valueOf(correctAnswer));
            temp = multipleChoiceFacade.findByProbAndNum(problem, upload.getNum());
            if (temp != null) {
                upload.setId(temp.getId());
                multipleChoiceFacade.edit(upload);
            } else
                multipleChoiceFacade.create(upload);

            // Upload answer 3.
            upload = new MultipleChoice();
            upload.setProblem(problem);
            upload.setNum(3);
            upload.setText(answer3);
            upload.setAnswer(Integer.valueOf(correctAnswer));
            temp = multipleChoiceFacade.findByProbAndNum(problem, upload.getNum());
            if (temp != null) {
                upload.setId(temp.getId());
                multipleChoiceFacade.edit(upload);
            } else
                multipleChoiceFacade.create(upload);

            // Upload answer 4.
            upload = new MultipleChoice();
            upload.setProblem(problem);
            upload.setNum(4);
            upload.setText(answer4);
            upload.setAnswer(Integer.valueOf(correctAnswer));
            temp = multipleChoiceFacade.findByProbAndNum(problem, upload.getNum());
            if (temp != null) {
                upload.setId(temp.getId());
                multipleChoiceFacade.edit(upload);
            } else
                multipleChoiceFacade.create(upload);

            Utilities.addMessage(FacesMessage.SEVERITY_INFO, "Answers Saved", null);
            isAnswerChanged = false;
        } catch (Throwable e) {
            Logger.getLogger(ProblemBean.class.getName()).log(Level.SEVERE, e.getMessage(), e);
            Utilities.addMessage(FacesMessage.SEVERITY_ERROR, "Upload failed. The answers were not saved.",
                    "If the problem persists, please contact the administrator.");
        }
    }

    /**
     * Save the current state of the problem.
     */
    public void saveProblem() {

        updateProblem();
        uploadText();
        uploadAnswers();
    }

    /**
     * Update the Problem name and/or points.
     */
    private void updateProblem() {

        try {
            if (!isProblemChanged)
                return;

            // Create a new problem with the new name and points and old num and assignment.
            Problem temp = new Problem();
            temp.setNum(problem.getNum());
            temp.setName(problemName);
            temp.setPoints(problemPoints);
            temp.setAssignment(problem.getAssignment());
            // Since the problem's name is its primary key, both the old and new values are needed for the update.
            problemFacade.update(problem, temp);

            Utilities.addMessage(FacesMessage.SEVERITY_INFO, "Problem Updated", null);

            problem = temp;
            isProblemChanged = false;
        } catch (Throwable e) {
            Logger.getLogger(ProblemBean.class.getName()).log(Level.SEVERE, e.getMessage(), e);
            Utilities.addMessage(FacesMessage.SEVERITY_ERROR, "Update failed. The problem was not updated.",
                    "If the problem persists, please contact the administrator.");
        }
    }

    /**
     * Check if edits have been made in the text editor. If there are unsaved edits, open a dialog to confirm cancel.
     */
    public void cancelEdits() {

        if (filesWaiting != 0 || isTextChanged || isAnswerChanged || isProblemChanged) {
            // Build the dynamic confirmation dialog.
            DialogFrameworkOptions options = DialogFrameworkOptions.builder()
                    .resizeObserver(true)
                    .resizeObserverCenter(true)
                    .closable(false)
                    .draggable(false)
                    .modal(true)
                    .build();
            PrimeFaces.current().dialog().openDynamic("/templates/confirmation_dialog", options, null);
        }
    }

    /**
     * Close the confirmation dialog.
     *
     * @param resultCode integer indicating which option was selected
     */
    public void dialogResult(int resultCode) {

        if (resultCode == 1)
            PrimeFaces.current().dialog().closeDynamic(1);
        else
            PrimeFaces.current().dialog().closeDynamic(2);
    }

    /**
     * Reset the text editor to cancel edits.
     *
     * @param event value from the confirmation dialog
     */
    public void onCancelSubmission(SelectEvent<Integer> event) {

        try {
            // If the event is 1, the cancel is confirmed and the editor is return to its previous state.
            if (event.getObject() == 1) {
                filesWaiting = 0;
                // If there is text for this problem in the text table, set the text in the editor to the text in the
                // table.
                // Otherwise, set the text to an empty String.
                if (textFacade.findByProblem(problem) != null)
                    text = textFacade.findByProblem(problem).getText();
                else text = "";
                // Reset the answers to null or to the values currently in the multiple_choice table.
                setAnswers();
                // Reset the change listeners.
                isProblemChanged = false;
                isTextChanged = false;
                isAnswerChanged = false;
                // Update the editor.
                PrimeFaces.current().ajax().update(Utilities.findComponent("problem_form"));
            }
        } catch (Throwable t) {
            Logger.getLogger(ProblemBean.class.getName()).log(Level.SEVERE, t.getMessage(), t);
            Utilities.fatalError(t);
        }
    }
}
