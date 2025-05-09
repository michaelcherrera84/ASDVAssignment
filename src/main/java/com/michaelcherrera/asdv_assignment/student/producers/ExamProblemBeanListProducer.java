package com.michaelcherrera.asdv_assignment.student.producers;

import com.michaelcherrera.asdv_assignment.student.beans.ExamProblemBean;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Named;

import java.util.ArrayList;

/**
 * Producer class for creating and exposing a managed {@code ArrayList} of {@link ExamProblemBean} instances to be used
 * within CDI-enabled classes.
 * <p>
 * This producer allows injection into fields or parameters marked with the same qualifier. It is also {@link Named} as
 * "examProblemBeanList" to allow EL access.
 *
 * @author Michael C. Herrera
 */
public class ExamProblemBeanListProducer {

    private final ArrayList<ExamProblemBean> examProblemBeans = new ArrayList<>();

    @Produces
    @ExamProblemBeanList
    @Named(value = "examProblemBeanList")
    public ArrayList<ExamProblemBean> getExamProblemBeans() {return examProblemBeans;}
}
