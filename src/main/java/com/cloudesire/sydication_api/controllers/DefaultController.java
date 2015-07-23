package com.cloudesire.sydication_api.controllers;

import com.cloudesire.tisana4j.exceptions.RestException;
import com.cloudesire.tisana4j.exceptions.RuntimeRestException;
import com.liberologico.cloudesire.cmw.model.dto.CompanyDTO;
import com.liberologico.cloudesire.cmw.model.dto.EventNotificationDTO;
import com.liberologico.cloudesire.cmw.model.dto.InvoiceDTO;
import com.liberologico.cloudesire.cmw.model.dto.MyUserDTO;
import com.liberologico.cloudesire.cmw.model.dto.ProductVersionDTO;
import com.liberologico.cloudesire.cmw.model.dto.SubscriptionDTO;
import com.liberologico.cloudesire.cmw.model.enums.CmwEventType;
import com.liberologico.cloudesire.cmw.model.enums.DeploymentStatusEnum;
import com.liberologico.cloudesire.cmw.restclient.CmwRestClient;
import com.liberologico.cloudesire.cmw.restclient.resources.CompanyClient;
import com.liberologico.cloudesire.cmw.restclient.resources.InvoiceClient;
import com.liberologico.cloudesire.cmw.restclient.resources.SubscriptionClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping ( "/api" )
public class DefaultController
{
    private static final Logger log = LoggerFactory.getLogger( DefaultController.class );

    @Resource(name = "vendorApiClient")
    private CmwRestClient vendorApiClient;
    @Resource(name = "vendorApiClient")
    private CmwRestClient adminApiClient;

    @RequestMapping ( value = "/event", method = RequestMethod.POST)
    @ResponseStatus ( HttpStatus.NO_CONTENT )
    public void handleEvent( @RequestBody EventNotificationDTO event ) throws Exception
    {
        if (
                StringUtils.isEmpty( event.getEntity() ) ||
                StringUtils.isEmpty( event.getId() ) ||
                StringUtils.isEmpty( event.getType() ))
        {
            throw new IllegalArgumentException( "Event seems malformed" );
        }

        log.debug( "Handling notification for {} with id {} of type {}", event.getEntity(), event.getId(), event.getType() );

        if ( "Subscription".equals( event.getEntity() ) ) handleSubscription( event );

        if ( "Invoice".equals( event.getEntity() ) ) handleInvoice( event );
    }

    private void handleInvoice( EventNotificationDTO event ) throws RuntimeRestException, RestException
    {
        if ( CmwEventType.MODIFIED.toString().equals( event.getType() ) )
        {
            final InvoiceClient invoiceClient = vendorApiClient.getInvoiceClient();
            final SubscriptionClient subscriptionClient = vendorApiClient.getSubscriptionClient();
            final CompanyClient companyClient = vendorApiClient.getCompanyClient();

            InvoiceDTO invoice = invoiceClient.get( event.getId() );
            SubscriptionDTO subscription = subscriptionClient.get( invoice.getSubscription() );

            if (invoice.isPaid() && subscription.getDeploymentStatus().equals( DeploymentStatusEnum.PENDING ))
            {
                invoice.getNominee().getUrl();
                MyUserDTO user = vendorApiClient.getUserClient().get( invoice.getNominee() );
                CompanyDTO company = companyClient.get( user.getCompany() );

                ProductVersionDTO productVersion = vendorApiClient.getProductVersionClient()
                        .get( subscription.getProductVersion() );
                if (productVersion.getName().equalsIgnoreCase( "Premium" ))
                    company.setMaxPublishedProducts( 100 );
                else
                    company.setMaxPublishedProducts( 1 );

                adminApiClient.getCompanyClient().update( company.getId(), company );
                log.info( "Set maxPublishedProduct={} companyName={} companyId={}", company.getMaxPublishedProducts(),
                        company.getId(), company.getName() );

                subscriptionClient.update( subscription.getId(), DeploymentStatusEnum.DEPLOYED, null, null );
                log.debug( "Set DEPLOYED to subscription {}", subscription.getId() );
            }
        }
    }

    private void handleSubscription( EventNotificationDTO event ) throws RuntimeRestException, RestException
    {
        if (CmwEventType.MODIFIED.toString().equals( event.getType()) || CmwEventType.DELETED.toString().equals(
                event.getType() ))
        {
            final SubscriptionClient subscriptionClient = vendorApiClient.getSubscriptionClient();
            final CompanyClient companyClient = vendorApiClient.getCompanyClient();

            SubscriptionDTO subscription = subscriptionClient.get( event.getId() );
            if (DeploymentStatusEnum.UNDEPLOY_SENT.toString().equals(subscription.getDeploymentStatus()))
            {
                MyUserDTO user = vendorApiClient.getUserClient().get( subscription.getBuyer() );
                CompanyDTO company = companyClient.get( user.getCompany() );

                company.setMaxPublishedProducts( 0 );
                adminApiClient.getCompanyClient().update( company.getId(), company );

                subscriptionClient.update( subscription.getId(), DeploymentStatusEnum.UNDEPLOYED, null, null);
                log.info("Subscription {} set to UNDEPLOYED", subscription.getId());
            }
        }
    }
}
