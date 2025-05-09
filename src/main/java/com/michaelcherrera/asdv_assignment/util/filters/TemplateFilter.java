package com.michaelcherrera.asdv_assignment.util.filters;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * TemplateFilter is a servlet filter that restricts access to specific URL patterns. Requests matching the configured
 * patterns are intercepted, and an HTTP 403 Forbidden response is returned, denying access to the resource.
 *
 * @author Michael C. Herrera
 */
@WebFilter(urlPatterns = {/*"/faces/templates/*", "/templates/*", */"/instructor/*"
        , "/student/*", "/admin/*"})
public class TemplateFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

        Filter.super.init(filterConfig);
    }

    /**
     * The doFilter method of the Filter is called by the container each time a request/response pair is passed through
     * the chain due to a client request for a resource at the end of the chain. The FilterChain passed in to this
     * method allows the Filter to pass on the request and response to the next entity in the chain.
     *
     * @param request  the {@link ServletRequest} object contains the client's request
     * @param response the {@link ServletResponse} object contains the filter's response
     * @param chain    the {@link FilterChain} for invoking the next filter or the resource
     * @throws IOException      if an I/O related error has occurred during the processing
     * @throws ServletException if an exception occurs that interferes with the filter's normal operation
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletResponse httpResponse = (HttpServletResponse) response;
        httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "Access to this resource is denied.");
    }

    @Override
    public void destroy() {

        Filter.super.destroy();
    }
}

