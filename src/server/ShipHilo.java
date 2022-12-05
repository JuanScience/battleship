package server;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ShipHilo extends Thread{
    private final int barco;
    private int[][] ships;

    public ShipHilo(int barco, int[][] ships) {
        this.barco = barco;
        this.ships = ships;
    }
    
    @Override
    public void run(){
        try {
            
            ships = generateShip(barco, ships);
            
        }catch (Exception ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public int[][] generateShip(int barco, int[][] ships) {
        int i = (int)(Math.random() * 9);
        int j = (int)(Math.random() * 9);
        int axis = (int)(Math.random() * 2);
        int direction = (int)(Math.random() * 2);
        if(axis == 0){
            if((direction == 0 && i - 2 < 0) || (direction == 1 && i + 2 < 8)){ //Vertical hacia arriba
                for (int k = 0; k < 3; k++) {
                    ships[barco * 3 + k][0] = i + k;
                    ships[barco * 3 + k][1] = j;
                    ships[barco * 3 + k][2] = 0;
                    ships[barco * 3 + k][3] = barco;
                }
            }else{ //Vertical hacia abajo
                for (int k = 0; k < 3; k++) {
                    ships[barco * 3 + k][0] = i - k;
                    ships[barco * 3 + k][1] = j;
                    ships[barco * 3 + k][2] = 0;
                    ships[barco * 3 + k][3] = barco;
                }
            }
        }else{
            if((direction == 0 && j - 2 < 0) || (direction == 1 && j + 2 < 8)){ //Horizontal hacia la izquierda
                for (int k = 0; k < 3; k++) {
                    ships[barco * 3 + k][0] = i;
                    ships[barco * 3 + k][1] = j + k;
                    ships[barco * 3 + k][2] = 0;
                    ships[barco * 3 + k][3] = barco;
                }
            }else{ //Horizontal hacia la derecha
                for (int k = 0; k < 3; k++) {
                    ships[barco * 3 + k][0] = i;
                    ships[barco * 3 + k][1] = j - k;
                    ships[barco * 3 + k][2] = 0;
                    ships[barco * 3 + k][3] = barco;
                }
            }
        }
        return ships;
    }
}
