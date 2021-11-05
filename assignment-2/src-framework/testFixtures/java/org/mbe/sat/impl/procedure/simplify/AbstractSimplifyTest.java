package org.mbe.sat.impl.procedure.simplify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mbe.sat.api.procedure.ISimplifier;
import org.mbe.sat.core.model.Assignment;
import org.mbe.sat.core.model.formula.*;
import org.mbe.sat.core.problem.F;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public abstract class AbstractSimplifyTest {

    // Static variables used for testing
    private final Literal A = F.A;
    private final Literal NA = F.NA;
    private final Literal NB = F.NB;
    private final Literal B = F.B;
    private final Literal C = F.C;
    private final Literal NC = F.NC;
    private final Literal D = F.D;
    private final Literal ND = F.ND;
    private final Variable VA = A.getVariable();
    private final Variable VB = B.getVariable();
    private final Variable VC = C.getVariable();
    private final Variable VD = D.getVariable();

    private ISimplifier<CnfFormula, Assignment> simplifier;

    public abstract ISimplifier<CnfFormula, Assignment> getSimplifier();

    /**
     * Tests for the formula [{A, -B} {B, C} {B, -D}, {-A, -C, D}].
     */
    @Nested
    @DisplayName("When simplifying formula [{A, -B} {B, C} {B, -D}, {-A, -C, D}]")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class WhenSimplifyingFormula {

        private CnfFormula cnfFormula;

        /**
         * Creates a new instance of the class that is tested to avoid possible side effect causes by object states.
         */
        @BeforeEach
        public void setup() {
            Or<Atom> clauseAOrNotB = F.or(A, NB);
            Or<Atom> clauseBOrC = F.or(B, C);
            Or<Atom> clauseBOrNotD = F.or(B, ND);
            Or<Atom> clauseNotAOrNotCOrD = F.or(NA, NC, D);
            cnfFormula = new CnfFormula(new HashSet<>(Arrays.asList(clauseAOrNotB, clauseBOrC, clauseBOrNotD, clauseNotAOrNotCOrD)));
            simplifier = getSimplifier();
        }

        // Provides the parameters for the tests
        Stream<Arguments> assignmentsAndVariables() {
            List<Variable> variables = Arrays.asList(VA, VB, VC, VD);
            return Stream.of(

                    // Assignments that cover the formula [{A, -B} {B, C} {B, -D}, {-A, -C, D}]
                    Arguments.of(
                            Assignment.from(variables, new boolean[]{true, true, true, true}),
                            F.cnf()),
                    Arguments.of(
                            Assignment.from(variables, new boolean[]{true, true, true, false}),
                            F.cnf(F.or())),
                    Arguments.of(
                            Assignment.from(variables, new boolean[]{true, true, false, true}),
                            F.cnf()),
                    Arguments.of(
                            Assignment.from(variables, new boolean[]{true, true, false, false}),
                            F.cnf()),
                    Arguments.of(
                            Assignment.from(variables, new boolean[]{true, false, true, true}),
                            F.cnf(F.or())),
                    Arguments.of(
                            Assignment.from(variables, new boolean[]{true, false, true, false}),
                            F.cnf(F.or())),
                    Arguments.of(
                            Assignment.from(variables, new boolean[]{true, false, false, true}),
                            F.cnf(F.or())),
                    Arguments.of(
                            Assignment.from(variables, new boolean[]{true, false, false, false}),
                            F.cnf(F.or())),
                    Arguments.of(
                            Assignment.from(variables, new boolean[]{false, true, true, true}),
                            F.cnf(F.or())),
                    Arguments.of(
                            Assignment.from(variables, new boolean[]{false, true, true, false}),
                            F.cnf(F.or())),
                    Arguments.of(
                            Assignment.from(variables, new boolean[]{false, true, false, true}),
                            F.cnf(F.or())),
                    Arguments.of(
                            Assignment.from(variables, new boolean[]{false, true, false, false}),
                            F.cnf(F.or())),
                    Arguments.of(
                            Assignment.from(variables, new boolean[]{false, false, true, true}),
                            F.cnf(F.or())),
                    Arguments.of(
                            Assignment.from(variables, new boolean[]{false, false, true, false}),
                            F.cnf()),
                    Arguments.of(
                            Assignment.from(variables, new boolean[]{false, false, false, true}),
                            F.cnf(F.or())),
                    Arguments.of(
                            Assignment.from(variables, new boolean[]{false, false, false, false}),
                            F.cnf(F.or())),

                    // Assignments that do not cover the formula [{A, -B} {B, C} {B, -D}, {-A, -C, D}]
                    Arguments.of(
                            Assignment.from(VA, true),
                            // [{B, C} {B, -D}, {-C, D}]
                            F.cnf(F.or(F.B, F.C), F.or(F.B, F.ND), F.or(F.NC, F.D))),
                    Arguments.of(
                            Assignment.from(VB, true),
                            // [{A} {-A, -C, D}]
                            F.cnf(F.or(F.A), F.or(F.NA, F.NC, F.D))),
                    Arguments.of(
                            Assignment.from(VC, true),
                            // [{A, -B}, {B, -D}, {-A, D}]
                            F.cnf(F.or(F.A, F.NB), F.or(F.B, F.ND), F.or(F.NA, F.D))),
                    Arguments.of(
                            Assignment.from(VD, true),
                            // [{A, -B}, {B, C} {B}]
                            F.cnf(F.or(F.A, F.NB), F.or(F.B, F.C), F.or(F.B)))
            );
        }

        @ParameterizedTest
        @MethodSource("assignmentsAndVariables")
        @DisplayName("simplify should not modify the formula")
        public void simplifyShouldNotModifyTheFormula(Assignment assignment) {

            // Using the hash code to check if the formula was modified. This is far from perfect but the best we can do
            // without writing out own comparator or overwriting the hashCode() method
            int hashCodeBeforeSimplifying = cnfFormula.hashCode();
            simplifier.simplify(cnfFormula, assignment);
            int hashCodeAfterSimplifying = cnfFormula.hashCode();

            assertThat(hashCodeAfterSimplifying, is(equalTo(hashCodeBeforeSimplifying)));
        }

        @ParameterizedTest
        @MethodSource("assignmentsAndVariables")
        @DisplayName("formula should be equal to expected formula")
        public void formulaShouldBeEqualToExpectedFormula(Assignment assignment, CnfFormula expectedFormula) {
            CnfFormula simplifiedFormula = simplifier.simplify(cnfFormula, assignment);
            assertThat(simplifiedFormula, is(equalTo(expectedFormula)));
        }

        @ParameterizedTest
        @MethodSource("assignmentsAndVariables")
        @DisplayName("formula should contain same number of variables")
        public void formulaShouldContainExpectedNumberOfVariables(Assignment assignment, CnfFormula expectedFormula) {
            CnfFormula simplifiedFormula = simplifier.simplify(cnfFormula, assignment);

            assertThat(simplifiedFormula.getVariables(), hasSize(expectedFormula.getVariables().size()));
        }

        @ParameterizedTest
        @MethodSource("assignmentsAndVariables")
        @DisplayName("formula should contain expected variables")
        public void formulaShouldContainExpectedVariables(Assignment assignment, CnfFormula expectedFormula) {
            CnfFormula simplifiedFormula = simplifier.simplify(cnfFormula, assignment);

            assertThat(simplifiedFormula.getVariables(), containsInAnyOrder(expectedFormula.getVariables().toArray()));
        }

        @ParameterizedTest
        @MethodSource("assignmentsAndVariables")
        @DisplayName("formula should contain same number of literals")
        public void formulaShouldContainExpectedNumberOfLiterals(Assignment assignment, CnfFormula expectedFormula) {
            CnfFormula simplifiedFormula = simplifier.simplify(cnfFormula, assignment);

            assertThat(simplifiedFormula.getLiterals(), hasSize(expectedFormula.getLiterals().size()));
        }

        @ParameterizedTest
        @MethodSource("assignmentsAndVariables")
        @DisplayName("formula should contain expected literals")
        public void formulaShouldContainExpectedLiterals(Assignment assignment, CnfFormula expectedFormula) {
            CnfFormula simplifiedFormula = simplifier.simplify(cnfFormula, assignment);

            assertThat(simplifiedFormula.getLiterals(), containsInAnyOrder(expectedFormula.getLiterals().toArray()));
        }
    }
}
