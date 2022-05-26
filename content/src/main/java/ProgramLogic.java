import java.awt.*;
import java.util.*;

public class ProgramLogic {


    private boolean gameProcess = true;
    private boolean gameOver = false;
    private boolean pause = false;
    private boolean solve = false;
    private boolean check = false;

    private ProgramInterface programInterface;
    public Color[][] matrix = new Color[12][21];
    public ArrayDeque<Integer> allShapes = new ArrayDeque<>();
    public int clearedLines;

    public NewShape currentShape;

   /* public int limit;

    public double[] bestScore;
    public ArrayDeque<Integer> bestXAndRotation;*/

    public boolean mySolverSecond = false;
    public boolean changeShape = true;

    private MyKeyboardListener myKeyboardListener;

    public boolean mySolverMonteCarlo = false;
    public boolean solverMCContinue = true;
    public double[] score;
    public int[] numberOfSuccess;
    int N;
    public int numberOfGeneratedShapes = 10;
    public int numberOfTries = 1000;

    public int amountOfShapes = numberOfGeneratedShapes;

    public void solverMonteCarlo() {
        score = new double[104];

        for (int i = 0; i < 104; i++) {
            score[i] = 0;
        }
        numberOfSuccess = new int[104];
        N = 0;

        NewShape newShape = new NewShape(currentShape);
        newShape.currentRotation = 0;


        for (int k = 0; k < 4; k++) {
            for (int i = 0; i < 11; i++) {

                Color[][] matrixHelp = new Color[12][21];
                for (int iHelp = 0; iHelp < 12; iHelp++) {
                    for (int jHelp = 0; jHelp < 21; jHelp++) {
                        matrixHelp[iHelp][jHelp] = this.matrix[iHelp][jHelp];
                    }
                }

                newShape.shift.x = i;
                if (isBump(0, 0, matrixHelp, newShape)) {
                    score[i * 10 + newShape.currentRotation] = -1e9;
                    //System.out.println(i * 10 + newShape.currentRotation);
                    continue;
                }
                int y = futurePosition(matrixHelp, newShape);
                for (Point point1: newShape.shapeCoordinates[newShape.currentRotation]) {
                    matrixHelp[point1.x + newShape.shift.x][point1.y + y] = Color.PINK;
                }

                algorithmMonteCarlo(matrixHelp, allShapes, newShape.shift.x * 10 + newShape.currentRotation);

                /*for (int i1 = 1; i < 11; i++) {
                    for (int j1 = 0; j1 < 20; j1++) {
                        if (matrixHelp[i1][j1] == Color.PINK) matrixHelp[i1][j1] = programInterface.emptyColor;
                    }
                }*/
            }

            if (k != 3) newShape.currentRotation++;
        }

        double bestScore = -1e9;
        int bestInfo = 11;
        double c = 1.41;
        //System.out.println(Arrays.toString(score));
        //System.out.println(Arrays.toString(numberOfSuccess));

        for (int i = 0; i < 104; i++) {
            if (i % 10 < 4 && numberOfSuccess[i] > 0) {
                //double currScore = score[i];

                double currScore = score[i] / numberOfSuccess[i];
                //double currScore = (double) numberOfSuccess[i] / numberOfTries;
                //double currScore = (double) (numberOfSuccess[i] / N) + c * Math.sqrt(Math.log(numberOfTries) / N);
                //double currScore = score[i] * (1 - (double) numberOfSuccess[i] / numberOfTries);

                //System.out.println(currScore);

                //int currScore = numberOfSuccess[i];
                if (currScore > bestScore) {
                    bestScore = currScore;
                    bestInfo = i;
                }

            }
        }
        //System.out.println(bestScore * numberOfSuccess[bestInfo] + "   " + bestInfo);

        //System.out.println(Arrays.toString(numberOfSuccess) + "\n" + bestInfo);
        //System.out.println(Arrays.toString(score));
        if (bestScore > -1e9) {
            currentShape.currentRotation = bestInfo % 10;
            currentShape.shift.x = bestInfo / 10;
        } else setGameOver(true);
    }

