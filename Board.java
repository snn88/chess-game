import java.awt.*;
import java.util.HashMap;

/**
 * Created by smithanagar on 6/19/17.
 */
public class Board {
    private static int WIDTH = 8;
    private static int LENGTH = 8;
    private Piece[][] board;

    public int getWIDTH() {
        return WIDTH;
    }

    public int getLENGTH() {
        return LENGTH;
    }

    public Board() {
        board = new Piece[LENGTH][WIDTH];
    }

    public void initBoard(HashMap<String, Point> boardPieces) {
        Piece p = null;
        for (String s : boardPieces.keySet()) {
            switch (s.charAt(0)) {
                case 'k':
                    p = new King(Color.WHITE, s);
                    break;
                case 'q':
                    p = new Queen(Color.WHITE, s);
                    break;
                case 'b':
                    p = new Bishop(Color.WHITE, s);
                    break;
                case 'n':
                    p = new Knight(Color.WHITE, s);
                    break;
                case 'r':
                    p = new Rook(Color.WHITE, s);
                    break;
                case 'p':
                    p = new Pawn(Color.WHITE, s);
                    break;
                case 'K':
                    p = new King(Color.BLACK, s);
                    break;
                case 'Q':
                    p = new Queen(Color.BLACK, s);
                    break;
                case 'B':
                    p = new Bishop(Color.BLACK, s);
                    break;
                case 'N':
                    p = new Knight(Color.BLACK, s);
                    break;
                case 'R':
                    p = new Rook(Color.BLACK, s);
                    break;
                case 'P':
                    p = new Pawn(Color.BLACK, s);
                    break;
                default:
                    System.err.println("Error in initializing board - invalid piece string.");
                    break;
            }
            board[boardPieces.get(s).y][boardPieces.get(s).x] = p;
            p.setPos(boardPieces.get(s).x, boardPieces.get(s).y);
            p.updateNextMoves(this);
        }
    }

    public static boolean withinBoard(String loc) {
        loc = loc.toLowerCase();
        return (loc.charAt(0) >= 'a') && (loc.charAt(0) < 'a' + WIDTH) &&
                (loc.charAt(1) >= '1') && (loc.charAt(1) < '1' + LENGTH);
    }

    public void display() {
        char x_axis = 'a';
        System.out.print("  | ");
        for (int i = 0; i < LENGTH; i++) {
            System.out.print((char)(x_axis + i) + "  | ");
        }
        System.out.print("\n-------------------------------------------\n");
        for (int i = 0; i < LENGTH; i++) {
            System.out.print(i + 1 + " | ");
            for (int j = 0; j < WIDTH; j++) {
                if (board[i][j] == null) {
                    System.out.print("   | ");
                } else {
                    System.out.print(board[i][j].getString());
                    System.out.print(" | ");
                }
            }
            System.out.print("\n-------------------------------------------\n");
        }
    }

    public Piece[][] getBoard() {
        return board;
    }

    public Piece movePiece(Point pos, int x, int y) {
        Piece killedPiece = null;
        if (board[y][x] != null) {
            killedPiece = board[y][x];
        }
        board[y][x] = board[pos.y][pos.x];
        board[pos.y][pos.x] = null;
        return killedPiece;
    }
}
