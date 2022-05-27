import javax.swing.*;
import java.awt.*;

public class ProgramInterface {
    JFrame jFrame;
    private final ProgramLogic programLogic;
    private int width = 447;
    private int height = 791;
    private int cubeWall = 35;
    public Color wallsColor = Color.DARK_GRAY;
    public Color emptyColor = Color.BLACK;
    public Color[][] matrix;
    public int iBorder;
    public int jBorder;
    public int rotationBorder;


    public ProgramInterface (ProgramLogic programLogic) throws InterruptedException {
        this.programLogic = programLogic;
        programLogic.setProgramInterface(this);
        this.iBorder = programLogic.iBorder;
        this.jBorder = programLogic.jBorder;
        this.rotationBorder = programLogic.rotationBorder;
        setUp();
    }

    private class MyComponent extends JComponent {
        @Override
        public void paint(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(Color.BLACK);
            g2.fillRect(0, 0, width, height);
            for (int i = 0; i < iBorder; i++) {
                for (int j = 0; j < jBorder; j++) {
                    g2.setColor(programLogic.matrix[i][j]);
                    g2.fillRect(i * (cubeWall + 1), j * (cubeWall + 1), cubeWall, cubeWall);
                }
            }

            if (!programLogic.getGameOver()) {
                g2.setColor(new Color(250, 250, 250));
                int y = programLogic.getSolve() ? 0 : programLogic.futurePosition(programLogic.matrix, programLogic.currentShape);
                for (Point point : programLogic.currentShape.getShapeCoordinates()[programLogic.currentShape.getCurrentRotation()]) {
                    g2.fillRect((programLogic.currentShape.getShift().x + point.x) * (cubeWall + 1),
                            (point.y + programLogic.currentShape.getShift().y + y) * (cubeWall + 1), cubeWall, cubeWall);
                }

                g2.setColor(programLogic.currentShape.getCurrentColor());
                for (Point point : programLogic.currentShape.getShapeCoordinates()[programLogic.currentShape.getCurrentRotation()]) {
                    g2.fillRect((programLogic.currentShape.getShift().x + point.x) * (cubeWall + 1),
                            (point.y + programLogic.currentShape.getShift().y) * (cubeWall + 1), cubeWall, cubeWall);
                }
            }
            g2.setColor(Color.WHITE);
            g2.drawString("Cleared lines = " + programLogic.clearedLines, 40, 10);
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
        Label upLabel = new Label("W: rotate object to the left");
        Label downLabel = new Label("S: rotate object to the right");
        Label leftLabel = new Label("A: move object to the left");
        Label rightLabel = new Label("D: move object to the right");
        Label spaceLabel = new Label("Space: fast-forward object");
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
