package com.cloudesire.syndication;

import com.cloudesire.platform.apiclient.CloudesireClient;
import com.cloudesire.platform.apiclient.CloudesireClientCallExecutor;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    private String vendorUsername;
    private String vendorPassword;
    private Boolean checkApiOnBoot;
    private String url;

    @Bean
    public CloudesireClient cloudesireClient( ObjectMapper objectMapper)
    {
        log.info( "Initializing vendor cmw-rest-client with username {} against {}", vendorUsername, url );
        CloudesireClient cloudesireClient = new CloudesireClient.Builder()
                .setBaseUrl( url )
                .setUsername( vendorUsername )
                .setPassword( vendorPassword )
                .setMapper( objectMapper )
                .build();
        if ( checkApiOnBoot ) cloudesireClient.getUserApi().getMe();
        return cloudesireClient;
    }

    @Bean
    public CloudesireClientCallExecutor cloudesireClientCallExecutor( ObjectMapper objectMapper )
    {
        return new CloudesireClientCallExecutor( objectMapper );
    }

    public void setVendorUsername( String vendorUsername )
    {
        this.vendorUsername = vendorUsername;
    }

    public void setVendorPassword( String vendorPassword )
    {
        this.vendorPassword = vendorPassword;
    }

    public void setCheckApiOnBoot( Boolean checkApiOnBoot )
    {
        this.checkApiOnBoot = checkApiOnBoot;
    }

    public void setUrl( String url )
    {
        this.url = url;
    }
}
