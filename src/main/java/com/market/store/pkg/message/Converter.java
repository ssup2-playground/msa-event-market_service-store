package com.market.store.pkg.message;

import java.nio.ByteBuffer;
import java.util.Base64;
import java.util.UUID;

public class Converter {
  public static UUID getUuidFromBase64(String encorded) {
    ByteBuffer bb = ByteBuffer.wrap(Base64.getDecoder().decode(encorded));
    return new UUID(bb.getLong(), bb.getLong());
  }
}
