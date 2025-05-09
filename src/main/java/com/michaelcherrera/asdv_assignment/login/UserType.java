package com.michaelcherrera.asdv_assignment.login;

/**
 * Enum representing different types of users in the system.
 *
 * <ul>
 * - ADMINISTRATOR: Represents a system administrator, typically responsible for managing users and system settings.
 * - STUDENT: Represents a student user, typically accessing educational content or exams.
 * - INSTRUCTOR: Represents an instructor user, typically managing educational materials and student assessments.
 * </ul>
 * <p>
 * This enum is used to define roles for the users, and it provides a method to determine the appropriate
 * login redirection path for each type of user.
 */
public enum UserType {

    ADMINISTRATOR, STUDENT, INSTRUCTOR;

    /**
     * Return the login redirection path for a specified user type.
     *
     * @param userType the specified user type
     * @return the login redirection path for the specified user type
     */
    public static String getLogin(UserType userType) {

        return switch (userType) {
            case ADMINISTRATOR -> "admin/admin_panel?faces-redirect=true";
            case STUDENT -> "student/assignments_table?faces-redirect=true";
            case INSTRUCTOR -> "instructor/assignments_table?faces-redirect=true";
            default -> null;
        };

    }
}



