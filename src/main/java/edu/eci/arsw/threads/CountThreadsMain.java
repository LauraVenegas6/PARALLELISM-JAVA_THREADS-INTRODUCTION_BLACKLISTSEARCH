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
        CountThread countThread1 = new CountThread(5, 10);
        countThread1.start();
    }
    
}
