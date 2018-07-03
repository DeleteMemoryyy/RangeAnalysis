package util;

import model.CFG;
import model.ConstraintGraph;
import model.Function;
import model.block.BasicBlock;
import model.constraintGraphNode.*;
import model.instructioin.*;
import util.math.ENumber;
import util.math.Interval;

import java.util.*;

public class ConstraintGraphFactory {
    private static ConstraintGraphFactory instance;

    private static Map<String, String> transpositionCompare = new HashMap<>();
    private static Map<String, String> oppsiteCompare = new HashMap<>();


    private ConstraintGraphFactory() {
        transpositionCompare.put(">", "<");
        transpositionCompare.put(">=", "<=");
        transpositionCompare.put("==", "==");
        transpositionCompare.put("<=", ">=");
        transpositionCompare.put("<", ">");
        transpositionCompare.put("!=", "!=");

        oppsiteCompare.put(">", "<=");
        oppsiteCompare.put(">=", "<");
        oppsiteCompare.put("==", "!=");
        oppsiteCompare.put("<=", ">");
        oppsiteCompare.put("<", ">=");
        oppsiteCompare.put("!=", "==");
    }

    public static boolean make(Function function) {
        if (instance == null)
            instance = new ConstraintGraphFactory();

        return instance._make(function);
    }

