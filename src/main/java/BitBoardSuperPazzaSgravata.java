import java.util.BitSet;

public class BitBoardSuperPazzaSgravata {
    private BitSet boardB, boardW;
    private byte numPedineB, numPedineW;

    public BitBoardSuperPazzaSgravata() {
        numPedineB = numPedineW = 12;

        boardW = BitSet.valueOf(new long[] {4547941575690240L});
        boardB = BitSet.valueOf(new long[] {2273971846778880L});
    }

    public int isPossible(Mossa m, int r, int c) {
        //System.out.println();
        //System.out.println(board);
        //System.out.println();
        BitSet board = (BitSet) boardW.clone();
        board.or(boardB);
        int r1 = r + m.getY();
        int c1 = c + m.getX();

        if(r1 < 0 || r1 > 7 || c1 < 0 || c1 > 7) // limiti scacchiera
            return -1;
        if(board.get(r1*8+c1)) // pedina
            return -2;
        return 0;
    }

    public boolean muovi(Mossa m, int r, int c, boolean bianco) {
        if(isPossible(m, r, c) < 0)
            return false;

        BitSet board = (BitSet) boardW.clone();
        board.or(boardB);

        if (bianco)
            boardW.clear(r*8+c);
        else
            boardB.clear(r*8+c);

        int r1 = r + m.getY();
        int c1 = c + m.getX();
        int flag = isPossible(m, r1, c1);

        while(flag == 0) {
            c1 += m.getX();
            r1 += m.getY();
            flag = isPossible(m, r1, c1);
        }

        if(flag == -2)
            esplodi(r1, c1);
        else
            if (bianco)
                boardW.set(r1*8+c1);
            else
                boardB.set(r1*8+c1);

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
        if(boardB.get(i)) {
            boardB.clear(i);
            numPedineB--;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(int i = 63; i >= 0; i-=8) {
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
