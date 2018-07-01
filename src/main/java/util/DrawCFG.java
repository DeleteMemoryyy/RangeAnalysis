package util;

import model.CFG;
import model.block.BasicBlock;
import model.block.EntryBlock;
import model.block.ExitBlock;
import model.instructioin.Expression;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class DrawCFG {
    private static DrawCFG instance;

    /**
     * if print svg images using graphviz
     */
    protected final boolean DOT_TO_IMAGE = true;
    /**
     * dot (graphviz) path
     */
    protected static String DOT_BIN_PATH = "dot";
    /**
     * dot file output path, and svg files will be stored in DOT_FILE_PATH/img/
     */
    protected static String DOT_FILE_PATH = "graph/";

    // color style config
    private static final String C_NODE_NORMAL = "black";
    private static final String C_NODE_ENTRY = "orange";
    private static final String C_NODE_EXIT = "dodgerblue3";
    private static final String C_NODE_TERMINATED = "blueviolet";
    private static final String C_EDGE_NORMAL = "black";
    private static final String C_EDGE_TRUE = "crimson";
    private static final String C_EDGE_FALSE = "teal";

    protected CFG cfg;
    protected String formatedName;
    protected int nextID;
    protected HashMap<BasicBlock, String> nodeHash;
    protected HashSet<BasicBlock> nodeVisited;
    protected HashSet<BasicBlock> nodeMerged;
    protected HashSet<BasicBlock> nodeMergedHead;

    protected DrawCFG() {
    }

    protected void init(CFG cfg, String fileName) {
        this.cfg = cfg;
        nextID = 0;
        nodeHash = new HashMap<>();
        nodeVisited = new HashSet<>();
        formatedName = formatFileName(fileName) + "_graph";
    }

    public static void print(CFG cfg, String fileName) {
        if (instance == null)
            instance = new DrawCFG();

        instance.init(cfg, fileName);
        instance.printGraph();
    }

    protected void printGraph() {

        File dotDir = new File(DOT_FILE_PATH);
        dotDir.mkdirs();
        String imagePath = DOT_FILE_PATH + "img/";
        File imageDir = new File(imagePath);
        if (!imageDir.exists())
            imageDir.mkdirs();

        String dotFile = DOT_FILE_PATH + formatedName + ".dot";

        PrintStream stream;
        try {
            stream = new PrintStream(new FileOutputStream(dotFile));
            stream.println("digraph " + formatedName + " { ");
            stream.println("\tcenter=true");
            stream.println("\tlabelloc=top");
            stream.println("\tfontname=Arial");
            stream.println("\tfontsize=12");
            stream.println("\tedge[fontname=Arial, fontsize=10]");
            stream.println("\tnode[fontname=Arial, fontsize=9]");
            stream.println();

            // print graph
            visitAllNodes(stream, true);
            stream.println();
            printAllNodes(stream);

            stream.println("}");
            stream.close();

            if (DOT_TO_IMAGE) {
                String[] cmds = {DOT_BIN_PATH, dotFile, "-Tsvg", "-o", imagePath + formatedName + ".svg"};
                Runtime.getRuntime().exec(cmds);
            }
        } catch (IOException iex) {
            System.err.println("Write dot file faild: " + dotFile);
            iex.printStackTrace();
        }
    }

    protected void visitAllNodes(PrintStream stream, boolean printEdge) {
        Iterator<BasicBlock> ite = cfg.getBlocks().iterator();
        while (ite.hasNext())
            visitNode(ite.next(), stream, printEdge);
    }

    protected void visitNode(BasicBlock node, PrintStream stream, boolean printEdge) {
        if (node == null || nodeVisited.contains(node))
            return;
        nodeVisited.add(node);
        String curID = getID(node);

        List<BasicBlock> succNodes = cfg.getSuccNodes(node);
        int succCount = succNodes.size();
        for (int i = 0; i < succCount; ++i) {
            BasicBlock succNode = succNodes.get(i);
            String succID = getID(succNode);
            if (printEdge) {
                stream.print("\t\t" + curID + " -> " + succID);
                if (succCount == 2) {
                    if (i == 0)
                        stream.print(" [color=" + C_EDGE_TRUE + "]");
                    else
                        stream.print(" [color=" + C_EDGE_FALSE + "]");

                } else
                    stream.print(" [color=" + C_EDGE_NORMAL + "]");
                stream.println();
            }
            visitNode(succNode, stream, printEdge);
        }

    }

    /**
     * need to used after calling visitAllNodes
     */
    protected void printAllNodes(PrintStream stream) {
        for (BasicBlock node : nodeVisited) {
            printNode(node, stream);
        }
    }

    protected void printNode(BasicBlock node, PrintStream stream) {
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

    protected HashMap<String, String> getNodeAttributes(BasicBlock node) {
        HashMap<String, String> attributes = new HashMap<>();

        String label = "\"{ " + getNodeLabel(node) + " }\"";
        String color = C_NODE_NORMAL;
        if (node instanceof EntryBlock) {
            color = C_NODE_ENTRY;
        } else if (node instanceof ExitBlock) {
            color = C_NODE_EXIT;
        } else if (cfg.getSuccNodeCount(node) == 0) {
            color = C_NODE_TERMINATED;
            if (label.equals("\"{ " + "(empty block)" + " }\""))
                label = "\"{ Terminated }\"";
        }

        attributes.put("label", label);
        attributes.put("color", color);
        attributes.put("shape", "Mrecord");
        attributes.put("style", "filled");
        attributes.put("fillcolor", "mintcream");
        return attributes;
    }

    protected String getNodeLabel(BasicBlock node) {
        StringBuffer buf = new StringBuffer();
        buf.append(node.getName());

        List<Expression> instList = node.getInstructionList();
        if (instList != null) {
            Iterator<Expression> instIterator = instList.iterator();
            while (instIterator.hasNext()) {
                buf.append('|');
                buf.append(instIterator.next());
            }
        }

        return escapeString(buf.toString());
    }

    /**
     * some characters can not be used in Windows file name
     */
    protected static String formatFileName(String fileName) {
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < fileName.length(); ++i) {
            char c = fileName.charAt(i);
            switch (c) {
                case '?':
                    result.append("_");
                    break;
                case '*':
                    result.append("_");
                    break;
                case '/':
                    result.append("_");
                    break;
                case '\\':
                    result.append("_");
                    break;
                case '<':
                    result.append("_");
                    break;
                case '>':
                    result.append("_");
                    break;
                case ':':
                    result.append("_");
                    break;
                case '\"':
                    result.append("_");
                    break;
                case '|':
                    result.append("_");
                    break;
                default:
                    result.append(c);
            }
        }
        return result.toString();
    }

    /**
     * some characters can not be used in HTML label string directly
     */
    protected static String escapeString(String data) {
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < data.length(); ++i) {
            char c = data.charAt(i);
            switch (c) {
                case '\n':
                    break;
                case '\\':
                    result.append("\\\\");
                    break;
                case '\"':
                    result.append("\\\"");
                    break;
                case '[':
                    result.append("\\[");
                    break;
                case ']':
                    result.append("\\]");
                    break;
                case '>':
                    result.append("\\>");
                    break;
                case '<':
                    result.append("\\<");
                    break;
                default:
                    result.append(c);
            }
        }
        return result.toString();
    }

    protected String getID(BasicBlock node) {
        String id = nodeHash.get(node);
        if (id == null) {
            id = "Node_" + nextID++;
            nodeHash.put(node, id);
        }
        return id;
    }
}
