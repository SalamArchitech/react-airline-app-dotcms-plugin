package com.dotmarketing.osgi.hooks;

import com.dotmarketing.business.APILocator;
import com.dotmarketing.exception.DotDataException;
import com.dotmarketing.exception.DotSecurityException;
import com.dotmarketing.portlets.contentlet.business.Contentlet;
import com.dotmarketing.portlets.contentlet.business.ContentletAPI;
import com.liferay.portal.model.User;

import java.util.List;

public class AirlineContentletService {

    private final ContentletAPI contentletAPI;
    private final User user;

    public AirlineContentletService(ContentletAPI contentletAPI, User user){
        this.contentletAPI = contentletAPI;
        this.user = user;
    }

    public AirlineContentletService(){
        this(APILocator.getContentletAPI(), APILocator.systemUser());
    }

    public List<Contentlet> findAllAirlineListing(){
        String query = String.format("+contentType:%s", "Airlinetimings");
        try {
            contentletAPI.search(query, 0, -1, null, user, true);
        } catch (DotDataException e) {
            e.printStackTrace();
        } catch (DotSecurityException e) {
            e.printStackTrace();
        }
        return null;
    }

}
