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
        long asd = 4547941575690240L;
        BitBoardSuperPazzaSgravata boardW = new BitBoardSuperPazzaSgravata(true);
        boardW.stampa();
        System.out.println();
        BitBoardSuperPazzaSgravata boardB = new BitBoardSuperPazzaSgravata(false);
        boardB.stampa();
        System.out.println(boardB.isPossible(Mossa.NE, -1, 2));
        // System.out.println(boardB.getNumPedine());
    }
}
