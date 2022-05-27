import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class MyKeyboardListener implements KeyListener {

    private final ProgramLogic programLogic;
    public boolean block = true;

    public MyKeyboardListener(ProgramLogic programLogic) {
        this.programLogic = programLogic;
        programLogic.setMyKeyboardListener(this);
    }

    @Override
    public void keyPressed(KeyEvent e) {

        int keyCode = e.getKeyCode();

        if (!block) {

            if (keyCode == KeyEvent.VK_W) {
                programLogic.upShapeRotate(programLogic.matrix, programLogic.currentShape);
            }

            if (keyCode == KeyEvent.VK_S) {
                programLogic.downShapeRotate(programLogic.matrix, programLogic.currentShape);
            }

            if (keyCode == KeyEvent.VK_A) {
                programLogic.shapeMove(-1, programLogic.matrix, programLogic.currentShape);
            }

            if (keyCode == KeyEvent.VK_D) {
                programLogic.shapeMove(1, programLogic.matrix, programLogic.currentShape);
            }

            if (keyCode == KeyEvent.VK_SPACE) {
                programLogic.oneTick(programLogic.matrix, programLogic.currentShape);
            }

        }

        if (keyCode == KeyEvent.VK_P) {
            programLogic.setPause(!programLogic.getPause());
        }

        if (keyCode == KeyEvent.VK_F) {
            programLogic.startSolveFirst();
        }

        if (keyCode == KeyEvent.VK_R) {
            programLogic.setUpNewGame();
        }

        if (keyCode == KeyEvent.VK_M) {
            programLogic.startSolveMonteCarlo();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }
}