public class Mossa {
    private Direzione dir;
    private int r, c;

    public Mossa(Direzione dir, int r, int c) {
        this.dir = dir;
        this.r = r;
        this.c = c;
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

    public char getRM(){
        return (char) (72 -r);
    }

    public int getCM(){ return 8-c; }

    public void setDir(Direzione dir) {
        this.dir = dir;
    }

    public void setR(int r) {
        this.r = r;
    }

    public void setC(int c) {
        this.c = c;
    }

    @Override
    public String toString() {
        return "Mossa{" +
                "dir=" + dir +
                ", r=" + r +
                ", c=" + c +
                '}';
    }
}
