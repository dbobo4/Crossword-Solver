import hu.dbobo4.util.FileHandler;

import java.util.*;

// The rules are:
//              -we can use the same letter for a solution
//              -the first solution from top to bottom and from left to right is a valid solution
//              -we're trying to find the FIRST valid solution
//              -a mirrored version of a word is a valid solution
//              -the solution line (horizontal, vertical, diagonal) can not be broken

public class Main {

    private static final String DELIMITER = ",";
    private static final String PATH_OF_SOURCE_FILE = "D:\\Masterfield\\Projects\\Excercieses\\Crossword\\src\\main\\java\\source\\crossword_example.txt";
    private static final String[][] taskTable = FileHandler.getWordArray(PATH_OF_SOURCE_FILE, DELIMITER);
    private static final List<String> FOUND_WORDS = new ArrayList<>();
    private static final List<String> NOT_FOUND_WORDS = new ArrayList<>();
    private static final HashMap<String, List<int[]>> FOUND_COORDINATES = new HashMap<>();

    public static void main(String[] args) {

        String[] keyWords = {
                "etolaj",
                "buzafinomliszt",
                "kristalycukor",
                "so",
                "eleszto",
                "viz",
                "csirkecombfile",
                "fuszerpaprika",
                "oregano",
                "feketebors",
                "fokhagymapor",
                "bazsalikom",
                "kakukkfu",
                "rozmaring",
                "komeny",
                "joghurt",
                "citromle",
                "kapor",
                "paradicsom",
                "kigyouborka",
                "jegsalata",
                "lilakaposzta",
                "lilahagyma",
                "fokhagyma"
        };

        List<int[]> finalResult = new ArrayList<>();
        for (String word : keyWords) {
            List<int[]> result = getCoordinates(word);
            FOUND_COORDINATES.put(word, result);
            finalResult.addAll(result);
        }

        printResult(finalResult);


    }

    private static void printResult(List<int[]> result) {
        System.out.println("INITIAL TABLE");
        System.out.println("----------------------------------------------------------------");
        for (int i = 0; i < taskTable.length; i++) {
            for (int j = 0; j < taskTable[0].length; j++) {
                System.out.print(taskTable[i][j] + "\t");
            }
            System.out.println();
            System.out.println();
        }

        System.out.println("SOLUTION TABLE");
        System.out.println("----------------------------------------------------------------");
        System.out.println();
        printResultTable(result);
        System.out.println();

        System.out.println("Found words: " + FOUND_WORDS.size());
        printData(FOUND_WORDS);
        System.out.println();
        if (NOT_FOUND_WORDS.isEmpty()) {
            System.out.println("All words have been found!");
            System.out.println();
        } else {
            System.out.println("Not found words: " + NOT_FOUND_WORDS.size());
            printData(NOT_FOUND_WORDS);
            System.out.println();
        }

        System.out.println("Found coordinates: ");
        for (Map.Entry<String, List<int[]>> entry : FOUND_COORDINATES.entrySet()) {
            String key = entry.getKey();
            List<int[]> value = entry.getValue();
            int[][] coordinates = new int[value.size()][value.get(0).length];
            for (int i = 0; i < coordinates.length; i++) {
                coordinates[i] = value.get(i);
            }

            System.out.println(key + ": " + Arrays.deepToString(coordinates));
        }


    }

    private static <T> void printData(List<T> data) {
        for (T dataPiece : data) {
            System.out.println(dataPiece);
        }
    }

    private static void printResultTable(List<int[]> result) {
        String[][] resultTable = new String[taskTable.length][taskTable[0].length];

        for (String[] strings : resultTable) {
            Arrays.fill(strings, " ");
        }

        for (int[] currentCoordinate : result) {
            resultTable[currentCoordinate[0]][currentCoordinate[1]] = taskTable[currentCoordinate[0]][currentCoordinate[1]];
        }

        for (int i = 0; i < resultTable.length; i++) {
            for (int j = 0; j < resultTable[0].length; j++) {
                System.out.print(resultTable[i][j] + "\t");
            }
            System.out.println();
            System.out.println();
        }
    }

