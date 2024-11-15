import java.awt.event.KeyEvent;
import java.awt.Font;
import java.util.Random;

public class G2048 {

    // ---------------------------------------------------------------------- //
    // Fonctions utilitaires que vous pourrez utiliser pour implémenter les
    // questions des exercices

    static Random rand = new Random ();

    /* La fonction suivante renvoie un entier tiré au hasard entre min et max. */
    public static int randInt (int min, int max) {
        int randomNum = rand.nextInt((max - min) + 1) + min;
        return randomNum;
    }

    /* Procédure affichant le contenu d'un tableau d'entiers. */
    public static void printIntArray(int[] a) {
        for(int i = 0; i < a.length; i++) {
            System.out.print(a[i] + " ");
        }
        System.out.println();
    }

    /* Procédure inversant les éléments d'un tableau

       Exemple : entrée : {1,2,3,4,5}, sortie : {5,4,3,2,1}
     */
    public static void reverse(int[] t) {
        for(int i = 0; i < t.length / 2; i++) {
            int tmp = t[i];
            t[i] = t[t.length - i - 1];
            t[t.length - i - 1] = tmp;
        }
    }

    /* Procédure inversant les lignes et colonnes d'un tableau de tableaux carré
       (les lignes deviennent les colonnes, et les colonnes deviennent les
       lignes).

       Exemple :
       entrée :
         { {0, 1, 2},
           {3, 4, 5},
           {6, 7, 8} }

       sortie :
         { {0, 3, 6},
           {1, 4, 7},
           {2, 5, 8} }
    */
    public static void transpose(int[][] t) {
        for(int i = 0; i < t.length; i++) {
            for(int j = i+1; j < t.length; j++) {
                int tmp = t[i][j];
                t[i][j] = t[j][i];
                t[j][i] = tmp;
            }
        }
    }

    // ---------------------------------------------------------------------- //

    // La grille
    public static int[][] board;

    // Taille de la grille
    public static int boardSize = 4;

    // Coups :
    public static int LEFT = 0;
    public static int RIGHT = 1;
    public static int UP = 2;
    public static int DOWN = 3;

    // Exercice 1 :
    // Implémentez initBoard, isBoardWinning et newSquareValue ici.

    public static void initBoard() {
        board = new int[boardSize][boardSize];
        for (int i = 0; i < 2; i++) {
            int x = randInt(0, boardSize - 1);
            int y = randInt(0, boardSize - 1);
            while (board[y][x] != 0) {
                x = randInt(0, boardSize - 1);
                y = randInt(0, boardSize - 1);
            }
            board[y][x] = 2;
        }
    }

