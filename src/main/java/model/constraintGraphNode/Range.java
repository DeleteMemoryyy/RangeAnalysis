package model.constraintGraphNode;

import model.instructioin.SingleVariable;
import util.math.Interval;

public class Range extends ConstraintGraphNode {

    private final SingleVariable variable;
    private final Interval interval;

    public Range(SingleVariable variable, Interval interval) {
        super(variable.getName());

        this.variable = variable;
        this.interval = interval;
    }

    public SingleVariable getVariable() {
        return variable;
    }

    public Interval getInterval() {
        return interval;
    }

    public void update(Interval i) {
        interval.lower = i.lower;
        interval.upper = i.upper;
    }

    public void update(double lower, double upper) {
        interval.lower = lower;
        interval.upper = upper;
    }

    @Override
    public String toString() {
        return variable.getName() + ": " + interval;
    }
}
