package org.mbe.sat.impl.solver;


import org.mbe.sat.api.solver.ISolver;
import org.mbe.sat.core.model.Assignment;
import org.mbe.sat.core.model.formula.CnfFormula;
import org.mbe.sat.core.model.formula.Variable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

/**
 * Abstract recursive SAT solver that builds up a decision tree and eliminates tree branches if its partial assignment
 * can never lead to satisfiable assignment. Therefore it greatly reduces the number of comparisons and evaluations that
 * have to be made. This class is an implementation of the Davis–Putnam algorithm. It leaves the simplification step
 * open to be implemented by the subclass, so that different simplification techniques such as PLE or UP can be tested
 * on this basic algorithm. When extending this class, the abstract method {@link #simplifyFormula(CnfFormula,
 * Assignment)} must be overwritten, which is the first method to be called in each recursive step.
 */
public abstract class AbstractDpSolver implements ISolver<CnfFormula, Optional<Assignment>> {

    /**
     * Static logger instance for this class.
     */
    private static final Logger LOG = LoggerFactory.getLogger(AbstractDpSolver.class);

    /**
     * Holds the the satisfying assignment, if any was found.
     */
    protected Assignment satisfyingAssignment;

    /**
     * Solves the given CNF formula by finding a satisfying assignment using a recursive decision tree. Partial
     * assignments that can no longer lead to a satisfying assignment, will be skipped.
     *
     * @param cnfFormula the formula that is solved
     * @return an optional containing the satisfying assignment, or an empty optional, if the formulas is not
     * satisfiable
     */
    @Override
    public Optional<Assignment> solve(CnfFormula cnfFormula) {

        // Check trivial cases to check if formula is satisfiable
        if (cnfFormula.isEmpty()) {
            // If the formula is empty, it is satisfiable
            return Optional.of(Assignment.empty());
        } else if (cnfFormula.containsEmptyClause()) {
            // If the formula contains an empty clause, it is NOT satisfiable
            return Optional.empty();
        }

        // Pick a variable that is used to recursively test both value assignments TRUE and FALSE
        Variable pickedVariable = pick(cnfFormula);
        LOG.trace("Picked variable '{}'", pickedVariable);

        // Test if the formula is solvable using TRUE and FALSE for the picked variable
        boolean satisfiable = solveInternal(cnfFormula, Assignment.empty(), Assignment.from(pickedVariable, true)) ||
                solveInternal(cnfFormula, Assignment.empty(), Assignment.from(pickedVariable, false));

        if (satisfiable) {
            // If the formula is satisfiable, we can return the satisfying assignment that was found. Since the DP
            // algorithm finds assignments that satisfy the formula but do not cover all variables, we need to fill all
            // missing variables in the assignments with arbitrary value
            satisfyingAssignment.setValueForUncoveredVariables(cnfFormula.getVariables(), true);
            return Optional.of(satisfyingAssignment);
        } else {
            return Optional.empty();
        }
    }

    /**
     * Given a CNF formula and an assignment, this method evaluates and simplifies the formula, thus reducing the
     * formulas size. The given assignment contains the variable that was picked in the previous iteration and has NOT
     * yet been 'put' into the formula.
     * <p>IMPORTANT: You must not modify the given formula and return a simplified
     * copy instead. Consider using {@link CnfFormula#CnfFormula(CnfFormula)} to create a copy of the formula.
     * </p>
     * All new variables assignments that are created during the simplification, must be added to the given assignment.
     * This can happen when using e.g. PLE or UP.
     *
     * @param formula    the formulas that is simplified
     * @param newAssignment the assignment that was created during the last iteration
     * @return the simplified formula
     *
     * @see CnfFormula#CnfFormula(CnfFormula)
     */
    protected abstract CnfFormula simplifyFormula(CnfFormula formula, Assignment newAssignment);

    /**
     * Internal solving method that solves the given CNF formula by finding a satisfying assignment using a recursive
     * decision tree. If the CNF is simplified so it only contains empty clauses, this method return <code>true</code>.
     * If the formula is simplified so it does not contain any clauses at all, this method returns <code>false</code>.
     * If none obe the above cases applies, a new variable is picked. The method calls itself recursively with a new
     * partial assignment using the newly picked variable and the current formula.
     *
     * @param cnfFormula the CNF formula that is solved
     * @param assignment the assignment that contains the picked variable
     * @return <code>true</code>, if the CNF is simplified so it only contains empty clauses, <code>false</code> if the
     * formula is simplified so it does not contain any clauses at all, or the result of the two recursive calls of this
     * method with a new variable pick.
     */
    protected boolean solveInternal(CnfFormula cnfFormula, Assignment assignment, Assignment newAssignment) {

        // Simplify with the given assignment. The assignment does only contain the value for the picked variable
        LOG.trace("Recursive solving step processing formula '{}'", cnfFormula);
        LOG.trace("Recursive solving step with current assignment '{}'", assignment);
        LOG.trace("Recursive solving step with variable pick '{}'", assignment);

        CnfFormula simplifiedCnfFormula = simplifyFormula(cnfFormula, newAssignment);

        if (simplifiedCnfFormula.getClauses().isEmpty()) {
            // All clauses that evaluate to true are removed. So if all clauses where removed, the formula evaluates to
            // true
            this.satisfyingAssignment = Assignment.mergeDistinct(assignment, newAssignment);
            return true;
        } else if (simplifiedCnfFormula.containsEmptyClause()) {
            // If the formula contains any empty clause, this means that this clause evaluates to false, making the
            // whole formula evaluate to false
            return false;
        }

        // By simplifying the formula, the new assignment was modified and is now merged with the current assignment, to
        // create the current assignment for the next recursive step.
        Assignment fullAssignment = Assignment.merge(assignment, newAssignment);

        // If we cannot say if the formula is satisfied or not, we need to continue searching with a new variable pick.
        // The old value of the previously picked variable remains in the new assignment, since we can the return the
        // satisfying assignment
        Variable pickedVariable = pick(simplifiedCnfFormula);
        LOG.trace("Picked variable '{}'", pickedVariable);

        // Test if the formula is solvable using TRUE and FALSE for the picked variable
        return solveInternal(simplifiedCnfFormula, fullAssignment, Assignment.from(pickedVariable, true)) ||
                solveInternal(simplifiedCnfFormula, fullAssignment, Assignment.from(pickedVariable, false));
    }

    /**
     * Picks a variable from the given CNF formula. This variable is used to perform the recursive step.
     *
     * @param cnfFormula the CNF formula whose variables are used to pick one
     * @return a picked variable from the formula
     */
    protected Variable pick(CnfFormula cnfFormula) {
        return cnfFormula.getVariables().iterator().next();
    }
}
