import java.awt.*;
import java.util.ArrayList;

public abstract class Piece {

    private Color color;
    private ArrayList<String> nextMoves;
    private String string;
    private Point pos;

    public Piece(Color c, String s) {
        this.color = c;
        this.string = s;
        this.pos = new Point();
    }

    // loc is "a8" or "c3"
    public boolean isValidMove(String loc) {
        return Board.withinBoard(loc) && this.isValidPos(loc);
    }

    // char that reps the piece, like 'q' for queen
    public String getString() {
        return string;
    }

    // loc is "a8" or "c3"
    public abstract boolean isValidPos(String loc);

    public Color getColor() {
        return color;
    }

    public static String pointToString(Point p) {
        return String.valueOf((char)('a' + p.x)) + String.valueOf((char)('1' + p.y));
    }

    public ArrayList<String> getNextMoves() {
        return nextMoves;
    }

    public boolean blocked(Point pos, Piece[][] board) {
        return targetBlocked(pos, board) || pathBlocked(pos, board);
    }

    public boolean targetBlocked(Point p, Piece[][] board) {
        return (board[p.y][p.x] != null && board[p.y][p.x].getColor() == this.getColor());
    }

    public abstract boolean pathBlocked(Point pos, Piece[][] board);

    public Point getPos() {
        return pos;
    }

    public void setPos(int x, int y) {
        this.pos.move(x, y);
    }

    protected abstract void updateNextMoves(Board b);

    public void setNextMoves(ArrayList<String> nextMoves) {
        this.nextMoves = nextMoves;
    }

    public static Point stringToPoint(String s) {
        return new Point(s.charAt(0) - 'a', s.charAt(1) - '1');
    }
}
