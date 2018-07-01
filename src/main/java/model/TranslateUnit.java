package model;

import util.DrawCFG;

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
            DrawCFG.print(function.getCfg(), name + "_" + function.getSimpleName());
        }
    }

    @Override
    public String toString() {
        return "TranslateUnit: " + name;
    }
}
