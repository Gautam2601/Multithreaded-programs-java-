import java.util.Scanner;

class PrimeMultiThread implements Runnable{
    int start;
    int end;
    Thread t;
    //long sumPrime;
    long localPartitionSum;
    PrimeMultiThread(int start, int end){
        this.start = start;
        this.end = end;
        //this.sumPrime = s.getSumPrime();
        t = new Thread(this);
        this.localPartitionSum = 0;

    }

    public void run(){
        SumPrimes();
    }

    private void SumPrimes(){
        for (int num = start; num <= end; num++) {
            if (num == 2 || num == 3) {
                localPartitionSum += num;
            } else if (num > 3 && num % 2 != 0 && num % 3 != 0) {
                boolean check = true;
                for (int i = 5; i * i <= num; i += 6) {
                    if (num % i == 0 || num % (i + 2) == 0) {
                        check = false;
                        break;
                    }
                }
                if (check) {
                    localPartitionSum += num;
                }
            }
        }

    }



    public long getSum(){
        return localPartitionSum;
    }
}

class DivisorMultiThread implements Runnable{
    int start;
    int end;
    Thread t;
    long localPartitionSumDivisor;
    DivisorMultiThread(int start, int end){
        this.start = start;
        this.end = end;
        this.localPartitionSumDivisor = 0;
        t = new Thread(this);
    }

    public void run(){
        SumDivisors();
    }

    private void SumDivisors(){
        for(int i = start; i<=end; i++){
            if(i%3 == 0){
                localPartitionSumDivisor += i;
            }else if(i%5 == 0){
                localPartitionSumDivisor += i;
            }else if(i%7 == 0){
                localPartitionSumDivisor += i;
            }
        }
    }

    public long getSum(){
        return localPartitionSumDivisor;
    }


}



public class PrimeMult{
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();

        try{
            if(n < 10){
                throw new IllegalArgumentException();
            }
        }catch(IllegalArgumentException e){
            System.out.println(e+": Wrong argument provided");
            return;
        }finally{
            sc.close();
        }

        int start = 2;

        int end = n;

        long finalPrimeSum = 0;
        long finalDivisorSum = 0;
        PrimeMultiThread threads = new PrimeMultiThread(start, end);
        DivisorMultiThread threads2 = new DivisorMultiThread(start, end);

        threads.t.start();
        threads2.t.start();

        try{
            threads.t.join();
            threads2.t.join();
        }catch(InterruptedException e){
            System.out.println(e+": Thread interrupted");
        }
        finalPrimeSum += threads.getSum();
        finalDivisorSum += threads2.getSum();


        System.out.println(finalPrimeSum+" "+finalDivisorSum);
    }
}
