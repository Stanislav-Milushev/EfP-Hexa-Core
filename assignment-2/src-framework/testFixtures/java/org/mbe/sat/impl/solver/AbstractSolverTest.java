package org.mbe.sat.impl.solver;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mbe.sat.api.solver.ISolver;
import org.mbe.sat.core.model.Assignment;
import org.mbe.sat.core.model.formula.CnfFormula;
import org.mbe.sat.core.model.formula.Tristate;
import org.mbe.sat.core.problem.F;
import org.mbe.sat.core.problem.SatProblemFixtures;
import org.mbe.sat.core.problem.SatProblemJsonModel;
import org.mbe.sat.core.runner.TimedCnfSolvableRunner;
import org.mbe.sat.core.runner.TimedCnfSolvableRunner.TimedResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class AbstractSolverTest {

    /**
     * Static logger instance for this class.
     */
    private static final Logger LOG = LoggerFactory.getLogger(AbstractSolverTest.class);

    /**
     * Solver that is tested.
     */
    private ISolver<CnfFormula, Optional<Assignment>> solver;

    /**
     * Runner that is used to run the solver and measure its execution time.
     */
    private TimedCnfSolvableRunner runner;

    /**
     * Returns complexity of the problem that the solver needs to solve. Based on the complexity the test will run the
     * solver with different test.
     *
     * @return complexity of the problem that the solver needs to solve
     */
    public abstract SatProblemJsonModel.Complexity getComplexity();

    /**
     * Returns a new instance of the SAT solver that is tested. This method is called for each test, so that a new
     * solver is used for each execution.
     *
     * @return a new instance of the solver that is tested
     */
    public abstract ISolver<CnfFormula, Optional<Assignment>> getSolver();

    public TimedCnfSolvableRunner getSolvableRunner() {
        return new TimedCnfSolvableRunner();
    }

    // Provides the parameters for the tests
    public Stream<SatProblemJsonModel> getSatProblems() {
        return SatProblemFixtures.getByComplexityCombined(getComplexity()).stream();
    }

    @BeforeEach
    final public void getSolverInstance() {
        solver = getSolver();
        runner = getSolvableRunner();
    }

    @Test
    public void emptyCnfFormulaShouldBeSatisfiable() {
        CnfFormula emptyCnfFormula = F.emptyCnf();

        LOG.info("Testing SAT for empty formula {}", emptyCnfFormula);
        Optional<Assignment> solve = solver.solve(emptyCnfFormula);
        boolean calculatedSatisfiability = solve.isPresent();

        LOG.info("Satisfiability expected >>{}<< and solver calculated >>{}<<", true, calculatedSatisfiability);

        assertThat(calculatedSatisfiability, is(true));
    }

    @Test
    public void cnfFormulaWithEmptyClauseShouldNotBeSatisfiable() {
        CnfFormula emptyCnfFormula = F.cnf(F.or());

        LOG.info("Testing SAT for empty formula {}", emptyCnfFormula);
        Optional<Assignment> solve = solver.solve(emptyCnfFormula);
        boolean calculatedSatisfiability = solve.isPresent();

        LOG.info("Satisfiability expected >>{}<< and solver calculated >>{}<<", false, calculatedSatisfiability);

        assertThat(calculatedSatisfiability, is(false));
    }

    @ParameterizedTest(name = "file {0}")
    @MethodSource("getSatProblems")
    @DisplayName("Satisfiability should match the satisfiability in the problem definition")
    public void cnfSatisfiabilityShouldMatchProblemDefinition(SatProblemJsonModel satProblem) {
        Tristate expectedSatisfiability = satProblem.isSatisfiable();

        // Run solver and measure time
        LOG.info("Using DIMACS file '{}'", satProblem.getFileName());
        LOG.info("Testing SAT for formula {}", satProblem.getFormula());
        TimedResult<Optional<Assignment>> booleanTimedResult = runner.runTimed(solver, satProblem.getFormula());
        boolean calculatedSatisfiable = booleanTimedResult.getResult().isPresent();

        LOG.info("Solving took {}", booleanTimedResult.getDurationAsString());
        LOG.info("Satisfiability expected >>{}<< and solver calculated >>{}<<", expectedSatisfiability.isTrue(), calculatedSatisfiable);

        assertThat(calculatedSatisfiable, is(expectedSatisfiability.isTrue()));
    }

    @ParameterizedTest(name = "file {0}")
    @MethodSource("getSatProblems")
    @DisplayName("Assignment should satisfy the formula")
    public void assignmentShouldSatisfyFormula(SatProblemJsonModel satProblem) {

        // Run solver and check if the formula is satisfiable
        LOG.info("Using DIMACS file '{}'", satProblem.getFileName());
        LOG.info("Testing SAT for formula {}", satProblem.getFormula());

        // If the formula is satisfiable, the returned assignment must satisfy the formula
        Optional<Assignment> result = solver.solve(satProblem.getFormula());
        if (result.isPresent()) {
            Assignment satisfyingAssignment = result.get();
            Tristate evaluationResult = satProblem.getFormula().isSatisfiedWith(satisfyingAssignment);

            assertThat(evaluationResult, is(Tristate.TRUE));
        } else {
            LOG.info("Formula is not satisfiable, so we cannot check if a returned assignment satisfies it");
        }
    }
}
