import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.Random;

public class Board {
    protected char[][][][] hyper;
    protected char[][] board;
    protected char state;
    protected char playr;
    protected Faces me;

    int mosX;
    int mosY;
    int boardX;
    int boardY;
    int winX1;
    int winX2;
    int winY1;
    int winY2;

    public Board() {
        hyper = new char[3][3][3][3];
        board = new char[3][3];
        state = '-';
        playr = 'x';
        me = new Faces ();
    }

    private int evaluate(char ch) {
        if(Character.isUpperCase(ch)){
            return 2;
        } else {
            return 0;
        }
    }
    public boolean checkChars(char flat, char check) {
        return (Character.toLowerCase(flat))==check;
    }
    private int reverseAxis(char ch, int val) {
        int[] forwards = {0,1,2};
        int[] backwards = {2,1,0};
        if(Character.isUpperCase(ch)){
            return forwards[val];
        } else {
            return backwards[val];
        }
    }
    public boolean isWon(char state) {
        return (state == 'x')||(state=='o');
    }
    public void randomizer(int randomness){
        hyper = new char[3][3][3][3];
        board = new char[3][3];
        Random ran = new Random();
        for(int w = 0; w < 3; w++) {
            for(int y = 0; y < 3; y++) {
                for(int x = 0; x < 3; x++) {
                    for(int z = 0; z < 3; z++) {
                        int r = ran.nextInt(randomness);
                        if (r == 0) {
                            hyper[x][y][z][w] = 'x';
                            System.out.print('x');
                        } else if (r == 1) {
                            hyper[x][y][z][w] = 'o';
                            System.out.print('o');
                        } else {
                            hyper[x][y][z][w] = '-';
                            System.out.print('-');
                        }
                    }
                }
            }
        }
        load(me);
    }


