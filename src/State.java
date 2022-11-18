import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.Random;

public class State {
    protected char[][][][] board;
    protected char[][] cubes;
    protected char hyper;
    Faces curr;

    int mosX;
    int mosY;
    int cubeAxis=2;
    int cubeDepth=0;
    char[] cubeNet = new char[6];
    int load;
    int flash;
    Polygon[] polygons;

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
        if (axis == 0) {
            for (int i = 0; i < 6; i++) {
                net[i] = board[depth*2][columnA[i]][columnB[i]][flip(columnC[i],depth)];
                //System.out.println("[State] Net " + i + ": " + depth*2 + " " + columnA[i] + " " + columnB[i] + " " + flip(columnC[i],depth) + " : " + net[i]);
            }
        } else if (axis == 1) {
            for (int i = 0; i < 6; i++) {
                net[i] = board[columnC[i]][depth*2][columnB[i]][flip(columnA[i],depth)];
                //System.out.println("[State] Net " + i + ": " + columnC[i] + " " + depth*2 + " " + columnB[i] + " " + flip(columnA[i],depth) + " : " + net[i]);
            }
        } else if (axis == 2) {
            for (int i = 0; i < 6; i++) {
                net[i] = board[columnC[i]][columnA[i]][depth*2][flip(columnB[i],depth)];
                //System.out.println("[State] Net " + i + ": " + columnC[i] + " " + columnA[i] + " " + depth*2 + " " + flip(columnB[i],depth) + " : " + net[i]);
            }
        } else {
            for (int i = 0; i < 6; i++) {
                net[i] = board[columnC[i]][columnA[i]][columnB[i]][depth*2];
                //System.out.println("[State] Net " + i + ": " + columnC[i] + " " + columnA[i] + " " + columnB[i] + " " + depth*2 + " : " + net[i]);
            }
        }
        System.out.print("[State]   ");
        for(int i=0;i<3;i++){
            System.out.print(" " + net[i*2] + net[i*2+1]);
        }
        System.out.println();
        return net;
    }

    public boolean conflicting(char state1, char state2){
        if(isWon(state1)&&isWon(state2)){
            if(state1 != state2){
                return true;
            }
            return false;
        } else if (state1 == 's' || state2 == 's'){
            return true;
        } else {
            return false;
        }
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
        System.out.println("[State] " + face + " set to " + state);
    }
    public boolean check( Faces face) {
        return isWon(get(face));
    }
    public boolean checkStalemate(int depth, int axis, char[] net) {
        if(cubes[depth][axis]!='s') {
            for (int i = 0; i < 3; i++) {
                if (!conflicting(net[i * 2], net[i * 2 + 1])) {
                    for (int j = 0; j < 6; j++) {
                        if (j != i * 2 && j != i * 2 + 1) {
                            if (!conflicting(net[i * 2], net[j])) {
                                System.out.println("[State] Cube " + cubeName(depth,axis) + " not in stalemate");
                                return false;
                            }
                        }
                    }
                }
            }
            cubes[depth][axis] = 's';
            System.out.println("[State] Cube " + cubeName(depth,axis) + " in stalemate");
            System.out.println("[State] " + cubeName(depth, axis) + " set to s");
        }
        return true;
    }
    public void checkCube(int depth, int axis) {
        //check if already won or not
        System.out.println("[State] Checking Cube " + cubeName(depth,axis) + "...");
        char[] net = getNet(depth, axis);
        if(!checkStalemate(depth,axis,net)) {
            if (!isWon(cubes[depth][axis])) {
                //checks cube for victory, updates its position in cubeStates
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
        }
    }
    public void checkHyper() {
        char temp;
        String row;

        //print cubeStates
        System.out.println("[State]      0 1");
        System.out.println("[State]    x " + cubes[0][0] + " " + cubes[1][0]);
        System.out.println("[State]    y " + cubes[0][1] + " " + cubes[1][1]);
        System.out.println("[State]    z " + cubes[0][2] + " " + cubes[1][2]);
        System.out.println("[State]    w " + cubes[0][3] + " " + cubes[1][3]);
        boolean stalemate = true;
        for (int i = 0; i < 4; i++) {
            if(!conflicting(cubes[1][i],cubes[0][i])){
                for (int depth = 0; depth < 2; depth++) {
                    for (int axis = 0; axis < 4; axis++) {
                        if (axis != i) {
                            if (!conflicting(cubes[depth][axis],cubes[0][i])){
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
    }
    public void endGamePolygons() {
        int offsetX=410;
        int offsetY=250;
        int[] px;
        int[] py;
        for(int i=0;i<8;i++){
            px=polygons[i].xpoints;
            py=polygons[i].ypoints;
            for(int j=0;j<6;j++){
                px[j]=px[j]+offsetX;
                py[j]=py[j]+offsetY;
            }
            polygons[i]= new Polygon(px,py,6);
        }
    }
    public void paintEnd(Graphics2D g){
        int offsetX=410;
        int offsetY=250;

        g.drawRect(460, 300, 240, 240);
        g.setColor(Color.black);
        g.fillPolygon(polygons[3]);
        g.setColor(getColor(3,0,false));
        g.drawPolygon(polygons[3]);
        g.drawString("w0",70+offsetX,80+offsetY);
        //x0
        g.setColor(Color.black);
        g.fillPolygon(polygons[0]);
        g.setColor(getColor(0,0,false));
        g.drawPolygon(polygons[0]);
        g.drawString("x0",110+offsetX,146+offsetY);
        //z2  250 - 190 , 170 - 110
        g.setColor(Color.black);
        g.fillPolygon(polygons[6]);
        g.setColor(getColor(2,1,false));
        g.drawPolygon(polygons[6]);
        g.drawString("z1",215+offsetX,146+offsetY);
        //y2  200 - 140 , 260 - 200
        g.setColor(Color.black);
        g.fillPolygon(polygons[5]);
        g.setColor(getColor(1,1,false));
        g.drawPolygon(polygons[5]);
        g.drawString("y1",163+offsetX,236+offsetY);
        //w2  200 - 140 , 200 - 140
        g.setColor(Color.black);
        g.fillPolygon(polygons[7]);
        g.setColor(getColor(3,1,false));
        g.drawPolygon(polygons[7]);
        g.drawString("w1",163+offsetX,176+offsetY);
        //x2  170 - 90  , 230 - 170
        g.setColor(Color.black);
        g.fillPolygon(polygons[4]);
        g.setColor(getColor(0,1,false));
        g.drawPolygon(polygons[4]);
        g.drawString("x1",215+offsetX,206+offsetY);
        //z0  150 - 90 , 230 - 170
        g.setColor(Color.black);
        g.fillPolygon(polygons[2]);
        g.setColor(getColor(2,0,false));
        g.drawPolygon(polygons[2]);
        g.drawString("z0",110+offsetX,206+offsetY);
        //y0  200 - 140 ,  140 - 60
        g.setColor(Color.black);
        g.fillPolygon(polygons[1]);
        g.setColor(getColor(1,0,false));
        g.drawPolygon(polygons[1]);
        g.drawString("y0",163+offsetX,116+offsetY);
        g.setColor(Color.white);
    }
    public void paintHyper(Graphics2D g){
        g.drawRect(50, 50, 240, 240);
        g.drawString("Hyper Net",50, 315);

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
            g.setColor(Color.black);
            g.fillPolygon(polygons[8]);
            g.setColor(getColor(cubeAxis, cubeDepth,false));
            g.drawPolygon(polygons[8]);
            g.drawString(cubeName(cubeDepth, cubeAxis), 90, 385);

            offsetX = 160;
            offsetY = 375;
            g.setColor(Color.black);
            g.fillPolygon(polygons[9]);
            g.setColor(getColorNet(1));
            g.drawPolygon(polygons[9]);
            g.drawString("1", 50 + offsetX, 90 + offsetY);

            g.setColor(Color.black);
            g.fillPolygon(polygons[10]);
            g.setColor(getColorNet(3));
            g.drawPolygon(polygons[10]);
            g.drawString("3", 70 + offsetX, 50 + offsetY);


            g.setColor(Color.black);
            g.fillPolygon(polygons[11]);
            g.setColor(getColorNet(4));
            g.drawPolygon(polygons[11]);
            g.drawString("4", 25 + offsetX, 50 + offsetY);

            offsetX = 80;
            offsetY = 435;
            g.setColor(Color.black);
            g.fillPolygon(polygons[12]);
            g.setColor(getColorNet(5));
            g.drawPolygon(polygons[12]);
            g.drawString("5", 70 + offsetX, 70 + offsetY);

            g.setColor(Color.black);
            g.fillPolygon(polygons[13]);
            g.setColor(getColorNet(2));
            g.drawPolygon(polygons[13]);
            g.drawString("2", 25 + offsetX, 70 + offsetY);

            g.setColor(Color.black);
            g.fillPolygon(polygons[14]);
            g.setColor(getColorNet(0));
            g.drawPolygon(polygons[14]);
            g.drawString("0", 45 + offsetX, 30 + offsetY);
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
        if(flash>=0){
            flash--;
        } else {
            flash=60;
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
        } else {
            click=false;
        }
        if(click) {
            System.out.println();
            System.out.println("Changing Cube Net...");
            cubeNet=getNet(cubeDepth,cubeAxis);
            load=15;
        }
    }
}
