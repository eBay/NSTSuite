package com.ebay.tool.thinmodelgen.utility;

public class CurlyBraceCounter {

  private int count = 0;

  public void readLine(String line) {
    if (line.contains("{")) {
      this.count ++;
    }

    if (line.contains("}")) {

      if (count <= 0) {
        throw new IllegalStateException("Curly brace should have been opened before closing curly brace is found.");
      }

      this.count --;
    }
  }

  public boolean isBraceCountEmpty() {
    return this.count <= 0;
  }

  public boolean isBraceCountNotEmpty() {
    return this.count > 0;
  }

}
