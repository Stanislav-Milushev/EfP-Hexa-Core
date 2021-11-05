package org.mbe.sat.core.problem;

public class SatProblemDefinition {
    private String file;
    private boolean satisfiable;
    private SatProblemJsonModel.Complexity complexity;
    private int numberOfClauses;
    private int numberOfVariables;

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public boolean isSatisfiable() {
        return satisfiable;
    }

    public void setSatisfiable(boolean satisfiable) {
        this.satisfiable = satisfiable;
    }

    public SatProblemJsonModel.Complexity getComplexity() {
        return complexity;
    }

    public void setComplexity(SatProblemJsonModel.Complexity complexity) {
        this.complexity = complexity;
    }

    public int getNumberOfClauses() {
        return numberOfClauses;
    }

    public void setNumberOfClauses(int numberOfClauses) {
        this.numberOfClauses = numberOfClauses;
    }

    public int getNumberOfVariables() {
        return numberOfVariables;
    }

    public void setNumberOfVariables(int numberOfVariables) {
        this.numberOfVariables = numberOfVariables;
    }
}
