import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

/**
 * Created by Chaiyong on 11/28/16.
 */
public class RandomNumbers {
    public static void main(String[] args) {
        int somePredefinedSeed = 53;
        int sampleSize = 26;

        Integer[] arr = new Integer[523];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = i;
        }
        Collections.shuffle(Arrays.asList(arr), new Random(somePredefinedSeed));
        System.out.println(Arrays.toString(arr));
        System.out.println("SAMPLED:");

        for (int i = 0; i < sampleSize; i++) {
            if (i == sampleSize - 1)
                System.out.print(arr[i]);
            else
                System.out.print(arr[i] + ", ");
        }

    }

    public Integer[] generateRandomNumbers(int seed, int populationSize, int sampleSize) {
        Integer[] samples = new Integer[sampleSize];
        Integer[] arr = new Integer[populationSize];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = i;
        }
        Collections.shuffle(Arrays.asList(arr), new Random(seed));

        for (int i = 0; i < sampleSize; i++) {
            samples[i] = arr[i];
        }

        return samples;
    }
}
