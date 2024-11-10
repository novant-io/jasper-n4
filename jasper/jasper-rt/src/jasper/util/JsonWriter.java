//
// Copyright (c) 2023, Novant LLC
// Licensed under the MIT License
//
// History:
//   19 Jan 2023  Andy Frank  Creation
//

package jasper.util;

import java.io.*;
import java.util.*;
import javax.baja.status.*;

/**
 * JsonWriter.
 */
public final class JsonWriter
{
  /** Constructor. */
  public JsonWriter(OutputStream out)
  {
    this.out = new PrintWriter(out);
  }

  /** Flush underlying output stream. */
  public JsonWriter flush() throws IOException
  {
    out.flush();
    return this;
  }

  /** Close underlying output stream. */
  public JsonWriter close() throws IOException
  {
    out.close();
    return this;
  }

  /** Write given char to output stream. */
  public JsonWriter write(char val) throws IOException
  {
    out.print(val);
    return this;
  }

  /** Write given name as "<name>": to output stream. */
  public JsonWriter writeKey(String name) throws IOException
  {
    out.print('\"');
    out.print(name);  // TODO: escape
    out.print('\"');
    out.print(':');
    return this;
  }

  /** Write given int to output stream. */
  public JsonWriter writeVal(int val) throws IOException
  {
    out.print(val);
    return this;
  }

  /** Write given int to output stream. */
  public JsonWriter writeVal(double val) throws IOException
  {
    if (Double.isNaN(val)) { out.print("\"na\""); return this; }
    if (val == Double.POSITIVE_INFINITY) { out.print("\"na\""); return this; }
    if (val == Double.NEGATIVE_INFINITY) { out.print("\"na\""); return this; }
    out.print(val);
    return this;
  }

  /** Write given object to output stream. */
  public JsonWriter writeVal(Object val) throws IOException
  {
    // null
    if (val == null)
    {
      out.print("null");
      return this;
    }

    // String
    if (val instanceof String)
    {
      // TODO: escape
      out.print('\"');
      out.print(val);
      out.print('\"');
      return this;
    }

    // Integer
    if (val instanceof Integer)
    {
      int i = ((Integer)val).intValue();
      this.writeVal(i);
      return this;
    }

    // Double
    if (val instanceof Double)
    {
      double d = ((Double)val).doubleValue();
      this.writeVal(d);
      return this;
    }

    // BStatusBoolean
    if (val instanceof BStatusBoolean)
    {
      BStatusBoolean b = (BStatusBoolean)val;
      out.print(b.getValue() ? 1 : 0);
      return this;
    }

    // BStatusNumeric
    if (val instanceof BStatusNumeric)
    {
      BStatusNumeric n = (BStatusNumeric)val;
      double d = n.getValue();
      this.writeVal(d);
      return this;
    }

    // BStatusEnum
    if (val instanceof BStatusEnum)
    {
      BStatusEnum e = (BStatusEnum)val;
      out.print(e.getValue().getOrdinal());
      return this;
    }

    // HashMap
    if (val instanceof HashMap)
    {
      HashMap map = (HashMap)val;
      out.print('{');
      int i = 0;

      Iterator iter = map.entrySet().iterator();
      while (iter.hasNext())
      {
        if (i > 0) out.print(',');
        Map.Entry e = (Map.Entry)iter.next();
        writeKey((String)e.getKey());
        writeVal(e.getValue().toString());
        i++;
      }

      out.print('}');
      return this;
    }

    // unsupported type
    throw new IOException("Unsupported type '" + val + "' [" + val.getClass().getName() + "]");
  }

  private PrintWriter out;
}