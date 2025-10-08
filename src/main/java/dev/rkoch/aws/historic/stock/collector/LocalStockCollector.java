package dev.rkoch.aws.historic.stock.collector;

import dev.rkoch.aws.stock.collector.StockCollector;
import dev.rkoch.aws.utils.log.SystemOutLambdaLogger;
import software.amazon.awssdk.regions.Region;

public class LocalStockCollector {

  public static void main(String[] args) {
    new StockCollector(new SystemOutLambdaLogger(), Region.EU_WEST_1).collect();
  }

}
