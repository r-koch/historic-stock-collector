package dev.rkoch.aws.historic.stock.collector;

import dev.rkoch.aws.news.collector.Handler;
import dev.rkoch.aws.news.collector.NewsCollector;
import dev.rkoch.aws.utils.log.SystemOutLambdaLogger;

public class LocalNewsCollector {

  public static void main(String[] args) {
    new NewsCollector(new SystemOutLambdaLogger(), new Handler()).collect();
  }

}
