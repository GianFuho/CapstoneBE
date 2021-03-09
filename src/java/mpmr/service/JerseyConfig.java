/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mpmr.service;

import org.glassfish.jersey.server.ResourceConfig;

/**
 *
 * @author Admin
 */
public class JerseyConfig extends ResourceConfig {

    public JerseyConfig() {

        register(CorsFilter.class);
        //other registrations

    }
}
