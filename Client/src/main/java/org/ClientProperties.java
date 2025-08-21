package org;

import java.util.Arrays;

public class ClientProperties {
    private final String[] INPUT_MODES = {"singleLine", "multipleLines"};
    private final String[] INPUT_SYNTAXES = {"variableNames"};
    protected String multipleInputEndSymbol = ";";
    protected String inputMode = "singleLine";
    protected String inputSyntax = "variableNames";
    public Boolean isRunning = false;

    public void setMultipleInputEndSymbol(String multipleInputEndSymbol){
        this.multipleInputEndSymbol = multipleInputEndSymbol;
    }

    public String getMultipleInputEndSymbol(){
        return multipleInputEndSymbol;
    }

    public String[] getInputModes(){
        return INPUT_MODES;
    }

    public String getInputMode(){
        return inputMode;
    }

    public void setInputMode(String inputMode){
        if (Arrays.asList(INPUT_MODES).contains(inputMode)){
            this.inputMode = inputMode;
        } else {
            throw new RuntimeException("Undefined input mode. Check the INPUT_MODES array and choose the right one. (method getInputModes())");
        }
    }

    public String[] getInputSyntaxes(){
        return INPUT_SYNTAXES;
    }

    public String getInputSyntax(){
        return inputSyntax;
    }

    public void setINPUT_SYNTAXES(String inputSyntax){
        if (Arrays.asList(INPUT_SYNTAXES).contains(inputSyntax)){
            this.inputSyntax = inputSyntax;
        } else {
            throw new RuntimeException("Undefined input syntax. Check the INPUT_SYNTAXES array and choose the right one. (method getInputSyntaxes())");
        }
    }

}
