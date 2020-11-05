

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.stage.Stage;
import org.opencv.core.Size;

import com.fazecast.jSerialComm.SerialPort;
import java.io.IOException;



/**
/**
 *
 * @author neto
 */
public class PruebaCirculos extends Application {
  double alfa=0.0216229058;
  String puerto="COM9";
  int velocidad=38400;
  coneccion_bluetooth sr=new coneccion_bluetooth(puerto,velocidad);  
  
   
  
  public double dist_pinghole(double HO, double df,  int Np){
//HO:altura del Objeto, df:distancia focal, Hp:altura en pixeles

return ((HO*df)/(alfa*Np))/10;
} 
public double alfa(double HO, double df, int Np,int DO){
//HO:altura del Objeto, df:distancia focal, Np:numero de pixeles, DO: distancia al objeto
return (HO*df)/(DO*Np);
}
  
public void adelante(double dx1,double dx2,double dy1,double dy2) throws IOException, InterruptedException{
   double p=(dy1-dy2)*alfa;//distancia profundiad
   double dp=(dx1-dx2)*alfa;//distancia de radios
    if(p<10){
        //va hacia adelante
        System.out.println("distancia profuncidad: "+p);
        if(dp>15){
            System.out.println("distancia radios: "+dp);
            sr.inicializar();
            sr.mandar_orden('2');
            Thread.sleep(5000);
            sr.mandar_orden('5');
            sr.finalizar();
        }
         Thread.sleep(1000);
     }
}
 public void start(Stage stage) throws IOException, InterruptedException {
      //Loading the OpenCV core library
    
      System.loadLibrary( Core.NATIVE_LIBRARY_NAME );
     // String file ="guia.jpg";
      //String file ="monedas.jpg";
     String file ="prueba.jpg";
     //String file ="canitas.jpg"
      Mat src = Imgcodecs.imread(file);
      //Converting the image to Gray
      Mat gray = new Mat();
      Imgproc.cvtColor(src, gray, Imgproc.COLOR_BGRA2GRAY);
      //eliminar ruido
      //Imgproc.GaussianBlur(gray, gray,new Size(9, 9), 1,6);
      Imgproc.GaussianBlur(gray, gray,new Size(9,9),12,14);
      //Imgproc.GaussianBlur(gray, gray,new Size(9,9),12,14);
      //Imgproc.GaussianBlur(gray, gray,new Size(9,9),20,20);
      //Blurring the image
      Mat blur = new Mat();
      Imgproc.medianBlur(gray, blur,5);
      //Detecting the Hough Circles
      Mat circles = new Mat();
      //Imgproc.HoughCircles(blur, circles, Imgproc.HOUGH_GRADIENT, Math.PI/180, 200); 
      


      
    
      Imgproc.HoughCircles (blur, circles, Imgproc.CV_HOUGH_GRADIENT,2,100, 100,90,0,1000);
      for (int i = 0; i < circles.cols(); i++ ) {
          
         double[] data = circles.get(0, i);
         Point center = new Point(Math.round(data[0]), Math.round(data[1]));
         
//         System.out.print(Math.round(data[0]));
//         System.out.print("  ");
//         System.out.println(Math.round(data[1]));
         // circle center
         Imgproc.circle(src, center, 1, new Scalar(163, 73, 164), 10, 8, 0 );
         // circle outline
         int radius = (int) Math.round(data[2]);
         Imgproc.circle(src, center, radius, new Scalar(0,143,57), 10, 8, 0 );
         double distancia= dist_pinghole(60,35,2*radius);
         System.out.println(distancia);
//        double alfa=alfa(60, 35, 2*radius,800);
//        
//         System.out.println(2*radius);
//         System.out.println(alfa); 
      }
      double[] p1 = circles.get(0, 0);
      double[] p2 = circles.get(0, 1);
      
      
    //adelante(Math.round(p1[0]),Math.round(p1[1]),Math.round(p2[0]),Math.round(p2[1])) ;
     

       
      //Converting matrix to JavaFX writable image
      Image img = HighGui.toBufferedImage(src);
      WritableImage writableImage= SwingFXUtils.toFXImage((BufferedImage) img, null);  
      //Setting the image view
      ImageView imageView = new ImageView(writableImage);
      imageView.setX(0);
      imageView.setY(0);
      imageView.setFitWidth(537);
      imageView.setPreserveRatio(true);
      //Setting the Scene object
      Group root = new Group(imageView);
      Scene scene = new Scene(root,537,404);
     // Scene scene = new Scene(root,1090,1080);
      stage.setTitle("Hough Circle Transform");
      stage.setScene(scene);
      stage.show();
      
     //double distancia= dist_pinghole(65,33,);
   }
    public static void main(String[] args) {
       Application.launch(args);
    }  
}
