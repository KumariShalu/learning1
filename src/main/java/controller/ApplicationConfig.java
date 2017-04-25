/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.util.HashSet;
import java.util.Set;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import javax.ws.rs.core.Application;

/**
 *
 * @author jGauravGupta
 */
@javax.ws.rs.ApplicationPath("resources")
public class ApplicationConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        addRestResourceClasses(resources);
        return resources;
    }


    @Override
    public Set<Object> getSingletons() {
        final Set<Object> instances = new HashSet<>();
        instances.add(new JacksonJsonProvider());
        return instances;
    }

    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(app.security.SecurityUtils.class);
        resources.add(app.security.jwt.JWTAuthenticationFilter.class);
        resources.add(controller.AccountController.class);
        resources.add(controller.ChildController.class);
        resources.add(controller.UserController.class);
        resources.add(controller.UserJWTController.class);
    }
    
}
