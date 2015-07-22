package com.cloudesire.sydication_api;

import com.liberologico.cloudesire.cmw.restclient.CmwRestClient;
import com.liberologico.cloudesire.cmw.restclient.resources.impl.CmwRestClientImpl;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "syndication")
public class ApplicationConfiguration
{
    private static final Logger log = LoggerFactory.getLogger( ApplicationConfiguration.class );

    @Setter
    private String username;
    @Setter
    private String password;
    @Setter
    private String superUsername;
    @Setter
    private String superPassword;
    @Setter
    private String url;

    @Bean ( name = "vendorApiClient" )
    public CmwRestClient getVendorApiClient()
    {
        log.info( "Initializing vendor cmw-rest-client with username {} against {}", username, url );
        return new CmwRestClientImpl( username, password, url );
    }

    @Bean ( name = "adminApiClient" )
    public CmwRestClient getAdminApiClient()
    {
        log.info( "Initializing admin cmw-rest-client with username {} against {}", superUsername, url );
        return new CmwRestClientImpl( superUsername, superPassword, url );
    }
}
