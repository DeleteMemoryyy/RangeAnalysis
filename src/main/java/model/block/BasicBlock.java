package model.block;

import model.CFG;

import java.util.List;

public class BasicBlock {
    private String id;
    private List<Integer> instructionId;
    private CFG cfg;

    public BasicBlock(String id, CFG cfg) {
        this.id = id;
        this.cfg = cfg;
    }

    public String getId() {
        return id;
    }

    public List<Integer> getInstructionId() {
        return instructionId;
    }

    public void setInstructionId(List<Integer> instructionId) {
        this.instructionId = instructionId;
    }

    public void setCfg(CFG cfg) {
        this.cfg = cfg;
    }

    public CFG getCfg() {
        return cfg;
    }

    public int getFirstinstructionId() {
        return instructionId.get(0);
    }

    public int getLastInstructionId() {
        return instructionId.get(instructionId.size() - 1);
    }

    public int getInstructionCount() {
        if (instructionId == null)
            return 0;
        return instructionId.size();
    }

    @Override
    public String toString() {
        return id;
    }
}
