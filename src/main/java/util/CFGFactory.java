package util;

import model.CFG;
import model.Function;

public class CFGFactory {
    private static CFGFactory instance;

    private CFGFactory() {
    }

    public static CFG make(Function function, int instStartLine, int instEndLine) {
        if (instance == null)
            instance = new CFGFactory();

        return instance._make(function, instStartLine, instEndLine);
    }

    private CFG _make(Function function, int instStartLine, int instEndLine) {

        return null;
    }
}
