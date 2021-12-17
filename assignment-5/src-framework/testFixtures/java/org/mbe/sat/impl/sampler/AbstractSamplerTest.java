package org.mbe.sat.impl.sampler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mbe.sat.api.sampler.ISampler;
import org.mbe.sat.core.model.Assignment;
import org.mbe.sat.core.model.formula.CnfFormula;
import org.mbe.sat.core.model.formula.Tristate;
import org.mbe.sat.core.problem.SatProblemFixtures;
import org.mbe.sat.core.problem.SatProblemJsonModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public abstract class AbstractSamplerTest {

    /**
     * Static logger instance for this class.
     */
    private static final Logger LOG = LoggerFactory.getLogger(AbstractSamplerTest.class);

    /**
     * Sample that is tested.
     */
    private ISampler<CnfFormula, Assignment> sampler;

    /**
     * Helper method to get SAT problems.
     */
    private Collection<SatProblemJsonModel> getSatProblems() {
        return SatProblemFixtures.getByComplexityCombined(SatProblemJsonModel.Complexity.MEDIUM);
    }

    public abstract ISampler<CnfFormula, Assignment> getSampler();

    @BeforeEach
    public void setup() {
        // Create a new sampler for each test
        sampler = getSampler();
    }

    @Nested
    @DisplayName("When sampling satisfiable formula")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class WhenSamplingSatisfiableFormula {

        Stream<Arguments> getSatisfiableSatProblems() {
            return getSatProblems().stream()
                    .filter(satProblemJsonModel -> satProblemJsonModel.isSatisfiable().isTrue())
                    .filter(satProblemJsonModel -> satProblemJsonModel.getFormula().getVariables().size() >= 2)
                    .map(Arguments::of);
        }

        @ParameterizedTest
        @MethodSource("getSatisfiableSatProblems")
        @DisplayName("sample should not be empty")
        public void sampleShouldNotBeEmpty(SatProblemJsonModel model) {
            Set<Assignment> sample = sampler.sample(model.getFormula());
            LOG.info("Sampler generated sample with {} configuration", sample.size());
            sample.forEach(configuration -> LOG.info("Generated configuration: {}", configuration));

            assertThat(sample, is(not(empty())));
        }

        @ParameterizedTest
        @MethodSource("getSatisfiableSatProblems")
        @DisplayName("sample configurations should satisfy formula")
        public void samplesShouldSatisfyFormula(SatProblemJsonModel model) {
            Set<Assignment> sample = sampler.sample(model.getFormula());
            LOG.info("Sampler generated sample with {} configuration", sample.size());
            sample.forEach(configuration -> LOG.info("Generated Sample: {}", configuration));

            sample.forEach(assignment -> assertThat(model.getFormula().isSatisfiedWith(assignment), is(equalTo(Tristate.TRUE))));
        }
    }

    @Nested
    @DisplayName("When sampling unsatisfiable formula")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class WhenSamplingUnsatisfiableFormula {

        Stream<Arguments> getUnsatisfiableSatProblems() {
            return getSatProblems().stream()
                    .filter(satProblemJsonModel -> satProblemJsonModel.isSatisfiable().isFalse())
                    .filter(satProblemJsonModel -> satProblemJsonModel.getFormula().getVariables().size() >= 2)
                    .map(Arguments::of);
        }

        @ParameterizedTest
        @MethodSource("getUnsatisfiableSatProblems")
        @DisplayName("sample should be empty")
        public void samplesShouldBeEmpty(SatProblemJsonModel model) {
            Set<Assignment> sample = sampler.sample(model.getFormula());
            LOG.info("Sampler generated sample with {} configuration", sample.size());

            assertThat(sample, is(empty()));
        }
    }
}
