package com.cloudesire.syndication.services;

import com.liberologico.cloudesire.cmw.model.dto.MyUserDTO;
import com.liberologico.cloudesire.cmw.model.dto.SubscriptionDTO;

public interface SubscriptionService
{
    void create( SubscriptionDTO subscription, MyUserDTO user );

    void modify( SubscriptionDTO subscription, MyUserDTO user );

    void undeploy( SubscriptionDTO subscription, MyUserDTO user );
}
