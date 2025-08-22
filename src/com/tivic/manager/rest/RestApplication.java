package com.tivic.manager.rest;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.internal.JerseyResourceContext;

import com.tivic.sol.auth.CORSFilter;
import com.tivic.sol.auth.jwt.JWTMiddleware;

public class RestApplication extends ResourceConfig {

    /**
     * Register JAX-RS application components.
     */
    public RestApplication () {
    	packages("com.tivic.manager");
        register(JacksonFeature.class);
        register(JWTMiddleware.class);
        register(CORSFilter.class);
        register(MultiPartFeature.class);
        register(JerseyResourceContext.class);
    }
}
