import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentSkipListSet;

public class MinMax {
    private static boolean bianco;
    private enum Righe {H,G,F,E,D,C,B,A}
    private static int LIVELLI = 5;

    static class Nodo implements Comparable<Nodo>{
        private boolean col; // true bianco, false nero
        private float euristica;
        //Scacchiera
        private BitBoard bb;
        private Queue<Nodo> figli = new PriorityQueue<>();
        private Mossa pre;

        public Nodo() {}

        public Nodo(boolean col, BitBoard b, Mossa pre) {
            this.col = col;
            this.bb = (BitBoard) b.clone();
            this.pre = pre;
        }

        public float calcolaEuristica2() { //euristica 1
            if(bianco)
                return bb.diff();
            return -bb.diff();
        }

        public float calcolaEuristica(){
            if (bb.somma() == 0)
                return 0;
            if(bianco)
                return ((float)bb.diff())/bb.somma();
            return ((float)-bb.diff())/bb.somma();
        }

        @Override
        public String toString() {
            return "Nodo{ eur " + euristica + " calcolaEur " + calcolaEuristica() + " max = "+ col + "\n" + bb + "\n}";
        }

        @Override
        public int compareTo(Nodo o) {
//            if(o.calcolaEuristica()!=calcolaEuristica())
                return (int) (o.calcolaEuristica() - calcolaEuristica());
//            return 1;

//            return (int) (o.calcolaEuristica() - calcolaEuristica());
        }

        //TODO in caso rimuovere
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Nodo nodo = (Nodo) o;
            return bb.equals(nodo.bb);
        }

