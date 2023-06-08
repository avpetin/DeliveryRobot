import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

public class Main {
    public static final Map<Integer, Integer> sizeToFreq = new LinkedHashMap<>();

    public static void main(String[] args) throws InterruptedException {
        Thread[] threads = new Thread[1000];
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(() -> {
                String route = generateRoute("RLRFR", 100);
                String[] rCountArray = route.split("R");
                System.out.println("Kоличество команд поворота направо " + rCountArray.length);
                synchronized (sizeToFreq) {
                    int value = sizeToFreq.get(rCountArray.length) == null ? 1 : sizeToFreq.get(rCountArray.length) + 1;
                    sizeToFreq.put(rCountArray.length, value);
                }
            });
            threads[i].start();
            try {
                threads[i].wait();
            }
            catch (IllegalMonitorStateException e){}
            threads[i].join();
        }
        Thread leaderThread = new Thread(() -> {
            int mapSize = 0;
            int max_key = 0;
            int max_value = 0;
            while(!Thread.interrupted()){
                if(mapSize >= sizeToFreq.size()) {
                    for (Map.Entry<Integer, Integer> entry : sizeToFreq.entrySet()) {
                        if (max_value < entry.getValue()) {
                            max_value = entry.getValue();
                            max_key = entry.getKey();
                        }
                    }
                    System.out.println("\nЛидер по частоте повторений " + max_key +
                            " (встретилось " + max_value + " раз)");
                }
               else {
                    mapSize++;
                    threads[sizeToFreq.size() - 1].notify();
               }
            }
        });
        leaderThread.start();
        while(sizeToFreq.size() != threads.length){};
        leaderThread.interrupt();
    }

    public static String generateRoute(String letters, int length) {
        Random random = new Random();
        StringBuilder route = new StringBuilder();
        for (int i = 0; i < length; i++) {
            route.append(letters.charAt(random.nextInt(letters.length())));
        }
        return route.toString();
    }
}