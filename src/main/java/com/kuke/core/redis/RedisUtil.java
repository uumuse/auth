package com.kuke.core.redis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import redis.clients.jedis.Builder;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Response;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPipeline;
import redis.clients.jedis.ShardedJedisPool;
import redis.clients.jedis.Transaction;
import redis.clients.util.SafeEncoder;

/**
 * 内存数据库Redis的辅助类，负责对内存数据库的所有操作
 * @version V1.0
 * @author 
 */
public class RedisUtil {

	// 数据源
	public ShardedJedisPool shardedJedisPool;

	
	/**
	 * 执行器，{@link com.kuke.core.redis.RedisNewUtil}的辅助类，
	 * 它保证在执行操作之后释放数据源returnResource(jedis)
	 * @version V1.0
	 * @author 
	 * @param <T>
	 */
	abstract class Executor<T> {
		ShardedJedis jedis = null;
		ShardedJedisPool shardedJedisPool = null;
		public Executor(ShardedJedisPool shardedJedisPool) {
			this.shardedJedisPool = shardedJedisPool;
			jedis = this.shardedJedisPool.getResource();
		}

		/**
		 * 回调
		 * @return 执行结果
		 */
		abstract T execute();

		/**
		 * 调用{@link #execute()}并返回执行结果
		 * 它保证在执行{@link #execute()}之后释放数据源returnResource(jedis)
		 * @return 执行结果
		 */
		public T getResult() {
			T result = null;
			try {
				result = execute();
			} catch (Exception e) {
				throw new RuntimeException("Redis execute exception", e);
			} finally {
				if (jedis != null) {
					this.shardedJedisPool.returnResource(jedis);
				}
			}
			return result;
		}
	}

	/**
	 * 删除模糊匹配的key
	 * @param likeKey 模糊匹配的key
	 */
	public long delKeysLike(final String likeKey) {
		return new Executor<Long>(shardedJedisPool) {

			@Override
			Long execute() {
				Collection<Jedis> jedisC = jedis.getAllShards();
				Iterator<Jedis> iter = jedisC.iterator();
				long count = 0;
				while (iter.hasNext()) {
					Jedis _jedis = iter.next();
					Set<String> keys = _jedis.keys(likeKey + "*");
					String[] strKeys = new String[keys.size()];
					keys.toArray(strKeys);
					count += _jedis.del(strKeys);
				}
				return count;
			}
		}.getResult();
	}
	
	/**
	 * 判断key是否存在
	 */
	public boolean containsKey(final String key) {
		return new Executor<Boolean>(shardedJedisPool) {
			@Override
			Boolean execute() {
				boolean existKey = false;
				existKey = jedis.exists(key);
				return existKey;
			}
		}.getResult();
	}
	
	/**
	 * 获取所有key
	 */
	public Set<String> getKeys(final String key) {
		return new Executor<Set<String>>(shardedJedisPool) {
			@Override
			Set<String> execute() {
				Collection<Jedis> jedisC = jedis.getAllShards();
				Iterator<Jedis> iter = jedisC.iterator();
				Set<String> keys = new HashSet<String>();
				while (iter.hasNext()) {
					Jedis _jedis = iter.next();
					keys = _jedis.keys(key);
					keys.addAll(keys);
				}
				return keys;
			}
		}.getResult();
	}

	/**
	 * 一个跨jvm的id生成器，利用了redis原子性操作的特点
	 * @param key id的key
	 * @return 返回生成的Id
	 */
	public long getID(final String key) {
		return new Executor<Long>(shardedJedisPool) {

			@Override
			Long execute() {
				long id = jedis.incr(key);
				if ((id + 75807) == Long.MAX_VALUE) {
					// 避免溢出，重置，getSet命令之前允许incr插队，75807就是预留的插队空间
					jedis.getSet(key, "0");
				}
				return id;
			}
		}.getResult();
	}

	/* ======================================Strings====================================== */

