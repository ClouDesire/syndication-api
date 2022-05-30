package com.cloudesire.syndication.services;

import com.cloudesire.platform.apiclient.dto.model.dto.MyUserDTO;
import com.cloudesire.platform.apiclient.dto.model.dto.SubscriptionDetailDTO;

public interface SubscriptionService
{
    void create( SubscriptionDetailDTO subscription, MyUserDTO user );

    void modify( SubscriptionDetailDTO subscription, MyUserDTO user );

    void undeploy( SubscriptionDetailDTO subscription, MyUserDTO user );
}