    public boolean _make(Function function) {
        if (function == null)
            return false;
        CFG cfg = function.getCFG();
        if (cfg == null)
            return false;
        List<Expression> instList = cfg.getInstructions();
        if (instList == null)
            return false;

        Map<SingleVariable, Range> rangeMap = new HashMap<>();
        Map<Constraint, Range> defMap = new HashMap<>();
        Map<Range, Set<Constraint>> useMap = new HashMap<>();

        // Convert instructions to constraints and build ConstraintGraph
        for (Expression instruction : instList) {
            if (instruction instanceof Assignment) {
                Assignment assignment = (Assignment) instruction;
                SingleVariable dstExpr = assignment.getLeftExpr();
                if (!rangeMap.keySet().contains(dstExpr))
                    rangeMap.put(dstExpr, new Range(dstExpr, new Interval()));
                Range dstRange = rangeMap.get(dstExpr);
                if (!useMap.keySet().contains(dstRange))
                    useMap.put(dstRange, new HashSet<>());

                SingleExpression srcExpr = assignment.getRightExpr();
                if (srcExpr instanceof ConstantExpression) {
                    ConstantExpression expr = (ConstantExpression) srcExpr;
                    AssignmentConstantConstraint constraint = new AssignmentConstantConstraint(instruction, expr.doubleValue());

                    defMap.put(constraint, dstRange);
                } else if (srcExpr instanceof SingleVariable) {
                    SingleVariable expr = (SingleVariable) srcExpr;
                    AssignmentVariableConstraint constraint = new AssignmentVariableConstraint(instruction, expr);

                    defMap.put(constraint, dstRange);

                    if (!rangeMap.keySet().contains(expr))
                        rangeMap.put(expr, new Range(expr, new Interval()));
                    Range srcRange = rangeMap.get(expr);
                    if (!useMap.keySet().contains(srcRange))
                        useMap.put(srcRange, new HashSet<>());
                    Set<Constraint> uses = useMap.get(srcRange);
                    uses.add(constraint);
                } else if (srcExpr instanceof ConvertExpression) {
                    ConvertExpression expr = (ConvertExpression) srcExpr;
                    SingleVariable singleVariable = expr.getSingleVariable();
                    AssignmentVariableConstraint constraint = new AssignmentVariableConstraint(instruction, singleVariable);

                    defMap.put(constraint, dstRange);

                    if (!rangeMap.keySet().contains(singleVariable))
                        rangeMap.put(singleVariable, new Range(singleVariable, new Interval()));
                    Range srcRange = rangeMap.get(singleVariable);
                    if (!useMap.keySet().contains(srcRange))
                        useMap.put(srcRange, new HashSet<>());
                    Set<Constraint> uses = useMap.get(srcRange);
                    uses.add(constraint);
                } else if (srcExpr instanceof ArithmeticExpression) {
                    ArithmeticExpression arithmeticExpr = (ArithmeticExpression) srcExpr;
                    SingleExpression expr1 = arithmeticExpr.getLeftExpr();
                    SingleExpression expr2 = arithmeticExpr.getRightExpr();
                    String operation = arithmeticExpr.getOperation();

                    if (expr1 instanceof SingleVariable) {
                        if (expr2 instanceof SingleVariable) {
                            SingleVariable singleExpr1 = (SingleVariable) expr1;
                            SingleVariable singleExpr2 = (SingleVariable) expr2;
                            ArithmeticDoubleConstraint constraint = new ArithmeticDoubleConstraint(instruction, operation, singleExpr1, singleExpr2);

                            defMap.put(constraint, dstRange);

                            if (!rangeMap.keySet().contains(singleExpr1))
                                rangeMap.put(singleExpr1, new Range(singleExpr1, new Interval()));
                            Range srcRange1 = rangeMap.get(singleExpr1);
                            if (!useMap.keySet().contains(srcRange1))
                                useMap.put(srcRange1, new HashSet<>());
                            Set<Constraint> uses = useMap.get(srcRange1);
                            uses.add(constraint);
                            if (!rangeMap.keySet().contains(singleExpr2))
                                rangeMap.put(singleExpr2, new Range(singleExpr2, new Interval()));
                            Range srcRange2 = rangeMap.get(singleExpr2);
                            if (!useMap.keySet().contains(srcRange2))
                                useMap.put(srcRange2, new HashSet<>());
                            uses = useMap.get(srcRange2);
                            uses.add(constraint);
                        } else {
                            SingleVariable singleExpr = (SingleVariable) expr1;
                            double value = ((ConstantExpression) expr2).doubleValue();
                            ArithmeticSingleLeftConstraint constraint = new ArithmeticSingleLeftConstraint(instruction, operation, singleExpr, value);

                            defMap.put(constraint, dstRange);

                            if (!rangeMap.keySet().contains(singleExpr))
                                rangeMap.put(singleExpr, new Range(singleExpr, new Interval()));
                            Range srcRange = rangeMap.get(singleExpr);
                            if (!useMap.keySet().contains(srcRange))
                                useMap.put(srcRange, new HashSet<>());
                            Set<Constraint> uses = useMap.get(srcRange);
                            uses.add(constraint);
                        }
                    } else if (expr2 instanceof SingleVariable) {
                        SingleVariable singleExpr = (SingleVariable) expr2;
                        double value = ((ConstantExpression) expr1).doubleValue();
                        ArithmeticSingleRightConstraint constraint = new ArithmeticSingleRightConstraint(instruction, operation, value, singleExpr);

                        defMap.put(constraint, dstRange);

                        if (!rangeMap.keySet().contains(singleExpr))
                            rangeMap.put(singleExpr, new Range(singleExpr, new Interval()));
                        Range srcRange = rangeMap.get(singleExpr);
                        if (!useMap.keySet().contains(srcRange))
                            useMap.put(srcRange, new HashSet<>());
                        Set<Constraint> uses = useMap.get(srcRange);
                        uses.add(constraint);
                    } else {
                        double value1 = ((ConstantExpression) expr1).doubleValue();
                        double value2 = ((ConstantExpression) expr2).doubleValue();
                        double result = 0.0;
                        if ("+".equals(operation))
                            result = value1 + value2;
                        else if ("-".equals(operation))
                            result = value1 - value2;
                        else if ("*".equals(operation))
                            result = value1 * value2;
                        else if ("/".equals(operation))
                            result = value1 / value2;
                        AssignmentConstantConstraint constraint = new AssignmentConstantConstraint(instruction, result);

                        defMap.put(constraint, dstRange);
                    }
                } else if (srcExpr instanceof FunctionCall) {
                    FunctionCall functionCall = (FunctionCall) srcExpr;
                    List<SingleExpression> arguments = functionCall.getArguments();
                    AssignmentFunctionCallConstraint constraint = new AssignmentFunctionCallConstraint(instruction, arguments, function.getTranslateUnit());

                    defMap.put(constraint, dstRange);

                    for (SingleExpression expression : arguments) {
                        if (expression instanceof SingleVariable) {
                            SingleVariable singleExpr = (SingleVariable) expression;
                            if (!rangeMap.keySet().contains(singleExpr))
                                rangeMap.put(singleExpr, new Range(singleExpr, new Interval()));
                            Range srcRange = rangeMap.get(singleExpr);
                            if (!useMap.keySet().contains(srcRange))
                                useMap.put(srcRange, new HashSet<>());
                            Set<Constraint> uses = useMap.get(srcRange);
                            uses.add(constraint);
                        }
                    }
                }
            } else if (instruction instanceof PHIExpression) {
                PHIExpression phiExpression = (PHIExpression) instruction;
                SingleVariable dstExpr = phiExpression.getLeftExpr();
                SingleVariable singleExpr1 = phiExpression.getFromExpr_1();
                SingleVariable singleExpr2 = phiExpression.getFromExpr_2();
                PHIConstraint constraint = new PHIConstraint(instruction, singleExpr1, singleExpr2);

                if (!rangeMap.keySet().contains(dstExpr))
                    rangeMap.put(dstExpr, new Range(dstExpr, new Interval()));
                Range dstRange = rangeMap.get(dstExpr);
                if (!useMap.keySet().contains(dstRange))
                    useMap.put(dstRange, new HashSet<>());

                defMap.put(constraint, dstRange);

                if (!rangeMap.keySet().contains(singleExpr1))
                    rangeMap.put(singleExpr1, new Range(singleExpr1, new Interval()));
                Range srcRange1 = rangeMap.get(singleExpr1);
                if (!useMap.keySet().contains(srcRange1))
                    useMap.put(srcRange1, new HashSet<>());
                Set<Constraint> uses = useMap.get(srcRange1);
                uses.add(constraint);
                if (!rangeMap.keySet().contains(singleExpr2))
                    rangeMap.put(singleExpr2, new Range(singleExpr2, new Interval()));
                Range srcRange2 = rangeMap.get(singleExpr2);
                if (!useMap.keySet().contains(srcRange2))
                    useMap.put(srcRange2, new HashSet<>());
                uses = useMap.get(srcRange2);
                uses.add(constraint);
            }
        }

        // live range splitting
        Map<BasicBlock, Set<BasicBlock>> dominanceFroniters = dominanceAnalysis(cfg);
        if (dominanceFroniters == null)
            return false;
        Map<BasicBlock, Set<SingleVariable>> blockUses = useAnalysis(cfg);
        if (blockUses == null)
            return false;
        List<BasicBlock> blockList = cfg.getBlocks();
        Map<String, BasicBlock> blockMap = cfg.getBlockMap();
        Map<Expression, BasicBlock> fromBlockMap = cfg.getFromBlockMap();
        for (BasicBlock currentBlock : blockList) {
            Expression instruction = currentBlock.getLastInstruction();
            if (instruction != null && instruction instanceof IfGotoExpression) {
                IfGotoExpression ifGotoExpression = (IfGotoExpression) instruction;
                ConditionExpression condition = ifGotoExpression.getConditionExpression();
                SingleExpression leftExpr = condition.getLeftExpr();
                SingleExpression rightExpr = condition.getRightExpr();
                String trueBlockId = ifGotoExpression.getTrueGotoBlockId();
                String falseBlockId = ifGotoExpression.getFalseGotoBlockId();
                BasicBlock trueBlock = blockMap.get(trueBlockId);
                BasicBlock falseBlock = blockMap.get(falseBlockId);
                if (trueBlock == null || falseBlock == null)
                    continue;

                Set<BasicBlock> trueBlockSet = new HashSet<>();
                Set<BasicBlock> falseBlockSet = new HashSet<>();

                if (leftExpr instanceof SingleVariable) {
                    SingleVariable variable = (SingleVariable) leftExpr;
                    SingleExpression otherExpr = rightExpr;

                    if (isSparatable(dominanceFroniters, blockUses, trueBlock, falseBlock, trueBlockSet, falseBlockSet, variable)) {
                        Range oriRange = rangeMap.get(variable);
                        if (oriRange == null) {
                            oriRange = new Range(variable, new Interval());
                            rangeMap.put(variable, oriRange);
                        }
                        Set<Constraint> oriConstraintSet = useMap.get(oriRange);
                        if (oriConstraintSet == null)
                            oriConstraintSet = new HashSet<>();

                        Set<Constraint> newConstraintSet = new HashSet<>();

                        SingleVariable newTrueVariable = new SingleVariable(variable.getName() + "_t", variable.getSimpleName(), variable.getVersion());
                        String op = condition.getCompareOperation();
                        Set<Constraint> newTrueUses = getConstraints(rangeMap, defMap, useMap, newConstraintSet, ifGotoExpression, trueBlockSet, variable, otherExpr, newTrueVariable, op);

                        SingleVariable newFalseVariable = new SingleVariable(variable.getName() + "_f", variable.getSimpleName(), variable.getVersion());
                        op = oppsiteCompare.get(condition.getCompareOperation());
                        Set<Constraint> newFalseUses = getConstraints(rangeMap, defMap, useMap, newConstraintSet, ifGotoExpression, falseBlockSet, variable, otherExpr, newFalseVariable, op);

                        useMap.put(oriRange, newConstraintSet);

                        replaceUseSet(oriConstraintSet, variable, fromBlockMap, trueBlockSet, falseBlockSet, newTrueVariable, newTrueUses, newFalseVariable, newFalseUses);
                    }

                }

                if (rightExpr instanceof SingleVariable) {
                    {
                        SingleVariable variable = (SingleVariable) rightExpr;
                        SingleExpression otherExpr = leftExpr;

                        if (isSparatable(dominanceFroniters, blockUses, trueBlock, falseBlock, trueBlockSet, falseBlockSet, variable)) {
                            Range oriRange = rangeMap.get(variable);
                            if (oriRange == null) {
                                oriRange = new Range(variable, new Interval());
                                rangeMap.put(variable, oriRange);
                            }
                            Set<Constraint> oriConstraintSet = useMap.get(oriRange);
                            if (oriConstraintSet == null)
                                oriConstraintSet = new HashSet<>();

                            Set<Constraint> newConstraintSet = new HashSet<>();

                            SingleVariable newTrueVariable = new SingleVariable(variable.getName() + "_t", variable.getSimpleName(), variable.getVersion());
                            String op = transpositionCompare.get(condition.getCompareOperation());
                            Set<Constraint> newTrueUses = getConstraints(rangeMap, defMap, useMap, newConstraintSet, ifGotoExpression, trueBlockSet, variable, otherExpr, newTrueVariable, op);

                            SingleVariable newFalseVariable = new SingleVariable(variable.getName() + "_f", variable.getSimpleName(), variable.getVersion());
                            op = oppsiteCompare.get(transpositionCompare.get(condition.getCompareOperation()));
                            Set<Constraint> newFalseUses = getConstraints(rangeMap, defMap, useMap, newConstraintSet, ifGotoExpression, falseBlockSet, variable, otherExpr, newFalseVariable, op);

                            useMap.put(oriRange, newConstraintSet);

                            replaceUseSet(oriConstraintSet, variable, fromBlockMap, trueBlockSet, falseBlockSet, newTrueVariable, newTrueUses, newFalseVariable, newFalseUses);

                        }
                    }
                }
            }
        }


        ConstraintGraph constraintGraph = new ConstraintGraph(function);
        constraintGraph.setDefMap(defMap);
        constraintGraph.setUseMap(useMap);
        function.setConstraintGraph(constraintGraph);
        return true;
    }

