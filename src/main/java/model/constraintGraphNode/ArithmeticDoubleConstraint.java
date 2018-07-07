package model.constraintGraphNode;

import model.instructioin.Expression;
import model.instructioin.SingleVariable;
import util.math.Interval;

import java.util.Map;

public class ArithmeticDoubleConstraint extends ArithmeticConstraint {
    private SingleVariable expr1;
    private SingleVariable expr2;

    public SingleVariable getExpr1() {
        return this.expr1;
    }

    public void setExpr1(SingleVariable expr1) {
        if (this.expr1 != null)
            readyMap.remove(this.expr1);
        readyMap.put(expr1, false);

        this.expr1 = expr1;
    }

    public SingleVariable getExpr2() {
        return this.expr2;
    }

    public void setExpr2(SingleVariable expr2) {
        if (this.expr2 != null)
            readyMap.remove(this.expr2);
        readyMap.put(expr2, false);

        this.expr2 = expr2;
    }

    public ArithmeticDoubleConstraint(Expression expression, String operation, SingleVariable expr1, SingleVariable expr2) {
        super(expression, operation);

        this.expr1 = expr1;
        this.expr2 = expr2;

        readyMap.put(expr1, false);
        readyMap.put(expr2, false);
    }

    @Override
    public Interval forward(Map<SingleVariable, Range> rangeMap) {
        Range range1 = rangeMap.get(expr1);
        Range range2 = rangeMap.get(expr2);
        if (range1 == null || range2 == null)
            return null;

        if ("+".equals(operation))
            return Interval.plus(range1.getInterval(), range2.getInterval());
        else if ("-".equals(operation))
            return Interval.minus(range1.getInterval(), range2.getInterval());
        else if ("*".equals(operation))
            return Interval.time(range1.getInterval(), range2.getInterval());
        else if ("/".equals(operation))
            return Interval.divide(range1.getInterval(), range2.getInterval());

        return null;
    }

}
