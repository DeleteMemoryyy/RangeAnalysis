package model.instructioin;

public class IfGotoExpression extends Expression {
    private ConditionExpression conditionExpression;
    private String trueGotoBlockId;
    private String falseGotoBlockId;

    public IfGotoExpression(String name, ConditionExpression conditionExpression) {
        super(name);

        this.conditionExpression = conditionExpression;
    }

    public ConditionExpression getConditionExpression() {
        return conditionExpression;
    }

    public String getTrueGotoBlockId() {
        return trueGotoBlockId;
    }

    public void setTrueGotoBlockId(String trueGotoBlockId) {
        this.trueGotoBlockId = trueGotoBlockId;
    }

    public String getFalseGotoBlockId() {
        return falseGotoBlockId;
    }

    public void setFalseGotoBlockId(String falseGotoBlockId) {
        this.falseGotoBlockId = falseGotoBlockId;
    }
}
