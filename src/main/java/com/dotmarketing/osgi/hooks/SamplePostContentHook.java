package com.dotmarketing.osgi.hooks;

import com.dotcms.repackage.org.directwebremoting.guice.ApplicationScoped;
import com.dotmarketing.beans.Host;
import com.dotmarketing.beans.Permission;
import com.dotmarketing.exception.DotDataException;
import com.dotmarketing.exception.DotSecurityException;
import com.dotmarketing.portlets.categories.model.Category;
import com.dotmarketing.portlets.contentlet.business.ContentletAPIPostHookAbstractImp;
import com.dotmarketing.portlets.contentlet.business.DotReindexStateException;
import com.dotmarketing.portlets.contentlet.model.Contentlet;
import com.dotmarketing.portlets.structure.model.ContentletRelationships;
import com.dotmarketing.util.Logger;
import com.liferay.portal.model.User;

import java.io.IOException;
import java.net.Socket;
import java.util.List;

public class SamplePostContentHook extends ContentletAPIPostHookAbstractImp {

    private AirlineWebSocketServer serverClass;

    public SamplePostContentHook (AirlineWebSocketServer serverClass) {
        super();

        this.serverClass = serverClass;

    }


    @Override
    public long contentletCount ( long returnValue ) throws DotDataException {

        Logger.info(this, "+++++++++++++++++++++++++++++++++++++++++++++++" );
        Logger.info(this, "INSIDE SamplePostContentHook.contentletCount() -->" + String.valueOf( returnValue ) );
        Logger.info(this, "+++++++++++++++++++++++++++++++++++++++++++++++" );

        return super.contentletCount( returnValue );
    }

    @Override
    public void checkin ( Contentlet currentContentlet, ContentletRelationships relationshipsData, List<Category> cats, List<Permission> selectedPermissions, User user, boolean respectFrontendRoles, Contentlet returnValue ) {

        Logger.info( this, "+++++++++++++++++++++++++++++++++++++++++++++++" );
        Logger.info( this, "INSIDE SamplePostContentHook.checkin()" );
        Logger.info(this, "Current Contentlet ID = "+currentContentlet.getContentTypeId()+"\n ");
        Logger.info(this, "Expected CAtegory ID = \n" +
                "ee20e22c-2dda-4f3c-9ce0-a2b4e16ad4c5 ");
        Logger.info( this, "+++++++++++++++++++++++++++++++++++++++++++++++" );

        //update front-end using socket
        if(currentContentlet.getContentTypeId().equals("ee20e22c-2dda-4f3c-9ce0-a2b4e16ad4c5") ) {

            Logger.info(this, "Found matching contentlet. Send update to the front-end");
            try {
                serverClass.getDataOutputStream().writeChars("Data was written and sent");
            }catch(IOException e){
                e.printStackTrace();
                Logger.info(this, e.toString());
            }
        }

        super.checkin( currentContentlet, relationshipsData, cats, selectedPermissions, user, respectFrontendRoles, returnValue );
    }

}