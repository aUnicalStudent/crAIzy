import java.util.Objects;

public class Mossa {
    private Direzione dir;
    private int r, c;

    public Mossa(Direzione dir, int r, int c) {
        this.dir = dir;
        this.r = r;
        this.c = c;
    }

    public Mossa(Direzione dir, char c, int i) {
        this.dir = dir;
        this.r = 72-c;
        this.c = 8-i;
    }

    public Direzione getDir() {
        return dir;
    }

    public int getR() {
        return r;
    }

    public int getC() {
        return c;
    }

    public String getCell() {
        return "" + (char) (72 -r) + (8-c);
    }

    public Mossa setDir(Direzione dir) {
        this.dir = dir;
        return this;
    }

    public Mossa setR(int r) {
        this.r = r;
        return this;
    }

    public Mossa setC(int c) {
        this.c = c;
        return this;
    }

    @Override
    public String toString() {
        return "Mossa{ dir= " + getDir() + ", cell= " + getCell() + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Mossa mossa = (Mossa) o;
        return r == mossa.r && c == mossa.c && dir == mossa.dir;
    }

    @Override
    public int hashCode() {
        return Objects.hash(dir, r, c);
    }
}