	/**
	 * 设置key对应的值为string类型的value，返回1表示成功，0失败。长度不能超过1073741824 bytes (1 GB).
	 * 时间复杂度: O(1)
	 * @param key key
	 * @param value value
	 * @return 返回1表示成功，0失败
	 */
	public String setString(final String key, final String value) {
		return new Executor<String>(shardedJedisPool) {
			@Override
			String execute() {
				return jedis.set(key, value);
			}
		}.getResult();
	}

	/**
	 * 设置key对应的值为string类型的value，返回1表示成功，0失败。长度不能超过1073741824 bytes (1 GB).
	 * 时间复杂度: O(1)
	 * @param key key
	 * @param value value
	 * @param expireSeconds 超时时间（秒），过期删除
	 * @return 返回1表示成功，0失败
	 */
	public String setString(final String key, final String value, final int expireSeconds) {
		return new Executor<String>(shardedJedisPool) {
			@Override
			String execute() {
				jedis.pipelined(new ShardedJedisPipeline() {
					@Override
					public void execute() {
						set(key, value);
						expire(key, expireSeconds);
					}
				});
				return null;
			}
		}.getResult();
	}

	/**
	 * 设定key有效期
	 * @param key
	 * @param expireSeconds
	 */
	public void setExpire(final String key,final Map<String, String> hash,final int expireSeconds){
		new Executor<String>(shardedJedisPool) {
			@Override
			String execute() {
				jedis.pipelined(new ShardedJedisPipeline() {
					@Override
					public void execute() {
						jedis.hmset(key, hash);
						expire(key, expireSeconds);
					}
				});
				return null;
			}
		}.getResult();
		
	}
	
	
	/**
	 * 设置key对应的值为string类型的value，返回1表示成功，0失败。如果key已经存在，返回0
	 * @param key key
	 * @param value value
	 * @return 返回1表示成功，0失败
	 */
	public String setStringIfNotExists(final String key, final String value) {
		return new Executor<String>(shardedJedisPool) {

			@Override
			String execute() {
				String status = null;
					if (!jedis.exists(key)) {
						jedis.set(key, value);
					}
				return status;
			}
		}.getResult();
	}
	

	/**
	 * 获取key对应的string值，如果key不存在返回nil。
	 *  如果对应的key存储的值不是一个字符串类型，将返回一个错误。
	 * 时间复杂度: O(1)
	 * @param key key
	 * @return value
	 */
	public String getString(final String key) {
		return new Executor<String>(shardedJedisPool) {

			@Override
			String execute() {
				return jedis.get(key);
			}
		}.getResult();
	}

	/**
	 * string的批量更新
	 * @param pairs 键值对数组{数组第一个元素为key，第二个元素为value}
	 */
	public List<Object> batchSetString(final List<Pair<String, String>> pairs) {
		return new Executor<List<Object>>(shardedJedisPool) {

			@Override
			List<Object> execute() {
				return jedis.pipelined(new ShardedJedisPipeline() {

					@Override
					public void execute() {
						for (Pair<String, String> pair : pairs) {
							set(pair.getKey(), pair.getValue());
						}
					}
				});
			}
		}.getResult();
	}

	/**
	 * string的批量获得
	 * @param keys keys
	 * @return values
	 */
	public List<String> batchGetString(final List<String> keys) {
		return new Executor<List<String>>(shardedJedisPool) {

			@Override
			List<String> execute() {
				List<Object> dataList = jedis.pipelined(new ShardedJedisPipeline() {

					@Override
					public void execute() {
						for (String key : keys) {
							get(key);
						}
					}
				});
				List<String> rtnDataList = new ArrayList<String>();
				for (Object data : dataList) {
					rtnDataList.add(STRING.build(data));
				}
				return rtnDataList;
			}
		}.getResult();
	}

	/* ======================================Hashes====================================== */

	/**
	 * 设置hash field为指定值，如果key不存在，则先创建  
	 * 时间复杂度: O(1)
	 * @param key key
	 * @param field field
	 * @param value value
	 * @return 如果field已经存在, {@link #hashSet(String, String, String)}更新value后返回0，否则一个新的field被创建并返回1。
	 */
	public long hashSet(final String key, final String field, final String value) {
		return new Executor<Long>(shardedJedisPool) {

			@Override
			Long execute() {
				return jedis.hset(key, field, value);
			}
		}.getResult();
	}

