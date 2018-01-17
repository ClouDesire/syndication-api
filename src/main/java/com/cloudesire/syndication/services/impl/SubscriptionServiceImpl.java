package com.cloudesire.syndication.services.impl;

import com.cloudesire.platform.apiclient.CloudesireClient;
import com.cloudesire.platform.apiclient.CloudesireClientCallExecutor;
import com.cloudesire.syndication.services.SubscriptionService;
import com.liberologico.cloudesire.cmw.model.dto.MyUserDTO;
import com.liberologico.cloudesire.cmw.model.dto.SubscriptionDTO;
import com.liberologico.cloudesire.cmw.model.dto.SubscriptionPatchDTO;
import com.liberologico.cloudesire.cmw.model.enums.DeploymentStatusEnum;
import com.liberologico.cloudesire.cmw.model.enums.OrderType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import static com.liberologico.cloudesire.cmw.model.enums.DeploymentStatusEnum.DEPLOYED;
import static com.liberologico.cloudesire.cmw.model.enums.DeploymentStatusEnum.PENDING;
import static com.liberologico.cloudesire.cmw.model.enums.DeploymentStatusEnum.UNDEPLOYED;

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

    private void updateStatus( DeploymentStatusEnum status, Integer subscriptionId )
    {
        SubscriptionPatchDTO patch = new SubscriptionPatchDTO();
        patch.setDeploymentStatus( status.toString() );
        callExecutor.execute(
                cloudesireClient.getSubscriptionApi().partialUpdate( subscriptionId, patch )
        );
    }
}
