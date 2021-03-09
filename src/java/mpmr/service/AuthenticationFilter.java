/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mpmr.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

/**
 *
 * @author Admin
 */
@Provider
@Secured
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationFilter implements ContainerRequestFilter {

    @Context
    private ResourceInfo resourceInfo;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {

        // Get the HTTP Authorization header from the request
        String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
        try {
            // Extract the token from the HTTP Authorization header
            String token = authorizationHeader.substring("Bearer".length()).trim();
            // Validate the token

            Claims claims = Jwts.parser().setSigningKey("flzxsqcysyhljt").parseClaimsJws(token).getBody();
            Method method = resourceInfo.getResourceMethod();
            if (method != null) {
                // Get allowed permission on method
                Secured JWTContext = method.getAnnotation(Secured.class);
                Role[] permission = JWTContext.value();
                if (permission != null) {
                    List<Role> list = new ArrayList<>();
                    // Get Role from jwt
                    for (int i = 0; i < permission.length; i++) {

                        Role role = permission[i];
                        list.add(role);
                    }
                    String roles = claims.get("jti", String.class);
                    Role roleUser = Role.valueOf(roles);
                    if (!list.contains(roleUser)) {
                        throw new Exception("no roles");
                    }

                    // if role allowed != role jwt -> UNAUTHORIZED
                }
            }

        } catch (Exception e) {
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
        }
    }
}
