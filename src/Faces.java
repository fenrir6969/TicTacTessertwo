import java.awt.*;
import java.awt.event.MouseEvent;

public class Faces {
    final static int xOffset = 870;
    final static int yOffset = 340;

    protected char A;
    protected char B;
    protected char C;
    protected char D;
    private Polygon curr;
    private Polygon next;
    public char[][] preview = new char[3][3];
    private String nextName;
    private int flashCount;
    private final int flashStart = 60;
    private int load;

    public Faces() {
        A = ' ';
        B = ' ';
        C = ' ';
        D = ' ';
    }

    public Faces(char axisA, char axisB, char depthC, char depthD) {
        A = axisA;
        B = axisB;
        C = depthC;
        D = depthD;
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

    public int value(char ch) {
        if (Character.isUpperCase(ch)) {
            return 2;
        } else {
            return 0;
        }
    }

    public void define(char axisA, char axisB, char depthC, char depthD) {
        A = axisA;
        B = axisB;
        C = depthC;
        D = depthD;
        System.out.println("[Faces] Defined Face " + this);
        curr = toPolygon();
    }

    public void set(Faces face) {
        A = face.A;
        B = face.B;
        C = face.C;
        D = face.D;
        curr = toPolygon();
    }

    Faces rotate(AxisDepth axisDepth, boolean clockwise) {
        switch (axisDepth) {
            case AC:
                return rotateAC(clockwise);
            case AD:
                return rotateAD(clockwise);
            case BC:
                return rotateBC(clockwise);
            case BD:
                return rotateBD(clockwise);
        }
        throw new IllegalArgumentException("Impossible!");
    }

    public Faces rotateAC(boolean clockwise) {
        Faces Next = new Faces();
        Next.A = A;
        Next.D = D;
        if (clockwise) {
            Next.B = flipCase(C);
            Next.C = B;
        } else {
            Next.C = flipCase(B);
            Next.B = C;
        }
        return Next;
    }

    public Faces rotateAD(boolean clockwise) {
        Faces Next = new Faces();
        Next.A = A;
        Next.C = C;
        if (clockwise) {
            Next.B = flipCase(D);
            Next.D = B;
        } else {
            Next.D = flipCase(B);
            Next.B = D;
        }
        return Next;
    }

    public Faces rotateBC(boolean clockwise) {
        Faces Next = new Faces();
        Next.B = B;
        Next.D = D;
        if (clockwise) {
            Next.A = flipCase(C);
            Next.C = A;
        } else {
            Next.C = flipCase(A);
            Next.A = C;
        }
        return Next;
    }

    public Faces rotateBD(boolean clockwise) {
        Faces Next = new Faces();
        Next.B = B;
        Next.C = C;
        if (clockwise) {
            Next.A = flipCase(D);
            Next.D = A;
        } else {
            Next.D = flipCase(A);
            Next.A = D;
        }
        return Next;
    }

    private String buttonDirection(boolean clockwise) {
        if(clockwise){
            return "+";
        } else {
            return "-";
        }
    }

    public void paintCompass(Graphics2D g) {
        if (nextName != null && load==0) {
            g.drawString("Compass   Previewing : " + nextName, 870, 605);
        } else {
            g.drawString("Compass", 870, 605);
        }
        g.drawRect(870, 340, 240, 240);
        g.setFont(new Font("Courier", Font.PLAIN, 20));

        for (NavButton button : NavButton.buttons) {
            if(load==0) {
                g.setColor(button.color);
            } else {
                g.setColor(Color.white);
            }
            g.drawLine(990, 460, (int) button.polygon.getBounds().getCenterX(), (int) button.polygon.getBounds().getCenterY());
            g.setColor(Color.black);
            g.fillPolygon(button.polygon);
            if(load==0) {
                g.setColor(button.color);
            } else {
                g.setColor(Color.white);
            }
            g.drawPolygon(button.polygon);
            g.drawString(button.axisDepth + buttonDirection(button.clockwise),(int) button.polygon.getBounds().getCenterX()-17,(int) button.polygon.getBounds().getCenterY()+6);
        }

        g.setColor(Color.black);
        g.fillRect(950, 420, 80, 80);

        g.setFont(new Font("Courier", Font.PLAIN, 15));
        if(next!=null&&load==0){
            g.setColor(Color.yellow);
        } else {
            g.setColor(Color.white);
        }
        if(preview!=null&&load==0) {
            for (int x = 0; x < 3; x++) {
                for (int y = 0; y < 3; y++) {
                    g.drawString("" + preview[x][y], 960 + (x * 26), 437 + (y * 26));
                }
            }
        }
        g.drawRect(977, 420, 26, 80);
        g.drawRect(950, 447, 80, 26);
        g.drawRect(950, 420, 80, 80);
    }

    public void paintAtlas(Graphics2D g) {
        g.drawString("Atlas        Viewing : " + this, 870, 315);
        g.drawRect(870, 50, 240, 240);

        g.setColor(Color.darkGray);
        g.fillPolygon(curr);
        if ((next != null) && (flashCount > flashStart / 2)) {
            g.fillPolygon(next);
        }
        g.setColor(Color.white);
        // magic numbers
        g.drawPolygon(Polygons.polygons[1][0][2][1]);
        g.drawPolygon(Polygons.polygons[1][0][0][1]);
        g.drawPolygon(Polygons.polygons[2][1][1][0]);
        g.drawPolygon(Polygons.polygons[0][1][1][0]);
        g.drawPolygon(Polygons.polygons[2][1][1][2]);
        g.drawPolygon(Polygons.polygons[0][1][1][2]);
        g.drawPolygon(Polygons.polygons[1][2][2][1]);
        g.drawPolygon(Polygons.polygons[1][2][0][1]);
        g.setColor(Color.yellow);
        g.drawPolygon(curr);
        if ((next != null) && (flashCount > flashStart / 2)) {
            g.drawPolygon(next);
        }
        g.setColor(Color.white);
    }

    public int axisIndex(char axis) {
        return switch (axis) {
            case 'x', 'X' -> 0;
            case 'y', 'Y' -> 1;
            case 'z', 'Z' -> 2;
            case 'w', 'W' -> 3;
            default -> throw new IllegalArgumentException("Impossible!");
        };
    }

    private Polygon toPolygon() {
        int[] address = new int[4];
        address[axisIndex(A)] = 1;
        address[axisIndex(B)] = 1;
        address[axisIndex(C)] = value(C);
        address[axisIndex(D)] = value(D);
        return Polygons.polygons[address[0]][address[1]][address[2]][address[3]];
    }

    public void updateComponent() {
        if (flashCount > 0) {
            flashCount--;
        } else {
            flashCount = flashStart;
        }
        if (load > 0) {
            load--;
        }
    }

    public boolean mouseClicked(MouseEvent e) {
        Faces face = new Faces();
        for (NavButton button : NavButton.buttons) {
            if (button.polygon.contains(e.getX(), e.getY())) {
                System.out.println();
                System.out.println("Rotating...");
                face.set(rotate(button.axisDepth, button.clockwise));
                next = face.rotate(button.axisDepth, button.clockwise).toPolygon();
                nextName = face.rotate(button.axisDepth, button.clockwise).toString();
                flashCount = flashStart / 2;
                load = 20;
                System.out.println("[Faces] Rotating from " + this + " to " + face + "...");
                set(face);
                return true;
            }
        }
        return false;
    }

    public Faces mouseMoved(MouseEvent e) {
        for (NavButton button : NavButton.buttons) {
            if (button.polygon.contains(e.getX(), e.getY())) {
                if (next != rotate(button.axisDepth, button.clockwise).toPolygon()) {
                    load += 5;
                }
                button.color = Color.yellow;
                next = rotate(button.axisDepth, button.clockwise).toPolygon();
                nextName = rotate(button.axisDepth, button.clockwise).toString();
                return rotate(button.axisDepth, button.clockwise);
            } else {
                button.color = Color.white;
            }
        }
        if(next!=null){
            load = 30;
        }
        next = null;
        nextName = null;
        flashCount = flashStart;
        return null;
    }
    enum AxisDepth {AC, AD, BC, BD}

    static class NavButton {
        Polygon polygon;
        Faces.AxisDepth axisDepth;
        boolean clockwise;
        Color color;
        static int[] leftA = {971,988,941,922};
        static int[] topA = {345,391,410,364};
        static int[] rightA = {1011,993,1038,1057};
        static int[] bottomA = {579,535,516,561};
        static int[] topB = {413,396,443,460};
        static int[] rightB = {1044,1089,1108,1062};
        static int[] bottomB = {510,529,483,464};
        static int[] leftB = {938,892,873,919};
        static NavButton[] buttons = {
                new NavButton(60 + xOffset, yOffset, AxisDepth.AC, false, new Polygon(leftA,topA,4)),
                new NavButton(120 + xOffset, yOffset, AxisDepth.AD, false, new Polygon(rightA,topA,4)),
                new NavButton(60 + xOffset, 180 + yOffset, AxisDepth.AD, true, new Polygon(leftA,bottomA,4)),
                new NavButton(120 + xOffset, 180 + yOffset, AxisDepth.AC, true, new Polygon(rightA,bottomA,4)),
                new NavButton(xOffset, 60 + yOffset, AxisDepth.BC, false, new Polygon(leftB,topB,4)),
                new NavButton(xOffset, 120 + yOffset, AxisDepth.BD, false, new Polygon(leftB,bottomB,4)),
                new NavButton(180 + xOffset, 60 + yOffset, AxisDepth.BD, true, new Polygon(rightB,topB,4)),
                new NavButton(180 + xOffset, 120 + yOffset, AxisDepth.BC, true, new Polygon(rightB,bottomB,4))
        };

        NavButton(int x, int y, Faces.AxisDepth axisDepth, boolean clockwise, Polygon polygon) {
            this.color = Color.white;
            this.polygon = polygon;
            this.axisDepth = axisDepth;
            this.clockwise = clockwise;
        }
    }

    static class Polygons {

        public static final Polygon[][][][] polygons = new Polygon[3][3][3][3];
        //                                   0   1   2   3   4   5   6   7   8   9   10  11  12  13  14  15  16
        private static final int[] pointsX = {0, 57, 208, 25, 177, 97, 151, 85, 140, 98, 154, 86, 141, 61, 213, 30, 180};
        private static final int[] pointsY = {0, 23, 25, 75, 77, 83, 85, 102, 104, 135, 137, 155, 157, 164, 170, 215, 220};

        static {
            initializePolygons();
        }

        private static Polygon makePolygon(int p1, int p2, int p3, int p4) {
            int[] x = {pointsX[p1], pointsX[p2], pointsX[p3], pointsX[p4]};
            int[] y = {pointsY[p1], pointsY[p2], pointsY[p3], pointsY[p4]};
            return new Polygon(x, y, 4);
        }

        private static void initializePolygons() {
            int offsetX = 870;
            int offsetY = 50;
            for (int i = 1; i < 17; i++) {
                pointsX[i] = pointsX[i] + offsetX;
                pointsY[i] = pointsY[i] + offsetY;
            }
            //       x  y  z  w
            polygons[1][0][1][0] = makePolygon(1, 2, 4, 3);
            polygons[1][0][2][1] = makePolygon(1, 2, 6, 5);
            polygons[2][0][1][1] = makePolygon(2, 4, 8, 6);
            polygons[1][0][0][1] = makePolygon(4, 3, 7, 8);
            polygons[0][0][1][1] = makePolygon(3, 1, 5, 7);
            polygons[1][1][2][0] = makePolygon(1, 2, 14, 13);
            polygons[2][1][1][0] = makePolygon(2, 4, 16, 14);
            polygons[1][1][0][0] = makePolygon(4, 3, 15, 16);
            polygons[0][1][1][0] = makePolygon(3, 1, 13, 15);
            polygons[1][0][1][2] = makePolygon(5, 6, 8, 7);
            polygons[1][1][2][2] = makePolygon(5, 6, 10, 9);
            polygons[2][1][1][2] = makePolygon(6, 8, 12, 10);
            polygons[1][1][0][2] = makePolygon(8, 7, 11, 12);
            polygons[0][1][1][2] = makePolygon(7, 5, 9, 11);
            polygons[1][2][1][2] = makePolygon(9, 10, 12, 11);
            polygons[1][2][2][1] = makePolygon(9, 10, 14, 13);
            polygons[2][2][1][1] = makePolygon(10, 12, 16, 14);
            polygons[1][2][0][1] = makePolygon(12, 11, 15, 16);
            polygons[0][2][1][1] = makePolygon(11, 9, 13, 15);
            polygons[1][2][1][0] = makePolygon(13, 14, 16, 15);
            polygons[0][1][2][1] = makePolygon(1, 5, 9, 13);
            polygons[2][1][2][1] = makePolygon(2, 6, 10, 14);
            polygons[2][1][0][1] = makePolygon(4, 8, 12, 16);
            polygons[0][1][0][1] = makePolygon(3, 7, 11, 15);
        }
    }
}