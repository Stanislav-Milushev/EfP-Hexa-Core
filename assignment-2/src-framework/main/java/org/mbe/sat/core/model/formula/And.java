package org.mbe.sat.core.model.formula;

import org.mbe.sat.core.model.Assignment;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Formula that represents a boolean conjunction, where {@link T} specifies the type of sub-formulas that the
 * conjunction consists of.
 *
 * @param <T> the type of formulas the conjunction consists of
 */
public class And<T extends Formula> extends NAryOperator<T> {

    /**
     * Creates a new instance of a {@link And conjunction} with an empty set of operands.
     */
    public And() {
        super(Collections.emptySet());
    }

    /**
     * Creates a new instance of a {@link And conjunction} with the given collection of operands.
     *
     * @param operands the list of operands the created {@link And conjunction} will consist of
     */
    public And(Set<T> operands) {
        super(operands);
    }

    /**
     * Creates a new instance of a {@link And conjunction} with the given two operands of the conjunction.
     *
     * @param firstOperand  the first operand the created {@link And conjunction} will consist of
     * @param secondOperand the second operand the created {@link And conjunction} will consist of
     */
    public And(T firstOperand, T secondOperand) {
        super(Stream.of(firstOperand, secondOperand).collect(Collectors.toSet()));
    }

    /**
     * Creates a new instance of {@link And} with an empty set of operands.
     */
    public static <P extends Formula> And<P> empty() {
        return new And<>();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Tristate isSatisfiedWith(Assignment assignment) {
        if (assignment.covers(this)) {

            // The evaluation result of a conjunction is only true, if the evaluation of all operands is true
            return Tristate.of(operands.stream().allMatch(t -> t.isSatisfiedWith(assignment).isTrue()));
        } else {
            // If not all operands are covered by the assignment, we must return UNDEFINED
            return Tristate.UNDEFINED;
        }
    }

    @Override
    public Formula evaluate(Assignment assignment) {
        Set<Formula> evaluatedOperands = getOperands().stream()
                .map(operand -> operand.evaluate(assignment))
                .collect(Collectors.toSet());

        return new And<>(evaluatedOperands);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "[" + operands.stream().map(Object::toString).collect(Collectors.joining(" AND ")) + "]";
    }
}
