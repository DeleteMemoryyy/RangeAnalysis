package model.constraintGraphNode;

import model.instructioin.Expression;
import model.instructioin.SingleVariable;
import util.math.Interval;

import java.util.Map;

abstract public class Constraint extends ConstraintGraphNode {
    private Expression expression;

    public Constraint(Expression expression) {
        super(expression.getName());

        this.expression = expression;
    }

    abstract public Interval forward(Map<SingleVariable, Range> rangeMap);

    public Expression getExpression() {
        return expression;
    }

    public void setExpression(Expression expression) {
        this.expression = expression;
    }

    @Override
    public String toString() {
        return name;
    }
}
