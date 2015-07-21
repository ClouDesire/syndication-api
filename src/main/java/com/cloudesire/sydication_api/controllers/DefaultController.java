package com.cloudesire.sydication_api.controllers;

import com.cloudesire.tisana4j.exceptions.RestException;
import com.cloudesire.tisana4j.exceptions.RuntimeRestException;
import com.liberologico.cloudesire.cmw.model.dto.CompanyDTO;
import com.liberologico.cloudesire.cmw.model.dto.EventDTO;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping ( "/api" )
public class DefaultController
{
    private static final Logger log = LoggerFactory.getLogger( DefaultController.class );

    @Resource
    private CmwRestClient apiClient;

    @RequestMapping ( "/event" )
    @ResponseStatus ( HttpStatus.NO_CONTENT )
    public void handleEvent( @RequestBody EventDTO event ) throws RuntimeRestException, RestException
    {
        log.debug( "Received {} {} {}", event.getEntityName(), event.getEntityId(), event.getType() );

        if ( "Subscription".equals( event.getEntityName() ) ) handleSubscription( event );

        if ( "Invoice".equals( event.getEntityName() ) ) handleInvoice( event );
    }

    private void handleInvoice( EventDTO event ) throws RuntimeRestException, RestException
    {
        if ( CmwEventType.MODIFIED.equals( event.getType() ) )
        {
            final InvoiceClient invoiceClient = apiClient.getInvoiceClient();
            final SubscriptionClient subscriptionClient = apiClient.getSubscriptionClient();
            final CompanyClient companyClient = apiClient.getCompanyClient();

            InvoiceDTO invoice = invoiceClient.get( event.getEntityId() );
            SubscriptionDTO subscription = subscriptionClient.get( invoice.getSubscription() );

            if (invoice.isPaid() && subscription.getDeploymentStatus().equals( DeploymentStatusEnum.PENDING ))
            {
                invoice.getNominee().getUrl();
                MyUserDTO user = apiClient.getUserClient().get( invoice.getNominee() );
                CompanyDTO company = companyClient.get( user.getCompany() );

                ProductVersionDTO productVersion = apiClient.getProductVersionClient()
                        .get( subscription.getProductVersion() );
                if (productVersion.getName().equalsIgnoreCase( "Premium" ))
                    company.setMaxPublishedProduct( 100 );
                else
                    company.setMaxPublishedProduct( 1 );

                companyClient.update( company.getId(), company );
                log.info( "Set maxPublishedProduct={} companyName={} companyId={}", company.getMaxPublishedProduct(),
                        company.getId(), company.getName() );

                subscriptionClient.update( subscription.getId(), DeploymentStatusEnum.DEPLOYED, null, null );
                log.debug( "Set DEPLOYED to subscription {}", subscription.getId() );
            }
        }
    }

    private void handleSubscription( EventDTO event ) throws RuntimeRestException, RestException
    {
        if (CmwEventType.MODIFIED.equals( event.getType()) || CmwEventType.DELETED.equals( event.getType()))
        {
            final SubscriptionClient subscriptionClient = apiClient.getSubscriptionClient();
            final CompanyClient companyClient = apiClient.getCompanyClient();

            SubscriptionDTO subscription = subscriptionClient.get( event.getEntityId() );
            if (subscription.getDeploymentStatus().equals( DeploymentStatusEnum.UNDEPLOY_SENT ))
            {
                MyUserDTO user = apiClient.getUserClient().get( subscription.getBuyer() );
                CompanyDTO company = companyClient.get( user.getCompany() );
                company.setMaxPublishedProduct( 0 );
                companyClient.update( company.getId(), company );
                subscription.setDeploymentStatus( DeploymentStatusEnum.UNDEPLOYED );
            }
        }
    }
}
