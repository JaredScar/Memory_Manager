package com.jaredscarito.memory_manager.api;

import com.jaredscarito.memory_manager.api.spaces.ProcessBlock;

import java.util.ArrayList;

public class API {
    private static API api;
    public static API getInstance() {
        return api;
    }
    private ArrayList<ProcessBlock> processBlocks = new ArrayList<>();

    public void addBlock(String pid, int size, int startY) {
        processBlocks.add(new ProcessBlock(pid, size, startY));
    }
    public void removeBlock(ProcessBlock block) {
        block.destroy();
        int index = 0;
        for(ProcessBlock arrBlock : processBlocks) {
            if(arrBlock.getPID().equals(block.getPID())) {
                index++;
            }
        }
        processBlocks.remove(index);
    }
    public int[] getEmptySpace() {}
    public int[][] getEmptySpaces() {}
    public void compactMemory() {}

    public ArrayList<ProcessBlock> getProcessBlocks() {
        return processBlocks;
    }

    public void setProcessBlocks(ArrayList<ProcessBlock> processBlocks) {
        this.processBlocks = processBlocks;
    }
}
