package org.mbe.sat.core.model;

import com.google.common.collect.Sets;
import org.mbe.sat.core.model.formula.Formula;
import org.mbe.sat.core.model.formula.Literal;
import org.mbe.sat.core.model.formula.Tristate;
import org.mbe.sat.core.model.formula.Variable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

/**
 * An assignment assigns a boolean value (<code>true</code> or <code>false</code>) to each variable in a set of
 * variables.
 */
public class Assignment {

    /**
     * Static logger instance for this class.
     */
    private static final Logger LOG = LoggerFactory.getLogger(Assignment.class);

    /**
     * Map that contains the actual mapping from variables to boolean values.
     */
    private final Map<Variable, Boolean> variableMapping;

    /**
     * Creates a new instance of {@link Assignment} with no variable assignments.
     */
    public Assignment() {
        variableMapping = new HashMap<>();
    }

    /**
     * Copy constructor that creates a new instance of {@link Assignment} by creating a duplicate of the given
     * assignment.
     *
     * @param assignment the assignment that is copied to create a new instance
     */
    public Assignment(Assignment assignment) {
        this();
        // Copy all values of the given assignment in this one
        variableMapping.putAll(assignment.variableMapping);
    }

    /**
     * Creates a new instance of {@link Assignment} with no variable assignments.
     */
    public static Assignment empty() {
        return new Assignment();
    }

    /**
     * Creates an assignment that assign the given boolean value to each of the given variables.
     *
     * @param variables the variables which get the given boolean value is assigned
     * @param value     the boolean value that is assigned to each variable
     * @return an assignment that assigns the given boolean value to each of the given variables
     */
    public static Assignment from(boolean value, Variable... variables) {
        Assignment assignment = new Assignment();
        Arrays.stream(variables).forEach(variable -> assignment.setValue(variable, value));
        return assignment;
    }

    /**
     * Creates an assignment that assign the given boolean value to the given variable.
     *
     * @param variable the variable which a boolean value is assigned to
     * @param value    the boolean value that is assigned to the variable
     * @return an assignment that assigns the given boolean value to the given variable
     */
    public static Assignment from(Variable variable, boolean value) {
        return from(value, variable);
    }

    /**
     * Creates an assignment that assign a boolean value to the given literals variable so that the literal evaluates to
     * true.
     */
    public static Assignment from(Literal literal) {
        Assignment assignment = new Assignment();
        assignment.setValue(literal.getVariable(), literal.isPositive());
        return assignment;
    }

    /**
     * Creates an assignment that assign each variable the boolean value of the given boolean array that is stored at
     * the same index as the variable is in the list. So the variable at index <code>i</code> gets assigned the boolean
     * value from the array at index <code>i</code>.
     *
     * @param variables the variables that get assigned the boolean value from the given array
     * @param values    the array that contains the values for the given variables
     * @return an assignment that assigns the boolean values of the array to the given list of variables
     */
    public static Assignment from(List<Variable> variables, boolean[] values) {

        // Check if the same number of variables and booleans is given
        if (variables.size() != values.length) {
            throw new IllegalArgumentException(String.format("The number of variables must be equal to the number of booleans. Got '%s' variables and '%s' booleans.", variables.size(), values.length));
        }

        // Create the assignment that is being created
        Assignment assignment = new Assignment();

        // Use indexed for loop to access list and array at the same time
        for (int i = 0; i < values.length; i++) {
            Variable variable = variables.get(i);
            boolean value = values[i];
            assignment.setValue(variable, value);
        }
        return assignment;
    }

    /**
     * Makes sure that the assignment covers all variables in the the given list by assigning the given boolean value to
     * each variable that is not yet covered by the assignment. All existing variable values will keep their original
     * values.
     *
     * @param variables the variables which must be covered by the returned assignment
     * @param value     the boolean value that is set for all missing variables
     */
    public void setValueForUncoveredVariables(Set<Variable> variables, boolean value) {
        LOG.trace("Filling missing variables in assignment...");

        // We don't need to fill the variable assignments if the variables are already covered by the assignment
        if (covers(variables)) {
            LOG.trace("Assignment does already cover all variables in the list");
            return;
        }

        // Assign the given boolean value to all variables that are not covered by the assignment
        variables.stream()
                // We only want to fill the values for the variables that are not yet covered by the assignment
                .filter(variable -> !covers(variable))
                // Using peek to produce some logging output
                .peek(variable -> LOG.trace("Filling missing variable '{}'", variable))
                // We simply fill the variables with the given value
                .forEach(variable -> setValue(variable, value));
    }

