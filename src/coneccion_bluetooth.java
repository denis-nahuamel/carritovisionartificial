
import com.fazecast.jSerialComm.SerialPort;
import java.io.IOException;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author neto
 */
public class coneccion_bluetooth {
    String puerto;
    int velocidad;
    SerialPort sp;

    public coneccion_bluetooth(String puerto, int velocidad) {
        this.puerto = puerto;
        this.velocidad = velocidad;        
    }

    public String getPuerto() {
        return puerto;
    }

    public void setPuerto(String puerto) {
        this.puerto = puerto;
    }

    public int getVelocidad() {
        return velocidad;
    }

    public void setVelocidad(int velocidad) {
        this.velocidad = velocidad;
    }
     public void inicializar(){
     sp = SerialPort.getCommPort(puerto); // device name TODO: must be changed
     sp.setComPortParameters(velocidad, 8, 1, 0); // default connection settings for Arduino
     sp.setComPortTimeouts(SerialPort.TIMEOUT_WRITE_BLOCKING, 0, 0);
     sp.openPort();
     
     }
      public void finalizar(){
      sp.closePort();
     }
      public void mandar_orden(char a) throws IOException{
      sp.getOutputStream().write(a);
      sp.getOutputStream().flush();
      
      } 
      
    
}
