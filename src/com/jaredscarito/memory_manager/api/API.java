package com.jaredscarito.memory_manager.api;

import com.jaredscarito.memory_manager.api.spaces.ProcessBlock;
import com.jaredscarito.memory_manager.main.Main;

import java.util.ArrayList;
import java.util.HashMap;

public class API {
    private static API api;
    public static API getInstance() {
        return api;
    }

    /**
     * This ArrayList keeps track of the ProcessBlocks being displayed and used them to figure out where
     * the empty spaces are within the graphic.
     */
    private ArrayList<ProcessBlock> processBlocks = new ArrayList<>();

    public int getTotalMemSize() {
        return Integer.parseInt(Main.getTotalMemField().getText());
    }

    public int getOSFieldSize() {
        return Integer.parseInt(Main.getOsMemField().getText());
    }

    /**
     *
     * @param pid - Process ID
     * @param size - Size of process
     * @param startY - Position of where the ProcessBlock is displayed
     * Adds a ProcessBlock instance to the processBlocks ArrayList with the specified params
     * pid, size, and startY
     */
    public void addBlock(String pid, int size, int startY) {
        processBlocks.add(new ProcessBlock(pid, size, startY));
    }

    /**
     *
     * @param block
     * Removes a ProcessBlock from the processBlocks ArrayList
     */
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

    /**
     * size = ( (600 / totalMemSize) * size )
     * @return int[] {startY, size}
     *
     * @return null if there are no empty spaces
     */
    public int[] getEmptySpace() {
        int emptyStartY = -1;
        if(processBlocks.size() < 1) return new int[] {0, 600};

        for(ProcessBlock block1 : processBlocks) {
            boolean isEmpty = true;
            for(ProcessBlock block2 : processBlocks) {
                int endY1 = block1.getEndY();
                int startY2 = block2.getStartY();
                emptyStartY = endY1;
                if(endY1 == startY2) {
                    isEmpty = false;
                }
            }
            if(isEmpty) {
                break;
            }
        }
        if(emptyStartY == -1) return null;
        // We need to find size of it now
        int size;
        int emptyEndY = 600;
        int startYCout = emptyStartY;
        if(startYCout > -1) {
            while (startYCout < 600) {
                startYCout += 1;
                for(ProcessBlock block : processBlocks) {
                    if(block.getStartY() == startYCout) {
                        // This is where the hole ends
                        emptyEndY = block.getStartY();
                    }
                }
            }
        }
        size = ( (emptyEndY - emptyStartY) / 600 ) * Integer.parseInt(Main.getTotalMemField().getText());
        return new int[] {emptyStartY, size};
    }

    /**
     * size = ( (600 / totalMemSize) * size )
     * @return int[][] {{startY, size}, {startY2, size2}}
     *
     * @return null if there are no empty spaces
     */
    public int[][] getEmptySpaces() {
        HashMap<Integer, Integer> emptySpaces = new HashMap<>(); // startY, size
        if(processBlocks.size() < 1) return new int[][] {{0, 600}};

        for(ProcessBlock block1 : processBlocks) {
            boolean isEmpty = true;
            int endY1 = -1;
            for(ProcessBlock block2 : processBlocks) {
                endY1 = block1.getEndY();
                int startY2 = block2.getStartY();
                if(endY1 == startY2) {
                    isEmpty = false;
                }
            }
            if(isEmpty) {
                if(endY1 >= 0) {
                    emptySpaces.put(endY1, 0);
                }
            }
        }
        for(Integer emptyStartY : emptySpaces.keySet()) {
            int yCout = emptyStartY;
            while(yCout < 600) {
                yCout += 1;
                for(ProcessBlock block : processBlocks) {
                    if(block.getStartY() == yCout) {
                        // Hole ends here
                        int size = ( ((yCout - emptyStartY) / 600) * Integer.parseInt(Main.getTotalMemField().getText()));
                        emptySpaces.put(emptyStartY, size);
                    }
                }
            }
        }
        int[][] emptySpacesArr = new int[emptySpaces.size()][2];
        int cout = 0;
        for(Integer startY : emptySpaces.keySet()) {
            emptySpacesArr[cout][0] = startY; // startY
            emptySpacesArr[cout][1] = emptySpaces.get(new Integer(startY)); // Size
        }
        if(emptySpaces.size() > 0) {
            return emptySpacesArr;
        }
        return null;
    }

    /**
     * Moves all the memory in the graphical display up
     */
    public void compactMemory() {}

    /**
     *
     * @return
     */
    public ArrayList<ProcessBlock> getProcessBlocks() {
        return processBlocks;
    }

    /**
     *
     * @param processBlocks
     */
    public void setProcessBlocks(ArrayList<ProcessBlock> processBlocks) {
        this.processBlocks = processBlocks;
    }
}
