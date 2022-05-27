import java.awt.*;
import java.util.*;

public class ProgramLogic {


    private boolean gameProcess = true;
    private boolean play = false;
    private boolean gameOver = false;
    private boolean pause = false;
    private boolean solve = false;
    private boolean checked = false;
    private ProgramInterface programInterface;

    public int iBorder = 12;
    public int jBorder = 21;
    public int rotationBorder = 4;
    public int numberOfDifferentShapes = 7;
    public Color[][] matrix = new Color[iBorder][jBorder];
    public ArrayDeque<Integer> allShapes = new ArrayDeque<>();
    public int clearedLines;

    public NewShape currentShape;
    private MyKeyboardListener myKeyboardListener;

    private boolean mySolverMonteCarlo = false;
    private boolean mySolverFirst = false;
    public double[] score;
    public int[] numberOfSuccess;
    public int numberOfGeneratedShapes = 20;
    public int numberOfTries = 500;
    public int amountOfShapes = numberOfGeneratedShapes;

    public boolean getMySolverFirst() {
        return mySolverFirst;
    }

    public boolean getChecked() {
        return checked;
    }

    public boolean getMySolverMonteCarlo() {
        return mySolverMonteCarlo;
    }

    public boolean getSolve() {
        return solve;
    }

    public boolean getGameOver() {
        return gameOver;
    }

    public boolean getPlay() {
        return play;
    }

    public boolean getPause() {
        return pause;
    }



    private void setChecked(boolean value) {
        checked = value;
    }

    public void setSolve(boolean value) {
        this.solve = value;
    }

    public void setMyKeyboardListener(MyKeyboardListener myKeyboardListener) {
        this.myKeyboardListener = myKeyboardListener;
    }

    //Method to assign jFrame
    public void setProgramInterface(ProgramInterface programInterface) {
        this.programInterface = programInterface;
    }

    public void setPause(boolean value) {
        myKeyboardListener.block = value;
        pause = value;
    }

    public void setPlay(boolean value) {
        play = value;
    }

    public void setGameOver(boolean value) {
        gameOver = value;
        if (value) myKeyboardListener.block = true;
    }




    public void startSolve() {
        myKeyboardListener.block = true;
        setChecked(false);
        setSolve(true);
        setPlay(false);
    }

    public void stopSolve() {
        myKeyboardListener.block = false;
        setChecked(true);
        setSolve(false);
        setMySolverFirst(false);
        setMySolverMonteCarlo(false);
        setPlay(true);
    }


    public void setMySolverFirst(boolean value) {
        mySolverFirst = value;
    }

    public void startSolveFirst() {
        if (!getMySolverFirst()) {
            startSolve();
            setMySolverFirst(true);
            setMySolverMonteCarlo(false);
        } else {
            stopSolve();
        }
    }

    public void solverFirst() throws InterruptedException {
        placeShapeInBestPosition();
    }

    public void placeShapeInBestPosition() {
        int x = 0;
        int y;
        double score;
        double bestScore = -1e6;
        int bestRotation = 0;
        int yOld = currentShape.getShift().y;
        currentShape.setCurrentRotation(0);
        Color[][] matrixHelp = new Color[iBorder][jBorder];
        for (int iHelp = 0; iHelp < iBorder; iHelp++) {
            System.arraycopy(this.matrix[iHelp], 0, matrixHelp[iHelp], 0, jBorder);
        }

        for (int k = 0; k < rotationBorder; k++) {
            for (int i = 0; i < iBorder - 1; i++) {

                currentShape.setShift(i,0);
                if (isBump(0, 0, matrixHelp , currentShape)) continue;
                y = futurePosition(matrixHelp, currentShape);
                for (Point point1: currentShape.getShapeCoordinates()[currentShape.getCurrentRotation()]) {
                    matrixHelp[point1.x + currentShape.getShift().x][point1.y + y] = currentShape.getCurrentColor();
                }
                score = checkScore(matrixHelp);
                if (score > bestScore) {
                    bestScore = score;
                    bestRotation = k;
                    x = i;
                }
                for (Point point1: currentShape.getShapeCoordinates()[currentShape.getCurrentRotation()]) {
                    matrixHelp[point1.x + currentShape.getShift().x][point1.y + y] = programInterface.emptyColor;
                }
            }
            if (k != rotationBorder - 1) currentShape.setCurrentRotation(currentShape.getCurrentRotation() + 1);
        }
        currentShape.setCurrentRotation(bestRotation);
        currentShape.setShift(x, yOld);
        setChecked(false);
    }


