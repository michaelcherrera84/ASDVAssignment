package com.michaelcherrera.asdv_assignment.instructor.producers;

import com.michaelcherrera.asdv_assignment.instructor.beans.ProblemBean;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Named;

import java.util.ArrayList;

/**
 * Producer class for creating and exposing a managed {@code ArrayList} of {@link ProblemBean} instances to be used
 * within CDI-enabled classes.
 * <p>
 * This producer allows injection into fields or parameters marked with the same qualifier. It is also {@link Named} as
 * "problemBeanList" to allow EL access.
 */
public class ProblemBeanListProducer {

    private final ArrayList<ProblemBean> problemBeans = new ArrayList<>();

    @Produces
    @ProblemBeanList
    @Named(value = "problemBeanList")
    public ArrayList<ProblemBean> getProblemBeanList() {return problemBeans;}
}
