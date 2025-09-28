package dev.rkoch.aws.historic.stock.collector;

import java.time.LocalDate;
import java.util.List;
import dev.rkoch.aws.s3.parquet.S3Parquet;
import dev.rkoch.aws.stock.collector.StockRecord;

public class Test {

  public static void main(String[] args) throws Exception {
    new S3Parquet().write("dev-rkoch-spre-test", "blub/LocalDate=%s/data.parquet".formatted(LocalDate.now()),
        List.of(StockRecord.of(LocalDate.now(), "test", 0, 0, 0, 0, 0)));
  }

}
