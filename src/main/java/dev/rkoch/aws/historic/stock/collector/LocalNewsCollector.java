package dev.rkoch.aws.historic.stock.collector;

import dev.rkoch.aws.news.collector.NewsCollector;
import dev.rkoch.aws.utils.log.SystemOutLambdaLogger;
import software.amazon.awssdk.regions.Region;

public class LocalNewsCollector {

  public static void main(String[] args) {
    new NewsCollector(new SystemOutLambdaLogger(), Region.EU_WEST_1).collect();
  }

}