    public static boolean isBoardWinning() {
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (board[i][j] == 2048) {
                    StdDraw.clear();
                    StdDraw.text(0.5, 0.5, "2048 !");
                    StdDraw.show();
                    return true;
                }
            }
        }
        return false;
    }

    public static int newSquareValue() {
        return randInt(0, 9) == 0 ? 4 : 2; 
    }

    // ...

    // Exercice 2 :
    // Implémentez les fonctions demandées ici.

    public static int[] newEmptyRow() {
        return new int[boardSize];
    }

    public static int[] slideLeftNoMerge(int[] row) {
        int[] newRow = newEmptyRow();
        int c = 0;
        for (int i = 0; i < row.length; i++) {
            if (row[i] != 0) {
                newRow[c] = row[i];
                c++;
            }
        }
        return newRow;
    }

    public static int[] slideLeftAndMerge(int[] row) {
        int[] newRow = newEmptyRow();
        int c = 0;
        for (int i = 0; i < row.length; i++) {
            if (row[i] != 0) {
                if (c > 0 && newRow[c - 1] == row[i]) {
                    newRow[c - 1] += row[i];
                } else {
                    newRow[c] = row[i];
                    c++;
                }
            }
        }
        return newRow;
    }

    public static int[] slideLeft(int[] row) {
        for (int i = 0; i < boardSize; i++) {
            row = slideLeftAndMerge(row);
        }
        return row;
    }

    // ...

    // Exercice 3 :
    // Implémentez les fonctions demandées ici, et complétez slideBoard().

    public static void slideBoardLeft() {
        for (int i = 0; i < board.length; i++) {
            board[i] = slideLeft(board[i]);
        }
    }

    public static void slideBoardRight() {
        for (int i = 0; i < board.length; i++) {
            reverse(board[i]);
            board[i] = slideLeft(board[i]);
            reverse(board[i]);
        }
    }

    public static void slideBoardUp() {
        transpose(board);
        slideBoardLeft();
        transpose(board);
    }
    
    public static void slideBoardDown() {
        transpose(board);
        slideBoardRight();
        transpose(board);
    }

    public static void slideBoard(int direction) {
        if (direction == LEFT) {
            slideBoardLeft();
        } else if (direction == RIGHT) {
            slideBoardRight();
        } else  if (direction == UP) {
            slideBoardUp();
        } else if (direction == DOWN) {
            slideBoardDown();
        }
    }

    // Exercice 4 :
    // Complétez addSquare

    public static void addSquare(int value) {
        int x = randInt(0, boardSize - 1);
        int y = randInt(0, boardSize - 1);
        while (board[y][x] != 0) {
            x = randInt(0, boardSize - 1);
            y = randInt(0, boardSize - 1);
        }
        board[y][x] = value;
    }

    // Exercice 5 :
    // Implémentez ce qui est demandé ici.

    public static boolean isValidMove(int direction) {
        boolean diff = false;
        int[][] test = new int[boardSize][boardSize];
        for (int i = 0; i < test.length; i++) {
            for (int j = 0; j < test.length; j++) {
                test[i][j] = board[i][j];
            }
        }
        if (direction == LEFT) {
            for (int i = 0; i < test.length; i++) {
                test[i] = slideLeft(test[i]);
            }
        } else if (direction == RIGHT) {
            for (int i = 0; i < test.length; i++) {
                reverse(test[i]);
                test[i] = slideLeft(test[i]);
                reverse(test[i]);
            }
        } else if (direction == UP) {
            transpose(test);
            for (int i = 0; i < test.length; i++) {
                test[i] = slideLeft(test[i]);
            }
            transpose(test);
        } else if (direction == DOWN) {
            transpose(test);
            for (int i = 0; i < test.length; i++) {
                reverse(test[i]);
                test[i] = slideLeft(test[i]);
                reverse(test[i]);
            }
            transpose(test);
        } else {
            System.out.println(direction);
        }
        for (int i = 0; i < test.length; i++) {
            for (int j = 0; j < test.length; j++) {
                if (test[i][j] != board[i][j]) {
                    diff = true;
                }
            }
        }
        return diff;
    }

    public static void main(String[] args) {
        // Écrivez vos tests ici

        // ...

        // Exécute la boucle principale du jeu
        // (runGame est définie ci-dessous, mais il n'est pas nécessaire de
        // comprendre ce qu'elle fait en détail).
        
        runGame();
    }

    // ---------------------------------------------------------------------- //
    // Ci-dessous, fonctions qu'il n'est pas forcément nécessaire de comprendre.
    //

    // move exécute un tour de jeu : si le coup est valide, alors on décale les
    // cases de la grille, et on en ajoute une nouvelle
    public static void move(int direction) {
        if (isValidMove(direction)) {
            slideBoard(direction);
            addSquare(newSquareValue());
        }
    }

    // dessine la grille courante
    public static void drawBoard() {
        StdDraw.clear();
        for(int i = 0; i <= boardSize; i++) {
            StdDraw.line(0, 0.25 * i, 1, 0.25 * i);
            StdDraw.line(0.25 * i, 0, 0.25 * i, 1);
        }

        for(int i = 0; i < boardSize; i++) {
            for(int j = 0; j < boardSize; j++) {
                if (board[i][j] != 0) {
                    StdDraw.text(j * 0.25 + 0.125,
                                 1 - i * 0.25 - 0.125,
                                 Integer.toString(board[i][j]));
                }
            }
        }
        StdDraw.show();
    }

    // récupère la direction indiquée au clavier
    public static int getDirection() {
        int direction = -1;
        while(direction == -1) {
            if(StdDraw.isKeyPressed(KeyEvent.VK_LEFT)) {
                direction = LEFT;
            } else if(StdDraw.isKeyPressed(KeyEvent.VK_RIGHT)) {
                direction = RIGHT;
            } else if(StdDraw.isKeyPressed(KeyEvent.VK_DOWN)) {
                direction = DOWN;
            } else if(StdDraw.isKeyPressed(KeyEvent.VK_UP)) {
                direction = UP;
            }
            if(StdDraw.isKeyPressed(KeyEvent.VK_SPACE)) {
                StdDraw.pause(16);
                StdDraw.clearKeyPressed();
                runGame();
            }
            StdDraw.pause(16);
        }
        StdDraw.clearKeyPressed();
        return direction;
    }

    // boucle principale du jeu : fait jouer des coups jusqu'à ce que la partie
    // soit gagnée
    public static void runGame() {
        Font font = new Font("Sans Serif", Font.PLAIN, 40);
        StdDraw.setCanvasSize(500, 500);
        StdDraw.setPenRadius(0.01);
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setFont(font);
        StdDraw.enableDoubleBuffering();

        initBoard();
        drawBoard();

        while(!isBoardWinning()) {
            int direction = getDirection();
            move(direction);
            drawBoard();
        }
    }
}
