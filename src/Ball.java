import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.Random;

public class Ball {
    Random ran = new Random();
    int ballX = 0;
    int ballY = 0;
    boolean flipY = false;
    boolean flipX = false;

    public Ball() {
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
        g.drawOval(ballX,ballY,50,50);
    }

    public void updateComponent() {
        if(ballX>1105){
            flipX=true;
        } else if(ballX<0){
            flipX=false;
        } if(ballY>605){
            flipY=true;
        } else if(ballY<0){
            flipY=false;
        }

        if(flipX){
            ballX--;
        } else {
            ballX++;
        } if(flipY){
            ballY--;
        } else {
            ballY++;
        }
    }
}
