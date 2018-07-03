package model.constraintGraphNode;

import model.TranslateUnit;
import model.instructioin.ConstantExpression;
import model.instructioin.Expression;
import model.instructioin.SingleExpression;
import model.instructioin.SingleVariable;
import util.math.ENumber;
import util.math.Interval;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AssignmentFunctionCallConstraint extends Constraint {
    private List<SingleExpression> actualArguments;
    private TranslateUnit translateUnit;

    public List<SingleExpression> getActualArguments() {
        return this.actualArguments;
    }

    public void setActualArguments(List<SingleExpression> actualArguments) {
        this.actualArguments = actualArguments;
    }

    public TranslateUnit getTranslateUnit() {
        return this.translateUnit;
    }

    public void setTranslateUnit(TranslateUnit translateUnit) {
        this.translateUnit = translateUnit;
    }

    public AssignmentFunctionCallConstraint(Expression expression, List<SingleExpression> actualArguments, TranslateUnit translateUnit) {
        super(expression);

        this.actualArguments = actualArguments;
        this.translateUnit = translateUnit;
    }

    @Override
    public Interval forward(Map<SingleVariable, Range> rangeMap) {
        List<Interval> argumentRange = new ArrayList<>();
        for (SingleExpression expression : actualArguments) {
            if (expression instanceof ConstantExpression) {
                double value = ((ConstantExpression) expression).doubleValue();
                argumentRange.add(new Interval(new ENumber(value), new ENumber(value)));
            } else if (expression instanceof SingleVariable) {
                Range range = rangeMap.get(expression);
                if (range == null)
                    return null;
                return range.getInterval();
            } else
                return null;
        }

        return translateUnit.getReturnRange(argumentRange);
    }
}
