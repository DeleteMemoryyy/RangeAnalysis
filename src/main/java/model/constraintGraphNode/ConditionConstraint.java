package model.constraintGraphNode;

import model.instructioin.Expression;
import model.instructioin.SingleVariable;
import util.math.ENumber;
import util.math.Interval;

import java.util.Map;

public class ConditionConstraint extends Constraint {
    private SingleVariable oriVariable;
    private Interval offsetInterval;
    private SingleVariable ftVariable;
    /**
     * 0 for not using ft(v)
     * -1 for added to lower
     * 1 for added to upper
     * 2 for using [ft(v), ft(v)]
     */
    private int ftPosition;

    public SingleVariable getOriVariable() {
        return this.oriVariable;
    }

    public Interval getOffsetInterval() {
        return offsetInterval;
    }

    public SingleVariable getFtVariable() {
        return ftVariable;
    }

    public int getFtPosition() {
        return ftPosition;
    }

    public ConditionConstraint(Expression expression, SingleVariable oriVariable, Interval offsetInterval, SingleVariable ftVariable, int ftPosition) {
        super(expression);

        this.oriVariable = oriVariable;
        this.offsetInterval = offsetInterval;
        this.ftVariable = ftVariable;
        this.ftPosition = ftPosition;
    }

    @Override
    public Interval forward(Map<SingleVariable, Range> rangeMap) {
        Range oriRange = rangeMap.get(oriVariable);
        if(oriRange == null)
            return null;
        if (ftPosition == 0)
            return Interval.intersection(oriRange.getInterval(), offsetInterval);

        Range ftRange = rangeMap.get(ftVariable);
        if (ftRange == null)
            return null;
        if (ftPosition == -1)
            return Interval.intersection(oriRange.getInterval(), new Interval(ENumber.plus(offsetInterval.lower, ftRange.getInterval().lower), offsetInterval.upper));
        else if (ftPosition == 1)
            return Interval.intersection(oriRange.getInterval(), new Interval(offsetInterval.lower, ENumber.plus(offsetInterval.upper, ftRange.getInterval().upper)));
        else if (ftPosition == 2)
            return Interval.intersection(oriRange.getInterval(),ftRange.getInterval());
        return null;
    }

}
