package fr.unice.polytech.si3.qgl.stormbreakers.math;

import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.IPoint;
import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Polygon;
import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Position;
import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Rectangle;
import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Shape;

public interface Surface extends IPoint,Orientable {

    public Shape getShape();
    //Une surface a une shape, des coordonnées x,y et une orientation
    //l'orientation ici est l'oriention de position pas celle de la shape
    public default boolean isPtInside(IPoint point) {
        // On se replace par rapport au centre de la forme
        
        Point2D pt = new Point2D(point.x() - this.x(), point.y() - this.y());
        
        double orientation = this.getOrientation();
        // On compense l'orientation du checkpoint
        if (orientation != 0)
            pt = pt.getRotatedBy(-orientation);
        return this.getShape().isPtInside(pt);
    }

    
    public default boolean intersectsWith(LineSegment2D lineSegment2D) {
        if (this.getShape().getType().equals("rectangle")) {
			
            return new RectanglePositioned((Rectangle) this.getShape(),new Position(this.x(), this.y(), getOrientation())).intersectsWith(lineSegment2D);
        } 
        else if(this.getShape().getType().equals("polygon")){
            Polygon shape=(Polygon)this.getShape();
            return shape.generateBordersInThePlan(this).stream().anyMatch(lineSegment2D::intersects);
        }
        
        else {
            return false;
        }
    }
    /**
     * This methode must return the point to aim for
     * so that you can go from depart to destination such that you will avoid 
     * Surface of course an effort must be made to reduce the distance to go round 
     * the surface
     * We assume that the SegmentLine formed by [depart,destination] intersects
     * With the surface
     * @param depart
     * @param destination
     * @return
     */
    public default IPoint avoidPoint(IPoint depart,IPoint destination){
        //LATER
        return null;
    }
}
