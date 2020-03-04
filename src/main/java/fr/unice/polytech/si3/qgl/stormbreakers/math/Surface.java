package fr.unice.polytech.si3.qgl.stormbreakers.math;

import java.util.ArrayList;
import java.util.List;

import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.IPoint;
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
            return new RectanglePositioned((Rectangle) this.getShape(), this).intersectsWith(lineSegment2D);
        } else {
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
     * @param depart     * @param destination
     * @return
     */
    public default List<IPoint> avoidHit(IPoint depart,IPoint destination){
        //LATER
    	int TAILLE_BATEAU=4;
    	
    	List<IPoint> list= new ArrayList<IPoint>();
    	
    	IPoint thisPoint= new Position(this.x(),this.y());
    	if(this.getShape().getType().equals("rectangle")) {

    		Rectangle r= (Rectangle)this.getShape();
    		Double heightRect =(r.getHeight()/2);
    		Double widthRect =(r.getWidth()/2 );
    		
    		
    		double orientation =this.getOrientation();
    		Point2D ptDepart= new Point2D(depart.x(),depart.y());
    		Point2D ptDest=new Point2D(destination.x(),destination.y());
    		Point2D ptThis = new Point2D(this.x(),this.y());
    		
    		Point2D PT_HG= new Point2D(ptThis.x()+heightRect+TAILLE_BATEAU,ptThis.y()-widthRect-TAILLE_BATEAU);
    		Point2D PT_HD= new Point2D(ptThis.x()+heightRect+TAILLE_BATEAU,ptThis.y()+widthRect+TAILLE_BATEAU);
    		Point2D PT_BG= new Point2D(ptThis.x()-heightRect-TAILLE_BATEAU,ptThis.y()-widthRect-TAILLE_BATEAU);
    		Point2D PT_BD= new Point2D(ptThis.x()-heightRect-TAILLE_BATEAU,ptThis.y()+widthRect+TAILLE_BATEAU);

    		ptDepart =ptDepart.getRotatedBy(-orientation);
    		ptDest =ptDest.getRotatedBy(-orientation);
    		ptThis= ptThis.getRotatedBy(-orientation); 	
    		
    		PT_HG= PT_HG.getRotatedBy(orientation);
    		PT_HD= PT_HD.getRotatedBy(orientation);
    		PT_BG= PT_BG.getRotatedBy(orientation);
    		PT_BD= PT_BD.getRotatedBy(orientation);
    		
    		if(ptThis.y()+(heightRect)<depart.y()) {
    				if(depart.x()>destination.x()) {
    					list.add(PT_HG);
    				}else {
    					list.add(PT_HD);
    				}
    		}else if(ptThis.y()-(heightRect)>depart.y()){
        		EquationDroite eq= new EquationDroite(ptDepart, ptDest);
        		double yCroisementCentreRect= eq.resolutionValY(ptThis.x());
        		if(yCroisementCentreRect>=ptThis.y()) {
        			
    				if(depart.x()>destination.x()) {
    					list.add(PT_HD);
    					list.add(PT_HG);
    				}else {
    					list.add(PT_HG);
    					list.add(PT_HD);
    				}
        		}else {
        			if(depart.x()>destination.x()) {
    					list.add(PT_BD);
    					list.add(PT_BG);
    				}else {
    					list.add(PT_BG);
    					list.add(PT_BD);
    				}
        		}
    		}else{
    			if(depart.x()>destination.x()) {
					list.add(PT_BG);
				}else {
					list.add(PT_BD);
				}
    		}
    	}else {
    		return getShape().avoidPoint(depart, destination, thisPoint);
    	}
    	
    	return list;
    }
}
