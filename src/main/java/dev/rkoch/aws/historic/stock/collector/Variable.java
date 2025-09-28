package dev.rkoch.aws.historic.stock.collector;

import dev.rkoch.aws.utils.ddb.DefaultVariable;

public enum Variable implements DefaultVariable {

  DATE_START_AV, //
  DATE_START_NQ, //
  ;

  @Override
  public String getTable() {
    return "STATE";
  }

}