        @Override
        public int hashCode() {
            return Objects.hash(col, euristica, bb, figli, pre);
        }
    }

    public static float minmax(Nodo nodoCorrente, int depth) {
        if (depth==0) {
            nodoCorrente.euristica = nodoCorrente.calcolaEuristica();
        }else{
            generaFigli(nodoCorrente);
            if (nodoCorrente.figli.size() == 0) {
                nodoCorrente.euristica = nodoCorrente.calcolaEuristica();
            }else if(!(nodoCorrente.col ^ bianco)){// massimizzatore
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
        }
        return nodoCorrente.euristica;
    }

    public static float minmaxCONLIVELLIWOW(Nodo nodoCorrente, int depth) {
        if (depth==0) {
            nodoCorrente.euristica = nodoCorrente.calcolaEuristica()*(float)Math.pow(10, LIVELLI);
        }else{
            generaFigli(nodoCorrente);
            if (nodoCorrente.figli.size() == 0) {
                nodoCorrente.euristica = nodoCorrente.calcolaEuristica()*(float)Math.pow(10, LIVELLI);
            }else if(nodoCorrente.col==bianco){// massimizzatore
                float eva = Float.NEGATIVE_INFINITY;
                for(Nodo n : nodoCorrente.figli) {
                    eva = Math.max(eva, minmax(n, depth - 1));
                    if (depth != LIVELLI)
                        eva += nodoCorrente.calcolaEuristica() * (float) Math.pow(10, LIVELLI - depth);
                }
                nodoCorrente.euristica = eva;
            }
            // MINIMIZZATORE
            else {
                float eva = Float.POSITIVE_INFINITY;
                for(Nodo n : nodoCorrente.figli) {
                    eva = Math.min(eva, minmax(n, depth - 1));
                    if (depth != LIVELLI)
                        eva += nodoCorrente.calcolaEuristica() * (float) Math.pow(10, LIVELLI - depth);
                }
                nodoCorrente.euristica = eva;
            }
        }
        return nodoCorrente.euristica;
    }

    //TODO Bisogna effettuare ulteriore debug
    public static float anAlfaBeta(Nodo nodoCorrente, int depth, float alpha, float beta) {
        Nodo x = null;
        float curr;
        if (depth==0) {
            nodoCorrente.euristica = nodoCorrente.calcolaEuristica()*(float)Math.pow(10, LIVELLI);
            return nodoCorrente.euristica;
        }

        generaFigli(nodoCorrente);
//        if(depth==LIVELLI)
//            Collections.sort(nodoCorrente.figli);
        if (nodoCorrente.figli.size() == 0) {
            nodoCorrente.euristica = nodoCorrente.calcolaEuristica()*(float)Math.pow(10, LIVELLI);
            return nodoCorrente.euristica;
        }

        // MASSIMIZZATORE
        if(nodoCorrente.col==bianco) {
            float eva = Float.NEGATIVE_INFINITY;
            for(Nodo n : nodoCorrente.figli) {
                //eva = Math.max(eva, anAlfaBeta(n, depth - 1, alpha, beta));
                curr = anAlfaBeta(n, depth - 1, alpha, beta);
                if(eva < curr){
                    eva = curr;
                    x = n;
                }
//                if(depth != LIVELLI) {
//                    float asd = nodoCorrente.calcolaEuristica() * (float) Math.pow(10, LIVELLI - depth);
////                    System.out.println("DEPTH = " + depth + " | " + asd + " + " + eva + " MAX = " + (eva+asd));
//                    eva += asd;
//                }
                alpha = Math.max(alpha, eva);
                if (beta <= alpha) {
                    break;
                }
            }
            nodoCorrente.euristica = eva;
            //nodoCorrente.figli.clear();
            //nodoCorrente.figli.add(x);
            return nodoCorrente.euristica;
        }
        // MINIMIZZATORE
        else {
            float eva = Float.POSITIVE_INFINITY;
            for(Nodo n : nodoCorrente.figli){
                //eva = Math.min(eva, anAlfaBeta(n, depth-1, alpha, beta)); //tipo qua
                curr = anAlfaBeta(n, depth - 1, alpha, beta);
                if(eva > curr){
                    eva = curr;
                    x = n;
                }
                if(depth != LIVELLI) {
                    float asd = nodoCorrente.calcolaEuristica() * (float) Math.pow(10, LIVELLI - depth);
                    //System.out.println("DEPTH = " + depth + " | " + asd + " + " + eva + " MIN = " + (eva+asd));
                    eva += asd;
                }
                beta = Math.min(beta, eva);
                if (beta <= alpha) {
                    break;
                }
            }
            nodoCorrente.euristica = eva;
            //nodoCorrente.figli.clear();
            //nodoCorrente.figli.add(x);
            return nodoCorrente.euristica;
        }
    }

    public static float negaScout (Nodo nodo, int depth, float alfa, float beta, boolean col){
        if (depth==0) {
            if (col)
                nodo.euristica = nodo.calcolaEuristica();
            else
                nodo.euristica = -nodo.calcolaEuristica();
            return nodo.euristica;
        }
        generaFigli(nodo);
        if (nodo.figli.size() == 0) {
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
        if (depth==0) {
            if (col)
                nodoCorrente.euristica = nodoCorrente.calcolaEuristica();
            else
                nodoCorrente.euristica = -nodoCorrente.calcolaEuristica();
            return nodoCorrente.euristica;
        }
        generaFigli(nodoCorrente);
        if (nodoCorrente.figli.size() == 0) {
            if (col)
                nodoCorrente.euristica = nodoCorrente.calcolaEuristica();
            else
                nodoCorrente.euristica = -nodoCorrente.calcolaEuristica();
            return nodoCorrente.euristica;
        }
        float eva = Float.NEGATIVE_INFINITY;
        for (Nodo n : nodoCorrente.figli) {
            eva = Math.max(eva, -negamaxAlphaBeta(n, depth - 1, -beta, -alpha, !col));
            alpha = Math.max(alpha, eva);
            if (alpha >= beta)
                break;
        }
        nodoCorrente.euristica = alpha;

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
        //System.out.println(ab? "AB": "MINMAX");
        val = !ab? minmax(nodo, LIVELLI): anAlfaBeta(nodo, LIVELLI, Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY);
        //System.out.println(val);
        //val = negamaxAlphaBeta(nodo, 4, Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY, true);
        //val = anAlfaBeta(nodo, LIVELLI, Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY);
        //val = negaScout(nodo, 3, Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY, true);
        float a;
        Mossa mossa = null;

        for(Nodo n : nodo.figli) {
            a = n.euristica;
            if(a == val) {
                mossa = n.pre;
                System.out.println("----------------------------------");
////                stampaGerarchia(n);
                System.out.println(nodo.figli);
                System.out.println("----------------------------------");
                System.out.println(mossa + " -> " + a);
                return mossa;
            }
        }

        return mossa;
    }

    private static void stampaGerarchia(Nodo n) {
        System.out.println(n);
        if(n.figli.isEmpty()) {
            return;
        }
        stampaGerarchia(n.figli.iterator().next());
    }

    public static void main(String[] args) throws IOException {
//        BitBoard board = new BitBoard(72057886800609344L, 4647719222420963344L);
        BitBoard board = new BitBoard(-9223372015379939328L, 4611690425064357888L);
//        BitBoard board = new BitBoard();
        System.out.println(board);
        Mossa m;
        Nodo nn = null;

        Scanner s = new Scanner(System.in);
        System.out.print("CON CHI VUOI GIOCARE ? G o S -> ");

        if(s.next().toUpperCase().equals("S")) {
            System.out.print("# LIVELLI: ");
            MinMax.LIVELLI = s.nextInt();
            System.out.print("ALGORITMO (true -> analfa / false -> minmax): ");
            boolean algo = s.nextBoolean();

            // CON SERVER
            ServerCommunication sc = new ServerCommunication();

            System.out.print("LOCALHOST ? Y o N -> ");
            if(s.next().toUpperCase().equals("Y"))
                sc.startConnection("localhost", 8901);
            else sc.startConnection("160.97.28.146", 8901);

            bianco = sc.recMessage().contains("White"); // Messaggio di welcome
            nn = new Nodo(bianco, board, null);
            //System.out.println(bianco);
            //        sc.recMessage(); // messaggio per aspettare il secondo giocatore
            //        System.out.println(sc.recMessage());
            //        sc.recMessage(); // messaggio per il fatto che tutti sono connessi
            System.out.println(sc.recMessage());
            System.out.println(sc.recMessage());
            //        System.out.println(sc.recMessage());


            if (bianco) {
                System.out.println(sc.recMessage());
                m = scegli(nn, algo);
                //System.out.println("BIANCO MOVE " + m.getCell() + "," + m.getDir());
                sc.sendMessage("MOVE " + m.getCell() + "," + m.getDir());
                nn.bb.muovi(m, bianco);
                System.out.println(nn.bb);
            }

            String msg = "";
            while (msg != null) {
                msg = sc.recMessage();
                System.out.println("MESSAGGIO DAL SERVER " + msg);
                if (msg != null && msg.contains("OPPONENT_MOVE")) {
                    String move[] = msg.split(" ")[1].split(",");
                    m = new Mossa(Direzione.valueOf(move[1]), move[0].charAt(0), Integer.parseInt(move[0].substring(1)));
                    //System.out.println("mossa dell'avversario: " + m);
                    nn = new Nodo(bianco, nn.bb, m);
                    nn.bb.muovi(m, !bianco);

                    m = scegli(nn, algo);
                    if (m == null)
                        break;
                    sc.sendMessage("MOVE " + m.getCell() + "," + m.getDir());
                    nn.bb.muovi(m, bianco);
                }
            }
        }

        //System.out.println(msg);
        else {
            // CON GIOCATORI
            Scanner sc = new Scanner(System.in);
            System.out.print("Che giocatore sei ? B o W > ");
            boolean ab = true;
            //sc.next();
            // Da cambiare quando si vuole giocare con un altro giocatore
            // Se B allora giochi da bianco, Se W giochi da nero
            bianco = sc.next().toUpperCase().equals("B");
            nn = new Nodo(bianco, board, null);


            if (bianco) {
                Instant start = Instant.now();
                m = scegli(nn, ab);
                Instant finish = Instant.now();
                System.out.println(m);
                long timeElapsed = Duration.between(start, finish).toMillis();
                nn.bb.muovi(m, bianco);
                System.out.println("[+] Time elapsed: " + timeElapsed);
                System.out.println(nn);
            }

            while (true) {
                System.out.print("> ");
                Direzione dir = Direzione.valueOf(sc.next().toUpperCase());
                String y = sc.next().toUpperCase();
                m = new Mossa(dir, y.charAt(0), Integer.parseInt(String.valueOf(y.charAt(1))));
                //            System.out.println(m.getRM() + " " + m.getCM());

                nn = new Nodo(bianco, nn.bb, null);
                nn.bb.muovi(m, !bianco);
                System.out.println(nn);
                Instant start = Instant.now();

                m = scegli(nn, ab);
                Instant finish = Instant.now();
                long timeElapsed = Duration.between(start, finish).toMillis();
                nn.bb.muovi(m, bianco);
                System.out.println("[+] Time elapsed: " + timeElapsed);
                System.out.println(m);
                System.out.println(nn);
            }
        }
    }
}
