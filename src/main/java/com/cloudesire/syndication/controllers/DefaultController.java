package com.cloudesire.syndication.controllers;

import com.cloudesire.platform.apiclient.CloudesireClient;
import com.cloudesire.platform.apiclient.CloudesireClientCallExecutor;
import com.cloudesire.platform.apiclient.api.SubscriptionApi;
import com.cloudesire.platform.apiclient.api.UserApi;
import com.cloudesire.platform.apiclient.dto.model.dto.EventNotificationDTO;
import com.cloudesire.platform.apiclient.dto.model.dto.MyUserDTO;
import com.cloudesire.platform.apiclient.dto.model.dto.SubscriptionDetailDTO;
import com.cloudesire.syndication.services.SubscriptionService;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping( "/api" )
public class DefaultController
{
    private static final Logger log = LoggerFactory.getLogger( DefaultController.class );

    @Resource
    private CloudesireClient cloudesireClient;

    @Resource
    private CloudesireClientCallExecutor callExecutor;

    @Resource
    private SubscriptionService subscriptionService;

    @PostMapping( "/event" )
    @ResponseStatus( HttpStatus.NO_CONTENT )
    public void handleEvent( @RequestBody EventNotificationDTO event )
    {
        log.info( "Handling notification for {} with id {} of type {}",
                event.getEntity(),
                event.getId(),
                event.getType() );

        if ( "Subscription".equals( event.getEntity() ) ) handleSubscription( event );
    }

    private void handleSubscription( EventNotificationDTO event )
    {
        SubscriptionApi subscriptionApi = cloudesireClient.getSubscriptionApi();
        SubscriptionDetailDTO subscription = callExecutor.execute( subscriptionApi.get( event.getId() ) );
        UserApi userApi = cloudesireClient.getUserApi();
        MyUserDTO user = callExecutor.execute( userApi.get( subscription.getBuyer().getId() ) );
        switch ( event.getType() )
        {
            case CREATED:
                subscriptionService.create( subscription, user );
                break;
            case MODIFIED:
                subscriptionService.modify( subscription, user );
                break;
            case DELETED:
                subscriptionService.undeploy( subscription, user );
                break;
            default:
        }
    }
}
