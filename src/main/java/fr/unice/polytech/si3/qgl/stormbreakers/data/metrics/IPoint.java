package fr.unice.polytech.si3.qgl.stormbreakers.data.metrics;

public interface IPoint {
    
    public double x();
    public double y();

    public double distanceTo(IPoint other);
}