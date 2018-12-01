package com.jaredscarito.memory_manager.api;

import com.jaredscarito.memory_manager.api.spaces.ProcessBlock;
import com.jaredscarito.memory_manager.main.Main;

import java.util.ArrayList;
import java.util.HashMap;

public class API {
    private static API api = new API();
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
    public String getSelectedPid() {
        return (String) Main.getPidBox().getSelectionModel().getSelectedItem();
    }
    public int getInputMemSize() {
        return Integer.parseInt(Main.getProcessSizeField().getText());
    }
    public String getSelectedAlgorithm() {
        return (String) Main.getAlgoBox().getSelectionModel().getSelectedItem();
    }

    /**
     *
     * @param pid - Process ID
     * @param size - Size of process
     * @param startY - Position of where the ProcessBlock is displayed
     * Adds a ProcessBlock instance to the processBlocks ArrayList with the specified params
     * pid, size, and startY
     */
    public boolean addBlock(String pid, double size, double startY) {
        ProcessBlock p = new ProcessBlock(pid, size, startY);
        if(p.getSize() != 0) {
            processBlocks.add(p);
            return true;
        }
        return false;
    }

    public boolean hasProcessById(String pid) {
        for(ProcessBlock block : processBlocks) {
            if(block.getPID().equalsIgnoreCase(pid)) {
                return true;
            }
        }
        return false;
    }

    /**
     *
     * @param block
     * @return isDeleted
     * Removes a ProcessBlock from the processBlocks ArrayList
     */
    public boolean removeBlock(ProcessBlock block) {
        boolean isDeleted = false;
        block.destroy();
        int index = 0;
        for(ProcessBlock arrBlock : processBlocks) {
            if(arrBlock.getPID().equals(block.getPID())) {
                isDeleted = true;
                break;
            }
            index++;
        }
        if(isDeleted) {
            processBlocks.remove(index);
        }
        return isDeleted;
    }

    /**
     *
     * @param pid
     * @return boolean
     */
    public boolean removeBlock(String pid) {
        ProcessBlock blockToDel = null;
        for(ProcessBlock block : processBlocks) {
            if(block.getPID().equals(pid)) {
                block.destroy();
                blockToDel = block;
            }
        }
        if(blockToDel != null) {
            processBlocks.remove(blockToDel);
            return true;
        }
        return false;
    }

    /**
     * size = ( (600 / totalMemSize) * size )
     * @return int[] {startY, size}
     *
     * @return null if there are no empty spaces
     */
    @Deprecated
    public double[] getEmptySpace() {
        double emptyStartY = -1;
        if(processBlocks.size() == 0) return new double[] {0, getTotalMemSize()};

        for(ProcessBlock block1 : processBlocks) {
            boolean isEmpty = true;
            for(ProcessBlock block2 : processBlocks) {
                double endY1 = block1.getEndY();
                double startY2 = block2.getStartY();
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
        double emptyEndY = 580;
        double startYCout = emptyStartY;
        if(startYCout > -1) {
            while (startYCout < 580) {
                startYCout += 1;
                for(ProcessBlock block : processBlocks) {
                    if(block.getStartY() == startYCout) {
                        // This is where the hole ends
                        emptyEndY = block.getStartY();
                    }
                }
            }
        }
        double size = (double) (( ((double) emptyEndY - (double) emptyStartY) / (double) 580 ) * Double.parseDouble(Main.getTotalMemField().getText()));
        return new double[] {emptyStartY, size};
    }

    /**
     * size = ( (600 / totalMemSize) * size )
     * @return int[][] {{startY, size}, {startY2, size2}}
     *
     * @return null if there are no empty spaces
     */
    public double[][] getEmptySpaces() {
        HashMap<Double, Double> emptySpaces = new HashMap<>(); // startY, size
        if(processBlocks.size() < 1) return new double[][] {{0.0, getTotalMemSize()}};

        for(ProcessBlock block1 : processBlocks) {
            boolean isEmpty = true;
            double endY1 = -1;
            for(ProcessBlock block2 : processBlocks) {
                endY1 = block1.getEndY();
                double startY2 = block2.getStartY();
                if(endY1 == startY2) {
                    isEmpty = false;
                }
            }
            if(isEmpty) {
                if(endY1 > 0) {
                    emptySpaces.put(endY1, 0.0);
                }
            }
        }
        for(Double emptyStartY : emptySpaces.keySet()) {
            double yCout = emptyStartY;
            while(yCout < 580) {
                yCout += 1;
                boolean foundEnd = false;
                for(ProcessBlock block : processBlocks) {
                    //System.out.println("Comparing " + Math.ceil(block.getStartY()) + " to " + Math.ceil(yCout)); // TODO - debug - get rid of
                    if(Math.ceil(block.getStartY()) == Math.ceil(yCout)) {
                        // Hole ends here
                        double size = ( ( block.getStartY() -  emptyStartY) / (double) 580 ) * Double.parseDouble(Main.getTotalMemField().getText());
                        /* Debugs * /
                        System.out.println("EmptySpaces - block.getStartY(): " + block.getStartY() + " | emptyStartY: "
                                + emptyStartY +  " | TotalMemField: " + Double.parseDouble(Main.getTotalMemField().getText()));
                        System.out.println("emptyStartY: " + emptyStartY + "Size: " + size); // TODO - debug - get rid of
                        /**/
                        emptySpaces.put(emptyStartY, size);
                        foundEnd = true;
                        break;
                    }
                }
                if(foundEnd) break;
                if(yCout >= 580) {
                    /* Debugs * /
                    System.out.println("EmptySpaces - yCout: " + yCout + " | emptyStartY: "
                            + emptyStartY +  " | TotalMemField: " + Double.parseDouble(Main.getTotalMemField().getText()));
                    /**/
                    double size = (( (yCout - emptyStartY) / (double) 580 ) * Double.parseDouble(Main.getTotalMemField().getText()));
                    emptySpaces.put(emptyStartY, size);
                }
            }
        }
        double[][] emptySpacesArr = new double[emptySpaces.size()][2];
        int cout = 0;
        for(Double startY : emptySpaces.keySet()) {
            emptySpacesArr[cout][0] = startY; // startY
            emptySpacesArr[cout][1] = (double) emptySpaces.values().toArray()[cout]; // Size
            cout++;
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
