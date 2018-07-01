package model;

import java.util.List;
import java.util.Map;

public class Function {
    private TranslateUnit translateUnit;
    private int startLine;
    private int endLine;
    private String simpleName;
    private int defNum;
    private int declUid;
    private int cGraphUid;
    private int symbolOrder;


    private List<String> argumentList;
    private Map<String, String> variableTable;
    private CFG cfg;

    public Function(TranslateUnit _translateUnit, int _startLine, int _endLine, String _simpleName, int _defNum, int _declUid, int _cGraphUid, int _symbolOrder) {
        translateUnit = _translateUnit;
        startLine = _startLine;
        endLine = _endLine;
        simpleName = _simpleName;
        defNum = _defNum;
        declUid = _declUid;
        cGraphUid = _cGraphUid;
        symbolOrder = _symbolOrder;
    }

    public TranslateUnit getTranslateUnit() {
        return translateUnit;
    }

    public int getStartLine() {
        return startLine;
    }

    public int getEndLine() {
        return endLine;
    }

    public String getSimpleName() {
        return simpleName;
    }

    public int getDefNum() {
        return defNum;
    }

    public int getDeclUid() {
        return declUid;
    }

    public int getcGraphUid() {
        return cGraphUid;
    }

    public int getSymbolOrder() {
        return symbolOrder;
    }

    public List<String> getArgumentList() {
        return argumentList;
    }

    public void setArgumentList(List<String> argumentList) {
        this.argumentList = argumentList;
    }

    public Map<String, String> getVariableTable() {
        return variableTable;
    }

    public void setVariableTable(Map<String, String> variableTable) {
        this.variableTable = variableTable;
    }

    public CFG getCfg() {
        return cfg;
    }

    public void setCfg(CFG cfg) {
        this.cfg = cfg;
    }

}
