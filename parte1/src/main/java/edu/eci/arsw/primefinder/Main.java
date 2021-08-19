package edu.eci.arsw.primefinder;

import java.io.*;

public class Main {

	public static void main(String[] args) throws InterruptedException {

		PrimeFinderThread pft1 = new PrimeFinderThread(0, 10000000);
		PrimeFinderThread pft2 = new PrimeFinderThread(10000001, 20000000);
		PrimeFinderThread pft3 = new PrimeFinderThread(20000001, 30000000);

		pft1.start();
		pft2.start();
		pft3.start();

		Thread.sleep(5000);

		pft1.pausa();
		pft2.pausa();
		pft3.pausa();

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		try {
			String entrada = br.readLine();
		} catch (Exception e) {
			System.out.println(e);
			e.printStackTrace();

		}

		pft1.reanudar();
		pft2.reanudar();
		pft3.reanudar();

	}

}