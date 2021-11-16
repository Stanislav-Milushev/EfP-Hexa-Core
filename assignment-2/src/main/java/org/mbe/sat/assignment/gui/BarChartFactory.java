package org.mbe.sat.assignment.gui;

import org.mbe.sat.assignment.exceptions.EmptyChartInputException;

import javax.swing.*;
import java.util.Arrays;

public class BarChartFactory implements IBarChartFactory{

    private int chartCount;
    private boolean valuesProvided;
    private boolean namesProvided;
    private boolean categoriesProvided;
    private boolean chartTitleProvided;

    private double[] values;
    private String[] names;
    private String[] categories;
    private String chartTitle;

    private IBarChartGui chartGui;

    public BarChartFactory(){
        this.chartCount=0;
        this.valuesProvided =false;
        this.namesProvided=false;
        this.categoriesProvided=false;
        this.chartTitleProvided=false;
        this.values=null;
        this.names=null;
        this.categories=null;
        this.chartGui=null;
        this.chartTitle=null;
    }
    
    public void setChartTitle(String input) throws NullPointerException, EmptyChartInputException {
        if(input==null){
            throw new NullPointerException("title-input is null");
        }

        if(input.isEmpty() || input.equalsIgnoreCase(" ")){
            throw new EmptyChartInputException("title-input is empty");
        }

        this.chartTitle=input;
        this.chartTitleProvided=true;
    }

    public void setValues(double[] input) throws NullPointerException, EmptyChartInputException {
        if(input==null){
            throw new NullPointerException("value-input is null");
        }

        if(input.length==0){
            throw new EmptyChartInputException("value-input is empty");
        }

        this.values= Arrays.copyOf(input,input.length);
        this.valuesProvided=true;
    }

    public void setNames(String[] input) throws NullPointerException, EmptyChartInputException {
        if(input==null){
            throw new NullPointerException("names-input is null");
        }

        if(input.length==0){
            throw new EmptyChartInputException("names-input is empty");
        }

        this.names= Arrays.copyOf(input,input.length);
        this.namesProvided=true;
    }

    public void setCategories(String[] input) throws NullPointerException, EmptyChartInputException {
        if(input==null){
            throw new NullPointerException("categories-input is null");
        }

        if(input.length==0){
            throw new EmptyChartInputException("categories-input is empty");
        }

        this.categories= Arrays.copyOf(input,input.length);
        this.categoriesProvided=true;
    }

    @Override
    public void requestExport() {
        this.handleExportRequest();
    }

    @Override
    public void requestExit() {
        this.handleExitRequest();
    }

    @Override
    public void showGui() {
        this.handleShowRequest();
    }

    private void handleShowRequest(){
        if(this.namesProvided && this.categoriesProvided && this.valuesProvided && this.chartTitleProvided){

//            if(!(this.values.length==this.names.length)){
//                UserCommunication.errorDialog("INPUT-ERROR : ","The values- and names-input must have the same size!");
//                return;
//            }

        	if(!(this.values.length==this.categories.length)){
                UserCommunication.errorDialog("INPUT-ERROR : ","The values- and names-input must have the same size!");
                return;
            }
        	
            if(this.chartCount>0){
                UserCommunication.errorDialog("DUPLICATE CHART ERROR : ", "A chart is already running");
                return;
            }

            this.chartGui=new BarChartGui(this, this.chartTitle, this.values, this.names, this.categories);
            this.chartCount++;

        }else{
            StringBuilder builder=new StringBuilder();

            if(!this.chartTitleProvided){
                builder.append("chart title");
                builder.append("\n");
            }
            if(!this.namesProvided){
                builder.append("chart names");
                builder.append("\n");
            }
            if(!this.valuesProvided){
                builder.append("chart values");
                builder.append("\n");
            }
            if(!this.categoriesProvided){
                builder.append("chart categories");
                builder.append("\n");
            }
            UserCommunication.errorDialog("ERROR : MISSING PARAMETERS", "Currently missing parameters : "+builder.toString());
        }
    }

    private void handleExportRequest(){
        UserCommunication.confirmAction("UNDER CONSTRUCTION","UNDER CONSTRUCTION" );
    }

    private void handleExitRequest(){
        if(UserCommunication.confirmAction("CONFIRM EXIT", "Do you want to exit the bar-chart?")){
            closeGui();
        }
    }

    private void closeGui(){
        this.chartGui.close();
        this.chartGui=null;
        this.chartTitle=null;
        this.chartTitleProvided=false;
        this.values=null;
        this.valuesProvided=false;
        this.names=null;
        this.namesProvided=false;
        this.categories=null;
        this.categoriesProvided=false;
        this.chartCount--;
    }

    /*
    usage :
    create BarChartFactory-Object -> pass title/value/name/category parameters -> factory.showGui()
     */

    public static void main(String[] args){
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            // If Nimbus is not available, you can set the GUI to another look and feel.
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
            } catch (InstantiationException ex) {
                ex.printStackTrace();
            } catch (IllegalAccessException ex) {
                ex.printStackTrace();
            } catch (UnsupportedLookAndFeelException ex) {
                ex.printStackTrace();
            }
        }

        BarChartFactory factory=new BarChartFactory();

        String chartTitle="Benchmark-Comparison";
        //String[] names={"Trivial","Easy","Medium","No Pain. No Gain"};
        String[] names={"Runtime"};
        String[] categories={"Trivial","Easy","Medium","No Pain. No Gain"};
        double[] values={10.0, 42.0, 69.69, 420.0};

        try {
            factory.setChartTitle(chartTitle);
        } catch (EmptyChartInputException e) {
            e.printStackTrace();
        }

        try {
            factory.setNames(names);
        } catch (EmptyChartInputException e) {
            e.printStackTrace();
        }

        try {
            factory.setCategories(categories);
        } catch (EmptyChartInputException e) {
            e.printStackTrace();
        }

        try {
            factory.setValues(values);
        } catch (EmptyChartInputException e) {
            e.printStackTrace();
        }

        factory.showGui();
    }
}
