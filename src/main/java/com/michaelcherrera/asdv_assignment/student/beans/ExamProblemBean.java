package com.michaelcherrera.asdv_assignment.student.beans;

import com.michaelcherrera.asdv_assignment.entities.*;
import com.michaelcherrera.asdv_assignment.instructor.beans.ProblemBean;
import com.michaelcherrera.asdv_assignment.interfaces.*;
import com.michaelcherrera.asdv_assignment.util.Utilities;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.Dependent;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.AjaxBehaviorEvent;
import jakarta.inject.Named;
import org.primefaces.PrimeFaces;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.file.UploadedFile;
import org.primefaces.util.SerializableSupplier;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The {@code ExamProblemBean} is the backing bean for exam problems and student answers.
 * <p>
 * This bean supports full CRUD functionality for a {@link Answer} entity.
 * <p>
 * Utilizes EJB facades to persist changes to the underlying database.
 *
 * @author Michael C. Herrera
 */
@Dependent
@Named
public class ExamProblemBean implements Serializable {

    /**
     * The current user
     */
    User user;
    /**
     * This problem
     */
    private Problem problem;
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
     * The points earned for this problem.
     */
    private String pointsEarned;
    /**
     * The correct answer provided by the instructor for this problem
     */
    private String multipleChoiceAnswer;
    /**
     * True if this exam is already completed.
     */
    private boolean isCompleted;

    /**
     * Database facade for the user table
     */
    @EJB
    UserFacadeLocal userFacade;
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
     * Database facade for the answer table
     */
    @EJB
    AnswerFacadeLocal answerFacade;

    /**
     * Construct an ExamProblemBean.
     */
    public ExamProblemBean() {links = new ArrayList<>();}

    /**
     * Set the values of this bean based on the specified problem.
     *
     * @param problem the problem
     */
    public void setBean(Problem problem) {

        try {
            this.problem = problem;
            problemPoints = problem.getPoints();
            links = fileFacade.findAllNamesByProblem(problem);

            Text t = textFacade.findByProblem(problem);
            if (t != null)
                text = t.getText();

            // Set the multiple choice answers for the problem.
            setMultipleChoice();
            user = (User) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user");
            // Initialize all the answers to null.
            setAnswers();
        } catch (Throwable t) {
            Logger.getLogger(ExamProblemBean.class.getName()).log(Level.SEVERE, t.getMessage(), t);
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
     * Get the value of problemPoints.
     *
     * @return the value of problemPoints
     */
    public Integer getProblemPoints() {return problemPoints;}

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
            Logger.getLogger(ExamProblemBean.class.getName()).log(Level.SEVERE, t.getMessage(), t);
            Utilities.addMessage(FacesMessage.SEVERITY_ERROR, "Error downloading " + filename,
                    "If the problem persists, please contact the administrator.");
        }
    }

    /**
     * Get the value of isAnswerChanged.
     *
     * @return the value of isAnswerChanged
     */
    public boolean isAnswerChanged() {return isAnswerChanged;}

    /**
     * Set the value of isAnswerChanged.
     */
    public void setAnswerChanged(AjaxBehaviorEvent event) {isAnswerChanged = true;}

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
     * Get the value of pointsEarned.
     *
     * @return the value of pointsEarned
     */
    public String getPointsEarned() {return pointsEarned;}

    /**
     * Get the value of multipleChoiceAnswer.
     *
     * @return the value of multipleChoiceAnswer
     */
    public String getMultipleChoiceAnswer() {return multipleChoiceAnswer;}

    /**
     * Get the value of isCompleted.
     *
     * @return the value of isCompleted
     */
    public boolean isCompleted() {return isCompleted;}

    /**
     * Set the value of isCompleted.
     *
     * @param completed new value of isCompleted
     */
    public void setCompleted(boolean completed) {isCompleted = completed;}

    /**
     * Set the number of files being uploaded.
     */
    public void setAttemptCount() {

        Map<String, String> request =
                FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        attemptCount = Integer.parseInt(request.get("count"));
    }

    /**
     * Set the values of the multiple choice answers from the multiple_choice table
     */
    private void setMultipleChoice() {

        answer1 = multipleChoiceFacade.findByProbAndNum(problem, 1).getText();
        answer2 = multipleChoiceFacade.findByProbAndNum(problem, 2).getText();
        answer3 = multipleChoiceFacade.findByProbAndNum(problem, 3).getText();
        answer4 = multipleChoiceFacade.findByProbAndNum(problem, 4).getText();
        // This is the answer the student has chosen as the correct answer.
        correctAnswer = null;
        // This is the actual correct answer. It will be displayed on the result page.
        multipleChoiceAnswer = multipleChoiceFacade.findByProbAndNum(problem, 1).getAnswer().toString();
    }

    /**
     * Initialize the selected answer to {@code null} (unanswered).
     */
    private void setAnswers() {

        isAnswerChanged = true;
        // Method call is not the result of the 'Save' button click.
        saveAnswer(false);
    }

