package edu.eci.arsw.blacklistvalidator;

import java.util.List;

public class Main {
    
    public static void main(String a[]){
        
        HostBlackListsValidator hblv=new HostBlackListsValidator();
        
        List<Integer> blackListOcurrences = hblv.checkHost("200.24.34.55", 4);
        System.out.println("The host was found in the following blacklists: " + blackListOcurrences);
        
        blackListOcurrences = hblv.checkHost("202.24.34.55", 8);
        System.out.println("The host was found in the following blacklists: " + blackListOcurrences);
    }
    
}
