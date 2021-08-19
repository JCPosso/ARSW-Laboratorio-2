package edu.eci.arsw.primefinder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class Main {

	public static void main(String[] args) throws InterruptedException, IOException {
		Integer p = 30000000/3;
		ArrayList<PrimeFinderThread> threads = new ArrayList<>();
		Thread t = new Thread();
		for (int i=1;i<=4;i++){
			threads.add (new PrimeFinderThread(p*(i), p*(i+1)));
		};
		long start = System.currentTimeMillis();
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		while (System.currentTimeMillis() - start > 5000) {
			System.out.println(br.read());
			System.out.println("hhhhhhhhhhhh"+(System.currentTimeMillis() - start));
			if (br.read()=='\n'){
				System.out.println("here");
				System.out.println("continue");
			}
		}
	}
	
}
