package com.cloudesire.syndication.services.impl;

import com.cloudesire.platform.apiclient.CloudesireClient;
import com.cloudesire.platform.apiclient.CloudesireClientCallExecutor;
import com.cloudesire.platform.apiclient.dto.model.dto.MyUserDTO;
import com.cloudesire.platform.apiclient.dto.model.dto.SubscriptionDTO;
import com.cloudesire.platform.apiclient.dto.model.dto.SubscriptionPatchDTO;
import com.cloudesire.platform.apiclient.dto.model.enums.DeploymentStatus;
import com.cloudesire.platform.apiclient.dto.model.enums.OrderType;
import com.cloudesire.syndication.services.SubscriptionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import static com.cloudesire.platform.apiclient.dto.model.enums.DeploymentStatus.*;

@Component
public class SubscriptionServiceImpl implements SubscriptionService
{
    private static final Logger log = LoggerFactory.getLogger( SubscriptionServiceImpl.class );

    @Resource
    private CloudesireClient cloudesireClient;
    @Resource
    private CloudesireClientCallExecutor callExecutor;

    @Override
    public void create( SubscriptionDTO subscription, MyUserDTO user )
    {
        if ( subscription.getDeploymentStatus() == PENDING )
        {
            if ( subscription.getType().equals( OrderType.TRIAL.toString() ) )
            {
                log.info( "Creating a new tenant for a trial account" );
            }
            else if ( subscription.getType().equals( OrderType.NORMAL.toString() ) &&
                    subscription.isPaid() )
            {
                log.info( "Creating a new paid tenant" );
            }
            updateStatus( DEPLOYED, subscription.getId() );
        }
    }

    @Override
    public void modify( SubscriptionDTO subscription, MyUserDTO user )
    {
        switch ( subscription.getDeploymentStatus() )
        {
            case PENDING:
                if ( subscription.isPaid() )
                {
                    log.info( "Creating a new paid tenant" );
                    updateStatus( DEPLOYED, subscription.getId() );
                }
                break;
            case STOPPED:
                log.info( "Temporarily suspend the subscription" );
                break;
            case DEPLOYED:
                log.info( "Check if tenant is ok" );
                break;
            default:
        }
    }

    @Override
    public void undeploy( SubscriptionDTO subscription, MyUserDTO user )
    {
        log.info( "Unprovision tenant and release resources" );
        updateStatus( UNDEPLOYED, subscription.getId() );
    }

    private void updateStatus( DeploymentStatus status, Integer subscriptionId )
    {
        SubscriptionPatchDTO patch = new SubscriptionPatchDTO();
        patch.setDeploymentStatus( status );
        callExecutor.execute(
                cloudesireClient.getSubscriptionApi().partialUpdate( subscriptionId, patch )
        );
    }
}