	/**
	 * 获取指定的hash field
	 * 时间复杂度:O(1)
	 * @param key key
	 * @param field field
	 * @return 指定的hash field
	 */
	public String hashGet(final String key, final String field) {
		return new Executor<String>(shardedJedisPool) {

			@Override
			String execute() {
				return jedis.hget(key, field);
			}
		}.getResult();
	}

	/**
	 * 同时设置hash的多个field。
	 * 时间复杂度: O(N) (N为fields的数量)
	 * @param key key
	 * @param hash hash
	 * @return 成功则返回 OK， hash为空则返回Exception
	 */
	public String hashMultipleSet(final String key, final Map<String, String> hash) {
		return new Executor<String>(shardedJedisPool) {

			@Override
			String execute() {
				return jedis.hmset(key, hash);
			}
		}.getResult();
	}
	
	

	/**
	 * 获取全部指定的hash filed。
	 * 时间复杂度: O(N) (N为fields的数量)
	 * @param key key
	 * @param fields fields
	 * @return 全部指定的hash filed
	 */
	public List<String> hashMultipleGet(final String key, final String... fields) {
		return new Executor<List<String>>(shardedJedisPool) {

			@Override
			List<String> execute() {
				return jedis.hmget(key, fields);
			}
		}.getResult();
	}

	/**
	 * 批量的{@link #hashMultipleSet(String, Map)}，在管道中执行
	 * @param pairs 多个hash的多个field
	 * @return 执行结果
	 */
	public List<Object> batchHashMultipleSet(final List<Pair<String, Map<String, String>>> pairs) {
		return new Executor<List<Object>>(shardedJedisPool) {

			@Override
			List<Object> execute() {
				return jedis.pipelined(new ShardedJedisPipeline() {

					@Override
					public void execute() {
						for (Pair<String, Map<String, String>> pair : pairs) {
							hmset(pair.getKey(), pair.getValue());
						}
					}
				});
			}
		}.getResult();
	}

	/**
	 * 批量的{@link #hashMultipleSet(String, Map)}，在管道中执行
	 * @param data Map<String, Map<String, String>>格式的数据
	 * @return 执行结果
	 */
	public List<Object> batchHashMultipleSet(final Map<String, Map<String, String>> data) {
		return new Executor<List<Object>>(shardedJedisPool) {

			@Override
			List<Object> execute() {
				return jedis.pipelined(new ShardedJedisPipeline() {

					@Override
					public void execute() {
						for (Map.Entry<String, Map<String, String>> iter : data.entrySet()) {
							hmset(iter.getKey(), iter.getValue());
						}
					}
				});
			}
		}.getResult();
	}

	/**
	 * 批量的{@link #hashMultipleGet(String, String...)}，在管道中执行
	 * @param pairs 多个hash的多个field
	 * @return 执行结果
	 */
	public List<List<String>> batchHashMultipleGet(final List<Pair<String, String[]>> pairs) {
		return new Executor<List<List<String>>>(shardedJedisPool) {

			@Override
			List<List<String>> execute() {
				List<Object> dataList = jedis.pipelined(new ShardedJedisPipeline() {

					@Override
					public void execute() {
						for (Pair<String, String[]> pair : pairs) {
							hmget(pair.getKey(), pair.getValue());
						}
					}
				});
				List<List<String>> rtnDataList = new ArrayList<List<String>>();
				for (Object data : dataList) {
					rtnDataList.add(STRING_LIST.build(data));
				}
				return rtnDataList;
			}
		}.getResult();
		
	}

	/**
	 * 返回hash的所有filed和value 
	 * 时间复杂度: O(N)
	 * @param key key
	 * @return hash的所有filed和value 
	 */
	public Map<String, String> hashGetAll(final String key) {
		return new Executor<Map<String, String>>(shardedJedisPool) {

			@Override
			Map<String, String> execute() {
				return jedis.hgetAll(key);
			}
		}.getResult();
	}

