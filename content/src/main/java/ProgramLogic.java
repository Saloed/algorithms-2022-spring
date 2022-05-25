import java.awt.*;
import java.util.*;

public class ProgramLogic {


    public class CurrentShape {
        public Point[][] shapeCoordinates;
        public int currentRotation = 0;
        public Color currentColor;
        public Point shift = new Point(5, 0);

        public CurrentShape(int number) {
            this.shapeCoordinates = Shapes.shapes[number];
            this.currentColor = Shapes.shapesColors[number];
        }
    }

    private boolean gameProcess = true;
    private boolean gameOver = false;
    private boolean pause = false;
    private boolean solve = false;
    private boolean check = false;

    private ProgramInterface programInterface;
    public Color[][] matrix = new Color[12][21];
    public ArrayDeque<Integer> allShapes = new ArrayDeque<>();
    public int amountOfShapes = 10;
    public int clearedLines;

    public CurrentShape currentShape;

    public int limit;
    public double[] bestScore;

    public ArrayDeque<Integer> bestXAndRotation;

    public boolean mySolverSecond = false;
    public boolean changeShape = true;

    private MyKeyboardListener myKeyboardListener;

    public void solverSecond() {
        limit = 8;
        bestScore = new double[limit + 1];
        mySolverSecond = true;
        for (int i = 0; i < limit + 1; i++) {
            bestScore[i] = -1e6;
        }
        dfs(matrix, allShapes, new ArrayDeque<>(), 0);
        programInterface.repaint();
        System.out.println(Arrays.toString(bestScore));
        //bestMatrix = new Color[12][21];
        //programInterface.repaint();
    }


    public void dfs(Color[][] matrix, ArrayDeque<Integer> allShapes, ArrayDeque<Integer> xAndRotation, int depth) {
        double currScore = checkScore(matrix);

        if (currScore < 0 || bestScore[depth] < 0) {
            if (-1 * currScore > -1 * 0.9 * bestScore[depth]) return;
        }

        if (depth == limit) {
            if (currScore > bestScore[limit]) {
                bestXAndRotation = xAndRotation.clone();
                bestScore[limit] = currScore;
            }
            return;
        }

        if (currScore > bestScore[depth]) {
            bestScore[depth] = currScore;
        }

        clearFullRows(matrix);

        ArrayDeque<Integer> allShapesHelp = allShapes.clone();
        CurrentShape currentShape = newShape(allShapesHelp);
        currentShape.currentRotation = 0;
        for (int k = 0; k < 4; k++) {
            for (int i = 1; i < 11; i++) {
                Color[][] matrixHelp = new Color[12][21];
                for (int iHelp = 0; iHelp < 12; iHelp++) {
                    System.arraycopy(matrix[iHelp], 0, matrixHelp[iHelp], 0, 21);
                }
                currentShape.shift = new Point(i,0);
                if (isBump(0, 0, matrixHelp, currentShape)) continue;
                int y = futurePosition(matrixHelp, currentShape);
                for (Point point1: currentShape.shapeCoordinates[currentShape.currentRotation]) {
                    matrixHelp[point1.x + currentShape.shift.x][point1.y + y] = currentShape.currentColor;
                }

                ArrayDeque<Integer> xAndRotationHelp = xAndRotation.clone();
                xAndRotationHelp.add(currentShape.shift.x * 100 + currentShape.currentRotation);

                dfs(matrixHelp, allShapesHelp.clone(), xAndRotationHelp, depth + 1);
            }
            currentShape.currentRotation++;
        }
    }


    private void setCheck(boolean value) {
        check = value;
    }

    public boolean getCheck() {
        return check;
    }

    public void startSolve() {
        myKeyboardListener.block = !myKeyboardListener.block;
        setCheck(true);
        setSolve(!getSolve());
    }

   /* public void toSolve() throws InterruptedException {
        placeShapeInBestPosition();
    }*/

    /*public void placeShapeInBestPosition() {
        int x = 0;
        int y;
        double score;
        double bestScore = -1e6;
        int bestRotation = 0;
        int yOld = shift.y;
        currentRotation = -1;
        for (int k = 0; k < 4; k++) {
            currentRotation++;
            for (int i = 0; i < 11; i++) {
                shift = new Point(i,0);
                if (isBump(0, 0)) continue;
                y = futurePosition(matrix);
                for (Point point1: currentShape[currentRotation]) {
                    matrix[point1.x + shift.x][point1.y + y] = currentColor;
                }
                score = checkScore();
                if (score > bestScore) {
                    bestScore = score;
                    bestRotation = k;
                    x = i;
                }
                for (Point point1: currentShape[currentRotation]) {
                    matrix[point1.x + shift.x][point1.y + y] = programInterface.emptyColor;
                }
            }
        }
        currentRotation = bestRotation;
        shift.x = x;
        shift.y = yOld;
        setCheck(false);
    }*/

    public double checkScore(Color[][] matrix) {
        return height(matrix) + lines(matrix) + bumpiness(matrix);
    }

    public double height(Color[][] matrix) {
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
        return -0.510066 * height + -0.35663 * holes;
    }

