import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class MyKeyboardListener implements KeyListener {

    private final ProgramLogic programLogic;
    boolean block = false;

    public MyKeyboardListener(ProgramLogic programLogic) {
        this.programLogic = programLogic;
        programLogic.setMyKeyboardListener(this);
    }

    @Override
    public void keyPressed(KeyEvent e) {

        if (!block) {

            int keyCode = e.getKeyCode();

            if (keyCode == KeyEvent.VK_UP) {
                programLogic.upShapeRotate();
            }

            if (keyCode == KeyEvent.VK_DOWN) {
                programLogic.downShapeRotate();
            }

            if (keyCode == KeyEvent.VK_LEFT) {
                programLogic.shapeMove(-1);
            }

            if (keyCode == KeyEvent.VK_RIGHT) {
                programLogic.shapeMove(1);
            }

            if (keyCode == KeyEvent.VK_R) {
                programLogic.setUpNewGame();
            }

            if (keyCode == KeyEvent.VK_SPACE) {
                programLogic.oneTick();
            }

        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }
}