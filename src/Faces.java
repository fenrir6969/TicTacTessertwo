import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.Random;

public class Faces {
    private static final char X = 'x';
    private static final char Y = 'y';

    private Polygon curr;
    private Polygon next;

    private int load;
    private int maxLoad = 60;

    protected char A;
    protected char B;
    protected char C;
    protected char D;

    public Faces() {
        A = ' ';
        B = ' ';
        C = ' ';
        D = ' ';
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
        curr = toPolygon();
    }
    public void set(Faces face){
        A = face.A;
        B = face.B;
        C = face.C;
        D = face.D;
        System.out.println("");
        System.out.println("[Faces] Defined Face " + face);
        curr = toPolygon();
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

    enum AxisDepth { AC, AD, BC, BD }

    Faces rotate(AxisDepth axisDepth, boolean clockwise) {
        switch(axisDepth) {
            case AC: return rotateAC(clockwise);
            case AD: return rotateAD(clockwise);
            case BC: return rotateBC(clockwise);
            case BD: return rotateBD(clockwise);
        }
        throw new IllegalArgumentException("Impossible!");
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
        g.setColor(Color.red);
        for(NavButton button : buttons){
            g.draw(button.rectangle);
        }
        /*
        g.drawRect(60+xOffset,yOffset,60,60);
        g.drawRect(120+xOffset,yOffset,60,60);
        g.drawRect(60+xOffset,180+yOffset,60,60);
        g.drawRect(120+xOffset,180+yOffset,60,60);
        g.drawRect(xOffset,60+yOffset,60,60);
        g.drawRect(xOffset,120+yOffset,60,60);
        g.drawRect(180+xOffset,60+yOffset,60,60);
        g.drawRect(180+xOffset,120+yOffset,60,60);

         */
        g.setColor(Color.white);



    }
    public void paintAtlas(Graphics2D g){
        g.drawString("Atlas        Viewing : " + toString(),870, 315);
        g.drawRect(870, 50, 240, 240);

        g.setColor(Color.darkGray);
        g.fillPolygon(curr);
        if ((next != null)&&(load>maxLoad/2)) {
            g.setColor(new Color(1,50,32));
            g.fillPolygon(next);
        }
        g.setColor(Color.white);
        // magic numbers
        g.drawPolygon(Polygons.polygons[2]);
        g.drawPolygon(Polygons.polygons[4]);
        g.drawPolygon(Polygons.polygons[7]);
        g.drawPolygon(Polygons.polygons[9]);
        g.drawPolygon(Polygons.polygons[12]);
        g.drawPolygon(Polygons.polygons[14]);
        g.drawPolygon(Polygons.polygons[16]);
        g.drawPolygon(Polygons.polygons[18]);
        g.setColor(Color.yellow);
        g.drawPolygon(curr);
        if ((next != null)&&(load>maxLoad/2)) {
            g.setColor(Color.green);
            g.drawPolygon(next);
        }
        g.setColor(Color.white);
    }


    private Polygon toPolygon(){
        if(flatten(C)==X){
            if(flatten(D)==Y){
                //xy
                if(evaluate(C)==2){
                    if(evaluate(D)==2){
                        //x2y2
                        return Polygons.polygons[18];
                    } else {
                        //x2y0
                        return Polygons.polygons[3];
                    }
                } else {
                    if(evaluate(D)==2){
                        //x0y2
                        return Polygons.polygons[19];
                    } else {
                        //x0y0
                        return Polygons.polygons[5];
                    }
                }
            }
            else if(flatten(D)=='z') {
                //xz
                if(evaluate(C)==2){
                    if(evaluate(D)==2){
                        //x2z2
                        return Polygons.polygons[22];
                    } else {
                        //x2z0
                        return Polygons.polygons[23];
                    }
                } else {
                    if(evaluate(D)==2){
                        //x0z2
                        return Polygons.polygons[21];
                    } else {
                        //x0z0
                        return Polygons.polygons[24];
                    }
                }
            }
            else {
                //xw
                if(evaluate(C)==2){
                    if(evaluate(D)==2){
                        //x2w2
                        return Polygons.polygons[12];
                    } else {
                        //x2w0
                        return Polygons.polygons[7];
                    }
                } else {
                    if(evaluate(D)==2){
                        //x0w2
                        return Polygons.polygons[14];
                    } else {
                        //x0w0
                        return Polygons.polygons[9];
                    }
                }
            }
        }
        else if(flatten(C)==Y){
            if(flatten(D)==X){
                //yx
                if(evaluate(C)==2){
                    if(evaluate(D)==2){
                        //y2x2
                        return Polygons.polygons[17];
                    } else {
                        //y2x0
                        return Polygons.polygons[19];
                    }
                } else {
                    if(evaluate(D)==2){
                        //y0x2
                        return Polygons.polygons[3];
                    } else {
                        //y0x0
                        return Polygons.polygons[5];
                    }
                }
            }
            else if(flatten(D)=='z'){
                //yz
                if(evaluate(C)==2){
                    if(evaluate(D)==2){
                        //y2z2
                        return Polygons.polygons[16];
                    } else {
                        //y2z0
                        return Polygons.polygons[18];
                    }
                } else {
                    if(evaluate(D)==2){
                        //y0z2
                        return Polygons.polygons[2];
                    } else {
                        //y0z0
                        return Polygons.polygons[4];
                    }
                }
            }
            else {
                //yw
                if(evaluate(C)==2){
                    if(evaluate(D)==2){
                        //y2w2
                        return Polygons.polygons[15];
                    } else {
                        //y2w0
                        return Polygons.polygons[20];
                    }
                } else {
                    if(evaluate(D)==2){
                        //y0w2
                        return Polygons.polygons[10];
                    } else {
                        //y0w0
                        return Polygons.polygons[1];
                    }
                }
            }
        }
        else if(flatten(C)=='z'){
            if(flatten(D)==X){
                //zx
                if(evaluate(C)==2){
                    if(evaluate(D)==2){
                        //z2x2
                        return Polygons.polygons[22];
                    } else {
                        //z2x0
                        return Polygons.polygons[21];
                    }
                } else {
                    if(evaluate(D)==2){
                        //z0x2
                        return Polygons.polygons[23];
                    } else {
                        //z0x0
                        return Polygons.polygons[24];
                    }
                }
            }
            else if(flatten(D)==Y){
                //zy
                if(evaluate(C)==2){
                    if(evaluate(D)==2){
                        //z2y2
                        return Polygons.polygons[16];
                    } else {
                        //z2y0
                        return Polygons.polygons[2];
                    }
                } else {
                    if(evaluate(D)==2){
                        //z0y2
                        return Polygons.polygons[18];
                    } else {
                        //z0y0
                        return Polygons.polygons[4];
                    }
                }
            }
            else{
                //zw
                if(evaluate(C)==2){
                    if(evaluate(D)==2){
                        //z2w2
                        return Polygons.polygons[11];
                    } else {
                        //z2w0
                        return Polygons.polygons[6];
                    }
                } else {
                    if(evaluate(D)==2){
                        //z0w2
                        return Polygons.polygons[13];
                    } else {
                        //z0w0
                        return Polygons.polygons[8];
                    }
                }
            }
        }
        else{
            if(flatten(D)==X){
                //wx
                if(evaluate(C)==2){
                    if(evaluate(D)==2){
                        //w2x2
                        return Polygons.polygons[12];
                    } else {
                        //w2x0
                        return Polygons.polygons[14];
                    }
                } else {
                    if(evaluate(D)==2){
                        //w0x2
                        return Polygons.polygons[7];
                    } else {
                        //w0x0
                        return Polygons.polygons[9];
                    }
                }
            }
            else if(flatten(D)==Y){
                //wy
                if(evaluate(C)==2){
                    if(evaluate(D)==2){
                        //w2y2
                        return Polygons.polygons[15];
                    } else {
                        //w2y0
                        return Polygons.polygons[10];
                    }
                } else {
                    if(evaluate(D)==2){
                        //w0y2
                        return Polygons.polygons[20];
                    } else {
                        //w0y0
                        return Polygons.polygons[1];
                    }
                }
            }
            else{
                //wz
                if(evaluate(C)==2){
                    if(evaluate(D)==2){
                        //w2z2
                        return Polygons.polygons[11];
                    } else {
                        //w2z0
                        return Polygons.polygons[13];
                    }
                } else {
                    if(evaluate(D)==2){
                        //w0z2
                        return Polygons.polygons[6];
                    } else {
                        //w0z0
                        return Polygons.polygons[8];
                    }
                }
            }
        }
    }
    public void updateComponent() {
        if(load>0){
            load--;
        } else {
            load=maxLoad;
        }
    }

    public Faces mouseClicked(MouseEvent e){
        return rotateRandom();
    }

    public void mouseMoved(MouseEvent e) {
        int mosX = e.getX();
        int mosY = e.getY();

        for (NavButton button : buttons) {
            if (button.rectangle.contains(mosX, mosY)) {
                next = rotate(button.axisDepth, button.clockwise).toPolygon();
                return;
            }
        }
        next = null;
    }

    final static int xOffset = 870;
    final static int yOffset = 340;
    static NavButton[] buttons = {
        new NavButton(60 + xOffset, yOffset, AxisDepth.AC, true),
        new NavButton(120 + xOffset, yOffset, AxisDepth.AD, true),
        new NavButton(60+xOffset,180+yOffset,AxisDepth.AC,false),
        new NavButton(120 + xOffset, 180 + yOffset, AxisDepth.AD, false),
        new NavButton(xOffset,60+yOffset,AxisDepth.BC,true),
        new NavButton(xOffset,120+yOffset,AxisDepth.BD,true),
        new NavButton(180+xOffset,60+yOffset,AxisDepth.BC,false),
        new NavButton(180+xOffset,120+yOffset,AxisDepth.BD,false)
    };

    static class NavButton {
        Rectangle rectangle;
        Faces.AxisDepth axisDepth;
        boolean clockwise;

        NavButton(int x, int y, Faces.AxisDepth axisDepth, boolean clockwise) {
            this.rectangle = new Rectangle(x, y, 60, 60);
            this.axisDepth = axisDepth;
            this.clockwise = clockwise;
        }
    }

}