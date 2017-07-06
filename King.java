import java.awt.*;
import java.util.ArrayList;

/**
 * Created by smithanagar on 6/19/17.
 */
public class King extends Piece {

    public King(Color c, String s) {
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
        return false;
    }

    @Override
    protected void updateNextMoves(Board b) {
        ArrayList<String> nextMoves = new ArrayList<>();
        Point current = new Point(this.getPos());

        for (int i = -1; i <= 1; i += 2) {
            current.move(this.getPos().x, this.getPos().y + i);
            if (Board.withinBoard(Piece.pointToString(current))) {
                nextMoves.add(Piece.pointToString(current));
            }
        }
        for (int i = -1; i <= 1; i += 2) {
            current.move(this.getPos().x + i, this.getPos().y);
            if (Board.withinBoard(Piece.pointToString(current))) {
                nextMoves.add(Piece.pointToString(current));
            }
        }
        setNextMoves(nextMoves);
    }
}
