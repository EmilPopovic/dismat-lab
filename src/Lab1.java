import java.math.BigInteger;
import java.util.*;

public class Lab1 {

    private static final Map<Integer, Map<Integer, BigInteger>> binomKoefMemo = new HashMap<>();

    public static void main(String[] args) {
        LinkedHashMap<String, Integer> cajevi = new LinkedHashMap<>();

        cajevi.put("Bohea", 0);
        cajevi.put("Congou", 0);
        cajevi.put("Souchong", 0);
        cajevi.put("Singlo", 0);
        cajevi.put("Hyson", 0);

        Scanner in = new Scanner(System.in);

        int n = 0;
        try {
            int i = 0;
            int upis;
            for (Map.Entry<String, Integer> entry : cajevi.sequencedEntrySet()) {
                System.out.print(
                        "Unesite broj vrecica caja " + entry.getKey() + " (parametar " + (char) ('a' + i++) + "): "
                );

                upis = in.nextInt();

                if (upis < -1) {
                    System.out.println("Neispravan unos, mora biti nenegativan cijeli broj ili -1");
                    System.exit(0);
                }

                cajevi.put(entry.getKey(), upis);
            }

            System.out.print("Unesite broj n: ");

            n = in.nextInt();

            if (n < 0) {
                System.out.println("Neispravan unos, n nije nenegativan broj");
                System.exit(0);
            }
        } catch (Exception e) {
            System.out.println("Neispravan unos, nije broj");
            System.exit(0);
        }

        BigInteger rezultat = prebroj(n, cajevi.values());

        System.out.println("Gradjani Bostona mogu napraviti " + rezultat.toString() + " razlicitih cajanki.");
    }

    private static BigInteger prebroj(int n, Collection<Integer> vrecice) {
        if (n == 0) {
            return BigInteger.ONE;
        }

        Map<Integer, BigInteger> nazivnik = nazivnikRedaN(vrecice.size());
        List<Map<Integer, BigInteger>> brojnici = new LinkedList<>();

        for (int v : vrecice) {
            Map<Integer, BigInteger> brojnik = new TreeMap<>();

            brojnik.put(0, BigInteger.ONE);
            brojnik.put((v >= 0) ? v + 1 : n + 1, BigInteger.valueOf(-1));

            brojnici.add(brojnik);
        }

        Map<Integer, BigInteger> brojnik = new TreeMap<>();
        brojnik.put(0, BigInteger.ONE);

        for (Map<Integer, BigInteger> b : brojnici) {
            brojnik = mnozi(brojnik, b);
        }

        Map<Integer, BigInteger> rezP = dijeli(brojnik, nazivnik);

        return rezP.getOrDefault(n, BigInteger.ZERO);
    }

    private static Map<Integer, BigInteger> nazivnikRedaN(int n) {
        Map<Integer, BigInteger> p = new TreeMap<>();
        for (int k = 0; k <= n; k++) {
            BigInteger koef = binomniKoef(n, k);
            p.put(k, (k % 2 == 0) ? koef : BigInteger.ZERO.subtract(koef));
        }
        return p;
    }

    private static BigInteger binomniKoef(int n, int k) {
        if (k > n) {
            return BigInteger.ZERO;
        } else if (k == 0 || k == n) {
            return BigInteger.ONE;
        } else if (binomKoefMemo.containsKey(n) && binomKoefMemo.get(n).containsKey(k)) {
            return binomKoefMemo.get(n).get(k);
        }

        BigInteger koef = binomniKoef(n - 1, k - 1).add(binomniKoef(n - 1, k));

        binomKoefMemo.putIfAbsent(n, new HashMap<>());
        binomKoefMemo.get(n).put(k, koef);

        return koef;
    }

    private static Map<Integer, BigInteger> mnozi(Map<Integer, BigInteger> p, Map<Integer, BigInteger> q) {
        Map<Integer, BigInteger> rez = new TreeMap<>();

        for (Map.Entry<Integer, BigInteger> entryP : p.entrySet()) {
            for (Map.Entry<Integer, BigInteger> entryQ : q.entrySet()) {
                BigInteger a = entryP.getValue().multiply(entryQ.getValue());
                int exp = entryP.getKey() + entryQ.getKey();

                rez.computeIfPresent(exp, (_, v) -> v.add(a));
                rez.putIfAbsent(exp, a);
                rez.remove(exp, BigInteger.ZERO);
            }
        }

        return rez;
    }

    private static Map<Integer, BigInteger> dijeli(Map<Integer, BigInteger> p, Map<Integer, BigInteger> q) {
        Map<Integer, BigInteger> rez = new TreeMap<>();
        Map<Integer, BigInteger> ost = new TreeMap<>(p);

        int redP = p.isEmpty() ? 0 : p.keySet().stream().max(Integer::compareTo).get();
        int redQ = q.isEmpty() ? 0 : q.keySet().stream().max(Integer::compareTo).get();

        while (!ost.isEmpty() && redP > redQ) {
            BigInteger vodeciP = ost.getOrDefault(redP, BigInteger.ZERO);
            BigInteger vodeciQ = q.getOrDefault(redQ, BigInteger.ZERO);

            BigInteger koef = vodeciP.divide(vodeciQ);

            int razlikaReda = redP - redQ;
            rez.put(razlikaReda, koef);

            for (Map.Entry<Integer, BigInteger> entry : q.entrySet()) {
                int red = entry.getKey() + razlikaReda;
                BigInteger oduzetiKoef = entry.getValue().multiply(koef);
                ost.put(red, ost.getOrDefault(red, BigInteger.ZERO).subtract(oduzetiKoef));
                if (ost.get(red).equals(BigInteger.ZERO)) {
                    ost.remove(red);
                }
            }

            redP = ost.isEmpty() ? 0 : ost.keySet().stream().max(Integer::compareTo).orElse(0);
        }

        return rez;
    }
}
