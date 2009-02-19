/***************************************************************************
 *   Copyright (C) 2007-2008 by Jan-David Quesel                                *
 *   quesel@informatik.uni-oldenburg.de                                    *
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU General Public License as published by  *
 *   the Free Software Foundation; either version 2 of the License, or     *
 *   (at your option) any later version.                                   *
 *                                                                         *
 *   This program is distributed in the hope that it will be useful,       *
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of        *
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the         *
 *   GNU General Public License for more details.                          *
 *                                                                         *
 *   You should have received a copy of the GNU General Public License     *
 *   along with this program; if not, write to the                         *
 *   Free Software Foundation, Inc.,                                       *
 *   59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.             *
 ***************************************************************************/
package de.uka.ilkd.key.dl.arithmetics.impl.csdp;

import java.util.Arrays;
import java.util.Random;

/**
 * @author jdq
 * 
 */
public class CSDP {
    static {
        System.loadLibrary("csdp");
    }

    /**
     * Input Parameters:
     * 
     * @param n
     *            gives the dimension of the X, C, and Z matrices.
     * @param k
     *            gives the number of constraints.
     * @param blockmatrixC
     *            gives the C matrix and implicitly defines the block structure
     *            of the block diagonal matrices.
     * @param a
     *            gives the right hand side vector a.
     * @param constraints
     *            specifies the problem constraints.
     * @param constant_offset
     *            This scalar is added to the primal and dual objective values.
     * 
     * 
     * @param blockmatrixpX
     *            On input, this parameter gives the initial primal solution X.
     *            On output, it gives the optimal primal solution X
     * @param py
     *            On input, this parameter gives the initial dual solution y. On
     *            output, it gives the optimal dual solution y
     * @param blockmatrixpZ
     *            On input, this parameter gives the initial dual solution Z. On
     *            output, it gives the optimal dual solution Z
     * @param ppobj
     *            gives the optimal primal objective value
     * @param pdobj
     *            gives the optimal dual objective value
     * @return <ul>
     *         <li>0: Success. Problem is solvable
     *         <li>1: Success. The problem is primal infeasible.
     *         <li>2: Success. The problem is dual infeasible.
     *         <li>3: Partial Success: A solution has been found, but full
     *         accuracy was not achieved. One or more of primal infeasibility,
     *         dual infeasibility, or relative duality gap are larger than their
     *         tolerances, but by a factor of less than 1000.
     *         <li>4: Failure. Maximum iterations reached.
     *         <li>5: Failure. Stuck at edge of primal feasibility.
     *         <li>6: Failure. Stuck at edge of dual infeasibility.
     *         <li>7: Failure. Lack of progress.
     *         <li>8: Failure. X, Z, or O was singular.
     *         <li>9: Failure. Detected NaN or Inf values.
     *         </ul>
     */
    private static native int easySDP(int n, int k, double[] blockmatrixC,
            double[] a, double[] constraints, double constant_offset,
            double[] blockmatrixpX, double[] py, double[] blockmatrixpZ,
            double[] ppobj, double[] pdobj);

    @SuppressWarnings("unused")
    private static native int test2(int n, int k, double[] blockmatrixC,
            double[] a, double[] constraints, double constant_offset,
            double[] blockmatrixpX, double[] py, double[] blockmatrixpZ,
            double[] ppobj, double[] pdobj);

    private static native int test();

    private static final double BIG_EPS = 0.00001;

    private static boolean isAlmostNothing(double x) {
        return x < BIG_EPS && x > -BIG_EPS;
    }

    /**
     * SDP method with a simpler signature, for convenience reasons.
     * 
     * <p>IMPORTANT: <code>constraints</code> has to be a sequence of upper
     *            triangular matrices, otherwise CSDP will do the most stupid
     *            things</p>
     */
    private static int easiestSDP(int matrixSize,
                                  double[] constraints, double[] constraintRhs,
                                  double[] solution,
                                  double[] goal) {
        assert (solution.length == matrixSize * matrixSize &&
                goal.length == matrixSize * matrixSize);
        final int k = constraints.length / (matrixSize * matrixSize);
        final double[] y = new double[constraintRhs.length];
        final double[] Z = emptyGoal(matrixSize);
        final double[] pobj = new double[matrixSize];
        final double[] dobj = new double[constraintRhs.length];

        // not sure whether this helps
        Arrays.fill(y, 0.1);
        Arrays.fill(Z, 0.1);

        return easySDP(matrixSize, k, goal, constraintRhs, constraints, 0,
                       solution, y, Z, pobj, dobj);
    }

    public static int sdp(int matrixSize, double[] constraints,
                          double[] constraintRhs, double[] solution) {

        Arrays.fill(solution, 0.1);

        return easiestSDP(matrixSize, makeTriangular(constraints, matrixSize),
                          constraintRhs, solution, emptyGoal(matrixSize));
    }

    private static double[] emptyGoal(int matrixSize) {
        final double[] res = new double [matrixSize*matrixSize];
        Arrays.fill(res, 0.0);
        return res;
    }