    public void algorithmMonteCarlo(Color[][] matrixHelp, ArrayDeque<Integer> allShapesOriginal, int index) {
        for (int j = 0; j < numberOfTries; j++) {

            ArrayDeque<Integer> allShapesHelp = allShapesOriginal.clone();
            for (int i = 0; i < numberOfGeneratedShapes; i++) {
                NewShape newShape = newShape(allShapesHelp);
                if (!isGameOver(matrixHelp, newShape)) {
                    N++;
                    do {
                        newShape.shift.x = (int) (Math.random() * 11);
                        newShape.currentRotation = (int) (Math.random() * 4);
                    } while (isBump(0, 0, matrixHelp, newShape));

                    int y = futurePosition(matrixHelp, newShape);
                    for (Point point1 : newShape.shapeCoordinates[newShape.currentRotation]) {
                        matrixHelp[point1.x + newShape.shift.x][point1.y + y] = Color.MAGENTA;
                    }

                } else break;

                if (i == numberOfGeneratedShapes - 1) {
                    double currScore = checkScore(matrixHelp);
                    //if (currScore > score[index]) score[index] = currScore;
                    score[index] += currScore;
                    numberOfSuccess[index]++;
                }

                clearFullRows(matrixHelp, false);
            }

            for (int i = 1; i < 11; i++) {
                for (int j1 = 0; j1 < 20; j1++) {
                    if (matrixHelp[i][j1] == Color.MAGENTA) matrixHelp[i][j1] = programInterface.emptyColor;
                }
            }
        }
    }



    /*
    public void solverSecond() {
        limit = 4;
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
            if (-1 * currScore > -1 * 0.2 * bestScore[depth]) return;
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
*/

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

    public void toSolve() throws InterruptedException {
        placeShapeInBestPosition();
    }

