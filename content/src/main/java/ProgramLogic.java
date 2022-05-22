import java.awt.*;
import java.util.ArrayDeque;
import java.util.Queue;

public class ProgramLogic {

    private boolean gameOn = true;
    public boolean gameOver = false;
    public boolean pause = false;
    public boolean solve = false;

    public boolean check = false;
    private ProgramInterface programInterface;
    public Color[][] matrix = new Color[12][21];
    public Queue<Integer> allShapes = new ArrayDeque<>();
    public int amountOfShapes = 10;
    public int clearedLines = 0;

    public int currentNumber;
    public int currentRotation;
    public Point shift;

    public MyKeyboardListener myKeyboardListener;

    public void toSolve() throws InterruptedException {

        placeShapeInBestPosition();
    }

    public void placeShapeInBestPosition() throws InterruptedException {
        int x = 0;
        int y = 0;
        double score = 0.0;
        double bestScore = -1e6;
        int bestRotation = 0;
        currentRotation = -1;
        for (int k = 0; k < 4; k++) {
            currentRotation++;
            for (int i = 0; i < 11; i++) {
                shift = new Point(i,0);
                if (isBump(0, 0)) continue;
                y = futurePosition();
                for (Point point1: Shapes.shapes[currentNumber][currentRotation]) {
                    matrix[point1.x + shift.x][point1.y + y] = Shapes.shapesColors[currentNumber];
                }
                //programInterface.repaint();
                //Thread.sleep(200);
                score = checkScore();
                if (score > bestScore) {
                    bestScore = score;
                    bestRotation = k;
                    x = i;
                }
                for (Point point1: Shapes.shapes[currentNumber][currentRotation]) {
                    matrix[point1.x + shift.x][point1.y + y] = programInterface.emptyColor;
                }
            }
        }
        currentRotation = bestRotation;
        shift.x = x;
        //shift.y = futurePosition();
        check = false;
    }

    public double checkScore() {
        return height() + lines() + bumpiness();
    }

    public double height() {
        int holes = 0;
        int height = 0;
        for (int i = 1; i < 11; i++) {
            int height1 = 0;
            for (int j = 0; j < 20; j++) {
                if (matrix[i][j] != programInterface.emptyColor) {
                    height1 = 20 - j;
                    break;
                }
            }
            for (int j = 19; j > 19 - height1; j--) {
                if (matrix[i][j] == programInterface.emptyColor) holes++;
            }
            height += height1;
        }
        //return holes;
        return -0.510066 * height + -0.35663 * holes;
    }

    public double lines() {
        int lines = 0;
        for (int j = 19; j > 0 ; j--) {
            boolean flag = true;
            for (int i = 1; i < 11; i++) {
                if (matrix[i][j] == programInterface.emptyColor) {
                    flag = false;
                    break;
                }
            }
            if (flag) lines++;
            if (lines == 4) break;
        }
        return 0.760666 * lines;
    }

    public double bumpiness() {
        int bumpiness = 0;
        int height1 = 20;
        for (int j = 0; j < 20; j++) {
            if (matrix[1][j] != programInterface.emptyColor) {
                height1 = j;
                break;
            }
        }
        for (int i = 2; i < 11; i++) {
            int height2 = 20;
            for (int j = 0; j < 20; j++) {
                if (matrix[i][j] != programInterface.emptyColor) {
                    height2 = j;
                    break;
                }
            }
            bumpiness += Math.abs(height1 - height2);

            height1 = height2;
        }
        return -0.184483 * bumpiness;
    }


    public boolean isBump(int x, int y) {
        for (int i = 0; i < 4; i++) {
            Point point = Shapes.shapes[currentNumber][currentRotation][i];
            if (matrix[point.x + shift.x + x][point.y + shift.y + y] != programInterface.emptyColor) {
                return true;
            }
        }
        return false;
    }

    public void startSolve() {
        myKeyboardListener.block = !myKeyboardListener.block;
        pause = false;
        solve = true;
    }

    public int futurePosition() {
        int minY = 2000;
        for (int k = 0; k < 4; k++) {
            int y = 0;
            Point point = Shapes.shapes[currentNumber][currentRotation][k];
            for (int j = point.y + shift.y + 1; j < 20; j++) {
                if (matrix[point.x + shift.x][j] == programInterface.emptyColor) {
                    y++;
                } else break;
            }
            if (y < minY) minY = y;
        }
        return minY;
    }

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
                    clearedLines++;
                    for (int i = j; i > 0; i--) {
                        for (int l = 1; l < 11; l++) {
                            matrix[l][i] = matrix[l][i - 1];
                        }
                    }
                    for (int i = 1; i < 11; i++) {
                        matrix[i][0] = programInterface.emptyColor;
                    }
                }
            }
        }
    }

    public void shapeMove(int k) {
        if (!isBump(k, 0)) {
            shift.x += k;
            programInterface.repaint();
        }
    }

    public void upShapeRotate() {
        if (currentRotation < 3) currentRotation++;
        else currentRotation = 0;
        for (int i = 0; i < 4; i++) {
            if (isBump(0, 0)) {
                if (currentRotation > 0) currentRotation--;
                else currentRotation = 3;
                return;
            }
        }
        programInterface.repaint();
    }

    public void downShapeRotate() {
        if (currentRotation > 0) currentRotation--;
        else currentRotation = 3;
        for (int i = 0; i < 4; i++) {
            if (isBump(0, 0)) {
                if (currentRotation < 3) currentRotation++;
                else currentRotation = 0;
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
                    if (!pause && !solve) {
                        Thread.sleep(1000);
                        if (!gameOver) {
                            oneTick();
                        }
                    }
                    if (!pause) {
                        if (solve && !gameOver) {
                            if (check) toSolve();
                            oneTick();
                            Thread.sleep(5);
                        }
                    }
                    programInterface.repaint();
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
        if (gameOver) myKeyboardListener.block = true;
    }

    public boolean isGameOver() {
        for (int k = 0; k < 4; k++) {
                if (isBump(0, 0)) {
                    System.out.println(clearedLines);
                    return true;
                }
        }
        return false;
    }

    public void newShape() {
        check = true;

        currentNumber = allShapes.peek();
        allShapes.remove();
        int numberOfNewShape = (int) (Math.random() * 7);
        allShapes.add(numberOfNewShape);
        currentRotation = (int) (Math.random() * 4);
        shift = new Point(5, 0);
        if (isGameOver()) {
            setGameOver(true);
        } else {
            clearFullRows();
            programInterface.repaint();
        }
    }

    public void oneTick() {
        if (isBump(0, 1)) {
            stopShape();
            return;
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

    public void setPause() {
        myKeyboardListener.block = !myKeyboardListener.block;
        pause = !pause;
    }

    public void setUpNewGame() {
        generateQueue();
        solve = false;
        pause = false;
        setGameOver(false);
        myKeyboardListener.block = false;
        clearedLines = 0;
        for (int i = 0; i < 12; i++) {
            for (int j = 0; j < 21; j++) {
                if (i == 0 || i == 11 || j == 20) matrix[i][j] = programInterface.wallsColor;
                else matrix[i][j] = programInterface.emptyColor;
            }
        }
        newShape();
        programInterface.repaint();
    }

    /*Package private*/ void logicStart() {
        myKeyboardListener = new MyKeyboardListener(this);
        programInterface.jFrame.addKeyListener(myKeyboardListener);
        setUpNewGame();
        launchApplication();
    }

}

