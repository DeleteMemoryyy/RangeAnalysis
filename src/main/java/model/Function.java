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
    private Map<String, String> transTable;
    private CFG cfg;
    private ConstraintGraph constraintGraph;

    public Function(TranslateUnit translateUnit, int startLine, int endLine, String simpleName, int defNum, int declUid, int cGraphUid, int symbolOrder) {
        this.translateUnit = translateUnit;
        this.startLine = startLine;
        this.endLine = endLine;
        this.simpleName = simpleName;
        this.defNum = defNum;
        this.declUid = declUid;
        this.cGraphUid = cGraphUid;
        this.symbolOrder = symbolOrder;
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

    public Map<String, String> getTransTable() {
        return transTable;
    }

    public void setTransTable(Map<String, String> transTable) {
        this.transTable = transTable;
    }

    public CFG getCFG() {
        return cfg;
    }

    public void setCFG(CFG cfg) {
        this.cfg = cfg;
    }

    public ConstraintGraph getConstraintGraph() {
        return constraintGraph;
    }

    public void setConstraintGraph(ConstraintGraph constraintGraph) {
        this.constraintGraph = constraintGraph;
    }

    public int getArgumentNum() {
        return argumentList.size();
    }

    @Override
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append(simpleName);
        buf.append("(");
        int argumentNum = getArgumentNum();
        for (int i = 0; i < argumentNum; ++i) {
            buf.append(argumentList.get(i));
            if (i < argumentNum - 1)
                buf.append(", ");
        }
        buf.append(")");

        return buf.toString();
    }

}
