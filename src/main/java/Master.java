import java.io.IOException;
import java.util.*;

public class Master {
    private static boolean colGiocatore;  // true bianco, false nero
    private static final int LIVELLI = 4;

    static class Nodo implements Comparable<Nodo>{
        private boolean col; // true bianco, false nero
        private float euristica;
        //Scacchiera
        private BitBoard bb;
        private Queue<Nodo> figli = new PriorityQueue<>();
        private Mossa pre;

        public Nodo(boolean col, BitBoard b, Mossa pre) {
            this.col = col;
            this.bb = (BitBoard) b.clone();
            this.pre = pre;
        }

        public float calcolaEuristica(){
            if (bb.somma() == 0)
                return 0;
            if(colGiocatore)
                return ((float)bb.diff())/bb.somma();
            return ((float)-bb.diff())/bb.somma();
        }

        @Override
        public String toString() {
            return "Nodo{ eur " + euristica + " calcolaEur " + calcolaEuristica() + " max = "+ col + "\n" + bb + "\n}";
        }

        @Override
        public int compareTo(Nodo o) {
            return (int) (o.calcolaEuristica() - calcolaEuristica());
        }

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
            }else if(nodoCorrente.col == colGiocatore){// massimizzatore
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

    public static float alfaBeta(Nodo nodoCorrente, int depth, float alpha, float beta) {
        if (depth==0) {
            nodoCorrente.euristica = nodoCorrente.calcolaEuristica()*(float)Math.pow(10, LIVELLI);
            return nodoCorrente.euristica;
        }

        generaFigli(nodoCorrente);
        if (nodoCorrente.figli.size() == 0) {
            nodoCorrente.euristica = nodoCorrente.calcolaEuristica()*(float)Math.pow(10, LIVELLI);
            return nodoCorrente.euristica;
        }

        // MASSIMIZZATORE
        if(nodoCorrente.col== colGiocatore) {
            float eva = Float.NEGATIVE_INFINITY;
            for(Nodo n : nodoCorrente.figli) {
                eva = Math.max(eva, alfaBeta(n, depth - 1, alpha, beta));

//                if(depth != LIVELLI)
//                    eva += nodoCorrente.calcolaEuristica() * (float) Math.pow(10, LIVELLI - depth);

                alpha = Math.max(alpha, eva);
                if (beta <= alpha)
                    break;
            }
            nodoCorrente.euristica = eva;
            return nodoCorrente.euristica;
        }

        // MINIMIZZATORE
        float eva = Float.POSITIVE_INFINITY;
        for(Nodo n : nodoCorrente.figli) {
            eva = Math.min(eva, alfaBeta(n, depth-1, alpha, beta));
            if(depth != LIVELLI)
                eva += nodoCorrente.calcolaEuristica() * (float) Math.pow(10, LIVELLI - depth);
            beta = Math.min(beta, eva);
            if (beta <= alpha)
                break;
        }
        nodoCorrente.euristica = eva;
        return nodoCorrente.euristica;

    }

    public static float negaScout (Nodo nodo, int depth, float alfa, float beta, boolean col){
        if (depth == 0) {
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
        boolean primo = true;
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
        if (depth == 0) {
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

    private static Mossa scegli(Nodo nodo) {
        float val = alfaBeta(nodo, LIVELLI, Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY);

        for(Nodo n : nodo.figli)
            if(n.euristica == val)
                return n.pre;

        return null;
    }

    private static Mossa mossaCrAIzy(char riga, int colonna) {
        List<Mossa> inganno = new ArrayList<>(4);

        if(colGiocatore) {
            inganno.add(new Mossa(Direzione.NE, 'C', 5));
            inganno.add(new Mossa(Direzione.NE, 'D', 6));
            inganno.add(new Mossa(Direzione.SW, 'E', 3));
            inganno.add(new Mossa(Direzione.SW, 'F', 4));
        }
        else {
            if(riga >= 'D' && colonna >= 5)
                inganno.add(new Mossa(Direzione.NW, 'D', 3));
            if(riga >= 'E' && colonna >= 4)
                inganno.add(new Mossa(Direzione.NW, 'C', 4));
            if(riga <= 'D' && colonna <= 5)
                inganno.add(new Mossa(Direzione.SE, 'F', 5));
            if(riga <= 'E' && colonna <= 4)
                inganno.add(new Mossa(Direzione.SE, 'E', 6));
        }

        return inganno.get(new Random().nextInt(inganno.size()));
    }

    public static void main(String[] args) throws IOException {
        BitBoard board = new BitBoard();
        Mossa m;

        // CON SERVER
        ServerCommunication sc = new ServerCommunication();

        if (args.length != 0)
            sc.startConnection(args[0], Integer.parseInt(args[1]));
        else
            System.out.println("Bad Connection");

        colGiocatore = sc.recMessage().contains("White"); // Messaggio di welcome
        Nodo nn = new Nodo(colGiocatore, board, null);
        sc.recMessage(); // messaggio per aspettare il secondo giocatore
        sc.recMessage(); // messaggio per il fatto che tutti sono connessi


        if (colGiocatore) {
            sc.recMessage();
            m = mossaCrAIzy('x', -1);
            sc.sendMessage("MOVE " + m.getCell() + "," + m.getDir());
            nn.bb.muovi(m, colGiocatore);
        }
        else {
            String msg = sc.recMessage();
            if (msg != null && msg.contains("OPPONENT_MOVE")) {
                String[] move = msg.split(" ")[1].split(",");
                m = new Mossa(Direzione.valueOf(move[1]), move[0].charAt(0), Integer.parseInt(move[0].substring(1)));
                nn = new Nodo(colGiocatore, nn.bb, m);
                nn.bb.muovi(m, !colGiocatore);

                m = mossaCrAIzy(move[0].charAt(0), Integer.parseInt(move[0].substring(1)));
                sc.sendMessage("MOVE " + m.getCell() + "," + m.getDir());
                nn.bb.muovi(m, colGiocatore);
            }
        }

        String msg = "";
        while (msg != null) {
            msg = sc.recMessage();
            if (msg != null && msg.contains("OPPONENT_MOVE")) {
                String[] move = msg.split(" ")[1].split(",");
                m = new Mossa(Direzione.valueOf(move[1]), move[0].charAt(0), Integer.parseInt(move[0].substring(1)));
                nn = new Nodo(colGiocatore, nn.bb, m);
                nn.bb.muovi(m, !colGiocatore);

                m = scegli(nn);
                if (m == null)
                    break;
                sc.sendMessage("MOVE " + m.getCell() + "," + m.getDir());
                nn.bb.muovi(m, colGiocatore);
            }
        }
    }
}
