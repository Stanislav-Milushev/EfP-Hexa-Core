package org.mbe.sat.impl.sampler;

import com.google.common.annotations.Beta;
import com.google.common.collect.Sets;
import com.google.common.primitives.Booleans;
import org.mbe.sat.api.ICombinationProvider;
import org.mbe.sat.api.procedure.ISimplifier;
import org.mbe.sat.api.sampler.ISampler;
import org.mbe.sat.api.solver.ISolver;
import org.mbe.sat.core.SequentialCombinationProvider;
import org.mbe.sat.core.model.Assignment;
import org.mbe.sat.core.model.formula.*;
import org.mbe.sat.impl.procedure.SolutionSimplifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Basic implementation of a k-wise combinatorial sample algorithm. Its is basically an implementation of "Chvatals"
 * sampling algorithm.
 */
public abstract class BasicKWiseCombinatorialSampler implements ISampler<CnfFormula, Assignment> {

    /**
     * Static logger instance for this class.
     */
    private static final Logger LOG = LoggerFactory.getLogger(BasicKWiseCombinatorialSampler.class);
    /**
     * The number of feature interactions that are covered. This number determines how large each feature combination
     * is.
     */
    private final int k;
    /**
     * The solver that is used to find the valid value schemas.
     */
    private final ISolver<CnfFormula, Optional<Assignment>> solver;
    /**
     * The simplifier that is used to simplify intermediate results.
     */
    private final ISimplifier<CnfFormula, Assignment> simplifier;

