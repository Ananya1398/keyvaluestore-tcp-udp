import java.util.concurrent.ConcurrentHashMap;

/**
 * The KeyValueStoreOperations class implements the KeyValueStoreOperation interface
 * to provide basic key-value store operations such as put, get, delete, and containsKey.
 * This class uses a ConcurrentHashMap to represent the key-value store.
 */
public class KeyValueStoreOperations implements KeyValueStoreOperation {
  private final ConcurrentHashMap<String, String> KeyValueStore = new ConcurrentHashMap<>();

  /**
   * Puts a key-value pair into the key-value store.
   * @param key The key to store.
   * @param value The value to associate with the key.
   */
  @Override
  public void put(String key, String value) {
    KeyValueStore.put(key, value);
  }

  /**
   * Retrieves the value associated with the specified key from the key-value store.
   * @param key The key whose associated value is to be retrieved.
   * @return The value associated with the key, or null if the key is not found.
   */
  @Override
  public String get(String key) {
    return KeyValueStore.get(key);
  }

  /**
   * Deletes the key-value pair associated with the specified key.
   * @param key The key to delete from the store.
   * @return The value associated with the key that was removed, or null if the key does not exist.
   */
  @Override
  public String delete(String key) {
    return KeyValueStore.remove(key);
  }

  /**
   * Checks if the specified key exists in the key-value store.
   * @param key The key to check for in the store.
   * @return true if the store contains the key, false otherwise.
   */
  @Override
  public boolean containsKey(String key) {
    return KeyValueStore.containsKey(key);
  }
}

