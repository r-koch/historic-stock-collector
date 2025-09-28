package dev.rkoch.aws.historic.stock.collector;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.naming.LimitExceededException;
import dev.rkoch.aws.s3.parquet.S3Parquet;
import dev.rkoch.aws.stock.collector.StockRecord;
import dev.rkoch.aws.stock.collector.Symbols;
import dev.rkoch.aws.stock.collector.api.AlphaVantageApi;

public class HistoricStockCollector {

  private static final String BUCKET_NAME = "dev-rkoch-spre";

  private static final String PARQUET_KEY = "raw/stock/localDate=%s/data.parquet";

  public static void main(String[] args) {
    new HistoricStockCollector().collect();
  }

  private AlphaVantageApi alphaVantageApi;

  private S3Parquet s3Parquet;

  public HistoricStockCollector() {

  }

  public void collect() {
    collect(Symbols.get());
  }

  private void collect(final List<String> symbols) {
    LocalDate avEndDate = LocalDate.parse(Variable.DATE_START_NQ.get());
    Map<LocalDate, List<StockRecord>> map = new HashMap<>();

    for (String symbol : symbols) {
      try {
        for (StockRecord stockRecord : getAlphaVantageApi().getData(symbol)) {
          LocalDate localDate = stockRecord.getLocalDate();
          if (localDate.isBefore(avEndDate)) {
            List<StockRecord> records = map.get(localDate);
            if (records == null) {
              records = new ArrayList<>();
            }
            records.add(stockRecord);
            map.put(localDate, records);
          }
        }
      } catch (LimitExceededException e) {
        saveState(map);
        System.out.println("save partial");
      }
    }
    saveState(map);
    System.out.println("save all");
    try {
      for (List<StockRecord> records : map.values()) {
        insert(records.getFirst().getLocalDate(), records);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void saveState(Map<LocalDate, List<StockRecord>> map) {
    try {
      Path tempFile = Files.createTempFile(null, null);
      try (ObjectOutputStream outputstream = new ObjectOutputStream(Files.newOutputStream(tempFile))) {
        outputstream.writeObject(map);
        System.out.println(tempFile.toString());
      }
    } catch (IOException e1) {
      e1.printStackTrace();
    }
  }

  private AlphaVantageApi getAlphaVantageApi() {
    if (alphaVantageApi == null) {
      alphaVantageApi = new AlphaVantageApi();
    }
    return alphaVantageApi;
  }

  private S3Parquet getS3Parquet() {
    if (s3Parquet == null) {
      s3Parquet = new S3Parquet();
    }
    return s3Parquet;
  }

  private void insert(final LocalDate date, final List<StockRecord> records) throws Exception {
    getS3Parquet().write(BUCKET_NAME, PARQUET_KEY.formatted(date), records);
  }

}
