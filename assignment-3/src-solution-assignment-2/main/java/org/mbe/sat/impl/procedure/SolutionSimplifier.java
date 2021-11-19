package org.mbe.sat.impl.procedure;

import org.mbe.sat.api.procedure.ISimplifier;
import org.mbe.sat.core.model.Assignment;
import org.mbe.sat.core.model.formula.*;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Performs simplifications on formulas and clauses by removing literals from clauses that evaluate to {@link
 * Tristate#FALSE} and removing clauses that contain at least one literal.
 */
public class SolutionSimplifier implements ISimplifier<CnfFormula, Assignment> {

    /**
     * Checks whether the given clause contains no {@link True} constant. The given clause contains no true constants,
     * if none of its operands is an instance of {@link True}.
     *
     * @param clause the clause that is searched for a {@link True} constants
     * @return <code>true</code> if the given clause contains no {@link True} constants, <code>false</code> otherwise
     *
     * @see #containsTrueConstant(Or)
     */
    public boolean containsNoTrueConstant(Or<Atom> clause) {
        return !containsTrueConstant(clause);
    }

    /**
     * Checks whether the given clause contains a {@link True} constant. The given clause contains a true constants, if
     * any of its operands is an instance of {@link True}.
     *
     * @param clause the clause that is searched for a {@link True} constants
     * @return <code>true</code> if the given clause contains a {@link True} constants, <code>false</code> otherwise
     *
     * @see #containsNoTrueConstant(Or)
     */
    public boolean containsTrueConstant(Or<Atom> clause) {
        return clause.getOperands().stream().anyMatch(atom -> atom instanceof True);
    }

    /**
     * Removes all {@link False} constants from the given formula, by checking, if any of its operands is an instance of
     * {@link False}. The given clause is not modified, instead a copy of the given clause is returned from which all
     * {@link False} constants are removed. In other words, this method does NOT work in-place. If the given clause does
     * not contain any {@link False} constant, a copy of the given clause is returned.
     *
     * @param clause the clause from which all {@link False} constants are removed
     * @return a copy of the given clause from which all {@link False} constants are removed
     */
    public Or<Atom> removeFalseConstants(Or<Atom> clause) {
        Set<Atom> operandsWithoutFalseConstants = clause.getOperands().stream()
                .filter(atom -> !(atom instanceof False))
                .collect(Collectors.toSet());

        return new Or<>(operandsWithoutFalseConstants);
    }

    /**
     * Evaluates the given CNF formula with the given assignment then simplifies the formula by simplifying each clause
     * and only keeping clauses that do not contain a {@link True} constant. Futher all {@link False} constants are
     * removed from the formulas clauses. The given CNF formula is not modified, instead a simplified copy of the given
     * formula is returned. In other words, this method does NOT work in-place.
     *
     * @param cnfFormula the CNF formula that is simplified
     * @param assignment the assignment that is used to evaluate the formula
     * @return the evaluated and simplified formula
     */
    @Override
    public CnfFormula simplify(CnfFormula cnfFormula, Assignment assignment) {

        // Evaluate formula with the assignment. Afterwards the formula should contain True and False constants
        CnfFormula evaluatedCnfFormula = cnfFormula.evaluate(assignment);

        return simplifyCnf(evaluatedCnfFormula);
    }

    /**
     * Simplifies the given CNF formula by removing clauses that contains a {@link True} constant and removing {@link
     * False} constants from clauses. If the given CNF formula does not contain any {@link Constant constants}, this
     * method will not perform any simplifications and return a copy of the given formula.
     *
     * @param cnfFormula the CNF formula that is simplified
     * @return the simplified formula
     */
    public CnfFormula simplifyCnf(CnfFormula cnfFormula) {

        // Remove all clauses that contain a TRUE
        Set<Or<Atom>> clausesWithoutTrue = cnfFormula.getClauses().stream()
                // Remove FALSE constants from all remaining clauses
                .map(this::removeFalseConstants)
                // Remove clauses that contain a TRUE constant
                .filter(this::containsNoTrueConstant)
                .collect(Collectors.toSet());

        // If the clauses list is empty, the formula is satisfiable, false otherwise
        return new CnfFormula(clausesWithoutTrue);
    }
}
