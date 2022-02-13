package com.ml.quasar.service.location;

import java.util.List;

import org.apache.commons.math3.fitting.leastsquares.LevenbergMarquardtOptimizer;
import org.springframework.stereotype.Service;

import com.lemmingapex.trilateration.NonLinearLeastSquaresSolver;
import com.lemmingapex.trilateration.TrilaterationFunction;
import com.ml.quasar.model.vo.PositionVo;
import com.ml.quasar.model.vo.SatelliteVo;

@Service
public class LocationService implements I_LocationService {
	
	public double[] getLocation(List<PositionVo> positions, List<SatelliteVo> satellites) {
		
		double[] point1 = new double[] {positions.get(0).getX(), positions.get(0).getY()};
		double[] point2 = new double[] {positions.get(1).getX(), positions.get(1).getY()};
		double[] point3 = new double[] {positions.get(2).getX(), positions.get(2).getY()};
		double[][] positionsToCalculate = new double[][] { point1, point2, point3 };
		
		double[] distances = this.getDistancesFromSatellites(satellites);
        TrilaterationFunction trilaterationFunction = new TrilaterationFunction(positionsToCalculate, distances);
        NonLinearLeastSquaresSolver nSolver = new NonLinearLeastSquaresSolver(trilaterationFunction, new LevenbergMarquardtOptimizer());
        return nSolver.solve().getPoint().toArray();
    }

	public double[] getHandLocation(List<PositionVo> positions, List<SatelliteVo> satellites) {
		double[] distances = this.getDistancesFromSatellites(satellites);
		
		return _getHandLocation(positions, distances);
	}
	
	private double[] getDistancesFromSatellites(List<SatelliteVo> satellites) {
		double[] distances = new double[satellites.size()];
		// We get the distances
		for (int i = 0; i < satellites.size(); i++) {
			distances[i] = satellites.get(i).getDistance();
		}
		return distances;
	}

	public double[] _getHandLocation(List<PositionVo> positions, double[] distances) {

		double[] point1 = new double[] {positions.get(0).getX(), positions.get(0).getY()};
		double[] point2 = new double[] {positions.get(1).getX(), positions.get(1).getY()};
		double[] point3 = new double[] {positions.get(2).getX(), positions.get(2).getY()};

		double[] ex = new double[2];
		double[] ey = new double[2];
		double[] substractP3P1 = new double[2];

		double jResult = 0;
		double powSubstractP2P1 = 0;
		double iResult = 0;
		double divisionP3P1i = 0;

		double resultX;
		double resultY;

		double xval;
		double yval;
		double divisionResult;

		double distance1 = distances[0];
		double distance2 = distances[1];
		double distance3 = distances[2];
		
		/**
		 *  We are supported by a formula to do this calculus, it is:
		 * 
		 *  FORMULA
		 *
		 *	(x0−x1)^2 + (y0−y1)^2 = d1^2
		 *	(x0−x2)^2 + (y0−y2)^2 = d2^2
		 *	(x0−x3)^2 + (y0−y3)^2 = d2^3
		 *	
		 *	where x0 = ? and y0 = ?, we don't have this information.
		 *
		 *  For example for the points P1 = (-500,-200), P2 = (100,-100), P3 = (500,100), and distances d1 = 100, d2 = 115.5, d3 = 142.7, we have something like this:
		 *
		 *	(x0 + 500)^2 + (y0 + 200)^2 = 100.0^2
		 *	(x0 - 100)^2 + (y0 + 100)^2 = 115.5^2
		 *  (x0 - 500)^2 + (y0 - 100)^2 = 142.7^2
		 * 
		 *  We know that this expression, ex = (P2 - P1) / ‖P2 - P1‖ means:
         *
		 *	ex,x = (P2x - P1x) / sqrt((P2x - P1x)2 + (P2y - P1y)2)
		 *	ex,y = (P2y - P1y) / sqrt((P2x - P1x)2 + (P2y - P1y)2)
         *
         *  And the calculation steps are:
         * 
		 *  ex = (P2 - P1) / ‖P2 - P1‖
		 *  iResult = ex(P3 - P1)
		 *  ey = (P3 - P1 - iResult · ex) / ‖P3 - P1 - iResult · ex‖ -> (P3x - P1x - iResult * ex) / sqrt((P3x - P1x - iResult · ex)^2 + (P3y - P3y - iResult · ex)^2) 
		 *  divisionResult = ‖P2 - P1‖ // This is a partial calculus in the algorithm
		 *  jResult = ey(P3 - P1)
		 *  xval = (r1^2 - r2^2 + divisionResult^2) / 2 * divisionResult
		 *  yval = (r1^2 - r3^2 + i^2 + jResult^2) / 2 * jResult - ix / j
		 * 
		 */
		
		/**
		 * A generic way to do that, because we have to repeat for "x" and "y" the same calculus,
		 * If not, we have something like that
		 * 
		 * d = Math.sqrt(Math.pow(P2[0] - P1[0], 2) + Math.pow(P2[1] - P1[1], 2));
		 * and
		 * ex[0] = (P2[0] - P1[0]) / d; ex[1] = (P2[1] - P1[1]) / d;
		 * 
		 * So, we repeat every time the same calculus for pos 0 and 1, or "x" and "y"
		 * 
		 */
		
		for (int i = 0; i < point1.length; i++) powSubstractP2P1 += Math.pow(point2[i] - point1[i], 2);
		divisionResult = Math.sqrt(powSubstractP2P1);
		for (int i = 0; i < point1.length; i++) ex[i] = (point2[i] - point1[i]) / divisionResult;
		for (int i = 0; i < point3.length; i++) substractP3P1[i] = point3[i] - point1[i];
//		for (int i = 0; i < ex.length; i++) iResult += Math.pow(ex[i] - substractP3P1[i], 2);
		for (int i = 0; i < ex.length; i++) iResult += ex[i] * substractP3P1[i];
		for (int i = 0; i < ex.length; i++) divisionP3P1i += Math.pow(point3[i] - point1[i] - iResult * ex[i], 2);
		for (int i = 0; i < point3.length; i++) ey[i] += (point3[i] - point1[i] - ex[i] * iResult) / Math.sqrt(divisionP3P1i);
//		for (int i = 0; i < ey.length; i++) jResult += Math.pow(ey[i] - substractP3P1[i], 2);
		for (int i = 0; i < ey.length; i++) jResult += ey[i] * substractP3P1[i];
		
		xval = (Math.pow(distance1, 2) - Math.pow(distance2, 2) + Math.pow(divisionResult, 2)) / (2 * divisionResult);
//		yval = ((Math.pow(distance1, 2) - Math.pow(distance3, 2) + Math.pow(iResult, 2) + Math.pow(jResult, 2)) / (2 * jResult)) - ((iResult / jResult) * xval);
		yval = ((Math.pow(distance1, 2) - Math.pow(distance3, 2) + Math.pow(iResult, 2) + Math.pow(jResult, 2)) / (2 * jResult)) - ((iResult*xval / jResult));

		resultX = (ex[0] * xval) + (ey[0] * yval);
		resultY = (ex[1] * xval) + (ey[1] * yval);

		double[] result = new double[2];
		result[0] = resultX;
		result[1] = resultY;
		return result;

    }
	
}
