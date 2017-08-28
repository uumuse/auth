package com.kuke.util;

import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.lang3.StringUtils;

public class CollectionUtil {
	public static <T> String join(Collection<T> collection, String separator) {
		return join(collection, separator, null);
	}

	public static <T> String join(Collection<T> collection, String separator, String wrap) {
		StringBuffer buffer = new StringBuffer();
		if (collection != null && !collection.isEmpty()) {
			Iterator<T> iterator = collection.iterator();
			while (iterator.hasNext()) {
				T item = iterator.next();
				if (StringUtils.isNotBlank(wrap)) {
					buffer.append(wrap + item + wrap);
				} else {
					buffer.append(item);
				}
				if (iterator.hasNext()) {
					buffer.append(separator);
				}
			}
		}
		return buffer.toString();
	}
	
	public static void main(String[] args) {
		
	}
}
