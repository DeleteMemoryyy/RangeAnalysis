package model.constraintGraphNode;

import model.instructioin.Expression;
import model.instructioin.SingleVariable;

public abstract class ArithmeticSingleConstraint extends ArithmeticConstraint {
    protected SingleVariable singleVariable;
    protected double value;

    public SingleVariable getSingleVariable() {
        return this.singleVariable;
    }

    public void setSingleVariable(SingleVariable singleVariable) {
        this.singleVariable = singleVariable;
    }

    public ArithmeticSingleConstraint(Expression expression, String operation, SingleVariable singleVariable, double value) {
        super(expression, operation);

        this.singleVariable = singleVariable;
        this.value = value;
    }
}
