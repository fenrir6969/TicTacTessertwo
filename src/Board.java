import java.awt.*;
import java.awt.event.MouseEvent;

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
    int load;
    int maxLoad = 40;

    public Board() {
        hyper = new char[3][3][3][3];
        board = new char[3][3];
        state = '-';
        playr = 'x';
        me = new Faces();
    }

    private boolean isWon(char state) {
        return (state == 'x') || (state == 'o');
    }

    private boolean conflicting(char state1, char state2) {
        if (isWon(state1) && isWon(state2)) {
            if (state1 != state2) {
                return true;
            }
        }
        return false;
    }

    private int reverseAxis(char ch, int val) {
        int[] forwards = {0, 1, 2};
        int[] backwards = {2, 1, 0};
        if (Character.isUpperCase(ch)) {
            return forwards[val];
        } else {
            return backwards[val];
        }
    }

    public char[][] loadGeneric(Faces face) {
        char[][] raw = new char[3][3];
        int[] address = new int[4];
        int d = face.value(face.D);
        int c = face.value(face.C);
        for (int b = 0; b < 3; b++) {
            for (int a = 0; a < 3; a++) {
                address[face.axisIndex(face.A)] = a;
                address[face.axisIndex(face.B)] = b;
                address[face.axisIndex(face.C)] = c;
                address[face.axisIndex(face.D)] = d;
                raw[reverseAxis(face.A, a)][reverseAxis(face.B, b)] =
                        hyper[address[0]][address[1]][address[2]][address[3]];
            }
        }
        return raw;
    }

    public void load(Faces face) {
        board = loadGeneric(face);
        System.out.println("[Board] Loading " + face + "...");
        for (int i = 0; i < 3; i++) {
            System.out.print("[Board]   ");
            for (int j = 0; j < 3; j++) {
                System.out.print(" " + board[j][i]);
            }
            System.out.println("");
        }
        System.out.println("[Board] State " + state);
        me = face;
        load = maxLoad;
        check();
    }

    public void save() {
        System.out.println("[Board] Saving " + me + "...");
        for (int i = 0; i < 3; i++) {
            System.out.print("[Board]   ");
            for (int j = 0; j < 3; j++) {
                System.out.print(" " + board[j][i]);
            }
            System.out.println("");
        }
        int[] address = new int[4];
        int d = me.value(me.D);
        int c = me.value(me.C);
        for (int b = 0; b < 3; b++) {
            for (int a = 0; a < 3; a++) {
                address[me.axisIndex(me.A)] = a;
                address[me.axisIndex(me.B)] = b;
                address[me.axisIndex(me.C)] = c;
                address[me.axisIndex(me.D)] = d;
                hyper[address[0]][address[1]][address[2]][address[3]] = board[reverseAxis(me.A, a)][reverseAxis(me.B, b)];
            }
        }
    }

    public char checkGeneric(char[][] raw) {
        char win = '-';
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
            System.out.println("[Board] Board won by " + state);
        } else {
            // nobody won
            if (checkStalemate(raw)) {
                System.out.println("[Board] Board in stalemate");
                win = 's';
            }
            System.out.println("[Board] Board not won");
        }
        return win;
    }

    private void check() {
        System.out.println("[Board] Checking " + me + "...");
        if (!isWon(state)) {
            state = checkGeneric(board);
        } else {
            System.out.println("[Board] Board already won by " + state);
        }
    }

    private boolean checkStalemate(char[][] raw) {
        //Diagonal 1
        if (!(conflicting(raw[0][0], raw[1][1]) || conflicting(raw[2][2], raw[1][1]) || conflicting(raw[0][0], raw[2][2]))) {
            return false;
        }
        //Diagonal 2
        if (!(conflicting(raw[0][2], raw[1][1]) || conflicting(raw[2][0], raw[1][1]) || conflicting(raw[2][0], raw[0][2]))) {
            return false;
        }
        for (int a = 0; a < 3; a++) {
            //Vertical
            if (!(conflicting(raw[a][0], raw[a][1]) || conflicting(raw[a][2], raw[a][1]) || conflicting(raw[a][0], raw[a][2]))) {
                return false;
            }
            //Horizontal
            if (!(conflicting(raw[0][a], raw[1][a]) || conflicting(raw[2][a], raw[1][a]) || conflicting(raw[0][a], raw[2][a]))) {
                return false;
            }
        }
        return true;
    }

    public void setState(char ch) {
        state = ch;
    }

    public char getState() {
        return state;
    }

    public void paintBackground(Graphics2D g) {
        g.setFont(new Font("Courier", Font.PLAIN, 40));
        if (playr == 'x') {
            g.setColor(new Color(15, 0, 5));
        } else {
            g.setColor(new Color(5, 0, 15));
        }
        for (int i = 1; i < 23; i++) {
            g.drawString("XOXOXOXOXOXOXOXOXOXOXOXOXOXOXOXOXOXOXOXOXOXOXOXOXOXOXOXOXOXOXOXOXOXOXOXOXOXOXOXO", 0, i * 30);
            i++;
            g.drawString("OXOXOXOXOXOXOXOXOXOXOXOXOXOXOXOXOXOXOXOXOXOXOXOXOXOXOXOXOXOXOXOXOXOXOXOXOXOXOXOX", 0, i * 30);
        }
    }

    public void paintWin(Graphics2D g) {
        int winX1 = 0;
        int winX2 = 0;
        int winY1 = 0;
        int winY2 = 0;
        //check diagonal 1
        if (isWon(board[0][0])) {
            if (board[0][0] == board[1][1]) {
                if (board[0][0] == board[2][2]) {
                    if (board[0][0] == state) {
                        winX1 = 0;
                        winX2 = 2;
                        winY1 = 0;
                        winY2 = 2;
                    }
                }
            }
        }
        //check diagonal 2
        if (isWon(board[0][2])) {
            if (board[0][2] == board[1][1]) {
                if (board[0][2] == board[2][0]) {
                    if (board[0][2] == state) {
                        winX1 = 0;
                        winX2 = 2;
                        winY1 = 2;
                        winY2 = 0;
                    }
                }
            }
        }
        for (int a = 0; a < 3; a++) {
            //check vertical
            if (isWon(board[a][0])) {
                if (board[a][0] == board[a][1]) {
                    if (board[a][0] == board[a][2]) {
                        if (board[a][0] == state) {
                            winX1 = a;
                            winX2 = a;
                            winY1 = 0;
                            winY2 = 2;
                            break;
                        }
                    }
                }
            }
            //check horizontal
            if (isWon(board[0][a])) {
                if (board[0][a] == board[1][a]) {
                    if (board[0][a] == board[2][a]) {
                        if (board[0][a] == state) {
                            winX1 = 0;
                            winX2 = 2;
                            winY1 = a;
                            winY2 = a;
                            break;
                        }
                    }
                }
            }
        }
        g.setStroke(new BasicStroke(5));
        g.drawLine(winX1 * 160 + 420, winY1 * 160 + 130, winX2 * 160 + 420, winY2 * 160 + 130);
        g.setStroke(new BasicStroke(1));
    }

    public void paintBoard(Graphics2D g) {
        g.setColor(Color.black);
        g.fillRect(340, 50, 480, 480);
        if (load < maxLoad / 2) {
            Color winState;
            if (isWon(state)) {
                if (state == 'x') {
                    winState = Color.red;
                } else {
                    winState = Color.blue;
                }
                g.setColor(winState);
                paintWin(g);
            } else {
                if (state == 's') {
                    winState = Color.darkGray;
                } else {
                    winState = Color.white;
                }
                g.setColor(winState);
            }
            g.drawRect(340, 50, 480, 480);
            g.drawRect(340, 210, 480, 160);
            g.drawRect(500, 50, 160, 480);
            for (int x = 0; x < 3; x++) {
                for (int y = 0; y < 3; y++) {
                    //System.out.print(board[x][y]);
                    if (isWon(board[x][y])) {
                        g.setFont(new Font("Courier", Font.PLAIN, 80));
                        if (board[x][y] == 'x') {
                            g.setColor(Color.red);
                        } else {
                            g.setColor(Color.blue);
                        }
                        if (state == 's') {
                            g.setColor(Color.darkGray);
                        }
                        g.drawString("" + board[x][y], (x * 160 + 393), (y * 160 + 153));
                    }

                    g.setColor(winState);
                    g.setFont(new Font("Courier", Font.PLAIN, 15));
                    g.drawString("" + reverseAxis(me.A, x) + ", " + reverseAxis(me.B, y), (x * 160 + 345), (y * 160 + 70));
                }
            }
            g.setColor(Color.white);
            g.setFont(new Font("Courier", Font.PLAIN, 15));
        }
    }

    public void paintInfo(Graphics2D g) {
        g.setColor(Color.white);
        g.drawLine(340, 580, 340 + 480, 580);
        g.setFont(new Font("Courier", Font.PLAIN, 25));
        g.drawString("Board ", 340, 580 - 15);
        if (load < maxLoad / 2) {
            g.setColor(Color.yellow);
        } else {
            g.setColor(Color.black);
        }
        g.drawString("      " + me.toString(), 340, 580 - 15);
        g.setColor(Color.white);
        g.drawString("State ", 660, 580 - 15);
        if (isWon(state)) {
            if (state == 'x') {
                g.setColor(Color.red);
            } else {
                g.setColor(Color.blue);
            }
        } else {
            if (state == 's') {
                g.setColor(Color.darkGray);
            }
        }
        if (!(load < maxLoad / 2)) {
            g.setColor(Color.black);
        }
        g.drawString("      " + Character.toUpperCase(state), 660, 580 - 15);
        g.setColor(Color.white);
        g.drawString(" Player ", 500, 580 + 25);
        if (playr == 'x') {
            g.setColor(Color.red);
        } else {
            g.setColor(Color.blue);
        }
        g.drawString("        " + Character.toUpperCase(playr), 500, 580 + 25);
        g.setFont(new Font("Courier", Font.PLAIN, 15));
        g.setColor(Color.white);
    }

    public void updateComponent() {
        if (load > 0) {
            load--;
        }
    }

    public boolean mouseClicked(MouseEvent e) {
        if (!isWon(state)) {
            mosX = e.getX();
            mosY = e.getY();
            Rectangle bounds = new Rectangle(340, 50, 480, 480);
            if (bounds.contains(mosX, mosY)) {
                System.out.println();
                boardX = (mosX - 340) / 160;
                boardY = (mosY - 50) / 160;
                System.out.println("Playing...");
                System.out.println("[Board] Checking " + boardX + ", " + boardY + "...");
                if (!isWon(board[boardX][boardY])) {
                    board[boardX][boardY] = playr;
                    System.out.println("[Board] Playing" + Character.toUpperCase(playr));
                    save();
                    check();
                    if (playr == 'x') {
                        playr = 'o';
                    } else {
                        playr = 'x';
                    }
                    return true;
                } else {
                    System.out.println("[Board] Position occupied");
                }
            }
        }
        return false;
    }
}