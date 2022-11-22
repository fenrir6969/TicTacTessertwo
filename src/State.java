import java.awt.*;
import java.awt.event.MouseEvent;

public class State {
    protected char[][][][] board;
    protected char[][] cubes;
    protected char hyper;

    int cubeAxis=3;
    int cubeDepth=0;
    char[] cubeNet = new char[6];
    int load;

    public State(){
        board = new char[3][3][3][3];
        cubes = new char[2][4];
        hyper = ' ';
    }

    private String cubeName(int depth, int axis) {
        char ax;
        if( axis == 0) {
            ax = 'x';
        } else if( axis == 1) {
            ax = 'y';
        } else if( axis == 2) {
            ax = 'z';
        } else {
            ax = 'w';
        }
        return "" + ax + depth;
    }
    private boolean isWon(char state){
        return (state == 'x') || (state == 'o');
    }
    private int flip(int i, int depth){
        if(i!=1){
            return (i+depth*2)%4;
        }
        return i;
    }
    private char[] getNet(int depth, int axis) {
        //fill net from boardStates
        int[] columnA = {0, 2, 1, 1, 1, 1};
        int[] columnB = {1, 1, 0, 2, 1, 1};
        int[] columnC = {1, 1, 1, 1, 0, 2};
        int[] address = new int[4];
        char[] net = new char[6];

        if (axis == 0) {
            for (int i = 0; i < 6; i++) {
                net[i] = board[depth*2][columnA[i]][columnB[i]][flip(columnC[i],depth)];
            }
        } else if (axis == 1) {
            for (int i = 0; i < 6; i++) {
                net[i] = board[columnC[i]][depth*2][columnB[i]][flip(columnA[i],depth)];
            }
        } else if (axis == 2) {
            for (int i = 0; i < 6; i++) {
                net[i] = board[columnC[i]][columnA[i]][depth*2][flip(columnB[i],depth)];
            }
        } else {
            for (int i = 0; i < 6; i++) {
                net[i] = board[columnC[i]][columnA[i]][columnB[i]][depth*2];
            }
        }
        System.out.print("[State]   ");
        for(int i=0;i<3;i++){
            System.out.print(" " + net[i*2] + net[i*2+1]);
        }
        System.out.println();
        return net;
    }

