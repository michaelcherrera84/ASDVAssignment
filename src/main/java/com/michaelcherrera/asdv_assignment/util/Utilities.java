package com.michaelcherrera.asdv_assignment.util;

import com.michaelcherrera.asdv_assignment.instructor.beans.AssignmentBean;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.core.Request;
import org.primefaces.PrimeFaces;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The Utilities class contains various methods to assist with development of the JSF application.
 *
 * @author Michael C. Herrera
 */
public class Utilities {

    /**
     * Appends a {@link FacesMessage} to messages to be displayed.
     *
     * @param severity severity level of the message
     * @param summary  summary of message
     * @param detail   detailed message
     */
    public static void addMessage(FacesMessage.Severity severity, String summary, String detail) {

        FacesMessage msg = new FacesMessage(severity, summary, detail);
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    /**
     * Returns a {@link UIComponent} based on the specified id value.
     *
     * @param id specified id value
     * @return {@link UIComponent} based on the specified id value
     */
    public static UIComponent findComponent(String id) {

        if (FacesContext.getCurrentInstance().getViewRoot() != null)
            return findComponent(FacesContext.getCurrentInstance().getViewRoot(), id);
        return null;
    }

    /**
     * Returns a {@link UIComponent} based on the {@link jakarta.faces.component.UIViewRoot} and specified id value.
     *
     * @param root {@link jakarta.faces.component.UIViewRoot}
     * @param id   specified id value
     * @return a {@link UIComponent} based on the {@link jakarta.faces.component.UIViewRoot} and specified id value
     */
    private static UIComponent findComponent(UIComponent root, String id) {

        if (root.getId().equals(id))
            return root;

        UIComponent result = null;
        for (UIComponent child : root.getChildren()) {
            if (child.getId().equals(id))
                return child;
            result = findComponent(child, id);
            if (result != null)
                break;
        }

        return result;
    }

    public static void fatalError(Throwable e) {

        try {
            HttpServletRequest request =
                    (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            FacesContext.getCurrentInstance().getExternalContext()
                    .redirect(request.getContextPath() + "/errors/error_page.html");
        } catch (IOException ignore) {
            FacesContext.getCurrentInstance().responseComplete();
            Logger.getLogger(AssignmentBean.class.getName()).log(Level.SEVERE, null, e);
        }
    }
}