	/**
	 * 批量的{@link #hashMultipleGet(String, String...)}
	 * @param keys keys
	 * @return 多个hash的所有filed和value
	 */
	public List<Map<String, String>> batchHashGetAll(final List<String> keys) {
		return new Executor<List<Map<String, String>>>(shardedJedisPool) {

			@Override
			List<Map<String, String>> execute() {
				List<Object> dataList = jedis.pipelined(new ShardedJedisPipeline() {

					@Override
					public void execute() {
						for (String key : keys) {
							hgetAll(key);
						}
					}
				});
				List<Map<String, String>> rtnDataList = new ArrayList<Map<String, String>>();
				for (Object data : dataList) {
						rtnDataList.add(STRING_MAP.build(data));
				}
				return rtnDataList;
			}
		}.getResult();
	}

	/**
	 * 批量的{@link #hashMultipleGet(String, String...)}，与{@link #batchHashGetAll(List)}不同的是，返回值为Map类型
	 * @param keys keys
	 * @return 多个hash的所有filed和value
	 */
	public Map<String, Map<String, String>> batchHashGetAllForMap(final List<String> keys) {
		return new Executor<Map<String, Map<String, String>>>(shardedJedisPool) {

			@Override
			Map<String, Map<String, String>> execute() {
				List<Object> dataList = jedis.pipelined(new ShardedJedisPipeline() {

					@Override
					public void execute() {
						for (String key : keys) {
							hgetAll(key);
						}
					}
				});
				Map<String, Map<String, String>> rtnDataMap = new HashMap<String, Map<String, String>>();
				if (keys.size() == dataList.size()) {
					for (int i = 0; i < keys.size(); ++i) {
						rtnDataMap.put(keys.get(i), STRING_MAP.build(dataList.get(i)));
					}
				} else {
					throw new RuntimeException("batchHashGetAllForMap error...");
				}
				return rtnDataMap;
			}
		}.getResult();
	}

	/* ======================================List====================================== */
	
	
	public Long listSize(final String key) {
		return new Executor<Long>(shardedJedisPool) {

			@Override
			Long execute() {
				return jedis.llen(key);
			}
		}.getResult();
	}

	/**
	 * 在key对应list的尾部部添加字符串元素，返回1表示成功，0表示key存在且不是list类型。
	 * @param key key
	 * @param value value
	 * @return list的size
	 */
	public Long listPushTail(final String key, final String value) {
		return new Executor<Long>(shardedJedisPool) {

			@Override
			Long execute() {
				return jedis.rpush(key, value);
			}
		}.getResult();
	}

	/**
	 * 在key对应list的头部添加字符串元素，返回1表示成功，0表示key存在且不是list类型。
	 * @param key key
	 * @param value value
	 * @param size 链表超过这个长度就修剪元素
	 * @return list的size
	 */
	public Long listPushHeadAndTrim(final String key, final String value, final long size) {
		return new Executor<Long>(shardedJedisPool) {

			@Override
			Long execute() {
				long listSize = jedis.lpush(key, value);
				if (listSize > size) {
					// 修剪列表元素
					jedis.ltrim(key, 0, size - 1);
				}
				return listSize;
			}
		}.getResult();
	}

	/**
	 * 批量的{@link #listPushTail(String, String)}，以锁的方式实现
	 * @param key key
	 * @param values values
	 * @param delOld 如果key存在，是否删除它。true 删除；false: 不删除，只是在行尾追加
	 * @return 执行结果
	 */
	public List<Object> batchListPushTail(final String key, final List<String> values, final boolean delOld) {
		return new Executor<List<Object>>(shardedJedisPool) {

			@Override
			List<Object> execute() {
				List<Object> status = null;
				if (delOld) {
					status = new ArrayList<Object>();
					List<Response<?>> responses = new ArrayList<Response<?>>();
					Pipeline pipeline = jedis.getShard(key).pipelined();
					responses.add(pipeline.del(key));
					for (String value : values) {
						responses.add(pipeline.rpush(key, value));
					}
					pipeline.sync();
					for (Response<?> response : responses) {
						status.add(response.get());
					}
				} else {
					status = jedis.pipelined(new ShardedJedisPipeline() {

						@Override
						public void execute() {
							for (String value : values) {
								rpush(key, value);
							}
						}
					});
				}
				return status;
			}
		}.getResult();
	}

