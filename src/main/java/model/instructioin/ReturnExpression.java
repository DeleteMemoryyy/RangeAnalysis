package model.instructioin;

public class ReturnExpression extends Expression {
    private SingleExpression retExpr;

    public ReturnExpression(String name, SingleExpression retExpr) {
        super(name);

        this.retExpr = retExpr;
    }

    public SingleExpression getRetExpr() {
        return retExpr;
    }
}
