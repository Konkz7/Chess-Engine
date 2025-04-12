public class Spot {
    private Piece piece;
    private int x;
    private int y;

    private int noAttacked = 0;

    public Spot(int x , int y, Piece piece){
        this.setPiece(piece);
        this.setX(x);
        this.setY(y);
    }

    public Piece getPiece()
    {
        return this.piece;
    }
    public void copySpot(Spot other){
        this.setX(other.getX());
        this.setY(other.getY());
    }
    public void setPiece(Piece p)
    {
        this.piece = p;
    }

    public int getX()
    {
        return this.x;
    }

    public void setX(int x)
    {
        this.x = x;
    }

    public int getY()
    {
        return this.y;
    }

    public void setY(int y)
    {
        this.y = y;
    }

    public int getNoAttacked() {
        return noAttacked;
    }

    public void addNoAttacked(int noAttacked) {
        this.noAttacked += noAttacked;
    }

    public void resetNoAttacked() {
        this.noAttacked = 0;
    }
}
