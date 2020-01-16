package fr.unice.polytech.si3.qgl.stormbreakers.data.metrics;

public class Rectangle extends Shape {
    private double width;
    private double lenght;
    private double orientation;

    Rectangle(double width, double lenght, double orientation) {
        super("rectangle");
        this.width = width;
        this.lenght = lenght;
        this.orientation = orientation;
    }
}
