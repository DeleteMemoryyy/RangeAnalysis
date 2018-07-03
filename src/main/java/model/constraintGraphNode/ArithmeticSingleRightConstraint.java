package model.constraintGraphNode;

import model.instructioin.Expression;
import model.instructioin.SingleVariable;
import util.math.Interval;

import java.util.Map;

public class ArithmeticSingleRightConstraint extends ArithmeticSingleConstraint {
    public ArithmeticSingleRightConstraint(Expression expression, String operation, double value, SingleVariable singleVariable) {
        super(expression, operation, singleVariable, value);
    }

    @Override
    public Interval forward(Map<SingleVariable, Range> rangeMap) {
        Range range = rangeMap.get(singleVariable);
        if (range == null)
            return null;

        if ("+".equals(operation))
            return Interval.time(value, range.getInterval());
        else if ("-".equals(operation))
            return Interval.minus(value, range.getInterval());
        else if ("*".equals(operation))
            return Interval.time(value, range.getInterval());
        else if ("/".equals(operation))
            return Interval.divide(value, range.getInterval());

        return null;
    }

}