    private boolean isSparatable(Map<BasicBlock, Set<BasicBlock>> dominanceFroniters, Map<BasicBlock, Set<SingleVariable>> blockUses, BasicBlock trueBlock, BasicBlock falseBlock, Set<BasicBlock> trueBlockSet, Set<BasicBlock> falseBlockSet, SingleVariable variable) {
        boolean flag = false;

        // Condition(i) Lt or Lf dominate any use of v
        for (BasicBlock block : blockUses.keySet()) {
            if (blockUses.get(block).contains(variable)) {
                Set<BasicBlock> dominator = dominanceFroniters.get(block);
                if (dominator.contains(trueBlock)) {
                    flag = true;
                    trueBlockSet.add(block);
                }
                if (dominator.contains(falseBlock)) {
                    flag = true;
                    falseBlockSet.add(block);
                }
            }
        }

//        condition(ii) there exists a use of v at the dominance frontier of Lt or Lf
//        Set<BasicBlock> dominator = dominanceFroniters.get(currentBlock);
//        for (BasicBlock block : dominator) {
//            if (blockUses.get(block).contains(variable)) {
//                flag = true;
//            }
//        }

        return flag;
    }

    private Set<Constraint> getConstraints(Map<SingleVariable, Range> rangeMap, Map<Constraint, Range> defMap, Map<Range, Set<Constraint>> useMap, Set<Constraint> newOriUses, IfGotoExpression ifGotoExpression, Set<BasicBlock> blockSet, SingleVariable variable, SingleExpression otherExpr, SingleVariable newVariable, String op) {
        Set<Constraint> newUses = new HashSet<>();
        if (!blockSet.isEmpty()) {
            Range newRange = new Range(newVariable, new Interval());
            rangeMap.put(newVariable, newRange);
            useMap.put(newRange, newUses);

            ConditionConstraint newConstraint = makeConditionConstraint(ifGotoExpression, op, variable, otherExpr);
            if (newConstraint != null)
                newOriUses.add(newConstraint);

            defMap.put(newConstraint, newRange);
        }
        return newUses;
    }

