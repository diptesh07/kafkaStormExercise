package com.bits.storm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.tuple.Tuple;

public class FlavorCounterBolt extends BaseBasicBolt {

	private Map<String, Integer> flavorCounter = new HashMap<String, Integer>();

	public void execute(Tuple tuple, BasicOutputCollector outputCollector) {
		String flavor = tuple.getStringByField("flavor");

		// If the list already contains the present user contact, increase his number of
		// trips by 1, else add him to the list
		if (flavorCounter.containsKey(flavor))
			flavorCounter.replace(flavor, flavorCounter.get(flavor) + 1);
		else
			flavorCounter.put(flavor, 1);
	}

	// This method is called at shutdown of cluster
	public void cleanup() {

		// We sort the hashMap and display the first three elements
		Set<Entry<String, Integer>> flavorSet = flavorCounter.entrySet();
		List<Entry<String, Integer>> flavorList = new ArrayList<Entry<String, Integer>>(flavorSet);

		Collections.sort(flavorList, new Comparator<Map.Entry<String, Integer>>() {
			public int compare(Map.Entry<String, Integer> flavor1, Map.Entry<String, Integer> flavor2) {
				return (flavor2.getValue()).compareTo(flavor1.getValue());
			}
		});

		System.out.println("The top 3 popular flavors are: ");

		for (int i = 0; i < 3; i++) {
			Map.Entry<String, Integer> entry = flavorList.get(i);
			System.out.println(entry.getKey() + " : " + entry.getValue());
		}

	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		// This bolt does not return anything, so empty output field
	}

}
