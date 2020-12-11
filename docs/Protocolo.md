# Protocolo

## Introduccion
El protocolo solo consiste en un paquete UDP que se envia a la IP Broadcast para notificar la existencia de un servicio. Este paquete no debe recibir respuesta. Dentro de la informacion del paquete se encuentra:
* Un UUID unico para el servicio que se está anunciando: este campo está pensado para que los receptores filtren los paquetes por este UUID.
* La version del servicio: este campo está pensado para que los receptores solo intenten conectarse a los servicios con los que son compatibles.
* Un numero de puerto para conectarse al servicio: si las condiciones anteriores se cumplen, los receptores deberian intentar conectarse a la IP del Beacon y el puerto que se indica en este campo.
* Una descripcion del servicio: Una descripcion opcional con un tamaño maximo de 255 bytes.
* Un campo de informacion extra con un tamaño maximo de 65 535 bytes. Este campo es usado para casos en los que es necesaria mas informacion para establecer la conexion, el programador debe decidir si este campo es necesario. Aunque el limite teorico de este campo es de 65 535 bytes, el limite practico es menor debido a que un paquete UDP tiene un limite de 65 535 bytes totales y ya se usan 20 bytes para el encabezado IP + 8 bytes para el encabezado UDP + los bytes que ya se estan usando para los demas campos de informacion.

## Estructura del paquete
* 128 bits: UUID unico del servicio
* 32 bits: Version del servicio
* 16 bits: Puerto al que conectarse para usar el servicio
* 8 bits: Longitud descripcion en bytes. Puede ser 0.
* X bits(Depende del parametro anterior): Descripcion del servicio
* 16 bits: Longitud extra en bytes. Puede ser 0.
* X bits(Depende del parametro anterior): Informacion extra del servicio
