package model.block;

import model.CFG;

public class ExitBlock extends BasicBlock {
    public ExitBlock(CFG cfg) {
        super("Exit", cfg);
    }
}
