package model.instructioin;

public class ConditionExpression extends Expression {
    private String compareOperation;
    private SingleExpression leftExpr;
    private SingleExpression rightExpr;

    public ConditionExpression(String name, String compareOperation, SingleExpression leftExpr, SingleExpression rightExpr) {
        super(name);

        this.compareOperation = compareOperation;
        this.leftExpr = leftExpr;
        this.rightExpr = rightExpr;
    }

    public String getCompareOperation() {
        return compareOperation;
    }

    public SingleExpression getLeftExpr() {
        return leftExpr;
    }

    public SingleExpression getRightExpr() {
        return rightExpr;
    }
}
