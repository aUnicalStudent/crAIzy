import java.util.BitSet;
import java.util.LinkedList;
import java.util.List;

public class MinMax {
    static class Nodo {
        private boolean max; // 1 noi, 0 avversario
        private float euristica;
        //Scacchiera
        private BitSet boardB = new BitSet(64);
        private BitSet boardW = new BitSet(64);
        private List<Nodo> figli = new LinkedList<>();

        public Nodo() {}

        public Nodo(boolean max, float euristica, BitSet boardB, BitSet boardW, List<Nodo> figli) {
            this.max = max;
            this.euristica = euristica;
            this.boardB = boardB;
            this.boardW = boardW;
            this.figli = figli;
        }
    }

    public float minmax(Nodo init_node, int depth) {
        //genera_figli(init_node);
        if(depth == 0 || init_node.figli.size() == 0)
            return init_node.euristica;

        // MASSIMIZZATORE
        if(init_node.max){
            float maxE = Float.MIN_VALUE;
            float eva = 0;
            for(Nodo n : init_node.figli)
                eva = minmax(n, depth--);
            return Math.max(eva, maxE);
        }
        // MINIMIZZATORE
        else {
            float minE = Float.MAX_VALUE;
            float eva = 0;
            for(Nodo n : init_node.figli)
                eva = minmax(n, depth--);
            return Math.min(eva, minE);
        }
    }

    public static void main(String[] args) {
        BitBoardSuperPazzaSgravata board = new BitBoardSuperPazzaSgravata();
        System.out.println(board);

        board.muovi(Mossa.NE, 5, 3, true);
        System.out.println("\nDopo la mossa NE, 5, 3, true");
        System.out.println(board);

        board.muovi(Mossa.S, 1, 4, false);
        System.out.println("\nDopo la mossa S, 4, 1, false");
        System.out.println(board);

        /* TEST ESPLOSIONE
            board.muovi(Mossa.NW, 4, 2, true);
            System.out.println("\nDopo la mossa NW, 4, 2, true");
            System.out.println(board);
        */

        // CASO LIMITE -> BORDO
        board.muovi(Mossa.E, 3, 1, true);
        System.out.println("\nDopo la mossa E, 3, 1, true");
        System.out.println(board);

        board.muovi(Mossa.W, 3, 6, false);
        System.out.println("\nDopo la mossa W, 3, 6, false");
        System.out.println(board);

        board.muovi(Mossa.W, 4, 6, true);
        System.out.println("\nDopo la mossa W, 4, 6, true");
        System.out.println(board);

        board.muovi(Mossa.E, 5, 2, false);
        System.out.println("\nDopo la mossa E, 5, 2, false");
        System.out.println(board);

        board.muovi(Mossa.N, 3, 0, true);
        System.out.println("\nDopo la mossa N, 3, 0, true");
        System.out.println(board);
    }
}
