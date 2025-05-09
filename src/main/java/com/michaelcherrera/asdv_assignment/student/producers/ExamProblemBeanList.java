package com.michaelcherrera.asdv_assignment.student.producers;

import com.michaelcherrera.asdv_assignment.student.beans.ExamProblemBean;
import jakarta.inject.Qualifier;

import java.lang.annotation.*;

/**
 * CDI Qualifier used to distinguish a specific injection point for a list of {@link ExamProblemBean} instances.
 *
 * @see ExamProblemBeanListProducer
 *
 * @author Michael C. Herrera
 */
@Qualifier
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER, ElementType.TYPE})
public @interface ExamProblemBeanList {}
