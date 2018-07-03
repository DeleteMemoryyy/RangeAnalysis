package util.graph;

import model.ConstraintGraph;
import model.constraintGraphNode.Constraint;
import model.constraintGraphNode.ConstraintGraphNode;
import model.constraintGraphNode.Range;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.HashSet;

public class DrawConstraintGraph extends DrawGraph {
    private static DrawConstraintGraph instance;

    // color style config
    private static final String C_NODE_CONSTRAINT = "lightblue";
    private static final String C_FILL_NODE_CONSTRAINT = "lightblue";
    private static final String C_NODE_RANGE = "moccasin";
    private static final String C_FILL_NODE_RANGE = "moccasin";

    private static final String C_EDGE_CONSTRAINT_TO_RANGE = "steelblue";
    private static final String C_EDGE_RANGE_TO_CONSTRAINT = "tan";

    protected ConstraintGraph constraintGraph;
    protected int nextID;
    protected HashMap<ConstraintGraphNode, String> nodeHash;
    protected HashSet<ConstraintGraphNode> nodeVisited;

    protected DrawConstraintGraph() {
    }

    protected void init(ConstraintGraph constraintGraph, String fileName) {
        this.constraintGraph = constraintGraph;
        nextID = 0;
        nodeHash = new HashMap<>();
        nodeVisited = new HashSet<>();
        formatedName = formatFileName(fileName) + "_ConstraintGraph";
    }

    public static void print(ConstraintGraph constraintGraph, String fileName) {
        if (instance == null)
            instance = new DrawConstraintGraph();

        instance.init(constraintGraph, fileName);
        instance.printGraph();
    }

    protected void visitAllNodes(PrintStream stream, boolean printEdge) {
        for (Constraint node : constraintGraph.getConstraintSet()) {
            if (node == null || nodeVisited.contains(node))
                return;
            nodeVisited.add(node);
            String curID = getID(node);
            Range succNode = constraintGraph.getDef(node);
            String succID = getID(succNode);
            if (printEdge) {
                stream.print("\t\t" + curID + " -> " + succID);
                stream.print(" [color=" + C_EDGE_CONSTRAINT_TO_RANGE + "]");
                stream.println();
            }
        }

        for (Range node : constraintGraph.getRangeSet()) {
            if (node == null || nodeVisited.contains(node))
                return;
            nodeVisited.add(node);
            String curID = getID(node);
            for (Constraint succNode : constraintGraph.getUses(node)) {
                String succID = getID(succNode);
                if (printEdge) {
                    stream.print("\t\t" + curID + " -> " + succID);
                    stream.print(" [color=" + C_EDGE_RANGE_TO_CONSTRAINT + "]");
                    stream.println();
                }
            }
        }
    }

    /**
     * need to used after calling visitAllNodes
     */
    protected void printAllNodes(PrintStream stream) {
        for (Constraint node : constraintGraph.getConstraintSet()) {
            stream.print("\t" + getID(node) + " [");
            HashMap<String, String> attributes = getNodeAttributes(node);
            boolean printComma = false;
            for (String key : attributes.keySet()) {
                if (printComma)
                    stream.print(", ");
                else
                    printComma = true;
                stream.print(key + "=" + attributes.get(key));
            }
            stream.println("]");
        }

        for (Range node : constraintGraph.getRangeSet()) {
            stream.print("\t" + getID(node) + " [");
            HashMap<String, String> attributes = getNodeAttributes(node);
            boolean printComma = false;
            for (String key : attributes.keySet()) {
                if (printComma)
                    stream.print(", ");
                else
                    printComma = true;
                stream.print(key + "=" + attributes.get(key));
            }
            stream.println("]");
        }
    }

    protected HashMap<String, String> getNodeAttributes(ConstraintGraphNode node) {
        HashMap<String, String> attributes = new HashMap<>();

        String label = "\"{ " + escapeString(node.toString()) + " }\"";
        String color;
        String fillcolor;
        if (node instanceof Constraint) {
            color = C_NODE_CONSTRAINT;
            fillcolor = C_FILL_NODE_CONSTRAINT;
        } else {
            color = C_NODE_RANGE;
            fillcolor = C_FILL_NODE_RANGE;
        }
        attributes.put("label", label);
        attributes.put("color", color);
        attributes.put("shape", "Mrecord");
        attributes.put("style", "filled");
        attributes.put("fillcolor", fillcolor);
        return attributes;
    }

    protected String getID(ConstraintGraphNode node) {
        String id = nodeHash.get(node);
        if (id == null) {
            id = "Node_" + nextID++;
            nodeHash.put(node, id);
        }
        return id;
    }
}
