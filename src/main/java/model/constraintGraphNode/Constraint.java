package model.constraintGraphNode;

import model.instructioin.Expression;
import model.instructioin.SingleVariable;
import util.math.Interval;

import java.util.HashMap;
import java.util.Map;

abstract public class Constraint extends ConstraintGraphNode {
    protected Expression expression;
    protected Map<SingleVariable, Boolean> readyMap;

    public Constraint(Expression expression) {
        super(expression.getName());

        this.expression = expression;
        readyMap = new HashMap<>();
    }

    abstract public Interval forward(Map<SingleVariable, Range> rangeMap);

    public Expression getExpression() {
        return expression;
    }

    public void setExpression(Expression expression) {
        this.expression = expression;
    }

    @Override
    public String toString() {
        return name;
    }

    public Map<SingleVariable, Boolean> getReadyMap() {
        return readyMap;
    }

    public void setReadyMap(Map<SingleVariable, Boolean> readyMap) {
        this.readyMap = readyMap;
    }

    public boolean isReady(SingleVariable variable) {
        Boolean ready = readyMap.get(variable);
        if (ready == null)
            return false;
        return ready;
    }

    public int getReadyCount() {
        int count = 0;
        for (SingleVariable variable : readyMap.keySet()) {
            if (readyMap.get(variable))
                count++;
        }
        return count;
    }

    public boolean hasReadyVariable() {
        for (SingleVariable variable : readyMap.keySet())
            if (readyMap.get(variable))
                return true;
        return false;
    }

    public boolean isAllReady() {
        return !readyMap.values().contains(Boolean.FALSE);
    }

    public void setReady(SingleVariable variable) {
        readyMap.put(variable, true);
    }

    public void setUnready(SingleVariable variable) {
        readyMap.put(variable, false);
    }

    public void setAllUnready() {
        for (SingleVariable variable : readyMap.keySet())
            readyMap.replace(variable, false);
    }
}
