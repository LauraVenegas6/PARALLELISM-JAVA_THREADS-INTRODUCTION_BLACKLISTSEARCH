package edu.eci.arsw.threads;

import edu.eci.arsw.spamkeywordsdatasource.HostBlacklistsDataSourceFacade;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class BlackListThread extends Thread {
    
    private int startIndex;
    private int endIndex;
    private String ipAddress;
    private AtomicInteger occurrencesFound;
    private List<Integer> blackListOccurrences;
    private HostBlacklistsDataSourceFacade skds;
    private AtomicInteger globalOccurrences;
    private static final int BLACK_LIST_ALARM_COUNT = 5;
    
    public BlackListThread(int startIndex, int endIndex, String ipAddress, AtomicInteger globalOccurrences) {
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        this.ipAddress = ipAddress;
        this.occurrencesFound = new AtomicInteger(0);
        this.blackListOccurrences = new LinkedList<>();
        this.skds = HostBlacklistsDataSourceFacade.getInstance();
        this.globalOccurrences = globalOccurrences;
    }
    
    @Override
    public void run() {
        for (int i = startIndex; i < endIndex; i++) {
            
            if (globalOccurrences.get() >= BLACK_LIST_ALARM_COUNT) {
                break; 
            }
            
            if (skds.isInBlackListServer(i, ipAddress)) {
                blackListOccurrences.add(i);
                occurrencesFound.incrementAndGet();
                globalOccurrences.incrementAndGet(); 
            }
        }
    }
    
    public int getOccurrencesFound() {
        return occurrencesFound.get();
    }
    
    public List<Integer> getBlackListOccurrences() {
        return blackListOccurrences;
    }
    
    public int getStartIndex() {
        return startIndex;
    }
    
    public int getEndIndex() {
        return endIndex;
    }
}
