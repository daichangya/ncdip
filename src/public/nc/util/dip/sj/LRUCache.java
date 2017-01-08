package nc.util.dip.sj;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

/**
*
*/
public class LRUCache<K,V> {

private static final float   hashTableLoadFactor = 0.75f;

private LinkedHashMap<K,V>   map;
private int                  cacheSize;
private Entry<K, V>                  eldestKey;

/**
* Creates a new LRU cache.
* @param cacheSize the maximum number of entries that will be kept in this cache.
*/
public LRUCache (int cacheSize) {
   this.cacheSize = cacheSize;
   int hashTableCapacity = (int)Math.ceil(cacheSize / hashTableLoadFactor) + 1;
   map = new LinkedHashMap<K,V>(hashTableCapacity, hashTableLoadFactor, true) {
      // (an anonymous inner class)
      private static final long serialVersionUID = 1;
      @Override protected boolean removeEldestEntry (Map.Entry<K,V> eldest) {
    	  eldestKey=eldest;
         return size() > LRUCache.this.cacheSize; }}; }

/**
* Retrieves an entry from the cache.<br>
* The retrieved entry becomes the MRU (most recently used) entry.
* @param key the key whose associated value is to be returned.
* @return    the value associated to this key, or null if no value with this key exists in the cache.
*/
public synchronized V get (K key) {
   return map.get(key); }

/**
* Adds an entry to this cache.
* If the cache is full, the LRU (least recently used) entry is dropped.
* @param key    the key with which the specified value is to be associated.
* @param value  a value to be associated with the specified key.
*/
public synchronized void put (K key, V value) {
	System.out.println("#############put"+key.toString());
	System.out.println("#############Key"+map.keySet().size());
   map.put (key,value); }

/**
* Clears the cache.
*/
public synchronized void clear() {
   map.clear(); }
public synchronized void remove(K key) {
	System.out.println("#############remove"+key.toString());
	System.out.println("#############remove"+map.keySet().size());
	   map.remove(key); 
	   }
public synchronized Set<K>  keySet( ) {
	   return map.keySet(); }


/**
* Returns the number of used entries in the cache.
* @return the number of entries currently in the cache.
*/
public synchronized int usedEntries() {

   return map.size(); }

public static void main (String[] args) {
          LRUCache<String,String> c = new LRUCache<String,String>(3);
          c.put ("1","one");                            // 1
          c.put ("2","two");                            // 2 1
          c.put ("3","three");                          // 3 2 1
          c.put ("4","four");                           // 4 3 2
          if (c.get("2")==null) throw new Error();      // 2 4 3
          c.put ("5","five");                           // 5 2 4
          c.put ("4","second four");                    // 4 5 2
          // Verify cache content.
          if (c.usedEntries() != 3)              throw new Error();
          if (!c.get("4").equals("second four")) throw new Error();
          if (!c.get("5").equals("five"))        throw new Error();
          if (!c.get("2").equals("two"))         throw new Error();
          // List cache content.
          for (Map.Entry<String,String> e : c.getAll())
             System.out.println (e.getKey() + " : " + e.getValue()); }



/**
* Returns a <code>Collection</code> that contains a copy of all cache entries.
* @return a <code>Collection</code> with a copy of the cache content.
*/
public synchronized Collection<Map.Entry<K,V>> getAll() {
   return new ArrayList<Map.Entry<K,V>>(map.entrySet()); }

public int getCacheSize() {
	return cacheSize;
}

public Entry<K, V> getEldestKey() {
	return eldestKey;
}

public void setEldestKey(Entry<K, V> eldestKey) {
	this.eldestKey = eldestKey;
}

} // end class LRUCache