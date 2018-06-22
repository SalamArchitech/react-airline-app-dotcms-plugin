package com.dotmarketing.osgi.hooks;

import com.dotcms.repackage.org.directwebremoting.guice.ApplicationScoped;
import com.dotmarketing.business.APILocator;
import com.dotmarketing.loggers.Log4jUtil;
import com.dotmarketing.osgi.GenericBundleActivator;
import com.dotmarketing.portlets.contentlet.business.ContentletAPI;
import com.dotmarketing.util.Logger;
import com.dotcms.repackage.org.apache.logging.log4j.LogManager;
import com.dotcms.repackage.org.apache.logging.log4j.core.LoggerContext;
import org.osgi.framework.BundleContext;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;


/**
 * Created by Jonathan Gamba
 * Date: 6/18/12
 */
public class Activator extends GenericBundleActivator {

    private LoggerContext pluginLoggerContext;
    private SocketThread aSocketThread;

    public Activator() throws IOException {
//        aSocketThread = new SocketThread(this);
    }


    @SuppressWarnings ("unchecked")
    public void start ( BundleContext context ) throws Exception {

        //Initializing log4j...
        LoggerContext dotcmsLoggerContext = Log4jUtil.getLoggerContext();
        //Initialing the log4j context of this plugin based on the dotCMS logger context
        pluginLoggerContext = (LoggerContext) LogManager.getContext(this.getClass().getClassLoader(),
                false,
                dotcmsLoggerContext,
                dotcmsLoggerContext.getConfigLocation());

        //Initializing services...
        initializeServices ( context );

        this.aSocketThread = new SocketThread(this);

        //Start the webSocket thread
        this.aSocketThread.start();


        //instantiate Hook classes
        SamplePostContentHook sPC = new SamplePostContentHook(this.aSocketThread.getSocketServerClass());


        //Adding hooks
        addPostHook(sPC);
//        addPreHook( Class.forName( SamplePreContentHook.class.getName() ).newInstance() );


        //Testing the hooks
        ContentletAPI conAPI = APILocator.getContentletAPI();

        Long count = conAPI.contentletCount();
        Logger.info(this, "+++++++++++++++++++++++++++++++++++++++++++++++");
        Logger.info(this, "ContentletAPI.contentletCount() = " + count);
        Logger.info(this, "+++++++++++++++++++++++++++++++++++++++++++++++");



    }


    public void stop ( BundleContext context ) throws Exception {

        //stop the thread
        this.aSocketThread.interrupt();

        unregisterServices( context );

        //Shutting down log4j in order to avoid memory leaks
        Log4jUtil.shutdown(pluginLoggerContext);
    }

}