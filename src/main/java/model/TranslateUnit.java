package model;

import java.io.File;
import java.util.List;

public class TranslateUnit {
    private String name;
    private File file;
    private List<String> lines;
    private List<Function> functions;

    public TranslateUnit(String _name, File _file, List<String> _lines) {
        name = _name;
        file = _file;
        lines = _lines;
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

    public String getLine(int index) {
        return lines.get(index);
    }

    public int getLineNum(){
        return lines.size();
    }

    public List<Function> getFunctions() {
        return functions;
    }

    public void setFunctions(List<Function> functions) {
        this.functions = functions;
    }
}
