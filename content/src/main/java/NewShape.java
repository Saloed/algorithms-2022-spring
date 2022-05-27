import java.awt.*;

public class NewShape {
    private Point[][] shapeCoordinates;
    private int currentRotation = 0;
    private Color currentColor;
    private Point shift = new Point(5, 0);

    public void setShift(int x, int y) {
        shift.x = x;
        shift.y = y;
    }

    public Point getShift() {
        return shift;
    }

    public void setCurrentRotation(int k) {
        currentRotation = k;
    }

    public int getCurrentRotation() {
        return currentRotation;
    }

    public Color getCurrentColor() {
        return currentColor;
    }

    public Point[][] getShapeCoordinates() {
        return shapeCoordinates;
    }

    public void setShapeCoordinates(Point[][] value) {
        shapeCoordinates = value;
    }

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