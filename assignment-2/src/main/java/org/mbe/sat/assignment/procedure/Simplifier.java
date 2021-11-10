package org.mbe.sat.assignment.procedure;

import java.util.Iterator;
import java.util.Set;

import org.mbe.sat.api.procedure.ISimplifier;
import org.mbe.sat.core.model.Assignment;
import org.mbe.sat.core.model.formula.Atom;
import org.mbe.sat.core.model.formula.CnfFormula;
import org.mbe.sat.core.model.formula.Literal;
import org.mbe.sat.core.model.formula.Literal.Polarity;
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

        CnfFormula evaluatedFormula = cnfFormula.evaluate(assignment);
        // TODO: Simplify evaluated formula
        
        Set<Or<Atom>> allClauses = evaluatedFormula.getClauses(); // alle klauseln holen
        Iterator<Or<Atom>> clauseIterator = allClauses.iterator(); // iterator erstellen
        
        while(clauseIterator.hasNext()) { // für jede Klausel...
        	
        	Or<Atom> currentClause = clauseIterator.next(); // die aktuelle klausel
        	Set<Literal> literals = currentClause.getLiterals(); // die literale der aktuellen klausel
        	Iterator<Literal> literalIterator = literals.iterator();
        	
        	while(literalIterator.hasNext()) { // für jedes literal in der aktuellen klausel
        		
        		Literal currentLiteral = literalIterator.next(); // aktuelles literal in der aktuelle klausel
        		Polarity polarity = currentLiteral.getPolarity(); // polarität des literals
        		Tristate belegung = currentLiteral.isSatisfiedWith(assignment); // belegung des literals
        		
        		switch(belegung) { // Fallunterscheidung der Belegung => FALSE, TRUE oder UNDEFINED
        			case FALSE: {
        				if(polarity.isPositive()) { // Polaritaet ist positiv, Belegung ist false
        					currentClause.getLiterals().remove(currentLiteral); // entfernen des akutellen literals aus der liste
        				}else { // Polaritaet ist negativ, Belegung ist false
        					allClauses.remove(currentClause); // entfernen der klausel
        				}
        				break;
        			}
        				
        			case TRUE: {
        				if(polarity.isPositive()) { // Polaritaet ist positiv, Belegung ist true
        					allClauses.remove(currentClause); // entfernen der klausel
        				}else { // Polaritaet ist negativ, Belegung ist true
        					currentClause.getLiterals().remove(currentLiteral); // entfernen der klausel
        				}
        				break;
        			}
        				
        			case UNDEFINED: { // belegung ist undefined
        				break; // nichts tun
        			}	
        		}
        		
        		if(currentClause.getLiterals().isEmpty()) {
        			return null; // unerfuellbar weil die aktuelle klausel leer ist
        		}
        		
        		        		
        	}
        	
        }
        
        return evaluatedFormula;
    }
}
