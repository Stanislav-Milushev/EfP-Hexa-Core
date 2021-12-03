package org.mbe.sat.impl.procedure.ple;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mbe.sat.api.procedure.IPureLiteralElimination;
import org.mbe.sat.core.model.Assignment;
import org.mbe.sat.core.model.formula.CnfFormula;
import org.mbe.sat.core.model.formula.Formula;
import org.mbe.sat.core.problem.F;
import org.mbe.sat.impl.procedure.SolutionSimplifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public abstract class AbstractPureLiteralEliminationTest {

    /**
     * Static logger instance for this class.
     */
    public static final Logger LOG = LoggerFactory.getLogger(AbstractPureLiteralEliminationTest.class);

    protected abstract IPureLiteralElimination<CnfFormula, Assignment> getPureLiteralElimination();

    /**
     * Tests for PLE with different formulas.
     */
    @Nested
    @DisplayName("When performing PLE")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class WhenPerformingPle {

        private IPureLiteralElimination<CnfFormula, Assignment> pureLiteralElimination;

        Stream<Arguments> formulaAndResultsForPle() {

            return Stream.of(
                    // {{A, B}}
                    Arguments.of(
                            F.cnf(F.or(F.A, F.B)),
                            F.emptyCnf()),
                    // {{A, -B, C}, {B}, {A, C}, {A, B, -C}, {A, C, D}, {-D}}
                    Arguments.of(
                            F.cnf(F.or(F.A, F.NB, F.C), F.or(F.B), F.or(F.C, F.A), F.or(F.NC, F.B, F.A), F.or(F.C, F.D, F.A), F.or(F.ND)),
                            F.emptyCnf()),
                    // {{A}, {A, B, C}, {-B, -C}, {B, C}}
                    Arguments.of(
                            F.cnf(F.or(F.A), F.or(F.A, F.B, F.C), F.or(F.NB, F.NC), F.or(F.B, F.C)),
                            F.cnf(F.or(F.NB, F.NC), F.or(F.B, F.C))),
                    // {{}}
                    Arguments.of(
                            F.emptyCnf(),
                            F.emptyCnf()),
                    // {{A, B, C}, {A, D, E}, {A, F, G}}
                    Arguments.of(
                            F.cnf(F.or(F.A, F.B, F.C), F.or(F.A, F.D, F.E), F.or(F.A, F.F, F.G)),
                            F.emptyCnf()),
                    // {{-A, B, C}, {-A, B, -C}, {-A, -B, C}}
                    Arguments.of(
                            F.cnf(F.or(F.NA, F.B, F.C), F.or(F.NA, F.B, F.NC), F.or(F.NA, F.NB, F.C)),
                            F.emptyCnf()),
                    // {{-A, B, C}, {-A, -B}, {C}, {-C}, {-B, -C, D}}
                    Arguments.of(
                            F.cnf(F.or(F.NA, F.B, F.C), F.or(F.NA, F.NB), F.or(F.C), F.or(F.NC), F.or(F.NB, F.NC, F.D)),
                            F.cnf(F.or(F.C), F.or(F.NC))),

                    // Formula where no PLE is possible
                    // {{-A, B, C}, {A, -B}, {-C}}
                    Arguments.of(
                            F.cnf(F.or(F.NA, F.B, F.C), F.or(F.A, F.NB), F.or(F.NC)),
                            F.cnf(F.or(F.NA, F.B, F.C), F.or(F.A, F.NB), F.or(F.NC)))
            );
        }

        @BeforeEach
        public void setup() {
            pureLiteralElimination = getPureLiteralElimination();
        }

        @MethodSource("formulaAndResultsForPle")
        @DisplayName("simplified formula should not be null")
        @ParameterizedTest()
        public void simplifiedFormulaShouldNotBeNull(CnfFormula cnf) {
            LOG.info("Simplifying '{}' using PLE", cnf);
            pureLiteralElimination.ple(cnf);
            assertThat(cnf, is(notNullValue()));
        }

        @MethodSource("formulaAndResultsForPle")
        @DisplayName("simplified formula should match expected formula")
        @ParameterizedTest()
        public void simplifiedFormulaShouldMatchExpectedFormula(CnfFormula cnf, CnfFormula simplifiedCnf) {
            LOG.info("Simplifying '{}' using PLE", cnf);
            pureLiteralElimination.ple(cnf);
            LOG.info("Formula simplified to '{}'", cnf);
            assertThat(cnf, is(equalTo(simplifiedCnf)));
        }

        @MethodSource("formulaAndResultsForPle")
        @DisplayName("simplified formula should have been modified if assignment is returned")
        @ParameterizedTest()
        public void whenAssignmentIsReturnedTheFormulaShouldHaveBeenModified(CnfFormula cnf) {
            CnfFormula givenFormula = new CnfFormula(cnf);

            LOG.info("Simplifying '{}' using PLE", cnf);
            Optional<Assignment> assignment = pureLiteralElimination.ple(cnf);
            if (assignment.isPresent()) {
                assertThat(cnf, is(not(equalTo(givenFormula))));
            }
        }

        @MethodSource("formulaAndResultsForPle")
        @DisplayName("simplified formula should not have been modified if no assignment is returned")
        @ParameterizedTest()
        public void whenNoAssignmentIsReturnedTheFormulaShouldNotHaveBeenModified(CnfFormula cnf) {
            CnfFormula givenFormula = new CnfFormula(cnf);

            LOG.info("Simplifying '{}' using PLE", cnf);
            Optional<Assignment> assignment = pureLiteralElimination.ple(cnf);
            if (!assignment.isPresent()) {
                assertThat(cnf, is(equalTo(givenFormula)));
            }
        }

        @MethodSource("formulaAndResultsForPle")
        @DisplayName("simplified formula should match formula created by simplifying given formula with calculated assignment")
        @ParameterizedTest()
        public void simplifiedFormulaShouldMatchFormulaSimplifiedWithReturnedAssignment(CnfFormula cnf) {
            CnfFormula givenFormula = new CnfFormula(cnf);

            LOG.info("Simplifying '{}' using PLE", givenFormula);
            Optional<Assignment> assignment = pureLiteralElimination.ple(cnf);
            LOG.info("Formula simplified to '{}'", cnf);
            LOG.info("Calculated assignment '{}'", assignment);

            // When simplifying the original formula with the assignment returned by PLE, the formula should match the
            // one that was created by PLE
            if (assignment.isPresent()) {
                Formula simplifiedFormula = new SolutionSimplifier().simplify(givenFormula, assignment.get());
                LOG.info("Simplified original formula with calculated assignment to '{}'", simplifiedFormula);
                assertThat(cnf, is(equalTo(simplifiedFormula)));
            }
        }
    }
}
