package edu.eci.arsw.threads;

import edu.eci.arsw.spamkeywordsdatasource.HostBlacklistsDataSourceFacade;
import java.util.LinkedList;
import java.util.List;

public class BlackListThread extends Thread {
    
    private int startIndex;
    private int endIndex;
    private String ipAddress;
    private int occurrencesFound;
    private List<Integer> blackListOccurrences;
    private HostBlacklistsDataSourceFacade skds;
    
    public BlackListThread(int startIndex, int endIndex, String ipAddress) {
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        this.ipAddress = ipAddress;
        this.occurrencesFound = 0;
        this.blackListOccurrences = new LinkedList<>();
        this.skds = HostBlacklistsDataSourceFacade.getInstance();
    }
    
    @Override
    public void run() {
        for (int i = startIndex; i < endIndex; i++) {
            if (skds.isInBlackListServer(i, ipAddress)) {
                blackListOccurrences.add(i);
                occurrencesFound++;
            }
        }
    }
    
    public int getOccurrencesFound() {
        return occurrencesFound;
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
