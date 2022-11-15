import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.Random;

public class State {
    protected char[][][][] board;
    protected char[][] cubes;
    protected char hyper;

    int mosX;
    int mosY;
    int cubeAxis;
    int cubeDepth;
    char[] cubeNet = new char[6];
    int load;
    Polygon polygons[];
    String[] cubeNames = new String[6];
    int[][] cubeAddresses = new int[6][2];

    public State(){
        board = new char[3][3][3][3];
        cubes = new char[2][4];
        hyper = ' ';
        initializePolygons();
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
    private char flattenCase(char ch) {
        return (Character.toLowerCase(ch));
    }
    private int evaluate(char ch) {
        if(Character.isUpperCase(ch)){
            return 2;
        } else {
            return 0;
        }
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
        char[] net = new char[6];
        System.out.println();
        System.out.println("[State] Retrieving cube " + cubeName(depth,axis));
        if (axis == 0) {
            for (int i = 0; i < 6; i++) {
                net[i] = board[depth*2][columnA[i]][columnB[i]][flip(columnC[i],depth)];
                System.out.println("[State] Net " + i + ": " + depth*2 + " " + columnA[i] + " " + columnB[i] + " " + flip(columnC[i],depth) + " : " + net[i]);
            }
        } else if (axis == 1) {
            for (int i = 0; i < 6; i++) {
                net[i] = board[columnC[i]][depth*2][columnB[i]][flip(columnA[i],depth)];
                System.out.println("[State] Net " + i + ": " + columnC[i] + " " + depth*2 + " " + columnB[i] + " " + flip(columnA[i],depth) + " : " + net[i]);
            }
        } else if (axis == 2) {
            for (int i = 0; i < 6; i++) {
                net[i] = board[columnC[i]][columnA[i]][depth*2][flip(columnB[i],depth)];
                System.out.println("[State] Net " + i + ": " + columnC[i] + " " + columnA[i] + " " + depth*2 + " " + flip(columnB[i],depth) + " : " + net[i]);
            }
        } else {
            for (int i = 0; i < 6; i++) {
                net[i] = board[columnC[i]][columnA[i]][columnB[i]][depth*2];
                System.out.println("[State] Net " + i + ": " + columnC[i] + " " + columnA[i] + " " + columnB[i] + " " + depth*2 + " : " + net[i]);
            }
        }
        System.out.println();
        return net;
    }
    public void randomizer(int randomness){
        board = new char[3][3][3][3];
        cubes = new char[2][4];
        Random ran = new Random();
        for(int w = 0; w < 3; w++) {
            for(int y = 0; y < 3; y++) {
                for(int x = 0; x < 3; x++) {
                    for(int z = 0; z < 3; z++) {
                        int r = ran.nextInt(randomness);
                        if (r == 0) {
                            board[x][y][z][w] = 'x';
                            System.out.print('x');
                        } else if (r == 1) {
                            board[x][y][z][w] = 'o';
                            System.out.print('o');
                        } else {
                            board[x][y][z][w] = '-';
                            System.out.print('-');
                        }
                    }
                }
            }
        }
        checkGame();
        cubeNet=getNet(cubeDepth,cubeAxis);
        load=20;
    }

    public char get( Faces face) {
        char state;
        if(flattenCase(face.C)=='x') {
            if(flattenCase(face.D)=='y') {
                state = board[evaluate(face.C)][evaluate(face.D)][1][1];
            } else if(flattenCase(face.D)=='z') {
                state = board[evaluate(face.C)][1][evaluate(face.D)][1];
            } else {
                state = board[evaluate(face.C)][1][1][evaluate(face.D)];
            }
        } else if(flattenCase(face.C)=='y') {
            if(flattenCase(face.D)=='x') {
                state = board[evaluate(face.D)][evaluate(face.C)][1][1];
            } else if(flattenCase(face.D)=='z') {
                state = board[1][evaluate(face.C)][evaluate(face.D)][1];
            } else {
                state = board[1][evaluate(face.C)][1][evaluate(face.D)];
            }
        } else if(flattenCase(face.C)=='z') {
            if(flattenCase(face.D)=='x') {
                state = board[evaluate(face.D)][1][evaluate(face.C)][1];
            } else if(flattenCase(face.D)=='y') {
                state = board[1][evaluate(face.D)][evaluate(face.C)][1];
            } else {
                state = board[1][1][evaluate(face.C)][evaluate(face.D)];
            }
        } else {
            if (flattenCase(face.D) == 'x') {
                state = board[evaluate(face.D)][1][1][evaluate(face.C)];
            } else if (flattenCase(face.D) == 'y') {
                state = board[1][evaluate(face.D)][1][evaluate(face.C)];
            } else {
                state = board[1][1][evaluate(face.D)][evaluate(face.C)];
            }
        }
        System.out.println("[State] BoardState " + face + " is " + state);
        return state;
    }
    public void set( Faces face, char state) {
        if(flattenCase(face.C)=='x') {
            if(flattenCase(face.D)=='y') {
                board[evaluate(face.C)][evaluate(face.D)][1][1] = state;
            } else if(flattenCase(face.D)=='z') {
                board[evaluate(face.C)][1][evaluate(face.D)][1] = state;
            } else {
                board[evaluate(face.C)][1][1][evaluate(face.D)] = state;
            }
        } else if(flattenCase(face.C)=='y') {
            if(flattenCase(face.D)=='x') {
                board[evaluate(face.D)][evaluate(face.C)][1][1] = state;
            } else if(flattenCase(face.D)=='z') {
                board[1][evaluate(face.C)][evaluate(face.D)][1] = state;
            } else {
                board[1][evaluate(face.C)][1][evaluate(face.D)] = state;
            }
        } else if(flattenCase(face.C)=='z') {
            if(flattenCase(face.D)=='x') {
                board[evaluate(face.D)][1][evaluate(face.C)][1] = state;
            } else if(flattenCase(face.D)=='y') {
                board[1][evaluate(face.D)][evaluate(face.C)][1] = state;
            } else {
                board[1][1][evaluate(face.C)][evaluate(face.D)] = state;
            }
        } else {
            if (flattenCase(face.D) == 'x') {
                board[evaluate(face.D)][1][1][evaluate(face.C)] = state;
            } else if (flattenCase(face.D) == 'y') {
                board[1][evaluate(face.D)][1][evaluate(face.C)] = state;
            } else {
                board[1][1][evaluate(face.D)][evaluate(face.C)] = state;
            }
        }
        System.out.println("[State] BoardState " + face + " set to " + state);
    }
    public boolean check( Faces face) {
        return isWon(get(face));
    }
    public void checkCube(int depth, int axis) {
        System.out.println("");
        //check if already won or not
        if (!isWon(cubes[depth][axis])) {
            char[] net = getNet(depth,axis);
            //checks cube for victory, updates its position in cubeStates
            for (int i = 0; i < 3; i++) {
                char side = net[i*2];
                char opposite = net[(i*2)+1];
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
                System.out.println("[State] Cube " + cubeName(depth,axis) + " won by " + cubes[depth][axis]);
            } else {
                //no change outcome
                System.out.println("[State] Cube " + cubeName(depth,axis) + " not won");
            }
        }
        else {
            //already won outcome
            System.out.println("[State] Ccube " + cubeName(depth,axis) + " already won by " + cubes[depth][axis]);
        }
    }
    public void checkHyper() {
        char temp;
        String row;

        //print cubeStates
        System.out.println("");
        if(!isWon(hyper)) {
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
            if(!isWon(hyper)) {
                System.out.println("[State] Hypercube not won");
            } else {
                System.out.println("[State] Hypercube won by " + hyper);
            }
        } else {
            System.out.println("[State] Hypercube already won by " + hyper);
        }
    }
    public void checkGame() {
        int checks = 0;
        boolean won = false;

        System.out.println("");
        System.out.println("[State] Checking Cubes...");
        for(int axis = 0; axis<4; axis++) {
            for (int depth = 0; depth < 2; depth++) {
                checkCube(depth, axis);
            }
        }
        checkHyper();
    }


    private void initializePolygons() {
        int[] px;
        int[] py;
        int offsetX;
        int offsetY;
        polygons = new Polygon[15];
        //x0
        px = new int[] {120,90,90,120,150,150};
        py = new int[] {107,119,161,173,161,119};
        polygons[0] = new Polygon(px,py,6);
        //y0
        px = new int[] {170,140,140,170,200,200};
        py = new int[] {77,89,131,143,131,89};
        polygons[1] = new Polygon(px,py,6);
        //z0
        px = new int[] {120,90,90,120,150,150};
        py = new int[] {167,179,221,233,221,179};
        polygons[2] = new Polygon(px,py,6);
        //w0
        px = new int[] {170,70,70,170,270,270};
        py = new int[] {60,100,240,280,240,100};
        polygons[3] = new Polygon(px,py,6);
        //x1
        px = new int[] {220,190,190,220,250,250};
        py = new int[] {167,179,221,233,221,179};
        polygons[4] = new Polygon(px,py,6);
        //y1
        px = new int[] {170,140,140,170,200,200};
        py = new int[] {197,209,251,263,251,209};
        polygons[5] = new Polygon(px,py,6);
        //z1
        px = new int[] {220,190,190,220,250,250};
        py = new int[] {107,119,161,173,161,119};
        polygons[6] = new Polygon(px,py,6);
        //w1
        px = new int[] {170,140,140,170,200,200};
        py = new int[] {137,149,191,203,191,149};
        polygons[7] = new Polygon(px,py,6);
        //boardState bg cube
        px = new int[]{170, 90, 90, 170, 250, 250};
        py = new int[]{370, 410, 510, 550, 510, 410};
        polygons[8] = new Polygon(px,py,6);
        //front faces
        offsetX = 160;
        offsetY = 375;
        //f1
        px = new int[]{50 + offsetX, offsetX, 50 + offsetX, 100 + offsetX};
        py = new int[]{60 + offsetY, 85 + offsetY, 110 + offsetY, 85 + offsetY};
        polygons[9] = new Polygon(px,py,4);
        //f3
        px = new int[]{50 + offsetX, 100 + offsetX, 100 + offsetX, 50 + offsetX};
        py = new int[]{offsetY, 25 + offsetY, 85 + offsetY, 60 + offsetY};
        polygons[10] = new Polygon(px,py,4);
        //f4
        px = new int[]{50 + offsetX, offsetX, offsetX, 50 + offsetX};
        py = new int[]{offsetY, 25 + offsetY, 85 + offsetY, 60 + offsetY};
        polygons[11] = new Polygon(px,py,4);
        //back faces
        offsetX = 80;
        offsetY = 435;
        //f5
        px = new int[]{50 + offsetX, 100 + offsetX, 100 + offsetX, 50 + offsetX};
        py = new int[]{50 + offsetY, 25 + offsetY, 85 + offsetY, 110 + offsetY};
        polygons[12] = new Polygon(px,py,4);
        //f2
        px = new int[]{50 + offsetX, offsetX, offsetX, 50 + offsetX};
        py = new int[]{50 + offsetY, 25 + offsetY, 85 + offsetY, 110 + offsetY};
        polygons[13] = new Polygon(px,py,4);
        //f0
        px = new int[]{50 + offsetX, offsetX, 50 + offsetX, 100 + offsetX};
        py = new int[]{offsetY, 25 + offsetY, 50 + offsetY, 25 + offsetY};
        polygons[14] = new Polygon(px,py,4);

        cubeNet=getNet(0,0);
        cubeAddresses = getDisplayNet(0,0);
        for(int i=0;i<6;i++){
            cubeNames[i] = cubeName(cubeAddresses[i][0],cubeAddresses[i][1]);
        }
    }
    public void paintHyper(Graphics2D g){
        g.drawRect(50, 50, 240, 240);
        g.drawString("Hyper Net",50, 315);
        int[] px;
        int[] py;

        //w0
        g.setColor(Color.black);
        g.fillPolygon(polygons[3]);
        g.setColor(getColor(3,0,true));
        g.drawPolygon(polygons[3]);
        g.drawString("w0",70,80);
        //x0
        g.setColor(Color.black);
        g.fillPolygon(polygons[0]);
        g.setColor(getColor(0,0,true));
        g.drawPolygon(polygons[0]);
        g.drawString("x0",110,146);
        //z2  250 - 190 , 170 - 110
        g.setColor(Color.black);
        g.fillPolygon(polygons[6]);
        g.setColor(getColor(2,1,true));
        g.drawPolygon(polygons[6]);
        g.drawString("z1",215,146);
        //y2  200 - 140 , 260 - 200
        g.setColor(Color.black);
        g.fillPolygon(polygons[5]);
        g.setColor(getColor(1,1,true));
        g.drawPolygon(polygons[5]);
        g.drawString("y1",163,236);
        //w2  200 - 140 , 200 - 140
        g.setColor(Color.black);
        g.fillPolygon(polygons[7]);
        g.setColor(getColor(3,1,true));
        g.drawPolygon(polygons[7]);
        g.drawString("w1",163,176);
        //x2  170 - 90  , 230 - 170
        g.setColor(Color.black);
        g.fillPolygon(polygons[4]);
        g.setColor(getColor(0,1,true));
        g.drawPolygon(polygons[4]);
        g.drawString("x1",215,206);
        //z0  150 - 90 , 230 - 170
        g.setColor(Color.black);
        g.fillPolygon(polygons[2]);
        g.setColor(getColor(2,0,true));
        g.drawPolygon(polygons[2]);
        g.drawString("z0",110,206);
        //y0  200 - 140 ,  140 - 60
        g.setColor(Color.black);
        g.fillPolygon(polygons[1]);
        g.setColor(getColor(1,0,true));
        g.drawPolygon(polygons[1]);
        g.drawString("y0",163,116);
        g.setColor(Color.white);
    }
    public void paintCube(Graphics2D g) {
        g.drawRect(50, 340, 240, 240);
        if(load>0){
            g.drawString("Cube Net       Viewing : ", 50, 605);
        } else {
            g.drawString("Cube Net       Viewing : " + cubeName(cubeDepth, cubeAxis), 50, 605);
        }
        int[] px;
        int[] py;
        int offsetX;
        int offsetY;
        if(load<0) {
            g.setColor(getColor(cubeAxis, cubeDepth,false));
            g.drawPolygon(polygons[8]);
            g.drawString(cubeName(cubeDepth, cubeAxis), 90, 385);

            //Addresses[address][0=depth,1=axis]
            offsetX = 160;
            offsetY = 375;
            g.setColor(Color.black);
            g.fillPolygon(polygons[9]);
            g.setColor(getColorNet(1));
            g.drawPolygon(polygons[9]);
            g.drawString(cubeNames[1], 50 + offsetX, 90 + offsetY);

            g.setColor(Color.black);
            g.fillPolygon(polygons[10]);
            g.setColor(getColorNet(3));
            g.drawPolygon(polygons[10]);
            g.drawString(cubeNames[3], 70 + offsetX, 50 + offsetY);

            g.setColor(Color.black);
            g.fillPolygon(polygons[11]);
            g.setColor(getColorNet(4));
            g.drawPolygon(polygons[11]);
            g.drawString(cubeNames[4], 25 + offsetX, 50 + offsetY);

            offsetX = 80;
            offsetY = 435;
            g.setColor(Color.black);
            g.fillPolygon(polygons[12]);
            g.setColor(getColorNet(5));
            g.drawPolygon(polygons[12]);
            g.drawString(cubeNames[5], 70 + offsetX, 70 + offsetY);

            g.setColor(Color.black);
            g.fillPolygon(polygons[13]);
            g.setColor(getColorNet(2));
            g.drawPolygon(polygons[13]);
            g.drawString(cubeNames[2], 25 + offsetX, 70 + offsetY);

            g.setColor(Color.black);
            g.fillPolygon(polygons[14]);
            g.setColor(getColorNet(0));
            g.drawPolygon(polygons[14]);
            g.drawString(cubeNames[0], 45 + offsetX, 30 + offsetY);
        }
        g.setColor(Color.white);
    }
    private int[][] getDisplayNet(int depth, int axis) {
        //fill net from boardStates
        int[] columnXa = {1,1,2,2,3,3};
        int[] columnYa = {3,3,2,2,0,0};
        int[] columnZa = {1,1,3,3,0,0};
        int[] columnWa = {1,1,2,2,0,0};
        int[] columnXd = {0,1,0,1,depth,(depth+1)%2};
        int[] columnYd = {depth,(depth+1)%2,0,1,0,1};
        int[] columnZd = {0,1,depth,(depth+1)%2,0,1};
        int[] columnWd = {0,1,0,1,0,1};

        int[][] net = new int[6][2];
        System.out.println();
        System.out.println("[State] Retrieving cube " + cubeName(depth,axis));
        if (axis == 0) {
            for (int i = 0; i < 6; i++) {
                net[i][0] = columnXd[i];
                net[i][1] = columnXa[i];
                System.out.println("[State] Display Net " + i + ": " + cubeName(net[i][0],net[i][1]));
            }
        } else if (axis == 1) {
            for (int i = 0; i < 6; i++) {
                net[i][0] = columnYd[i];
                net[i][1] = columnYa[i];
                System.out.println("[State] Display Net " + i + ": " + cubeName(net[i][0],net[i][1]));
            }
        } else if (axis == 2) {
            for (int i = 0; i < 6; i++) {
                net[i][0] = columnZd[i];
                net[i][1] = columnZa[i];
                System.out.println("[State] Display Net " + i + ": " + cubeName(net[i][0],net[i][1]));
            }
        } else {
            for (int i = 0; i < 6; i++) {
                net[i][0] = columnWd[i];
                net[i][1] = columnWa[i];
                System.out.println("[State] Display Net " + i + ": " + cubeName(net[i][0],net[i][1]));
            }
        }
        System.out.println();
        return net;
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
        } else {
            return Color.white;
        }
    }
    public void updateComponent() {
        if(load>=0){
            load--;
        }
    }
    public void mouseClicked(MouseEvent e) {
        boolean click = true;
        mosX=e.getX();
        mosY=e.getY();
        if((mosX<200&&mosX>140)&&(mosY<140&&mosY>80)){
            //y0  200 - 140 ,  140 - 60
            cubeAxis=1;
            cubeDepth=0;
        } else if((mosX<150&&mosX>90)&&(mosY<230&&mosY>170)){
            //z0  150 - 90 , 230 - 170
            cubeAxis=2;
            cubeDepth=0;
        } else if((mosX<250&&mosX>190)&&(mosY<230&&mosY>170)){
            //x1  250 - 190  , 230 - 170
            cubeAxis=0;
            cubeDepth=1;
        } else if((mosX<200&&mosX>140)&&(mosY<200&&mosY>140)){
            //w2  200 - 140 , 200 - 140
            cubeAxis=3;
            cubeDepth=1;
        } else if((mosX<200&&mosX>140)&&(mosY<260&&mosY>200)){
            //y2  200 - 140 , 260 - 200
            cubeAxis=1;
            cubeDepth=1;
        } else if((mosX<250&&mosX>190)&&(mosY<170&&mosY>110)){
            //z2  250 - 190 , 170 - 110
            cubeAxis=2;
            cubeDepth=1;
        } else if((mosX<150&&mosX>90)&&(mosY<170&&mosY>110)){
            //x0  150 - 90  , 170 - 110
            cubeAxis=0;
            cubeDepth=0;
        } else if((mosX<270&&mosX>70)&&(mosY<270&&mosY>70)){
            //w0  270 - 70  , 270 - 70
            cubeAxis=3;
            cubeDepth=0;
        } else if((mosX<(160+100)&&mosX>(160))&&(mosY<(375+110)&&mosY>(375+60))){
            //f1
            cubeAxis=cubeAddresses[1][1];
            cubeDepth=cubeAddresses[1][0];
        } else if((mosX<(160+100)&&mosX>(160+50))&&(mosY<(375+85)&&mosY>(375-25))) {
            //f3
            cubeAxis = cubeAddresses[3][1];
            cubeDepth = cubeAddresses[3][0];
        } else if((mosX<(160+50)&&mosX>(160))&&(mosY<(375+85)&&mosY>(375-25))) {
            //f4
            cubeAxis = cubeAddresses[4][1];
            cubeDepth = cubeAddresses[4][0];
        } else if((mosX<(80+100)&&mosX>(80+50))&&(mosY<(435+110)&&mosY>(435+50))) {
            //f5
            cubeAxis = cubeAddresses[5][1];
            cubeDepth = cubeAddresses[5][0];
        } else if((mosX<(80+50)&&mosX>(80))&&(mosY<(435+110)&&mosY>(435+50))) {
            //f2
            cubeAxis = cubeAddresses[2][1];
            cubeDepth = cubeAddresses[2][0];
        } else if((mosX<(80+100)&&mosX>(80))&&(mosY<(435+50)&&mosY>(435))) {
            //f0
            cubeAxis = cubeAddresses[0][1];
            cubeDepth = cubeAddresses[0][0];
        } else {
            click=false;
            randomizer(4);
        }
        if(click) {

            cubeNet=getNet(cubeDepth,cubeAxis);
            cubeAddresses = getDisplayNet(cubeDepth,cubeAxis);
            for(int i=0;i<6;i++){
                cubeNames[i] = cubeName(cubeAddresses[i][0],cubeAddresses[i][1]);
            }
            load=15;
        }
    }

}
