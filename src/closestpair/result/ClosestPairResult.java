/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package closestpair.result;

import closestpair.Point2D;

/**
 * Resulting data from the execution of the closest pair algorithm
*/
public class ClosestPairResult extends ExecutionResult {
    private final Point2D point1;
    private final Point2D point2;
    private final double distance;

    public ClosestPairResult(Point2D point1, Point2D point2, double distance, long iterations, long time) {
        super(iterations, time);
        this.point1 = point1;
        this.point2 = point2;
        this.distance = distance;
    }

    public Point2D getPoint1() {
        return this.point1;
    }

    public Point2D getPoint2() {
        return this.point2;
    }

    public double getDistance() {
        return this.distance;
    }
}
