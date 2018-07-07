package model.constraintGraphNode;

import model.TranslateUnit;
import model.instructioin.ConstantExpression;
import model.instructioin.Expression;
import model.instructioin.SingleExpression;
import model.instructioin.SingleVariable;
import util.math.Interval;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AssignmentFunctionCallConstraint extends Constraint {
    private String functionSimpleName;
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

        readyMap.clear();
        for (SingleExpression singleExpression : actualArguments) {
            if (singleExpression instanceof SingleVariable) {
                SingleVariable variable = (SingleVariable) singleExpression;
                readyMap.put(variable, false);
            }
        }
    }

    public AssignmentFunctionCallConstraint(Expression expression, String functionSimpleName, List<SingleExpression> actualArguments, TranslateUnit translateUnit) {
        super(expression);

        this.functionSimpleName = functionSimpleName;
        this.actualArguments = actualArguments;
        this.translateUnit = translateUnit;

        for (SingleExpression singleExpression : actualArguments) {
            if (singleExpression instanceof SingleVariable) {
                SingleVariable variable = (SingleVariable) singleExpression;
                readyMap.put(variable, false);
            }
        }
    }

    @Override
    public Interval forward(Map<SingleVariable, Range> rangeMap) {
        List<Interval> argumentRange = new ArrayList<>();
        for (SingleExpression expression : actualArguments) {
            if (expression instanceof ConstantExpression) {
                double value = ((ConstantExpression) expression).doubleValue();
                argumentRange.add(new Interval(value, value));
            } else if (expression instanceof SingleVariable) {
                Range range = rangeMap.get(expression);
                if (range == null)
                    return null;
                argumentRange.add(range.getInterval());
            } else
                return null;
        }

        return translateUnit.getReturnRange(functionSimpleName, argumentRange, true);
    }
}