    /**
     * Returns a Tristate representing the value of the variable in the assignment, or an {@link Tristate#UNDEFINED
     * UNDEFINED} Tristate, if the variable is not covered by the assignment.
     *
     * @param variable the variable whose value is returned
     * @return the value that is assigned to the variable
     *
     * @see #covers(Variable)
     */
    public Tristate getValue(Variable variable) {
        if (!covers(variable)) {
            return Tristate.UNDEFINED;
        } else {
            return Tristate.of(variableMapping.get(variable));
        }
    }

    /**
     * Assigns a new value to the variable, overwriting any existing assignment for the variable if present.
     *
     * @param variable the variable that gets the given boolean value assigned
     * @param value    the boolean value that is assigned to the variable
     */
    public void setValue(Variable variable, boolean value) {
        variableMapping.put(variable, value);
    }

    /**
     * Checks if the assignment assigns a value to the given variable. It returns <code>true</code> if a boolean value
     * is assigned to the variable, or <code>false</code> otherwise.
     *
     * @param variable the variable that is checked for the presence of an assignment
     * @return <code>true</code> if a boolean value is assigned to variable, <code>false</code> otherwise
     */
    public boolean covers(Variable variable) {
        return variableMapping.containsKey(variable);
    }

    /**
     * Checks if the assignment assigns a value to all variables in the given array. It returns <code>true</code> if a
     * boolean value is assigned to each variable, or <code>false</code> otherwise.
     *
     * @param variables the variables that are checked for the presence of an assignment
     * @return <code>true</code> if a boolean value is assigned to each variable, <code>false</code> otherwise
     */
    public boolean covers(Variable... variables) {
        return covers(new HashSet<>(Arrays.asList(variables)));
    }

    /**
     * Checks if the assignment assigns a value to all variables in the given list. It returns <code>true</code> if a
     * boolean value is assigned to each variable, or <code>false</code> otherwise.
     *
     * @param variables the variables that are checked for the presence of an assignment
     * @return <code>true</code> if a boolean value is assigned to each variable, <code>false</code> otherwise
     */
    public boolean covers(Set<Variable> variables) {
        return variables.stream().allMatch(this::covers);
    }

    /**
     * Checks if the assignment assigns a value to all variables in the given formula. It returns <code>true</code> if a
     * boolean value is assigned to each variable, or <code>false</code> otherwise.
     *
     * @param formula the formula whose variables are checked for the presence of an assignment
     * @return <code>true</code> if a boolean value is assigned to each variable, <code>false</code> otherwise
     */
    public boolean covers(Formula formula) {
        return covers(formula.getVariables());
    }

    /**
     * Merges this assignment with the given one. The given assignment is not modified. The assignments can only be
     * merged, if both assignments {@link #canBeMergedDistinct are distinct}, meaning that they do not share any
     * variable assignments. This is also true, if they assign the same value to all of the shared variables. This
     * specific method may only be useful, if you know that you only deal with distinct assignments and want to enforce
     * this condition, since this method will throw an {@link AssignmentException} if the two assignment are not
     * distinct. Since it is actually safe to merge non-distinct assignments, consider using {@link
     * #merge(Assignment)}.
     * <p>
     * The following list shows examples of assignments that cannot be merged, since they are not distinct:
     *      <ul>
     *         <li>(A -> TRUE, B -> FALSE) AND (A -> FALSE, C -> FALSE)</li>
     *         <li>(A -> TRUE) AND (A -> TRUE, C -> FALSE)</li>
     *         <li>(A -> TRUE) AND (A -> TRUE)</li>
     *     </ul>
     *     The following list shows examples of assignments that can be merged, since they are distinct:
     *     <ul>
     *          <li>(empty) AND (A -> TRUE)</li>
     *          <li>(A -> TRUE, B -> FALSE) AND (C -> FALSE)</li>
     *     </ul>
     * </p>
     *
     * @param assignment the assignment that is merged
     * @throws AssignmentException if the two assignments share variable mappings (are not distinct)
     * @see #merge(Assignment)
     * @see #canBeMergedDistinct(Assignment, Assignment)
     */
    public void mergeDistinct(Assignment assignment) throws AssignmentException {

        // Throw assignment exception if the assignments are not distinct
        if (!canBeMergedDistinct(this, assignment)) {
            throw new AssignmentException("The given assignments cannot be merged, since they share variables. Make sure to merge assignments with distinct variable sets.");
        }

        // Add all variable assignment from the given assignment into this one
        mergeInternal(assignment);
    }