    public char[][] loadGeneric( Faces face){
        char[][] raw = new char[3][3];
        int d = evaluate(face.D);
        int c = evaluate(face.C);
        if(checkChars(face.A,'x')){
            if(checkChars(face.B,'y')){
                //xyzw
                if(checkChars(face.C,'z')){
                    for(int b=0;b<3;b++) {
                        for(int a=0;a<3;a++) {
                            raw[reverseAxis(face.A,a)][reverseAxis(face.B,b)] = hyper[a][b][c][d];
                        }
                    }
                }
                //xywz
                else {
                    for(int b=0;b<3;b++) {
                        for(int a=0;a<3;a++) {
                            raw[reverseAxis(face.A,a)][reverseAxis(face.B,b)] = hyper[a][b][d][c];
                        }
                    }
                }
            }
            else if(checkChars(face.B,'z')) {
                //xzyw
                if(checkChars(face.C,'y')){
                    for(int b=0;b<3;b++) {
                        for(int a=0;a<3;a++) {
                            raw[reverseAxis(face.A,a)][reverseAxis(face.B,b)] = hyper[a][c][b][d];
                        }
                    }
                }
                //xzwy
                else {
                    for(int b=0;b<3;b++) {
                        for(int a=0;a<3;a++) {
                            raw[reverseAxis(face.A,a)][reverseAxis(face.B,b)] = hyper[a][d][b][c];
                        }
                    }
                }
            }
            else {
                //xwzy
                if(checkChars(face.C,'z')){
                    for(int b=0;b<3;b++) {
                        for(int a=0;a<3;a++) {
                            raw[reverseAxis(face.A,a)][reverseAxis(face.B,b)] = hyper[a][d][c][b];
                        }
                    }
                }
                //xwyz
                else {
                    for(int b=0;b<3;b++) {
                        for(int a=0;a<3;a++) {
                            raw[reverseAxis(face.A,a)][reverseAxis(face.B,b)] = hyper[a][c][d][b];
                        }
                    }
                }
            }
        }
        else if(checkChars(face.A,'y')){
            if(checkChars(face.B,'x')){
                //yxzw
                if(checkChars(face.C,'z')){
                    for(int b=0;b<3;b++) {
                        for(int a=0;a<3;a++) {
                            raw[reverseAxis(face.A,a)][reverseAxis(face.B,b)] = hyper[b][a][c][d];
                        }
                    }
                }
                //yxwz
                else {
                    for(int b=0;b<3;b++) {
                        for(int a=0;a<3;a++) {
                            raw[reverseAxis(face.A,a)][reverseAxis(face.B,b)] = hyper[b][a][d][c];
                        }
                    }
                }
            }
            else if(checkChars(face.B,'z')){
                //yzxw
                if(checkChars(face.C,'x')){
                    for(int b=0;b<3;b++) {
                        for(int a=0;a<3;a++) {
                            raw[reverseAxis(face.A,a)][reverseAxis(face.B,b)] = hyper[c][a][b][d];
                        }
                    }
                }
                //yzwx
                else {
                    for(int b=0;b<3;b++) {
                        for(int a=0;a<3;a++) {
                            raw[reverseAxis(face.A,a)][reverseAxis(face.B,b)] = hyper[d][a][b][c];
                        }
                    }
                }
            }
            else {
                //ywzx
                if(checkChars(face.C,'z')){
                    for(int b=0;b<3;b++) {
                        for(int a=0;a<3;a++) {
                            raw[reverseAxis(face.A,a)][reverseAxis(face.B,b)] = hyper[d][a][c][b];
                        }
                    }
                }
                //ywxz
                else {
                    for(int b=0;b<3;b++) {
                        for(int a=0;a<3;a++) {
                            raw[reverseAxis(face.A,a)][reverseAxis(face.B,b)] = hyper[c][a][d][b];
                        }
                    }
                }
            }
        }
        else if(checkChars(face.A,'z')){
            if(checkChars(face.B,'x')){
                //zxyw
                if(checkChars(face.C,'y')){
                    for(int b=0;b<3;b++) {
                        for(int a=0;a<3;a++) {
                            raw[reverseAxis(face.A,a)][reverseAxis(face.B,b)] = hyper[b][c][a][d];
                        }
                    }
                }
                //zxwy
                else {
                    for(int b=0;b<3;b++) {
                        for(int a=0;a<3;a++) {
                            raw[reverseAxis(face.A,a)][reverseAxis(face.B,b)] = hyper[b][d][a][c];
                        }
                    }
                }
            }
            else if(checkChars(face.B,'y')){
                //zyxw
                if(checkChars(face.C,'x')){
                    for(int b=0;b<3;b++) {
                        for(int a=0;a<3;a++) {
                            raw[reverseAxis(face.A,a)][reverseAxis(face.B,b)] = hyper[c][b][a][d];
                        }
                    }
                }
                //zywx
                else {
                    for(int b=0;b<3;b++) {
                        for(int a=0;a<3;a++) {
                            raw[reverseAxis(face.A,a)][reverseAxis(face.B,b)] = hyper[d][b][a][c];
                        }
                    }
                }
            }
            else{
                //zwxy
                if(checkChars(face.C,'x')){
                    for(int b=0;b<3;b++) {
                        for(int a=0;a<3;a++) {
                            raw[reverseAxis(face.A,a)][reverseAxis(face.B,b)] = hyper[c][d][a][b];
                        }
                    }
                }
                //zwyx
                else {
                    for(int b=0;b<3;b++) {
                        for(int a=0;a<3;a++) {
                            raw[reverseAxis(face.A,a)][reverseAxis(face.B,b)] = hyper[d][c][a][b];
                        }
                    }
                }
            }
        }
        else{
            if(checkChars(face.B,'x')){
                //wxzy
                if(checkChars(face.C,'z')){
                    for(int b=0;b<3;b++) {
                        for(int a=0;a<3;a++) {
                            raw[reverseAxis(face.A,a)][reverseAxis(face.B,b)] = hyper[b][d][c][a];
                        }
                    }
                }
                //wxyz
                else {
                    for(int b=0;b<3;b++) {
                        for(int a=0;a<3;a++) {
                            raw[reverseAxis(face.A,a)][reverseAxis(face.B,b)] = hyper[b][c][d][a];
                        }
                    }
                }
            }
            else if(checkChars(face.B,'y')){
                //wyzx
                if(checkChars(face.C,'z')){
                    for(int b=0;b<3;b++) {
                        for(int a=0;a<3;a++) {
                            raw[reverseAxis(face.A,a)][reverseAxis(face.B,b)] = hyper[d][b][c][a];
                        }
                    }
                }
                //wyxz
                else {
                    for(int b=0;b<3;b++) {
                        for(int a=0;a<3;a++) {
                            raw[reverseAxis(face.A,a)][reverseAxis(face.B,b)] = hyper[c][b][d][a];
                        }
                    }
                }
            }
            else{
                //wzxy
                if(checkChars(face.C,'x')){
                    for(int b=0;b<3;b++) {
                        for(int a=0;a<3;a++) {
                            raw[reverseAxis(face.A,a)][reverseAxis(face.B,b)] = hyper[c][d][b][a];
                        }
                    }
                }
                //wzyx
                else {
                    for(int b=0;b<3;b++) {
                        for(int a=0;a<3;a++) {
                            raw[reverseAxis(face.A,a)][reverseAxis(face.B,b)] = hyper[d][c][b][a];
                        }
                    }
                }
            }
        }
        return raw;
    }
    public void load( Faces face) {
        System.out.println("");
        System.out.println("[Board] Loading " + face);
        board = loadGeneric(face);
        check();
        me = face;
    }
    public char checkGeneric( Faces face) {
        char win = ' ';
        char[][] raw = loadGeneric(face);

        System.out.println("");
        //check diagonal 1
        if (isWon(raw[0][0])) {
            if (raw[0][0] == raw[1][1]) {
                if (raw[0][0] == raw[2][2]) {
                    win = raw[0][0];
                }
            }
        }
        //check diagonal 2
        if (isWon(raw[0][2])) {
            if (raw[0][2] == raw[1][1]) {
                if (raw[0][2] == raw[2][0]) {
                    win = raw[0][2];
                }
            }
        }
        for (int a = 0; a < 3; a++) {
            //check vertical
            if (isWon(raw[a][0])) {
                if (raw[a][0] == raw[a][1]) {
                    if (raw[a][0] == raw[a][2]) {
                        win = raw[a][0];
                        break;
                    }
                }
            }
            //check horizontal
            if (isWon(raw[0][a])) {
                if (raw[0][a] == raw[1][a]) {
                    if (raw[0][a] == raw[2][a]) {
                        win = raw[0][a];
                        break;
                    }
                }
            }
        }
        if (isWon(win)) {
            // somebody won
            System.out.println("[Board] Board " + face + " won by " + win);
            return win;
        } else {
            // nobody won
            System.out.println("[Board] Board " + face + " not won");
            return win;
        }
    }
    public void check() {
        //check diagonal 1
        state = '-';
        if (isWon(board[0][0])) {
            if (board[0][0] == board[1][1]) {
                if (board[0][0] == board[2][2]) {
                    state = board[0][0];
                    winX1=0;
                    winX2=2;
                    winY1=0;
                    winY2=2;
                }
            }
        }
        //check diagonal 2
        if (isWon(board[0][2])) {
            if (board[0][2] == board[1][1]) {
                if (board[0][2] == board[2][0]) {
                    state = board[0][2];
                    winX1=0;
                    winX2=2;
                    winY1=2;
                    winY2=0;
                }
            }
        }
        for (int a = 0; a < 3; a++) {
            //check vertical
            if (isWon(board[a][0])) {
                if (board[a][0] == board[a][1]) {
                    if (board[a][0] == board[a][2]) {
                        state = board[a][0];
                        winX1=a;
                        winX2=a;
                        winY1=0;
                        winY2=2;
                        break;
                    }
                }
            }
            //check horizontal
            if (isWon(board[0][a])) {
                if (board[0][a] == board[1][a]) {
                    if (board[0][a] == board[2][a]) {
                        state = board[0][a];
                        winX1=0;
                        winX2=2;
                        winY1=a;
                        winY2=a;
                        break;
                    }
                }
            }
        }
    }
    public void save(){
        System.out.println("");
        System.out.println("[Board] Saving " + me);
        int d = evaluate(me.D);
        int c = evaluate(me.C);
        if(checkChars(me.A,'x')){
            if(checkChars(me.B,'y')){
                //xyzw
                if(checkChars(me.C,'z')){
                    for(int b=0;b<3;b++) {
                        for(int a=0;a<3;a++) {
                            hyper[a][b][c][d] = board[reverseAxis(me.A,a)][reverseAxis(me.B,b)];
                        }
                    }
                }
                //xywz
                else {
                    for(int b=0;b<3;b++) {
                        for(int a=0;a<3;a++) {
                            hyper[a][b][d][c] = board[reverseAxis(me.A,a)][reverseAxis(me.B,b)];
                        }
                    }
                }
            }
            else if(checkChars(me.B,'z')) {
                //xzyw
                if(checkChars(me.C,'y')){
                    for(int b=0;b<3;b++) {
                        for(int a=0;a<3;a++) {
                            hyper[a][c][b][d] = board[reverseAxis(me.A,a)][reverseAxis(me.B,b)];
                        }
                    }
                }
                //xzwy
                else {
                    for(int b=0;b<3;b++) {
                        for(int a=0;a<3;a++) {
                            hyper[a][d][b][c] = board[reverseAxis(me.A,a)][reverseAxis(me.B,b)];
                        }
                    }
                }
            }
            else {
                //xwzy
                if(checkChars(me.C,'z')){
                    for(int b=0;b<3;b++) {
                        for(int a=0;a<3;a++) {
                            hyper[a][d][c][b] = board[reverseAxis(me.A,a)][reverseAxis(me.B,b)];
                        }
                    }
                }
                //xwyz
                else {
                    for(int b=0;b<3;b++) {
                        for(int a=0;a<3;a++) {
                            hyper[a][c][d][b] = board[reverseAxis(me.A,a)][reverseAxis(me.B,b)];
                        }
                    }
                }
            }
        }
        else if(checkChars(me.A,'y')){
            if(checkChars(me.B,'x')){
                //yxzw
                if(checkChars(me.C,'z')){
                    for(int b=0;b<3;b++) {
                        for(int a=0;a<3;a++) {
                            hyper[b][a][c][d] = board[reverseAxis(me.A,a)][reverseAxis(me.B,b)];
                        }
                    }
                }
                //yxwz
                else {
                    for(int b=0;b<3;b++) {
                        for(int a=0;a<3;a++) {
                            hyper[b][a][d][c] = board[reverseAxis(me.A,a)][reverseAxis(me.B,b)];
                        }
                    }
                }
            }
            else if(checkChars(me.B,'z')){
                //yzxw
                if(checkChars(me.C,'x')){
                    for(int b=0;b<3;b++) {
                        for(int a=0;a<3;a++) {
                            hyper[c][a][b][d] = board[reverseAxis(me.A,a)][reverseAxis(me.B,b)];
                        }
                    }
                }
                //yzwx
                else {
                    for(int b=0;b<3;b++) {
                        for(int a=0;a<3;a++) {
                            hyper[d][a][b][c] = board[reverseAxis(me.A,a)][reverseAxis(me.B,b)];
                        }
                    }
                }
            }
            else {
                //ywzx
                if(checkChars(me.C,'z')){
                    for(int b=0;b<3;b++) {
                        for(int a=0;a<3;a++) {
                            hyper[d][a][c][b] = board[reverseAxis(me.A,a)][reverseAxis(me.B,b)];
                        }
                    }
                }
                //ywxz
                else {
                    for(int b=0;b<3;b++) {
                        for(int a=0;a<3;a++) {
                            hyper[c][a][d][b] = board[reverseAxis(me.A,a)][reverseAxis(me.B,b)];
                        }
                    }
                }
            }
        }
        else if(checkChars(me.A,'z')){
            if(checkChars(me.B,'x')){
                //zxyw
                if(checkChars(me.C,'y')){
                    for(int b=0;b<3;b++) {
                        for(int a=0;a<3;a++) {
                            hyper[b][c][a][d] = board[reverseAxis(me.A,a)][reverseAxis(me.B,b)];
                        }
                    }
                }
                //zxwy
                else {
                    for(int b=0;b<3;b++) {
                        for(int a=0;a<3;a++) {
                            hyper[b][d][a][c] = board[reverseAxis(me.A,a)][reverseAxis(me.B,b)];
                        }
                    }
                }
            }
            else if(checkChars(me.B,'y')){
                //zyxw
                if(checkChars(me.C,'x')){
                    for(int b=0;b<3;b++) {
                        for(int a=0;a<3;a++) {
                            hyper[c][b][a][d] = board[reverseAxis(me.A,a)][reverseAxis(me.B,b)];
                        }
                    }
                }
                //zywx
                else {
                    for(int b=0;b<3;b++) {
                        for(int a=0;a<3;a++) {
                            hyper[d][b][a][c] = board[reverseAxis(me.A,a)][reverseAxis(me.B,b)];
                        }
                    }
                }
            }
            else{
                //zwxy
                if(checkChars(me.C,'x')){
                    for(int b=0;b<3;b++) {
                        for(int a=0;a<3;a++) {
                            hyper[c][d][a][b] = board[reverseAxis(me.A,a)][reverseAxis(me.B,b)];
                        }
                    }
                }
                //zwyx
                else {
                    for(int b=0;b<3;b++) {
                        for(int a=0;a<3;a++) {
                            hyper[d][c][a][b] = board[reverseAxis(me.A,a)][reverseAxis(me.B,b)];
                        }
                    }
                }
            }
        }
        else{
            if(checkChars(me.B,'x')){
                //wxzy
                if(checkChars(me.C,'z')){
                    for(int b=0;b<3;b++) {
                        for(int a=0;a<3;a++) {
                            hyper[b][d][c][a] = board[reverseAxis(me.A,a)][reverseAxis(me.B,b)];
                        }
                    }
                }
                //wxyz
                else {
                    for(int b=0;b<3;b++) {
                        for(int a=0;a<3;a++) {
                            hyper[b][c][d][a] = board[reverseAxis(me.A,a)][reverseAxis(me.B,b)];
                        }
                    }
                }
            }
            else if(checkChars(me.B,'y')){
                //wyzx
                if(checkChars(me.C,'z')){
                    for(int b=0;b<3;b++) {
                        for(int a=0;a<3;a++) {
                            hyper[d][b][c][a] = board[reverseAxis(me.A,a)][reverseAxis(me.B,b)];
                        }
                    }
                }
                //wyxz
                else {
                    for(int b=0;b<3;b++) {
                        for(int a=0;a<3;a++) {
                            hyper[c][b][d][a] = board[reverseAxis(me.A,a)][reverseAxis(me.B,b)];
                        }
                    }
                }
            }
            else{
                //wzxy
                if(checkChars(me.C,'x')){
                    for(int b=0;b<3;b++) {
                        for(int a=0;a<3;a++) {
                            hyper[c][d][b][a] = board[reverseAxis(me.A,a)][reverseAxis(me.B,b)];
                        }
                    }
                }
                //wzyx
                else {
                    for(int b=0;b<3;b++) {
                        for(int a=0;a<3;a++) {
                            hyper[d][c][b][a] = board[reverseAxis(me.A,a)][reverseAxis(me.B,b)];
                        }
                    }
                }
            }
        }
    }
    public void setState( char ch) {
        state = ch;
    }
    public char getState() {
        return state;
    }

