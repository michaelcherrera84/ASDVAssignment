package com.michaelcherrera.asdv_assignment.login;

import com.michaelcherrera.asdv_assignment.entities.User;
import com.michaelcherrera.asdv_assignment.interfaces.UserFacadeLocal;
import com.michaelcherrera.asdv_assignment.util.Utilities;
import com.michaelcherrera.asdv_assignment.util.security.DES;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpSession;
import org.primefaces.PrimeFaces;

import javax.crypto.BadPaddingException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Managed bean responsible for handling user login functionality within a Jakarta EE web application.
 *
 * <p>The bean manages user input for login credentials (username and password), and delegates authentication
 * logic to the {@code UserFacadeLocal} EJB. It encrypts and validates the provided password using a DES-based custom
 * encryption utility and verifies the user's role (e.g., instructor or student) to determine the appropriate
 * redirection page.</p>
 *
 * <p>If the credentials are invalid or an exception occurs during validation, a dynamic warning message is displayed
 * via PrimeFaces.</p>
 *
 * @author Michael C. Herrera
 */
@Named
@RequestScoped
public class LoginBean {

    private String username;
    private String password;

    @EJB
    private UserFacadeLocal userFacade;

    /**
     * Get the value of username.
     *
     * @return the value of username
     */
    public String getUsername() {return username;}

    /**
     * Set the value of username.
     *
     * @param username new value of username
     */
    public void setUsername(String username) {this.username = username;}

    /**
     * Get the value of password.
     *
     * @return the value of password
     */
    public String getPassword() {return password;}

    /**
     * Set the value of password.
     *
     * @param password new value of password
     */
    public void setPassword(String password) {this.password = password;}

    /**
     * Validates the user's password against the stored data. This method ensures the entered password matches the
     * expected password associated with the current user.
     */
    public String validatePassword() {

        //todo: Remove testing login credentials
        if (username.equals("admin")) {
            return UserType.getLogin(UserType.ADMINISTRATOR);
        }
        
        try {
            User user = userFacade.findByUsername(username);
            // If the username exists validate the password.
            if (user != null) {
                DES des = new DES();
                // Get the user's 4096 character password.
                String encryptedPassword = user.getPassword();
                // Encrypt the password entered into the login field with DES.
                String desPassword = des.encryptDES(username, password);
                // Decrypt the user's password.
                String decryptedPassword;
                decryptedPassword = des._4096toPassword(encryptedPassword.toCharArray(), desPassword.length());
                decryptedPassword = des.decryptDes(decryptedPassword, des.getDESkeyForUserNameAndPassword(username,
                        password));
                // If the passwords match, login.
                if (password.equals(decryptedPassword)) {
                    ((HttpSession) FacesContext.getCurrentInstance().getExternalContext()
                            .getSession(false)).setAttribute("user", user);

                    return UserType.getLogin(UserType.valueOf(user.getPosition().toUpperCase()));
                }
            }

            Utilities.addMessage(FacesMessage.SEVERITY_WARN, "Login Failed", "Invalid username or password.");
        } catch (IllegalArgumentException | BadPaddingException e) {
            Utilities.addMessage(FacesMessage.SEVERITY_WARN, "Login Failed", "Invalid username or password.");
        } catch (Throwable e) {
            Logger.getLogger(LoginBean.class.getName()).log(Level.SEVERE, null, e);
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
                    "An unexpected error has occurred. Please try again later.<br/>If the problem persists, " +
                            "please contact your system administrator.");
            PrimeFaces.current().dialog().showMessageDynamic(msg, false);
        }

        return "";
    }

    /**
     * Invalidate the user's session and return to the login page if requested.
     *
     * @param redirect true if a redirect to the login page is requested by the caller
     * @return redirect URL
     */
    public String logout(boolean redirect) {

        ((HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false)).invalidate();
        if (redirect)
            return "/index.xhtml?faces-redirect=true";
        return "";
    }
}
