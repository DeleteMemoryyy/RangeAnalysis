package model;

import model.block.BasicBlock;
import model.instructioin.Expression;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class CFG {

    private Function function;
    private int instStartLine;
    private int instEndLine;

    private List<Expression> instructions;
    private List<BasicBlock> blocks;
    private Map<String, BasicBlock> blockMap;
    private Map<BasicBlock, List<BasicBlock>> successors;
    private Map<BasicBlock, List<BasicBlock>> precursors;

    public CFG(Function function, int instStartLine, int instEndLine) {
        this.function = function;
        this.instStartLine = instStartLine;
        this.instEndLine = instEndLine;
    }

    public Function getFunction() {
        return function;
    }

    public int getInstStartLine() {
        return instStartLine;
    }

    public int getInstEndLine() {
        return instEndLine;
    }

    public List<Expression> getInstructions() {
        return instructions;
    }

    public void setInstructions(List<Expression> instructions) {
        this.instructions = instructions;
    }

    public Iterator<Expression> getInstructionsIterator() {
        return instructions.iterator();
    }

    public int getInstructionNum() {
        return instructions.size();
    }

    public Expression getInstruction(int index) {
        return instructions.get(index);
    }

    public List<BasicBlock> getBlocks() {
        return blocks;
    }

    public void setBlocks(List<BasicBlock> blocks) {
        this.blocks = blocks;
    }

    public Map<String, BasicBlock> getBlockMap() {
        return blockMap;
    }

    public void setBlockMap(Map<String, BasicBlock> blockMap) {
        this.blockMap = blockMap;
    }

    public Map<BasicBlock, List<BasicBlock>> getSuccessors() {
        return successors;
    }

    public void setSuccessors(Map<BasicBlock, List<BasicBlock>> successors) {
        this.successors = successors;
    }

    public BasicBlock getEntryBlock() {
        return blocks.get(0);
    }

    public BasicBlock getExitBlock() {
        return blocks.get(blocks.size() - 1);
    }

    public Map<BasicBlock, List<BasicBlock>> getPrecursors() {
        return precursors;
    }

    public void setPrecursors(Map<BasicBlock, List<BasicBlock>> precursors) {
        this.precursors = precursors;
    }

    public List<BasicBlock> getSuccNodes(BasicBlock block) {
        return successors.get(block);
    }

    public int getSuccNodeCount(BasicBlock block) {
        return successors.get(block).size();
    }

    public List<BasicBlock> getPredNodes(BasicBlock block) {
        return precursors.get(block);
    }

    public int getPredNodeCount(BasicBlock block) {
        return precursors.get(block).size();
    }

    public boolean containsNode(BasicBlock block) {
        return blocks.contains(block);
    }

}
