package model;

import model.constraintGraphNode.Constraint;
import model.constraintGraphNode.ConstraintGraphNode;
import model.constraintGraphNode.PHIConstraint;
import model.constraintGraphNode.Range;
import model.instructioin.SingleVariable;
import util.math.Interval;

import java.util.*;

public class ConstraintGraph {
    private Function function;

    private Map<SingleVariable, Range> rangeMap;
    private Map<Constraint, Range> defMap;
    private Map<Range, Set<Constraint>> useMap;
    private Map<Range, Constraint> revDefMap;
    private Map<Constraint, Set<Range>> revUseMap;
    private List<Set<ConstraintGraphNode>> SCCSetList;
    private List<Set<Constraint>> SCCIncomingList;

    private Map<ConstraintGraphNode, Integer> SCCLabel;
    private Map<Integer, Boolean> SCCStatus;

    private Range returnPoint;
    private Set<Constraint> entryPoints;

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
//        Set<Range> ret = new HashSet<>();
//        ret.addAll(rangeMap.values());
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

    public Set<Constraint> getSCCIncomingNodes(int index) {
        return SCCIncomingList.get(index);
    }

    public List<Set<Constraint>> getSCCIncomingList() {
        return SCCIncomingList;
    }

    public void setSCCIncomingList(List<Set<Constraint>> SCCIncomingList) {
        this.SCCIncomingList = SCCIncomingList;
    }

    public Map<Integer, Boolean> getSCCStatus() {
        return SCCStatus;
    }

    public void setSCCStatus(Map<Integer, Boolean> SCCStatus) {
        this.SCCStatus = SCCStatus;
    }

    public void setSCCReady(int index) {
        SCCStatus.replace(index, true);
    }

    public Range getReturnPoint() {
        return returnPoint;
    }

    public void setReturnPoint(Range returnPoint) {
        this.returnPoint = returnPoint;
    }

    public Set<Constraint> getEntryPoints() {
        return entryPoints;
    }