    private static List<int[]> getCoordinates(String word) {
        List<int[]> coordinateList = new ArrayList<>();

        char firstLetter = word.charAt(0);
        for (int i = 0; i < taskTable.length; i++) {
            for (int j = 0; j < taskTable[i].length; j++) {

                if (taskTable[i][j].equalsIgnoreCase(String.valueOf(firstLetter))) {
                    if (coordinateList.size() < word.length()) {
                        coordinateList.clear();
                    }
                    coordinateList.add(new int[]{i, j});
                    //it will find just ONE and the FIRST solution!
                    if (getMatchedNeighbourCoordinates(Orientation.NONE, Orientation.NONE, i, j, i, j, word, 1, String.valueOf(word.charAt(0)), coordinateList)) {
                        FOUND_WORDS.add(word);
                        return coordinateList;
                    }
                }
            }
        }
        NOT_FOUND_WORDS.add(word);

        return new ArrayList<>();
    }

    private static Orientation decideOrientation(int currentRow, int currentColumn, int preRow, int preColumn) {
        if (currentColumn == preColumn) {
            return Orientation.VERTICAL;
        } else if (currentRow == preRow) {
            return Orientation.HORIZONTAL;
        } else return Orientation.DIAGONAL;
    }

    private static boolean getMatchedNeighbourCoordinates(Orientation mainOrientation, Orientation currentOrientation, int baseRow, int baseColumn, int x, int y, String word, int nextLetterPos, String currentWord, List<int[]> coordinateList) {
        if (currentWord.equalsIgnoreCase(word)) {
            return true;
        }

        for (int row = x - 1; row <= x + 1; row++) {
            for (int column = y - 1; column <= y + 1; column++) {
                if (row < 0 || row > (taskTable.length - 1) || column < 0 || column > (taskTable[0].length - 1)) {
                } else {
                    if (!((row == x) && (column == y))) {
                        if (taskTable[row][column].equalsIgnoreCase(String.valueOf(word.charAt(nextLetterPos)))) {
                            if (nextLetterPos == 1) {
                                mainOrientation = decideOrientation(row, column, baseRow, baseColumn);
                            }
                            if ((nextLetterPos > 1)) {
                                currentOrientation = decideOrientation(row, column, baseRow, baseColumn);
                                if ((currentOrientation == mainOrientation) && isValidPosition(row, column, baseRow, baseColumn, coordinateList)) {
                                    coordinateList.add(new int[]{row, column});
                                    currentWord += word.charAt(nextLetterPos);
                                    if (getMatchedNeighbourCoordinates(mainOrientation, currentOrientation, baseRow, baseColumn, row, column, word, ++nextLetterPos, currentWord, coordinateList)) {
                                        return true;
                                    } else {
                                        currentWord = currentWord.substring(0, currentWord.length() - 1);
                                        --nextLetterPos;
                                        coordinateList.remove(coordinateList.size() - 1);
                                    }
                                }
                            } else {
                                coordinateList.add(new int[]{row, column});
                                currentWord += word.charAt(nextLetterPos);
                                if (getMatchedNeighbourCoordinates(mainOrientation, currentOrientation, baseRow, baseColumn, row, column, word, ++nextLetterPos, currentWord, coordinateList)) {
                                    return true;
                                } else {
                                    currentWord = currentWord.substring(0, currentWord.length() - 1);
                                    --nextLetterPos;
                                    coordinateList.remove(coordinateList.size() - 1);
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    private static boolean isValidPosition(int currentRow, int currentColumn, int baseRow, int baseColumn, List<int[]> coordinateList) {
        return ((baseRow == currentRow || baseColumn == currentColumn || (Math.abs(currentRow - baseRow) == Math.abs(currentColumn - baseColumn)))) && !isNewCoordinate(currentRow, currentColumn, coordinateList);
    }

    private static boolean isNewCoordinate(int row, int column, List<int[]> coordinateList) {
        for (int[] coordinates : coordinateList) {
            if ((coordinates[0] == row) && (coordinates[1] == column)) {
                return true;
            }
        }
        return false;
    }
}


