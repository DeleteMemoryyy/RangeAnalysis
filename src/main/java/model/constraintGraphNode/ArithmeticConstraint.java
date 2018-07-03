package model.constraintGraphNode;

import model.instructioin.Expression;

public abstract class ArithmeticConstraint extends Constraint {
    protected String operation;

    public String getOperation() {
        return this.operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public ArithmeticConstraint(Expression expression, String operation) {
        super(expression);

        this.operation = operation;
    }
}
