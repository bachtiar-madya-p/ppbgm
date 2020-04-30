package id.io.portal.service.filter;

import java.io.IOException;
import java.lang.reflect.Method;
import javax.annotation.security.PermitAll;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.ext.Provider;
import id.io.portal.filter.SessionFilter;
import id.io.portal.model.Principal;
import id.io.portal.util.rest.exception.UnauthorizedException;

@Provider
public class AuthenticationFilter implements ContainerRequestFilter {

    @Context
    private ResourceInfo resourceInfo;

    @Context
    private HttpServletRequest httpServletRequest;


    public AuthenticationFilter() {
        //Empty Constructor
    }

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        if (!hasSecurityAnnotations(resourceInfo)) {
            return;
        }

        // Session is Present
        if (hasSession(httpServletRequest)) {
            // User is Valid
            return;
        } else {
            throw new UnauthorizedException();
        }
    }

    private boolean hasSession(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return session != null && session.getAttribute(Principal.class.getCanonicalName()) != null
                && session.getAttribute(SessionFilter.SESSION_KEY) != null;
    }

    private boolean hasSecurityAnnotations(ResourceInfo resourceInfo) {
        Method method = resourceInfo.getResourceMethod();
        Class<?> clazz = resourceInfo.getResourceClass();
        return method.isAnnotationPresent(PermitAll.class) || clazz.isAnnotationPresent(PermitAll.class);

    }

}
