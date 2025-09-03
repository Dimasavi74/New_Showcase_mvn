package org.example.Modules;

import org.example.ClientProperties;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ConsoleInputModule implements InputModule {
    protected ClientProperties properties;
    protected ConsoleOutputModule outputModule;
    protected Scanner input;

    public ConsoleInputModule(ClientProperties properties, ConsoleOutputModule outputModule) {
        this.properties = properties;
        this.outputModule = outputModule;
        this.input = new Scanner(System.in);
    }

    public String inputLine(){
        String text = input.nextLine();
        return text;
    }

    public String inputLineWithDescription(String description) {
        outputModule.outputLine(description);
        String text = input.nextLine();
        return text;
    }

    public String inputMultipleLines(){
        String text = input.nextLine();
        List<String> textList = new ArrayList<>();
        textList.add(text);
        while (!text.contains(properties.getMultipleInputEndSymbol())) {
            text = input.nextLine();
            textList.add(text);
        }
        String line =  String.join(" \n", textList).replace(properties.getMultipleInputEndSymbol().charAt(0), ' ');
        return line;
    }

    public String inputMultipleLinesWithDescription(String description){
        System.out.print(description);
        return inputMultipleLines();
    }

    public String readFile(String filePath) throws IOException {
            Path path = Paths.get(filePath);
            return Files.readString(path);
    }
}
