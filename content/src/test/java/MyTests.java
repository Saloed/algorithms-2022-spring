import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

public class MyTests {

    ProgramLogic programLogic;
    ProgramInterface programInterface;
    MyKeyboardListener myKeyboardListener;

    public MyTests() throws InterruptedException {
        programLogic = new ProgramLogic();
        programInterface = new ProgramInterface(programLogic);
        programLogic.logicStart();
        programLogic.setUpNewGame();
    }

    @org.junit.jupiter.api.Test
    void initializationTest() {
        boolean flag = true;
        for (int i = 0; i < 12; i++) {
            for (int j = 0; j < 21; j++) {
                if (i == 0 || i == 11 || j == 20) {
                    if (programLogic.matrix[i][j] != programInterface.wallsColor) {
                        flag = false;
                        break;
                    }
                }
            }
        }
        assertTrue(flag);
    }

    @org.junit.jupiter.api.Test
    void moveLeftTest() {
        programLogic.currentShape.setShift(5, 0);
        programLogic.currentShape.setShapeCoordinates(Shapes.shapes[0]);
        programLogic.currentShape.setCurrentRotation(1);
        programLogic.shapeMove(-1, programLogic.matrix, programLogic.currentShape);
        for (Point point: programLogic.currentShape.getShapeCoordinates()[programLogic.currentShape.getCurrentRotation()]) {
            assertEquals(5, point.x + programLogic.currentShape.getShift().x);
        }
    }

    @org.junit.jupiter.api.Test
    void moveRightTest() {
        programLogic.currentShape.setShift(5, 0);
        programLogic.currentShape.setShapeCoordinates(Shapes.shapes[0]);
        programLogic.currentShape.setCurrentRotation(1);
        programLogic.shapeMove(1, programLogic.matrix, programLogic.currentShape);
        for (Point point: programLogic.currentShape.getShapeCoordinates()[programLogic.currentShape.getCurrentRotation()]) {
            assertEquals(7, point.x + programLogic.currentShape.getShift().x);
        }
    }

    @org.junit.jupiter.api.Test
    void oneTickTest() {
        programLogic.currentShape.setShift(5, 0);
        programLogic.currentShape.setShapeCoordinates(Shapes.shapes[0]);
        programLogic.currentShape.setCurrentRotation(0);
        programLogic.oneTick(programLogic.matrix, programLogic.currentShape);
        for (Point point: programLogic.currentShape.getShapeCoordinates()[programLogic.currentShape.getCurrentRotation()]) {
            assertEquals(2, point.y + programLogic.currentShape.getShift().y);
        }
    }

    @org.junit.jupiter.api.Test
    void clearRowTest() {
        for (int i = 1; i < 11; i++) {
            programLogic.matrix[i][19] = new Color(71, 156, 48);
        }
        programLogic.clearFullRows(programLogic.matrix, false);
        for (int i = 1; i < 11; i++) {
            assertEquals(programInterface.emptyColor, programLogic.matrix[i][19]);
        }


        for (int j = 17; j < 20; j++) {
            for (int i = 1; i < 11; i++) {
                programLogic.matrix[i][j] = new Color(71, 156, 48);
            }
        }
        programLogic.matrix[5][18] = programInterface.emptyColor;
        programLogic.clearFullRows(programLogic.matrix, false);

        for (int i = 1; i < 11; i++) {
            if (i == 5) {
                assertEquals(programInterface.emptyColor, programLogic.matrix[5][19]);
            } else {
                assertEquals(programInterface.emptyColor, programInterface.emptyColor);
            }
        }

    }

    @org.junit.jupiter.api.Test
    void futurePositionTest() {
        programLogic.currentShape.setShift(5, 0);
        programLogic.currentShape.setShapeCoordinates(Shapes.shapes[0]);
        programLogic.currentShape.setCurrentRotation(0);
        for (int i = 0; i < 11; i++) {
            programLogic.matrix[i][19] = new Color(71, 156, 48);
        }

        int y = programLogic.futurePosition(programLogic.matrix, programLogic.currentShape);
        for (Point point : programLogic.currentShape.getShapeCoordinates()[programLogic.currentShape.getCurrentRotation()]) {
            assertEquals(18, programLogic.currentShape.getShift().y + y + point.y);
        }
    }
}


