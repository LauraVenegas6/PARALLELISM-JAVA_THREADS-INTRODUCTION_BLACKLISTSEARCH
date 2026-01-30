/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.threads;

/**
 *
 * @author Laura Alejandra Venegas Piraban
 * @author Sergio Alejandro Idarraga Torres
 */
public class CountThreadsMain {
    
    public static void main(String a[]){
        // Parte Prueba
        CountThread countThread1 = new CountThread(5, 10);
        countThread1.start();

        //Parte 2 Nums [0, 99]
        System.out.println("[0, 99] -----------------");
        CountThread countThread2 = new CountThread(0, 99);
        countThread2.run();

        //Parte 3 Nums [99, 199]
        System.out.println("[99, 199] -----------------");
        CountThread countThread3 = new CountThread(99, 199);
        countThread3.run();

        //Parte 4 Nums [200, 299]
        System.out.println("[200, 299] -----------------");
        CountThread countThread4 = new CountThread(200, 299);
        countThread4.run();


        // // Parte Prueba
        // CountThread countThread1 = new CountThread(5, 10);
        // countThread1.start();

        // //Parte 2 Nums [0, 99]
        // System.out.println("[0, 99] -----------------");
        // CountThread countThread2 = new CountThread(0, 99);
        // countThread2.start();

        // //Parte 3 Nums [99, 199]
        // System.out.println("[99, 199] -----------------");
        // CountThread countThread3 = new CountThread(99, 199);
        // countThread3.start();

        // //Parte 4 Nums [200, 299]
        // System.out.println("[200, 299] -----------------");
        // CountThread countThread4 = new CountThread(200, 299);
        // countThread4.start();
    }
    
}
