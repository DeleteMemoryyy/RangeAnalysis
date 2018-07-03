package model.constraintGraphNode;

import model.instructioin.Expression;
import model.instructioin.SingleVariable;
import util.math.Interval;

import java.util.Map;

public class AssignmentVariableConstraint extends Constraint {
    private SingleVariable singleVariable;

    public SingleVariable getSingleVariable() {
        return this.singleVariable;
    }

    public void setSingleVariable(SingleVariable singleVariable) {
        this.singleVariable = singleVariable;
    }

    public AssignmentVariableConstraint(Expression expression, SingleVariable singleVariable) {
        super(expression);

        this.singleVariable = singleVariable;
    }

    @Override
    public Interval forward(Map<SingleVariable, Range> rangeMap) {
        Range range = rangeMap.get(singleVariable);
        if (range == null)
            return null;

        return range.getInterval();
    }

}
