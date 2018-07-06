package model.constraintGraphNode;

import model.instructioin.Expression;
import model.instructioin.SingleVariable;
import util.math.Interval;

import java.util.Map;

public class AssignmentConstantConstraint extends Constraint {
    public double getValue() {
        return this.value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    private double value;

    public AssignmentConstantConstraint(Expression expression, double value) {
        super(expression);

        this.value = value;
    }

    @Override
    public Interval forward(Map<SingleVariable, Range> rangeMap) {
        return new Interval(value, value);
    }

}
