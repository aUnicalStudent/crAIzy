import java.util.Arrays;
import java.util.BitSet;
import java.util.LinkedList;
import java.util.List;

public class BitBoard implements Cloneable {
    private BitSet boardB, boardW;
    private byte numPedineB, numPedineW;

    public BitBoard() {
        this.numPedineB = this.numPedineW = 12;

        this.boardW = BitSet.valueOf(new long[] {4547941575690240L});
        this.boardB = BitSet.valueOf(new long[] {2273971846778880L});
    }

    public BitBoard(long white, long black) {
        this.numPedineB = this.numPedineW = 2;

        this.boardW = BitSet.valueOf(new long[] {white});
        this.boardB = BitSet.valueOf(new long[] {black});
    }

    public int isPossible(Mossa m) {
        BitSet board = (BitSet) boardW.clone();
        board.or(boardB);
        int r1 = m.getR() + m.getDir().getY();
        int c1 = m.getC() + m.getDir().getX();

        if(r1 < 0 || r1 > 7 || c1 < 0 || c1 > 7) // limiti scacchiera
            return -1;
        if(board.get(r1*8+c1)) // pedina
            return -2;
        return 0;
    }

    public boolean muovi(Mossa m, boolean bianco) {
        BitSet board = (BitSet) boardW.clone();
        board.or(boardB);

        BitSet b;
        if (bianco) {
            b = boardW;
        }
        else {
            b = boardB;
        }
        b.clear(m.getR()*8+m.getC());

        int r1 = m.getR() + m.getDir().getY();
        int c1 = m.getC() + m.getDir().getX();
        Mossa tmp = new Mossa(m.getDir(), r1, c1);
        int flag = isPossible(tmp);

        while(flag == 0) {
            r1 += tmp.getDir().getY();
            tmp.setR(r1);

            c1 += tmp.getDir().getX();
            tmp.setC(c1);

            flag = isPossible(tmp);
        }

        if(flag == -2) {
            if(bianco)
                numPedineW--;
            else
                numPedineB--;
            esplodi(r1, c1);
        }
        else
            b.set(r1*8+c1);

        return true;
    }

    private void esplodi(int r, int c) {
        if(r - 1 >= 0){
            if(c - 1 >= 0)
                elimina((r-1)*8+(c-1));
            elimina((r-1)*8+c);
            if(c + 1 <= 7)
                elimina((r-1)*8+(c+1));
        }
        if(c - 1 >= 0)
            elimina(r*8+(c-1));
        if(c + 1 <= 7)
            elimina(r*8+(c+1));
        if(r + 1 <= 7){
            if(c - 1 >= 0)
                elimina((r+1)*8+(c-1));
            elimina((r+1)*8+c);
            if(c + 1 <= 7)
                elimina((r+1)*8+(c+1));
        }
    }

    private void elimina(int i) {
        if(boardW.get(i)) {
            boardW.clear(i);
            numPedineW--;
        }
        else if(boardB.get(i)) {
            boardB.clear(i);
            numPedineB--;
        }
    }

    public byte diff() {
        return (byte) (numPedineW - numPedineB);
    }

    public int somma(){
        return numPedineW+numPedineB;
    }
    public List<Mossa> mossePossibili(boolean bianco) {
        List<Mossa> m = new LinkedList<>();

        BitSet b;
        if (bianco)
            b = boardW;
        else
            b = boardB;

        b.stream().forEach(i -> {
            for(Direzione d : Direzione.values()) {
                Mossa m1 = new Mossa(d, i/8, i%8);
                if(isPossible(m1) == 0)
                    m.add(m1);
            }
        });

        return m;
    }

    public Object clone() {
        try {
            BitBoard obj = (BitBoard) super.clone();

            obj.boardW = (BitSet) this.boardW.clone();
            obj.boardB = (BitSet) this.boardB.clone();

            return obj;
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public byte getNumPedineB() {
        return numPedineB;
    }

    public byte getNumPedineW() {
        return numPedineW;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("#bianche " + numPedineW + "    #nere " + numPedineB + "\n");
        sb.append("CHECKPOINT BIANCO " + Arrays.toString(boardW.toLongArray()) + "     CHECKPOINT NERO " + Arrays.toString(boardB.toLongArray()) + "\n");
        sb.append("  1 2 3 4 5 6 7 8\n");
        int a = 65;
        for(int i = 63; i >= 0; i-=8) {
            sb.append((char)a + " ");
            a++;
            for(int j = i; j > i - 8; j--) {
                if(boardW.get(j))
                    sb.append("W ");
                else if(boardB.get(j))
                    sb.append("B ");
                else
                    sb.append("- ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
