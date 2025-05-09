package com.michaelcherrera.asdv_assignment.admin;

import com.michaelcherrera.asdv_assignment.entities.User;
import com.michaelcherrera.asdv_assignment.client.UserAdmin;
import com.michaelcherrera.asdv_assignment.util.Utilities;
import com.michaelcherrera.asdv_assignment.util.security.DES;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import org.primefaces.PrimeFaces;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.Serializable;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The {@code AdminPanelBean} class represents the managed bean to handle the administrative functionalities for
 * managing users in the application. It provides capabilities for user retrieval, user addition, user updates, user
 * deletions, retrieving user data in bulk or by specific range, and other administrative actions related to user
 * management.
 *
 * @author Michael C. Herrera
 */
@Named
@ViewScoped
public class AdminPanelBean implements Serializable {

    /**
     * User found in the database
     */
    private User findUser;
    /**
     * User to add to the database
     */
    private User addUser;
    /**
     * User to update in the database
     */
    private User updateUser;
    /**
     * User to delete from the database
     */
    private User deleteUser;
    /**
     * Count of all Users
     */
    private String userCount;
    /**
     * List of all users in the database
     */
    private List<User> users;
    /**
     * List a specified range of users in the database
     */
    private List<User> userRange;
    private Integer from;
    private Integer to;

    /**
     * Create a new instance of AdminPanelBean.
     */
    public AdminPanelBean() {

        findUser = new User();
        addUser = new User();
        updateUser = new User();
        deleteUser = new User();
        users = new ArrayList<>();
        userRange = new ArrayList<>();
    }

    /**
     * Initialize the AdminPanelBean.
     */
    @PostConstruct
    public void init() {
        findAllUsers();
    }

    /**
     * Get the value of findUser.
     *
     * @return the value of findUser
     */
    public User getFindUser() {
        return findUser;
    }

    /**
     * Set the value of findUser.
     *
     * @param findUser new value of findUser
     */
    public void setFindUser(User findUser) {
        this.findUser = findUser;
    }

    /**
     * Get the value of addUser.
     *
     * @return the value of addUser
     */
    public User getAddUser() {
        return addUser;
    }

    /**
     * Set the value of addUser.
     *
     * @param addUser new value of addUser
     */
    public void setAddUser(User addUser) {
        this.addUser = addUser;
    }

    /**
     * Get the value of updateUser.
     *
     * @return the value of updateUser
     */
    public User getUpdateUser() {
        return updateUser;
    }

    /**
     * Set the value of updateUser.
     *
     * @param updateUser new value of updateUser
     */
    public void setUpdateUser(User updateUser) {
        this.updateUser = updateUser;
    }

    /**
     * Get the value of deleteUser.
     *
     * @return the value of deleteUser
     */
    public User getDeleteUser() {
        return deleteUser;
    }

    /**
     * Set the value of deleteUser.
     *
     * @param deleteUser new value of deleteUser
     */
    public void setDeleteUser(User deleteUser) {
        this.deleteUser = deleteUser;
    }

    /**
     * Get the value of userCount.
     *
     * @return the value of userCount
     */
    public String getUserCount() {

        try {
            UserAdmin client = new UserAdmin();
            userCount = client.countREST();
            return userCount;
        } catch (Throwable t) {
            Logger.getLogger(AdminPanelBean.class.getName()).log(Level.SEVERE, t.getMessage(), t);
            Utilities.addMessage(FacesMessage.SEVERITY_ERROR, "Error finding user count",
                    "If the problem persists, please contact the system administrator.");
        }
        return "";
    }

    /**
     * Get the value of users.
     *
     * @return the value of users
     */
    public List<User> getUsers() {
        return users;
    }

    /**
     * Get the value of userRange.
     *
     * @return the value of userRange
     */
    public List<User> getUserRange() {
        return userRange;
    }

    /**
     * Get the value of from.
     *
     * @return the value of from
     */
    public Integer getFrom() {
        return from;
    }

    /**
     * Set the value of from.
     *
     * @param from new value of from
     */
    public void setFrom(Integer from) {
        this.from = from;
    }

    /**
     * Get the value of to.
     *
     * @return the value of to
     */
    public Integer getTo() {
        return to;
    }

    /**
     * Set the value of to.
     *
     * @param to new value of to
     */
    public void setTo(Integer to) {
        this.to = to;
    }

