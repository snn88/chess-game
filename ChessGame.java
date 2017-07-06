import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Created by smithanagar on 6/19/17.
 */
public class ChessGame {

    // TODO when piece reaches the end, convert piece
    // TODO castleing
    // TODO fix checkmate check - null pointer exception in movePiece (98) in updateAllPiecesMoves
    // TODO race conditions?

    private boolean gameOver;
    private HashMap<String, Point> boardPieces;
    private Board board;
    private static ChessGame cg;
    private Color turn;
    private static final String tryAgain = "Enter a valid command.\n";
    private static final String instructions = "Enter moves in the format 'k c3' followed by " +
            "enter. Uppercase pieces are black and lowercase are white. White moves first. Type " +
            "'help' to see these instructions gain. Type 'reset' to reset the game. Type 'quit' " +
            "to exit the game.\n";
    private static final String quit = "Hope you had a savvy time playing this gorgeous game of " +
            "chess!.\n";
    private static final String check = "Your king is in check. You must protect your king.\n";
    private static final String endGame0 = "Check mate! Game over. ";
    private static final String endGame1 = " wins.\n";

    public static void main(String[] args) {
        cg = new ChessGame();
        cg.initGame();
        cg.playGame();
    }

    private void playGame() {
        Scanner s = new Scanner(System.in);
        String input;
        while (cg.gameNotOver()) {
            cg.getBoard().display();
            if (checkCheck()) {
                System.out.print(check);
            }
            input = s.nextLine();
            switch (input) {
                case "help":
                    System.out.print(instructions);
                    break;
                case "reset":
                    cg.resetGame();
                    System.out.print("\n");
                    break;
                case "quit":
                    gameOver = true;
                    System.out.print(quit);
                    break;
                default:
                    if (!cg.makeMove(input)) {
                        System.out.print(tryAgain);
                    } else {
                        turn = (turn == Color.WHITE) ? Color.BLACK : Color.WHITE;
                    }
                    break;
            }
            if (checkCheckMate()) {
                gameOver = true;
                System.out.print(endGame0 + (turn == Color.WHITE ? Color.BLACK : Color.WHITE) +
                        endGame1);
            }
        }
    }

    private boolean checkCheck() {
        return kingExposed(null);
    }

    private boolean checkCheckMate() {
        // if not in a check, no need to execute computationally expensive check for a checkmate
        if (!checkCheck()) {
            return false;
        }
        // for each piece of the current color in check, if any piece has an unblocked next move
        // that will stop the king from being exposed, return false
        for (String s : boardPieces.keySet()) {
            if ((turn == Color.WHITE && Character.isLowerCase(s.charAt(0))) ||
                    turn == Color.BLACK && Character.isUpperCase(s.charAt(0))) {
                Piece p = board.getBoard()[boardPieces.get(s).y][boardPieces.get(s).x];
                int original_x = boardPieces.get(s).x;
                int original_y = boardPieces.get(s).y;
                for (String pos : p.getNextMoves()) {
                    Point newPos = Piece.stringToPoint(pos);
                    if (!p.blocked(newPos, board.getBoard())) {
                        Piece killedPiece = movePiece(newPos.x, newPos.y, p);
                        if (!kingExposed(killedPiece)) {
                            movePiece(original_x, original_y, p);
                            if (killedPiece != null) {
                                board.getBoard()[newPos.y][newPos.x] = killedPiece;
                            }
                            return false;
                        } else {
                            movePiece(original_x, original_y, p);
                            if (killedPiece != null) {
                                board.getBoard()[newPos.y][newPos.x] = killedPiece;
                            }
                        }
                    }
                }
            }
        }
        // so now, there is no way to block the king with another piece, so check if the king
        // itself can move out of the way or kill the threat
        String kingString = (turn == Color.WHITE) ? "k1" : "K1";
        Piece king = board.getBoard()[boardPieces.get(kingString).y][boardPieces.get(kingString).x];
        int original_x = boardPieces.get(kingString).x;
        int original_y = boardPieces.get(kingString).y;
        // for each possible unblocked next move for the king, if moving to any such position will
        // stop the king from being exposed, return false
        for (String kingPos : king.getNextMoves()) {
            Point newPos = Piece.stringToPoint(kingPos);
            if (!king.blocked(newPos, board.getBoard())) {
                Piece killedPiece = movePiece(newPos.x, newPos.y, king);
                if (!kingExposed(killedPiece)) {
                    movePiece(original_x, original_y, king);
                    if (killedPiece != null) {
                        board.getBoard()[newPos.y][newPos.x] = killedPiece;
                    }
                    return false;
                } else {
                    movePiece(original_x, original_y, king);
                    if (killedPiece != null) {
                        board.getBoard()[newPos.y][newPos.x] = killedPiece;
                    }
                }
            }
        }
        // nothing can block a threat from the king and the king cannot move to save itself, so
        // checkmate
        return true;
    }

