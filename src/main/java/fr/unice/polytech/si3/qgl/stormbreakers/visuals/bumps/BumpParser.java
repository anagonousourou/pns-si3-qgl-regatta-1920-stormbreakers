package fr.unice.polytech.si3.qgl.stormbreakers.visuals.bumps;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Circle;
import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Polygon;
import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Position;
import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Shape;
import fr.unice.polytech.si3.qgl.stormbreakers.data.objective.Checkpoint;
import fr.unice.polytech.si3.qgl.stormbreakers.data.ocean.Boat;
import fr.unice.polytech.si3.qgl.stormbreakers.data.ocean.Courant;
import fr.unice.polytech.si3.qgl.stormbreakers.data.ocean.OceanEntityType;
import fr.unice.polytech.si3.qgl.stormbreakers.data.ocean.Recif;
import fr.unice.polytech.si3.qgl.stormbreakers.math.Point2D;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class BumpParser {

    private List<TupleRoundStepPosLines> positionsPerStep;

    private List<VisibleEntity> entities;
    private List<Position> positions;

    private static final String POSITION_KEY = "POSITION";
    private static final String ROUND_KEY = "ROUND";
    private static final String STEP_KEY = "STEP";

    private static final String CHECKPOINT_TOKEN = "checkpoint";
    private static final String REEF_TOKEN = "reef";
    private static final String STREAM_TOKEN = "stream";
    private static final String SHIP_TOKEN = "ship";

    private static final String CIRCLE_TOKEN = "circle";
    private static final String POLYGON_TOKEN = "polygon";

    private static final String STORMBREAKERS_ID = "1652807864";

    public BumpParser(Scanner scanner) {
        positionsPerStep = new ArrayList<>();

        entities = new ArrayList<>();
        positions = new ArrayList<>();

        preparse(scanner);

        System.out.println("Bump parsed");
    }

    static class TupleRoundStepPosLines {
        int round;
        int step;
        Position position;

        TupleRoundStepPosLines(int round, int step, Position position) {
            this.round = round;
            this.step = step;
            this.position = position;
        }
    }

    // TODO: 31/03/2020 Warning:(68, 18) Refactor this method to reduce its Cognitive Complexity from 18 to the 15 allowed.
    private void preparse(Scanner scanner) {
        System.out.println("Now parsing ...");

        boolean stillInHeader = true;
        String currentLine;

        // Scanning in header

        int lineNb = 0;
        String lastLine = "";
        while (scanner.hasNextLine() && stillInHeader) {
            currentLine = scanner.nextLine();

            if (lineNb % 2 == 0) {
                // Entity line -> save
                lastLine = currentLine;
            } else {
                // Position Line
                VisibleEntity entity = parseEntity(lastLine, currentLine);
                entities.add(entity);
            }

            lineNb++;

            // Stop whenever we hit a round
            if (currentLine.contains(ROUND_KEY)) {
                stillInHeader = false;
            }
        }

        // Scanning in body
        int round = 0;
        int step = 0;
        while (scanner.hasNextLine()) {
            currentLine = scanner.nextLine();
            String[] currentTokens = currentLine.split(" ");
            if (currentTokens.length >= 2) {
                if (ROUND_KEY.equals(currentTokens[0])) {
                    String roundString = currentLine.split(" ")[1];
                    round = Integer.parseInt(roundString);
                }
                if (STEP_KEY.equals(currentTokens[0])) {
                    String stepString = currentLine.split(" ")[1];
                    step = Integer.parseInt(stepString);
                }
            }
            if (currentLine.contains(POSITION_KEY + " " + STORMBREAKERS_ID)) {
                Position position = buildPosition(Arrays.asList(currentTokens).subList(2,4+1));
                positionsPerStep.add(new TupleRoundStepPosLines(round, step, position));
                positions.add(position);
            }
        }

    }

    private Position buildPosition(List<String> posComponents) {
        List<Double> posComps = posComponents.stream().map(Double::parseDouble).collect(Collectors.toList());
        double xPos = posComps.get(0);
        double yPos = posComps.get(1);
        double orientationPos = posComps.get(2);
        return new Position(xPos,yPos,orientationPos);
    }

    private VisibleEntity parseEntity(String firstEntityLine, String secondEntityLine) {
        // ENTITY id NAME SHAPE SHAPE_PARAMS
        // "ENTITY \\d+ (\\w+) (\\w+) ((?:\\w| |\\d)+)"
        List<String> entityTokens = Arrays.asList(firstEntityLine.split(" "));
        String name = entityTokens.get(2);
        String shapeName = entityTokens.get(2 + 1);
        List<String> shapeParams = entityTokens.subList(4,entityTokens.size());

        // POSITION id X Y ORIENTATION
        // "POSITION \\d+ (-*\\d+\\.\\d+) (-*\\d+\\.\\d+) (-*\\d+\\.\\d+)"
        List<String> positionComponents = Arrays.asList(secondEntityLine.split(" "));
        Position position = buildPosition(positionComponents.subList(2,4+1));
        
        Shape shape = buildShape(shapeName,shapeParams);
        return buildEntity(name,shape,position);
    }

    private VisibleEntity buildEntity(String name, Shape shape, Position position) {
        if (STREAM_TOKEN.equals(name)) {
            return new Courant(position, shape,150.0);
        }
        if (SHIP_TOKEN.equals(name)) {
            return new Boat(position,0,0,0,null,shape);
        }
        if (REEF_TOKEN.equals(name)) {
            return new Recif(position, shape);
        }
        if (CHECKPOINT_TOKEN.equals(name)) {
            return new Checkpoint(position,shape);
        }
        return null;
    }

    private Shape buildShape(String shapeName, List<String> shapeParams) {
        if (CIRCLE_TOKEN.equals(shapeName)) {
            // circle 150.0
            double radius = Double.parseDouble(shapeParams.get(0));
            return new Circle(radius);
        }
        if (POLYGON_TOKEN.equals(shapeName)) {
            // polygon -583.3333333333335 96.66666666666704 ...
            List<Double> polygonVertices = shapeParams.stream().map(Double::parseDouble).collect(Collectors.toList());
            List<Point2D> vertices = new ArrayList<>();
            for (int i=0; i<polygonVertices.size(); i+=2) {
                vertices.add(new Point2D(polygonVertices.get(i), polygonVertices.get(i+1)));
            }
            return new Polygon(0.0,vertices);
        }
        return null;
    }

    public List<Checkpoint> getCheckpoints() {
        return entities.stream().filter(ent ->  VisibleEntity.CHECKPOINT_TOKEN.equals(ent.getType()))
                .map(ent -> (Checkpoint) ent)
                .collect(Collectors.toList());
    }

    public List<Recif> getReefs() {
        return entities.stream().filter(ent ->  OceanEntityType.RECIF.entityCode.equals(ent.getType()))
                .map(ent -> (Recif) ent)
                .collect(Collectors.toList());
    }

    public List<Courant> getStreams() {
        return entities.stream().filter(ent ->  OceanEntityType.COURANT.entityCode.equals(ent.getType()))
                .map(ent -> (Courant) ent)
                .collect(Collectors.toList());
    }

    public Shape getBoatShape() {
        return entities.stream().filter(ent -> OceanEntityType.BOAT.entityCode.equals(ent.getType()))
                .map(ent -> (Boat)ent)
                .map(Boat::getShape)
                .collect(Collectors.toList()).get(0);
    }

    public List<Position> getRoundPos() {
        return positionsPerStep.stream().filter(i -> i.step == 0).map(i -> i.position).collect(Collectors.toList());
    }

    public String getJsonData() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        //mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.disable(MapperFeature.AUTO_DETECT_GETTERS);

        ObjectNode rootNode = mapper.createObjectNode();
        ObjectNode dataNode = rootNode.putObject("data");

        List<Checkpoint> checkpoints = getCheckpoints();
        JsonNode cpNode = mapper.valueToTree(checkpoints);

        List<Recif> reefs = getReefs();
        JsonNode reefsNode = mapper.valueToTree(reefs);

        dataNode.set("checkpoints", cpNode);
        dataNode.set("reefs", reefsNode);

        return mapper.writeValueAsString(dataNode);
    }
}
