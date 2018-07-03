package model;

import model.constraintGraphNode.Constraint;
import model.constraintGraphNode.Range;
import model.instructioin.SingleVariable;

import java.util.Map;
import java.util.Set;

public class ConstraintGraph {
    private Function function;

    private Map<SingleVariable, Range> rangeMap;
    private Map<Constraint, Range> defMap;
    private Map<Range, Set<Constraint>> useMap;

    public ConstraintGraph(Function function) {
        this.function = function;
    }

    public Function getFunction() {
        return function;
    }

    public Map<SingleVariable, Range> getRangeMap() {
        return rangeMap;
    }

    public void setRangeMap(Map<SingleVariable, Range> rangeMap) {
        this.rangeMap = rangeMap;
    }

    public Map<Constraint, Range> getDefMap() {
        return defMap;
    }

    public void setDefMap(Map<Constraint, Range> defMap) {
        this.defMap = defMap;
    }

    public Map<Range, Set<Constraint>> getUseMap() {
        return useMap;
    }

    public void setUseMap(Map<Range, Set<Constraint>> useMap) {
        this.useMap = useMap;
    }

    public Set<Constraint> getConstraintSet() {
        return defMap.keySet();
    }

    public Set<Range> getRangeSet() {
        return useMap.keySet();
    }

    public Range getDef(Constraint constraint) {
        return defMap.get(constraint);
    }

    public Set<Constraint> getUses(Range range) {
        return useMap.get(range);
    }

}