    public void setMySolverMonteCarlo(boolean value) {
        mySolverMonteCarlo = value;
    }

    public void startSolveMonteCarlo() {
        if (!getMySolverMonteCarlo()) {
            startSolve();
            setMySolverFirst(false);
            setMySolverMonteCarlo(true);
        } else {
            stopSolve();
        }
    }

    public void solverMonteCarlo() {
        score = new double[104];
        numberOfSuccess = new int[104];

        NewShape newShape = new NewShape(currentShape);
        newShape.setCurrentRotation(0);


        for (int k = 0; k < rotationBorder; k++) {
            for (int i = 0; i < iBorder - 1; i++) {

                Color[][] matrixHelp = new Color[iBorder][jBorder];
                for (int iHelp = 0; iHelp < iBorder; iHelp++) {
                    System.arraycopy(this.matrix[iHelp], 0, matrixHelp[iHelp], 0, jBorder);
                }

                newShape.setShift(i, 0);
                if (isBump(0, 0, matrixHelp, newShape)) {
                    score[i * 10 + newShape.getCurrentRotation()] = -1e9;
                    newShape.setShift(4, 0);
                    continue;
                }
                int y = futurePosition(matrixHelp, newShape);
                for (Point point: newShape.getShapeCoordinates()[newShape.getCurrentRotation()]) {
                    matrixHelp[point.x + newShape.getShift().x][point.y + y] = newShape.getCurrentColor();
                }

                algorithmMonteCarlo(matrixHelp, allShapes, newShape.getShift().x * 10 + newShape.getCurrentRotation());
            }

            if (k != rotationBorder - 1) newShape.setCurrentRotation(newShape.getCurrentRotation() + 1);
        }

        double bestScore = -1e9;
        int bestInfo = 40;

        for (int i = 0; i < 104; i++) {
            if (i % 10 < rotationBorder && numberOfSuccess[i] > 0) {

                double currScore = score[i] / numberOfSuccess[i];
                if (currScore > bestScore) {
                    bestScore = currScore;
                    bestInfo = i;
                }

            }
        }

        currentShape.setCurrentRotation(bestInfo % 10);
        currentShape.setShift(bestInfo / 10, 0);
        setChecked(true);
    }

    public void algorithmMonteCarlo(Color[][] matrixHelp, ArrayDeque<Integer> allShapesOriginal, int index) {
        for (int j = 0; j < numberOfTries; j++) {

            ArrayDeque<Integer> allShapesHelp = allShapesOriginal.clone();
            for (int i = 0; i < numberOfGeneratedShapes; i++) {
                NewShape newShape = newShape(allShapesHelp);
                if (!isGameOver(matrixHelp, newShape)) {
                    do {
                        newShape.setShift((int) (Math.random() * (iBorder - 1)), 0);
                        newShape.setCurrentRotation((int) (Math.random() * rotationBorder));
                    } while (isBump(0, 0, matrixHelp, newShape));

                    int y = futurePosition(matrixHelp, newShape);
                    for (Point point : newShape.getShapeCoordinates()[newShape.getCurrentRotation()]) {
                        matrixHelp[point.x + newShape.getShift().x][point.y + y] = Color.MAGENTA;
                    }

                } else break;

                if (i == numberOfGeneratedShapes - 1) {
                    double currScore = checkScore(matrixHelp);
                    score[index] += currScore;
                    numberOfSuccess[index]++;
                }

                clearFullRows(matrixHelp, false);
            }

            for (int i = 1; i < iBorder - 1; i++) {
                for (int j1 = 0; j1 < jBorder - 1; j1++) {
                    if (matrixHelp[i][j1] == Color.MAGENTA) matrixHelp[i][j1] = programInterface.emptyColor;
                }
            }
        }
    }



