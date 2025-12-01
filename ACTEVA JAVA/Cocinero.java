// ==================== Cocinero.java ====================
// Archivo: Cocinero.java
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Cocinero {
    public static void main(String[] args) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(System.in))) {
            String linea;
            while ((linea = in.readLine()) != null) {
                if ("FIN".equals(linea)) {
                    System.out.println("FIN");
                    System.out.flush();
                    break;
                }
                
                String listo = linea + " (listo)";
                System.out.println(listo);
                System.out.flush();
                
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            System.err.println("[Cocinero] Interrumpido");
        } catch (Exception e) {
            System.err.println("[Cocinero] Error: " + e.getMessage());
        }
    }
}