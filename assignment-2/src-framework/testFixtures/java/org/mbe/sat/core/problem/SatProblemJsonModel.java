package org.mbe.sat.core.problem;

import org.mbe.sat.core.model.formula.CnfFormula;
import org.mbe.sat.core.model.formula.Tristate;

import java.util.HashSet;
import java.util.Set;

/**
 * Model class that represents objects which are loaded from the "_index.json" file when testing parsers and solvers.
 * Therefore the scope of this class is "test" only.
 */
public class SatProblemJsonModel {
    private CnfFormula formula;
    private transient Tristate satisfiable;
    private Complexity complexity;
    private String fileName;

    public SatProblemJsonModel(CnfFormula formula, Tristate satisfiable, Complexity complexity) {
        this.formula = formula;
        this.satisfiable = satisfiable;
        this.complexity = complexity;
    }

    public CnfFormula getFormula() {
        return formula;
    }

    public void setFormula(CnfFormula formula) {
        this.formula = formula;
    }

    public Tristate isSatisfiable() {
        return satisfiable;
    }

    public void setSatisfiable(Tristate satisfiable) {
        this.satisfiable = satisfiable;
    }

    public Complexity getComplexity() {
        return complexity;
    }

    public void setComplexity(Complexity complexity) {
        this.complexity = complexity;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * Defines the complexity of a SAT problem. Each complexity has an {@link #getIncludedComplexity included
     * complexity}, which contains the complexity itself as well as all complexities that are easier. To get all
     * included complexities recursively for a class use {@link #getIncludedComplexities}.
     */
    public enum Complexity {
        TRIVIAL {
            @Override
            public Complexity getIncludedComplexity() {
                return null;
            }
        },
        EASY {
            @Override
            public Complexity getIncludedComplexity() {
                return TRIVIAL;
            }
        },
        MEDIUM {
            @Override
            public Complexity getIncludedComplexity() {
                return EASY;
            }
        },
        HARD {
            @Override
            public Complexity getIncludedComplexity() {
                return MEDIUM;
            }
        },
        INSANE {
            @Override
            public Complexity getIncludedComplexity() {
                return HARD;
            }
        };

        public Set<Complexity> getIncludedComplexities() {
            Set<Complexity> complexityClasses = new HashSet<>();
            Complexity complexity = this;

            while (complexity != null) {
                complexityClasses.add(complexity);
                complexity = complexity.getIncludedComplexity();
            }

            return complexityClasses;
        }

        public abstract Complexity getIncludedComplexity();
    }

    @Override
    public String toString() {
        return "SatProblemJsonModel{" +
                "satisfiable=" + satisfiable +
                ", complexity=" + complexity +
                ", fileName='" + fileName + '\'' +
                '}';
    }
}
