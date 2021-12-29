import java.util.BitSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class MinMax {
    private static boolean bianco;

    static class Nodo {
        private boolean raffaeleMASTER; // true bianco, false nero
        private float euristica;
        //Scacchiera
        private BitBoardSuperPazzaSgravata bb;
        private List<Nodo> figli = new LinkedList<>();
        private Mossa pre;

        public Nodo() {}

        public Nodo(boolean col, BitBoardSuperPazzaSgravata b, Mossa pre) {
            this.raffaeleMASTER = col;
            this.bb = (BitBoardSuperPazzaSgravata) b.clone();
            this.pre = pre;
        }

        public float calcolaEuristica() {
            if(raffaeleMASTER)
                return bb.diff();
            return -bb.diff();
        }

        @Override
        public String toString() {
            return "Nodo{ max = " + raffaeleMASTER + "\n\n" + bb + "\n}";
        }
    }

    public static float minmax(Nodo nodoCorrente, int depth) {
        generaFigli(nodoCorrente);
        if(depth == 0 || nodoCorrente.figli.size() == 0)
            nodoCorrente.euristica = nodoCorrente.calcolaEuristica();
        // MASSIMIZZATORE
        else if(!(nodoCorrente.raffaeleMASTER ^ bianco)){
            float eva = 0;
            for(Nodo n : nodoCorrente.figli)
                eva = minmax(n, depth--);
            nodoCorrente.euristica = Math.max(eva, Float.MIN_VALUE);
        }
        // MINIMIZZATORE
        else {
            float eva = 0;
            for(Nodo n : nodoCorrente.figli)
                eva = minmax(n, depth--);
            nodoCorrente.euristica = Math.min(eva, Float.MAX_VALUE);
        }

        return nodoCorrente.euristica;
    }

    private static void generaFigli(Nodo nodo) {
        List<Mossa> m = nodo.bb.mossePossibili(nodo.raffaeleMASTER);
        Nodo n;
        for(Mossa m1 : m) {
            //System.out.println(m1);
            n = new Nodo(!nodo.raffaeleMASTER, nodo.bb, m1);
            n.bb.muovi(m1, nodo.raffaeleMASTER);
            nodo.figli.add(n);
            //System.out.println(n);
        }
    }

    private static Mossa scegli(Nodo nodo) {
        float val = minmax(nodo, 1);

        for(Nodo n : nodo.figli)
            if(n.euristica == val)
                return n.pre;

        return null;
    }

    public static void main(String[] args) {
        BitBoardSuperPazzaSgravata board = new BitBoardSuperPazzaSgravata();
        //System.out.println(board);
        bianco = true;
        Nodo nn = new Nodo(true, board, null);
        //generaFigli(nn);
        //System.out.println("---------------------------------------------");
        //System.out.println(nn.figli);
        Mossa m = scegli(nn);
        Scanner sc = new Scanner(System.in);
        /*while(true) {
            Mossa m = scegli(nn);
            nn.bb.muovi(m, bianco);
            System.out.println(nn);
            System.out.print("> ");

            Direzione dir = Direzione.valueOf(sc.next());
            int x = sc.nextInt();
            int y = sc.nextInt();
            m = new Mossa(dir, x, y);
            nn = new Nodo(!bianco, nn.bb, null);
            nn.bb.muovi(m, !bianco);
        }*/

        /*
        // INIT
        board.muovi(new Mossa(Direzione.NE, 5, 3), true);
        System.out.println("\nDopo la mossa NE, 5, 3, true");
        System.out.println(board);

        board.muovi(new Mossa(Direzione.E, 4, 1), false);
        System.out.println("\nDopo la mossa E, 4, 1, false");
        System.out.println(board);

        // TEST ESPLOSIONE
        board.muovi(new Mossa(Direzione.NW, 4, 2), true);
        System.out.println("\nDopo la mossa NW, 4, 2, true");
        System.out.println(board);


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

        // CLONE
        BitBoardSuperPazzaSgravata clonata = (BitBoardSuperPazzaSgravata) board.clone();

        clonata.muovi(Mossa.NE, 5, 3, true);
        System.out.println("\nDopo la mossa NE, 5, 3, true");
        System.out.println(clonata);

        board.muovi(Mossa.S, 1, 4, false);
        System.out.println("\nDopo la mossa S, 4, 1, false");
        System.out.println(board);
        */
    }
}
