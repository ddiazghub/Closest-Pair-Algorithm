/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package closestpair.result;

import closestpair.SpacePartition2D;
import java.util.ArrayList;

/**
 * Resulting data from partitioning points
*/
public class SpacePartitionResult extends ExecutionResult {
    public ArrayList<SpacePartition2D> partitions;

    public SpacePartitionResult(ArrayList<SpacePartition2D> partitions, long iterations, long time) {
        super(iterations, time);
        this.partitions = partitions;
    }

    public ArrayList<SpacePartition2D> getPartitions() {
        return this.partitions;
    }
}