/**
 * The KeyValueStoreOperations class implements the KeyValueStoreOperation interface
 * to provide basic key-value store operations such as put, get, delete, and containsKey.
 */
public interface KeyValueStoreOperation {

  /**
   * Puts a key-value pair into the key-value store.
   * @param key The key to store.
   * @param value The value to associate with the key.
   */
  void put(String key, String value);

  /**
   * Retrieves the value associated with the specified key from the key-value store.
   * @param key The key whose associated value is to be retrieved.
   * @return The value associated with the key, or null if the key is not found.
   */
  String get(String key);

  /**
   * Deletes the key-value pair associated with the specified key.
   * @param key The key to delete from the store.
   * @return The value associated with the key that was removed, or null if the key does not exist.
   */
  String delete(String key);

  /**
   * Checks if the specified key exists in the key-value store.
   * @param key The key to check for in the store.
   * @return true if the store contains the key, false otherwise.
   */
  boolean containsKey(String key);
}