    private boolean makeMove(String input) {
        // is the input in the correct format? does it try to move the correct color piece? does
        // it move it within the board?
        if (!checkInput(input)) {
            return false;
        }

        String piece = input.substring(0, 2);
        String pos = input.substring(3);
        Point pointPos = Piece.stringToPoint(pos);
//        int x = pos.charAt(0) - 'a';
//        int y = pos.charAt(1) - '1';
        Piece p = board.getBoard()[boardPieces.get(piece).y][boardPieces.get(piece).x];

        // does the position fit the piece's method of moving?
        if (!p.getNextMoves().contains(pos)) {
            return false;
        }

//        Point pointPos = new Point(pos.charAt(0) - 'a', pos.charAt(1) - '1');
        // are there any pieces in the path to the position? is the position already filled with
        // the same color piece?
        if (p.blocked(pointPos, board.getBoard())) {
            return false;
        }

        int original_x = boardPieces.get(piece).x;
        int original_y = boardPieces.get(piece).y;


        // temporarily move the piece.
        Piece killedPiece = movePiece(pointPos.x, pointPos.y, p);

        // does this protect the king? if not, move it back
        if (kingExposed(killedPiece)) {
            movePiece(original_x, original_y, p);
            if (killedPiece != null) {
                board.getBoard()[pointPos.y][pointPos.x] = killedPiece;
            }
            return false;
        }

        if (killedPiece != null) {
            boardPieces.remove(killedPiece.getString());
        }
        return true;
    }

    private void updateAllPiecesMoves() {
        for (String s : boardPieces.keySet()) {
            board.getBoard()[boardPieces.get(s).y][boardPieces.get(s).x].updateNextMoves(board);
        }
    }

    // TODO check this
    private boolean kingExposed(Piece killedPiece) {
        String king = (turn == Color.WHITE) ? "k1" : "K1";
        for (String s : boardPieces.keySet()) {
            if ((turn == Color.WHITE && Character.isUpperCase(s.charAt(0))) ||
                    turn == Color.BLACK && Character.isLowerCase(s.charAt(0))) {
                Piece p = board.getBoard()[boardPieces.get(s).y][boardPieces.get(s).x];
                if (p != killedPiece) {
                    if (p.getNextMoves().contains(Piece.pointToString(boardPieces.get(king))) &&
                            !p.blocked(boardPieces.get(king), board.getBoard())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private Piece movePiece(int x, int y, Piece p) {
        Piece killedPiece = board.movePiece(p.getPos(), x, y);
        p.setPos(x, y);
        p.updateNextMoves(board);
        boardPieces.put(p.getString(), p.getPos());
        updateAllPiecesMoves();
        return killedPiece;
    }

    private boolean checkInput(String input) {
        return (input.length() == 5 && input.charAt(2) == ' ' &&
                Board.withinBoard(input.substring(3)) &&
                (boardPieces.keySet().contains(input.substring(0, 2)) &&
                        ((Character.isLowerCase(input.charAt(0)) && turn == Color.WHITE) ||
                                (Character.isUpperCase(input.charAt(0)) && turn == Color.BLACK))));
    }

    public void initGame() {
        gameOver = false;
        boardPieces = new HashMap<>();
        board = new Board();
        this.addAllBoardPieces();
        board.initBoard(boardPieces);
        turn = Color.WHITE;
        System.out.print(instructions);
    }

    private void addAllBoardPieces() {
        boardPieces.put("k1", new Point('e' - 'a', '1' - '1'));
        boardPieces.put("q1", new Point('d' - 'a', '1' - '1'));
        boardPieces.put("b1", new Point('c' - 'a', '1' - '1'));
        boardPieces.put("b2", new Point('f' - 'a', '1' - '1'));
        boardPieces.put("n1", new Point('b' - 'a', '1' - '1'));
        boardPieces.put("n2", new Point('g' - 'a', '1' - '1'));
        boardPieces.put("r1", new Point('a' - 'a', '1' - '1'));
        boardPieces.put("r2", new Point('h' - 'a', '1' - '1'));
        for (int i = 0; i < 8; i++) {
            boardPieces.put("p" + i, new Point(i, '2' - '1'));
        }

        boardPieces.put("K1", new Point('e' - 'a', '8' - '1'));
        boardPieces.put("Q1", new Point('d' - 'a', '8' - '1'));
        boardPieces.put("B1", new Point('c' - 'a', '8' - '1'));
        boardPieces.put("B2", new Point('f' - 'a', '8' - '1'));
        boardPieces.put("N1", new Point('b' - 'a', '8' - '1'));
        boardPieces.put("N2", new Point('g' - 'a', '8' - '1'));
        boardPieces.put("R1", new Point('a' - 'a', '8' - '1'));
        boardPieces.put("R2", new Point('h' - 'a', '8' - '1'));
        for (int i = 0; i < 8; i++) {
            boardPieces.put("P" + i, new Point(i, '7' - '1'));
        }
    }

    public boolean gameNotOver() {
        return !gameOver;
    }

    public void resetGame() {
        initGame();
    }

    public Board getBoard() {
        return board;
    }
}
