# Binary Sudoku

Binary Sudoku is a game similar to sudoku, but with slightly different rules.
In my implementation, the rules are as follows:
1. 3 of the same color tile in each row/column is not allowed.
2. Each row/column must have the same amount of red and blue tiles.
3. No two rows/columns are the same.
4. Tiles with lines through them are unchangable.

This game was inspired by a similar one created by Martin Kool (https://0hh1.com/). I implemented the simple graphics for this game through the Northeastern image library.

Some things I learned from this project:
1. Large algorithms and functions, if possible, should be split up in order to faciliate readability and testing. As I was working on the function which determines a winning condition for the game, I realized it would be easier to just make separate functions for each condition instead of one giant function that captures them all because if something goes wrong, then it would be tough to figure out where the error is coming from.
2. Before I start a project, I should think about what data structures would be best suited for what I want to represent. In my game, I realized that it was easy to traverse rows within the board using an ArrayList of ArrayList of Cells to represent the board, but it became tougher to traverse columns and compare them to each other. A custom object would've been more suited to handle the functions for my game.

![image](https://user-images.githubusercontent.com/44933949/117245877-6f89f300-adf0-11eb-83a1-eeca80bea249.png)

