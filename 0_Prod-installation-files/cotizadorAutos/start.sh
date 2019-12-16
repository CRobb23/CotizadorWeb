##/bin/bash

cd /opt/elroble-cotizador-web/cotizador

/opt/elroble-cotizador-web/play-1.4.5/play stop

rm -f server.pid

if [ ! -d "logs" ];
then
	mkdir logs
fi

/opt/elroble-cotizador-web/play-1.4.5/play clean
/opt/elroble-cotizador-web/play-1.4.5/play start -Duser.timezone=GMT-6 -Xms6144m -Xmx8192m -XX:MaxPermSize=1024m
