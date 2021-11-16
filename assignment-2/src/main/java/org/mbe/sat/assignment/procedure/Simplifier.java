package org.mbe.sat.assignment.procedure;

import org.mbe.sat.api.procedure.ISimplifier;
import org.mbe.sat.core.model.Assignment;
import org.mbe.sat.core.model.formula.Atom;
import org.mbe.sat.core.model.formula.CnfFormula;
import org.mbe.sat.core.model.formula.Or;
import org.mbe.sat.core.model.formula.Tristate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class Simplifier implements ISimplifier<CnfFormula, Assignment> {

    /**
     * Static logger instance for this class.
     */
    private static final Logger LOG = LoggerFactory.getLogger(Simplifier.class);

    public CnfFormula simplify(CnfFormula cnfFormula, Assignment assignment) {
        LOG.debug("Simplifying formula '{}' with assignment '{}'", cnfFormula, assignment);
    	
        //CNF-Formel wird evaluiert
    	CnfFormula evaluatedFormula = cnfFormula.evaluate(assignment);
        Set<Or<Atom>> clauses = evaluatedFormula.getClauses();	//hole Klauseln der evaluierten CNF
        Iterator<Or<Atom>> clausesIterator = clauses.iterator();
		Set<Or<Atom>> simplifiedClauses = new HashSet(); // hier kommen alle Klauseln rein, die weiterhin in der Formel vorhanden sein sollen
		System.out.println("evaluated: " + evaluatedFormula);

        /*Durch alle Klauseln der Formel iterieren*/
        while(clausesIterator.hasNext()) {
        	
        	Or<Atom> currentClause = clausesIterator.next(); //aktuelle Klausel
        	Set<Atom> literals = currentClause.getOperands(); //alle Literale der aktuellen Klausel
        	Iterator<Atom> literalIterator = literals.iterator();
			Set<Atom> simplifiedLiterals = new HashSet(); // hier kommen alle Literale rein, die weiterhin in der Formel vorhanden sein sollen

			//System.out.println("====== Next Clause ======");

        	boolean hasTrue = false; // zum ueberpruefen, ob in der Klausel ein Literal auf Top gesetzt wurde
        	
        	
        	/*Durch alle Literale der Klausel iterieren*/
        	while(literalIterator.hasNext()) {
        		
        		Atom currentliteral = literalIterator.next(); // aktuelles Literal
        		Tristate belegung = currentliteral.isSatisfiedWith(assignment); // Belegung des aktuellen Literals


				//System.out.println("Literal State: " + belegung);
        		
        		
        		/*Wenn die Belegung Top ist, dann wird hasTrue auf true gesetzt (=> wenn hasTrue = true, dann wird die Klausel nicht mehr in der Formel stehen)*/
        		 if(belegung == Tristate.TRUE) {
        			hasTrue = true;
					//System.out.println("Literal is TRUE");
        		}else if(belegung == Tristate.UNDEFINED){ // wenn die Belegung des literals UNDEFINED ist, wird es in der Formel bleiben. FALSE fliegt IMMER raus!
					 simplifiedLiterals.add(currentliteral);
					 //System.out.println("Literal is UNDEFINED => literal soll weiter in der Formel sein");
				 }
        	}
        	// wenn eine Klausel nicht TRUE beinhaltet, soll diese weiterhin in der Formel stehen
			if(!hasTrue) {
				currentClause.setOperands(simplifiedLiterals); // setzt die Literale der Klausel auf die, die wir behalten wollen
				simplifiedClauses.add(currentClause); // addet die Klausel zu einer Liste an Klauseln, die wir behalten wollen
        	}
        }

		evaluatedFormula.setClauses(simplifiedClauses); // wir überschreiben die Klauseln mit denen die noch vorhanden sein sollen

		System.out.println("simplified: " + evaluatedFormula);
        return evaluatedFormula;   
    }
}