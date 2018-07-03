package model.block;

import model.CFG;
import model.instructioin.Expression;

import java.util.List;

public class BasicBlock {
    private String id;
    private String name;
    private List<Expression> instructionList;
    private CFG cfg;

    public BasicBlock(String id, CFG cfg) {
        this.id = id;
        this.name = "<" + id + ">";
        this.cfg = cfg;
    }

    public String getId() {
        return id;

    }

    public String getName() {
        return name;
    }

    public List<Expression> getInstructionList() {
        return instructionList;
    }

    public void setInstructionIist(List<Expression> instructionList) {
        this.instructionList = instructionList;
    }

    public void setCfg(CFG cfg) {
        this.cfg = cfg;
    }

    public CFG getCfg() {
        return cfg;
    }

    public Expression getFirstinstruction() {
        if (instructionList == null)
            return null;
        return instructionList.get(0);
    }

    public Expression getLastInstruction() {
        if (instructionList == null || instructionList.isEmpty())
            return null;
        return instructionList.get(instructionList.size() - 1);
    }

    public int getInstructionCount() {
        if (instructionList == null)
            return 0;
        return instructionList.size();
    }

    @Override
    public String toString() {
        return name;
    }
}
