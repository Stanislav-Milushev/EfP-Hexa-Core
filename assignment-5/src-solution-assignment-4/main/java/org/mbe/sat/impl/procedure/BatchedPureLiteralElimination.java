package org.mbe.sat.impl.procedure;

import org.mbe.sat.api.procedure.IPureLiteralElimination;
import org.mbe.sat.api.procedure.ISimplifier;
import org.mbe.sat.core.model.Assignment;
import org.mbe.sat.core.model.formula.CnfFormula;
import org.mbe.sat.core.model.formula.Literal;
import org.mbe.sat.core.model.formula.Tristate;
import org.mbe.sat.core.model.formula.Variable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Performs simplifications on formulas and clauses by removing clauses that contain a literal which has the same
 * polarisation in all clauses it occurs in.
 */
public class BatchedPureLiteralElimination implements IPureLiteralElimination<CnfFormula, Assignment> {

    /**
     * Static logger instance for this class.
     */
    public static final Logger LOG = LoggerFactory.getLogger(BatchedPureLiteralElimination.class);

    /**
     * The simplifier that is used to eliminate the pure literals in the formula for a given set of pure literals.
     */
    private final ISimplifier<CnfFormula, Assignment> simplifier;

    public BatchedPureLiteralElimination(ISimplifier<CnfFormula, Assignment> simplifier) {
        this.simplifier = simplifier;
    }

    /**
     * Performs the simplification procedure "Pure Literal Elimination" (PLE) on the given formula. This method works
     * in-place and modifies the given formula. PLE removes clauses that contain a literal which has the same
     * polarisation in all clauses it occurs in. The literal's variable is then assigned a value according to the
     * literals polarisation, causing all clauses that contain the literal to become TRUE. This way PLE can greatly
     * reduce the number of clauses in formula.
     *
     * @param cnfFormula the formula that is simplified using PLE
     * @return the assignment that contains the values that where set to the variables during PLE
     */
    public Optional<Assignment> ple(CnfFormula cnfFormula) {
        LOG.trace("Simplifying formula '{}'", cnfFormula);

        // Find the first pure literal in the given CNF formula
        Set<Literal> pureLiterals = getPureLiterals(cnfFormula);

        // If the formula does not contain any pure literal, we can abort the procedure immediately
        if (pureLiterals.isEmpty()) {
            return Optional.empty();
        }

        // The assignment that is finally returned. All values for the pure literals are collected in this assignment.
        Assignment assignment = new Assignment();

        // Now we can remove all clauses that contain the pure variable
        CnfFormula simplifiedCnfFormula = new CnfFormula(cnfFormula);
        while (!pureLiterals.isEmpty()) {

            Assignment assignmentFromPureLiterals = getAssignmentFromPureLiterals(pureLiterals);

            simplifiedCnfFormula = simplifier.simplify(simplifiedCnfFormula, assignmentFromPureLiterals);
            assignment.mergeDistinct(assignmentFromPureLiterals);

            pureLiterals = getPureLiterals(simplifiedCnfFormula);

        }

        // Replace the given CNF formulas clauses with the simplified CNF formulas clauses "cnf = simplifiedCnfFormula"
        // would not work, because Java is using "pass by value" for parameters
        cnfFormula.setClauses(simplifiedCnfFormula.getClauses());

        return Optional.of(assignment);
    }

    private static Assignment getAssignmentFromPureLiterals(Set<Literal> pureLiterals) {
        return pureLiterals.stream()
                .map(Assignment::from)
                .reduce(Assignment.empty(), (assignment1, assignment2) -> Assignment.mergeDistinct(assignment1, assignment2));
    }

    /**
     * Returns the next pure literal from the given CNF formula. A literal is pure, if in all clauses it is contained
     * in, the literal has the same polarization. In the formula {{A, B, -C}, {A, -B, -D}, {C, D}} the only pure literal
     * is therefore A. All other literals have a different polarization across all clauses. There is no guarantee about
     * the order in which pure literals are returned.
     *
     * @param cnfFormula the formula that is searched for pure literals
     * @return an optional containing any pure literal from the formula, or an empty optional, if the formulas does not
     * contain any pure literal
     */
    public static Set<Literal> getPureLiterals(CnfFormula cnfFormula) {
        HashMap<Variable, Tristate> literals = new HashMap<>();

        // For each variable, check if another variable with same name bur different polarity exists
        cnfFormula.getLiterals().forEach(literal -> literals.compute(literal.getVariable(), (variable, tristate) -> {
            if (tristate == null) {
                return Tristate.of(literal.isPositive());
            } else if (tristate.isTrue() && literal.isNegative()) {
                return Tristate.UNDEFINED;
            } else if (tristate.isFalse() && literal.isPositive()) {
                return Tristate.UNDEFINED;
            } else {
                return tristate;
            }
        }));

        return literals.entrySet().stream()
                .filter(variableTristateEntry -> !variableTristateEntry.getValue().isUndefined())
                .map(BatchedPureLiteralElimination::getLiteralFromEntry)
                .collect(Collectors.toSet());
    }

    private static Literal getLiteralFromEntry(Map.Entry<Variable, Tristate> variableTristateEntry) {
        return new Literal(variableTristateEntry.getKey(), Literal.Polarity.of(variableTristateEntry.getValue().isTrue()));
    }
}
