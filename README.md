# Step 1
Modify the redis.conf, set `notify-keyspace-events "EX"`. More other options could be found in the redis.conf file.
```text
 #  K     Keyspace events, published with __keyspace@<db>__ prefix.
 #  E     Keyevent events, published with __keyevent@<db>__ prefix.
 #  g     Generic commands (non-type specific) like DEL, EXPIRE, RENAME, ...
 #  $     String commands
 #  l     List commands
 #  s     Set commands
 #  h     Hash commands
 #  z     Sorted set commands
 #  x     Expired events (events generated every time a key expires)
 #  e     Evicted events (events generated when a key is evicted for maxmemory)
 #  A     Alias for g$lshzxe, so that the "AKE" string means all the events.
```
Note that, <db> is range from 0 to 15.

# Step 2
Startup the current application.

# More Thinking
The keyevent could not return any value to clients, only keys. If we want to handle the value corresponding the key which is expired.
We could copy keys which will be expired, maybe named `copy:expire:{key}`. Then set the key with a expire time, set copy-key without a expire time.
When we receive key-expired-events, we could get values by copy-keys. After handling values, manual delete copy-keys.