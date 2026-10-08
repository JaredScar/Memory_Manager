package com.jaredscarito.memory_manager.api;

import com.jaredscarito.memory_manager.api.spaces.ProcessBlock;
import com.jaredscarito.memory_manager.main.Main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Jared Scarito
 */

public class API {
    private static final API api = new API();

    public static API getInstance() {
        return api;
    }

    /**
     * Process blocks currently drawn on the memory map. Their positions are how empty holes are found.
     */
    private ArrayList<ProcessBlock> processBlocks = new ArrayList<ProcessBlock>();

    public int getTotalMemSize() {
        return parseKb(Main.getTotalMemField().getText());
    }

    public int getOSFieldSize() {
        return parseKb(Main.getOsMemField().getText());
    }

    public String getSelectedPid() {
        String pid = Main.getPidBox().getValue();
        return pid == null ? "" : pid;
    }

    public int getInputMemSize() {
        return parseKb(Main.getProcessSizeField().getText());
    }

    public String getSelectedAlgorithm() {
        String algorithm = Main.getAlgoBox().getValue();
        return algorithm == null ? "" : algorithm;
    }

    private int parseKb(String text) {
        if (text == null) {
            return 0;
        }
        try {
            return Integer.parseInt(text.trim());
        } catch (NumberFormatException ex) {
            return 0;
        }
    }

    /**
     * Adds a process block. Returns false when the block is too small to draw.
     */
    public boolean addBlock(String pid, double size, double startY) {
        ProcessBlock block = new ProcessBlock(pid, size, startY);
        if (block.getSize() != 0) {
            processBlocks.add(block);
            return true;
        }
        return false;
    }

    public boolean hasProcessById(String pid) {
        return getProcessBlockById(pid) != null;
    }

    public ProcessBlock getProcessBlockById(String pid) {
        for (ProcessBlock block : processBlocks) {
            if (block.getPID().equalsIgnoreCase(pid)) {
                return block;
            }
        }
        return null;
    }

    public boolean removeBlock(ProcessBlock block) {
        if (block == null) {
            return false;
        }
        block.destroy();
        return processBlocks.remove(block);
    }

    public boolean removeBlock(String pid) {
        ProcessBlock blockToDelete = null;
        for (ProcessBlock block : processBlocks) {
            if (block.getPID().equals(pid)) {
                blockToDelete = block;
                break;
            }
        }
        if (blockToDelete == null) {
            return false;
        }
        blockToDelete.destroy();
        processBlocks.remove(blockToDelete);
        return true;
    }

    /**
     * Holes in the map, each as {startY, sizeKb}. Null when memory is completely full.
     */
    public double[][] getEmptySpaces() {
        if (processBlocks.isEmpty()) {
            return new double[][] {{0.0, getTotalMemSize()}};
        }

        HashMap<Double, Double> emptySpaces = new HashMap<Double, Double>();
        double top = MemoryLayout.VIEW_HEIGHT;
        for (ProcessBlock block : processBlocks) {
            top = Math.min(top, block.getStartY());
        }
        if (top > 0.5) {
            emptySpaces.put(0.0, 0.0);
        }
        for (ProcessBlock block : processBlocks) {
            double endY = block.getEndY();
            boolean touchesNext = false;
            for (ProcessBlock other : processBlocks) {
                if (endY == other.getStartY()) {
                    touchesNext = true;
                    break;
                }
            }
            if (!touchesNext && endY > 0) {
                emptySpaces.put(endY, 0.0);
            }
        }

        double totalKb = getTotalMemSize();
        for (Double emptyStartY : new ArrayList<Double>(emptySpaces.keySet())) {
            double yCursor = emptyStartY;
            while (yCursor < MemoryLayout.VIEW_HEIGHT) {
                yCursor += 1;
                boolean foundEnd = false;
                for (ProcessBlock block : processBlocks) {
                    if (Math.ceil(block.getStartY()) == Math.ceil(yCursor)) {
                        double size = MemoryLayout.kbForPixels(block.getStartY() - emptyStartY, totalKb);
                        emptySpaces.put(emptyStartY, size);
                        foundEnd = true;
                        break;
                    }
                }
                if (foundEnd) {
                    break;
                }
                if (yCursor >= MemoryLayout.VIEW_HEIGHT) {
                    double size = MemoryLayout.kbForPixels(Math.floor(yCursor) - emptyStartY, totalKb);
                    emptySpaces.put(emptyStartY, size);
                }
            }
        }

        if (emptySpaces.isEmpty()) {
            return null;
        }
        double[][] holes = new double[emptySpaces.size()][2];
        int index = 0;
        for (Map.Entry<Double, Double> entry : emptySpaces.entrySet()) {
            holes[index][0] = entry.getKey();
            holes[index][1] = entry.getValue();
            index++;
        }
        return holes;
    }

    /**
     * Slides every block toward address 0, keeping their order, so free memory becomes one hole at the bottom.
     */
    public boolean compactMemory() {
        ArrayList<ProcessBlock> ordered = new ArrayList<ProcessBlock>(processBlocks);
        ordered.sort((left, right) -> Double.compare(left.getStartY(), right.getStartY()));

        String[] pids = new String[ordered.size()];
        int[] sizes = new int[ordered.size()];
        for (int i = 0; i < ordered.size(); i++) {
            pids[i] = ordered.get(i).getPID();
            sizes[i] = ordered.get(i).getDisplaySize();
        }
        for (String pid : pids) {
            removeBlock(pid);
        }

        double cursor = 0;
        for (int i = 0; i < pids.length; i++) {
            if (!addBlock(pids[i], sizes[i], cursor)) {
                return false;
            }
            ProcessBlock placed = getProcessBlockById(pids[i]);
            if (placed == null) {
                return false;
            }
            cursor = placed.getEndY();
        }

        double[][] holes = getEmptySpaces();
        return holes == null || holes.length <= 1;
    }

    public ArrayList<ProcessBlock> getProcessBlocks() {
        return processBlocks;
    }

    public void setProcessBlocks(ArrayList<ProcessBlock> processBlocks) {
        this.processBlocks = processBlocks;
    }
}
