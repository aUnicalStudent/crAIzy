import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class MinMax {
    private static boolean bianco;
    private enum Righe {H,G,F,E,D,C,B,A}

    static class Nodo {
        private boolean col; // true bianco, false nero
        private float euristica;
        //Scacchiera
        private BitBoard bb;
        private List<Nodo> figli = new LinkedList<>();
        private Mossa pre;

        public Nodo() {}

        public Nodo(boolean col, BitBoard b, Mossa pre) {
            this.col = col;
            this.bb = (BitBoard) b.clone();
            this.pre = pre;
        }

        public float calcolaEuristica() {
            if(bianco)
                return bb.diff();
            return -bb.diff();
        }

        @Override
        public String toString() {
            return "Nodo{ \n" + bb + "\n}";
        }
    }

    public static float minmax(Nodo nodoCorrente, int depth) {
        generaFigli(nodoCorrente);
        if(depth == 0 || nodoCorrente.figli.size() == 0)
            nodoCorrente.euristica = nodoCorrente.calcolaEuristica();
        // MASSIMIZZATORE
        else if(!(nodoCorrente.col ^ bianco)){
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
        else if(!(nodoCorrente.col ^ bianco)){
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

      /*
        *
        *
        *Aggiunto negascout da controllare bene
        *
        *
    *//*
    public static float negaScout(Nodo nodoCorrente, int depth, float alpha, float beta, boolean col) {
        generaFigli(nodoCorrente);
        if(depth == 0 || nodoCorrente.figli.size() == 0){
                if (col)
                    nodoCorrente.euristica = nodoCorrente.calcolaEuristica();
                else
                    nodoCorrente.euristica = -nodoCorrente.calcolaEuristica();
                return nodoCorrente.euristica;

        }
        else{
            float score= Float.NEGATIVE_INFINITY, n1= beta, cur;
            for (Nodo n : nodoCorrente.figli) {
                cur= -negaScout(n, depth-1, -n1, -alpha, !col);
                if (cur > score) {
                    if (n1==beta) {
                        score= cur;
                    }else{
                        score=-negaScout(n, depth-1, -beta, -cur, !col);
                    }
                }
                if (score>alpha)
                    alpha=score;
    
                if(alpha>= beta){
                    nodoCorrente.euristica=alpha;
                    return alpha;
                }
                n1= alpha+1;
            }
            nodoCorrente.euristica= score;
            return score;
        }
    }*/

    public static float negaScout (Nodo nodo, int depth, float alfa, float beta, boolean col){
        generaFigli(nodo);
        if(depth == 0 || nodo.figli.size() == 0){
            if (col)
                nodo.euristica = nodo.calcolaEuristica();
            else
                nodo.euristica = -nodo.calcolaEuristica();
            return nodo.euristica;
        }
        boolean primo=true;
        float score;
        for (Nodo n : nodo.figli) {
            if (!primo) {
                score= -negaScout(n, depth-1, (-alfa)-1, -alfa, !col);
                if(alfa<score && score <beta)
                    score= -negaScout(n, depth-1, -beta, -score, !col);
            }else{
                score= -negaScout(n, depth-1, -beta, -alfa, !col);
                primo=false;
            }
            alfa=Math.max(alfa, score);
            if(alfa >= beta)
                break; 
            
        }
        nodo.euristica=alfa;
        return alfa;
    }


    public static float negamaxAlphaBeta(Nodo nodoCorrente, int depth, float alpha, float beta, boolean col) {
        generaFigli(nodoCorrente);
        if(depth == 0 || nodoCorrente.figli.size() == 0) {
            if (col)
                nodoCorrente.euristica = nodoCorrente.calcolaEuristica();
            else
                nodoCorrente.euristica = -nodoCorrente.calcolaEuristica();
        }
        else {
            float eva = Float.NEGATIVE_INFINITY;
            for (Nodo n : nodoCorrente.figli) {
                eva = Math.max(eva, -negamaxAlphaBeta(n, depth - 1, -beta, -alpha, !col));
                alpha = Math.max(alpha, eva);
                if (alpha >= beta)
                    break;
            }
            nodoCorrente.euristica = alpha;
        }

        return nodoCorrente.euristica;
    }

    private static void generaFigli(Nodo nodo) {
        if((nodo.bb.getNumPedineB() <= 1 && nodo.bb.getNumPedineW() <= 1) || nodo.bb.getNumPedineB() < 1 || nodo.bb.getNumPedineW() < 1)
            return;

        List<Mossa> m = nodo.bb.mossePossibili(nodo.col);
        Nodo n;
        for(Mossa m1 : m) {
            n = new Nodo(!nodo.col, nodo.bb, m1);
            n.bb.muovi(m1, nodo.col);
            nodo.figli.add(n);
        }
    }

    private static Mossa scegli(Nodo nodo, boolean ab) {
        float val;
        //val = !ab? minmax(nodo, 3): anAlfaBeta(nodo, 3, Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY);
        //System.out.println(val);
        //val = negamaxAlphaBeta(nodo, 4, Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY, true);
        val = anAlfaBeta(nodo, 3, Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY);
        //val = negaScout(nodo, 3, Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY, true);
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
        BitBoard board = new BitBoard();
        System.out.println(board);
        Mossa m;

        ServerCommunication sc = new ServerCommunication();
        sc.startConnection("localhost", 8901);

        bianco = sc.recMessage().contains("White"); // Messaggio di welcome
        Nodo nn = new Nodo(bianco, board, null);
        //System.out.println(bianco);
//        sc.recMessage(); // messaggio di minchia per aspettare il secondo giocatore
//        System.out.println(sc.recMessage());
//        sc.recMessage(); // messaggio di minchia per il fatto che tutti sono connessi
        System.out.println(sc.recMessage());
        System.out.println(sc.recMessage());
//        System.out.println(sc.recMessage());

        if(bianco) {
            System.out.println(sc.recMessage());
            m = scegli(nn, true);
            //System.out.println("BIANCO MOVE " + m.getCell() + "," + m.getDir());
            sc.sendMessage("MOVE " + m.getCell() + "," + m.getDir());
            nn.bb.muovi(m, bianco);
            System.out.println(nn.bb);
        }

        String msg = "";
        while(msg != null) {
            msg = sc.recMessage();
            System.out.println("MESSAGGIO DAL SERVER " + msg);
            if(msg != null && msg.contains("OPPONENT_MOVE")) {
                String move [] = msg.split(" ")[1].split(",");
                m = new Mossa(Direzione.valueOf(move[1]),move[0].charAt(0),Integer.parseInt(move[0].substring(1)));
                //System.out.println("mossa dell'avversario: " + m);
                nn = new Nodo(bianco, nn.bb, m);
                nn.bb.muovi(m,!bianco);

                m = scegli(nn,bianco);
                if(m == null)
                    break;
                sc.sendMessage("MOVE " + m.getCell() + "," + m.getDir());
                nn.bb.muovi(m,bianco);
            }
        }
        //System.out.println(msg);


//        Scanner sc = new Scanner(System.in);
//        System.out.print("Che giocatore sei ? B o W > ");
//        sc.next();
//        bianco = sc.next().equals("White");
//        bianco = true;

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
            //Instant start = Instant.now();
// CODE HERE
            m = scegli(nn, true);
            //Instant finish = Instant.now();
            //long timeElapsed = Duration.between(start, finish).toMillis();
            nn.bb.muovi(m, bianco);
            //System.out.println("[+] Time elapsed: " + timeElapsed);
            System.out.println(m);
            System.out.println(nn);
        }

/*
        while(true) {
            //Instant start = Instant.now();
// CODE HERE
            m = scegli(nn, bianco);
            //Instant finish = Instant.now();
            //long timeElapsed = Duration.between(start, finish).toMillis();
            nn.bb.muovi(m, bianco);
            //System.out.println("[+] Time elapsed: " + timeElapsed);
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
