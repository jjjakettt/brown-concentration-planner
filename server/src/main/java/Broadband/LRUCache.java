package Broadband;

import java.util.LinkedHashMap;
import java.util.Map;

public class LRUCache<K, V> extends LinkedHashMap<K, V> {
  private final int maxSize;

  /**
   * Constructs an LRUCache with a specified maximum size. The cache automatically evicts the least
   * recently used entry when the size limit is exceeded.
   *
   * @param maxSize the maximum number of entries the cache can hold.
   */
  public LRUCache(int maxSize) {
    super(maxSize, 0.75f, true); // 'true' for access-order
    this.maxSize = maxSize;
  }

  /**
   * Determines whether the eldest entry in the cache should be removed based on the cache size.
   *
   * @param eldest the eldest entry in the cache.
   * @return true if the cache size exceeds the maximum size, causing the eldest entry to be
   *     removed.
   */
  @Override
  protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
    return size() > maxSize;
  }
}
