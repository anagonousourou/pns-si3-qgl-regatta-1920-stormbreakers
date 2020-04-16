package fr.unice.polytech.si3.qgl.stormbreakers.visuals.draw.drawings;

import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Position;

import java.awt.*;
import java.awt.geom.Path2D;
import java.util.List;

public class PathDrawing extends Drawing {
    private final List<Position> positions;

    public PathDrawing(List<Position> positions) {
        super(positions.get(0),1);
        positions.stream().map(p -> p.distanceTo(positions.get(0))).sorted().findFirst().ifPresent( this::setSize );
        this.positions = positions;
    }

    @Override
    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        Path2D.Double path = new Path2D.Double();

        Position start = positions.get(0);
        path.moveTo(start.x(),start.y());
        for (Position position : positions) {
            path.lineTo(position.x(),position.y());
        }

        g2d.draw(path);
        g2d.dispose();
    }
}
