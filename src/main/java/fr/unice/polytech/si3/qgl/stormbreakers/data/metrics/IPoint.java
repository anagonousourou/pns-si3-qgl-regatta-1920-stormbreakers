package fr.unice.polytech.si3.qgl.stormbreakers.data.metrics;

public interface IPoint {
    
    public double x();
    public double y();

    
    public default double distanceTo(IPoint other) {
        return Math.hypot(other.x() - this.x(), other.y() - this.y());
    }

    public static double distance(double x1, double y1, double x2, double y2) {
        return Math.hypot(x2 - x1, y2 - y1);
    }
}