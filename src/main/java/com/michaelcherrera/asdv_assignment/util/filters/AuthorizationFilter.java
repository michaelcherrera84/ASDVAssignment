package com.michaelcherrera.asdv_assignment.util.filters;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * AuthorizationFilter is a servlet filter used to protect specific URL patterns by verifying the existence of a user in
 * the session. It allows access to secured resources only if the user is authenticated.
 * <p>
 * This filter intercepts requests for paths matching "/faces/instructor/*" and "/faces/student/*". If no user session
 * exists or the session user attribute is null, it redirects the request to the login page located at
 * "/faces/index.xhtml".
 * <p>
 * The filter lifecycle methods (init and destroy) provide basic initialization and cleanup functionalities using the
 * default implementations in the Filter interface.
 *
 * @author Michael C. Herrera
 */
@WebFilter(urlPatterns = {"/faces/instructor/*", "/faces/student/*"})
public class AuthorizationFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

        Filter.super.init(filterConfig);
    }

    /**
     * The doFilter method of the Filter is called by the container each time a request/response pair is passed through
     * the chain due to a client request for a resource at the end of the chain. The FilterChain passed in to this
     * method allows the Filter to pass on the request and response to the next entity in the chain.
     *
     * @param servletRequest  the {@link ServletRequest} object contains the client's request
     * @param servletResponse the {@link ServletResponse} object contains the filter's response
     * @param filterChain     the {@link FilterChain} for invoking the next filter or the resource
     * @throws IOException      if an I/O related error has occurred during the processing
     * @throws ServletException if an exception occurs that interferes with the filter's normal operation
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpSession session = request.getSession();

        // If there is no session or the User is null, redirect back to the Login page.
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/faces/index.xhtml");
        } else
            filterChain.doFilter(servletRequest, servletResponse);

    }

    @Override
    public void destroy() {

        Filter.super.destroy();
    }
}
