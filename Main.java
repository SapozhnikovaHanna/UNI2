//java package
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// Інтерфейс
interface Sorter {
    List<Integer> sort(List<Integer> input);
}

// Клас сортування бульбашкою
class BubbleSorter implements Sorter {
    public List<Integer> sort(List<Integer> input) {
        List<Integer> sorted = new ArrayList<>(input);
        int n = sorted.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (sorted.get(j) > sorted.get(j + 1)) {
                    int temp = sorted.get(j);
                    sorted.set(j, sorted.get(j + 1));
                    sorted.set(j + 1, temp);
                }
            }
        }
        return sorted;
    }
}

// Клас сортування Шелла
class ShellSorter implements Sorter {
    public List<Integer> sort(List<Integer> input) {
        List<Integer> sorted = new ArrayList<>(input);
        int n = sorted.size();
        for (int gap = n / 2; gap > 0; gap /= 2) {
            for (int i = gap; i < n; i++) {
                int temp = sorted.get(i);
                int j;
                for (j = i; j >= gap && sorted.get(j - gap) > temp; j -= gap) {
                    sorted.set(j, sorted.get(j - gap));
                }
                sorted.set(j, temp);
            }
        }
        return sorted;
    }
}


// Клас сортування злиттям
class MergeSorter implements Sorter {
    public List<Integer> sort(List<Integer> input) {
        List<Integer> sorted = new ArrayList<>(input);
        mergeSort(sorted, 0, sorted.size() - 1);
        return sorted;
    }

    private void mergeSort(List<Integer> input, int left, int right) {
        if (left < right) {
            int mid = (left + right) / 2;
            mergeSort(input, left, mid);
            mergeSort(input, mid + 1, right);
            merge(input, left, mid, right);
        }
    }

    private void merge(List<Integer> input, int left, int mid, int right) {
        int n1 = mid - left + 1;
        int n2 = right - mid;
        List<Integer> leftArr = new ArrayList<>();
        List<Integer> rightArr = new ArrayList<>();
        for (int i = 0; i < n1; i++) {
            leftArr.add(input.get(left + i));
        }
        for (int j = 0; j < n2; j++) {
            rightArr.add(input.get(mid + 1 + j));
        }
        int i = 0;
        int j = 0;
        int k = left;
        while (i < n1 && j < n2) {
            if (leftArr.get(i) <= rightArr.get(j)) {
                input.set(k, leftArr.get(i));
                i++;
            } else {
                input.set(k, rightArr.get(j));
                j++;
            }
            k++;
        }
        while (i < n1) {
            input.set(k, leftArr.get(i));
            i++;
            k++;
        }
        while (j < n2) {
            input.set(k, rightArr.get(j));
            j++;
            k++;
        }
    }
}

// Клас швидкого сортування
class QuickSorter implements Sorter {
    public List<Integer> sort(List<Integer> input) {
        List<Integer> sorted = new ArrayList<>(input);
        quickSort(sorted, 0, sorted.size() - 1);
        return sorted;
    }

    private void quickSort(List<Integer> input, int low, int high) {
        if (low < high) {
            int pi = partition(input, low, high);
            quickSort(input, low, pi - 1);
            quickSort(input, pi + 1, high);
        }
    }

    private int partition(List<Integer> input, int low, int high) {
        int pivot = input.get(high);
        int i = low - 1;
        for (int j = low; j < high; j++) {
            if (input.get(j) < pivot) {
                i++;
                int temp = input.get(i);
                input.set(i, input.get(j));
                input.set(j, temp);
            }
        }
        int temp = input.get(i + 1);
        input.set(i + 1, input.get(high));
        input.set(high, temp);
        return i + 1;
    }
}

// Перерахування для типів сортування
enum SortingType {
    BUBBLE,
    SHELL,
    MERGE,
    QUICK
}

//Фабричний(статичний) метод
class SortingFactory {
    public static Sorter createSorter(SortingType type) {
        switch (type) {
            case BUBBLE:
                return new BubbleSorter();
            case SHELL:
                return new ShellSorter();
            case MERGE:
                return new MergeSorter();
            case QUICK:
                return new QuickSorter();
        }
        return null;
    }
}

public class Main {
    public static void main(String[] args) {
        int[] counts = {10, 1000, 10000, 1000000};

        for (int count : counts) {
            //кількість ел-тів
            System.out.println("Кількість елементів: " + count);
            //Генерація масиву
            List<Integer> input = generateRandomArray(count);
            System.out.println("Вхідний масив: " + inputToString(input));

            for (SortingType type : SortingType.values()) {
                //тип сортування
                System.out.println("Тип сортування: " + type);
                Sorter sorter = SortingFactory.createSorter(type);

                //час виконання
                List<Integer> sorted = new ArrayList<>(input);
                long startTime = System.nanoTime();
                sorted = sorter.sort(sorted);
                long endTime = System.nanoTime();
                long duration = (endTime - startTime) / 1000000;

                System.out.println("Відсортований масив: " + inputToString(sorted.subList(0, Math.min(50, sorted.size()))));
                System.out.println("Час сортування: " + duration + " мс");
                System.out.println();
            }
        }
    }

    private static List<Integer> generateRandomArray(int count) {
        Random random = new Random();
        List<Integer> array = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            array.add(random.nextInt(count + 1));
        }
        return array;
    }

    private static String inputToString(List<Integer> array) {
        StringBuilder builder = new StringBuilder();
        int count = array.size();
        for (int i = 0; i < Math.min(50, count); i++) {
            builder.append(array.get(i));
            if (i != count - 1) {
                builder.append(", ");
            }
        }
        return builder.toString();
    }
}