    /**
     * Find a user in the database.
     */
    public void findUser() {

        UserAdmin client = new UserAdmin();

        try {
            findUser = client.find(findUser.getClass(), findUser.getId().toString());
            if (findUser == null)  // if the user doesn't exist in the database
                Utilities.addMessage(FacesMessage.SEVERITY_INFO, "User not found",
                        "No user exists with the given ID.");
        } catch (Throwable t) {
            Logger.getLogger(AdminPanelBean.class.getName()).log(Level.SEVERE, t.getMessage(), t);
            Utilities.addMessage(FacesMessage.SEVERITY_ERROR, "Error finding user",
                    "If the problem persists, please contact the system administrator.");
        }

        client.close();
    }

    /**
     * Find all the users in the database.
     */
    private void findAllUsers() {

        UserAdmin client = new UserAdmin();

        try {
            users = client.findAll(users.getClass());
        } catch (Throwable t) {
            Logger.getLogger(AdminPanelBean.class.getName()).log(Level.SEVERE, t.getMessage(), t);
            Utilities.addMessage(FacesMessage.SEVERITY_ERROR, "Error Finding Users",
                    "If the problem persists, please contact the system administrator.");
        }

        client.close();
    }

    /**
     * Find a range of Users in the database.
     */
    public void findUserRange() {

        UserAdmin client = new UserAdmin();

        // The user range cannot be negative or null.
        if (from == null || to == null || from < 1) {
            Utilities.addMessage(FacesMessage.SEVERITY_ERROR, "Invalid range",
                    "The from and to fields must be integers from 1 to the total number of users in the database.");
            from = null;
            to = null;
            return;
        }

        // The from and to range begins at 0 but is received from the user in a range beginning at 1.
        String f = String.valueOf(from - 1);
        String t = String.valueOf(to - 1);

        try {
            userRange = client.findRange(userRange.getClass(), f, t);
        } catch (Throwable e) {
            Logger.getLogger(AdminPanelBean.class.getName()).log(Level.SEVERE, e.getMessage(), e);
            Utilities.addMessage(FacesMessage.SEVERITY_ERROR, "Error finding users",
                    "If the problem persists, please contact the system administrator.");
        }

        // Reset field values
        from = null;
        to = null;
        client.close();
    }

    /**
     * Encrypt the password for the user to be added to the database.
     *
     * @return encrypted password.
     */
    private String encryptPassword(User user)
            throws IllegalBlockSizeException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeySpecException,
            BadPaddingException, InvalidKeyException {

        String encryptedPassword;
        DES des = new DES();

        // Encrypt the password with DES.
        String desEncrypted = des.encryptDES(user.getUsername(), user.getPassword());
        // Further encrypt the password by spreading the DES over 4096 characters.
        encryptedPassword = new String(des.passwordTo4096(desEncrypted));

        return encryptedPassword;
    }

    /**
     * Build a {@link JsonObject} for the specified User with the given {@link JsonObjectBuilder}.
     *
     * @param job  given JsonObjectBuilder
     * @param user the specified User
     * @param date the date the User was created as a String
     * @return JSON object for inserting into or updating in the database
     */
    private JsonObject buildJSON(JsonObjectBuilder job, User user, String date)
            throws IllegalBlockSizeException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeySpecException,
            BadPaddingException, InvalidKeyException {

        job.add("username", user.getUsername());
        job.add("password", encryptPassword(user));
        job.add("fname", user.getFname());
        job.add("lname", user.getLname());
        job.add("position", user.getPosition());
        job.add("date", date);
        return job.build();
    }

    /**
     * Insert a new user in the database.
     */
    public void insertUser() {

        UserAdmin client = new UserAdmin();
        JsonObjectBuilder job = Json.createObjectBuilder();

        List<String> usernames = client.findAllNames();
        if (usernames.contains(addUser.getUsername())) {
            Utilities.addMessage(FacesMessage.SEVERITY_ERROR, "Duplicate username",
                    "A User already exists with the given username.");
            addUser = new User();
            return;
        }

        // Get the User date in a JSON friendly String format.
        Instant instant = new Date().toInstant();
        String date = instant.atZone(ZoneId.systemDefault()).toLocalDateTime()
                .format(DateTimeFormatter.ISO_DATE_TIME);

        try {
            client.create(buildJSON(job, addUser, date));
            Utilities.addMessage(FacesMessage.SEVERITY_INFO, "Success", "User has been saved.");
            client.close();
            // Reset fields for update.
            addUser = new User();
            findAllUsers();
            userRange = new ArrayList<>();
        } catch (Throwable t) {
            Logger.getLogger(AdminPanelBean.class.getName()).log(Level.SEVERE, t.getMessage(), t);
            Utilities.addMessage(FacesMessage.SEVERITY_ERROR, "Error saving user",
                    "If the problem persists, please contact the system administrator.");
        }
    }

