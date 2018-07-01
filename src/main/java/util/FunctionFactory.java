package util;

import model.CFG;
import model.Function;
import model.TranslateUnit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FunctionFactory {
    private static FunctionFactory instance;

    private TranslateUnit currentTU;
    private int currentStartLine;
    private int currentEndLine;

    Pattern patternFunctionDecleration = Pattern.compile("\\A;; Function ([A-Za-z_][A-Za-z0-9_]*) \\(([A-Za-z_][A-Za-z0-9_]*), funcdef_no=([0-9]*), decl_uid=([0-9]*), cgraph_uid=([0-9]*), symbol_order=([0-9]*)\\)\\Z");
    Pattern patternFunctionDefnition = Pattern.compile("\\A[A-Za-z_][A-Za-z0-9_]* \\([A-Za-z0-9_, ]*\\)\\Z");
    Pattern patternVariableDecleration = Pattern.compile("([A-Za-z_][A-Za-z0-9_]*) ([A-Za-z_][A-Za-z0-9_.]*)");

    private FunctionFactory() {
    }

    public static FunctionFactory getFactory() {
        if (instance == null)
            instance = new FunctionFactory();

        return instance;
    }

    public void init(TranslateUnit translateUnit) {
        currentTU = translateUnit;
        currentEndLine = currentTU.getLineNum();
        currentStartLine = 0;
    }

    public boolean hasNextFunction() {
        return currentStartLine < currentEndLine;
    }

    public Function make() {
        int currentLineNum = currentStartLine;
        int funcStartLine = currentStartLine + 1;
        String declLine = currentTU.getLine(currentLineNum);
        Matcher matcherFunctionDecleration = patternFunctionDecleration.matcher(declLine);
        if (!matcherFunctionDecleration.find())
            return null;
        if (!matcherFunctionDecleration.group(1).equals(matcherFunctionDecleration.group(2)))
            return null;

        String simpleName = matcherFunctionDecleration.group(1);
        int defNum = Integer.valueOf(matcherFunctionDecleration.group(3));
        int declUid = Integer.valueOf(matcherFunctionDecleration.group(4));
        int cgraphUid = Integer.valueOf(matcherFunctionDecleration.group(5));
        int symbolOrder = Integer.valueOf(matcherFunctionDecleration.group(6));

        List<String> argumentList = new ArrayList<>();
        Map<String, String> transTable = new HashMap<>();

        // Process arguments
        String defLine = currentTU.getLine(++currentLineNum);
        Matcher matcherFunctionDefnition = patternFunctionDefnition.matcher(defLine);
        if (!matcherFunctionDefnition.find())
            return null;
        String argumentsStr = matcherFunctionDefnition.group();

        Matcher matcherArguments = patternVariableDecleration.matcher(argumentsStr);
        while (matcherArguments.find()) {
            argumentList.add(matcherArguments.group(1));
            transTable.put(matcherArguments.group(2), matcherArguments.group(1));
        }

        String tmpLine = currentTU.getLine(++currentLineNum);
        if (!"{".equals(tmpLine))
            return null;

        int varDeclStartLine = currentLineNum + 1;
        int endLineNum = currentLineNum;
        while (++endLineNum < currentEndLine)
            if ("}".equals(currentTU.getLine(endLineNum)))
                break;
        endLineNum++;
        if (endLineNum > currentEndLine)
            return null;

        // Process variable declarations
        while (varDeclStartLine < endLineNum - 1) {
            tmpLine = currentTU.getLine(varDeclStartLine);
            if (tmpLine.startsWith("<"))
                break;
            Matcher matcherVariableDecleration = patternVariableDecleration.matcher(tmpLine);
            if (matcherVariableDecleration.find() && !
                    matcherVariableDecleration.group(2).startsWith("D.")) {
                transTable.put(matcherVariableDecleration.group(2), matcherVariableDecleration.group(1));
            }

            varDeclStartLine++;
        }

        currentStartLine = endLineNum;

        Function function = new Function(currentTU, funcStartLine, endLineNum, simpleName, defNum, declUid, cgraphUid, symbolOrder);
        function.setArgumentList(argumentList);
        function.setTransTable(transTable);

        CFG cfg = CFGFactory.make(function, varDeclStartLine, endLineNum - 1);
        function.setCfg(cfg);

        return function;
    }

}