    /**
     * <p>
     * Static version of {@link #mergeDistinct(Assignment)}. Description copied from {@link #mergeDistinct(Assignment)}
     * and modified.
     * </p>
     * Merges the two given assignments. Both assignments are not modified. The assignments can only be merged, if both
     * assignments {@link #canBeMergedDistinct are distinct}, meaning that they do not share any variable mappings. This
     * is also true, if they map the same value to all of the shared variables. This specific method may only be useful,
     * if you know that you only deal with distinct assignments and want to enforce this condition, since this method
     * will throw an {@link AssignmentException} if the two assignment are not distinct. Since it is actually safe to
     * merge non-distinct assignments, consider using {@link #merge(Assignment, Assignment)}.
     * <p>
     * The following list shows examples of assignments that cannot be merged, since they are not distinct:
     *      <ul>
     *         <li>(A -> TRUE, B -> FALSE) AND (A -> FALSE, C -> FALSE)</li>
     *         <li>(A -> TRUE) AND (A -> TRUE, C -> FALSE)</li>
     *         <li>(A -> TRUE) AND (A -> TRUE)</li>
     *     </ul>
     *     The following list shows examples of assignments that can be merged, since they are distinct:
     *     <ul>
     *          <li>(empty) AND (A -> TRUE)</li>
     *          <li>(A -> TRUE, B -> FALSE) AND (C -> FALSE)</li>
     *     </ul>
     * </p>
     *
     * @param assignmentA the first assignment that is merged with the second one
     * @param assignmentB the second assignment that is merged with the first one
     * @return a new assignment that contains all variable mappings of the two given assignments
     *
     * @throws AssignmentException if the two assignments share variable mappings (are not distinct)
     */
    public static Assignment mergeDistinct(Assignment assignmentA, Assignment assignmentB) throws AssignmentException {

        // The method mergeDistinct ensures that the two assignments do not share variables
        Assignment result = new Assignment();
        result.mergeDistinct(assignmentA);
        result.mergeDistinct(assignmentB);

        return result;
    }

    /**
     * Merges this assignment with the given one. The given assignment is not modified. The assignments can only be
     * merged, if both assignments {@link #canBeMerged only share variables with the same mapping}, meaning that they do
     * not share any variables that are mapped to different boolean values.
     * <p>
     * The following list shows examples of assignments that cannot be merged, since they share different variable
     * mappings:
     *  <ul>
     *   <li>(A -> TRUE) AND (A -> FALSE)</li>
     *   <li>(A -> TRUE, B -> TRUE) AND (A -> FALSE, B -> TRUE)</li>
     *  </ul>
     *  The following list shows examples of assignments that can be merged, since they do only share variable mappings
     *  with the same value, or assignments that do not share any variable mappings:
     *  <ul>
     *   <li>(A -> TRUE) AND (A -> TRUE)</li>
     *   <li>(A -> TRUE, B -> FALSE) AND (A -> TRUE, B -> FALSE)</li>
     *   <li>(A -> TRUE, B -> FALSE) AND (A -> TRUE, C -> FALSE)</li>
     *   <li>(empty) AND (A -> TRUE)</li>
     *   <li>(A -> TRUE, B -> FALSE) AND (C -> FALSE)</li>
     *  </ul>
     * </p>
     *
     * @param assignment the assignment that is merged with this one
     * @throws AssignmentException if the assignments cannot be merged since they share variable mappings with different
     *                             values
     * @see #merge(Assignment, Assignment)
     */
    public void merge(Assignment assignment) throws AssignmentException {

        // Check if the two assignments do only share variables that do already have the same polarization
        if (!canBeMerged(this, assignment)) {
            throw new AssignmentException("The given assignments cannot be merged, since they share variables that have different values.");
        }

        mergeInternal(assignment);
    }

    /**
     * <p>
     * Static version of {@link #merge(Assignment)}. Description copied from {@link #merge(Assignment)} and modified.
     * </p>
     * Merges the two given assignments. The given assignments are not modified. The assignments can only be merged, if
     * both assignments {@link #canBeMerged only share variables with the same mapping}, meaning that they do not share
     * any variables that are mapped to different boolean values.
     * <p>
     * The following list shows examples of assignments that cannot be merged, since they share different variable
     * mappings:
     *  <ul>
     *   <li>(A -> TRUE) AND (A -> FALSE)</li>
     *   <li>(A -> TRUE, B -> TRUE) AND (A -> FALSE, B -> TRUE)</li>
     *  </ul>
     *  The following list shows examples of assignments that can be merged, since they do only share variable mappings
     *  with the same value, or assignments that do not share any variable mappings:
     *  <ul>
     *   <li>(A -> TRUE) AND (A -> TRUE)</li>
     *   <li>(A -> TRUE, B -> FALSE) AND (A -> TRUE, B -> FALSE)</li>
     *   <li>(A -> TRUE, B -> FALSE) AND (A -> TRUE, C -> FALSE)</li>
     *   <li>(empty) AND (A -> TRUE)</li>
     *   <li>(A -> TRUE, B -> FALSE) AND (C -> FALSE)</li>
     *  </ul>
     * </p>
     *
     * @param assignmentA the first assignment that is merged with the second one
     * @param assignmentB the second assignment that is merged with the first one
     * @return a new assignment that contains all variable mappings of the two given assignments
     *
     * @throws AssignmentException if the assignments cannot be merged since they share variable mappings with different
     *                             values
     */
    public static Assignment merge(Assignment assignmentA, Assignment assignmentB) throws AssignmentException {

        Assignment result = new Assignment();
        result.merge(assignmentA);
        result.merge(assignmentB);

        return result;
    }

