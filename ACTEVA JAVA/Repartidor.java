// ==================== Repartidor.java ====================
// Archivo: Repartidor.java
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Repartidor {
    public static void main(String[] args) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(System.in))) {
            String linea;
            while ((linea = in.readLine()) != null) {
                if ("FIN".equals(linea)) {
                    break;
                }
                
                System.out.println(linea + " -> entregado");
                System.out.flush();
                
                Thread.sleep(800);
            }
        } catch (InterruptedException e) {
            System.err.println("[Repartidor] Interrumpido");
        } catch (Exception e) {
            System.err.println("[Repartidor] Error: " + e.getMessage());
        }
    }
}