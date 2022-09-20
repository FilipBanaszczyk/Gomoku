package com.example.myapplication;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Evaluation {

    //map of patterns and their value
    public HashMap<String, Integer> patterns = new HashMap<String, Integer>() {{
        put("22222", 100000);
        put("022220", 10000);
        put("02220", 1000);
        put("02200", 100);
        put("00220", 100);

        put("122220", 1000);
        put("102220", 900);
        put("122202", 900);
        put("122200", 100);
        put("122020", 90);
        put("122000", 10);
        put("102200", 10);
        put("100220", 10);

        put("022221", 1000);
        put("022201", 900);
        put("202221", 900);
        put("002221", 100);
        put("020221", 90);
        put("000221", 10);
        put("002201", 10);
        put("022001", 10);

        put("20202", 100);

        //defence
        put("11111", -110000);  //extra
        put("011110", -11000);  //extra
        put("01110", -1100);   //extra
        put("01100", -100);
        put("00110", -100);

        put("211110", -1100);  //extra
        put("201110", -900);
        put("211101", -900);
        put("211100", -100);
        put("211010", -90);
        put("211000", -10);
        put("201100", -10);
        put("200110", -10);

        put("011112", -1000);
        put("011102", -900);
        put("101112", -900);
        put("001112", -100);
        put("010112", -90);
        put("000112", -10);
        put("001102", -10);
        put("011002", -10);

        put("10101", -100);

        put("211112",10000);  //to boost low bot
        put("122221",-10000); //to boost low bot
    }};

    //returns board score for computer
    public int getScore(Board board) {
        int score = 0;

        //list of all lines on board(horizontal, vartical, diagonal)
        ArrayList<String> all_lines = new ArrayList<>();

        //rows
        for (int r = 0; r != board.board_matrix.length; r++) {
            String line = "";
            for (int c = 0; c != board.board_matrix[0].length; c++) {
                line = line.concat(String.valueOf(board.board_matrix[r][c]));
            }
            all_lines.add(line);
        }

        //columns
        for (int c = 0; c != board.board_matrix[0].length; c++) {
            String line = "";
            for (int r = 0; r != board.board_matrix.length; r++) {
                line = line.concat(String.valueOf(board.board_matrix[r][c]));
            }
            all_lines.add(line);
        }

        //diagonal right
        for (int k = 0; k <= board.board_matrix.length + board.board_matrix[0].length - 2; k++) {
            String line = "";
            for (int j = 0; j <= k; j++) {
                int i = k - j;
                if (i < board.board_matrix.length && j < board.board_matrix[0].length) {
                    line = line.concat(String.valueOf(board.board_matrix[i][j]));
                }
            }
            if (line.length() >= 5) all_lines.add(line);
        }

        //diagonal left
        for (int k = 0; k <= board.board_matrix.length + board.board_matrix[0].length - 2; k++) {
            String line = "";
            for (int j = 0; j <= k; j++) {
                int i = k - j;
                if (i < board.board_matrix.length && j < board.board_matrix[0].length) {
                    line = line.concat(String.valueOf(board.board_matrix[board.board_matrix.length - i - 1][j]));
                }
            }
            if (line.length() >= 5) all_lines.add(line);
        }

        //check all lines for patterns add and points if found
        for (String line : all_lines) {
            for (Map.Entry<String, Integer> entry : patterns.entrySet())
                if (line.contains(entry.getKey())) score += entry.getValue();
        }

        return score;
    }


}