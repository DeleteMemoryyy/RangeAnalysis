package model.constraintGraphNode;

import model.instructioin.Expression;
import model.instructioin.SingleVariable;
import util.math.Interval;

import java.util.Map;

public class PHIConstraint extends Constraint {
    private SingleVariable fromExpr_1;
    private SingleVariable fromExpr_2;

    public SingleVariable getFromExpr_1() {
        return this.fromExpr_1;
    }

    public void setFromExpr_1(SingleVariable fromExpr_1) {
        if (this.fromExpr_1 != null)
            readyMap.remove(this.fromExpr_1);
        readyMap.put(fromExpr_1, false);

        this.fromExpr_1 = fromExpr_1;
    }

    public SingleVariable getFromExpr_2() {
        return this.fromExpr_2;
    }

    public void setFromExpr_2(SingleVariable fromExpr_2) {
        if (this.fromExpr_2 != null)
            readyMap.remove(this.fromExpr_2);
        readyMap.put(fromExpr_2, false);

        this.fromExpr_2 = fromExpr_2;
    }

    public PHIConstraint(Expression expression, SingleVariable fromExpr_1, SingleVariable fromExpr_2) {
        super(expression);

        this.fromExpr_1 = fromExpr_1;
        this.fromExpr_2 = fromExpr_2;

        readyMap.put(fromExpr_1, false);
        readyMap.put(fromExpr_2, false);
    }

    @Override
    public Interval forward(Map<SingleVariable, Range> rangeMap) {
        Range range1 = rangeMap.get(fromExpr_1);
        Range range2 = rangeMap.get(fromExpr_2);
        if (range1 == null || range2 == null)
            return null;

        return Interval.union(range1.getInterval(), range2.getInterval());
    }


}
