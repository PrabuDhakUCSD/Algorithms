package strings;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/*
 * Consistent hashing places a set of machines in a circular ring of range
 * [0,1) based on the hash of machine Id. Then it accepts a keys and then places
 * them in the right place in the ring. 
 * 
 * For each key, hashvalue is calculated to be in the range [0,1) and then the
 * first machine with higher hashvalue accepts the key.
 */

class NameHash implements Comparable<NameHash> {
    String name;
    double hashValue;
    
    NameHash(String name, double hashValue) {
        this.name = name;
        this.hashValue = hashValue;
    }
    
    @Override
    public int compareTo(NameHash other) {
        double diff = this.hashValue - other.hashValue;
        if (diff == 0)
            return 0;
        if (diff < 0)
            return -1;
        return 1;
    }
    
    @Override
    public String toString() {
        return String.format("[%s, %f]", name, hashValue);
    }
}
public class ConsistentHashing {

    static BufferedReader br = new BufferedReader(
            new InputStreamReader(System.in));
    
    static double getHash(String key) {
        int hashValue = key.hashCode() & 0x7fffffff;
        // System.out.println(hashValue);
        return (hashValue / (double) Integer.MAX_VALUE) % Integer.MAX_VALUE;
    }

    static String readString() {
        String input = "";
        try {
            input = br.readLine();
        } catch(Exception ex) {
            ex.printStackTrace();
        }
        
        return input;
    }
    
    static int readInt() {
        return getInt(readString());
    }
    
    static int getInt(String str) {
        return Integer.parseInt(str);
    }
    
    List<NameHash> machines;
    public ConsistentHashing(int numMachines, int numReplicas) {
        machines = new ArrayList<NameHash>();
        String name = "";
        for (int i=0; i<numMachines; i++) {
            for (int j=0; j<numReplicas; j++) {
                name = String.format("%d_Machine_%d_Replica", i, j);
                machines.add(new NameHash(name, getHash(name)));
            }
        }
        
        Collections.sort(machines);
    }
    
    String getMachineForKey(String key) {
        double keyHash = getHash(key);
        System.out.println("Hash for key: " + keyHash);
        int start=0, end = machines.size()-1;
        String machineId = "";
        
        while(start <= end) {
            int mid = start + (end-start)/2;
            NameHash machine = machines.get(mid);
            
            if (keyHash == machine.hashValue)
                return machine.name;
            
            if (keyHash < machine.hashValue) {
                machineId = machine.name;
                end = mid-1;
            } else {
                start = mid+1;
            }
        }
        
        if (machineId.isEmpty()) {
            machineId = machines.get(0).name;
        }
       
        return machineId;
    }
    
    void printList() {
        for (NameHash machine : machines) 
            System.out.println(machine.toString());
    }
    
    public static void main(String[] args) {
        System.out.println("Enter num machines: ");
        int numMachines = readInt();
        System.out.println("Enter num replicas: ");
        int numReplicas = readInt();
        
        ConsistentHashing ch = new ConsistentHashing(numMachines, numReplicas);
        ch.printList();
        
        String key;
        System.out.println("Enter key one by one...");
        while(!(key = readString()).isEmpty()) {
            System.out.println(ch.getMachineForKey(key));
        }
    }
}