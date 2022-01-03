import java.io.BufferedReader;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class MinMax {
    private static boolean bianco;
    private enum Righe {H,G,F,E,D,C,B,A}

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
            if(bianco)
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
            float eva = Float.NEGATIVE_INFINITY;
            for(Nodo n : nodoCorrente.figli)
                eva = Math.max(eva, minmax(n, depth-1));
            nodoCorrente.euristica = eva;
        }
        // MINIMIZZATORE
        else {
            float eva = Float.POSITIVE_INFINITY;
            for(Nodo n : nodoCorrente.figli)
                eva = Math.min(eva, minmax(n, depth-1));
            nodoCorrente.euristica = eva;
        }

        return nodoCorrente.euristica;
    }

    public static float anAlfaBeta(Nodo nodoCorrente, int depth, float alpha, float beta) {
        generaFigli(nodoCorrente);
        if(depth == 0 || nodoCorrente.figli.size() == 0)
            nodoCorrente.euristica = nodoCorrente.calcolaEuristica();
        // MASSIMIZZATORE
        else if(!(nodoCorrente.raffaeleMASTER ^ bianco)){
            float eva = Float.NEGATIVE_INFINITY;
            for(Nodo n : nodoCorrente.figli) {
                eva = Math.max(eva, anAlfaBeta(n, depth - 1, alpha, beta));
                alpha = Math.max(alpha, eva);
                if (beta <= alpha)
                    break;
            }
            nodoCorrente.euristica = eva;
        }
        // MINIMIZZATORE
        else {
            float eva = Float.POSITIVE_INFINITY;
            for(Nodo n : nodoCorrente.figli){
                eva = Math.min(eva, anAlfaBeta(n, depth-1, alpha, beta));
                beta = Math.min(beta, eva);
                if (beta <= alpha)
                    break;
            }
            nodoCorrente.euristica = eva;
        }

        return nodoCorrente.euristica;
    }

    private static void generaFigli(Nodo nodo) {
        if((nodo.bb.getNumPedineB() <= 1 && nodo.bb.getNumPedineW() <= 1) || nodo.bb.getNumPedineB() < 1 || nodo.bb.getNumPedineW() < 1)
            return;

        List<Mossa> m = nodo.bb.mossePossibili(nodo.raffaeleMASTER);
        Nodo n;
        for(Mossa m1 : m) {
            n = new Nodo(!nodo.raffaeleMASTER, nodo.bb, m1);
            n.bb.muovi(m1, nodo.raffaeleMASTER);
            nodo.figli.add(n);
        }
    }

    private static Mossa scegli(Nodo nodo, boolean ab) {
        float val;
        val = !ab? minmax(nodo, 3): anAlfaBeta(nodo, 4, Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY);
        //System.out.println(val);

        float a;
        for(Nodo n : nodo.figli) {
            a = n.euristica;
            //System.out.println(a);
            if(a == val)
                return n.pre;
        }

        return null;
    }

    public static void main(String[] args) throws IOException {
        //BitBoardSuperPazzaSgravata board = new BitBoardSuperPazzaSgravata(1152921642045800448L, 2305843009213693968L);
        BitBoardSuperPazzaSgravata board = new BitBoardSuperPazzaSgravata();
        System.out.println(board);
        Mossa m;
        //Scanner sc = new Scanner(System.in);
        //System.out.print("Che giocatore sei ? B o W > ");
        //bianco = sc.next().toUpperCase().equals("B");
        bianco = true;
        Nodo nn = new Nodo(bianco, board, null);

/*
        if(bianco) {
            m = scegli(nn, true);
            System.out.println(m);
            nn.bb.muovi(m, bianco);
            System.out.println(nn);
        }

        while(true) {
            System.out.print("> ");
            Direzione dir = Direzione.valueOf(sc.next().toUpperCase());
            int y = sc.nextInt();
            int x = sc.nextInt();
            m = new Mossa(dir, y, x);
//            System.out.println(m.getRM() + " " + m.getCM());

            nn = new Nodo(bianco, nn.bb, null);
            nn.bb.muovi(m, !bianco);
            System.out.println(nn);
            Instant start = Instant.now();
// CODE HERE
            m = scegli(nn, true);
            Instant finish = Instant.now();
            long timeElapsed = Duration.between(start, finish).toMillis();
            nn.bb.muovi(m, bianco);
            System.out.println("[+] Time elapsed: " + timeElapsed);
            System.out.println(m);
            System.out.println(nn);
        }


        while(true) {
            Instant start = Instant.now();
// CODE HERE
            m = scegli(nn, bianco);
            Instant finish = Instant.now();
            long timeElapsed = Duration.between(start, finish).toMillis();
            nn.bb.muovi(m, bianco);
            System.out.println("[+] Time elapsed: " + timeElapsed);
            System.out.println(m);
            System.out.println(nn);
            bianco = !bianco;
            nn = new Nodo(bianco, nn.bb, null);
            System.out.println(!bianco? "BIANCO" : "NERO");
        }

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
