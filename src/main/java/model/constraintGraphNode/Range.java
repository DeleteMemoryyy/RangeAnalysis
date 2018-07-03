package model.constraintGraphNode;

import model.instructioin.SingleVariable;
import util.math.ENumber;
import util.math.Interval;

public class Range extends ConstraintGraphNode {
    private final SingleVariable variable;
    private final Interval interval;

    public Range(SingleVariable variable, Interval interval) {
        super(variable.getName());

        this.variable = variable;
        this.interval = interval;
    }

    public Interval getInterval() {
        return interval;
    }

    public void update(Interval i) {
        interval.lower = new ENumber(i.lower);
        interval.upper = new ENumber(i.upper);
    }

    @Override
    public String toString() {
        return variable.getName() + ": " + interval;
    }
}
