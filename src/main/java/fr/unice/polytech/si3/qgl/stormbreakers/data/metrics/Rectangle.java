package fr.unice.polytech.si3.qgl.stormbreakers.data.metrics;

public class Rectangle extends Shape {
    private double width;
    private double height;
    private double orientation;

    Rectangle(double width, double height, double orientation) {
        super("rectangle");
        this.width = width;
        this.height = height;
        this.orientation = orientation;
    }
}