	/**
	 * 在key对应list的尾部部添加字符串元素,如果key存在，什么也不做
	 * @param key key
	 * @param values values
	 */
	public void insertListIfNotExists(final String key, final List<String> values) {
		new Executor<Object>(shardedJedisPool) {

			@Override
			Object execute() {
				if (!jedis.exists(key)) {
					Pipeline pipeline = jedis.getShard(key).pipelined();
					for (String value : values) {
						pipeline.rpush(key, value);
					}
					pipeline.sync();
				}
				return null;
			}
		}.getResult();
	}

	/**
	 * 同{@link #batchListPushTail(String, List, boolean)},不同的是利用redis的事务特性来实现
	 * @param key key
	 * @param values values
	 * @return null
	 */
	public Object updateListInTransaction(final String key, final List<String> values) {
		return new Executor<Object>(shardedJedisPool) {

			@Override
			Object execute() {
				Transaction transaction = jedis.getShard(key).multi();
				transaction.del(key);
				for (String value : values) {
					transaction.rpush(key, value);
				}
				transaction.exec();
				return null;
			}
		}.getResult();
	}

	/**
	 * 返回list所有元素，下标从0开始，负值表示从后面计算，-1表示倒数第一个元素，key不存在返回空列表
	 * @param key key
	 * @return list所有元素
	 */
	public List<String> listGetAll(final String key) {
		return new Executor<List<String>>(shardedJedisPool) {

			@Override
			List<String> execute() {
				return jedis.lrange(key, 0, -1);
			}
		}.getResult();
	}

	/**
	 * 返回指定区间内的元素，下标从0开始，负值表示从后面计算，-1表示倒数第一个元素，key不存在返回空列表
	 * @param key key
	 * @param beginIndex 下标开始索引（包含）
	 * @param endIndex 下标结束索引（不包含）
	 * @return 指定区间内的元素
	 */
	public List<String> listRange(final String key, final long beginIndex, final long endIndex) {
		return new Executor<List<String>>(shardedJedisPool) {

			@Override
			List<String> execute() {
				return jedis.lrange(key, beginIndex, endIndex - 1);
			}
		}.getResult();
	}

	/**
	 * 循环调用{@link #batchListPushTail(String, List, boolean)}
	 * @param pairs pairs
	 * @return 执行结果
	 */
	public List<Object> batchInsertList(final List<Pair<String, List<String>>> pairs) {
		return new Executor<List<Object>>(shardedJedisPool) {

			@Override
			List<Object> execute() {
				List<Object> status = new ArrayList<Object>();
				for (Pair<String, List<String>> pair : pairs) {
					status.addAll(batchListPushTail(pair.getKey(), pair.getValue(), true));
				}
				return status;
			}
		}.getResult();
	}

	/**
	 * 这些key必须在同一个节点上，如果配置了集群，请不要使用这个方法
	 * @param keys keys
	 * @return 执行结果
	 */
	public Map<String, List<String>> batchGetAllList(final List<String> keys) {
		return new Executor<Map<String, List<String>>>(shardedJedisPool) {

			@Override
			Map<String, List<String>> execute() {
				Map<String, List<String>> data = new HashMap<String, List<String>>();
				// 所有的key必须在同一个节点上
				Pipeline pipeline = jedis.getShard(keys.get(0)).pipelined();
				Map<String, Response<List<String>>> responses = new HashMap<String, Response<List<String>>>();
				for (String key : keys) {
					responses.put(key, pipeline.lrange(key, 0, -1));
				}
				pipeline.sync();
				for (Map.Entry<String, Response<List<String>>> iter : responses.entrySet()) {
					data.put(iter.getKey(), iter.getValue().get());
				}
				return data;
			}
		}.getResult();
	}

	/* ======================================Builder====================================== */

	public static final Builder<Double> DOUBLE = new Builder<Double>() {

		@Override
		public Double build(Object data) {
			return Double.valueOf(STRING.build(data));
		}

		@Override
		public String toString() {
			return "double";
		}
	};

