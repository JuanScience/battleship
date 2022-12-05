/*
Juan Carlos Salazar Muñoz CC 1098607290
Ormolgud Gonzalez Cardona CC 1017923990
*/
package cliente;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Cliente {
    
    //Host del servidor a conectarse
    private static final String HOST = "127.0.0.1";
    //Puerto del servidor a conectarse
    private static final int PUERTO = 54321;
    
    public static void main(String[] args) throws SocketException {
        
        DataInputStream in;
        DataOutputStream out;
        
        try {
            
            //Creo el socket para conectarme con el Cliente
            Socket sc = new Socket(HOST, PUERTO);

            in = new DataInputStream(sc.getInputStream());
            out = new DataOutputStream(sc.getOutputStream());
            
            Scanner teclado = new Scanner(System.in);
            
            //Recibo el mensaje del servidor
            String mensaje = in.readUTF();
            System.out.println(mensaje);
            
            while (true){

                //Envio un mensaje al Cliente
                out.writeUTF(teclado.nextLine());

                mensaje = in.readUTF();
                System.out.println(mensaje);
                
                if(mensaje.contains("Gracias por jugar"))
                    break;
            }
            
            sc.close();
          
        } catch (UnknownHostException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
}