    public void placeShapeInBestPosition() {
        int x = 0;
        int y;
        double score;
        double bestScore = -1e6;
        int bestRotation = 0;
        int yOld = currentShape.shift.y;
        currentShape.currentRotation = 0;
        for (int k = 0; k < 4; k++) {
            for (int i = 0; i < 11; i++) {
                currentShape.shift = new Point(i,0);
                if (isBump(0, 0, matrix, currentShape)) continue;
                y = futurePosition(matrix, currentShape);
                for (Point point1: currentShape.shapeCoordinates[currentShape.currentRotation]) {
                    matrix[point1.x + currentShape.shift.x][point1.y + y] = currentShape.currentColor;
                }
                score = checkScore(matrix);
                if (score > bestScore) {
                    bestScore = score;
                    bestRotation = k;
                    x = i;
                }
                for (Point point1: currentShape.shapeCoordinates[currentShape.currentRotation]) {
                    matrix[point1.x + currentShape.shift.x][point1.y + y] = programInterface.emptyColor;
                }
            }
            if (k != 3)currentShape.currentRotation++;
        }
        currentShape.currentRotation = bestRotation;
        currentShape.shift.x = x;
        currentShape.shift.y = yOld;
        setCheck(false);
    }

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
        int height1 = 0;
        for (int j = 0; j < 21; j++) {
            if (matrix[1][j] != programInterface.emptyColor) {
                height1 = j;
                break;
            } else height1 = 20;
        }
        for (int i = 2; i < 11; i++) {
            int height2 = 0;
            for (int j = 0; j < 21; j++) {
                if (matrix[i][j] != programInterface.emptyColor) {
                    height2 = j;
                    break;
                } else height2 = 20;
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

    public boolean isBump(int x, int y, Color[][] matrix, NewShape newShape) {
        for (Point point: newShape.shapeCoordinates[newShape.currentRotation]) {
            if (matrix[point.x + newShape.shift.x + x][point.y + newShape.shift.y + y] != programInterface.emptyColor) {
                return true;
            }
        }
        return false;
    }

    public int futurePosition(Color[][] matrix, NewShape newShape) {
        int minY = 2000;
        for (Point point: newShape.shapeCoordinates[newShape.currentRotation]) {
            int y = 0;
            for (int j = point.y + newShape.shift.y + 1; j < 20; j++) {
                if (matrix[point.x + newShape.shift.x][j] == programInterface.emptyColor) {
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

    public int clearFullRows(Color[][] matrix, boolean toAdd) {
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
                    if (toAdd) {
                        amount++;
                        clearedLines++;
                    }
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

    public void shapeMove(int k, Color[][] matrix, NewShape newShape) {
        for (Point point: newShape.shapeCoordinates[newShape.currentRotation])
            if (matrix[newShape.shift.x + point.x][newShape.shift.y + point.y + 1] != programInterface.emptyColor) return;
        if (!isBump(k, 0, matrix, newShape)) {
            newShape.shift.x += k;
            programInterface.repaint();
        }
    }

    public void upShapeRotate(Color[][] matrix, NewShape newShape) {
        if (newShape.currentRotation < 3) newShape.currentRotation++;
        else newShape.currentRotation = 0;
        if (isBump(0, 0, matrix, newShape)) {
            if (newShape.currentRotation > 0) newShape.currentRotation--;
            else newShape.currentRotation = 3;
            return;
        }
        programInterface.repaint();
    }

    public void downShapeRotate(Color[][] matrix, NewShape newShape) {
        if (newShape.currentRotation > 0) newShape.currentRotation--;
        else newShape.currentRotation = 3;
        if (isBump(0, 0, matrix, newShape)) {
            if (newShape.currentRotation < 3) newShape.currentRotation++;
            else newShape.currentRotation = 0;
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
                    if (!gameOver) {

                        if (solve) {
                            toSolve();
                            oneTick(matrix, currentShape);
                            //Thread.sleep(2);
                        }


                        if (mySolverMonteCarlo) {
                            if (solverMCContinue) {
                                solverMonteCarlo();
                                solverMCContinue = false;
                            }

                            oneTick(matrix, currentShape);
                            //Thread.sleep(2);
                        }
                    }
/*
                    if (!pause && !mySolverSecond) {
                        Thread.sleep(1000);
                        if (!gameOver) {
                            oneTick(matrix, currentShape);
                        }

                    } else if (!pause) {
                        if (!gameOver) {
                            if (limit == 0) {
                                //solverSecond();
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
                    }*/

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

    public boolean isGameOver(Color[][] matrix, NewShape newShape) {
        for (int k = 0; k < 4; k++) {
            if (isBump(0, 0, matrix, newShape)) {
                return true;
            }
        }
        return false;
    }

    public NewShape newShape(ArrayDeque<Integer> allShapesHelp) {
        setCheck(true);
        NewShape currentShape = new NewShape(allShapesHelp.poll());
        currentShape.currentRotation = (int) (Math.random() * 4);
        int numberOfNewShape = (int) (Math.random() * 7);
        allShapesHelp.add(numberOfNewShape);
        return currentShape;
    }

    public void oneTick(Color[][] matrix, NewShape newShape) {
        if (isBump(0, 1, matrix, newShape)) {
            stopShape(matrix, newShape);
            return;
        }
        newShape.shift.y++;
    }

    public void stopShape(Color[][] matrix, NewShape currentShape) {
        for (Point point: currentShape.shapeCoordinates[currentShape.currentRotation]) {
            matrix[point.x + currentShape.shift.x][point.y + currentShape.shift.y] = currentShape.currentColor;
        }

        solverMCContinue = true;
        changeShape = true;
        this.currentShape = newShape(allShapes);

        if (isGameOver(matrix, this.currentShape)) {
            setGameOver(true);
        } else {
            clearFullRows(matrix, true);
        }
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
        //limit = 0;
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