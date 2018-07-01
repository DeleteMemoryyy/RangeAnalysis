package model.block;

import model.CFG;

public class EntryBlock extends BasicBlock {
    public EntryBlock(CFG cfg) {
        super("Entry", cfg);
    }
}