    private boolean notConflicting(char state1, char state2){
        if(isWon(state1)&&isWon(state2)){
            if(state1 != state2){
                return false;
            }
            return true;
        } else if (state1 == 's' || state2 == 's'){
            return false;
        } else {
            return true;
        }
    }
    public char get( Faces face) {
        int[] address = new int[4];
        address[face.axisIndex(face.A)] = 1;
        address[face.axisIndex(face.B)] = 1;
        address[face.axisIndex(face.C)] = face.value(face.C);
        address[face.axisIndex(face.D)] = face.value(face.D);
        return board[address[0]][address[1]][address[2]][address[3]];
    }
    public void set( Faces face, char state) {
        int[] address = new int[4];
        address[face.axisIndex(face.A)] = 1;
        address[face.axisIndex(face.B)] = 1;
        address[face.axisIndex(face.C)] = face.value(face.C);
        address[face.axisIndex(face.D)] = face.value(face.D);
        board[address[0]][address[1]][address[2]][address[3]] = state;
        System.out.println("[State] " + face + " set to " + state);
    }
    public boolean check( Faces face) {
        return isWon(get(face));
    }
    private void checkCube(int depth, int axis) {
        System.out.println("[State] Checking Cube " + cubeName(depth,axis) + "...");
        char[] net = getNet(depth, axis);
        boolean stalemate = true;

        //Checking Stalemate
        if(cubes[depth][axis]!='s') {
            for (int i = 0; i < 3; i++) {
                if (notConflicting(net[i * 2], net[i * 2 + 1])) {
                    for (int j = 0; j < 6; j++) {
                        if (j != i * 2 && j != i * 2 + 1) {
                            if (notConflicting(net[i * 2], net[j])) {
                                System.out.println("[State] Cube " + cubeName(depth,axis) + " not in stalemate");
                                stalemate = false;
                                break;
                            }
                        }
                    }
                }
                if(!stalemate){
                    break;
                }
            }
        }
        //Checking Cube
        if(!stalemate) {
            if (!isWon(cubes[depth][axis])) {
                for (int i = 0; i < 3; i++) {
                    char side = net[i * 2];
                    char opposite = net[(i * 2) + 1];
                    if (isWon(side)) {
                        if (side == opposite) {
                            for (int j = 0; j < 6; j++) {
                                if ((j != i * 2) && (j != i * 2 + 1)) {
                                    if (net[j] == side) {
                                        cubes[depth][axis] = side;
                                    }
                                }
                            }
                        }
                    }
                }
                if (isWon(cubes[depth][axis])) {
                    //someone won outcome
                    System.out.println("[State] Cube " + cubeName(depth, axis) + " won by " + cubes[depth][axis]);
                } else {
                    //no change outcome
                    System.out.println("[State] Cube " + cubeName(depth, axis) + " not won");
                }
                System.out.println("[State] " + cubeName(depth, axis) + " set to " + cubes[depth][axis]);
            } else {
                //already won outcome
                System.out.println("[State] Cube " + cubeName(depth, axis) + " already won by " + cubes[depth][axis]);
            }
        } else {
            cubes[depth][axis] = 's';
            System.out.println("[State] Cube " + cubeName(depth,axis) + " in stalemate");
            System.out.println("[State] " + cubeName(depth, axis) + " set to s");
        }
    }
    private void checkHyper() {
        //print cubeStates
        System.out.println("[State]      0 1");
        System.out.println("[State]    x " + cubes[0][0] + " " + cubes[1][0]);
        System.out.println("[State]    y " + cubes[0][1] + " " + cubes[1][1]);
        System.out.println("[State]    z " + cubes[0][2] + " " + cubes[1][2]);
        System.out.println("[State]    w " + cubes[0][3] + " " + cubes[1][3]);
        boolean stalemate = true;
        for (int i = 0; i < 4; i++) {
            if(notConflicting(cubes[1][i], cubes[0][i])){
                for (int depth = 0; depth < 2; depth++) {
                    for (int axis = 0; axis < 4; axis++) {
                        if (axis != i) {
                            if (notConflicting(cubes[depth][axis], cubes[0][i])){
                                stalemate= false;
                            }
                        }
                    }
                }
            }
        }
        if(!stalemate) {
            if (!isWon(hyper)) {
                for (int i = 0; i < 4; i++) {
                    if (isWon(cubes[0][i])) {
                        if (cubes[1][i] == cubes[0][i]) {
                            for (int depth = 0; depth < 2; depth++) {
                                for (int axis = 0; axis < 4; axis++) {
                                    if (axis != i) {
                                        if (cubes[depth][axis] == cubes[0][i]) {
                                            hyper = cubes[0][i];
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                if (!isWon(hyper)) {
                    System.out.println("[State] Hypercube not won");
                } else {
                    System.out.println("[State] Hypercube won by " + hyper);
                }
            } else {
                System.out.println("[State] Hypercube already won by " + hyper);
            }
        } else {
            hyper='s';
            System.out.println("[State] Hypercube in stalemate");
        }
    }
    public void checkGame() {
        int checks = 0;
        boolean won = false;
        System.out.println();
        System.out.println("Checking Game...");
        for(int axis = 0; axis<4; axis++) {
            for (int depth = 0; depth < 2; depth++) {
                checkCube(depth, axis);
            }
        }
        System.out.println("[State] Checking Hypercube...");
        checkHyper();
    }

    public void paintHyper(Graphics2D g){
        //hyper='x';
        g.setFont(new Font("Courier", Font.PLAIN, 15));
        g.drawRect(50, 50, 240, 240);
        g.drawString("Hyper Net",50, 315);
        String[] names = {"w0","x0","y1","z1","w1","x1","y0","z0"};
        int[] stringA = {3,0,1,2,3,0,1,2};
        int[] stringD = {0,0,1,1,1,1,0,0};
        for(int i=0;i<8;i++) {
            g.setColor(Color.black);
            g.fillPolygon(Polygons.hyperNetPolygons[i]);
            g.setColor(getColor(stringA[i], stringD[i], true));
            g.drawPolygon(Polygons.hyperNetPolygons[i]);
            if (i != 0) {
                g.drawString(cubeName(stringD[i], stringA[i]), Polygons.hyperNetPolygons[i].getBounds().x + 20, Polygons.hyperNetPolygons[i].getBounds().y + 39);
            } else {
                g.drawString("w0",70,80);
            }
        }
        g.setColor(Color.white);
    }
    public void paintEndScreen(Graphics2D g){
        g.setFont(new Font("Courier", Font.PLAIN, 15));
        g.drawRect(460, 230, 240, 240);
        g.drawString("Hyper Net",460, 490);
        String[] names = {"w0","x0","y1","z1","w1","x1","y0","z0"};
        int[] stringA = {3,0,1,2,3,0,1,2};
        int[] stringD = {0,0,1,1,1,1,0,0};
        for(int i=0;i<8;i++) {
            g.setColor(Color.black);
            g.fillPolygon(Polygons.endScreenPolygons[i]);
            g.setColor(getColor(stringA[i], stringD[i], false));
            g.drawPolygon(Polygons.endScreenPolygons[i]);
            if (i != 0) {
                g.drawString(cubeName(stringD[i], stringA[i]), Polygons.endScreenPolygons[i].getBounds().x + 20, Polygons.endScreenPolygons[i].getBounds().y + 39);
            } else {
                g.drawString("w0",480,260);
            }
        }
        g.setColor(Color.white);
    }
    public void paintCube(Graphics2D g) {
        g.drawRect(50, 340, 240, 240);
        g.drawString("Cube Net       Viewing : ", 50, 605);
        int[] stringX = {125,210,105,230,185,150};
        int[] stringY = {465,460,505,425,425,505};
        int[] cubePointsX = new int[]{170, 90, 90, 170, 250, 250};
        int[] cubePointsY = new int[]{370, 410, 510, 550, 510, 410};
        Polygon backgroundCube = new Polygon(cubePointsX, cubePointsY, 6);
        if(load<0) {
            g.drawString("                         " + cubeName(cubeDepth, cubeAxis), 50, 605);
            g.setColor(Color.black);
            g.fillPolygon(backgroundCube);
            g.setColor(getColor(cubeAxis, cubeDepth,false));
            g.drawPolygon(backgroundCube);
            g.drawString(cubeName(cubeDepth, cubeAxis), 90, 385);

            for(int i=0;i<6;i++){
                g.setColor(Color.black);
                g.fillPolygon(Polygons.cubeNetPolygons[(i+1)%6]);
                g.setColor(getColorNet((i+1)%6));
                g.drawPolygon(Polygons.cubeNetPolygons[(i+1)%6]);
                g.drawString( (i+1)%6 + "", stringX[(i+1)%6], stringY[(i+1)%6]);
            }
        }
        g.setColor(Color.white);
    }
    private Color getColor(int axis,int depth,boolean highlight){
        char state = cubes[depth][axis];
        if(depth==cubeDepth&&axis==cubeAxis&&highlight){
            return Color.yellow;
        }
        if(isWon(state)){
            if(state=='x'){
                return Color.red;
            } else {
                return Color.blue;
            }
        } else if(state=='s'){
            return Color.darkGray;
        } else {
            return Color.white;
        }
    }
    private Color getColorNet(int face){
        char state = cubeNet[face];
        if(isWon(state)){
            if(state=='x'){
                return Color.red;
            } else {
                return Color.blue;
            }
        } else if(state=='s'){
            return Color.darkGray;
        } else {
            return Color.white;
        }
    }

    public void updateComponent() {
        if(load>=0){
            load--;
        }
    }
    public void reloadCubeNet(boolean silent) {
        cubeNet=getNet(cubeDepth,cubeAxis);
        if(!silent){
            load=15;
        }
    }
    public void mouseClicked(MouseEvent e) {
        boolean click = true;
        int mosX=e.getX();
        int mosY=e.getY();
        int[] selectedAxis = {3,0,1,2,3,0,1,2};
        int[] selectedDepth = {0,0,1,1,1,1,0,0};
        click=false;
        //loop through selections and check each
        for(int i=0;i<8;i++){
            if(Polygons.hyperNetPolygons[i].contains(mosX,mosY)){
                cubeAxis=selectedAxis[i];
                cubeDepth=selectedDepth[i];
                click=true;
            }
        }
        if(click) {
            System.out.println();
            System.out.println("Changing Cube Net...");
            cubeNet=getNet(cubeDepth,cubeAxis);
            load=15;
        }
    }

    static class Polygons {
        // Hyper Net                                  0   1   2   3   4   5   6   7
        private static final int[] hyperNetX =       {30 ,0  ,0  ,30 ,60 ,60 };
        private static final int[] hyperNetY =       {0  ,12 ,54 ,66 ,54 ,12 };
        private static final int[] hyperNetOffsetX = {70 ,90 ,140,190,140,190,140,90 };
        private static final int[] hyperNetOffsetY = {60 ,107,197,107,137,167,77 ,167};
        public static final Polygon[] hyperNetPolygons  = new Polygon[8];
        public static final Polygon[] endScreenPolygons = new Polygon[8];
        // Cube Net                                   0   1   2   3   4   5   6   7   8   9   10  11
        private static final int[] cubeNetX =        {50 ,0  ,50 ,100,50 ,0  ,0  ,50 ,50 ,0  ,0  ,50 };
        private static final int[] cubeNetY =        {0  ,25 ,50 ,25 ,25 ,0  ,60 ,85 ,0  ,25 ,85 ,60 };
        private static final int[] cubeNetOffsetX =  {80 ,160,80 ,210,160,130};
        private static final int[] cubeNetOffsetY =  {435,435,460,375,375,460};
        public static final Polygon[] cubeNetPolygons = new Polygon[6];

        static {
            initializePolygons();
        }

        private static void initializePolygons() {
            int[] pointsX = new int[6];
            int[] pointsY = new int[6];
            int[] endScreenX = new int[6];
            int[] endScreenY = new int[6];
            // Hyper Net Polygons
            for(int i=0;i<8;i++) {
                for(int p=0;p<6;p++) {
                    // Cube w0 override
                    if(i!=0) {
                        pointsX[p] = hyperNetX[p] + hyperNetOffsetX[i];
                        pointsY[p] = hyperNetY[p] + hyperNetOffsetY[i];
                    } else {
                        pointsX[p] = (hyperNetX[p]*10)/3 + hyperNetOffsetX[i];
                        pointsY[p] = (hyperNetY[p]*10)/3 + hyperNetOffsetY[i];
                    }
                    endScreenX[p] = pointsX[p] + 410;
                    endScreenY[p] = pointsY[p] + 180;
                }
                hyperNetPolygons[i] = new Polygon(pointsX,pointsY,6);
                endScreenPolygons[i] = new Polygon(endScreenX,endScreenY,6);
            }
            // Cube Net Polygons
            pointsX = new int[4];
            pointsY = new int[4];
            for(int i=0;i<6;i++){
                for(int p=0;p<4;p++){
                    pointsX[p] = cubeNetX[p + (i/2)*4] + cubeNetOffsetX[i];
                    pointsY[p] = cubeNetY[p + (i/2)*4] + cubeNetOffsetY[i];
                }
                cubeNetPolygons[i] = new Polygon(pointsX,pointsY,4);
            }
        }
    }
}