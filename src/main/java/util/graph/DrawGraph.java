package util.graph;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

public abstract class DrawGraph {

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
    protected static String DOT_FILE_PATH = "dot/";

    protected String formatedName;

    protected void printGraph() {

        File dotDir = new File(DOT_FILE_PATH);
        dotDir.mkdirs();
        String imagePath = DOT_FILE_PATH + "../img/";
        File imageDir = new File(imagePath);
        imageDir.mkdirs();

        String dotFile = DOT_FILE_PATH + formatedName + ".dot";

        PrintStream stream;
        try {
            stream = new PrintStream(new FileOutputStream(dotFile));
            stream.println("digraph " + formatedName + " { ");
            stream.println("\tlabel=" + formatedName);
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

    protected abstract void visitAllNodes(PrintStream stream, boolean printEdge);

    protected abstract void printAllNodes(PrintStream stream);

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

}
