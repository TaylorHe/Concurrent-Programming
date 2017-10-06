package HW1;
/*
 * Taylor He
 * CS 511
 * I pledge my honor that I have abided by the Stevens Honor System. -Taylor He
 * September 7, 2017
 */

import java.util.ArrayList;
import java.util.List;

public class PrimeFinder implements Runnable{

	private Integer start, end;
	private List<Integer> primes;
	
	/**
	 * Constructor sets the start and end
	 * @param startNum
	 * @param endNum
	 */
	PrimeFinder(Integer startNum, Integer endNum){
		primes = new ArrayList<Integer>();
		this.start = startNum;
		this.end = endNum;
	}
	/**
	 * Returns the list of all primes in the interval
	 * @return List<Integer> primes
	 */
	public List<Integer> getPrimesList(){
		return primes;
	}
	/**
	 * Return if n is prime
	 * @param n
	 * @return Boolean
	 */
	public Boolean isPrime(int n){
		// Even check
		if(n == 2)
			return true;
		if(n % 2 == 0){
			return false;
		}
		for(int i = 3; i*i <= n; i+=2){
			if(n % i == 0){
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Populates the attribute primes by adding prime numbers
	 * in the interval [start, end)
	 * @return
	 */
	@Override
	public void run() {
		//System.out.println(this.start +":"+ this.end);
		for(int i = this.start; i < this.end; ++i){
			if (isPrime(i)){
				//System.out.println("hfd");
				primes.add(new Integer(i));
			}
		}
	}
	
}
