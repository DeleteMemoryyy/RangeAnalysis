package model.constraintGraphNode;

import model.instructioin.Expression;
import model.instructioin.SingleVariable;
import util.math.Interval;

import java.util.Map;

public class ConditionConstraint extends Constraint {
    private boolean condition;
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

    public boolean getCondition() {
        return condition;
    }

    public void setCondition(boolean condition) {
        this.condition = condition;
    }

    public SingleVariable getOriVariable() {
        return this.oriVariable;
    }

    public void setOriVariable(SingleVariable oriVariable) {
        if (this.oriVariable != null)
            readyMap.remove(this.oriVariable);
        readyMap.put(oriVariable, false);

        this.oriVariable = oriVariable;
    }

    public Interval getOffsetInterval() {
        return offsetInterval;
    }

    public SingleVariable getFtVariable() {

        return ftVariable;
    }

    public void setFtVariable(SingleVariable ftVariable) {
//        if (this.ftVariable != null)
//            readyMap.remove(this.ftVariable);
//        if (ftVariable != null)
//            readyMap.put(ftVariable, false);

        this.ftVariable = ftVariable;
    }

    public int getFtPosition() {
        return ftPosition;
    }

    public ConditionConstraint(Expression expression, boolean condition, SingleVariable oriVariable, Interval offsetInterval, SingleVariable ftVariable, int ftPosition) {
        super(expression);
        this.condition = condition;

        this.oriVariable = oriVariable;
        this.offsetInterval = offsetInterval;
        this.ftVariable = ftVariable;
        this.ftPosition = ftPosition;

        readyMap.put(oriVariable, false);
//        if (ftVariable != null)
//            readyMap.put(ftVariable, false);
    }

    @Override
    public Interval forward(Map<SingleVariable, Range> rangeMap) {
        Range oriRange = rangeMap.get(oriVariable);
        if (oriRange == null)
            return null;
        if (ftPosition == 0)
            return Interval.intersection(oriRange.getInterval(), offsetInterval);

        Range ftRange = rangeMap.get(ftVariable);
        if (ftRange == null)
            return null;
        if (ftPosition == -1)
            return Interval.intersection(oriRange.getInterval(), new Interval(offsetInterval.lower + ftRange.getInterval().lower, offsetInterval.upper));
        else if (ftPosition == 1)
            return Interval.intersection(oriRange.getInterval(), new Interval(offsetInterval.lower, offsetInterval.upper + ftRange.getInterval().upper));
        else if (ftPosition == 2)
            return Interval.intersection(oriRange.getInterval(), ftRange.getInterval());
        return null;
    }

}
