# PSP-DAM-ACTEVA01B-java-1-
Simulación de restaurante con procesos en Java
Este proyecto simula el flujo de pedidos de un restaurante usando tres procesos Java (Cliente, Cocinero, Repartidor) coordinados por un proceso principal (GestorRestaurante) mediante tuberías (pipes) y hebras.

Descripción general
Flujo básico:

Cliente genera pedidos: Pedido-1, Pedido-2, … y al final envía FIN.

Cocinero recibe cada línea por entrada estándar, marca el pedido como listo (Pedido-1 (listo)), y reenvía; cuando recibe FIN, lo reenvía y termina.

Repartidor recibe los pedidos listos, muestra que han sido entregados (Pedido-1 (listo) -> entregado) y termina al recibir FIN.

GestorRestaurante lanza los tres procesos, conecta sus entradas/salidas y muestra el seguimiento del flujo.

Archivos y clases
Cliente.java

Método main:

Genera numPedidos (por defecto 5).

Imprime Pedido-i cada 500 ms.

Al final imprime FIN.

Cocinero.java

Lee líneas de System.in.

Si la línea es FIN: imprime FIN y termina.

En otro caso: añade (listo) y la imprime, esperando 1 s entre pedidos.

Repartidor.java

Lee líneas de System.in.

Si es FIN: termina.

Si no: imprime <pedido> -> entregado y espera 800 ms.

GestorRestaurante.java

Lanza los procesos Cliente, Cocinero y Repartidor con ProcessBuilder.

Calcula el classpath con obtenerClasspath().

Crea tres hebras:

hiloClienteCocinero: conecta salida de Cliente con entrada de Cocinero.

hiloCocineroRepartidor: conecta salida de Cocinero con entrada de Repartidor.

hiloRepartidorSalida: muestra la salida del Repartidor.

Espera a que terminen los procesos y muestra sus códigos de salida.

Requisitos
Java 8 o superior.

Todos los .java en el mismo directorio (o correctamente empaquetados).

Compilación
En el directorio donde están los archivos:

bash
javac Cliente.java Cocinero.java Repartidor.java GestorRestaurante.java
Ejecución
Ejecutar el gestor:

bash
java GestorRestaurante
Verás mensajes indicando:

Apertura del restaurante y classpath usado.

Pedidos generados por Cliente.

Pedidos preparados por Cocinero.

Pedidos entregados por Repartidor.

Cierre del restaurante y códigos de salida.
