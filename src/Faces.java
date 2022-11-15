import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.Random;

public class Faces {
    Polygon next;
    int mosX;
    int mosY;
    int oldX;
    int oldY;
    int[] pointsX = new int[17];
    int[] pointsY = new int[17];
    Polygon[] polygons = new Polygon[26];
    int load;
    int maxLoad = 60;

    protected char A;
    protected char B;
    protected char C;
    protected char D;
    protected Polygon currPolygon;

    public Faces() {
        A = ' ';
        B = ' ';
        C = ' ';
        D = ' ';
        currPolygon = new Polygon();
    }

    @Override
    public String toString() {
        return "" + A + B + C + D;
    }
    private char flipCase(char ch) {
        if (Character.isUpperCase(ch)) {
            return Character.toLowerCase(ch);
        } else {
            return Character.toUpperCase(ch);
        }
    }
    private void rotationLogger(Faces face) {
        System.out.println("");
        System.out.println("[Faces] Rotating from " + this + " to " + face);
        this.toPolygon();
        next = this.currPolygon;
    }
    public char flatten(char ch) {
        return Character.toLowerCase(ch);
    }
    public int evaluate(char ch) {
        if(Character.isUpperCase(ch)){
            return 2;
        } else {
            return 0;
        }
    }

    public void define(char axisA,char axisB,char depthC,char depthD) {
        A = axisA;
        B = axisB;
        C = depthC;
        D = depthD;
        System.out.println("");
        System.out.println("[Faces] Defined Face " + this);
    }
    public void set(Faces face){
        A = face.A;
        B = face.B;
        C = face.C;
        D = face.D;
        System.out.println("");
        System.out.println("[Faces] Defined Face " + face);
        currPolygon = face.currPolygon;
    }

    private char randomChar() {
        Random ran = new Random();
        char ax;
        int r = ran.nextInt(4);
        if( r == 0) {
            ax = 'x';
        } else if( r == 1) {
            ax = 'y';
        } else if( r == 2) {
            ax = 'z';
        } else {
            ax = 'w';
        }
        if(ran.nextInt(2)==0){
            return Character.toUpperCase(ax);
        } else {
            return ax;
        }

    }
    public Faces rotateRandom(){
        Random ran = new Random();
        int r = ran.nextInt(4);
        Faces face;
        if( r == 0) {
            face = rotateAC(ran.nextInt(2)==0);
        } else if( r == 1) {
            face = rotateAD(ran.nextInt(2)==0);
        } else if( r == 2) {
            face = rotateBC(ran.nextInt(2)==0);
        } else {
            face = rotateBD(ran.nextInt(2)==0);
        }
        return face;
    }

    public Faces rotateAC( boolean clockwise) {
        Faces Next = new Faces();
        Next.A = A;
        Next.D = D;
        if( clockwise ) {
            Next.B = flipCase(C);
            Next.C = B;
        }
        else {
            Next.C = flipCase(B);
            Next.B = C;
        }
        rotationLogger(Next);
        return Next;
    }
    public Faces rotateAD( boolean clockwise) {
        Faces Next = new Faces();
        Next.A = A;
        Next.C = C;
        if( clockwise ) {
            Next.B = flipCase(D);
            Next.D = B;
        }
        else {
            Next.D = flipCase(B);
            Next.B = D;
        }
        rotationLogger(Next);
        return Next;
    }
    public Faces rotateBC( boolean clockwise) {
        Faces Next = new Faces();
        Next.B = B;
        Next.D = D;
        if( clockwise ) {
            Next.A = flipCase(C);
            Next.C = A;
        }
        else {
            Next.C = flipCase(A);
            Next.A = C;
        }
        rotationLogger(Next);
        return Next;
    }
    public Faces rotateBD( boolean clockwise) {
        Faces Next = new Faces();
        Next.B = B;
        Next.C = C;
        if( clockwise ) {
            Next.A = flipCase(D);
            Next.D = A;
        }
        else {
            Next.D = flipCase(A);
            Next.A = D;
        }
        rotationLogger(Next);
        return Next;
    }

