package org.mbe.sat.core.problem;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

public class SatProblemFixtures {

    public static final String RESOURCE_BASE_PATH = "/org/mbe/sat/core/problem";

    public static Collection<SatProblemJsonModel> getAll() {
        InputStream resourceAsStream = SatProblemFixtures.class.getResourceAsStream(RESOURCE_BASE_PATH + "/_index.json");
        Reader reader = new InputStreamReader(resourceAsStream);

        JsonDeserializer<SatProblemJsonModel> deserializer = new DimacsSatProblemDeserializer(RESOURCE_BASE_PATH);

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(SatProblemJsonModel.class, deserializer)
                .create();

        return Arrays.asList(gson.fromJson(reader, SatProblemJsonModel[].class));
    }

    public static Collection<SatProblemJsonModel> getByComplexityCombined(SatProblemJsonModel.Complexity complexity) {
        return getAll().stream().filter(satProblem -> complexity.getIncludedComplexities().contains(satProblem.getComplexity())).collect(Collectors.toList());
    }

    public static Collection<SatProblemJsonModel> getByComplexity(SatProblemJsonModel.Complexity complexity) {
        return getAll().stream().filter(satProblem -> satProblem.getComplexity().equals(complexity)).collect(Collectors.toList());
    }

    public static SatProblemJsonModel getByFileName(String fileName) {
        return getAll().stream().filter(satProblem -> satProblem.getFileName().equals(fileName)).findFirst().get();
    }

    public static Collection<SatProblemJsonModel> getTrivial() {
        return getByComplexity(SatProblemJsonModel.Complexity.TRIVIAL);
    }

    public static Collection<SatProblemJsonModel> getEasy() {
        return getByComplexity(SatProblemJsonModel.Complexity.EASY);
    }

    public static Collection<SatProblemJsonModel> getMedium() {
        return getByComplexity(SatProblemJsonModel.Complexity.MEDIUM);
    }

    public static Collection<SatProblemJsonModel> getHard() {
        return getByComplexity(SatProblemJsonModel.Complexity.HARD);
    }

    public static Collection<SatProblemJsonModel> getInsane() {
        return getByComplexity(SatProblemJsonModel.Complexity.INSANE);
    }
}
