package utils;

import java.util.HashMap;
import java.util.Map;


public class MessageHolder {

    public static String watchedId;
    /**
     * Singleton de {@link MessageHolder}.
     */
    public static MessageHolder defaultHolder;

    /**
     * {@link Map} que contendrá los objetos {@link MessageData} que encapsulan los mensajes
     * generados por un {@link Thread}.
     */
    protected static Map< Thread, MessageData > messages;

    static {
        messages = new HashMap< Thread, MessageData >();

        defaultHolder = new MessageHolder();
    }

    /**
     * Devuelve el objeto {@link MessageData} asociado con el {@link Thread} que está llamando el método.
     * @return el objeto {@link MessageData} asociado con el {@link Thread} que está llamando el método.
     */
    public static MessageData getData() {
        return defaultHolder.getMessageData();
    }

    public static void printData( String id ) {
        MessageData data = defaultHolder.getMessageData();

//		if ( Logger.isDebugEnabled() ) {
        //if ( watchedId == null || watchedId.equalsIgnoreCase( id ) ) {
        if ( data != null ) {

            if ( data.getRequest() != null )
            {

            }

            if ( data.getResponse() != null ){

            }

        } else{

        }

        //}
/*		} else if ( watchedId != null && watchedId.equalsIgnoreCase( id ) ) {
			if ( data != null ) {
				Logger.error( "-----------------------------------" );
				Logger.error( "elapsed  %d", data.getElapsedTime() );

				if ( data.getRequest() != null )
					Logger.error( "request  %s", data.getRequest().replaceAll( "&gt;", ">" ).replaceAll( "&lt;", "<" ) );

				if ( data.getResponse() != null )
					Logger.error( "response %s", data.getResponse().replaceAll( "&gt;", ">" ).replaceAll( "&lt;", "<" ) );
			} else
				Logger.error( "No recorded webservices data from services" );
		}*/
    }

    public static void printDataOnError() {
        MessageData data = defaultHolder.getMessageData();

        if ( data != null ) {


            if ( data.getRequest() != null ){

            }


            if ( data.getResponse() != null ){

            }

        } else{

        }

    }

    /**
     * Devuelve el objeto {@link MessageData} asociado con el {@link Thread} que está llamando el método.
     * @return el objeto {@link MessageData} asociado con el {@link Thread} que está llamando el método.
     */
    public MessageData getMessageData() {
        return messages.remove( Thread.currentThread() );
    }

    /**
     * Concatena a los mensajes enviados a los servicios de la entidad financiera el valor recibido por el parámetro
     * <code>request</code> para el {@link Thread} que hace la llamada a este método.
     * @param request un nuevo mensaje enviado por este {@link Thread} a los servicios de la entidad financiera.
     */
    public void setInternalRequest( String request ) {
        MessageData data = messages.get( Thread.currentThread() );

        if ( data == null ) {
            data = new MessageData();

            messages.put( Thread.currentThread(), data );
        }

        if ( data.getRequest() == null )
            data.setRequest( request );
        else
            data.setRequest( data.getRequest() + '\n' + request );
    }

    /**
     * Concatena a los mensajes recibidos por los servicios de la entidad financiera el valor recibido por el parámetro
     * <code>response</code> para el {@link Thread} que hace la llamada a este método.
     * @param response un nuevo mensaje recibido por este {@link Thread} de los servicios de la entidad financiera.
     */
    public void setInternalResponse( String response ) {
        MessageData data = messages.get( Thread.currentThread() );

        if ( data == null ) {
            data = new MessageData();

            messages.put( Thread.currentThread(), data );
        }

        if ( data.getResponse() == null )
            data.setResponse( response );
        else
            data.setResponse( data.getResponse() + '\n' + response );

        //Logger.info( "\tinternal response: {}", response );
    }

    /**
     * Guarda el tiempo de inicio de una llamada a los servicios de la entidad financiera para posteriormente calcula el tiempo
     * que tomó ejecutar la llamada.
     */
    public void startTime() {
        MessageData data = messages.get( Thread.currentThread() );

        if ( data == null ) {
            data = new MessageData();
            messages.put( Thread.currentThread(), data );
        }

        data.setTmpTime( System.currentTimeMillis() );
    }

    /**
     * Guarda el tiempo de finalización de una llamada a los servicios de la entidad financiera y calcula el tiempo
     * que tomó ejecutar la llamada.
     */
    public void endTime() {
        MessageData data = messages.get( Thread.currentThread() );
        if(data != null) {
            data.addTime();
        }
    }

    /**
     * Clase que encapsula los mensajes de solicitud y respuesta enviados hacia y por los servicios de la entidad financiera.
     *
     * @author clavarreda
     */
    public static class MessageData {
        /**
         * Los mensajes de solicitud concatenados.
         */
        protected String request;

        /**
         * Los mensajes de respuesa concatenados.
         */
        protected String response;

        /**
         * Variable temporal utilizada para guardar el tiempo de inicio de llamada de un servicio.
         */
        protected long tmpTime;

        /**
         * Tiempo total de las llamadas de los servicios por el {@link Thread} que realiza la llamada.
         */
        protected int elapsedTime;

        /**
         * Suma <code>System.currentTimeMillis() - tmpTime</code> a el tiempo total de ejecución de servicios.
         */
        public void addTime() {
            elapsedTime += ( int ) ( System.currentTimeMillis() - tmpTime );

            tmpTime = 0;
        }

        /**
         * Devuelve los mensajes de solicitud concatenados.
         * @return los mensajes de solicitud concatenados.
         */
        public String getRequest() {
            return request;
        }

        /**
         * Asigna los mensajes de solicitud concatenados.
         * @param request los mensajes de solicitud concatenados.
         */
        public void setRequest( String request ) {
            this.request = request;
        }

        /**
         * Devuelve los mensajes de respuesa concatenados.
         * @return los mensajes de respuesa concatenados.
         */
        public String getResponse() {
            return response;
        }

        /**
         * Asigna los mensajes de respuesa concatenados.
         * @param response los mensajes de respuesa concatenados.
         */
        public void setResponse( String response ) {
            this.response = response;
        }

        /**
         * Devuelve el tiempo total de las llamadas de los servicios por el {@link Thread} que realiza la llamada.
         * @return el tiempo total de las llamadas de los servicios por el {@link Thread} que realiza la llamada.
         */
        public int getElapsedTime() {
            return elapsedTime;
        }

        /**
         * Asigna el tiempo total de las llamadas de los servicios por el {@link Thread} que realiza la llamada.
         * @param elapsedTime el tiempo total de las llamadas de los servicios por el {@link Thread} que realiza la llamada.
         */
        public void setElapsedTime( int elapsedTime ) {
            this.elapsedTime = elapsedTime;
        }

        /**
         * Devuelve la variable temporal utilizada para guardar el tiempo de inicio de llamada de un servicio.
         * @return la variable temporal utilizada para guardar el tiempo de inicio de llamada de un servicio.
         */
        public long getTmpTime() {
            return tmpTime;
        }

        /**
         * Asigna la variable temporal utilizada para guardar el tiempo de inicio de llamada de un servicio.
         */
        public void setTmpTime( long tmpTime ) {
            this.tmpTime = tmpTime;
        }
    }
}
