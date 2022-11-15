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
    Faces faces = new Faces();
    boolean stop = false;
    boolean end = false;

    public Game() {
        setBackground(Color.black);
        faces.define('X','Y','Z','W');
        board.load(faces);
        board.setState(state.get(faces));
        addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if(!end) {
                    if (faces.mouseClicked(e)) {
                        board.load(faces);
                        board.setState(state.get(faces));
                        board.check();
                    }
                    if (board.mouseClicked(e)) {
                        state.set(faces, board.getState());
                        checkAdjacentSides();
                        state.checkGame();
                        state.reloadCubeNet();
                    }
                    state.mouseClicked(e);
                } else {
                    Rectangle boundsN = new Rectangle(772,548,20,20);
                    Rectangle boundsY = new Rectangle(720,548,20,20);
                    if(boundsN.contains(e.getX(),e.getY())){
                        stop=true;
                    }
                    if(boundsY.contains(e.getX(),e.getY())){
                        restart();
                    }
                }
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
            ball1.paint(g);
            g.setColor(Color.red);
            ball2.paint(g);
            g.setColor(Color.blue);
            ball3.paint(g);
            g.setColor(Color.white);
            board.paintBoard(g2d);
            board.paintInfo(g2d);
            state.paintHyper(g2d);
            state.paintCube(g2d);
            faces.paintAtlas(g2d);
            faces.paintCompass(g2d);
        } else {
            ball1.paint(g);
            g.setColor(Color.red);
            ball2.paint(g);
            g.setColor(Color.blue);
            ball3.paint(g);
            if(state.hyper=='x'){
                g2d.setColor(Color.red);
            } else {
                g2d.setColor(Color.blue);
            }
            g2d.setFont(new Font("Courier", Font.PLAIN, 80));
            g2d.drawString("Game Over", 360,240);
            g2d.setFont(new Font("Courier", Font.PLAIN, 21));
            g2d.drawString("                                " + Character.toUpperCase(state.hyper), 360,270);
            g2d.setColor(Color.white);
            g2d.drawString("Tic Tac Tesseract won by Player ", 360,270);
            state.paintEnd(g2d);
            g2d.drawString("Play Again?                   /  ", 360,565);
            g2d.setColor(Color.red);
            g2d.drawString("                            Y    ", 360,565);
            g2d.setColor(Color.blue);
            g2d.drawString("                                N", 360,565);
        }
    }
    public boolean isWon() {
        return (state.hyper=='x'||state.hyper=='o');
    }
    private void checkSide(Faces side) {
        if (!state.check(side)){
            state.set(side,board.checkGeneric(board.loadGeneric(side)));
        }
    }
    private void checkAdjacentSides() {
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
        if(isWon()){
            if(!end){
                state.endGamePolygons();
                end=true;
            }
        }
    }
    private void restart() {
        state = new State();
        board = new Board();
        faces= new Faces();
        faces.define('X','Y','Z','W');
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
