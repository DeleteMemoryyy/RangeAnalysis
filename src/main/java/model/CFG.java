package model;

import model.block.BasicBlock;
import model.instructioin.Expression;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CFG {

    private Function function;
    private int instStartLine;
    private int instEndLine;

    private List<Expression> instructions;
    private List<BasicBlock> blocks;
    private Map<String, BasicBlock> blockMap;
    private Map<BasicBlock, Set<BasicBlock>> successors;
    private Map<BasicBlock, Set<BasicBlock>> precursors;

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

    public Map<BasicBlock, Set<BasicBlock>> getSuccessors() {
        return successors;
    }

    public void setSuccessors(Map<BasicBlock, Set<BasicBlock>> successors) {
        this.successors = successors;
    }

    public BasicBlock getFirstBlock() {
        return blocks.get(0);
    }

    public BasicBlock getLastBlock() {
        return blocks.get(blocks.size() - 1);
    }

    public Map<BasicBlock, Set<BasicBlock>> getPrecursors() {
        return precursors;
    }

    public void setPrecursors(Map<BasicBlock, Set<BasicBlock>> precursors) {
        this.precursors = precursors;
    }

    public Iterator<BasicBlock> getSuccNodes(BasicBlock block) {
        return successors.get(block).iterator();
    }

    public int getSuccNodeCount(BasicBlock block) {
        return successors.get(block).size();
    }

    public Iterator<BasicBlock> getPredNodes(BasicBlock block) {
        return precursors.get(block).iterator();
    }

    public int getPredNodeCount(BasicBlock block) {
        return precursors.get(block).size();
    }

    public boolean containsNode(BasicBlock block) {
        return blocks.contains(block);
    }

}
