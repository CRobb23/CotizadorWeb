package utils;

import java.lang.reflect.Constructor;
import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebServiceClient;

import play.Logger;
import play.Play;

public class WSClientFactory {

    private static MultiInstanceCache serviceCache;

    private static ServiceURLProvider serviceURLProvider;

    private static int cacheSize;
    private static int cacheCleanInterval;

    protected static MultiInstanceCache getServiceCache() {
        if ( serviceCache == null ) {

            if ( getCacheSize() == 0 )
                setCacheSize( 30 );

            if ( getCacheCleanInterval() == 0 )
                setCacheCleanInterval( 3600 );

            serviceCache = new MultiInstanceCache( "WSClients", getCacheSize(), getCacheCleanInterval() );
        }

        return serviceCache;
    }

    public static void cleanup() {
        getServiceCache().cleanup();
    }

    @SuppressWarnings("unchecked")
    public static < P > P getService( Class< ? > serviceClass, Class< P > portClass) {
        String mapKey = serviceClass.getName();
        Object port = getServiceCache().get( mapKey );

        if ( port == null ) {
            Service service;

            try {
                service = ( Service ) createService( serviceClass );
            } catch ( Exception e ) {
                throw new RuntimeException( e );
            }

            port = service.getPort( portClass );
        }

        MessageHolder.defaultHolder.startTime();

        return ( P ) port;
    }

    public static void closeService( Class< ? > serviceClass, Object port ) {
        getServiceCache().put( serviceClass.getName(), port );
        MessageHolder.defaultHolder.endTime();
    }

    private static < T > T createService( Class< T > serviceClass ) throws Exception {
        Constructor< T > constructor =  serviceClass.getConstructor( URL.class, QName.class );

        WebServiceClient annotation = serviceClass.getAnnotation( WebServiceClient.class );

        String url;

        if ( serviceURLProvider != null ) {
            url = serviceURLProvider.getWSDLURLFor( annotation.wsdlLocation() );
        } else {
            url = annotation.wsdlLocation();
        }
        Logger.info("PATH: " + Play.applicationPath.getCanonicalPath());
        Logger.info("URL: " + url);
        URL wsdlURL = new URL( "file:///" + Play.applicationPath.getCanonicalPath() + "/" + url );
        T service = constructor.newInstance( wsdlURL, new QName( annotation.targetNamespace(), annotation.name() ) );

        ( ( Service ) service ).setHandlerResolver( JAXWSHandlerResolver.handlerResolver );

        return service;
    }

    public static int getCacheSize() {
        return cacheSize;
    }

    public static void setCacheSize( int cacheSize ) {
        WSClientFactory.cacheSize = cacheSize;
    }

    public static int getCacheCleanInterval() {
        return cacheCleanInterval;
    }

    public static void setCacheCleanInterval( int cacheCleanInterval ) {
        WSClientFactory.cacheCleanInterval = cacheCleanInterval;
    }

    public static ServiceURLProvider getServiceURLProvider() {
        return serviceURLProvider;
    }

    public static void setServiceURLProvider( ServiceURLProvider serviceURLProvider ) {
        WSClientFactory.serviceURLProvider = serviceURLProvider;
    }

    public interface ServiceURLProvider {
        String getWSDLURLFor( String wsdlName );
    }

}
