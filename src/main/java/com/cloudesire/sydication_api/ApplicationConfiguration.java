package com.cloudesire.sydication_api;

import com.cloudesire.tisana4j.exceptions.RestException;
import com.cloudesire.tisana4j.exceptions.RuntimeRestException;
import com.liberologico.cloudesire.cmw.restclient.CmwRestClient;
import com.liberologico.cloudesire.cmw.restclient.resources.impl.CmwRestClientImpl;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties ( prefix = "syndication" )
public class ApplicationConfiguration
{
    private static final Logger log = LoggerFactory.getLogger( ApplicationConfiguration.class );

    @Setter
    private String vendorUsername;
    @Setter
    private String vendorPassword;
    @Setter
    private String username;
    @Setter
    private String password;
    @Setter
    private Boolean checkApiOnBoot;
    @Setter
    private String url;

    @Bean ( name = "vendorApiClient" )
    public CmwRestClient getVendorApiClient() throws RuntimeRestException, RestException
    {
        log.info( "Initializing vendor cmw-rest-client with username {} against {}", vendorUsername, url );
        CmwRestClientImpl client = new CmwRestClientImpl( vendorUsername, vendorPassword, url );
        if ( checkApiOnBoot ) client.getUserClient().getMe();
        return client;
    }

    @Bean ( name = "adminApiClient" )
    public CmwRestClient getAdminApiClient() throws RuntimeRestException, RestException
    {
        log.info( "Initializing admin cmw-rest-client with username {} against {}", username, url );
        CmwRestClientImpl client = new CmwRestClientImpl( username, password, url );
        if ( checkApiOnBoot ) client.getUserClient().getMe();
        return client;
    }
}
