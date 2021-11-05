package org.mbe.sat.core.problem;

import org.mbe.sat.core.model.formula.*;

import java.util.Arrays;
import java.util.HashSet;

public class F {

    // Literals
    public static final Literal A = literal("A", true);
    public static final Literal B = literal("B", true);
    public static final Literal C = literal("C", true);
    public static final Literal D = literal("D", true);
    public static final Literal E = literal("E", true);
    public static final Literal F = literal("F", true);
    public static final Literal G = literal("G", true);

    public static final Literal NA = literal("A", false);
    public static final Literal NB = literal("B", false);
    public static final Literal NC = literal("C", false);
    public static final Literal ND = literal("D", false);

    // Variables
    public static final Variable VA = A.getVariable();
    public static final Variable VB = B.getVariable();
    public static final Variable VC = C.getVariable();
    public static final Variable VD = D.getVariable();

    //Constants
    public static final True TRUE = True.create();
    public static final False FALSE = False.create();

    @SafeVarargs
    public static CnfFormula cnf(Or<Atom>... ors) {

        HashSet<Or<Atom>> ors1 = new HashSet<>(Arrays.asList(ors));

        return new CnfFormula(ors1);
    }

    public static CnfFormula emptyCnf() {
        return new CnfFormula(new HashSet<>());
    }

    public static <T extends Formula> Or<T> or(T left, T right) {
        return new Or<>(left, right);
    }

    @SafeVarargs
    public static <T extends Formula> Or<T> or(T... operands) {
        return new Or<>(new HashSet<>(Arrays.asList(operands)));
    }

    public static <T extends Formula> And<Formula> and(T left, T right) {
        return new And<>(left, right);
    }

    @SafeVarargs
    public static <T extends Formula> And<T> and(T... operands) {
        return new And<>(new HashSet<>(Arrays.asList(operands)));
    }

    public static Literal literal(String variableName, boolean polarity) {
        return literal(Variable.fromName(variableName), polarity);
    }

    public static Literal literal(Variable variable) {
        return literal(variable, true);
    }

    public static Literal literal(Variable variable, boolean polarity) {
        return new Literal(variable, Literal.Polarity.of(polarity));
    }

    public static Variable variable(String name) {
        return new Variable(name);
    }
}
