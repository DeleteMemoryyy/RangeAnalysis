package model;

import util.ConstraintGraphFactory;
import util.graph.DrawCFG;
import util.graph.DrawConstraintGraph;
import util.math.Interval;

import java.io.File;
import java.util.List;

public class TranslateUnit {
    private String name;
    private File file;

    private List<String> lines;
    private List<Function> functions;

    public TranslateUnit(String name, File file) {
        this.name = name;
        this.file = file;
    }

    public String getName() {
        return name;
    }

    public File getFile() {
        return file;
    }

    public List<String> getLines() {
        return lines;
    }

    public void setLines(List<String> lines) {
        this.lines = lines;
    }

    public String getLine(int index) {
        return lines.get(index);
    }

    public int getLineNum() {
        return lines.size();
    }

    public List<Function> getFunctions() {
        return functions;
    }

    public void setFunctions(List<Function> functions) {
        this.functions = functions;
    }

    public void drawAllCFG() {
        for (Function function : functions) {
            DrawCFG.print(function.getCFG(), name + "_" + function.getSimpleName());
        }
    }

    public void drawAllConstraintGraph() {
        for (Function function : functions) {
            DrawConstraintGraph.print(function.getConstraintGraph(), name + "_" + function.getSimpleName());
        }
    }

    public Interval getReturnRange(String functionName, List<Interval> argumentRange) {
        for (Function function : functions)
            if (functionName.equals(function.getSimpleName())) {
                ConstraintGraph constraintGraph = function.getConstraintGraph();
                if (constraintGraph == null) {
                    ConstraintGraphFactory.make(function);
                    constraintGraph = function.getConstraintGraph();
                }
                return constraintGraph.computeReturnRange(argumentRange);
            }
        return null;
    }

    @Override
    public String toString() {
        return "TranslateUnit: " + name;
    }
}