    /**
     * Checks if the given assignments are safe to be merged, by checking if they do not share any variables. Since it
     * is actually possible to merge assignments that share variables (are not distinct) consider using {@link
     * #canBeMerged(Assignment, Assignment)} instead.
     * <p>
     * The following list shows examples of assignments that cannot be merged, since they are not distinct:
     *      <ul>
     *         <li>(A -> TRUE, B -> FALSE) AND (A -> FALSE, C -> FALSE)</li>
     *         <li>(A -> TRUE) AND (A -> TRUE, C -> FALSE)</li>
     *         <li>(A -> TRUE) AND (A -> TRUE)</li>
     *     </ul>
     *     The following list shows examples of assignments that can be merged, since they are distinct:
     *     <ul>
     *          <li>(empty) AND (A -> TRUE)</li>
     *          <li>(A -> TRUE, B -> FALSE) AND (C -> FALSE)</li>
     *     </ul>
     * </p>
     *
     * @param assignmentA the first assignment that is checked if it can be merged with the second one
     * @param assignmentB the second assignment that is checked if it can be merged with the first one
     * @return <code>true</code> if the two assignments can be merged, or <code>false</code> otherwise
     *
     * @see #canBeMerged(Assignment, Assignment)
     */
    public static boolean canBeMergedDistinct(Assignment assignmentA, Assignment assignmentB) {
        return Collections.disjoint(assignmentA.variableMapping.keySet(), assignmentB.variableMapping.keySet());
    }

    /**
     * Checks if the given assignments are safe to be merged. Two assignments are safe to be merged if they only share
     * variable mappings that assign the same value to each variable. In other words, assignments that map different
     * values to the same variable are not safe to be merged. Assignments are also safe to be merged if they do not
     * share any variables (are distinct).
     * <p>
     * The following list shows examples of assignments that can be merged:
     * <ul>
     *  <li>(empty) AND (A -> TRUE)</li>
     *  <li>(A -> TRUE) AND (B -> FALSE)</li>
     *  <li>(A -> TRUE) AND (A -> TRUE)</li>
     *  <li>(A -> TRUE) AND (A -> TRUE, B -> FALSE)</li>
     *  <li>(A -> TRUE, B -> FALSE) AND (C -> FALSE, D -> TRUE)</li>
     * </ul>
     * The following list shows examples of assignments that cannot be merged:
     * <ul>
     *  <li>(A -> TRUE) AND (A -> FALSE)</li>
     *  <li>(A -> TRUE, B -> TRUE) AND (A -> TRUE, B -> FALSE)</li>
     * </ul>
     * </p>
     *
     * @param assignmentA the first assignment that is checked if it is safe be be merged with the second assignment
     * @param assignmentB the second assignment that is checked if it is safe be be merged with the first assignment
     * @return <code>true</code> if the two assignments can be merged, or <code>false</code> otherwise
     */
    public static boolean canBeMerged(Assignment assignmentA, Assignment assignmentB) {
        // Get the keys that are contained in both assignments
        Sets.SetView<Variable> intersection = Sets.intersection(assignmentA.variableMapping.keySet(), assignmentB.variableMapping.keySet());

        // If the two sets do not have any variable in common, they can safely be merged
        if (intersection.isEmpty()) {
            return true;
        }

        // If there is a key that is present in both assignments, the assigned value must be equal in both sets
        return intersection.stream().allMatch(variable -> assignmentA.getValue(variable).equals(assignmentB.getValue(variable)));
    }

    /**
     * Internal method that merges this assignment with the given one. It does not check if the assignments are distinct
     * and simply adds all assignments from the given one to this. To make sure that merging is safe, either use {@link
     * #merge(Assignment)} or {@link #mergeDistinct(Assignment)}.
     *
     * @param assignment the assignment that is merged
     */
    private void mergeInternal(Assignment assignment) {
        this.variableMapping.putAll(assignment.variableMapping);
    }

    /**
     * Returns all variables that are covered by the assignment.
     *
     * @return all variables that are covered by the assignment
     */
    public List<Variable> getVariables() {
        return new ArrayList<>(variableMapping.keySet());
    }

    /**
     * Returns a string that shows the boolean value for each variable in the assignment.
     *
     * @return a string that shows the boolean value for each variable in the assignment
     */
    @Override
    public String toString() {
        return "Assignment{" + getVariables().stream().map(variable -> variable + "=" + getValue(variable).toString()).collect(Collectors.joining(", ")) + '}';
    }
}
