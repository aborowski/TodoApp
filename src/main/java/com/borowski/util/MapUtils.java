package com.borowski.util;

import java.util.Map;
import java.util.Map.Entry;

public class MapUtils {
	
	public static String printMap(Map<?, ?> map) {
		StringBuilder sb = new StringBuilder();
		
		for(Entry<?, ?> mapEntry : map.entrySet()) {
			sb.append(mapEntry.getKey().toString());
			sb.append(": ");
			sb.append(mapEntry.getValue().toString());
			sb.append("\n");
		}
		
		return sb.toString();
	}
}
