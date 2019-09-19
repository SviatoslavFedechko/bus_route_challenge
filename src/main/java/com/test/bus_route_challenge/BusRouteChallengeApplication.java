package com.test.bus_route_challenge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.*;
import java.util.Random;

@SpringBootApplication
public class BusRouteChallengeApplication {

    public static void main(String[] args) {
        SpringApplication.run(BusRouteChallengeApplication.class, args);
        System.out.println("hello");

        readFromFile();

//        writeToFile();
    }

	private static void readFromFile() {
		try {

			File file = new File("C:\\Sviat/testout.txt");
			BufferedReader br = new BufferedReader(new FileReader(file));
			String st;
			while ((st = br.readLine()) != null)
				System.out.println(st);
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	private static void writeToFile() {
        try {
            FileWriter fw = new FileWriter("C:\\Sviat/testout.txt");
            fw.write("100 000");
            Random rand = new Random();
            fw.write(System.lineSeparator());
            for (int i = 1; i <= 100000; i++) {
                fw.write(i + " ");
                for (int j = 1; j <= 1000; j++) {
                    fw.write(j + rand.nextInt(500) + " ");
                }
                fw.write(System.lineSeparator());
                fw.write(System.lineSeparator());
            }

            fw.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        System.out.println("Success...");
    }

}
