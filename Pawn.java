import java.awt.*;
import java.util.ArrayList;

/**
 * Created by smithanagar on 6/19/17.
 */
public class Pawn extends Piece {

    public Pawn(Color c, String s) {
        super(c, s);
    }

    @Override
    public boolean isValidPos(String loc) {
        return false;
    }

    @Override
    public ArrayList<String> getNextMoves() {
        return super.getNextMoves();
    }

    @Override
    public boolean targetBlocked(Point p, Piece[][] board) {
        return (board[p.y][p.x] != null && board[p.y][p.x].getColor() == this.getColor() ||
                (p.x == this.getPos().x && board[p.y][p.x] != null));
    }

    @Override
    public boolean pathBlocked(Point p, Piece[][] board) {
        Point current = new Point(this.getPos());
        if (Math.abs(p.y - current.y) == 2) {
            int y_diff = (p.y > current.y) ? 1 : -1;
            return board[current.y + y_diff][current.x] != null;
        }
        return false;
    }

    @Override
    protected void updateNextMoves(Board b) {
        ArrayList<String> nextMoves = new ArrayList<>();
        int y_diff = (this.getColor() == Color.WHITE) ? 1 : -1;
        Point current = new Point(this.getPos());
        current.move(current.x, current.y + y_diff);
        if (Board.withinBoard(Piece.pointToString(current))) {
            nextMoves.add(Piece.pointToString(current));
        }
        if ((y_diff == 1 && this.getPos().y == 1) || (y_diff == -1 && this.getPos().y == 6)) {
            current.move(current.x, current.y + y_diff);
            if (Board.withinBoard(Piece.pointToString(current))) {
                nextMoves.add(Piece.pointToString(current));
            }
            current.move(current.x, current.y - y_diff);
        }
        for (int i = -1; i <= 2; i += 3) {
            current.move(current.x + i, current.y);
            if (Board.withinBoard(Piece.pointToString(current)) &&
                    b.getBoard()[current.y][current.x] != null &&
                    b.getBoard()[current.y][current.x].getColor() != this.getColor()) {
                nextMoves.add(Piece.pointToString(current));
            }
        }
        setNextMoves(nextMoves);
    }
}
