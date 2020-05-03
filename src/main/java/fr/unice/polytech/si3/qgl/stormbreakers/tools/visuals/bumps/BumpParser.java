package fr.unice.polytech.si3.qgl.stormbreakers.tools.visuals.bumps;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fr.unice.polytech.si3.qgl.stormbreakers.math.metrics.Circle;
import fr.unice.polytech.si3.qgl.stormbreakers.math.metrics.Polygon;
import fr.unice.polytech.si3.qgl.stormbreakers.math.metrics.Position;
import fr.unice.polytech.si3.qgl.stormbreakers.math.metrics.Shape;
import fr.unice.polytech.si3.qgl.stormbreakers.data.objective.Checkpoint;
import fr.unice.polytech.si3.qgl.stormbreakers.data.ocean.Boat;
import fr.unice.polytech.si3.qgl.stormbreakers.data.ocean.Stream;
import fr.unice.polytech.si3.qgl.stormbreakers.data.ocean.OceanEntityType;
import fr.unice.polytech.si3.qgl.stormbreakers.data.ocean.Reef;
import fr.unice.polytech.si3.qgl.stormbreakers.math.Point2D;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class BumpParser {

    private final List<TupleRoundStepPosLines> positionsPerStep;

    private final List<VisibleEntity> entities;
    private final List<Position> positions;

    private static final String POSITION_KEY = "POSITION";
    private static final String ROUND_KEY = "ROUND";
    private static final String STEP_KEY = "STEP";

    private static final String CHECKPOINT_TOKEN = "checkpoint";
    static final String REEF_TOKEN = "reef";
    private static final String STREAM_TOKEN = "stream";
    private static final String SHIP_TOKEN = "ship";

    private static final String CIRCLE_TOKEN = "circle";
    private static final String POLYGON_TOKEN = "polygon";

    private static final String STORMBREAKERS_ID = "1325144078"; // CHANGE ME

    public BumpParser(Scanner scanner) {
        positionsPerStep = new ArrayList<>();

        entities = new ArrayList<>();
        positions = new ArrayList<>();

        preparse(scanner);

    }

    static class TupleRoundStepPosLines {
        final int round;
        final int step;
        final Position position;

        TupleRoundStepPosLines(int round, int step, Position position) {
            this.round = round;
            this.step = step;
            this.position = position;
        }
    }

    /**
     * Processes the input by parsing distinctly the bump header and the bump body
     * Delegates object reconstruction to corresponding methods
     * @param scanner the input scanner
     */
    private void preparse(Scanner scanner) {

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

    /**
     * Creates position element from string tokens
     * @param posComponents the string tokens
     * @return the reconstructed position
     */
    private Position buildPosition(List<String> posComponents) {
        List<Double> posComps = posComponents.stream().map(Double::parseDouble).collect(Collectors.toList());
        double xPos = posComps.get(0);
        double yPos = posComps.get(1);
        double orientationPos = posComps.get(2);
        return new Position(xPos,yPos,orientationPos);
    }

    /**
     * Creates an entity from two string token lines
     * @param firstEntityLine ENTITY id NAME SHAPE [SHAPE_PARAMS]
     * @param secondEntityLine POSITION id X Y ORIENTATION
     * @return the reconstructed entity
     */
    VisibleEntity parseEntity(String firstEntityLine, String secondEntityLine) {
        List<String> entityTokens = Arrays.asList(firstEntityLine.split(" "));
        String name = entityTokens.get(2);
        String shapeName = entityTokens.get(2 + 1);
        List<String> shapeParams = entityTokens.subList(4,entityTokens.size());

        List<String> positionComponents = Arrays.asList(secondEntityLine.split(" "));
        Position position = buildPosition(positionComponents.subList(2,4+1));

        Shape shape = buildShape(shapeName,shapeParams);
        return buildEntity(name,shape,position);
    }

    /**
     * Creates an entity from given parameters
     * @param name entity's type
     * @param shape entity's shape
     * @param position entity's position
     * @return the reconstructed entity
     */
    private VisibleEntity buildEntity(String name, Shape shape, Position position) {
        if (STREAM_TOKEN.equals(name)) {
            return new Stream(position, shape,150.0);
        }
        if (SHIP_TOKEN.equals(name)) {
            return new Boat(position,0,0,0,null,shape);
        }
        if (REEF_TOKEN.equals(name)) {
            return new Reef(position, shape);
        }
        if (CHECKPOINT_TOKEN.equals(name)) {
            return new Checkpoint(position,shape);
        }
        return null;
    }

    /**
     * Creates a shape from given parameters
     * @param shapeName shape's type
     * @param shapeParams shape's additional parameters
     * @return the reconstructed entity
     */
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

    /**
     * Retrieve parsed checkpoints
     */
    public List<Checkpoint> getCheckpoints() {
        return entities.stream().filter(ent ->  Checkpoint.BUMP_TOKEN.equals(ent.getType()))
                .map(ent -> (Checkpoint) ent)
                .collect(Collectors.toList());
    }

    /**
     * Retrieve parsed reefs
     */
    public List<Reef> getReefs() {
        return entities.stream().filter(ent ->  OceanEntityType.REEF.entityCode.equals(ent.getType()))
                .map(ent -> (Reef) ent)
                .collect(Collectors.toList());
    }

    /**
     * Retrieve parsed streams
     */
    public List<Stream> getStreams() {
        return entities.stream().filter(ent ->  OceanEntityType.STREAM.entityCode.equals(ent.getType()))
                .map(ent -> (Stream) ent)
                .collect(Collectors.toList());
    }

    /**
     * Retrieve parsed boat shape
     */
    public Shape getBoatShape() {
        return entities.stream().filter(ent -> OceanEntityType.BOAT.entityCode.equals(ent.getType()))
                .map(ent -> (Boat)ent)
                .map(Boat::getShape)
                .collect(Collectors.toList()).get(0);
    }

    /**
     * Retrieve parsed boat positions per round
     */
    public List<Position> getRoundPos() {
        return positionsPerStep.stream().filter(i -> i.step == 0).map(i -> i.position).collect(Collectors.toList());
    }

    /**
     * Retrieve parsed data as Json string
     * @return Json string
     * @throws JsonProcessingException if jackson's processing fails
     */
    public String getJsonData() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(MapperFeature.AUTO_DETECT_GETTERS);

        ObjectNode rootNode = mapper.createObjectNode();
        ObjectNode dataNode = rootNode.putObject("data");

        List<Checkpoint> checkpoints = getCheckpoints();
        JsonNode cpNode = mapper.valueToTree(checkpoints);

        List<Reef> reefs = getReefs();
        JsonNode reefsNode = mapper.valueToTree(reefs);

        dataNode.set("checkpoints", cpNode);
        dataNode.set("reefs", reefsNode);

        return mapper.writeValueAsString(dataNode);
    }
}
