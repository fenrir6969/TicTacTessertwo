import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.Random;

public class Balls {
    Random ran = new Random();
    double ballX = 0;
    double ballY = 0;
    boolean flipY = false;
    boolean flipX = false;
    double slope = 1.0;

    public Balls() {
        ballY = ran.nextInt(600);
        ballX = ran.nextInt(1100);
        if(ran.nextInt(2)==0){
            flipY=true;
        }
        if(ran.nextInt(2)==0){
            flipX=true;
        }
    }

    public void paint(Graphics g) {
        g.drawOval((int)ballX,(int)ballY,50,50);
    }

    public void updateComponent() {
        if(ballX>1160-50){
            flipX=true;
            slope = ran.nextInt(10)+5;
            ballX=1160-51;
        } else if(ballX<0){
            flipX=false;
            slope = ran.nextInt(10)+5;
            ballX=1;
        } if(ballY>670-75){
            flipY=true;
            slope = ran.nextInt(10)+5;
            ballY=670-76;
        } else if(ballY<0){
            flipY=false;
            slope = ran.nextInt(10)+5;
            ballY=1;
        }

        if(flipX){
            ballX-=(slope/10);
        } else {
            ballX+=(slope/10);
        } if(flipY){
            ballY-=(2.0-slope/10);
        } else {
            ballY+=(2.0-slope/10);
        }
    }

    public void mouseClicked(MouseEvent e) {
        Rectangle bounds = new Rectangle((int)ballX,(int)ballY,50,50);
        if(bounds.contains(e.getX(),e.getY())) {
            if (ran.nextInt(2) == 0) {
                flipY = !flipY;
            } else {
                flipX = !flipX;
            }
            System.out.println("[Balls] Boing!");
        }
        //slope=ran.nextInt(10)+5;
    }
    //  2.0 1.0 0.0
    //  0.0 1.0 2.0
    //  slope 0 - 20
    //  x=slope/10
    //  y=2.0-slope/10
}
