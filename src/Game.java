import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class Game extends JPanel implements MouseListener, MouseMotionListener {
    Board board = new Board();
    State state = new State();
    Ball ball1 = new Ball();
    Ball ball2 = new Ball();
    Ball ball3 = new Ball();
    Ball ball4 = new Ball();
    Faces faces = new Faces();
    boolean stop = false;
    boolean end = false;

    public Game() {
        setBackground(Color.black);
        System.out.println();
        System.out.println("Starting Game...");
        faces.define('X','Y','z','w');
        board.load(faces);
        board.setState(state.get(faces));
        addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if(!end) {
                    if (faces.mouseClicked(e)) {
                        board.setState(state.get(faces));
                        board.load(faces);
                    }
                    if (board.mouseClicked(e)) {
                        state.set(faces, board.getState());
                        checkAdjacentSides();
                        state.checkGame();
                        System.out.println("[Gayme] Reloading Cube Net...");
                        state.reloadCubeNet(false);
                        System.out.println();
                        board.load(faces);
                    }
                    state.mouseClicked(e);
                } else {
                    /*
                    g2d.drawString("                            Y    ", 360,525);
                    g2d.setColor(Color.blue);
                    g2d.drawString("                                N", 360,525);
                    */
                    Rectangle boundsN = new Rectangle(772,505,20,20);
                    Rectangle boundsY = new Rectangle(720,505,20,20);
                    if(boundsN.contains(e.getX(),e.getY())){
                        stop=true;
                    }
                    if(boundsY.contains(e.getX(),e.getY())){
                        restart();
                    }
                }
                ball1.mouseClicked(e);
                ball2.mouseClicked(e);
                ball3.mouseClicked(e);
                ball4.mouseClicked(e);
            }
            @Override
            public void mousePressed(MouseEvent e) {

            }
            public void mouseReleased(MouseEvent e) {

            }
            public void mouseEntered(MouseEvent e) {

            }
            public void mouseExited(MouseEvent e) {

            }
        });
        addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseMoved(MouseEvent e) {
                if(!end) {
                    faces.mouseMoved(e);
                }
            }
            @Override
            public void mouseDragged(MouseEvent e) {

            }
        });
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.setColor(Color.white);
        g.setFont(new Font("Courier", Font.PLAIN, 15));
        Graphics2D g2d = (Graphics2D) g;
        if(!end) {
            board.paintBackground(g2d);
            /*
            g.setColor(Color.red);
            ball2.paint(g);
            g.setColor(Color.blue);
            ball3.paint(g);
            g.setColor(Color.darkGray);
            ball4.paint(g);
            g.setColor(Color.white);
            ball1.paint(g);

             */
            board.paintBoard(g2d);
            board.paintInfo(g2d);
            state.paintHyper(g2d);
            state.paintCube(g2d);
            faces.paintAtlas(g2d);
            faces.paintCompass(g2d);
        } else {
            g.setFont(new Font("Courier", Font.PLAIN, 40));
            if(state.hyper=='x') {
                g.setColor(new Color(12, 1, 5));
            } else if(state.hyper=='o'){
                g.setColor(new Color(5, 1, 12));
            } else {
                g.setColor(new Color(6, 6, 6));
            }
            for(int i=1;i<23;i++) {
                g.drawString("XOXOXOXOXOXOXOXOXOXOXOXOXOXOXOXOXOXOXOXOXOXOXOXOXOXOXOXOXOXOXOXOXOXOXOXOXOXOXOXO", 0, i*30);
                i++;
                g.drawString("OXOXOXOXOXOXOXOXOXOXOXOXOXOXOXOXOXOXOXOXOXOXOXOXOXOXOXOXOXOXOXOXOXOXOXOXOXOXOXOX", 0, i*30);
            }
            g.setColor(Color.white);
            ball1.paint(g);
            g.setColor(Color.red);
            ball2.paint(g);
            g.setColor(Color.blue);
            ball3.paint(g);
            g.setColor(Color.darkGray);
            ball4.paint(g);
            if(state.hyper=='x'){
                g2d.setColor(Color.red);
            } else if(state.hyper=='o'){
                g2d.setColor(Color.blue);
            } else {
                g2d.setColor(Color.darkGray);
            }
            g2d.setFont(new Font("Courier", Font.PLAIN, 81));
            g2d.drawString("Game Over", 359,170);
            g2d.setFont(new Font("Courier", Font.PLAIN, 21));
            if(state.hyper!='s') {
                g2d.drawString("                                " + Character.toUpperCase(state.hyper), 360, 200);
                g2d.setColor(Color.white);
                g2d.drawString("Tic Tac Tesseract won by Player ", 360, 200);
            } else {
                g2d.drawString("                         Stalemate", 360, 200);
                g2d.setColor(Color.white);
                g2d.drawString("Tic Tac Tesseract end by ", 360, 200);
            }
            state.paintEndScreen(g2d);
            Rectangle boundsN = new Rectangle(772,505,20,20);
            Rectangle boundsY = new Rectangle(720,505,20,20);
            g2d.setFont(new Font("Courier", Font.PLAIN, 21));
            g2d.drawString("Play Again?                   /  ", 360,525);
            g2d.setColor(Color.red);
            g2d.drawString("                            Y    ", 360,525);
            //g2d.draw(boundsY);
            g2d.setColor(Color.blue);
            g2d.drawString("                                N", 360,525);
            //g2d.draw(boundsN);
        }
    }
    public boolean isWon() {
        return (state.hyper=='x'||state.hyper=='o'||state.hyper=='s');
    }
    private void checkSide(Faces side) {
        System.out.println("[Gayme] Checking " + side + "...");
        if (!state.check(side)){
            state.set(side,board.checkGeneric(board.loadGeneric(side)));
        }
    }
    private void checkAdjacentSides() {
        System.out.println();
        System.out.println("Checking adjacent Boards...");
        checkSide(faces.rotateAC(true));
        checkSide(faces.rotateAD(true));
        checkSide(faces.rotateAC(false));
        checkSide(faces.rotateAD(false));
        checkSide(faces.rotateBC(true));
        checkSide(faces.rotateBD(true));
        checkSide(faces.rotateBC(false));
        checkSide(faces.rotateBD(false));
    }
    private void updateComponent() {
        board.updateComponent();
        faces.updateComponent();
        state.updateComponent();
        ball1.updateComponent();
        ball2.updateComponent();
        ball3.updateComponent();
        ball4.updateComponent();
        if(isWon()){
            if(!end){
                end=true;
                System.out.println();
                System.out.println("Game Over");

            }
        }
    }
    private void restart() {
        System.out.println();
        System.out.println("Restarting Game...");
        state = new State();
        board = new Board();
        faces= new Faces();
        faces.define('X','Y','z','w');
        board.load(faces);
        board.setState(state.get(faces));
        end=false;
    }

    public static void main(String[] args) throws InterruptedException{
        JFrame window = new JFrame("Tic Tac Tesseract");
        Game game = new Game();
        window.add(game);
            window.setSize( new Dimension(1160, 670));
        window.setVisible(true);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        while(!game.stop) {
            game.updateComponent();
            game.repaint();
            Thread.sleep(10);
        }
        window.dispose();
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }
    public void mousePressed(MouseEvent e) {

    }
    public void mouseReleased(MouseEvent e) {

    }
    public void mouseEntered(MouseEvent e) {

    }
    public void mouseExited(MouseEvent e) {

    }
    public void mouseDragged(MouseEvent e) {

    }
    public void mouseMoved(MouseEvent e) {

    }
}