    public double lines(Color[][] matrix) {
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

    public double bumpiness(Color[][] matrix) {
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

    public void setSolve(boolean value) {
        this.solve = value;
    }

    public boolean getSolve() {
        return solve;
    }

    public boolean isBump(int x, int y, Color[][] matrix, CurrentShape currentShape) {
        for (Point point: currentShape.shapeCoordinates[currentShape.currentRotation]) {
            if (matrix[point.x + currentShape.shift.x + x][point.y + currentShape.shift.y + y] != programInterface.emptyColor) {
                return true;
            }
        }
        return false;
    }

    public int futurePosition(Color[][] matrix, CurrentShape currentShape) {
        int minY = 2000;
        for (Point point: currentShape.shapeCoordinates[currentShape.currentRotation]) {
            int y = 0;
            for (int j = point.y + currentShape.shift.y + 1; j < 20; j++) {
                if (matrix[point.x +currentShape.shift.x][j] == programInterface.emptyColor) {
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

    public int clearFullRows(Color[][] matrix) {
        int amount = 0;
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
                    amount++;
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
        return amount;
    }

    public void shapeMove(int k, Color[][] matrix, CurrentShape currentShape) {
        for (Point point: currentShape.shapeCoordinates[currentShape.currentRotation])
            if (matrix[currentShape.shift.x + point.x][currentShape.shift.y + point.y + 1] != programInterface.emptyColor) return;
        if (!isBump(k, 0, matrix, currentShape)) {
            currentShape.shift.x += k;
            programInterface.repaint();
        }
    }

    public void upShapeRotate(Color[][] matrix, CurrentShape currentShape) {
        if (currentShape.currentRotation < 3) currentShape.currentRotation++;
        else currentShape.currentRotation = 0;
        if (isBump(0, 0, matrix, currentShape)) {
            if (currentShape.currentRotation > 0) currentShape.currentRotation--;
            else currentShape.currentRotation = 3;
            return;
        }
        programInterface.repaint();
    }

    public void downShapeRotate(Color[][] matrix, CurrentShape currentShape) {
        if (currentShape.currentRotation > 0) currentShape.currentRotation--;
        else currentShape.currentRotation = 3;
        if (isBump(0, 0, matrix, currentShape)) {
            if (currentShape.currentRotation < 3) currentShape.currentRotation++;
            else currentShape.currentRotation = 0;
            return;
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
                while (gameProcess) {


                    if (!pause && !mySolverSecond) {
                        Thread.sleep(1000);
                        if (!gameOver) {
                            oneTick(matrix, currentShape);
                        }

                    } else if (!pause) {
                        if (!gameOver) {
                            if (limit == 0) {
                                solverSecond();
                            } else {
                                if (changeShape) {
                                    System.out.println("score = " + checkScore(matrix));
                                    int a = bestXAndRotation.pollLast();
                                    currentShape.currentRotation = a  % 10;
                                    currentShape.shift.x = a / 100;
                                    limit--;
                                    changeShape = false;
                                }

                                oneTick(matrix, currentShape);
                                Thread.sleep(50);
                            }
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
        if (value) myKeyboardListener.block = true;
    }

    public boolean getGameOver() {
        return gameOver;
    }

    public boolean isGameOver(Color[][] matrix, CurrentShape currentShape) {
        for (int k = 0; k < 4; k++) {
            if (isBump(0, 0, matrix, currentShape)) {
                return true;
            }
        }
        return false;
    }

    public CurrentShape newShape(ArrayDeque<Integer> allShapes) {
        setCheck(true);
        CurrentShape currentShape = new CurrentShape(allShapes.peek());
        currentShape.currentRotation = (int) (Math.random() * 4);
        allShapes.remove();
        int numberOfNewShape = (int) (Math.random() * 7);
        allShapes.add(numberOfNewShape);
        return currentShape;
    }

    public void oneTick(Color[][] matrix, CurrentShape currentShape) {
        if (isBump(0, 1, matrix, currentShape)) {
            stopShape(matrix, currentShape);
            return;
        }
        currentShape.shift.y++;
        if (isGameOver(matrix, currentShape)) {
            setGameOver(true);
        } else {
            clearFullRows(matrix);
            programInterface.repaint();
        }
    }

    public void stopShape(Color[][] matrix, CurrentShape currentShape) {
        for (Point point: currentShape.shapeCoordinates[currentShape.currentRotation]) {
            matrix[point.x + currentShape.shift.x][point.y + currentShape.shift.y] = currentShape.currentColor;
        }
        changeShape = true;
        this.currentShape = newShape(allShapes);
    }

    public void setPause(boolean value) {
        myKeyboardListener.block = value;
        pause = value;
    }

    public boolean getPause() {
        return pause;
    }

    public void setUpNewGame() {
        generateQueue();
        mySolverSecond = false;
        limit = 0;
        setSolve(false);
        setPause(false);
        setGameOver(false);
        clearedLines = 0;
        for (int i = 0; i < 12; i++) {
            for (int j = 0; j < 21; j++) {
                if (i == 0 || i == 11 || j == 20) matrix[i][j] = programInterface.wallsColor;
                else matrix[i][j] = programInterface.emptyColor;
            }
        }
        this.currentShape = newShape(allShapes);
        programInterface.repaint();
    }

    /*Package private*/ void logicStart() {
        myKeyboardListener = new MyKeyboardListener(this);
        programInterface.jFrame.addKeyListener(myKeyboardListener);
        setUpNewGame();
        launchApplication();
    }

}