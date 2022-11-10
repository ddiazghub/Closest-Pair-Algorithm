/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package closestpair.result;

/**
 * Resulting data from the execution of an algorithm
*/
public class ExecutionResult {
    private final long iterations;
    private final long time;

    public ExecutionResult(long iterations, long time) {
        this.iterations = iterations;
        this.time = time;
    }

    public long getTime() {
        return this.time;
    }

    public long getIterations() {
        return this.iterations;
    }
}