    private Map<BasicBlock, Set<BasicBlock>> dominanceAnalysis(CFG cfg) {
        if (cfg == null)
            return null;
        List<BasicBlock> blockList = cfg.getBlocks();
        if (blockList == null)
            return null;

        Map<BasicBlock, Set<BasicBlock>> dominanceFrontiers = new HashMap<>();
        for (BasicBlock block : blockList) {
            Set<BasicBlock> set = new HashSet<>();
            set.add(block);
            dominanceFrontiers.put(block, set);
        }
        boolean changed = true;
        while (changed == true) {
            changed = false;
            for (BasicBlock block : blockList) {
                Set<BasicBlock> result = new HashSet<>();
                List<BasicBlock> predNodes = cfg.getPredNodes(block);
                if (!predNodes.isEmpty())
                    result.addAll(dominanceFrontiers.get(predNodes.get(0)));
                for (BasicBlock pred : predNodes)
                    if (pred != block && !dominanceFrontiers.get(pred).contains(block))
                        result.retainAll(dominanceFrontiers.get(pred));

                Set<BasicBlock> frontier = dominanceFrontiers.get(block);
                int oriSize = frontier.size();
                frontier.addAll(result);
                if (frontier.size() > oriSize)
                    changed = true;
            }
        }
        return dominanceFrontiers;
    }

