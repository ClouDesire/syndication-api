package com.cloudesire.syndication.test;

import com.cloudesire.platform.apiclient.CloudesireClient;
import com.cloudesire.platform.apiclient.CloudesireClientCallExecutor;
import com.cloudesire.platform.apiclient.dto.model.dto.ProductDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith( SpringRunner.class )
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
        List<ProductDTO> products = callExecutor.execute( client.getProductApi().getAll( ) );
        assertThat( products ).isNotEmpty();
    }

}
