import com.cloudesire.sydication_api.Application;
import com.cloudesire.tisana4j.RestClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.liberologico.cloudesire.cmw.model.dto.EventNotificationDTO;
import com.liberologico.cloudesire.cmw.model.dto.InvoiceDTO;
import com.liberologico.cloudesire.cmw.model.dto.UrlEntityDTO;
import com.liberologico.cloudesire.cmw.model.enums.CmwEventType;
import com.liberologico.cloudesire.cmw.restclient.CmwRestClient;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockserver.client.server.MockServerClient;
import org.mockserver.junit.MockServerRule;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.net.URL;
import java.util.Date;

@RunWith (SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration (classes = Application.class)
@WebIntegrationTest
public class IntegrationTest
{
    @Autowired
    private CmwRestClient cmwRestClientlient;
    @Autowired
    private ObjectMapper mapper;

    private RestClient restClient;

    @Rule
    public MockServerRule mockServerRule =  new MockServerRule(8082, this);

    private MockServerClient mockServerClient;

    @Before
    public void setup() throws JsonProcessingException
    {
        restClient = new RestClient();

        InvoiceDTO invoice = new InvoiceDTO();
        invoice.setSubscription( new UrlEntityDTO( "subscription/123" ) );
        mockServerClient.when(
                HttpRequest.request()
                        .withMethod( "GET" )
                        .withPath( "/cmw/invoice/123" ) )
                .respond( HttpResponse.response().withStatusCode( 200 ).withBody( mapper.writeValueAsString( invoice ) )
                                .withHeader( "Content-type", "application/json" ) );
    }

    @Test
    @Ignore("needs work")
    public void test() throws Exception
    {
        EventNotificationDTO eventDTO = new EventNotificationDTO();
        eventDTO.setDate( new Date() );
        eventDTO.setType( CmwEventType.MODIFIED.toString() );
        eventDTO.setEntity( "Invoice" );
        eventDTO.setId( 123 );
        restClient.post( new URL( "http://localhost:8080/api/event" ), eventDTO );
    }
}
