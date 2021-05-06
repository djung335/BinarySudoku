import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;

import javalib.impworld.World;
import javalib.impworld.WorldScene;
import javalib.worldimages.*;
import tester.Tester;
import javax.swing.Timer;

// represents a coordinate on the board
class Pair {
  int x, y;

  Pair(int x, int y) {
    this.x = x;
    this.y = y;
  }

}

// represents a cell of the binary sudoku game
class Cell {
  int x, y;

  boolean permanent;
  int timesClicked;
  Color color;

  // represents the top, right, left, and bottom cells
  Cell top;
  Cell left;
  Cell bottom;
  Cell right;

  Cell(int x, int y, int timesClicked) {
    this.x = x;
    this.y = y;
    this.timesClicked = timesClicked;
    this.assignColor();
    this.permanent = false;

  }

  void assignColor() {
    if (this.timesClicked == 0) {
      this.color = Color.WHITE;
    }
    else if (this.timesClicked == 1) {
      this.color = Color.BLUE;
    }
    else {
      this.color = Color.RED;
    }
  }

  // overriding the cell class to show cell equality by their color
  public boolean equals(Object o) {
    if (o instanceof Cell) {
      if (this.timesClicked == ((Cell) o).timesClicked) {
        return true;
      }

    }

    return false;

  }

  // draws a cell
  WorldImage drawCell() {
    WorldImage border = new RectangleImage(40, 40, OutlineMode.OUTLINE, Color.BLACK);
    WorldImage notAllowed = new OverlayImage(new LineImage(new Posn(40, 40), Color.BLACK),
        new LineImage(new Posn(40, 40), Color.BLACK));

    WorldImage bordered = new OverlayImage(
        new RectangleImage(40, 40, OutlineMode.SOLID, this.color), border);

    if (this.permanent) {
      return new OverlayImage(notAllowed, bordered);

    }

    return bordered;

  }

}

// represents the main game class
class BinarySudoku extends World {
  ArrayList<ArrayList<Cell>> board = new ArrayList<ArrayList<Cell>>();
  // minimum should be 4 by 4
  int size;

  BinarySudoku(int size) {
    this.size = size;
    this.createBoard();
    this.removeRandomSpots();
  }

  // generates the initial board
  void createBoard() {
    ArrayList<ArrayList<Cell>> newBoard = new ArrayList<ArrayList<Cell>>();

    // generate a random

    for (int row = 0; row < this.size; row++) {
      ArrayList<Cell> newRow = new ArrayList<Cell>();
      for (int col = 0; col < this.size; col++) {

        newRow.add(new Cell(row, col, (int) Math.round(Math.random()) + 1));
      }
      newBoard.add(newRow);

    }

    this.board = newBoard;

    // creates a random solution
    while (!this.winCondition()) {
      this.createBoard();

    }

  }

  // removes random spots in a board that is already solved.
  void removeRandomSpots() {

    // create the coordinates
    ArrayList<Pair> coordinates = new ArrayList<Pair>();

    for (int row = 0; row < this.size; row++) {

      for (int col = 0; col < this.size; col++) {
        coordinates.add(new Pair(row, col));
      }

    }

    // shuffle the coordinates
    Collections.shuffle(coordinates);

    while (coordinates.size() > 6) {
      coordinates.remove(0);

    }

    for (Pair p : coordinates) {
      this.board.get(p.x).get(p.y).permanent = true;

    }

    for (int row = 0; row < this.size; row++) {

      for (int col = 0; col < this.size; col++) {
        if (!this.board.get(row).get(col).permanent) {
          this.board.get(row).get(col).timesClicked = 0;
          this.board.get(row).get(col).color = Color.WHITE;
        }
      }
    }
  }