    public static int minimalSdp(int matrixSize,
                                 double[] constraints, double[] constraintRhs,
                                 double[] solution) {
        assert (solution.length == matrixSize * matrixSize);
        
        final double[] goal = emptyGoal(matrixSize);
        final double[] inpConstraints = makeTriangular(constraints, matrixSize);

        final int res = easiestSDP(matrixSize, inpConstraints, constraintRhs,
                                   solution, goal);

        if (res == 0) {
            // SUCCESS: try to find a nice solution that contains as small
            // entries as possible
            for (int i = 0; i < matrixSize; ++i)
                for (int j = i; j < matrixSize; ++j) {
                    final int pos = i*matrixSize + j;
                    if (isAlmostNothing(solution[pos])) {
                        // nothing
                        continue;
                    } else if (solution[pos] > 0) {
                        goal[pos] = -1.0;
                    } else {
                        goal[pos] = 1.0;
                    }
                    // check that we still get the same result
                    if (res != easiestSDP(matrixSize, inpConstraints, constraintRhs,
                                          solution, goal)) {
                        // go back to the previous solution
                        goal[pos] = 0.0;
                    }
                }
            
            // ensure that we get the right solution
            final int res2 = easiestSDP(matrixSize, inpConstraints, constraintRhs,
                                        solution, goal);
            assert (res == res2);
        }

        return res;
    }

    // make sure that the constraints are given as upper triangular
    // matrices
    private static double[] makeTriangular(double[] constraints, int n) {
        int k = constraints.length / (n * n);
        final double[] inpConstraints = constraints.clone();
        for (int i = 0; i < n; ++i)
            for (int j = 0; j < i; ++j)
                for (int l = 0; l < k; ++l)
                    inpConstraints[l * n * n + i * n + j] = 0.0;
        return inpConstraints;
    }

    public static int robustSdp(int n, int k, double[] a, double[] constraints,
            double[] solution) {
        int res = sdp(n, constraints, a, solution);

        if (res != 0 && res != 3)
            return res;

        // otherwise, we found a solution and try to make it more robust

        // traces of the constraints
        final double[] traces = new double[k];
        Arrays.fill(traces, 0.0);
        for (int l = 0; l < k; ++l)
            for (int i = 0; i < n; ++i)
                traces[l] = traces[l] + constraints[l * n * n + i * (n + 1)];

        double lambda = 1.0;
        final double[] inpA = new double[k];

        while (lambda > 0.000000001) {
            System.out.println("Robustification: Trying lambda = " + lambda);

            for (int l = 0; l < k; ++l)
                inpA[l] = a[l] - lambda * traces[l];
            final int res2 = sdp(n, constraints, inpA, solution);

            if (res2 != 0 && res2 != 3) {
                lambda = lambda / 2.0;
                continue;
            }

            // otherwise we have found a robust solution, but need to
            // correct it
            System.out.println("Robustification succeeded with lambda = "
                    + lambda);

            for (int i = 0; i < n; ++i)
                solution[i * (n + 1)] = solution[i * (n + 1)] + lambda;

            return res2;
        }

        // no robust solution could be found
        System.out.println("Robustification failed");
        return sdp(n, constraints, a, solution);
    }

    private static final Random random = new Random();

    private static void fillRandomly(double[] ar) {
        for (int i = 0; i < ar.length; ++i)
            ar[i] = random.nextDouble() - 0.5;
    }

    public static void main(String[] args) {
        System.loadLibrary("lapack");
        System.loadLibrary("blas");
        System.loadLibrary("m");
        System.loadLibrary("csdp");
        // System.loadLibrary("sdp");

        test();

        int n = 7;

        double[] constraints = new double[] { 3, 1, 0, 0, 0, 0, 0, 1, 3, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0,

                0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 0, 1, 0, 0,
                0, 0, 0, 4, 0, 0, 0, 0, 0, 1, 0, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 1 };
        double[] C = new double[] { 2, 1, 0, 0, 0, 0, 0, 1, 2, 0, 0, 0, 0, 0,
                0, 0, 3, 0, 1, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 1, 0, 3, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
        double[] a = new double[] { 1, 2 };
        double[] X = new double[C.length], y = new double[a.length], Z = new double[C.length], pobj = new double[n], dobj = new double[a.length];

        easySDP(7, 2, convertToFortranForm(C, 7), a, constraints, 0, X, y, Z,
                pobj, dobj);
        System.out.println("X: " + Arrays.toString(X));// XXX
        System.out.println("y: " + Arrays.toString(y));// XXX
        System.out.println("Z: " + Arrays.toString(Z));// XXX
        System.out.println("pobj: " + Arrays.toString(pobj));// XXX
        System.out.println("dobj: " + Arrays.toString(dobj));// XXX
    }

    private static double[] convertToFortranForm(double[] array, int dim) {
        double[][] tmp = new double[dim][dim];
        double[] result = new double[array.length];
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                tmp[i][j] = array[j + i * dim];
                System.out.println("Setting pos (" + i + ", " + j + ")"
                        + " to value " + array[j + i * dim]);// XXX
            }
        }

        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                result[j + dim * i] = tmp[j][i];
            }
        }
        return result;
    }

}
