package model.instructioin;

public class ArithmeticExpression extends SingleExpression {
    private String operation;
    private SingleExpression leftExpr;
    private SingleExpression rightExpr;

    public ArithmeticExpression(String name, String operation, SingleExpression leftExpr, SingleExpression rightExpr) {
        super(name);

        this.operation = operation;
        this.leftExpr = leftExpr;
        this.rightExpr = rightExpr;
    }

    public String getOperation() {
        return operation;
    }

    public SingleExpression getLeftExpr() {
        return leftExpr;
    }

    public SingleExpression getRightExpr() {
        return rightExpr;
    }
}
