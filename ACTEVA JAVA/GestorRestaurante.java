import java.io.*;

public class GestorRestaurante {
    public static void main(String[] args) throws Exception {
        System.out.println("[Gestor] El restaurante ha abierto.");
        System.out.flush();

        // Intentar múltiples formas de encontrar las clases
        String classpath = obtenerClasspath();
        System.out.println("[Gestor] Usando classpath: " + classpath);
        
        ProcessBuilder clientePB = new ProcessBuilder("java", "-cp", classpath, "Cliente");
        ProcessBuilder cocineroPB = new ProcessBuilder("java", "-cp", classpath, "Cocinero");
        ProcessBuilder repartidorPB = new ProcessBuilder("java", "-cp", classpath, "Repartidor");

        clientePB.redirectErrorStream(true);
        cocineroPB.redirectErrorStream(true);
        repartidorPB.redirectErrorStream(true);

        Process clienteProc = clientePB.start();
        Process cocineroProc = cocineroPB.start();
        Process repartidorProc = repartidorPB.start();

        BufferedReader clienteOut = new BufferedReader(
            new InputStreamReader(clienteProc.getInputStream())
        );
        BufferedWriter cocineroIn = new BufferedWriter(
            new OutputStreamWriter(cocineroProc.getOutputStream())
        );

        BufferedReader cocineroOut = new BufferedReader(
            new InputStreamReader(cocineroProc.getInputStream())
        );
        BufferedWriter repartidorIn = new BufferedWriter(
            new OutputStreamWriter(repartidorProc.getOutputStream())
        );

        BufferedReader repartidorOut = new BufferedReader(
            new InputStreamReader(repartidorProc.getInputStream())
        );

        Thread hiloClienteCocinero = new Thread(() -> {
            try {
                String linea;
                while ((linea = clienteOut.readLine()) != null) {
                    System.out.println("[Gestor] Cliente genera: " + linea);
                    System.out.flush();
                    
                    cocineroIn.write(linea);
                    cocineroIn.newLine();
                    cocineroIn.flush();
                    
                    if ("FIN".equals(linea)) {
                        break;
                    }
                }
            } catch (IOException e) {
                System.err.println("[Gestor] Error en pipeline Cliente->Cocinero: " + e.getMessage());
            } finally {
                try { cocineroIn.close(); } catch (Exception ignored) {}
                try { clienteOut.close(); } catch (Exception ignored) {}
            }
        });

        Thread hiloCocineroRepartidor = new Thread(() -> {
            try {
                String linea;
                while ((linea = cocineroOut.readLine()) != null) {
                    System.out.println("[Gestor] Cocinero prepara: " + linea);
                    System.out.flush();
                    
                    repartidorIn.write(linea);
                    repartidorIn.newLine();
                    repartidorIn.flush();
                    
                    if ("FIN".equals(linea)) {
                        break;
                    }
                }
            } catch (IOException e) {
                System.err.println("[Gestor] Error en pipeline Cocinero->Repartidor: " + e.getMessage());
            } finally {
                try { repartidorIn.close(); } catch (Exception ignored) {}
                try { cocineroOut.close(); } catch (Exception ignored) {}
            }
        });

        Thread hiloRepartidorSalida = new Thread(() -> {
            try {
                String linea;
                while ((linea = repartidorOut.readLine()) != null) {
                    System.out.println("[Gestor] Repartidor: " + linea);
                    System.out.flush();
                }
            } catch (IOException e) {
                System.err.println("[Gestor] Error leyendo salida del Repartidor: " + e.getMessage());
            } finally {
                try { repartidorOut.close(); } catch (Exception ignored) {}
            }
        });

        hiloClienteCocinero.start();
        hiloCocineroRepartidor.start();
        hiloRepartidorSalida.start();

        int exitCliente = clienteProc.waitFor();
        int exitCocinero = cocineroProc.waitFor();
        int exitRepartidor = repartidorProc.waitFor();

        hiloClienteCocinero.join();
        hiloCocineroRepartidor.join();
        hiloRepartidorSalida.join();

        System.out.println("\n[Gestor] El restaurante ha cerrado.");
        System.out.println("[Gestor] Códigos de salida -> Cliente: " + exitCliente + 
                           ", Cocinero: " + exitCocinero + ", Repartidor: " + exitRepartidor);
    }
    
    private static String obtenerClasspath() {
        // Primero intentar encontrar donde está la clase GestorRestaurante
        String rutaClase = GestorRestaurante.class.getProtectionDomain()
                                                    .getCodeSource()
                                                    .getLocation()
                                                    .getPath();
        
        // Si la ruta contiene un archivo .class, usar el directorio padre
        File archivoClase = new File(rutaClase);
        if (archivoClase.isFile()) {
            return archivoClase.getParent();
        }
        
        // Si no, intentar con el directorio actual
        String dirActual = System.getProperty("user.dir");
        File dirActualFile = new File(dirActual);
        
        // Verificar si las clases están en el directorio actual
        if (new File(dirActualFile, "Cliente.class").exists()) {
            return dirActual;
        }
        
        // Como último recurso, usar el classpath actual del sistema
        String classpathSistema = System.getProperty("java.class.path");
        String[] paths = classpathSistema.split(File.pathSeparator);
        
        for (String path : paths) {
            File dir = new File(path);
            if (dir.isDirectory() && new File(dir, "Cliente.class").exists()) {
                return path;
            }
        }
        
        // Si nada funciona, usar el directorio actual
        return dirActual;
    }
}