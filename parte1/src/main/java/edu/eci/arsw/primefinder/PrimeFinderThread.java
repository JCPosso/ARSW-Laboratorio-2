package edu.eci.arsw.primefinder;

import java.util.LinkedList;
import java.util.List;

public class PrimeFinderThread extends Thread {

	private boolean pausa;
	int a, b;
	Object key;

	private List<Integer> primes = new LinkedList<Integer>();

	public PrimeFinderThread(int a, int b) {
		super();
		this.a = a;
		this.b = b;
		pausa = false;
		key = 1;

	}

	public void run() {

		for (int i = a; i <= b; i++) {
			if (isPrime(i)) {
				primes.add(i);
				System.out.println(i);
			}
			if (pausa) {
				synchronized (key) {
					try {
						key.wait();
					} catch (InterruptedException e) {

						e.printStackTrace();
					}
				}
			}

		}

	}

	boolean isPrime(int n) {
		if (n % 2 == 0)
			return false;
		for (int i = 3; i * i <= n; i += 2) {
			if (n % i == 0)
				return false;
		}
		return true;
	}

	public List<Integer> getPrimes() {
		return primes;
	}

	public void pausa() {
		pausa = true;

	}

	public void reanudar() {
		pausa = false;
		synchronized (key) {
			key.notify();
		}
		;
	}

}