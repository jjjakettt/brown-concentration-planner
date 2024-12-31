package GeneralAPIData;

import Broadband.LRUCache;

public class GeneralCacheProxyDatasource {
  private final LRUCache<String, GeneralEntry> cache;
  private final GeneralCensusAPIAdapter acsAdapter;
  private String input_variable;

  public GeneralCacheProxyDatasource(int cacheSize, String input_variable) {
    this.cache = new LRUCache<String, GeneralEntry>(cacheSize);
    this.acsAdapter = new GeneralCensusAPIAdapter(input_variable);
    this.input_variable = input_variable;
  }

  /**
   * Retrieves broadband data for a specific key from the cache. If the data is not in the cache, it
   * fetches it from the ACS API and stores it in the cache.
   *
   * @param key the key representing the state and county to fetch broadband data for.
   * @return the BroadbandEntry object containing the data for the specified key.
   * @throws Exception if there is an issue with fetching data from the ACS API.
   */
  public GeneralEntry getBroadbandData(String key) throws Exception {
    // Validate input: throw IllegalArgumentException if key is null or empty
    if (key == null || key.trim().isEmpty()) {
      throw new IllegalArgumentException("Key cannot be null or empty");
    }

    // Proceed with the rest of the method after validation
    System.out.println("Checking cache for key: " + key);
    GeneralEntry cachedData = cache.get(key);
    System.out.println("Cache returned: " + cachedData);

    if (cachedData != null) {
      System.out.println("Cache hit for key: " + key);
      return cachedData;
    }

    // If not in cache, fetch from ACS
    System.out.println("Cache miss for key: " + key + ". Fetching from ACS.");
    GeneralEntry fetchedData = acsAdapter.fetchData(key);
    System.out.println("Fetched from ACS: " + fetchedData);

    // Insert fetched data into cache
    if (fetchedData != null) {
      System.out.println("Inserting into cache: Key = " + key + ", Data = " + fetchedData);
      cache.put(key, fetchedData);
    }

    return fetchedData;
  }

  /**
   * Returns the current state of the cache.
   *
   * @return the LRUCache storing the broadband data.
   */
  public LRUCache<String, GeneralEntry> getCache() {
    return this.cache;
  }
}