    private Map<BasicBlock, Set<SingleVariable>> useAnalysis(CFG cfg) {
        if (cfg == null)
            return null;
        List<BasicBlock> blockList = cfg.getBlocks();
        if (blockList == null)
            return null;
        Map<BasicBlock, Set<SingleVariable>> blockUses = new HashMap<>();

        for (BasicBlock block : blockList) {
            Set<SingleVariable> uses = new HashSet<>();
            blockUses.put(block, uses);
            for (Expression instruction : block.getInstructionList()) {
                if (instruction instanceof Assignment) {
                    Assignment assignment = (Assignment) instruction;
                    SingleExpression srcExpr = assignment.getRightExpr();
                    if (srcExpr instanceof SingleVariable)
                        uses.add((SingleVariable) srcExpr);
                    else if (srcExpr instanceof ConvertExpression)
                        uses.add(((ConvertExpression) srcExpr).getSingleVariable());
                    else if (srcExpr instanceof ArithmeticExpression) {
                        ArithmeticExpression arithmeticExpr = (ArithmeticExpression) srcExpr;
                        SingleExpression expr1 = arithmeticExpr.getLeftExpr();
                        SingleExpression expr2 = arithmeticExpr.getRightExpr();
                        if (expr1 instanceof SingleVariable)
                            uses.add((SingleVariable) expr1);
                        if (expr2 instanceof SingleVariable)
                            uses.add((SingleVariable) expr2);
                    } else if (srcExpr instanceof FunctionCall) {
                        for (SingleExpression expression : ((FunctionCall) srcExpr).getArguments()) {
                            if (expression instanceof SingleVariable)
                                uses.add((SingleVariable) expression);
                        }
                    }
                } else if (instruction instanceof PHIExpression) {
                    PHIExpression phiExpression = (PHIExpression) instruction;
                    uses.add(phiExpression.getFromExpr_1());
                    uses.add(phiExpression.getFromExpr_2());
                } else if (instruction instanceof IfGotoExpression) {
                    ConditionExpression condition = ((IfGotoExpression) instruction).getConditionExpression();
                    SingleExpression leftExpr = condition.getLeftExpr();
                    SingleExpression rightExpr = condition.getRightExpr();
                    if (leftExpr instanceof SingleVariable)
                        uses.add((SingleVariable) leftExpr);
                    if (rightExpr instanceof SingleVariable)
                        uses.add((SingleVariable) rightExpr);
                }
            }
        }
        return blockUses;
    }