    public void paintCompass(Graphics2D g){
        g.drawString("Compass", 870, 605);
        g.drawRect(870, 340, 240, 240);
        int xOffset=870;
        int yOffset=340;
        g.setColor(Color.red);
        //up C
        g.drawRect(60+xOffset,yOffset,60,60);
        //up D
        g.drawRect(120+xOffset,yOffset,60,60);
        //Down C
        g.drawRect(60+xOffset,180+yOffset,60,60);
        //Down D
        g.drawRect(120+xOffset,180+yOffset,60,60);
        //Left C
        g.drawRect(xOffset,60+yOffset,60,60);
        //Left D
        g.drawRect(xOffset,120+yOffset,60,60);
        //Right C
        g.drawRect(xOffset+180,60+yOffset,60,60);
        //Right D
        g.drawRect(xOffset+180,120+yOffset,60,60);




    }
    public void paintAtlas(Graphics2D g){
        g.drawString("Atlas        Viewing : " + toString(),870, 315);
        g.drawRect(870, 50, 240, 240);
        g.setColor(Color.darkGray);
        g.fillPolygon(currPolygon);
        System.out.println("[Faces] Drawing Current : " + currPolygon);
        if(load<maxLoad/2){
            g.setColor(Color.darkGray);
            g.fillPolygon(next);
            System.out.println("[Faces] Drawing Preview : " + next);
        }
        g.setColor(Color.white);
        g.drawPolygon(polygons[2]);
        g.drawPolygon(polygons[4]);
        g.drawPolygon(polygons[7]);
        g.drawPolygon(polygons[9]);
        g.drawPolygon(polygons[12]);
        g.drawPolygon(polygons[14]);
        g.drawPolygon(polygons[16]);
        g.drawPolygon(polygons[18]);
        g.setColor(Color.yellow);
        g.drawPolygon(currPolygon);
        if(load<maxLoad/2){
            g.setColor(Color.yellow);
            g.drawPolygon(next);
        }
        g.setColor(Color.white);


    }
    private Polygon makePolygon(int p1, int p2, int p3, int p4){
        int[] x = {pointsX[p1],pointsX[p2],pointsX[p3],pointsX[p4]};
        int[] y = {pointsY[p1],pointsY[p2],pointsY[p3],pointsY[p4]};
        return new Polygon(x,y,4);
    }
    private void toPolygon(){
        if(flatten(C)=='x'){
            if(flatten(D)=='y'){
                //xy
                if(evaluate(C)==2){
                    if(evaluate(D)==2){
                        //x2y2
                        currPolygon = polygons[18];
                    } else {
                        //x2y0
                        currPolygon = polygons[3];
                    }
                } else {
                    if(evaluate(D)==2){
                        //x0y2
                        currPolygon = polygons[19];
                    } else {
                        //x0y0
                        currPolygon = polygons[5];
                    }
                }
            }
            else if(flatten(D)=='z') {
                //xz
                if(evaluate(C)==2){
                    if(evaluate(D)==2){
                        //x2z2
                        currPolygon = polygons[22];
                    } else {
                        //x2z0
                        currPolygon = polygons[23];
                    }
                } else {
                    if(evaluate(D)==2){
                        //x0z2
                        currPolygon = polygons[21];
                    } else {
                        //x0z0
                        currPolygon = polygons[24];
                    }
                }
            }
            else {
                //xw
                if(evaluate(C)==2){
                    if(evaluate(D)==2){
                        //x2w2
                        currPolygon = polygons[12];
                    } else {
                        //x2w0
                        currPolygon = polygons[7];
                    }
                } else {
                    if(evaluate(D)==2){
                        //x0w2
                        currPolygon = polygons[14];
                    } else {
                        //x0w0
                        currPolygon = polygons[9];
                    }
                }
            }
        }
        else if(flatten(C)=='y'){
            if(flatten(D)=='x'){
                //yx
                if(evaluate(C)==2){
                    if(evaluate(D)==2){
                        //y2x2
                        currPolygon = polygons[17];
                    } else {
                        //y2x0
                        currPolygon = polygons[19];
                    }
                } else {
                    if(evaluate(D)==2){
                        //y0x2
                        currPolygon = polygons[3];
                    } else {
                        //y0x0
                        currPolygon = polygons[5];
                    }
                }
            }
            else if(flatten(D)=='z'){
                //yz
                if(evaluate(C)==2){
                    if(evaluate(D)==2){
                        //y2z2
                        currPolygon = polygons[16];
                    } else {
                        //y2z0
                        currPolygon = polygons[18];
                    }
                } else {
                    if(evaluate(D)==2){
                        //y0z2
                        currPolygon = polygons[2];
                    } else {
                        //y0z0
                        currPolygon = polygons[4];
                    }
                }
            }
            else {
                //yw
                if(evaluate(C)==2){
                    if(evaluate(D)==2){
                        //y2w2
                        currPolygon = polygons[15];
                    } else {
                        //y2w0
                        currPolygon = polygons[20];
                    }
                } else {
                    if(evaluate(D)==2){
                        //y0w2
                        currPolygon = polygons[10];
                    } else {
                        //y0w0
                        currPolygon = polygons[1];
                    }
                }
            }
        }
        else if(flatten(C)=='z'){
            if(flatten(D)=='x'){
                //zx
                if(evaluate(C)==2){
                    if(evaluate(D)==2){
                        //z2x2
                        currPolygon = polygons[22];
                    } else {
                        //z2x0
                        currPolygon = polygons[21];
                    }
                } else {
                    if(evaluate(D)==2){
                        //z0x2
                        currPolygon = polygons[23];
                    } else {
                        //z0x0
                        currPolygon = polygons[24];
                    }
                }
            }
            else if(flatten(D)=='y'){
                //zy
                if(evaluate(C)==2){
                    if(evaluate(D)==2){
                        //z2y2
                        currPolygon = polygons[16];
                    } else {
                        //z2y0
                        currPolygon = polygons[2];
                    }
                } else {
                    if(evaluate(D)==2){
                        //z0y2
                        currPolygon = polygons[18];
                    } else {
                        //z0y0
                        currPolygon = polygons[4];
                    }
                }
            }
            else{
                //zw
                if(evaluate(C)==2){
                    if(evaluate(D)==2){
                        //z2w2
                        currPolygon = polygons[11];
                    } else {
                        //z2w0
                        currPolygon = polygons[6];
                    }
                } else {
                    if(evaluate(D)==2){
                        //z0w2
                        currPolygon = polygons[13];
                    } else {
                        //z0w0
                        currPolygon = polygons[8];
                    }
                }
            }
        }
        else{
            if(flatten(D)=='x'){
                //wx
                if(evaluate(C)==2){
                    if(evaluate(D)==2){
                        //w2x2
                        currPolygon = polygons[12];
                    } else {
                        //w2x0
                        currPolygon = polygons[14];
                    }
                } else {
                    if(evaluate(D)==2){
                        //w0x2
                        currPolygon = polygons[7];
                    } else {
                        //w0x0
                        currPolygon = polygons[9];
                    }
                }
            }
            else if(flatten(D)=='y'){
                //wy
                if(evaluate(C)==2){
                    if(evaluate(D)==2){
                        //w2y2
                        currPolygon = polygons[15];
                    } else {
                        //w2y0
                        currPolygon = polygons[10];
                    }
                } else {
                    if(evaluate(D)==2){
                        //w0y2
                        currPolygon = polygons[20];
                    } else {
                        //w0y0
                        currPolygon = polygons[1];
                    }
                }
            }
            else{
                //wz
                if(evaluate(C)==2){
                    if(evaluate(D)==2){
                        //w2z2
                        currPolygon = polygons[11];
                    } else {
                        //w2z0
                        currPolygon = polygons[13];
                    }
                } else {
                    if(evaluate(D)==2){
                        //w0z2
                        currPolygon = polygons[6];
                    } else {
                        //w0z0
                        currPolygon = polygons[8];
                    }
                }
            }
        }
    }
    public void initializePolygons() {
        int offsetX = 870;
        int offsetY = 50;
        // I fucked up. I made the first polygon 1 instead of 0, but I've done too much work to go back now.
        // Please, forgive me for these sins.
        //                  0   1   2   3   4   5   6   7   8   9   10  11  12  13  14  15  16
        pointsX = new int[]{0  ,57 ,208,25 ,177,97 ,151,85 ,140,98 ,154,86 ,141,61 ,213,30 ,180};
        pointsY = new int[]{0  ,23 ,25 ,75 ,77 ,83 ,85 ,102,104,135,137,155,157,164,170,215,220};
        for(int i=1;i<17;i++){
            pointsX[i] = pointsX[i] + offsetX;
            pointsY[i] = pointsY[i] + offsetY;
            System.out.println(pointsX[i] + " " + pointsY[i]);
        }
        polygons[1]=makePolygon(1,2,4,3);
        polygons[2]=makePolygon(1,2,6,5);
        polygons[3]=makePolygon(2,4,8,6);
        polygons[4]=makePolygon(4,3,7,8);
        polygons[5]=makePolygon(3,1,5,7);
        polygons[6]=makePolygon(1,2,14,13);
        polygons[7]=makePolygon(2,4,16,14);
        polygons[8]=makePolygon(4,3,15,16);
        polygons[9]=makePolygon(3,1,13,15);
        polygons[10]=makePolygon(5,6,8,7);
        polygons[11]=makePolygon(5,6,10,9);
        polygons[12]=makePolygon(6,8,12,10);
        polygons[13]=makePolygon(8,7,11,12);
        polygons[14]=makePolygon(7,5,9,11);
        polygons[15]=makePolygon(9,10,12,11);
        polygons[16]=makePolygon(9,10,14,13);
        polygons[17]=makePolygon(10,12,16,14);
        polygons[18]=makePolygon(12,11,15,16);
        polygons[19]=makePolygon(11,9,13,15);
        polygons[20]=makePolygon(13,14,16,15);
        polygons[21]=makePolygon(1,5,9,13);
        polygons[22]=makePolygon(2,6,10,14);
        polygons[23]=makePolygon(4,8,12,16);
        polygons[24]=makePolygon(3,7,11,15);
        define('x', 'y', 'z','w');
        toPolygon();
        next = currPolygon;
    }
    public void updateComponent() {
        if(load>0){
            load--;
        } else {
            load=maxLoad;
        }
        if(currPolygon ==null){
            toPolygon();
        }
        if(next==null){
            next = currPolygon;
        }
    }

    public Faces mouseClicked(MouseEvent e){
        return rotateRandom();
    }
    public void mouseMoved(MouseEvent e) {
        mosX = e.getX();
        mosY = e.getY();
        int xOffset=870;
        int yOffset=340;
        Faces face = new Faces();
        //up C
        if((mosX>0&&mosX<60)&&(mosY>0&&mosY<60)){
            face.set(rotateAC(true));
            next = face.currPolygon;
            next = rotateAC(true).currPolygon;

        } else {
            next = currPolygon;
        }
        /*
        g.drawRect(60+xOffset,yOffset,60,60);
        //up D
        g.drawRect(120+xOffset,yOffset,60,60);
        //Down C
        g.drawRect(60+xOffset,180+yOffset,60,60);
        //Down D
        g.drawRect(120+xOffset,180+yOffset,60,60);
        //Left C
        g.drawRect(xOffset,60+yOffset,60,60);
        //Left D
        g.drawRect(xOffset,120+yOffset,60,60);
        //Right C
        g.drawRect(xOffset+180,60+yOffset,60,60);
        //Right D
        g.drawRect(xOffset+180,120+yOffset,60,60);

         */
    }

}