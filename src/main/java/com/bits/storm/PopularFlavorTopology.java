package com.bits.storm;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.generated.StormTopology;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.tuple.Fields;

public class PopularFlavorTopology {
	public static void main(String args[]) {
		TopologyBuilder builder = new TopologyBuilder();

		// declaring the spout
		builder.setSpout("Order Generator", new OrderGenSpout());

		// declaring bolts
		/*
		 * We used shuffle grouping when transferring a tuple from spout to the user
		 * extractor bolt because it does not matter at which bolt the user contact is
		 * being retrieved. However when moving from the user extractor to user count,
		 * we are using fields grouping so that same users go to same bolt. Otherwise
		 * separate bolts will have separate counts for the same user.
		 */

		builder.setBolt("Flavor Extractor", new FlavorExtractorBolt()).shuffleGrouping("Order Generator");
		builder.setBolt("Flavor Count", new FlavorCounterBolt()).fieldsGrouping("Flavor Extractor",
				new Fields("flavor"));

		Config config = new Config();
		/*
		 * If we want to see the flow of tuples from one spout/bolt to another, we can
		 * set the config to debug mode
		 */
		// config.setDebug(true);

		StormTopology topology = builder.createTopology();
		LocalCluster cluster = new LocalCluster();
		cluster.submitTopology("PopularFlavourTopology", config, topology);

		try {
			Thread.sleep(30000);
			/*
			 * The spout is continuously emitting data from the file. We can change the
			 * sleep time to see the difference in the number of data points which have been
			 * processed during this time-period
			 */
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		cluster.killTopology("PopularFlavourTopology");
		cluster.shutdown();
	}

}
