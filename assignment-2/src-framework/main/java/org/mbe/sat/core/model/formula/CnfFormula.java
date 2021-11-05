package org.mbe.sat.core.model.formula;

import org.mbe.sat.core.model.Assignment;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * A specification of a boolean conjunction that represents a conjunctive normal form (CNF). A CNF is a {@link And
 * conjunction} of {@link Or disjunctions} of {@link Atom atoms}. An atom can either be a {@link Literal literal} or a
 * {@link Constant constant}.
 */
public class CnfFormula extends And<Or<Atom>> {

    public CnfFormula() {
        super(new HashSet<>());
    }

    /**
     * Creates a new instance of a {@link CnfFormula CNF formula} with the given list of clauses (conjunctions).
     *
     * @param operands the list of conjunctions that the created CNF will consist of
     */
    public CnfFormula(Set<Or<Atom>> operands) {
        super(new HashSet<>(operands));
    }

    /**
     * Creates a new instance of a {@link CnfFormula CNF formula} with the given collection of clauses (conjunctions).
     *
     * @param operands the collection of conjunctions that the created CNF will consist of
     */
    public CnfFormula(Collection<Or<Atom>> operands) {
        super(new HashSet<>(operands));
    }

    /**
     * Copy constructor that creates a new instance of a {@link CnfFormula CNF formula} using the collection of clauses
     * (conjunctions) of the given CNF.
     *
     * @param cnfFormula the CNF formula whose collection of conjunctions is used to create a new CNF
     */
    public CnfFormula(CnfFormula cnfFormula) {
        this(cnfFormula.getClauses());
    }

    /**
     * Returns all clauses that this CNF consists of, or an empty collection if the CNF does not contain any clause. To
     * check if this CNF does not contain any clause, you can use {@link #containsEmptyClause}
     *
     * @return all clauses that this CNF consists of, or an empty collection if the CNF does not contain any clause
     *
     * @see #containsEmptyClause()
     */
    public Set<Or<Atom>> getClauses() {
        return super.getOperands();
    }

    public void setClauses(Set<Or<Atom>> clauses) {
        super.setOperands(clauses);
    }

    /**
     * Return <code>true</code>, if the formula does not contain any clauses, <code>false</code> otherwise.
     *
     * @return <code>true</code>, if the formula does not contain any clauses, <code>false</code> otherwise.
     */
    public boolean isEmpty() {
        return getClauses().isEmpty();
    }

    /**
     * Returns <code>true</code>, if this CNF does not contain any clause or <code>false</code> in case it contains one
     * or more clauses.
     *
     * @return <code>true</code>, if this CNF does not contain any clause or <code>false</code> otherwise
     */
    public boolean containsEmptyClause() {
        return getClauses().stream().anyMatch(clause -> clause.getOperands().isEmpty());
    }

    /**
     * Evaluate the CNF formula with the given assignment and creates a new formula that contains the result of the
     * evaluation. The original formula is not modified.
     *
     * @param assignment the assignment that is used to evaluate the formula
     * @return the formula that was created by evaluating this formula with the given assignment
     */
    @SuppressWarnings("unchecked")
    public CnfFormula evaluate(Assignment assignment) {

        // The desired behavior would be to have the "evaluate" method return a "CnfFormula" here, but that is not
        // possible when using inheritance.
        // Since "CnfFormula" extends "And" we cannot override the "evaluate" method with another return type. Moving
        // the evaluate method to an interface would also not work since the "And" class and "CnfFormula" class would
        // both need to implement the same interface only with different return types, which is not allowed due to type
        // erasure.
        // Only workaround would be to use composition instead of inheritance for the complete formula class structure
        // to have a special return type for each class. This would result in a huge refactoring which does not feasible
        // at the moment.
        Formula evaluatedAnd = super.evaluate(assignment);

        if (evaluatedAnd instanceof And) {
            And<Or<Atom>> restrict = (And<Or<Atom>>) evaluatedAnd;

            return new CnfFormula(restrict.getOperands());
        } else {
            throw new RuntimeException();
        }
    }
}
