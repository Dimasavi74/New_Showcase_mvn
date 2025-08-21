package org.Modules;

import org.ClientProperties;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class ConsoleInputModule implements InputModule {
    protected ClientProperties properties;

    public ConsoleInputModule(ClientProperties properties) {
        this.properties = properties;
    }

    public String inputLine(){
        Scanner input = new Scanner(System.in);
        String text = input.nextLine();
        return text;
    }

    public String inputLineWithDescription(String description) {
        Scanner input = new Scanner(System.in);
        System.out.print(description);
        String text = input.nextLine();
        return text;
    }

    public String inputMultipleLines(){
        Scanner input = new Scanner(System.in);
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
        Scanner input = new Scanner(System.in);
        System.out.print(description);
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

    public String readFile(String filePath) throws IOException {
            Path path = Paths.get(filePath);
            return Files.readString(path);
    }
}
