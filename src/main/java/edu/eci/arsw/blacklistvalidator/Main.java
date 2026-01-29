package edu.eci.arsw.blacklistvalidator;

import java.util.List;

public class Main {
    
    public static void main(String a[]){
        performanceTest();
    }
    
    
    public static void performanceTest() {
        String ipDispersa = "202.24.34.55";  
        
   
        int numCores = Runtime.getRuntime().availableProcessors();
        System.out.println("EVALUACIÓN DE DESEMPEÑO");
        System.out.println("Número de núcleos disponibles: " + numCores);
        
      
        int[] threadCounts = {
            1,
            numCores,
            numCores * 2,
            50,
            100
        };
  
        long[] executionTimes = new long[threadCounts.length];
        
        HostBlackListsValidator validator = new HostBlackListsValidator();
      
        for (int i = 0; i < threadCounts.length; i++) {
            int numThreads = threadCounts[i];
            
            System.out.println("Prueba con " + numThreads + " hilos...");
            
    
            long startTime = System.currentTimeMillis();
            
            
            List<Integer> result = validator.checkHost(ipDispersa, numThreads);
            
           
            long endTime = System.currentTimeMillis();
            
          
            long executionTime = endTime - startTime;
            executionTimes[i] = executionTime;
            
            System.out.println("  Tiempo: " + executionTime + " ms");
            System.out.println("  Encontrado en " + result.size() + " listas negras");
            System.out.println();
        }
        
   
        printResults(threadCounts, executionTimes, numCores);
    }
    
  
    private static void printResults(int[] threadCounts, long[] times, int numCores) {
        System.out.println("\nRESULTADOS");
        System.out.println("Hilos | Tiempo (ms) | Mejora");
        System.out.println("------|-------------|--------");
        
        long baselineTime = times[0]; 
        
        for (int i = 0; i < threadCounts.length; i++) {
            double speedup = (double) baselineTime / times[i];
            String speedupStr = String.format("%.2f", speedup);
            
            System.out.printf("%5d | %11d | %s x\n", 
                threadCounts[i], times[i], speedupStr);
        }
        
       
    }
}
