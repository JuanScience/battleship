package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ServidorHilo extends Thread {
    private final DataInputStream in;
    private final DataOutputStream out;
    private final Socket sc;
    private int[][] ships;
    private int turn;
    private int[][] boardMatrix;
    private String board;            
    private String status;
    private String nextLine;

    public ServidorHilo(Socket sc, DataInputStream in, DataOutputStream out, int[][] ships) {
        this.sc = sc;
        this.in = in;
        this.out = out;
        this.ships = ships;
        this.turn = 1;
        this.board = "";
        this.status = "";
        this.nextLine = "";
    }
    
    @Override
    public void run(){
        
        try {
            boardMatrix = new int[9][9];
            board = imprimirMatriz(boardMatrix);
            //Le envio un mensaje
            out.writeUTF("¡Bienvenido Juagador a Batalla Naval!:\n\nSu turno es "
                    + "el: " + turn + "\n" + board +
                    "\nPor favor ingrese una posición válida: ");

            while (turn <= 15){
                turn++;
                //Leo el mensaje que me envia
                String mensaje = in.readUTF();
                System.out.println("Cliente(" + sc.getPort() + ") turno(" + turn + ")>> " + mensaje);
                if(validar(mensaje)){
                    String[] parts = mensaje.split(",");
                    int x = Integer.parseInt(parts[0]);
                    int y = Integer.parseInt(parts[1]);
                    int answer = hiting(x, y);
                    if(answer < 3){//Si impacta
                        status = updateStatus(answer);
                        boardMatrix[x][y] = 2;
                        
                    }else{
                        status = "Has fallado";
                        if(boardMatrix[x][y] == 0)
                            boardMatrix[x][y] = 1;
                    }
                    board = imprimirMatriz(boardMatrix);
                    nextLine = findNext();
                    out.writeUTF("\n" + status + "\n" + board
                            + "\nSu turno es el: " + turn + nextLine);
                }else{
                    out.writeUTF("Favor ingresar una posición válida\n"
                            + "(Dos valores entre 0 y 8 separados por una coma)");
                }    
            }
            //Cierro el socket
            sc.close();
            System.out.println("Cliente desconectado");
        } catch (SocketException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public String imprimirMatriz(int[][] matrix) { //Devuelve un string formateado para imprimir la matriz
        String ans = "";
        for (int[] m : matrix) {
            ans = ans + "[";
            for (int j = 0; j < matrix[0].length; j++) {
                ans = ans + " " + m[j] + " ";
            }
            ans = ans + "]\n";
        }
        return ans;
    }

    public boolean validar(String mensaje) { //Valida con expresiones regulares si hay dos dígitos del 0 al 9 separados por coma
        boolean answer;
        Pattern pat = Pattern.compile("[0-8],[0-8]");
        Matcher mat = pat.matcher(mensaje);                                                                           
        answer = mat.matches();
        return answer;
    }
    
    public int hiting(int x, int y) { //Valida si se ha acertado a alguna posición
        int answer = 3;
        for (int i = 0; i < ships.length; i++) {
            if (ships[i][0] == x && ships[i][1] == y && ships[i][2] == 0) {
                ships[i][2] = 1;
                answer = ships[i][3];
            }
        }
        return answer;        
    }

    public String updateStatus(int answer) {
        int hits = 0;
        String stat = "Ha acertado al barco " + answer;
        for (int i = 0; i < 3; i++) {
            if(ships[answer * 3 + i][2] == 1){
                hits++;
            }
        }
        switch (hits) {
            case 3:
                stat = stat + " y le ha destruído.";
                break;
            case 1:
                stat = stat + " y faltan dos aciertos";
                break;
            case 2:
                stat = stat + " y falta un acierto";
                return stat;
            default:
                break;
        }
        return stat;
    }

    public String findNext() {
        String ans = "";
        int countDown = 0;
        System.out.println("Ingresa");
        for (int[] ship : ships) {
            if (ship[2] == 1) {
                countDown++;
            }
        }
        System.out.println("CountDoown = " + countDown);
        if(countDown == 9)
            ans = "\nFelicitaciones, ha ganado. Gracias por jugar.";
        else if(turn == 15)
            ans = "\nEn otra ocación será, ha perdido. Gracias por jugar.";
        else
            ans = "\nPor favor ingrese una posición válida: ";
        return ans;
    }
}
