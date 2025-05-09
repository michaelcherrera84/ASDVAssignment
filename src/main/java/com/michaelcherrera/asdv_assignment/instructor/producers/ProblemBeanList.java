package com.michaelcherrera.asdv_assignment.instructor.producers;

import com.michaelcherrera.asdv_assignment.instructor.beans.ProblemBean;
import jakarta.inject.Qualifier;

import java.lang.annotation.*;

/**
 * CDI Qualifier used to distinguish a specific injection point for a list of {@link ProblemBean} instances.
 *
 * @see ProblemBeanListProducer
 */
@Qualifier
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER, ElementType.TYPE})
public @interface ProblemBeanList {}
