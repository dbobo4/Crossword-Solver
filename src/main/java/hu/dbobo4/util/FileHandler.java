package hu.dbobo4.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public class FileHandler {

    public static String[] getKeywords(String path) throws IOException {
        try (Stream<String> lines = Files.lines(Path.of(path))) {
            return lines.toArray(String[]::new);
        }
    }

    public static String[][] getWordArray(String path, String delimiter) {
        int[] tableParameters = getTableParameters(path, delimiter);
        int rowNumber = tableParameters[0];
        int columnNumber = tableParameters[1];

        String[][] resultTable = new String[rowNumber + 1][columnNumber];

        try (FileReader fileReader = new FileReader(path);
             BufferedReader bufferedReader = new BufferedReader(fileReader)) {

            int currentRowCnt = 0;
            String row = "";
            while ((row = bufferedReader.readLine()) != null) {
                String[] currentRow = row.split(delimiter);
                for (int i = 0; i < currentRow.length; i++) {
                    String letter = validLetter(currentRow[i]);
                    resultTable[currentRowCnt][i] = letter;
                }
                currentRowCnt++;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return resultTable;
    }

    private static String validLetter(String letter) {
        String validLetter = switch (letter.toLowerCase()) {
            case "á" -> "a";
            case "é" -> "e";
            case "í" -> "i";
            case "ó", "ö", "ő" -> "o";
            case "ú", "ü", "ű" -> "u";
            default -> letter.toUpperCase();
        };
        return validLetter.toUpperCase();
    }

    public static int[] getTableParameters(String path, String delimiter) {
        try (FileReader fileReader = new FileReader(path);
             BufferedReader bufferedReader = new BufferedReader(fileReader)) {

            int rowCnt = 0;

            String firstRow = bufferedReader.readLine();
            String[] splittedFirstRow = firstRow.split(delimiter);

            int columnCnt = splittedFirstRow.length;

            while ((bufferedReader.readLine()) != null) {
                rowCnt++;
            }

            int[] tableParameters = new int[2];
            tableParameters[0] = rowCnt;
            tableParameters[1] = columnCnt;

            return tableParameters;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return new int[2];
    }

}
