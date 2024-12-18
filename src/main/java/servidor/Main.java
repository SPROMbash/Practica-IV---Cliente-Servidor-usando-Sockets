package servidor;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    private final static int PUERTO = 12345;
    private final static ExecutorService threadPool = Executors.newFixedThreadPool(3);

    public static void main(String[] args) {
        try {
            ServerSocket server = new ServerSocket(PUERTO);
            System.out.println("Servidor iniciado en el puerto 1234");
            while (true) {
                Socket socket = server.accept();
                ManejoCliente manejador = new ManejoCliente(socket);
                Thread hilo = new Thread(manejador);
                threadPool.execute(hilo);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
