import java.awt.*;

public class NewShape {
    public Point[][] shapeCoordinates;
    public int currentRotation = 0;
    public Color currentColor;
    public Point shift = new Point(5, 0);

    public NewShape(int number) {
        this.shapeCoordinates = Shapes.shapes[number];
        this.currentColor = Shapes.shapesColors[number];
    }

    public NewShape (NewShape currentShape) {
        this.currentRotation = currentShape.currentRotation;
        this.shapeCoordinates = currentShape.shapeCoordinates.clone();
        this.currentColor = currentShape.currentColor;
        int x = currentShape.shift.x;
        int y = currentShape.shift.y;
        this.shift = new Point(x, y);
    }
}