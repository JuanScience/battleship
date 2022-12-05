package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.IOException;
import java.net.SocketException;

public class Server {

    private static final int PUERTO = 54321;

    public static void main(String[] args) throws InterruptedException {

        ServerSocket servidor;
        Socket sc;
        DataInputStream in;
        DataOutputStream out;
        
        System.out.println("Inciniando server...");

        try {
            
            //Creamos el socket del servidor
            servidor = new ServerSocket(PUERTO);
            System.out.println("Servidor iniciado");
            //Siempre estara escuchando peticiones

            while (true) {
                System.out.println("Servidor esperando");
                //Espero a que un cliente se conecte
                sc = servidor.accept();
                System.out.println("Cliente(" + sc.getPort() + ") conectado");
                in = new DataInputStream(sc.getInputStream());
                out = new DataOutputStream(sc.getOutputStream());
                
                int[][] ships = new int[9][4];
            
                //Se generan las posiciones de los barcos con tres hilos
                ShipHilo ship0 = new ShipHilo(0, ships);
                ShipHilo ship1 = new ShipHilo(1, ships);
                ShipHilo ship2 = new ShipHilo(2, ships);
                ship0.start();
                ship1.start();
                ship2.start();
                
                //Esperamos que terminen los tres hilos
                ship0.join();
                ship1.join();
                ship2.join();
                
                //Validamos casillas entre los barcos repetidas
                int confirm = 0;
                while(confirm < 12){
                    confirm = 0;
                    for (int c = 0; c < ships.length; c++) {//Todas las posiciones del arreglo
                        for (int d = 0; d < 3; d++) {
                            if(ships[3 + d][0] == ships[c][0] && ships[3 + d][1] == ships[c][1] && 3 + d != c){
                                ship1 = new ShipHilo(1, ships);
                                ship1.start();
                                ship1.join();
                            }else {
                                confirm++;
                            }
                            if(ships[6 + d][0] == ships[c][0] && ships[6 + d][1] == ships[c][1] && 6 + d != c){
                                ship2 = new ShipHilo(2, ships);
                                ship2.start();
                                ship2.join();
                            }else {
                                confirm++;
                            }
                        }                        
                    }
                }
                
                //Imprimimos del lado del servidor los barcos resultantes
                System.out.println("Ships: ");
                for (int i = 0; i < ships.length; i++) {
                    System.out.println(ships[i][0] + "-" + ships[i][1] + "-" + ships[i][2]);                    
                }
                
                ServidorHilo hilo = new ServidorHilo(sc, in, out, ships);
                hilo.start();
                System.out.println("Hilo del cliente(" + sc.getPort() + ") creado");
                
            }
        } catch (SocketException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    
}
