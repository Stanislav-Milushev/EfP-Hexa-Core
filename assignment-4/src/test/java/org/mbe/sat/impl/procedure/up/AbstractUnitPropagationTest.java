package org.mbe.sat.impl.procedure.up;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mbe.sat.api.procedure.IUnitPropagation;
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

public abstract class AbstractUnitPropagationTest {

    /**
     * Static logger instance for this class.
     */
    private static final Logger LOG = LoggerFactory.getLogger(AbstractUnitPropagationTest.class);

    protected abstract IUnitPropagation<CnfFormula, Assignment> getUnitPropagation();

    /**
     * Tests for UP with different formulas.
     */
    @Nested
    @DisplayName("When performing UP")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class WhenPerformingUp {

        private IUnitPropagation<CnfFormula, Assignment> unitPropagation;

        Stream<Arguments> formulaAndResultsForUp() {

            return Stream.of(

                    // Formula where multiple UP steps are possible but the simplified formula is not empty
                    // {{A, -B, C}, {B}, {C, A}, {-C, B, A}, {C, D, A}, {-D}}
                    // Pick Unit clause: {B}
                    // Remove {B} from formula and continue with B = TRUE
                    // {{A, -TRUE, C}, {TRUE}, {C, A}, {-C, TRUE, A}, {C, D, A}, {-D}} >>Simplify>> {{A, C}, {C, D, A}, {-D}}
                    // {{A, C}, {C, D, A}, {-D}}
                    // Pick Unit clause: {-D}
                    // Remove {-D} from formula and continue with D = FALSE
                    // {{A, C}, {C, FALSE, A}, {-FALSE}} >>Simplify>> {{A, C}, {C, A}} = {{A, C}}
                    Arguments.of(
                            F.cnf(F.or(F.A, F.NB, F.C), F.or(F.B), F.or(F.A, F.C), F.or(F.A, F.B, F.NC), F.or(F.A, F.C, F.D), F.or(F.ND)),
                            F.cnf(F.or(F.A, F.C))
                    ),

                    // Formula with no unit clauses should not be simplified
                    // {{A, -B, C}, {A, C}, {A, B, -C}, {A, C, D}}
                    Arguments.of(
                            F.cnf(F.or(F.A, F.NB, F.C), F.or(F.C, F.A), F.or(F.NC, F.B, F.A), F.or(F.C, F.D, F.A)),
                            F.cnf(F.or(F.A, F.NB, F.C), F.or(F.C, F.A), F.or(F.NC, F.B, F.A), F.or(F.C, F.D, F.A))),


                    // Formula that is simplified to the empty formula
                    // {{A, -B}, {A, -B}, {A, B}, {-A, B}, {A}}
                    // Pick Unit clause: {A}
                    // Remove {A} from formula and continue with A = TRUE
                    // {{TRUE, -B}, {TRUE, -B}, {TRUE, B}, {-TRUE, B}, {TRUE}} >>Simplify>> {{B}}
                    // Pick Unit clause: {B}
                    // Remove {B} from formula and continue with B = TRUE
                    // {{TRUE}} >>Simplify>> {}
                    Arguments.of(
                            F.cnf(F.or(F.A, F.NB), F.or(F.A, F.NB), F.or(F.A, F.B), F.or(F.NA, F.B), F.or(F.A)),
                            F.emptyCnf()),

                    // {{-A, -B, C}, {-C, D}, {A}}
                    // Pick Unit clause: {A}
                    // Remove {A} from formula and continue with A = TRUE
                    // {{-TRUE, -B, C}, {-C, D}, {TRUE}}  >>Simplify>> {{-B, C}, {-C, D}}
                    Arguments.of(
                            F.cnf(F.or(F.NA, F.NB, F.C), F.or(F.NC, F.D), F.or(F.A)),
                            F.cnf(F.or(F.NB, F.C), F.or(F.NC, F.D))),

                    // Empty CNF
                    Arguments.of(
                            F.cnf(),
                            F.cnf()
                    ),

                    // Two conflicting clauses {A} and {-A} should result in an CNF that contains only an empty clause
                    // and is therefore UNSAT
                    Arguments.of(
                            F.cnf(F.or(F.A), F.or(F.NA)),
                            F.cnf(F.or())
                    )
            );
        }

        @BeforeEach
        public void setup() {
            unitPropagation = getUnitPropagation();
        }

        @MethodSource("formulaAndResultsForUp")
        @DisplayName("simplified formula should not be null")
        @ParameterizedTest()
        public void simplifiedFormulaShouldNotBeNull(CnfFormula cnf) {
            LOG.info("Simplifying '{}' using UP", cnf);
            unitPropagation.up(cnf);
            LOG.info("Formula simplified to '{}'", cnf);

            assertThat(cnf, is(notNullValue()));
        }

        @MethodSource("formulaAndResultsForUp")
        @DisplayName("simplified formula should match expected formula")
        @ParameterizedTest()
        public void simplifiedFormulaShouldMatchExpectedFormula(CnfFormula cnf, CnfFormula simplifiedCnf) {
            LOG.info("Simplifying '{}' using UP", cnf);
            unitPropagation.up(cnf);
            LOG.info("Formula simplified to '{}'", cnf);

            assertThat(cnf, is(equalTo(simplifiedCnf)));
        }

        @MethodSource("formulaAndResultsForUp")
        @DisplayName("simplified formula should have been modified if assignment is returned")
        @ParameterizedTest()
        public void whenAssignmentIsReturnedTheFormulaShouldHaveBeenModified(CnfFormula cnf) {
            CnfFormula givenFormula = new CnfFormula(cnf);

            LOG.info("Simplifying '{}' using UP", cnf);
            Optional<Assignment> assignment = unitPropagation.up(cnf);
            LOG.info("Formula simplified to '{}'", cnf);

            if (assignment.isPresent()) {
                assertThat(cnf, is(not(equalTo(givenFormula))));
            }
        }

        @MethodSource("formulaAndResultsForUp")
        @DisplayName("simplified formula should not have been modified if no assignment is returned")
        @ParameterizedTest()
        public void whenNoAssignmentIsReturnedTheFormulaShouldNotHaveBeenModified(CnfFormula cnf) {
            CnfFormula givenFormula = new CnfFormula(cnf);

            LOG.info("Simplifying '{}' using UP", givenFormula);
            Optional<Assignment> assignment = unitPropagation.up(cnf);
            LOG.info("Formula simplified to '{}'", cnf);

            if (!assignment.isPresent()) {
                assertThat(cnf, is(equalTo(givenFormula)));
            }
        }

        @MethodSource("formulaAndResultsForUp")
        @DisplayName("simplified formula should match formula created by simplifying given formula with calculated assignment")
        @ParameterizedTest()
        public void simplifiedFormulaShouldMatchFormulaSimplifiedWithReturnedAssignment(CnfFormula cnf) {
            CnfFormula givenFormula = new CnfFormula(cnf);

            LOG.info("Simplifying '{}' using UP", givenFormula);
            Optional<Assignment> assignment = unitPropagation.up(cnf);
            LOG.info("Formula simplified to '{}'", cnf);
            LOG.info("Calculated assignment '{}'", assignment);

            // When simplifying the original formula with the assignment returned by UP, the formula should match the
            // one that was created by UP
            if (assignment.isPresent()) {
                Formula simplifiedFormula = new SolutionSimplifier().simplify(givenFormula, assignment.get());
                LOG.info("Simplified original formula with calculated assignment to '{}'", simplifiedFormula);
                assertThat(cnf, is(equalTo(simplifiedFormula)));
            }
        }
    }
}