    /**
     * Creates a new instance of {@link BasicKWiseCombinatorialSampler} with the given solver and size of feature
     * combinations.
     *
     * @param solver the solver that is used to generate the valid feature combinations and to generate complete
     *               configurations
     * @param k      the size of the sets that represent a feature selection
     */
    public BasicKWiseCombinatorialSampler(ISolver<CnfFormula, Optional<Assignment>> solver, int k) {
        this.solver = solver;
        this.k = k;
        this.simplifier = new SolutionSimplifier();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<Assignment> sample(CnfFormula cnfFormula) {
        LOG.debug("Generating sample for formula '{}'", cnfFormula);

        // If the formula is not satisfiable, we can immediately return an empty sample
        if (!solver.solve(cnfFormula).isPresent()) {
            LOG.warn("Formula is invalid. Returning empty sample.");
            return Collections.emptySet();
        }

        // The total list of all configurations (sample) that is generated and returned
        Set<Assignment> sample = new HashSet<>();

        // From the given set of features (variables of the formula), generate a set of all valid value schemas
        LOG.info("Deriving valid value schemas...");
        Set<Set<Literal>> valueSchemas = getValidFeatureCombinations(cnfFormula);
        LOG.info("Derived {} valid value schemas", valueSchemas.size());

        // We want to create a sample that cover all of our collected valid value schemas. So we continue to generate new
        // configurations that cover as many value schemas as possible, until all value schemas have been covered
        while (!valueSchemas.isEmpty()) {
            LOG.trace("{} value schemas remaining", valueSchemas.size());

            // Create a new configuration that is filled with value schemas as long as possible
            Assignment configuration = new Assignment();

            // Remember all value schemas that have been covered in the current while-loop-iteration
            Set<Set<Literal>> coveredValueSchemas = new HashSet<>();

            // Add as many of the value schemas to the current configuration as possible. For each value schema, we try
            // to add it to the current configuration, if it does not make the configuration invalid
            for (Set<Literal> valueSchema : valueSchemas) {

                // Create an assignment from the current set of literals
                Assignment valueSchemaAssignment = createAssignmentFrom(valueSchema);
                LOG.trace("Processing value schema '{}'", valueSchemaAssignment);

                // Check if the current value schema can be merged with the configuration. If this is not the case, the
                // value schema is ignored and we look at the next value schema
                if (Assignment.canBeMerged(configuration, valueSchemaAssignment)) {

                    // Now we know that the current value schema can be merged with the current configuration. The
                    // result of this is called the "configuration candidate". It's just a candidate, since it's
                    // possible that by merging the value schema with the configuration makes the configuration invalid.
                    // So we need to check, if the configuration candidate is valid. If not, the merging made the
                    // configuration invalid and we skip the value schema that caused this. If the candidate however is
                    // remains valid, it is used as the new configuration.
                    Assignment configurationCandidate = Assignment.merge(configuration, valueSchemaAssignment);

                    // Check if the configuration candidate is valid for the formula
                    if (isConfigurationValidForFormula(configurationCandidate, cnfFormula)) {
                        LOG.trace("Value schema {} is valid and added to current configuration", valueSchemaAssignment);

                        // Mark the current value schema as covered by adding it to the list of covered value schemas
                        // and update the configuration with our candidate
                        coveredValueSchemas.add(valueSchema);
                        configuration = configurationCandidate;
                        LOG.trace("New configuration is {}", configuration);
                    }
                }
            }

            // At this point we have once added as many value schemas to the current configuration as possible. All
            // value schemas that have been covered by the current configuration are removed from the value schemas that
            // have to be covered
            valueSchemas.removeAll(coveredValueSchemas);
            LOG.debug("Iteration covered {} value schemas. {} remaining", coveredValueSchemas.size(), valueSchemas.size());

            // The current value schema may satisfy the formula, but it can still be incomplete, meaning that it does
            // not assign a value to each variable of the formula (covers it). If that is the case, we fill up all
            // remaining variables, while making sure that the assignment still satisfies the formula.
            if (!configuration.covers(cnfFormula)) {
                // Evaluate each literal in the formula. The resulting formula will only contain the variables that are
                // missing in the configuration
//                CnfFormula cnfFormulaWithMissingVariables = cnfFormula.restrictCnf(configuration);

                // Generate a configuration that is complete AND satisfies the formula
                configuration = getCompleteConfigurationForFormula(configuration, cnfFormula);
            }
            sample.add(configuration);
        }

        return sample;
    }

    /**
     * Checks if the given configuration is valid for the given formula. A configuration is valid for a formula, if the
     * new formula, that is created by restricting the given formula with the given configuration, is satisfiable. This
     * is done by simplifying the formula with the configuration and
     * checking if the simplified formula is empty (configuration is valid), contains an empty clause (configuration is
     * invalid) or is not satisfiable according to the solver provided in {@link BasicKWiseCombinatorialSampler#BasicKWiseCombinatorialSampler(ISolver,
     * int)} (configuration is invalid).
     *
     * @param configuration the configuration that is tested if it satisfies the formula
     * @param cnfFormula    the formula that the configuration is tested for
     * @return <code>true</code> if the configuration is valid for the formula, <code>false</code> otherwise
     */
    private boolean isConfigurationValidForFormula(Assignment configuration, CnfFormula cnfFormula) {
        CnfFormula cnfFormulaWithConfiguration = simplifier.simplify(cnfFormula, configuration);
        return solver.solve(cnfFormulaWithConfiguration).isPresent();
    }

    /**
     * Given an (possibly) incomplete configuration, this method finds the missing variable assignments to expand the
     * given configuration to a complete configuration that satisfies the formula.
     *
     * @param configuration the configuration that may be incomplete and is expanded to a complete configuration
     * @param cnfFormula    the formula that is used to find the missing assignments (the missing assignments must
     *                      satisfy this formula)
     * @return a complete configuration that is a superset of the given configuration and satisfies the given formula
     */
    private Assignment getCompleteConfigurationForFormula(Assignment configuration, CnfFormula cnfFormula) {

        // If the configuration covers the formula, it is already complete
        if (configuration.covers(cnfFormula)) {
            return new Assignment(configuration);
        }

        // We have a formula and an incomplete configuration. To get the missing assignment for our incomplete
        // configuration, we first restrict the formula with the given assignment. This will return a formula which does
        // only contain the missing variables. Then we use the SAT solver to find an assignment for the formula that
        // only contains the missing variables. This is possible, since the solver also returns a satisfying assignment,
        // if a formula is satisfiable
        CnfFormula cnfFormulaWithMissingVariables = cnfFormula.evaluate(configuration);
        Optional<Assignment> missingAssignment = solver.solve(cnfFormulaWithMissingVariables);

        // If the formula is solvable we use the calculated satisfying assignment to merge it with the configuration to
        // get the complete configuration
        if (missingAssignment.isPresent()) {
            Assignment missingAssignments = missingAssignment.get();
            LOG.trace("Missing assignment is '{}'", missingAssignments);

            // The two configuration MUST be distinct
            if (Assignment.canBeMergedDistinct(configuration, missingAssignments)) {
                return Assignment.mergeDistinct(configuration, missingAssignments);
            } else {
                throw new RuntimeException(String.format("Assignment for missing variables is not distinct from configuration. Missing assignment was %s. Sample was %s.", missingAssignments, configuration));
            }
        } else {
            throw new RuntimeException(String.format("Could not find assignment for missing variables. The formula was not solvable. Formula was %s", cnfFormulaWithMissingVariables));
        }
    }

    /**
     * Given a set of literals, this method creates an assignment that lets all literals evaluate to true. This is done
     * by choosing the variable mapping for the literal based on its polarization.
     *
     * @param literals the literals that the assignment is created for
     * @return an assignment that lets all literals evaluate to true
     */
    private Assignment createAssignmentFrom(Set<Literal> literals) {
        Assignment assignment = new Assignment();
        for (Literal literal : literals) {
            assignment = Assignment.mergeDistinct(assignment, Assignment.from(literal));
        }

        return assignment;
    }

    /**
     * Generates a set that contains all valid combinations of {@link #k} features in the given formula. Each
     * combination of features is expanded to also contain all possible polarizations (selection) of this features. All
     * 2-wise combinatorial feature combinations for the formula that consist of the variables {1, 2, 3} would therefore
     * be {{1, 2}, {-1, 2}, {1, -2}, {-1, -2}, {2, 3}, {-2, 3}, {2, -3}, {-2, -3}, {1, 3}, {-1, 3}, {1, -3}, {-1, -3}}.
     * From this list all invalid combinations are removed, leaving only all valid k-wise combinatorial features left.
     *
     * @param cnfFormula the formula that is used to derive the feature combinations
     * @return a set containing all valid {@link #k}-wise combinatorial feature combinations for the formula
     */
    private Set<Set<Literal>> getValidFeatureCombinations(CnfFormula cnfFormula) {

        // Result set containing all valid feature combinations
        Set<Set<Literal>> validFeatureCombinations = new HashSet<>();

        // Get all features of the model (variables of the formula) (e.g. {1, 2, 3})
        Set<Variable> features = cnfFormula.getVariables();

        // All possible features combination (e.g. {{1, 2}, {2, 3}, {1, 3}} )
        Set<Set<Variable>> allFeatureCombinations = getAllCombinations(features);

        // Now we got a set of all possible variable combinations, this would be fine, if we would just want to do
        // "Pairwise Feature". But we want to do k-wise combinatorial feature coverage. So we need to generate all
        // possible combinations of polarizations that the variables can have.
        // (e.g. {{1, 2}, {-1, 2}, {1, -2}, {-1, -2}, {2, 3}, {-2, 3}, {2, -3}, {-2, -3}, {1, 3}, {-1, 3}, {1, -3}, {-1, -3}})
        Set<Set<Literal>> allPolarizationCombinations = allFeatureCombinations.stream()
                .flatMap(variables -> getAllPolarizationCombinations(variables).stream())
                .collect(Collectors.toSet());

        // Since we only want to generate valid configurations, we remove the combinations that are invalid. This is done, by
        // checking, if the model is valid with this specific selection of features. This problem can be answered by
        // checking if SAT(formula && f1 && f2 && ... && fn) is valid. All features that are tested, are added to the
        // conjunction of clauses of the given formula. If the formula then is still satisfiable, it means that the
        // feature selection is valid as well
        allPolarizationCombinations.forEach(featureCombination -> {

            // Create copy so we do not modify the original formula
            CnfFormula cnfFormulaWithFeatureSelection = new CnfFormula(cnfFormula);
            Set<Or<Atom>> featureCombinationClauses = featureCombination.stream()
                    // For each feature (literal), create a clause that only contains one literal
                    .map(literal -> new Or<Atom>(Collections.singleton(literal)))
                    .collect(Collectors.toSet());

            // Add all of the calculated clauses to the formula to check if the feature selection is valid
            cnfFormulaWithFeatureSelection.getClauses().addAll(featureCombinationClauses);

            // Run the solver on the calculated formula to check if it is satisfiable
            boolean featureCombinationIsValid = solver.solve(cnfFormulaWithFeatureSelection).isPresent();

            if (featureCombinationIsValid) {
                validFeatureCombinations.add(featureCombination);
            }
        });

        return validFeatureCombinations;
    }

    /**
     * Given a set of variables, this method generates all possible polarizations combinations. For example, all
     * polarizations of the set {1, 2} are {{1, 2}, {-1, 2}, {1, -2}, {-1, -2}}.
     *
     * @param variablesSet the set of all variables whose polarization combinations are returned
     */
    private Set<Set<Literal>> getAllPolarizationCombinations(Set<Variable> variablesSet) {

        // This method polarizes the variables according to the boolean value of the combination provider. Therefore
        // it is essential, that the iterator in the polarize method picks up the variables in the exact same order each
        // time. This may not be the case for a set, since they do not have a defined order and are not guaranteed to
        // provide the variables in the same order each time.
        ArrayList<Variable> variables = new ArrayList<>(variablesSet);

        // Result set containing all possible polarization combinations of the given variables
        Set<Set<Literal>> allPolarizationCombinations = new HashSet<>();

        // We can reuse our ICombinationProvider to get all possible combinations for a boolean array of size 'k'. For
        // example, for k=2, the ICombinationProvider would return the boolean arrays [TRUE, TRUE], [TRUE, FALSE],
        // [FALSE, TRUE] and [FALSE, FALSE]. We can use this arrays to generate out polarization combinations
        ICombinationProvider combinationProvider = new SequentialCombinationProvider(k);

        // Consider each boolean array like [TRUE, FALSE] and polarize the variables accordingly
        while (combinationProvider.hasNext()) {

            // Get the next boolean value combination like [TRUE, FALSE]
            boolean[] polarizations = combinationProvider.next();

            // Polarize the variables according to the boolean values. If the given variables are e.g. {1, 2}, and the
            // current boolean array is [TRUE, FALSE], the result of the polarize method will be {1, -2}. This is done
            // for each possible boolean combination
            allPolarizationCombinations.add(polarize(variables, polarizations));
        }

        return allPolarizationCombinations;
    }

    /**
     * Polarizes the given variables according to the values in the given boolean array. When providing a boolean array
     * like [FALSE, TRUE, FALSE] and a list of variables like {A, B, C} the returned set of literals will be {-A, B,
     * -C}.
     *
     * @param variables     the variables that are polarized
     * @param polarizations the polarization values for the variables
     * @return a set of literals where each literal is polarized according to the boolean array
     */
    private Set<Literal> polarize(List<Variable> variables, boolean[] polarizations) {

        // Check that the boolean array contains the exact number of polarizations as the set contains variables
        if (variables.size() != polarizations.length) {
            throw new IllegalArgumentException(String.format("The number of variables '%s' does not match the number of polarizations that should be applied '%s'", variables.size(), polarizations.length));
        }

        // For each boolean value we polarize a variable. The result is a literal, since we will use the literals in our
        // solver
        Iterator<Variable> iterator = variables.iterator();
        return Booleans.asList(polarizations).stream()
                // For each boolean the next variable is polarized (a literal is created using the boolean polarization)
                .map(polarization -> new Literal(iterator.next(), polarization))
                // The result is collected and returned as set
                .collect(Collectors.toSet());
    }

    /**
     * From a given set of variables, this method generates a set that contains all possible combinations of the
     * variables. For example, for the set of variables {1, 2, 3} getAllCombinations would return {{1, 2}, {1, 3}, {2,
     * 3}}.<br/> This method uses Google Guavas {@link Sets#combinations(Set, int)} method which is currently marked as
     * {@link Beta}.
     *
     * @param variables the set of all variables whose combinations are returned
     * @return all possible combinations of the * variables
     */
    private Set<Set<Variable>> getAllCombinations(Set<Variable> variables) {
        return Sets.combinations(variables, k);
    }
}
