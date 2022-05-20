import java.awt.*;
import java.util.ArrayDeque;
import java.util.Queue;

public class ProgramLogic {

    private boolean gameOn = false;
    private boolean gameOver = false;
    private ProgramInterface programInterface;
    public Color[][] matrix = new Color[12][21];
    public Queue<Integer> allShapes = new ArrayDeque<>();
    public int amountOfShapes = 10;

    public int currentNumber;
    public int currentRotation;
    public Point shift;

    public MyKeyboardListener myKeyboardListener;

    public void setMyKeyboardListener(MyKeyboardListener myKeyboardListener) {
        this.myKeyboardListener = myKeyboardListener;
    }

    public void clearFullRows() {
        for (int k = 0; k < 4; k++) {
            for (int j = 19; j > 0; j--) {
                boolean flag = true;
                for (int i = 1; i < 11; i++) {
                    if (matrix[i][j] == programInterface.emptyColor) {
                        flag = false;
                        break;
                    }
                }
                if (flag) {
                    for (int i = j; i > 0; i--) {
                        for (int l = 1; l < 11; l++) {
                            matrix[l][i] = matrix[l][i - 1];
                        }
                    }

                }
            }
        }
    }

    public void shapeMove(int k) {
        for (int i = 0; i < 4; i++) {
            Point point = Shapes.shapes[currentNumber][currentRotation][i];
            if (matrix[point.x + shift.x + k][point.y + shift.y] != programInterface.emptyColor) {
                return;
            }
        }
        shift.x += k;
        programInterface.repaint();
    }

    public void upShapeRotate() {
        if (currentRotation < 3) currentRotation++;
        else currentRotation = 0;
        for (int i = 0; i < 4; i++) {
            if (matrix[Shapes.shapes[currentNumber][currentRotation][i].x + shift.x]
                    [Shapes.shapes[currentNumber][currentRotation][i].y + shift.y] != programInterface.emptyColor) {
                currentRotation--;
                return;
            }
        }
        programInterface.repaint();
    }

    public void downShapeRotate() {
        if (currentRotation > 0) currentRotation--;
        else currentRotation = 3;
        for (int i = 0; i < 4; i++) {
            if (matrix[Shapes.shapes[currentNumber][currentRotation][i].x + shift.x]
                    [Shapes.shapes[currentNumber][currentRotation][i].y + shift.y] != programInterface.emptyColor) {
                currentRotation++;
                return;
            }
        }
        programInterface.repaint();
    }


    //Method to assign jFrame
    public void setProgramInterface(ProgramInterface programInterface) {
        this.programInterface = programInterface;
    }

    //Run of the process
    private void launchApplication() {
        Runnable r = ()->{
            try {
                while (gameOn) {
                    if (!gameOver) {
                        oneTick();
                        programInterface.repaint();
                        Thread.sleep(1000);
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };

        Thread myThread = new Thread(r,"MyThread");
        myThread.start();
    }

    public void generateQueue() {
        allShapes = new ArrayDeque<>();
        for (int i = 0; i < amountOfShapes; i++) {
            int nextShape = (int) (Math.random() * 7);
            allShapes.add(nextShape);
        }
    }

    public void setGameOver(boolean value) {
        gameOver = value;
    }

    public boolean isGameOver() {
        for (int k = 0; k < 4; k++) {
                Point point = Shapes.shapes[currentNumber][currentRotation][k];
                if (matrix[point.x + shift.x][point.y + shift.y] != programInterface.emptyColor)
                    return true;
        }
        return false;
    }

    public void newShape() {
        currentNumber = allShapes.peek();
        allShapes.remove();
        int numberOfNewShape = (int) (Math.random() * 7);
        allShapes.add(numberOfNewShape);
        currentRotation = (int) (Math.random() * 4);
        shift = new Point(5, 0);
        setGameOver(isGameOver());
        clearFullRows();
        programInterface.repaint();
    }

    public void oneTick() {
        for (int i = 0; i < 4; i++) {
            if (!matrix[Shapes.shapes[currentNumber][currentRotation][i].x + shift.x][Shapes.shapes[currentNumber]
                    [currentRotation][i].y + shift.y + 1]
                    .equals(programInterface.emptyColor)) {
                stopShape();
                return;
            }
        }
        shift.y++;
        programInterface.repaint();
    }

    public void stopShape() {
        for (int i = 0; i < 4; i++) {
            matrix[Shapes.shapes[currentNumber][currentRotation][i].x + shift.x]
                    [Shapes.shapes[currentNumber][currentRotation][i].y + shift.y]
                    = Shapes.shapesColors[currentNumber];
        }
        newShape();
    }

    public void setUpNewGame() {
        gameOn = true;
        setGameOver(false);
        generateQueue();
        newShape();
        for (int i = 0; i < 12; i++) {
            for (int j = 0; j < 21; j++) {
                if (i == 0 || i == 11 || j == 20) matrix[i][j] = programInterface.wallsColor;
                else matrix[i][j] = programInterface.emptyColor;
            }
        }
        programInterface.repaint();
    }

    /*Package private*/ void logicStart() {
        myKeyboardListener = new MyKeyboardListener(this);
        programInterface.jFrame.addKeyListener(myKeyboardListener);
        setUpNewGame();
        launchApplication();
    }

}