    private void replaceUseSet(Set<Constraint> oriConstraintSet, SingleVariable variable, Map<Expression, BasicBlock> fromBlockMap, Set<BasicBlock> trueBlockSet, Set<BasicBlock> falseBlockSet, SingleVariable newTrueVariable, Set<Constraint> newTrueUses, SingleVariable newFalseVariable, Set<Constraint> newFalseUses) {
        for (Constraint oriConstraint : oriConstraintSet) {
            Expression oriExpr = oriConstraint.getExpression();
            BasicBlock oriBlock = fromBlockMap.get(oriExpr);
            if (trueBlockSet.contains(oriBlock)) {
                if (falseBlockSet.contains(oriBlock)) {
                    // resolve error
                    continue;
                }

                if (replaceVariable(oriConstraint, variable, newTrueVariable))
                    newTrueUses.add(oriConstraint);

            } else if (falseBlockSet.contains(oriBlock)) {
                if (replaceVariable(oriConstraint, variable, newFalseVariable))
                    newFalseUses.add(oriConstraint);
            }
        }
    }

    private boolean replaceVariable(Constraint constraint, SingleVariable oriVariable, SingleVariable newVariable) {
        if (constraint instanceof AssignmentVariableConstraint) {
            AssignmentVariableConstraint variableConstraint = (AssignmentVariableConstraint) constraint;
            SingleVariable contraintVariable = variableConstraint.getSingleVariable();
            if (contraintVariable == oriVariable) {
                variableConstraint.setSingleVariable(newVariable);
                variableConstraint.setName(variableConstraint.getName().replace(oriVariable.getName(), newVariable.getName()));
                return true;
            } else
                return false;
        } else if (constraint instanceof AssignmentFunctionCallConstraint) {
            List<SingleExpression> argumentList = ((AssignmentFunctionCallConstraint) constraint).getActualArguments();
            boolean flag = false;
            for (SingleExpression expression : argumentList) {
                if (expression instanceof SingleVariable && expression == oriVariable) {
                    argumentList.set(argumentList.indexOf(expression), newVariable);
                    constraint.setName(constraint.getName().replace(oriVariable.getName(), newVariable.getName()));
                    flag = true;
                }
            }
            return flag;
        } else if (constraint instanceof ArithmeticSingleConstraint) {
            ArithmeticSingleConstraint variableConstraint = (ArithmeticSingleConstraint) constraint;
            SingleVariable contraintVariable = variableConstraint.getSingleVariable();
            if (contraintVariable == oriVariable) {
                variableConstraint.setSingleVariable(newVariable);
                variableConstraint.setName(variableConstraint.getName().replace(oriVariable.getName(), newVariable.getName()));
                return true;
            } else
                return false;
        } else if (constraint instanceof ArithmeticDoubleConstraint) {
            ArithmeticDoubleConstraint variableConstraint = (ArithmeticDoubleConstraint) constraint;
            SingleVariable contraintVariable = variableConstraint.getExpr1();
            boolean flag = false;
            if (contraintVariable == oriVariable) {
                variableConstraint.setExpr1(newVariable);
                variableConstraint.setName(variableConstraint.getName().replace(oriVariable.getName(), newVariable.getName()));
                flag = true;
            }
            contraintVariable = variableConstraint.getExpr2();
            if (contraintVariable == oriVariable) {
                variableConstraint.setExpr2(newVariable);
                variableConstraint.setName(variableConstraint.getName().replace(oriVariable.getName(), newVariable.getName()));
                flag = true;
            }
            return flag;
        } else if (constraint instanceof PHIConstraint) {
            PHIConstraint variableConstraint = (PHIConstraint) constraint;
            SingleVariable contraintVariable = variableConstraint.getFromExpr_1();
            boolean flag = false;
            if (contraintVariable == oriVariable) {
                variableConstraint.setFromExpr_1(newVariable);
                variableConstraint.setName(variableConstraint.getName().replace(oriVariable.getName(), newVariable.getName()));
                flag = true;
            }
            contraintVariable = variableConstraint.getFromExpr_1();
            if (contraintVariable == oriVariable) {
                variableConstraint.setFromExpr_2(newVariable);
                variableConstraint.setName(variableConstraint.getName().replace(oriVariable.getName(), newVariable.getName()));
                flag = true;
            }
            return flag;
        }

        return false;
    }

