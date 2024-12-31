package Creators;

import Parser.*;
import java.util.List;

public class TrivialCreator implements CreatorFromRow<List<String>> {

  @Override
  public List<String> create(List<String> row) throws FactoryFailureException {
    // Simply return the row as is, no transformation
    return row;
  }
}