    public void setEntryPoints(Set<Constraint> entryPoints) {
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

    public Interval computeReturnRange(List<Interval> argumentRange) {
        clearComputingStatus();
        Interval returnInterval = null;

        if (argumentRange.size() != argumentRange.size())
            return null;
        List<Range> formalParams = new ArrayList<>();
        List<String> actualParams = getFunction().getArgumentList();
        Set<Range> initialSet = new HashSet<>();
        for (Range range : getRangeSet()) {
            if (!revDefMap.containsKey(range))
                initialSet.add(range);
        }
        for (String name : actualParams) {
            boolean flag = false;
            for (Range initialRange : initialSet) {
                String variableName = initialRange.getVariable().getSimpleName();
                if (name.equals(variableName)) {
                    formalParams.add(initialRange);
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                System.err.println("Resolve formal parameters error");
//                return null;
            }
        }
        int size = argumentRange.size();
        for (int i = 0; i < size; ++i) {
            Interval interval = argumentRange.get(i);
            Range range = formalParams.get(i);
            range.update(interval);
        }
        LinkedList<Constraint> currentList = new LinkedList<>();
        currentList.addAll(entryPoints);
        for (Range range : formalParams) {
            SingleVariable variable = range.getVariable();
            Set<Constraint> uses = useMap.get(range);
            for (Constraint use : uses) {
                use.setReady(variable);
                currentList.offer(use);
            }
        }

        // start computing
        while (!currentList.isEmpty()) {
            Constraint currentNode = currentList.poll();
            Integer label = getNodeSCCLabel(currentNode);

            // in SCC
            if (label != null) {
                if (SCCStatus.get(label))
                    continue;

                Set<Constraint> incomingNodes = getSCCIncomingNodes(label);
                boolean flag = true;
                for (Constraint incoming : incomingNodes) {
                    if (!incoming.hasReadyVariable()) {
                        flag = false;
                        break;
                    }
                }

                // compute SCC
                if (flag) {
                    // save initial ready status
                    Set<SingleVariable> initialReadyVars = new HashSet<>();
                    for (Constraint incoming : incomingNodes) {
                        Set<Range> revUses = revUseMap.get(incoming);
                        if (revUses == null)
                            continue;

                        for (Range range : revUses) {
                            SingleVariable variable = range.getVariable();
                            Boolean ready = incoming.isReady(variable);
                            if (ready)
                                initialReadyVars.add(variable);
                        }
                    }

                    Set<Constraint> marked = new HashSet<>();
                    LinkedList<Constraint> sccList = new LinkedList<>();
                    boolean infeasiblePath = false;

                    // widen
                    for (int i = 0; i < 3; ++i) {
                        marked.clear();
                        sccList.clear();
                        sccList.offer(currentNode);

                        while (!sccList.isEmpty()) {
                            Constraint currentSccNode = sccList.poll();
                            if (!marked.contains(currentSccNode)) {
                                if (currentSccNode.isAllReady() || (currentSccNode instanceof PHIConstraint && currentSccNode.hasReadyVariable())) {
                                    marked.add(currentSccNode);
                                    Interval interval = currentSccNode.forward(rangeMap);
                                    // infeasible control path
                                    if (interval == null) {
                                        infeasiblePath = true;
                                        break;
                                    }
                                    Range def = defMap.get(currentSccNode);
                                    if (i == 0) {
                                        if (currentSccNode.isAllReady())
                                            def.update(interval);
                                        else {
                                            Map<SingleVariable, Boolean> readyMap = currentSccNode.getReadyMap();
                                            Range range = null;
                                            for (SingleVariable variable : readyMap.keySet()) {
                                                if (readyMap.get(variable)) {
                                                    range = rangeMap.get(variable);
                                                    break;
                                                }
                                            }
                                            if (range == null) {
                                                System.err.println("Compute error.");
                                                return null;
                                            }

                                            def.update(range.getInterval());
                                        }
                                    } else {
                                        Interval oriInterval = def.getInterval();
                                        if (interval.lower < oriInterval.lower)
                                            oriInterval.lower = Double.NEGATIVE_INFINITY;
                                        if (interval.upper > oriInterval.upper)
                                            oriInterval.upper = Double.POSITIVE_INFINITY;
                                    }

                                    SingleVariable defVar = def.getVariable();
                                    Set<Constraint> uses = useMap.get(def);
                                    setNextNode(label, sccList, defVar, uses);
                                }
                            }
                        }

                        if (infeasiblePath)
                            break;

                        // recovery ready status
                        for (Constraint constraint : marked) {
                            constraint.setAllUnready();
                        }
                        for (Constraint incoming : incomingNodes) {
                            for (SingleVariable variable : initialReadyVars)
                                if (incoming.getReadyMap().keySet().contains(variable))
                                    incoming.setReady(variable);
                        }
                    }

                    if (infeasiblePath) {

                        Set<ConstraintGraphNode> nodeSet = getSCCSet(label);
                        for (ConstraintGraphNode node : nodeSet) {
                            if (node instanceof Range) {
                                Range range = (Range) node;
                                SingleVariable variable = range.getVariable();
                                Set<Constraint> uses = useMap.get(range);
                                for (Constraint use : uses) {
                                    Integer nextLabel = getNodeSCCLabel(use);
                                    if (nextLabel == null || !nextLabel.equals(label)) {
                                        use.setReady(variable);
                                        if (!currentList.contains(use))
                                            currentList.offer(use);
                                    }
                                }
                            }
                        }


                        setSCCReady(label);
                        continue;
                    }

                    // future range and narrowing
                    for (int i = 0; i < 3; ++i) {
                        marked.clear();
                        sccList.clear();
                        sccList.offer(currentNode);

                        while (!sccList.isEmpty()) {
                            Constraint currentSccNode = sccList.poll();
                            if (!marked.contains(currentSccNode)) {
                                if (currentSccNode.isAllReady() || (currentSccNode instanceof PHIConstraint && currentSccNode.hasReadyVariable())) {
                                    if (currentSccNode.isAllReady())
                                        marked.add(currentSccNode);
                                    Interval interval = currentSccNode.forward(rangeMap);
                                    Range def = defMap.get(currentSccNode);
                                    Interval oriInterval = def.getInterval();
                                    oriInterval.lower = Double.max(oriInterval.lower, interval.lower);
                                    oriInterval.upper = Double.min(oriInterval.upper, interval.upper);

                                    SingleVariable defVar = def.getVariable();
                                    Set<Constraint> uses = useMap.get(def);
                                    for (Constraint use : uses) {
                                        Integer nextLabel = getNodeSCCLabel(use);
                                        if (nextLabel != null && nextLabel.equals(label)) {
                                            use.setReady(defVar);
                                            sccList.offer(use);
                                        } else if (i == 2) {
                                            use.setReady(defVar);
                                            if (!currentList.contains(use))
                                                currentList.offer(use);
                                        }
                                    }
                                }
                            }
                        }

                        // recovery ready status
                        for (Constraint constraint : marked) {
                            constraint.setAllUnready();
                        }
                        for (Constraint incoming : incomingNodes) {
                            for (SingleVariable variable : initialReadyVars)
                                if (incoming.getReadyMap().keySet().contains(variable))
                                    incoming.setReady(variable);
                        }
                    }

                    setSCCReady(label);
                }
            }
            // not in SCC
            else {
                if (currentNode.isAllReady()) {
                    Interval interval = currentNode.forward(rangeMap);
                    Range def = defMap.get(currentNode);

                    SingleVariable variable = def.getVariable();
                    // Return value
                    if (def == returnPoint) {
                        returnInterval = new Interval(interval);
                        break;
                    }


                    Set<Constraint> uses = useMap.get(def);

                    if (interval != null)
                        def.update(interval);
                    else
                        def.update(Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY);

                    for (Constraint use : uses) {
                        use.setReady(variable);
                        if (!currentList.contains(use))
                            currentList.offer(use);
                    }
                }
                // not ready yet, put back to queue
                else
                    currentList.offer(currentNode);
            }
        }

        System.out.println("Funciton " + function.getSimpleName() + " returning: " + returnInterval);
        return returnInterval;
    }

    private void setNextNode(Integer label, LinkedList<Constraint> sccList, SingleVariable
            defVar, Set<Constraint> uses) {
        for (Constraint use : uses) {
            Integer nextLabel = getNodeSCCLabel(use);
            if (nextLabel != null && nextLabel.equals(label)) {
                use.setReady(defVar);
                sccList.offer(use);
            }
        }
    }

    private void clearComputingStatus() {
        for (SingleVariable variable : rangeMap.keySet())
            rangeMap.get(variable).update(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);

        for (Constraint constraint : getConstraintSet())
            constraint.setAllUnready();

        for (Integer label : SCCStatus.keySet())
            SCCStatus.replace(label, false);
    }

}
