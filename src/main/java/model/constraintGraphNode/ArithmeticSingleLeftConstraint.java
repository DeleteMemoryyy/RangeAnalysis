package model.constraintGraphNode;

import model.instructioin.Expression;
import model.instructioin.SingleVariable;
import util.math.Interval;

import java.util.Map;

public class ArithmeticSingleLeftConstraint extends ArithmeticSingleConstraint {
    public ArithmeticSingleLeftConstraint(Expression expression, String operation, SingleVariable singleVariable, double value) {
        super(expression, operation, singleVariable, value);
    }

    @Override
    public Interval forward(Map<SingleVariable, Range> rangeMap) {
        Range range = rangeMap.get(singleVariable);
        if (range == null)
            return null;

        if ("+".equals(operation))
            return Interval.plus(range.getInterval(), value);
        else if ("-".equals(operation))
            return Interval.minus(range.getInterval(), value);
        else if ("*".equals(operation))
            return Interval.time(range.getInterval(), value);
        else if ("/".equals(operation))
            return Interval.divide(range.getInterval(), value);

        return null;
    }
}
