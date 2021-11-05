package org.mbe.sat.core.problem;

import com.google.gson.Gson;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Arrays;
import java.util.Collection;

/**
 * Provides utility methods to access fixed {@link SatProblemDefinition SatProblemDefinitions}.
 */
public class SatProblemDefinitionFixtures {

    /**
     * Uses the "_index.json" file in the given resources folder to locate {@link SatProblemDefinition SAT problem
     * definitions}. The "_index.json" file contains a set of SAT problem definitions and a file name to its
     * corresponding .cnf file. The given resource folder must point to a folder in a
     * <a href="https://docs.gradle.org/current/dsl/org.gradle.api.tasks.SourceSet.html">SourceSet</a>. Usually these
     * paths are organized like Java packages and separated by forward slashes, e.g. "org/mbe/sat/core/problem".
     *
     * @param resourceFolder the path to the resource folder that contains the "_index.json" file
     * @return a collection of all {@link SatProblemDefinition SAT problem definitions} that are defined in the
     * "_index.json" file
     */
    public static Collection<SatProblemDefinition> getAll(String resourceFolder) {
        InputStream resourceAsStream = SatProblemDefinitionFixtures.class.getResourceAsStream("/" + resourceFolder + "/_index.json");
        Reader reader = new InputStreamReader(resourceAsStream);

        return Arrays.asList(new Gson().fromJson(reader, SatProblemDefinition[].class));
    }
}
