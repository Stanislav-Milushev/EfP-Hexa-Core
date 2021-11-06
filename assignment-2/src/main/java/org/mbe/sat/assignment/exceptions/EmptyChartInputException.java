package org.mbe.sat.assignment.exceptions;

public class EmptyChartInputException extends Exception{

    public EmptyChartInputException(){
        super();
    }

    public EmptyChartInputException(String errorMessage){
        super(errorMessage);
    }
}
