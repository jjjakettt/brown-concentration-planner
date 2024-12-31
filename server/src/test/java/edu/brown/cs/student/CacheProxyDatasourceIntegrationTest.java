package edu.brown.cs.student;

import static org.junit.Assert.*;

import Broadband.BroadbandEntry;
import Broadband.CacheProxyDatasource;
import Broadband.CensusAPIAdapter;
import Broadband.LRUCache;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class CacheProxyDatasourceIntegrationTest {

  private CacheProxyDatasource cacheProxyDatasource;

  @Before
  public void setUp() {
    // Set up with a small cache size for testing eviction policy
    cacheProxyDatasource = new CacheProxyDatasource(2);
  }

  /**
   * Tests the behavior of the cache during cache hits and misses. It simulates fetching data for
   * two different keys and checks whether the same data is retrieved from the cache on subsequent
   * requests. Verifies that cache hits are correctly handled using mocked data.
   *
   * @throws Exception if data retrieval fails
   */
  @Test
  public void testCacheHitAndMiss() throws Exception {
    // Mock the data retrieval method
    CensusAPIAdapter mockAdapter = Mockito.mock(CensusAPIAdapter.class);
    LRUCache<String, BroadbandEntry> mockCache = Mockito.mock(LRUCache.class);

    // Simulate a broadband entry for cache
    BroadbandEntry entry1 = new BroadbandEntry("California", "Butte County", 83.5);

    // Mocking behavior: Cache miss on first call, cache hit on second call
    Mockito.when(mockCache.get("California,Butte County")).thenReturn(null, entry1);
    Mockito.when(mockAdapter.fetchData("California,Butte County")).thenReturn(entry1);

    // Inject the mocks into the datasource
    CacheProxyDatasource cacheProxyDatasource = new CacheProxyDatasource(mockCache, mockAdapter);

    // First call should fetch from the ACS
    BroadbandEntry result1 = cacheProxyDatasource.getBroadbandData("California,Butte County");

    // Second call should hit the cache
    BroadbandEntry cachedResult = cacheProxyDatasource.getBroadbandData("California,Butte County");

    // Verify results
    Assert.assertSame(entry1, result1);
    Assert.assertSame(entry1, cachedResult); // Cached result should be the same

    // Verify that the cache was accessed and ACS was called once
    Mockito.verify(mockCache, Mockito.times(2)).get("California,Butte County");
    Mockito.verify(mockAdapter, Mockito.times(1)).fetchData("California,Butte County");
  }

  /**
   * Tests the Least Recently Used (LRU) eviction policy of the cache. Fills the cache to its
   * maximum capacity, then adds another entry to trigger an eviction. Verifies that the least
   * recently used entry is correctly evicted from the cache using mocked data.
   *
   * @throws Exception if eviction behavior is incorrect
   */
  @Test
  public void testLRUEvictionPolicy() throws Exception {
    // Mock the LRUCache and CensusAPIAdapter
    LRUCache<String, BroadbandEntry> cache = new LRUCache<String, BroadbandEntry>(2);
    CensusAPIAdapter mockAdapter = Mockito.mock(CensusAPIAdapter.class);

    // Simulate data fetching from the ACS API
    BroadbandEntry entry1 = new BroadbandEntry("California", "Butte County", 83.5);
    BroadbandEntry entry2 = new BroadbandEntry("California", "Alameda County", 90.0);
    BroadbandEntry entry3 = new BroadbandEntry("Texas", "Bexar County", 75.0);

    Mockito.when(mockAdapter.fetchData("California,Butte County")).thenReturn(entry1);
    Mockito.when(mockAdapter.fetchData("California,Alameda County")).thenReturn(entry2);
    Mockito.when(mockAdapter.fetchData("Texas,Bexar County")).thenReturn(entry3);

    CacheProxyDatasource datasource = new CacheProxyDatasource(cache, mockAdapter);

    // Fill the cache
    datasource.getBroadbandData("California,Butte County");
    datasource.getBroadbandData("California,Alameda County");

    // Adding a third entry should evict the least recently used one
    datasource.getBroadbandData("Texas,Bexar County");

    // Verify that the LRU eviction works correctly
    assertNull(cache.get("California,Butte County")); // Should be evicted
    assertNotNull(cache.get("California,Alameda County")); // Should still be in cache
    assertNotNull(cache.get("Texas,Bexar County")); // Should be in cache
  }

  /**
   * Tests automatic caching of data fetched from the ACS using mocked data. Fetches data for a key
   * and checks whether the data is automatically cached after retrieval. Ensures that the cache is
   * populated after the first fetch and that the data is stored correctly.
   *
   * @throws Exception if caching behavior is incorrect
   */
  @Test
  public void testAutomaticCaching() throws Exception {
    String key = "Texas,Bexar County";

    // Mock the cache and the adapter
    LRUCache<String, BroadbandEntry> mockCache = Mockito.mock(LRUCache.class);
    CensusAPIAdapter mockAdapter = Mockito.mock(CensusAPIAdapter.class);

    // Simulate data fetching
    BroadbandEntry entry = new BroadbandEntry("Texas", "Bexar County", 75.0);
    Mockito.when(mockAdapter.fetchData(key)).thenReturn(entry);

    // Inject the mocks into the datasource
    CacheProxyDatasource datasource = new CacheProxyDatasource(mockCache, mockAdapter);

    // Fetch data and simulate caching
    BroadbandEntry result = datasource.getBroadbandData(key);

    // Verify that data was fetched and cached
    Mockito.verify(mockCache).put(key, entry);
    Mockito.verify(mockAdapter).fetchData(key);

    // Check that the returned data matches the mock
    assertSame(entry, result);
  }

  /**
   * Tests the cache size configuration and eviction behavior using mocked data. Sets up a cache
   * with a fixed size and verifies that the cache evicts the least recently used entry when adding
   * a new entry beyond its capacity. This test ensures that when the cache size limit is reached,
   * the cache evicts the least recently used entry to make room for a new entry, adhering to the
   * LRU policy.
   */
  @Test
  public void testCacheSizeConfiguration() {
    // Create a cache with a capacity of 2
    LRUCache<String, BroadbandEntry> smallCache = new LRUCache<String, BroadbandEntry>(2);

    // Mock the BroadbandEntry objects
    BroadbandEntry entry1 = Mockito.mock(BroadbandEntry.class);
    BroadbandEntry entry2 = Mockito.mock(BroadbandEntry.class);
    BroadbandEntry entry3 = Mockito.mock(BroadbandEntry.class);

    // Add two entries to the cache
    smallCache.put("Texas,Bexar County", entry1);
    smallCache.put("California,Butte County", entry2);

    // Assert that the cache contains exactly 2 entries
    assertEquals("Cache should have exactly 2 entries", 2, smallCache.size());

    // Adding a new entry should trigger the eviction of the least recently used entry
    smallCache.put("California,Alameda County", entry3);

    // Verify that the cache still contains only 2 entries
    assertEquals("Cache should still only have 2 entries after eviction", 2, smallCache.size());

    // Verify that the least recently used entry ("Texas,Bexar County") was evicted
    assertNull(
        "\"Texas,Bexar County\" should have been evicted but was found in the cache",
        smallCache.get("Texas,Bexar County"));

    // Verify that the other entries are present
    assertNotNull(
        "\"California,Butte County\" should still be in the cache",
        smallCache.get("California,Butte County"));
    assertNotNull(
        "\"California,Alameda County\" should be in the cache after addition",
        smallCache.get("California,Alameda County"));
  }

  /**
   * Tests behavior of the cache when a cache hit occurs. Ensures that when data is found in the
   * cache, it is returned without querying the ACS. Verifies that the cache returns the correct
   * cached data.
   *
   * @throws Exception if cache hit behavior is incorrect
   */
  @Test
  public void testCacheHit() throws Exception {
    // Mock the LRUCache and CensusAPIAdapter
    LRUCache<String, BroadbandEntry> mockCache = Mockito.mock(LRUCache.class);
    CensusAPIAdapter mockAdapter = Mockito.mock(CensusAPIAdapter.class);

    // Create a mock BroadbandEntry to simulate cached data
    BroadbandEntry cachedData = new BroadbandEntry("California", "Butte County", 83.5);

    // Ensure that the cache returns the cachedData when queried
    Mockito.when(mockCache.get("California,Butte County")).thenReturn(cachedData);

    // Inject the mocks directly into CacheProxyDatasource
    CacheProxyDatasource datasource = new CacheProxyDatasource(mockCache, mockAdapter);

    // Call getBroadbandData and ensure it returns the cached data
    BroadbandEntry result = datasource.getBroadbandData("California,Butte County");

    // Print debug info to verify cache interaction
    System.out.println("Retrieved from cache: " + result);

    // Verify that the returned result is the same as the cached data
    Assert.assertNotNull(result); // Ensure that the result is not null
    Assert.assertEquals(cachedData, result);

    // Ensure that the CensusAPIAdapter was never called (because we hit the cache)
    Mockito.verify(mockAdapter, Mockito.never()).fetchData(Mockito.anyString());
  }

  /**
   * Tests that the mocked cache behaves as expected. Ensures that when querying the mock cache, the
   * mocked data is returned. This test verifies that the mocking setup is correct and returns the
   * expected data.
   */
  @Test
  public void testCacheMockWorks() {
    // Mock the LRUCache
    LRUCache<String, BroadbandEntry> mockCache = Mockito.mock(LRUCache.class);

    // Create a mock BroadbandEntry
    BroadbandEntry cachedData = new BroadbandEntry("California", "Butte County", 83.5);

    // Ensure that the cache returns the cachedData when queried
    Mockito.when(mockCache.get("California,Butte County")).thenReturn(cachedData);

    // Call the mock cache directly and print the result
    BroadbandEntry result = mockCache.get("California,Butte County");
    System.out.println("Retrieved from mock cache: " + result);

    // Verify that the correct data is returned
    Assert.assertNotNull(result); // Should not be null
    Assert.assertEquals(cachedData, result);
  }

  /**
   * Tests cache behavior when there is a cache miss and the data needs to be fetched from the ACS.
   * Simulates a cache miss scenario where data is retrieved from the ACS and then stored in the
   * cache. Verifies that the data is correctly fetched and inserted into the cache.
   *
   * @throws Exception if cache miss handling or fetching behavior fails
   */
  @Test
  public void testCacheMissWithFetch() throws Exception {
    // Mock the cache and adapter
    LRUCache<String, BroadbandEntry> mockCache = Mockito.mock(LRUCache.class);
    CensusAPIAdapter mockAdapter = Mockito.mock(CensusAPIAdapter.class);

    // Simulate cache miss
    Mockito.when(mockCache.get("California,Butte County")).thenReturn(null);

    // Simulate fetching data from adapter
    BroadbandEntry fetchedData = new BroadbandEntry("California", "Butte County", 83.5);
    Mockito.when(mockAdapter.fetchData("California,Butte County")).thenReturn(fetchedData);

    // Inject the mocks into CacheProxyDatasource
    CacheProxyDatasource datasource = new CacheProxyDatasource(2);
    Field cacheField = CacheProxyDatasource.class.getDeclaredField("cache");
    cacheField.setAccessible(true);
    cacheField.set(datasource, mockCache);

    Field adapterField = CacheProxyDatasource.class.getDeclaredField("acsAdapter");
    adapterField.setAccessible(true);
    adapterField.set(datasource, mockAdapter);

    // Call method and assert data is fetched from the adapter and cached
    BroadbandEntry result = datasource.getBroadbandData("California,Butte County");
    Assert.assertEquals(fetchedData, result);

    // Verify that the adapter was called once
    Mockito.verify(mockAdapter, Mockito.times(1)).fetchData("California,Butte County");

    // Verify that the data was stored in the cache
    Mockito.verify(mockCache, Mockito.times(1)).put("California,Butte County", fetchedData);
  }

  /**
   * Tests eviction behavior when the cache reaches its size limit. Simulates adding entries to a
   * cache with a fixed size and verifies that the least recently used entry is evicted when a new
   * entry is added.
   *
   * @throws Exception if eviction does not occur as expected
   */
  @Test
  public void testCacheEviction() throws Exception {
    // Create a cache with a maximum size of 2
    LRUCache<String, BroadbandEntry> cache = new LRUCache<String, BroadbandEntry>(2);

    // Add two entries to the cache
    cache.put("California,Butte County", new BroadbandEntry("California", "Butte County", 83.5));
    cache.put(
        "California,Alameda County", new BroadbandEntry("California", "Alameda County", 82.0));

    // Add a third entry, causing an eviction
    cache.put("Texas,Bexar County", new BroadbandEntry("Texas", "Bexar County", 75.3));

    // Verify that the least recently used entry was evicted
    Assert.assertNull(cache.get("California,Butte County"));
    Assert.assertNotNull(cache.get("California,Alameda County"));
    Assert.assertNotNull(cache.get("Texas,Bexar County"));
  }

  /**
   * Tests the behavior of the system when both the cache and the ACS API fail. Simulates a scenario
   * where data is not present in the cache and the ACS API request also fails, ensuring that the
   * system handles these failures gracefully and throws appropriate exceptions. Verifies that no
   * data is inserted into the cache when both the cache and ACS fail.
   *
   * @throws Exception if the system does not handle cache and API failures correctly
   */
  @Test
  public void testCacheAndAcsFailure() throws Exception {
    // Mock the LRUCache and CensusAPIAdapter
    LRUCache<String, BroadbandEntry> mockCache = Mockito.mock(LRUCache.class);
    CensusAPIAdapter mockAdapter = Mockito.mock(CensusAPIAdapter.class);

    // Simulate cache miss and ACS failure
    Mockito.when(mockCache.get("California,Butte County")).thenReturn(null);
    Mockito.when(mockAdapter.fetchData("California,Butte County"))
        .thenThrow(new RuntimeException("ACS Fetch Failed"));

    // Inject the mocks directly into CacheProxyDatasource
    CacheProxyDatasource datasource = new CacheProxyDatasource(mockCache, mockAdapter);

    try {
      // Call getBroadbandData and expect an exception
      datasource.getBroadbandData("California,Butte County");
      Assert.fail("Expected an exception to be thrown");
    } catch (RuntimeException e) {
      // Verify that the correct exception is thrown
      Assert.assertEquals("ACS Fetch Failed", e.getMessage());
    }

    // Ensure that the cache was queried but no data was stored
    Mockito.verify(mockCache).get("California,Butte County");
    Mockito.verify(mockCache, Mockito.never())
        .put(Mockito.anyString(), Mockito.any(BroadbandEntry.class));
  }

  /**
   * Tests the real integration with the ACS API by fetching live data. This test bypasses mock data
   * and uses the actual ACS API to ensure that the system interacts correctly with the real-world
   * datasource. Verifies that the data is retrieved and cached correctly after a successful API
   * call. Note: This test should be run sparingly as it relies on external data availability and
   * network conditions.
   *
   * @throws Exception if the API request or caching behavior fails
   */
  @Test
  public void testRealDataIntegration() throws Exception {
    // Create an actual CensusAPIAdapter and LRUCache
    CensusAPIAdapter realAdapter = new CensusAPIAdapter();
    LRUCache<String, BroadbandEntry> realCache = new LRUCache<String, BroadbandEntry>(2);

    // Create the CacheProxyDatasource with real implementations
    CacheProxyDatasource datasource = new CacheProxyDatasource(realCache, realAdapter);

    // Call getBroadbandData and fetch real data
    BroadbandEntry result = datasource.getBroadbandData("California,Butte County");

    // Assert that real data is fetched and cached
    Assert.assertNotNull(result);
    Assert.assertEquals("California", result.state);
    Assert.assertEquals("Butte County", result.county);

    // Ensure that the data is now stored in the cache
    Assert.assertEquals(result, realCache.get("California,Butte County"));
  }

  /**
   * Tests cache behavior with invalid inputs. Specifically tests with null and empty string inputs
   * to ensure that the method throws IllegalArgumentException as expected. Ensures that no data is
   * cached or retrieved when invalid inputs are provided.
   *
   * @throws Exception if invalid input handling fails
   */
  @Test
  public void testInvalidInputs() throws Exception {
    // Mock the LRUCache and CensusAPIAdapter
    LRUCache<String, BroadbandEntry> mockCache = Mockito.mock(LRUCache.class);
    CensusAPIAdapter mockAdapter = Mockito.mock(CensusAPIAdapter.class);

    // Inject the mocks into CacheProxyDatasource
    CacheProxyDatasource datasource = new CacheProxyDatasource(mockCache, mockAdapter);

    // Test with null input
    try {
      datasource.getBroadbandData(null);
      Assert.fail("Expected an IllegalArgumentException for null input");
    } catch (IllegalArgumentException e) {
      Assert.assertEquals("Key cannot be null or empty", e.getMessage());
    }

    // Test with empty string input
    try {
      datasource.getBroadbandData("");
      Assert.fail("Expected an IllegalArgumentException for empty input");
    } catch (IllegalArgumentException e) {
      Assert.assertEquals("Key cannot be null or empty", e.getMessage());
    }

    // Ensure that no data was retrieved or cached
    Mockito.verify(mockCache, Mockito.never()).get(Mockito.anyString());
    Mockito.verify(mockAdapter, Mockito.never()).fetchData(Mockito.anyString());
  }

  /**
   * Tests concurrent access to the cache from multiple threads. Simulates fetching data from the
   * ACS using multiple threads and verifies that the cache behaves correctly under concurrent
   * access. Ensures that the cache remains consistent and does not lose data.
   *
   * @throws Exception if concurrency causes issues with cache consistency
   */
  @Test
  public void testCacheConcurrency() throws Exception {
    // Create a real LRUCache with a small size
    LRUCache<String, BroadbandEntry> realCache = new LRUCache<String, BroadbandEntry>(2);
    CensusAPIAdapter mockAdapter = Mockito.mock(CensusAPIAdapter.class);

    // Create the CacheProxyDatasource with the real cache
    CacheProxyDatasource datasource = new CacheProxyDatasource(realCache, mockAdapter);

    // Simulate fetching data from ACS (mocked for concurrency)
    Mockito.when(mockAdapter.fetchData(Mockito.anyString()))
        .thenReturn(new BroadbandEntry("California", "Butte County", 83.5));

    // Create multiple threads to access the cache concurrently
    ExecutorService executor = Executors.newFixedThreadPool(5);
    List<Future<BroadbandEntry>> futures = new ArrayList<>();

    for (int i = 0; i < 5; i++) {
      futures.add(
          executor.submit(
              () -> {
                return datasource.getBroadbandData("California,Butte County");
              }));
    }

    // Wait for all threads to complete and ensure that the cache behaves correctly
    for (Future<BroadbandEntry> future : futures) {
      BroadbandEntry result = future.get();
      Assert.assertNotNull(result);
      Assert.assertEquals("California", result.state);
      Assert.assertEquals("Butte County", result.county);
    }

    // Ensure that the cache size is correct after concurrency tests
    Assert.assertEquals(1, realCache.size());
    executor.shutdown();
  }
}
