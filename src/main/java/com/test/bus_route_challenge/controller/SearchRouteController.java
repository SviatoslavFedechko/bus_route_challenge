package com.test.bus_route_challenge.controller;

import com.test.bus_route_challenge.model.DirectBusRouteResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Random;

@RestController
@RequestMapping("/api")
public class SearchRouteController {

    /**
     * Is direct bus route present for requested parameters.
     *
     * @return the DirectBusRouteResponse json object
     */
    @GetMapping("/direct")
    public DirectBusRouteResponse isDirectBusRouteExist(@RequestParam("dep_sid") Long dep_sid,
                                                        @RequestParam("arr_sid") Long arr_sid) {

//        readFromFile();
//        writeToFile();
        return new DirectBusRouteResponse(dep_sid, arr_sid, true);
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
