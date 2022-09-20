package com.example.myapplication;

import java.util.ArrayList;
import java.util.List;

public class Board {

    final int FREE = 0, P1 = 1, P2 = 2;
    public int BOARD_WIDTH;
    public int BOARD_HEIGHT;
    int[][] board_matrix;

    public Board(int rows, int columns) {
        this.BOARD_HEIGHT = rows;
        this.BOARD_WIDTH = columns;
        this.board_matrix = new int[BOARD_HEIGHT][BOARD_WIDTH];

        for (int i = 0; i < BOARD_HEIGHT; i++) {
            for (int j = 0; j < BOARD_WIDTH; j++) {
                board_matrix[i][j] = FREE;
            }
        }
    }

    public Board(int[][] board_matrix) {
        this.BOARD_HEIGHT = board_matrix.length;
        this.BOARD_WIDTH = board_matrix[0].length;
        this.board_matrix = board_matrix;
    }

    //copy constructor
    public Board(Board b) {
        this.BOARD_WIDTH = b.BOARD_WIDTH;
        this.BOARD_HEIGHT = b.BOARD_HEIGHT;
        this.board_matrix = new int[BOARD_HEIGHT][BOARD_WIDTH];

        for (int i = 0; i < BOARD_HEIGHT; i++) {
            board_matrix[i] = b.board_matrix[i].clone();
        }
    }

    //make move
    public int makeMove(int x, int y, int player) {
        if (isMoveAvailable(x, y)) {
            board_matrix[x][y] = player;
            return x;
        }
        return -1;
    }

    //check if field is not set
    boolean isMoveAvailable(int row, int col) {
        if (board_matrix[row][col] == FREE) return true;
        return false;
    }

    //to check for draw
    List<Integer[]> getAvailableMoves() {
        List<Integer[]> availableMoves = new ArrayList<>();
        for (int col = 0; col < BOARD_WIDTH; col++) {
            for (int row = 0; row < BOARD_HEIGHT; row++) {

                if (board_matrix[row][col] == FREE) {
                    Integer[] i = {row, col};

                    availableMoves.add(i);
                }
            }

        }
        return availableMoves;
    }

    public Boolean isEmpty() {
        for (int col = 0; col < BOARD_WIDTH; col++) {
            for (int row = 0; row < BOARD_HEIGHT; row++) {
                if (board_matrix[row][col] != 0)
                    return false;
            }
        }
        return true;
    }
}