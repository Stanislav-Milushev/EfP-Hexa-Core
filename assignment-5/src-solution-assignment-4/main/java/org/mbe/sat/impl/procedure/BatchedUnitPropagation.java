package org.mbe.sat.impl.procedure;

import com.google.common.collect.Iterables;
import org.mbe.sat.api.procedure.ISimplifier;
import org.mbe.sat.api.procedure.IUnitPropagation;
import org.mbe.sat.core.model.Assignment;
import org.mbe.sat.core.model.formula.Atom;
import org.mbe.sat.core.model.formula.CnfFormula;
import org.mbe.sat.core.model.formula.Literal;
import org.mbe.sat.core.model.formula.Or;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Performs simplifications on formulas and clauses by propagating assignments for literals in clauses that only contain
 * one literal.
 */
public class BatchedUnitPropagation implements IUnitPropagation<CnfFormula, Assignment> {

    /**
     * Static logger instance for this class.
     */
    public static final Logger LOG = LoggerFactory.getLogger(BatchedUnitPropagation.class);

    private final ISimplifier<CnfFormula, Assignment> simplifier;

    public BatchedUnitPropagation(ISimplifier<CnfFormula, Assignment> simplifier) {
        this.simplifier = simplifier;
    }

    /**
     * Performs the simplification procedure "Unit Propagation" (UP) on the given formula. This method works in-place
     * and modifies the given formula. UP searches for clauses that only contain one literal. In order to satisfy this
     * clause, the literal must evaluate to TRUE. So the variable is assigned a value to satisfy its clause and the
     * value is set (propagated) for all literals with the same variable in the whole formula.
     *
     * @param cnfFormula the formula that is simplified using UP
     * @return the assignment that contains the values that where set to the variables during UP
     */
    @Override
    public Optional<Assignment> up(CnfFormula cnfFormula) {
        LOG.trace("Simplifying formulas using unit propagation {}", cnfFormula);

        // Find all unit clauses in the given CNF formula
        //TODO: Returning an optional of an empty set is redundant. We can simply check if the set is empty
        Set<Or<Atom>> unitClauses = getUnitClauses(cnfFormula);

        // If the CNF does not contain any unit clause, we can abort the procedure
        if (unitClauses.isEmpty()) {
            return Optional.empty();
        }

        // The assignment that is finally returned. All values for the unit clauses are collected in this assignment.
        Assignment assignment = new Assignment();

        CnfFormula simplifiedCnfFormula = new CnfFormula(cnfFormula);
        while (!unitClauses.isEmpty()) {

            // Create an assignment for all of the given unit clauses
            Assignment assignmentForUnitClauses = getAssignmentForUnitClauses(unitClauses);

            simplifiedCnfFormula = simplifier.simplify(simplifiedCnfFormula, assignmentForUnitClauses);
            assignment.mergeDistinct(assignmentForUnitClauses);

            unitClauses = getUnitClauses(simplifiedCnfFormula);

            LOG.trace("Formula simplified to '{}'", cnfFormula);
            LOG.trace("Next unit clauses are {}", unitClauses);
        }

        // Replace the given CNF formulas clauses with the simplified CNF formulas clauses "cnf = simplifiedCnfFormula"
        // would not work, because Java is using "pass by value" for parameters
        cnfFormula.setClauses(simplifiedCnfFormula.getClauses());

        return Optional.of(assignment);
    }

    private static Assignment getAssignmentForUnitClauses(Set<Or<Atom>> unitClauses) {
        Assignment unitClauseAssignment = Assignment.empty();
        unitClauses.forEach(unitClause -> {

            // Unit clauses must only contain one literal
            if (unitClause.getOperands().size() != 1) {
                throw new IllegalArgumentException("A unit clause must contain exactly one operand");
            }

            // Get the only literal of the unit clause and create assignment that assign the variable a value so the
            // unit clause evaluates to TRUE
            Atom unitClauseLiteralOrAtom = Iterables.getOnlyElement(unitClause.getOperands());
            if (!(unitClauseLiteralOrAtom instanceof Literal)) {
                throw new IllegalArgumentException("A unit clause must contain a literal");
            }
            Literal unitClauseLiteral = (Literal) unitClauseLiteralOrAtom;

            // Create an assignment that makes the unit clause evaluate to true
            Assignment assignmentFromUnitClauseLiteral = Assignment.from(unitClauseLiteral);
            if (Assignment.canBeMerged(unitClauseAssignment, assignmentFromUnitClauseLiteral)) {
                unitClauseAssignment.merge(assignmentFromUnitClauseLiteral);
            } else {
                // We have a formula with conflicting unit clauses like {-A} AND {A}. In this case we will simply skip
                // the unit clause merging and continue. When evaluating and simplifying the formula with this
                // assignment, simplify will recognize the conflict, because the unit clause {A} e.g. will make the
                // clause {-A} be simplified to the empty clause, thus making the formula unsatisfied.
            }
        });

        return unitClauseAssignment;
    }

    private static Set<Or<Atom>> getUnitClauses(CnfFormula cnfFormula) {
        return cnfFormula.getClauses().stream()
                // Get the clauses that only contain one literal
                .filter(clause -> clause.getLiterals().size() == 1)
                // Pick any of the filtered clauses
                .collect(Collectors.toSet());
    }
}
