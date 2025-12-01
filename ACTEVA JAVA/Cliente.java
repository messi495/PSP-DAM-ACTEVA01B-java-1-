// ==================== Cliente.java ====================
// Archivo: Cliente.java
public class Cliente {
    public static void main(String[] args) {
        int numPedidos = 5;
        try {
            for (int i = 1; i <= numPedidos; i++) {
                String pedido = "Pedido-" + i;
                System.out.println(pedido);
                System.out.flush();
                Thread.sleep(500);
            }
            System.out.println("FIN");
            System.out.flush();
        } catch (InterruptedException e) {
            System.err.println("[Cliente] Interrumpido");
        }
    }
}
