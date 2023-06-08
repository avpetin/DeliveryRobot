import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

public class Main {
    public static final Map<Integer, Integer> sizeToFreq = new LinkedHashMap<>();

    public static void main(String[] args) throws InterruptedException {
        final Thread[] threads = new Thread[1000];
        for (int i = 0; i < threads.length; i++){
            threads[i] = new Thread(()->{
                String route = generateRoute("RLRFR", 100);
                String[] rCountArray = route.split("R");
                System.out.println("Kоличество команд поворота направо " + rCountArray.length);
                synchronized (sizeToFreq){
                    int value = sizeToFreq.get(rCountArray.length) == null ? 1 : sizeToFreq.get(rCountArray.length) + 1;
                    sizeToFreq.put(rCountArray.length, value);
                }
            });
            threads[i].start();
            threads[i].join();
        }

/*        Map<Integer, Integer> sortedMap = sizeToFreq
                .entrySet()
                .stream()
                .sorted(Map.Entry.<Integer, Integer>comparingByValue().reversed())
                .collect( Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,
                        LinkedHashMap::new));*/

        int max_key = 0;
        int max_value = 0;
        for(Map.Entry<Integer, Integer> entry : sizeToFreq.entrySet()){
            if(max_value < entry.getValue()){
                max_value = entry.getValue();
                max_key = entry.getKey();
            }
        }
        System.out.println("\nСамое частое количество повторений " + max_key +
                " (встретилось " + max_value + " раз)");
        System.out.println("Другие размеры:");

        for(Map.Entry<Integer, Integer> entry : sizeToFreq.entrySet()){
            if(entry.getKey() != max_key && entry.getValue() != max_value) {
                System.out.println("- " + entry.getKey() + " (" + entry.getValue() + " раз)");
            }
        }
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