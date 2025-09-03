package org.example;

import java.util.Arrays;

public class ClientProperties {
    protected String multipleInputEndSymbol = ";";
    protected InputModes inputMode = InputModes.SINGLE_LINE;
    protected InputSyntaxes inputSyntax = InputSyntaxes.VARIABLE_NAMES;
    public Boolean isRunning = false;

    public void setMultipleInputEndSymbol(String multipleInputEndSymbol){
        this.multipleInputEndSymbol = multipleInputEndSymbol;
    }

    public String getMultipleInputEndSymbol(){
        return multipleInputEndSymbol;
    }


    public InputModes getInputMode(){
        return inputMode;
    }

    public void setInputMode(InputModes inputMode){
        this.inputMode = inputMode;
    }

    public InputSyntaxes getInputSyntax(){
        return inputSyntax;
    }

    public void setInputSyntax(InputSyntaxes inputSyntax){
        this.inputSyntax = inputSyntax;
    }

}