    public void findUpdateUser() {
        UserAdmin client = new UserAdmin();

        try {
            updateUser = client.find(updateUser.getClass(), updateUser.getId().toString());
            if (updateUser == null) { // if the user doesn't exist in the database
                Utilities.addMessage(FacesMessage.SEVERITY_INFO, "User not found",
                        "No user exists with the given ID.");
                updateUser = new User();
            }
        } catch (Throwable t) {
            Logger.getLogger(AdminPanelBean.class.getName()).log(Level.SEVERE, t.getMessage(), t);
            Utilities.addMessage(FacesMessage.SEVERITY_ERROR, "Error finding user",
                    "If the problem persists, please contact the system administrator.");
        }

        client.close();
    }

    /**
     * Update a user in the database.
     *
     * @implNote the User is only updated if a User exists with the specified user ID. To add a user, use
     * {@link #insertUser()}. This means a User's auto-incremented ID cannot be changed.
     */
    public void updateUser() {

        UserAdmin client = new UserAdmin();
        User tempUser = client.find(updateUser.getClass(), updateUser.getId().toString());

        try {
            if (tempUser == null) {  // if the user doesn't exist in the database
                Utilities.addMessage(FacesMessage.SEVERITY_INFO, "User not found",
                        "No user exists with the given ID.");
                // Reset fields for update.
                updateUser = new User();
                return;
            }

            JsonObjectBuilder job = Json.createObjectBuilder();
            // Get the User date in a JSON friendly String format.
            String date = tempUser.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()
                    .format(DateTimeFormatter.ISO_DATE_TIME);

            // Set the set the fields to the new values.
            job.add("id", updateUser.getId());
            job.add("username", updateUser.getUsername());
            job.add("password", tempUser.getPassword());
            job.add("fname", updateUser.getFname());
            job.add("lname", updateUser.getLname());
            job.add("position", updateUser.getPosition());
            job.add("date", date);
            JsonObject jo = job.build();

            // Edit the user.
            client.edit(jo, updateUser.getId().toString());
            Utilities.addMessage(FacesMessage.SEVERITY_INFO, "Success", "User has been updated.");
            client.close();

            // Reset fields for update.
            updateUser = new User();
            findUser = new User();
            findAllUsers();
            userRange = new ArrayList<>();
        } catch (Throwable t) {
            Logger.getLogger(AdminPanelBean.class.getName()).log(Level.SEVERE, t.getMessage(), t);
            Utilities.addMessage(FacesMessage.SEVERITY_ERROR, "Error updating user",
                    "If the problem persists, please contact the system administrator.");
        }
    }

    /**
     * Validate value of the Delete User Id.
     *
     * @param context   per-request state information related to the processing of this Jakarta Faces request
     * @param component the PrimeFaces interface component associated with the current request
     * @param value     the value of the Delete User Id field
     */
    public void validateDeleteID(FacesContext context, UIComponent component, Object value) {

        if (value != null) {
            UserAdmin client = new UserAdmin();
            User temp = client.find(deleteUser.getClass(), value.toString());
            client.close();

            // Show comfirmation dialog if the user exists. Otherwise, notify the admin that the user doesn't exist.
            if (temp != null)
                PrimeFaces.current().executeScript("PF('confirm_delete').show()");
            else {
                Utilities.addMessage(FacesMessage.SEVERITY_INFO, "User not found",
                        "No user exists with the given ID.");
                deleteUser = new User();
                PrimeFaces.current().ajax().update(component);
            }
        }
    }

    /**
     * Remove a User from the database.
     */
    public void deleteUser() {

        UserAdmin client = new UserAdmin();

        try {
            client.remove(deleteUser.getId().toString());
            Utilities.addMessage(FacesMessage.SEVERITY_INFO, "Success", "User has been removed.");
            // Reset fields for update.
            deleteUser = new User();
            findUser = new User();
            findAllUsers();
            userRange = new ArrayList<>();
            client.close();
        } catch (Throwable t) {
            Logger.getLogger(AdminPanelBean.class.getName()).log(Level.SEVERE, t.getMessage(), t);
            Utilities.addMessage(FacesMessage.SEVERITY_ERROR, "Error deleting user",
                    "If the problem persists, please contact the system administrator.");
        }
    }

    /**
     * Reset the Delete User Id field.
     */
    public void resetDelete() {
        deleteUser = new User();
    }
}
