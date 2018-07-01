package model.instructioin;

public class Assignment extends Expression {
    private SingleVariable leftExpr;
    private SingleExpression rightExpr;

    public Assignment(String name, SingleVariable leftExpr, SingleExpression rightExpr) {
        super(name);

        this.leftExpr = leftExpr;
        this.rightExpr = rightExpr;
    }

    public SingleVariable getLeftExpr() {
        return leftExpr;
    }

    public SingleExpression getRightExpr() {
        return rightExpr;
    }
}
