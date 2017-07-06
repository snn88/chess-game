import java.awt.*;
import java.util.ArrayList;

/**
 * Created by smithanagar on 6/19/17.
 */
public class Bishop extends Piece {

    public Bishop(Color c, String s) {
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
    public boolean pathBlocked(Point p, Piece[][] board) {
        Point current = new Point(this.getPos());
        int x_diff = (p.x > current.x) ? 1 : -1;
        int y_diff = (p.y > current.y) ? 1 : -1;

        current.move(current.x + x_diff, current.y + y_diff);
        while (current.x != p.x || current.y != p.y) {
            if (board[current.y][current.x] != null) {
                return true;
            }
            current.move(current.x + x_diff, current.y + y_diff);
        }
        return false;
    }

    @Override
    protected void updateNextMoves(Board b) {
        ArrayList<String> nextMoves = new ArrayList<>();
        Point current = new Point(this.getPos());
        for (int x_diff = -1; x_diff <= 1; x_diff += 2) {
            for (int y_diff = -1; y_diff <= 1; y_diff +=2) {
                current.move(this.getPos().x + x_diff, this.getPos().y + y_diff);
                while (Board.withinBoard(Piece.pointToString(current))) {
                    nextMoves.add(Piece.pointToString(current));
                    current.move(current.x + x_diff, current.y + y_diff);
                }
            }
        }
        setNextMoves(nextMoves);
    }
}
