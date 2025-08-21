package org.Modules;

import java.util.HashMap;
import java.util.Map;

public class ParsingModule {
    public Map<String, String> parseVariableNamesSyntaxLine(String line) {
//      Парсинг строки формата /commandName commandArg1{data} commandArg2{data1, data2...} ... ;

        HashMap<String, String> parsedMap = new HashMap<>();

        if (line.contains("/")) {
            line = line.substring(line.indexOf('/'));
        }

        // Получение команды, может вернуть ""
        StringBuilder commandName = new StringBuilder();
        if (line.charAt(0) == '/') {
            for (int i = 1; i < line.length(); i++) {
                char c = line.charAt(i);
                if ((c != ' ') & (c != ';')) {
                    commandName.append(c);
                } else {
                    line = line.substring(i);
                    break;
                }
            }
        }
        parsedMap.put("command", String.valueOf(commandName));

        // Полуение аргументов
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '{') {
                String[] all_keys = line.substring(0, i).split(" ");
                String keyName = all_keys[all_keys.length - 1].strip();
                StringBuilder value = new StringBuilder();
                i++;
                c = line.charAt(i);
                while ((c != '}') & (c != ';')) {
                    value.append(c);
                    i++;
                    c = line.charAt(i);
                }
                parsedMap.put(keyName, String.valueOf(value));
                line = line.substring(i + 1);
                i = 0;
            }
        }

        return parsedMap;
    }
}
