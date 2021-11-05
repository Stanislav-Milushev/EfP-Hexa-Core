package org.mbe.sat.core.problem;

import com.google.gson.*;
import org.mbe.sat.api.parser.IFormulaParser;
import org.mbe.sat.core.model.formula.CnfFormula;
import org.mbe.sat.core.model.formula.Tristate;
import org.mbe.sat.core.parser.DimacsFormulaParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;

public class DimacsSatProblemDeserializer implements JsonDeserializer<SatProblemJsonModel> {

    private final IFormulaParser<CnfFormula> formulaParser;
    private final String resourceBasePath;

    public DimacsSatProblemDeserializer(String resourceBasePath) {
        this.resourceBasePath = resourceBasePath;
        this.formulaParser = DimacsFormulaParserFactory.createFormulaParser();
    }

    @Override
    public SatProblemJsonModel deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();

        // Use the default JSON deserialization, so we only need to manually deserialize the formula
        SatProblemJsonModel satProblem = new Gson().fromJson(jsonObject, SatProblemJsonModel.class);

        // Manually parse field "satisfiable", since it is not stored as boolean, but as Tristate
        String satisfiableJsonString = jsonObject.get("satisfiable").getAsString();
        Tristate satisfiable = Tristate.of(Boolean.parseBoolean(satisfiableJsonString));
        satProblem.setSatisfiable(satisfiable);

        // Manually parse the formula  by using the file name to load and parse the file using the given formula parser
        String fileName = jsonObject.get("file").getAsString();
        satProblem.setFileName(fileName);

        try (InputStream resourceAsStream = SatProblemFixtures.class.getResourceAsStream(resourceBasePath + "/" + fileName)) {
            CnfFormula formula = formulaParser.parse(resourceAsStream);
            satProblem.setFormula(formula);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return satProblem;
    }
}