    private ConditionConstraint makeConditionConstraint(IfGotoExpression instruction, String compareOperation, SingleVariable variable, SingleExpression otherExpr) {
        ConditionConstraint constraint = null;

        if (otherExpr instanceof ConstantExpression) {
            double value = ((ConstantExpression) otherExpr).doubleValue();

            if (compareOperation.equals(">"))
                constraint = new ConditionConstraint(instruction, variable, new Interval(new ENumber(value + 1.0), new ENumber(1)), null, 0);
            else if (compareOperation.equals(">="))
                constraint = new ConditionConstraint(instruction, variable, new Interval(new ENumber(value), new ENumber(1)), null, 0);
            else if (compareOperation.equals("=="))
                constraint = new ConditionConstraint(instruction, variable, new Interval(new ENumber(value), new ENumber(value)), null, 0);
            else if (compareOperation.equals("<"))
                constraint = new ConditionConstraint(instruction, variable, new Interval(new ENumber(-1), new ENumber(value - 1)), null, 0);
            else if (compareOperation.equals("<="))
                constraint = new ConditionConstraint(instruction, variable, new Interval(new ENumber(-1), new ENumber(value)), null, 0);
            else if (compareOperation.equals("!="))
                constraint = new ConditionConstraint(instruction, variable, new Interval(new ENumber(-1), new ENumber(1)), null, 0);
        } else if (otherExpr instanceof SingleVariable) {
            SingleVariable ftVariable = (SingleVariable) otherExpr;

            if (compareOperation.equals(">"))
                constraint = new ConditionConstraint(instruction, variable, new Interval(new ENumber(1.0), new ENumber(1)), ftVariable, -1);
            else if (compareOperation.equals(">="))
                constraint = new ConditionConstraint(instruction, variable, new Interval(new ENumber(0.0), new ENumber(1)), ftVariable, -1);
            else if (compareOperation.equals("=="))
                constraint = new ConditionConstraint(instruction, variable, null, ftVariable, 2);
            else if (compareOperation.equals("<"))
                constraint = new ConditionConstraint(instruction, variable, new Interval(new ENumber(-1), new ENumber(-1.0)), ftVariable, 1);
            else if (compareOperation.equals("<="))
                constraint = new ConditionConstraint(instruction, variable, new Interval(new ENumber(-1), new ENumber(0.0)), ftVariable, 1);
            else if (compareOperation.equals("!="))
                constraint = new ConditionConstraint(instruction, variable, new Interval(new ENumber(-1), new ENumber(1)), null, 0);
        }
        if (constraint != null)
            constraint.setName("if (" + variable.getName() + " " + compareOperation + " " + otherExpr.getName() + ")");

        return constraint;
    }

}
