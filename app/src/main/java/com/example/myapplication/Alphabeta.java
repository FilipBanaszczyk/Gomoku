package com.example.myapplication;

import android.util.Log;

import java.util.*;

public class Alphabeta {

    Evaluation evaluation;

    public Alphabeta() {
        this.evaluation = new Evaluation();
    }

    //returns list of potential moves (surrounding of already filled fields)
    //list is in order where fileds in the midle of matrix goes first
    public TreeMap<Double, int[]> getPossibleMoves(Board board) {

        //filled cells and their surrounding
        HashSet<String> filed_and_surr = new HashSet<>();
        for (int i = 0; i < board.BOARD_HEIGHT; i++) {
            for (int j = 0; j < board.BOARD_WIDTH; j++) {
                if (board.board_matrix[i][j] != 0) {

                    filed_and_surr.add("" + i + "," + j);
                    if (i != 0) {
                        filed_and_surr.add("" + (i - 1) + "," + j);
                        if (j != 0) filed_and_surr.add("" + (i - 1) + "," + (j - 1));
                        if (j != board.BOARD_WIDTH - 1) filed_and_surr.add("" + (i - 1) + "," + (j + 1));
                    }
                    if (i != board.BOARD_HEIGHT - 1) {
                        filed_and_surr.add("" + (i + 1) + "," + j);
                        if (j != 0) filed_and_surr.add("" + (i + 1) + "," + (j - 1));
                        if (j != board.BOARD_WIDTH - 1) filed_and_surr.add("" + (i + 1) + "," + (j + 1));
                    }

                    if (j != 0) filed_and_surr.add("" + (i) + "," + (j - 1));
                    if (j != board.BOARD_WIDTH - 1) filed_and_surr.add("" + (i) + "," + (j + 1));

                }
            }
        }

        //map to keep cells in order
        TreeMap<Double, int[]> result = new TreeMap<>();
        double half_height = (double) board.BOARD_HEIGHT / 2;
        double half_width = (double) board.BOARD_WIDTH / 2;

        Random r = new Random();

        //adding only empty cells in correct order
        for (String roccol : filed_and_surr) {
            String[] split = roccol.split(",");
            int x = Integer.parseInt(split[0]);
            int y = Integer.parseInt(split[1]);
            if (board.board_matrix[x][y] == 0) {
                //  System.out.print(split[0]+","+split[1]+"  ");
                double key = (Math.abs(x - half_height) + Math.abs(y - half_width)) + r.nextDouble();
                result.put(key, new int[]{x, y});
            }
        }

        return result;
    }

    //returns object with the best move using alpha-beta algorithm
    public Object[] Search(int depth, Board board, boolean max, int player, int alpha, int beta) {
        int oponent = (player == 1 ? 2 : 1);

        if (depth == 0) {
            //Log.d("EVALUTAION: ", Integer.toString(evaluation.getScore(board)));
            Object[] x = {evaluation.getScore(board), null};
            return x;
        }

        if (board.isEmpty()) return new Object[] {0, new int[] {board.BOARD_WIDTH/2, board.BOARD_HEIGHT/2}};

        TreeMap<Double, int[]> all_poss_moves = getPossibleMoves(board);

        if (all_poss_moves.size() == 0) {
            Object[] x = {0, null};
            return x;
        }

        Object[] best_move = new Object[2];
        if (max) {
            best_move[0] = Integer.MIN_VALUE;
            for (int[] i : all_poss_moves.values()) {
                Board dummy_board = new Board(board);
                dummy_board.makeMove(i[0], i[1], player);
                Object[] temp_move = Search(depth - 1, dummy_board, !max, player, alpha, beta);

                //update alpha
                if ((int) (temp_move[0]) > alpha) {
                    alpha = (int) (temp_move[0]);
                }
                //puring beta
                if ((int) (temp_move[0]) >= beta) {
                    return temp_move;
                }

                if ((int) temp_move[0] > (int) best_move[0]) {
                    best_move = temp_move;
                    best_move[1] = i;
                }
            }
        } else {
            best_move[0] = Integer.MAX_VALUE;
            for (int[] i : all_poss_moves.values()) {
                Board dummy_board = new Board(board);
                dummy_board.makeMove(i[0], i[1], oponent);
                Object[] temp_move = Search(depth - 1, dummy_board, !max, player, alpha, beta);

                //update best
                if (((int) temp_move[0]) < beta) {
                    beta = (int) (temp_move[0]);
                }
                //puring alpha
                if ((int) (temp_move[0]) <= alpha) {
                    return temp_move;
                }

                if ((int) temp_move[0] < (int) best_move[0]) {
                    best_move = temp_move;
                    best_move[1] = i;
                }
            }
        }
        return best_move;
    }
}