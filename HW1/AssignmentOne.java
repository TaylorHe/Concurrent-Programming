/*
 * Taylor He
 * CS 511
 * I pledge my honor that I have abided by the Stevens Honor System. -Taylor He
 * September 7, 2017
 */
import java.util.ArrayList;
import java.util.List;

public class AssignmentOne {
	/**
	 * Returns a list of primes given an array of Interval lists
	 * @param intervals
	 * @return
	 */
	public List<Integer> lprimes(List<Integer[]> intervals){
		List<Integer> primeList = new ArrayList<Integer>(); 
		List<Thread> threadList = new ArrayList<Thread>(intervals.size());
		List<PrimeFinder> primeFinderList = new ArrayList<PrimeFinder>(intervals.size());
		
		for(int i = 0; i < intervals.size(); ++i){
			primeFinderList.add(new PrimeFinder(intervals.get(i)[0], intervals.get(i)[1]));
			threadList.add((new Thread(primeFinderList.get(i))));
		}
		for(Thread t: threadList){
			t.start();
		}
		for(Thread t: threadList){
			try {
				t.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		for(PrimeFinder p: primeFinderList){
			primeList.addAll(p.getPrimesList());
		}
		
		return primeList;
	}
	
	public static void main(String args[]){
		if(args.length % 2 == 1){
			System.out.println("Invalid number of arguments");
		}
		Integer[] toAdd;
		List<Integer[]> intlist = new ArrayList<Integer[]>();
		AssignmentOne a = new AssignmentOne();
		for(int i = 0; i < args.length-1; i++){
			toAdd = new Integer[2];
			toAdd[0] = Integer.valueOf(args[i]);
			toAdd[1] = Integer.valueOf(args[i+1]);
			intlist.add(toAdd);
		}

		System.out.println(a.lprimes(intlist));
	}
}