	public static final Builder<Boolean> BOOLEAN = new Builder<Boolean>() {

		@Override
		public Boolean build(Object data) {
			return ((Long) data) == 1;
		}

		@Override
		public String toString() {
			return "boolean";
		}
	};

	public static final Builder<Long> LONG = new Builder<Long>() {

		@Override
		public Long build(Object data) {
			return (Long) data;
		}

		@Override
		public String toString() {
			return "long";
		}

	};

	public static final Builder<String> STRING = new Builder<String>() {

		@Override
		public String build(Object data) {
			return SafeEncoder.encode((byte[]) data);
		}

		@Override
		public String toString() {
			return "string";
		}

	};

	public static final Builder<List<String>> STRING_LIST = new Builder<List<String>>() {

		@Override
		@SuppressWarnings("unchecked")
		public List<String> build(Object data) {
			if (null == data) {
				return null;
			}
			List<byte[]> l = (List<byte[]>) data;
			final ArrayList<String> result = new ArrayList<String>(l.size());
			for (final byte[] barray : l) {
				if (barray == null) {
					result.add(null);
				} else {
					result.add(SafeEncoder.encode(barray));
				}
			}
			return result;
		}

		@Override
		public String toString() {
			return "List<String>";
		}

	};

	public static final Builder<Map<String, String>> STRING_MAP = new Builder<Map<String, String>>() {

		@Override
		@SuppressWarnings("unchecked")
		public Map<String, String> build(Object data) {
			final List<byte[]> flatHash = (List<byte[]>) data;
			final Map<String, String> hash = new HashMap<String, String>();
			final Iterator<byte[]> iterator = flatHash.iterator();
			while (iterator.hasNext()) {
				hash.put(SafeEncoder.encode(iterator.next()), SafeEncoder.encode(iterator.next()));
			}

			return hash;
		}

		@Override
		public String toString() {
			return "Map<String, String>";
		}

	};

	public static final Builder<Set<String>> STRING_SET = new Builder<Set<String>>() {

		@Override
		@SuppressWarnings("unchecked")
		public Set<String> build(Object data) {
			if (null == data) {
				return null;
			}
			List<byte[]> l = (List<byte[]>) data;
			final Set<String> result = new HashSet<String>(l.size());
			for (final byte[] barray : l) {
				if (barray == null) {
					result.add(null);
				} else {
					result.add(SafeEncoder.encode(barray));
				}
			}
			return result;
		}

		@Override
		public String toString() {
			return "Set<String>";
		}

	};

	public static final Builder<Set<String>> STRING_ZSET = new Builder<Set<String>>() {

		@Override
		@SuppressWarnings("unchecked")
		public Set<String> build(Object data) {
			if (null == data) {
				return null;
			}
			List<byte[]> l = (List<byte[]>) data;
			final Set<String> result = new LinkedHashSet<String>(l.size());
			for (final byte[] barray : l) {
				if (barray == null) {
					result.add(null);
				} else {
					result.add(SafeEncoder.encode(barray));
				}
			}
			return result;
		}

		@Override
		public String toString() {
			return "ZSet<String>";
		}

	};

	/* ======================================Other====================================== */

	/**
	 * 设置数据源
	 * @param shardedJedisPool 数据源
	 */
	public void setShardedJedisPool(ShardedJedisPool shardedJedisPool) {
		this.shardedJedisPool = shardedJedisPool;
	}

	/**
	 * 构造Pair键值对
	 * @param key key
	 * @param value value
	 * @return 键值对
	 */
	public <K, V> Pair<K, V> makePair(K key, V value) {
		return new Pair<K, V>(key, value);
	}

	/**
	 * 键值对
	 * @version V1.0
	 * @author fengjc
	 * @param <K> key
	 * @param <V> value
	 */
	public class Pair<K, V> {

		private K key;
		private V value;

		public Pair(K key, V value) {
			this.key = key;
			this.value = value;
		}

		public K getKey() {
			return this.key;
		}

		public void setKey(K key) {
			this.key = key;
		}

		public V getValue() {
			return this.value;
		}

		public void setValue(V value) {
			this.value = value;
		}
	}
}