    public void paintBoard(Graphics2D g) {
        if(isWon(state)){
            if(state=='x'){
                g.setColor(Color.red);
            } else {
                g.setColor(Color.blue);
            }
            g.setStroke(new BasicStroke(5));
            g.drawLine(winX1*160+420,winY1*160+130,winX2*160+420,winY2*160+130);
            g.setStroke(new BasicStroke(1));
        } else {
            g.setColor(Color.white);
        }
        g.drawRect(340, 50, 480, 480);
        g.drawRect(340, 210, 480, 160);
        g.drawRect(500, 50, 160, 480);
        for(int x=0;x<3;x++){
            for(int y=0;y<3;y++){
                //System.out.print(board[x][y]);
                if(isWon(board[x][y])){
                    g.setFont(new Font("Courier",Font.PLAIN,80));
                    if(board[x][y]=='x'){
                        g.setColor(Color.red);
                    } else {
                        g.setColor(Color.blue);
                    }
                    g.drawString("" + board[x][y],(x*160+393),(y*160+153));
                }
            }
        }
        g.setColor(Color.white);
        g.setFont(new Font("Courier",Font.PLAIN,15));

    }
    public void paintInfo(Graphics2D g) {
        g.setColor(Color.white);
        g.drawLine(340,580,340+480,580);
        g.setFont(new Font("Courier",Font.PLAIN,25));
        g.drawString("Board ",340, 580-15);
        g.setColor(Color.yellow);
        g.drawString("      " + me.toString(),340, 580-15);
        g.setColor(Color.white);
        g.drawString("State ",660, 580-15);
        if(isWon(state)){
            if(state=='x'){
                g.setColor(Color.red);
            } else {
                g.setColor(Color.blue);
            }
        }
        g.drawString("      " + Character.toUpperCase(state),660, 580-15);
        g.setColor(Color.white);
        g.drawString(" Player ",500, 580+25);
        if(playr=='x') {
            g.setColor(Color.red);
        } else {
            g.setColor(Color.blue);
        }
        g.drawString("        " + Character.toUpperCase(playr),500, 580+25);
        g.setFont(new Font("Courier",Font.PLAIN,15));
        g.setColor(Color.white);
    }
    public void updateComponent() {

    }
    public void mouseClicked(MouseEvent e){
        mosX=e.getX();
        mosY=e.getY();
        if(!((mosX>820)||(mosX<340)||(mosY>530)||(mosY<50))) {
            boardX = (mosX - 340) / 160;
            if (boardX > 2) {
                boardX = 2;
            }
            if (boardX < 0) {
                boardX = 0;
            }
            boardY = (mosY - 50) / 160;
            if (boardY > 2) {
                boardY = 2;
            }
            if (boardY < 0) {
                boardY = 0;
            }
            System.out.println("[Board] Clicked " + boardX + " " + boardY);
        }
    }
}
