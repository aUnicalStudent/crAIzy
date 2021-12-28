import java.util.BitSet;

public class BitBoardSuperPazzaSgravata {
    private BitSet board;
    private byte numPedine;

    public BitBoardSuperPazzaSgravata(boolean bianco) {
        numPedine = 12;

        if(bianco)
            board = BitSet.valueOf(new long[] {4547941575690240L});
        else
            board = BitSet.valueOf(new long[] {2273971846778880L});
    }

    public boolean isPossible(Mossa m, int r, int c) {
        int r1 = r + m.getY();
        int c1 = c + m.getX();

        if(r1 < 0 || r1 > 7 || c1 < 0 || c1 > 7) // limiti scacchiera
            return false;
        return false;
    }

    public byte getNumPedine() {
        return numPedine;
    }

    public void stampa() {
        for(int i = 0; i < 64; i+=8) {
            for(int j = i; j < i + 8; j++)
                System.out.print((board.get(j) == true ? 1 : 0) + " ");
            System.out.println();
        }
    }
}
