package model;

import model.constraintGraphNode.Constraint;
import model.constraintGraphNode.ConstraintGraphNode;
import model.constraintGraphNode.Range;
import model.instructioin.SingleVariable;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class ConstraintGraph {
    private Function function;

    private Map<SingleVariable, Range> rangeMap;
    private Map<Constraint, Range> defMap;
    private Map<Range, Set<Constraint>> useMap;
    private Map<Range, Constraint> revDefMap;
    private Map<Constraint, Set<Range>> revUseMap;
    private List<Set<ConstraintGraphNode>> SCCSetList;

    private Map<ConstraintGraphNode, Integer> SCCLabel;

    private Range returnPoint;
    private Set<ConstraintGraphNode> entryPoints;

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

    public Map<Range, Constraint> getRevDefMap() {
        return revDefMap;
    }

    public void setRevDefMap(Map<Range, Constraint> revDefMap) {
        this.revDefMap = revDefMap;
    }

    public Map<Constraint, Set<Range>> getRevUseMap() {
        return revUseMap;
    }

    public void setRevUseMap(Map<Constraint, Set<Range>> revUseMap) {
        this.revUseMap = revUseMap;
    }

    public Set<ConstraintGraphNode> getSCCSet(int index) {
        return SCCSetList.get(index);
    }

    public List<Set<ConstraintGraphNode>> getSCCSetList() {
        return SCCSetList;
    }

    public void setSCCSetList(List<Set<ConstraintGraphNode>> SCCSetList) {
        this.SCCSetList = SCCSetList;
    }

    public Range getReturnPoint() {
        return returnPoint;
    }

    public void setReturnPoint(Range returnPoint) {
        this.returnPoint = returnPoint;
    }

    public Set<ConstraintGraphNode> getEntryPoints() {
        return entryPoints;
    }

    public void setEntryPoints(Set<ConstraintGraphNode> entryPoints) {
        this.entryPoints = entryPoints;
    }

    public Integer getNodeSCCLabel(ConstraintGraphNode node) {
        return SCCLabel.get(node);
    }

    public Map<ConstraintGraphNode, Integer> getSCCLabel() {
        return this.SCCLabel;
    }

    public void setSCCLabel(Map<ConstraintGraphNode, Integer> SCCLabel) {
        this.SCCLabel = SCCLabel;
    }
}
