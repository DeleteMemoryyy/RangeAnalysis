package util;

import model.CFG;
import model.Function;
import model.TranslateUnit;
import model.block.BasicBlock;
import model.block.EntryBlock;
import model.block.ExitBlock;
import model.instructioin.*;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CFGFactory {
    private static CFGFactory instance;

    private Pattern patternLabel = Pattern.compile("\\A<(.*)>:\\Z");
    private Pattern patternAssignment = Pattern.compile("\\A([A-Za-z_][A-Za-z0-9_]*) = (.*);\\Z");
    private Pattern patternFuncitonCallInstruction = Pattern.compile("\\A([A-Za-z_][A-Za-z0-9_]*) \\(([A-Za-z0-9_(), ]*)\\);\\Z");
    private Pattern patternPHIExpression = Pattern.compile("\\A# ([A-Za-z_][A-Za-z0-9_]*) = PHI <([A-Za-z_][A-Za-z0-9_]*)(\\(D\\))?\\(([0-9]*)\\), ([A-Za-z_][A-Za-z0-9_]*)(\\(D\\))?\\(([0-9]*)\\)>\\Z");
    private Pattern patternGotoExpression = Pattern.compile("\\Agoto <(.*)>( \\(<(.*)>\\))?;\\Z");
    private Pattern patternIfExpression = Pattern.compile("\\Aif \\(([A-Za-z0-9_()>=<! ]*)\\)\\Z");
    private Pattern patternReturnExpression = Pattern.compile("\\Areturn( (.*))?;\\Z");
    private Pattern patternConditionExpression = Pattern.compile("\\A([A-Za-z0-9_().+-]*) (>|>=|==|<|<=|!=) ([A-Za-z0-9_().+-]*)\\Z");

    // SingleExpression
    private Pattern patternSingleVariable = Pattern.compile("\\A(([A-Za-z_][A-Za-z0-9_]*)?_([0-9]*))(\\(D\\))?\\Z");
    private Pattern patternConstantInt = Pattern.compile("\\A(-?[0-9]*)\\Z");
    private Pattern patternConstantFloat = Pattern.compile("\\A(-?[0-9]*.[0-9]*)(e([+\\-][0-9]*))?\\Z");
    private Pattern patternFunctionCall = Pattern.compile("\\A([A-Za-z_][A-Za-z0-9_]*) \\(([A-Za-z0-9_().+-, ]*)\\)\\Z");
    private Pattern patternArithmeticExpression = Pattern.compile("\\A([A-Za-z0-9_().+-]*) ([+\\-*/]) ([A-Za-z0-9_().+-]*)\\Z");
    private Pattern patternConvertExpression = Pattern.compile("\\A\\(([A-Za-z0-9_]*)\\) ([A-Za-z0-9_().+-]*)\\Z");

    private Map<String, SingleVariable> variableMap;

    private CFGFactory() {
    }

    public static boolean make(Function function, int instStartLine, int instEndLine) {
        if (instance == null)
            instance = new CFGFactory();

        return instance._make(function, instStartLine, instEndLine);
    }

    private boolean _make(Function function, int instStartLine, int instEndLine) {
        CFG cfg = new CFG(function, instStartLine, instEndLine);
        variableMap = new HashMap<>();

        if (!makeInstructions(cfg))
            return false;
        if (!makeBlocks(cfg))
            return false;
        if (!makeEdges(cfg))
            return false;

        cfg.setVariableMap(variableMap);
        function.setCFG(cfg);
        return true;
    }

    private boolean makeInstructions(CFG cfg) {
        if (cfg == null)
            return false;
        TranslateUnit translateUnit = cfg.getFunction().getTranslateUnit();
        if (translateUnit == null)
            return false;

        int instStartLine = cfg.getInstStartLine();
        int instEndLine = cfg.getInstEndLine();

        List<Expression> instructions = new ArrayList<>();
        int currentLineNum = instStartLine;
        while (currentLineNum < instEndLine) {
            String tmpLine = translateUnit.getLine(currentLineNum);
            if (!tmpLine.startsWith("<")) {
                Expression expression;

                expression = resolveAssignment(tmpLine);
                if (expression != null) {
                    instructions.add(expression);
                    currentLineNum++;
                    continue;
                }

                expression = resolvePHIExpression(tmpLine);
                if (expression != null) {
                    instructions.add(expression);
                    currentLineNum++;
                    continue;
                }

                expression = resolveReturnExpression(tmpLine);
                if (expression != null) {
                    instructions.add(expression);
                    currentLineNum++;
                    continue;
                }

                expression = resolveFucntionCallInstruction(tmpLine);
                if (expression != null) {
                    instructions.add(expression);
                    currentLineNum++;
                    continue;
                }

                expression = resolveIfGotoExpression(tmpLine);
                if (expression != null) {
                    if (currentLineNum + 3 >= instEndLine)
                        return false;
                    if (!"else".equals(translateUnit.getLine(currentLineNum + 2)))
                        return false;
                    GotoExpression trueGotoExpr = resolveGotoExpression(translateUnit.getLine(currentLineNum + 1));
                    GotoExpression falseGotoExpr = resolveGotoExpression(translateUnit.getLine(currentLineNum + 3));
                    if (trueGotoExpr == null || falseGotoExpr == null)
                        return false;

                    IfGotoExpression ifGotoExpression = (IfGotoExpression) expression;
                    ifGotoExpression.setTrueGotoBlockId(trueGotoExpr.getGotoBlockId());
                    ifGotoExpression.setFalseGotoBlockId(falseGotoExpr.getGotoBlockId());

                    ifGotoExpression.setName(ifGotoExpression.getName() + " " + trueGotoExpr.getName() + " else " + falseGotoExpr.getName());

                    instructions.add(ifGotoExpression);
                    currentLineNum += 4;
                    continue;
                }

                expression = resolveGotoExpression(tmpLine);
                if (expression != null) {
                    instructions.add(expression);
                    currentLineNum++;
                    continue;
                }

                System.err.println("Resolve instruction error: " + tmpLine);
                return false;
            }
            currentLineNum++;
        }

        cfg.setInstructions(instructions);
        return true;
    }

    private boolean makeBlocks(CFG cfg) {
        if (cfg == null)
            return false;
        List<Expression> instructions = cfg.getInstructions();
        TranslateUnit translateUnit = cfg.getFunction().getTranslateUnit();
        if (instructions == null || translateUnit == null)
            return false;

        int instStartLine = cfg.getInstStartLine();
        int instEndLine = cfg.getInstEndLine();

        List<BasicBlock> blocks = new ArrayList<>();
        Map<String, BasicBlock> blockMap = new HashMap<>();
        Map<Expression, BasicBlock> fromBlockMap = new HashMap<>();

        EntryBlock entryBlock = new EntryBlock(cfg);
        entryBlock.setInstructionIist(new ArrayList<>());
        blocks.add(entryBlock);
        blockMap.put(entryBlock.getId(), entryBlock);

        BasicBlock currentBlock = null;
        List<Expression> instructionList = null;
        Iterator<Expression> expressionIterator = instructions.iterator();
        int currentLineNum = instStartLine;
        while (currentLineNum < instEndLine) {
            String tmpLine = translateUnit.getLine(currentLineNum);

            Matcher matcher = patternLabel.matcher(tmpLine);
            if (matcher.find()) {
                String id = matcher.group(1);
                currentBlock = new BasicBlock(id, cfg);
                blocks.add(currentBlock);
                blockMap.put(id, currentBlock);
                instructionList = new ArrayList<>();
                currentBlock.setInstructionIist(instructionList);
                currentLineNum++;
                continue;
            }

            Expression instruction = expressionIterator.next();
            instructionList.add(instruction);
            fromBlockMap.put(instruction, currentBlock);

            if (instruction instanceof IfGotoExpression)
                currentLineNum += 4;
            else currentLineNum++;
        }

        ExitBlock exitBlock = new ExitBlock(cfg);
        exitBlock.setInstructionIist(new ArrayList<>());
        blocks.add(exitBlock);
        blockMap.put(exitBlock.getId(), exitBlock);

        cfg.setBlocks(blocks);
        cfg.setBlockMap(blockMap);
        cfg.setFromBlockMap(fromBlockMap);
        return true;
    }

    private boolean makeEdges(CFG cfg) {
        if (cfg == null)
            return false;

        List<BasicBlock> blocks = cfg.getBlocks();
        Map<String, BasicBlock> blockMap = cfg.getBlockMap();
        if (blocks == null || blockMap == null)
            return false;

        Map<BasicBlock, List<BasicBlock>> successors = new HashMap<>();
        Map<BasicBlock, List<BasicBlock>> precursors = new HashMap<>();


        for (BasicBlock block : blocks) {
            successors.put(block, new ArrayList<>());
            precursors.put(block, new ArrayList<>());
        }

        successors.get(cfg.getEntryBlock()).add(blocks.get(1));

        int blockNum = blocks.size();
        for (int i = 1; i < blockNum - 1; ++i) {
            BasicBlock currentBlock = blocks.get(i);
            List<BasicBlock> successor = successors.get(currentBlock);
            Expression lastInstruction = currentBlock.getLastInstruction();
            if (lastInstruction instanceof GotoExpression) {
                GotoExpression gotoExpression = (GotoExpression) lastInstruction;
                successor.add(blockMap.get(gotoExpression.getGotoBlockId()));
            } else if (lastInstruction instanceof IfGotoExpression) {
                IfGotoExpression ifGotoExpression = (IfGotoExpression) lastInstruction;
                successor.add(blockMap.get(ifGotoExpression.getTrueGotoBlockId()));
                successor.add(blockMap.get(ifGotoExpression.getFalseGotoBlockId()));
            } else {
                successor.add(blocks.get(i + 1));
            }
        }
        cfg.setSuccessors(successors);

        for (BasicBlock predBlock : blocks) {
            Iterator<BasicBlock> succNodesIterator = cfg.getSuccNodes(predBlock).iterator();
            while (succNodesIterator.hasNext()) {
                BasicBlock succBlock = succNodesIterator.next();
                precursors.get(succBlock).add(predBlock);
            }
        }
        cfg.setPrecursors(precursors);

        return true;
    }

    private Assignment resolveAssignment(String name) {
        Matcher matcher = patternAssignment.matcher(name);
        if (!matcher.find())
            return null;

        String leftStr = matcher.group(1);
        SingleVariable leftExpr = resolveSingleVariable(leftStr);
        if (leftExpr == null)
            return null;

        String rightStr = matcher.group(2);
        SingleExpression rightExpr = resolveSingleExpression(rightStr);
        if (rightExpr == null)
            return null;

        return new Assignment(name.substring(0, name.length() - 1), leftExpr, rightExpr);
    }

    private SingleExpression resolveSingleExpression(String name) {
        SingleExpression singleExpression = resolveSingleVariable(name);
        if (singleExpression == null) {
            singleExpression = resolveConstantExpression(name);
            if (singleExpression == null) {
                singleExpression = resolveFunctionCallExpression(name);
                if (singleExpression == null) {
                    singleExpression = resolveArithmeticExpression(name);
                    if (singleExpression == null)
                        singleExpression = resolveConvertExpression(name);
                }
            }
        }
        return singleExpression;
    }


    private SingleVariable resolveSingleVariable(String name) {
        Matcher matcher = patternSingleVariable.matcher(name);
        if (!matcher.find())
            return null;

        String oriName = matcher.group(1);
        String simpleName = matcher.group(2);
        int version = 0;
        if (simpleName == null)
            simpleName = oriName;
        else
            version = Integer.valueOf(matcher.group(3));

        SingleVariable singleVariable = variableMap.get(oriName);
        if (singleVariable == null) {
            singleVariable = new SingleVariable(oriName, simpleName, version);
            variableMap.put(oriName, singleVariable);
        }

        return singleVariable;
    }

    private ConstantExpression resolveConstantExpression(String name) {
        ConstantExpression expression = resolveConstantInt(name);
        if (expression == null)
            expression = resolveConstantFloat(name);

        return expression;
    }

    private ConstantInt resolveConstantInt(String name) {
        Matcher matcher = patternConstantInt.matcher(name);
        if (!matcher.find())
            return null;

        return new ConstantInt(name);
    }

    private ConstantFloat resolveConstantFloat(String name) {
        Matcher matcher = patternConstantFloat.matcher(name);
        if (!matcher.find())
            return null;

        float factor = Float.valueOf(matcher.group(1));
        int exponent = 0;
        String exponentStr = matcher.group(2);
        if (exponentStr != null)
            exponent = Integer.valueOf(exponentStr.substring(1));

        return new ConstantFloat(name, factor, exponent);
    }

    private FunctionCall resolveFunctionCallExpression(String name) {
        Matcher matcher = patternFunctionCall.matcher(name);
        if (!matcher.find())
            return null;

        String simpleName = matcher.group(1);
        String argumentStr = matcher.group(2);
        List<SingleExpression> arguments = new ArrayList<>();
        if (argumentStr != null) {
            String[] argumentStrList = argumentStr.split(", ");
            for (String str : argumentStrList) {
                SingleExpression singleExpression = resolveSingleExpression(str);
                if (singleExpression == null)
                    return null;
                arguments.add(singleExpression);
            }
        }

        FunctionCall functionCall = new FunctionCall(name, simpleName);
        functionCall.setArguments(arguments);

        return functionCall;
    }

    private ArithmeticExpression resolveArithmeticExpression(String name) {
        Matcher matcher = patternArithmeticExpression.matcher(name);
        if (!matcher.find())
            return null;

        String operation = matcher.group(2);
        SingleExpression leftExpr = resolveSingleExpression(matcher.group(1));
        SingleExpression rightExpr = resolveSingleExpression(matcher.group(3));
        if (leftExpr == null || rightExpr == null)
            return null;

        return new ArithmeticExpression(name, operation, leftExpr, rightExpr);
    }

    private ConvertExpression resolveConvertExpression(String name) {
        Matcher matcher = patternConvertExpression.matcher(name);
        if (!matcher.find())
            return null;

        String toType = matcher.group(1);
        SingleVariable singleVariable = resolveSingleVariable(matcher.group(2));
        if (singleVariable == null)
            return null;

        return new ConvertExpression(name, toType, singleVariable);
    }

    private PHIExpression resolvePHIExpression(String name) {
        Matcher matcher = patternPHIExpression.matcher(name);
        if (!matcher.find())
            return null;

        SingleVariable leftExpr = resolveSingleVariable(matcher.group(1));
        SingleVariable fromExpr_1 = resolveSingleVariable(matcher.group(2));
        SingleVariable fromExpr_2 = resolveSingleVariable(matcher.group(5));
        int fromBlockId_1 = Integer.valueOf(matcher.group(4));
        int fromBlockId_2 = Integer.valueOf(matcher.group(7));
        if (fromExpr_1 == null || fromExpr_2 == null)
            return null;

        return new PHIExpression(name, leftExpr, fromExpr_1, fromExpr_2, fromBlockId_1, fromBlockId_2);
    }

    private ReturnExpression resolveReturnExpression(String name) {
        Matcher matcher = patternReturnExpression.matcher(name);
        if (!matcher.find())
            return null;

        String retExprStr = matcher.group(2);
        SingleExpression retExpr = null;
        if (retExprStr != null)
            retExpr = resolveSingleExpression(retExprStr);

        return new ReturnExpression(name.substring(0, name.length() - 1), retExpr);
    }

    private FunctionCall resolveFucntionCallInstruction(String name) {
        Matcher matcher = patternFuncitonCallInstruction.matcher(name);
        if (!matcher.find())
            return null;

        String functiolCallStr = name.substring(0, name.length() - 1);
        return resolveFunctionCallExpression(functiolCallStr);
    }

    private GotoExpression resolveGotoExpression(String name) {
        Matcher matcher = patternGotoExpression.matcher(name);
        if (!matcher.find())
            return null;

        String gotoBlockId = matcher.group(3);
        if (gotoBlockId == null)
            gotoBlockId = matcher.group(1);

        return new GotoExpression(name.substring(0, name.length() - 1), gotoBlockId);
    }

    private IfGotoExpression resolveIfGotoExpression(String name) {
        Matcher matcher = patternIfExpression.matcher(name);
        if (!matcher.find())
            return null;

        ConditionExpression conditionExpression = resolveConditionExpression(matcher.group(1));
        if (conditionExpression == null)
            return null;

        return new IfGotoExpression(name, conditionExpression);
    }

    private ConditionExpression resolveConditionExpression(String name) {
        Matcher matcher = patternConditionExpression.matcher(name);
        if (!matcher.find())
            return null;

        String compareOperation = matcher.group(2);
        SingleExpression leftExpr = resolveSingleExpression(matcher.group(1));
        SingleExpression rightExpr = resolveSingleExpression(matcher.group(3));
        if (leftExpr == null || rightExpr == null)
            return null;

        return new ConditionExpression(name, compareOperation, leftExpr, rightExpr);
    }

}
