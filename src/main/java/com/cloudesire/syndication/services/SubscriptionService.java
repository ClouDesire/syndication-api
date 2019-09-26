package com.cloudesire.syndication.services;

import com.cloudesire.platform.apiclient.dto.model.dto.MyUserDTO;
import com.cloudesire.platform.apiclient.dto.model.dto.SubscriptionDTO;

public interface SubscriptionService
{
    void create( SubscriptionDTO subscription, MyUserDTO user );

    void modify( SubscriptionDTO subscription, MyUserDTO user );

    void undeploy( SubscriptionDTO subscription, MyUserDTO user );
}
