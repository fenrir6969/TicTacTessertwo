import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
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

    public Game() {
        setBackground(Color.black);
        faces.initializePolygons();
        faces.define('X','Y','Z','W');
        board.randomizer(3);
        state.randomizer(3);
        board.load(faces);
        addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {
                state.mouseClicked(e);
                board.mouseClicked(e);
                Faces random = faces.mouseClicked(e);
                board.load(random);
                faces.set(random);
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
            }

            @Override
            public void mouseDragged(MouseEvent e) {

            }
        });
    }

    private void changeFace() {
        board.save();
        board.load(faces);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.setColor(Color.white);
        g.setFont(new Font("Courier",Font.PLAIN,15));
        Graphics2D g2d = (Graphics2D) g;
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
    }

    private void updateComponent() {
        board.updateComponent();
        faces.updateComponent();
        state.updateComponent();
        ball1.updateComponent();
        ball2.updateComponent();
        ball3.updateComponent();
    }

    public static void main(String[] args) throws InterruptedException{
        JFrame window = new JFrame("Tic Tac Tesseract");
        Game game = new Game();
        window.add(game);
        window.setSize( new Dimension(1160, 670));
        window.setVisible(true);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        while(true) {
            game.updateComponent();
            game.repaint();
            Thread.sleep(10);
        }
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