  // we need to check a few things to see if the player has won the game:
  boolean winCondition() {
    // first, check the board across the rows to see if there are any three colors
    // in a row
    for (ArrayList<Cell> ac : this.board) {

      for (int i = 2; i < this.size; i++) {
        Color colorOne = ac.get(i - 2).color;
        Color colorTwo = ac.get(i - 1).color;
        Color colorThree = ac.get(i).color;
        if (colorOne.equals(colorTwo) && colorOne.equals(colorThree)) {
          return false;

        }

      }

    }

    // next, we want to check the board across the columns to see if there are any
    // three colors in a row

    for (int col = 0; col < this.size; col++) {

      for (int row = 2; row < this.size; row++) {
        Color colorOne = this.board.get(row - 2).get(col).color;
        Color colorTwo = this.board.get(row - 1).get(col).color;
        Color colorThree = this.board.get(row).get(col).color;
        if (colorOne.equals(colorTwo) && colorOne.equals(colorThree)) {
          return false;

        }
      }

    }

    // now, we want to check to see that in every row, the number of colors are
    // unbalanced.

    for (ArrayList<Cell> ac : this.board) {
      int counterBlue = 0;
      int counterRed = 0;

      for (int i = 0; i < this.size; i++) {
        if (ac.get(i).timesClicked == 1) {
          counterBlue++;
        }
        if (ac.get(i).timesClicked == 2) {
          counterRed++;
        }

      }

      if (counterBlue != this.size / 2 || counterRed != this.size / 2) {
        return false;

      }

    }

    // now we want to check to see that in every column, the number of colors are
    // unbalanced.
    for (int col = 0; col < this.size; col++) {
      int counterBlue = 0;
      int counterRed = 0;

      for (int row = 0; row < this.size; row++) {
        if (this.board.get(row).get(col).timesClicked == 1) {
          counterBlue++;
        }
        if (this.board.get(row).get(col).timesClicked == 2) {
          counterRed++;
        }

      }

      if (counterBlue != this.size / 2 || counterRed != this.size / 2) {
        return false;

      }

    }

    // TODO check to see if a row is the same as the next row and return false; if
    // they are the same

    for (int row1 = 0; row1 < this.size; row1++) {

      for (int row2 = row1 + 1; row2 < this.size; row2++) {
        if (this.board.get(row1).equals(this.board.get(row2))) {
          return false;
        }

      }

    }

    // checks the columns for column equality

    for (int col1 = 0; col1 < this.size; col1++) {
      for (int col2 = col1 + 1; col2 < this.size; col2++) {

        int counter = 0;
        for (int i = 0; i < this.size; i++) {

          if (this.board.get(i).get(col1).timesClicked == this.board.get(i)
              .get(col2).timesClicked) {
            counter++;

          }

        }
        if (counter == this.size) {
          return false;
        }

      }

    }

    return true;

  }

  WorldImage drawBoard() {
    WorldImage whole = new EmptyImage();
    for (int row = 0; row < this.size; row++) {
      WorldImage singleLine = new EmptyImage();
      for (int col = 0; col < this.size; col++) {
        singleLine = new BesideImage(singleLine, this.board.get(row).get(col).drawCell());
      }
      whole = new AboveImage(whole, singleLine);

    }
    return whole;
  }

  WorldImage drawYouWin() {
    return new TextImage("You Win!", 40, Color.BLACK);
  }

  public WorldScene makeScene() {
    int width = 500;
    int height = 500;
    WorldScene bkg = new WorldScene(width, height);
    WorldImage winScreen = new AboveImage(this.drawBoard(), this.drawYouWin());

    if (this.winCondition()) {
      bkg.placeImageXY(new AboveImage(winScreen), (int) winScreen.getWidth() / 2,
          (int) winScreen.getHeight() / 2 + (int) this.drawYouWin().getHeight() / 2);
      return bkg;
    }
    else {

      bkg.placeImageXY(this.drawBoard(), (int) this.drawBoard().getWidth() / 2,
          (int) this.drawBoard().getHeight() / 2);
      return bkg;

    }

  }

  public void onMouseClicked(Posn posn) {

    int xPos = posn.x / 40;
    int yPos = posn.y / 40;

    if (xPos >= 0 && yPos >= 0 && xPos < this.board.size() && yPos < this.board.size())

    {

      if (this.board.get(yPos).get(xPos).permanent) {

      }
      else {

        if (this.board.get(yPos).get(xPos).timesClicked == 2) {
          this.board.get(yPos).get(xPos).timesClicked = 0;
          this.board.get(yPos).get(xPos).assignColor();
        }
        else {

          this.board.get(yPos).get(xPos).timesClicked++;
          this.board.get(yPos).get(xPos).assignColor();

        }
      }
    }

  }

  // EFFECT: updates the board to the original board when the key "r" is pressed.
  public void onKeyEvent(String ke) {
    if (ke.equals("r")) {
      this.createBoard();
      this.removeRandomSpots();

    }
  }

}

class ExamplesBinarySudoku {

  BinarySudoku exampleGame = new BinarySudoku(4);

  void testBigBang(Tester t) {

    World w = this.exampleGame;
    double tickRate = 0.1;
    w.bigBang(500, 500, tickRate);
  }

}
