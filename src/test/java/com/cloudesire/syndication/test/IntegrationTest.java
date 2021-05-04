package com.cloudesire.syndication.test;

import com.cloudesire.platform.apiclient.CloudesireClient;
import com.cloudesire.platform.apiclient.CloudesireClientCallExecutor;
import com.cloudesire.platform.apiclient.dto.model.dto.CloudProviderDTO;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest( webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT )
public class IntegrationTest
{
    @Resource
    private CloudesireClient cloudesireClient;

    @Resource
    private CloudesireClientCallExecutor callExecutor;

    @Test
    public void up()
    {
        CloudesireClient client = cloudesireClient.newBuilder()
                .setBaseUrl( "https://staging.cloudesire.com/api" )
                .setUsername( null )
                .setPassword( null )
                .build();
        List<CloudProviderDTO> providers = callExecutor.execute( client.getCloudProviderApi().getAll() );
        assertThat( providers ).isNotEmpty();
    }
}