    public double checkScore(Color[][] matrix) {
        return height(matrix) + lines(matrix) + bumpiness(matrix);
    }

    public double height(Color[][] matrix) {
        int holes = 0;
        int height = 0;
        for (int i = 1; i < iBorder - 1; i++) {
            int height1 = 0;
            for (int j = 0; j < jBorder - 1; j++) {
                if (matrix[i][j] != programInterface.emptyColor) {
                    height1 = 20 - j;
                    break;
                }
            }
            for (int j = jBorder - 2; j > jBorder - 2 - height1; j--) {
                if (matrix[i][j] == programInterface.emptyColor) holes++;
            }
            height += height1;
        }
        return -0.510066 * height + -0.35663 * holes;
    }

    public double lines(Color[][] matrix) {
        int lines = 0;
        for (int j = jBorder - 2; j > 0 ; j--) {
            boolean flag = true;
            for (int i = 1; i < iBorder - 1; i++) {
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
        int height1 = 0;
        for (int j = 0; j < jBorder; j++) {
            if (matrix[1][j] != programInterface.emptyColor) {
                height1 = j;
                break;
            } else height1 = jBorder - 1;
        }
        for (int i = 2; i < iBorder - 1; i++) {
            int height2 = 0;
            for (int j = 0; j < jBorder; j++) {
                if (matrix[i][j] != programInterface.emptyColor) {
                    height2 = j;
                    break;
                } else height2 = jBorder - 1;
            }
            bumpiness += Math.abs(height1 - height2);

            height1 = height2;
        }
        return -0.184483 * bumpiness;
    }



    public boolean isBump(int x, int y, Color[][] matrix, NewShape newShape) {
        for (Point point: newShape.getShapeCoordinates()[newShape.getCurrentRotation()]) {
            if (matrix[point.x + newShape.getShift().x + x][point.y + newShape.getShift().y + y] != programInterface.emptyColor) {
                return true;
            }
        }
        return false;
    }

    public int futurePosition(Color[][] matrix, NewShape newShape) {
        int minY = jBorder + 1;
        for (Point point: newShape.getShapeCoordinates()[newShape.getCurrentRotation()]) {
            int y = 0;
            for (int j = point.y + newShape.getShift().y + 1; j < jBorder - 1; j++) {
                if (matrix[point.x + newShape.getShift().x][j] == programInterface.emptyColor) {
                    y++;
                } else break;
            }
            if (y < minY) minY = y;
        }
        return minY;
    }

    public int clearFullRows(Color[][] matrix, boolean toAdd) {
        int amount = 0;
        for (int k = 0; k < rotationBorder; k++) {
            for (int j = jBorder - 2; j > 0; j--) {
                boolean flag = true;
                for (int i = 1; i < iBorder - 1; i++) {
                    if (matrix[i][j] == programInterface.emptyColor) {
                        flag = false;
                        break;
                    }
                }
                if (flag) {
                    if (toAdd) {
                        amount++;
                        clearedLines++;
                    }
                    for (int i = j; i > 0; i--) {
                        for (int l = 1; l < iBorder - 1; l++) {
                            matrix[l][i] = matrix[l][i - 1];
                        }
                    }
                    for (int i = 1; i < iBorder - 1; i++) {
                        matrix[i][0] = programInterface.emptyColor;
                    }
                }
            }
        }
        return amount;
    }

    public void shapeMove(int k, Color[][] matrix, NewShape newShape) {
        for (Point point: newShape.getShapeCoordinates()[newShape.getCurrentRotation()])
            if (matrix[newShape.getShift().x + point.x][newShape.getShift().y + point.y + 1] != programInterface.emptyColor) return;
        if (!isBump(k, 0, matrix, newShape)) {
            newShape.getShift().x += k;
            programInterface.repaint();
        }
    }

    public void upShapeRotate(Color[][] matrix, NewShape newShape) {
        if (newShape.getCurrentRotation() < 3) newShape.setCurrentRotation(newShape.getCurrentRotation() + 1);
        else newShape.setCurrentRotation(0);
        if (isBump(0, 0, matrix, newShape)) {
            if (newShape.getCurrentRotation() > 0) newShape.setCurrentRotation(newShape.getCurrentRotation() - 1);
            else newShape.setCurrentRotation(3);
            return;
        }
        programInterface.repaint();
    }

    public void downShapeRotate(Color[][] matrix, NewShape newShape) {
        if (newShape.getCurrentRotation() > 0) newShape.setCurrentRotation(newShape.getCurrentRotation() - 1);
        else newShape.setCurrentRotation(3);
        if (isBump(0, 0, matrix, newShape)) {
            if (newShape.getCurrentRotation() < 3) newShape.setCurrentRotation(newShape.getCurrentRotation() + 1);
            else newShape.setCurrentRotation(0);
            return;
        }
        programInterface.repaint();
    }

    public void generateQueue() {
        allShapes = new ArrayDeque<>();
        for (int i = 0; i < amountOfShapes; i++) {
            int nextShape = (int) (Math.random() * numberOfDifferentShapes);
            allShapes.add(nextShape);
        }
    }

    public boolean isGameOver(Color[][] matrix, NewShape newShape) {
        for (int k = 0; k < rotationBorder; k++) {
            if (isBump(0, 0, matrix, newShape)) {
                return true;
            }
        }
        return false;
    }

    public NewShape newShape(ArrayDeque<Integer> allShapesHelp) {
        NewShape newShape = new NewShape(allShapesHelp.poll());
        newShape.setCurrentRotation((int) (Math.random() * rotationBorder));
        int numberOfNewShape = (int) (Math.random() * numberOfDifferentShapes);
        allShapesHelp.add(numberOfNewShape);
        return newShape;
    }

    public void oneTick(Color[][] matrix, NewShape newShape) {
        if (isBump(0, 1, matrix, newShape)) {
            stopShape(matrix, newShape);
            return;
        }
        newShape.setShift(newShape.getShift().x, newShape.getShift().y + 1);
        programInterface.repaint();
    }

    public void stopShape(Color[][] matrix, NewShape currentShape) {
        for (Point point: currentShape.getShapeCoordinates()[currentShape.getCurrentRotation()]) {
            matrix[point.x + currentShape.getShift().x][point.y + currentShape.getShift().y] = currentShape.getCurrentColor();
        }

        setChecked(false);
        this.currentShape = newShape(allShapes);

        if (isGameOver(matrix, this.currentShape)) {
            setGameOver(true);
        } else {
            clearFullRows(matrix, true);
        }
    }

    public void setUpNewGame() {
        generateQueue();
        stopSolve();
        setPause(false);
        setGameOver(false);
        clearedLines = 0;
        for (int i = 0; i < iBorder; i++) {
            for (int j = 0; j < jBorder; j++) {
                if (i == 0 || i == iBorder - 1 || j == jBorder - 1) matrix[i][j] = programInterface.wallsColor;
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

    //Run of the process
    private void launchApplication() {
        Runnable r = ()->{
            try {
                while (gameProcess) {
                    programInterface.repaint();

                    if (!getGameOver()) {
                        if (getPlay()) {
                            oneTick(matrix, currentShape);
                            Thread.sleep(1000);

                        } else if (getSolve()) {
                            if (!getChecked()) {
                                if (getMySolverMonteCarlo()) {
                                    solverMonteCarlo();
                                } else if (getMySolverFirst()) {
                                    solverFirst();
                                }
                            }

                            oneTick(matrix, currentShape);
                            Thread.sleep(10);
                        }
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };

        Thread myThread = new Thread(r,"MyThread");
        myThread.start();
    }
}