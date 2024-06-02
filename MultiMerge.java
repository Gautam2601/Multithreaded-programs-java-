


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

class MergeSort implements Runnable {
    private int[] array;
    private int si, ei;
    private Thread t;
    private int[] finalAns;

    MergeSort(int[] array, int si, int ei) {
        this.array = array;
        this.si = si;
        this.ei = ei;
        this.finalAns = new int[ei - si + 1];
        t = new Thread(this);
    }

    public void run() {
        sort(array, si, ei);
    }

    private void sort(int[] array, int si, int ei) {
        if (si < ei) {
            int mid = (si + ei) / 2;
            sort(array, si, mid);
            sort(array, mid + 1, ei);
            merge(array, si, mid, ei);
        }
    }

    private void merge(int[] array, int si, int mid, int ei) {
        int sizeArr1 = mid - si + 1;
        int sizeArr2 = ei - mid;

        int[] Arr1 = new int[sizeArr1];
        int[] Arr2 = new int[sizeArr2];

        for (int i = 0; i < sizeArr1; i++) {
            Arr1[i] = array[si + i];
        }
        for (int i = 0; i < sizeArr2; i++) {
            Arr2[i] = array[mid + 1 + i];
        }

        int Arr1Index = 0, Arr2Index = 0, finalArrIndex = si;

        while (Arr1Index < sizeArr1 && Arr2Index < sizeArr2) {
            if (Arr1[Arr1Index] <= Arr2[Arr2Index]) {
                array[finalArrIndex++] = Arr1[Arr1Index++];
            } else {
                array[finalArrIndex++] = Arr2[Arr2Index++];
            }
        }

        while (Arr1Index < sizeArr1) {
            array[finalArrIndex++] = Arr1[Arr1Index++];
        }

        while (Arr2Index < sizeArr2) {
            array[finalArrIndex++] = Arr2[Arr2Index++];
        }
    }

    public int[] getSortedArray() {
        return array;
    }

    public Thread getThread() {
        return t;
    }
}

public class MultiMerge {

    public static void main(String[] args) {
        FileInputStream fin = null;
        try {
            fin = new FileInputStream(args[0]);
        } catch (FileNotFoundException e) {
            System.out.println(e + ": Error opening the specified file");
            return;
        }

        Scanner sc = new Scanner(fin);
        int[] array = new int[1000];
        int arraySize = 0;

        try {
            while (sc.hasNext()) {
                array[arraySize++] = sc.nextInt();
            }
            if (arraySize < 1) {
                throw new IllegalArgumentException();
            }
        } catch (IllegalArgumentException e) {
            System.out.println(e + ": Wrong argument provided");
        } finally {
            sc.close();
            try {
                fin.close();
            } catch (IOException e) {
                System.out.println(e + ": IO exception");
            }
        }


        int si = 0;
        int ei = arraySize - 1;
        int mid = (ei + si) / 2;

        int leftHalfArr[] = new int[mid - si + 1];
        int RightHalfArr[] = new int[ei - mid];

        int lSize = leftHalfArr.length;
        int rSize = RightHalfArr.length;

        for (int i = 0; i < lSize; i++) {
            leftHalfArr[i] = array[i];
        }

        for (int i = 0; i < rSize; i++) {
            RightHalfArr[i] = array[mid + 1 + i];
        }


        MergeSort mergeSort = new MergeSort(leftHalfArr, 0, lSize - 1);
        MergeSort mergeSort2 = new MergeSort(RightHalfArr, 0, rSize - 1);
        mergeSort.getThread().start();
        mergeSort2.getThread().start();
        try {
            mergeSort.getThread().join();
            mergeSort2.getThread().join();
        } catch (InterruptedException e) {
            System.out.println(e + ": Thread interrupted");
        } finally {

            array = merge(mergeSort.getSortedArray(), mergeSort2.getSortedArray(), arraySize);

            for (int i = 0; i < arraySize; i++) {
                System.out.print(array[i]);
                if (i != arraySize - 1) {
                    System.out.print(" ");
                }
            }
        }
    }

    public static int[] merge(int[] Arr1, int[] Arr2, int arraySize) {
        int[] finalAns = new int[arraySize];
        int sizeArr1 = Arr1.length;
        int sizeArr2 = Arr2.length;
        int Arr1Index = 0, Arr2Index = 0, finalArrIndex = 0;

        while (Arr1Index < sizeArr1 && Arr2Index < sizeArr2) {
            if (Arr1[Arr1Index] <= Arr2[Arr2Index]) {
                finalAns[finalArrIndex++] = Arr1[Arr1Index++];
            } else {
                finalAns[finalArrIndex++] = Arr2[Arr2Index++];
            }
        }

        while (Arr1Index < sizeArr1) {
            finalAns[finalArrIndex++] = Arr1[Arr1Index++];
        }

        while (Arr2Index < sizeArr2) {
            finalAns[finalArrIndex++] = Arr2[Arr2Index++];
        }

        return finalAns;
    }
}
