import javax.swing.*;
import java.awt.*;

public class ProgramInterface {
    JFrame jFrame;
    private final ProgramLogic programLogic;
    private int width = 447;
    private int height = 794;
    private int cubeWall = 35;
    public Color wallsColor = Color.DARK_GRAY;
    public Color emptyColor = Color.BLACK;
    public Color[][] matrix;


    public ProgramInterface (ProgramLogic programLogic) throws InterruptedException {
        this.programLogic = programLogic;
        programLogic.setProgramInterface(this);
        setUp();
    }

    private class MyComponent extends JComponent {
        @Override
        public void paint(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(Color.BLACK);
            g2.fillRect(0, 0, width, height);
            for (int i = 0; i < 12; i++) {
                for (int j = 0; j < 21; j++) {
                    g2.setColor(matrix[i][j]);
                    g2.fillRect(i * (cubeWall + 1), j * (cubeWall + 1), cubeWall, cubeWall);
                }
            }

            if (!programLogic.gameOver) {

                g2.setColor(new Color(250, 250, 250));
                int y = programLogic.solve? 0 : programLogic.futurePosition();
                for (int k = 0; k < 4; k++) {
                    Point point = Shapes.shapes[programLogic.currentNumber][programLogic.currentRotation][k];
                    g2.fillRect((programLogic.shift.x + point.x) * (cubeWall + 1),
                            (point.y + programLogic.shift.y + y) * (cubeWall + 1), cubeWall, cubeWall);
                }

                g2.setColor(Shapes.shapesColors[programLogic.currentNumber]);
                for (int k = 0; k < 4; k++) {
                    Point point = Shapes.shapes[programLogic.currentNumber][programLogic.currentRotation][k];
                    g2.fillRect((programLogic.shift.x + point.x ) * (cubeWall + 1),
                            (programLogic.shift.y + point.y) * (cubeWall + 1), cubeWall, cubeWall);

                }
            }
        }
    }

    void repaint() {
        jFrame.repaint();
    }

    void gameStart() throws InterruptedException {
        matrix = programLogic.matrix;
        jFrame = new JFrame("Tetris");
        jFrame.toFront();
        jFrame.setVisible(true);
        jFrame.setResizable(false);
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jFrame.setBounds(10, 10, width, height);
        jFrame.add(new MyComponent());
        programLogic.logicStart();

    }

    private void setUp() throws InterruptedException {
        jFrame = new JFrame("Tetris menu");
        jFrame.toFront();
        jFrame.setVisible(true);
        jFrame.setResizable(false);
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jFrame.setBounds(10, 10, width, height);
        jFrame.getContentPane().setBackground(Color.white);

        JPanel buttons = new JPanel();
        buttons.setVisible(true);
        buttons.setLocation(0, height / 2 - 100);

        Button start = new Button("start");
        start.setBounds(width / 3, 0, width / 3, 30);
        start.setBackground(Color.CYAN);
        start.addActionListener(e -> {
            jFrame.dispose();
            try {
                gameStart();
            } catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            }
        });

        Button help = new Button("help");
        help.setBounds(width / 3, height / 7, width / 3, 30);
        help.setBackground(Color.CYAN);
        help.addActionListener(e -> helpCall());


        buttons.add(help);
        buttons.add(start);

        Thread.sleep(50);
        jFrame.add(buttons);
    }

    private void helpCall() {
        JFrame jFrame = new JFrame("Help");

        jFrame.toFront();
        jFrame.setVisible(true);
        jFrame.setResizable(false);
        jFrame.setBounds(20, 40, 225, 300);
        jFrame.getContentPane().setBackground(Color.white);

        JPanel labels = new JPanel();
        labels.setLocation(0, 0);
        Label welcomeLabel = new Label("Welcome! This is the Tetris!");

        Label keyLabel = new Label("Key list:");
        Label upLabel = new Label("up arrow: rotate object to the left");
        Label downLabel = new Label("down arrow: rotate object to the right");
        Label leftLabel = new Label("left arrow: move object to the left");
        Label rightLabel = new Label("right arrow: move object to the right");
        Label spaceLabel = new Label("space: fast-forward object");
        Label rLabel = new Label("R: restart game");
        Label fLabel = new Label("F: start/stop auto-solver");

        labels.add(welcomeLabel);
        labels.add(keyLabel);
        labels.add(upLabel);
        labels.add(downLabel);
        labels.add(leftLabel);
        labels.add(rightLabel);
        labels.add(spaceLabel);
        labels.add(rLabel);
        labels.add(fLabel);

        jFrame.add(labels);
    }
}