    /**
     * @throws UnsupportedOperationException not supported yet
     */
    public void uploadFile() {

        throw new UnsupportedOperationException("Not supported yet.");
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
     * Download the requested file.
     *
     * @return the requested file
     */
    public StreamedContent downloadFile() {

        try {
            InputStream inputStream = new ByteArrayInputStream(downloadFile.getData());
            DefaultStreamedContent streamedContent = DefaultStreamedContent.builder().name(downloadFile.getName())
                    .stream((SerializableSupplier<InputStream>) () -> inputStream).build();
            return streamedContent;
        } catch (Throwable t) {
            Logger.getLogger(ExamProblemBean.class.getName()).log(Level.SEVERE, t.getMessage(), t);
            return null;
        }
    }

    /**
     * Return a primary key for an Answer entity.
     *
     * @return a primary key for an Answer entity
     */
    private AnswerPK getAnswerPK() {

        AnswerPK answerPK = new AnswerPK();
        // Set the value of the primary key.
        answerPK.setAssignment(problem.getAssignment().getId());
        answerPK.setProblem(problem.getName());
        answerPK.setUser(user.getId());
        return answerPK;
    }

    /**
     * On submission of the exam, the set answer isComplete value to 1 (true).
     */
    public void onSubmit() {

        AnswerPK answerPK = getAnswerPK();
        Answer answer = answerFacade.findByKey(answerPK);
        answer.setIsComplete((short) 1);
        answerFacade.edit(answer);
    }

    /**
     * Save the selected answer to the answer table.
     */
    public void saveAnswer(boolean isClick) {

        if (!isAnswerChanged)
            return;

        try {
            // Set the primary key for this answer.
            AnswerPK answerPK = getAnswerPK();

            Answer temp = answerFacade.findByKey(answerPK);

            // Set the answer
            Answer answer = new Answer();
            answer.setUser1(user);
            answer.setAssignment1(problem.getAssignment());
            answer.setProblem1(problem);
            // Answer will be null when the bean is initialized.
            if (correctAnswer != null) {
                answer.setAnswer(Integer.valueOf(correctAnswer));
                answer.setIsComplete(temp.getIsComplete());
                // If temp is not null, the student has started the exam. Set the answer to the saved answer if an
                // answer has been saved.
            } else if (temp != null && temp.getAnswer() != null) {
                answer.setAnswer(temp.getAnswer());
                answer.setIsComplete(temp.getIsComplete());
                if (answer.getIsComplete() == 1)
                    isCompleted = true;
            } else  // Otherwise set the answer to null.
                answer.setAnswer(null);
            // If the answer is correct, set according to the problem points. Otherwise, set to 0.
            if (Objects.equals(multipleChoiceFacade.findByProbAndNum(problem, 1).getAnswer(), answer.getAnswer()))
                answer.setCorrect(problem.getPoints().doubleValue());
            else // todo: Implement partial credit for a future project.
                answer.setCorrect(0.0);

            answer.setAnswerPK(answerPK);

            // Set the value of correctAnswer to the current value in the answer table.
            correctAnswer = String.valueOf(answer.getAnswer());

            // If there isn't a record for this problem in the answer table, create one. Otherwise, edit the existing
            // record.
            if (temp == null) {
                answer.setIsComplete((short) 0);
                answerFacade.create(answer);
            } else
                answerFacade.edit(answer);

            setPointsEarned();
            isAnswerChanged = false;

            // If the save button is clicked on the final question of the exam, a message is displayed.
            List<Problem> problems = problemFacade.findByAssignment(problem.getAssignment());
            // The final question is the last question in a list of questions for this assignment. This message is only
            // displayed if this method call is the result of clicking the save button.
            if (problem.equals(problems.get(problems.size() - 1)) && isClick) {
                Utilities.addMessage(FacesMessage.SEVERITY_INFO, "You have reached the end of this exam.",
                        "You may review your answers and submit when ready.");
                PrimeFaces.current().ajax().update(Utilities.findComponent("problem_growl"));
            }
        } catch (Throwable t) {
            Logger.getLogger(ExamProblemBean.class.getName()).log(Level.SEVERE, t.getMessage(), t);
            Utilities.addMessage(FacesMessage.SEVERITY_ERROR, "An error occurred saving the answer",
                    "If the problem persists, please notify your instructor.");
        }
    }

    /**
     * Set the value of pointsEarned.
     */
    private void setPointsEarned() {

        AnswerPK answerPK = new AnswerPK();
        answerPK.setUser(user.getId());
        answerPK.setAssignment(problem.getAssignment().getId());
        answerPK.setProblem(problem.getName());

        // Get the points earned from the answer table.
        Double points = answerFacade.findByKey(answerPK).getCorrect();
        // If the points earned is a whole number, display without a decimal place.
        if (points != null && points % 1 == 0)
            pointsEarned = String.valueOf(points.intValue());
        else
            pointsEarned = String.valueOf(points);
    }

    /**
     * Highlight the selected answer on the result page if it was incorrect.
     *
     * @param s the value from the radio button
     * @return a String name of a CSS style class.
     */
    public String highlightIncorrect(String s) {

        // If the radio button value (s) is the answer selected by the student (correctAnswer), and the selection is
        // not the actual correct answer (multipleChoiceAnswer), return the CSS class to highlight the incorrect
        // selection.
        if (s.equals(correctAnswer) && !correctAnswer.equals(multipleChoiceAnswer))
            return "highlighted";
        return "";
    }
}
