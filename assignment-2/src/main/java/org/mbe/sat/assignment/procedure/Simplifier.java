package org.mbe.sat.assignment.procedure;

import java.util.Iterator;
import java.util.Set;
import org.mbe.sat.api.procedure.ISimplifier;
import org.mbe.sat.core.model.Assignment;
import org.mbe.sat.core.model.formula.Atom;
import org.mbe.sat.core.model.formula.CnfFormula;
import org.mbe.sat.core.model.formula.Or;
import org.mbe.sat.core.model.formula.Tristate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
        
        /*Durch alle Klauseln der Formel iterieren*/
        while(clausesIterator.hasNext()) {
        	
        	Or<Atom> currentClause = clausesIterator.next(); //aktuelle Klausel
        	Set<Atom> literals = currentClause.getOperands(); //alle Literale der aktuellen Klausel
        	Iterator<Atom> literalIterator = literals.iterator();
        	boolean hasTrue = false; // zum ueberpruefen, ob in der Klausel ein Literal auf Top gesetzt wurde
        	
        	
        	/*Durch alle Literale der Klausel iterieren*/
        	while(literalIterator.hasNext()) {
        		
        		Atom currentliteral = literalIterator.next(); // aktuelles Literal
        		Tristate belegung = currentliteral.isSatisfiedWith(assignment); // Belegung des aktuellen Literals
        		
        		
        		/*Wenn die Belegung Top ist dann wird hasTrue auf true gesetzt (=>wenn hasTrue = true, dann Klausel entfernen)*/
        		 if(belegung == Tristate.TRUE) {
        			hasTrue = true;
        		}
        	}
        	/*entferne Klausel, wenn ein Top Literal enthalten ist*/
        	if(hasTrue) {
        		clausesIterator.remove();
        	}
        }
        return evaluatedFormula;   
    }
}