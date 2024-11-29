//
// Copyright (c) 2023, Novant LLC
// Licensed under the MIT License
//
// History:
//   19 Jan 2023  Andy Frank  Creation
//

package jasper.util;

import java.io.*;
import javax.baja.io.*;
import javax.baja.control.*;
import javax.baja.control.enums.*;
import javax.baja.naming.*;
import javax.baja.registry.*;
import javax.baja.status.*;
import javax.baja.sys.*;
import javax.baja.util.*;
import javax.baja.web.*;
import javax.servlet.http.*;
import jasper.service.*;

/**
 * JasperUtil
 */
public final class JasperUtil
{

////////////////////////////////////////////////////////////////
// BComponent
////////////////////////////////////////////////////////////////

  /**
   * Conveneince to check object type.
   * */
  public static boolean isType(BObject obj, String type)
  {
    TypeInfo info = Sys.getRegistry().getType(type);
    return obj.getType().getTypeInfo().is(info);
  }

  /**
   * Get the Jasper point id for given point.
   */
  public static String getSourceId(BComponent c)
  {
    // strip h: from handle ord
    String handle = c.getHandleOrd().toString();
    String suffix = handle.substring(2);
    return suffix;
  }

  /**
   * Get the Jasper point addr for given point.
   */
  public static String getPointAddr(JasperSource source, BComponent c)
  {
    // get relative slot path from parent source
    String sslot  = source.slotPath();
    String pslot  = c.getSlotPath().toString();
    String suffix = pslot.substring(sslot.length() + 1);

    // cleanup slotpath suffix
    suffix = JasperUtil.slotPathToSuffix(suffix);

    // point type
    if (isType(c, "control:NumericWritable")) return "av." + suffix;
    if (isType(c, "control:NumericPoint"))    return "ai." + suffix;
    if (isType(c, "control:BooleanWritable")) return "bv." + suffix;
    if (isType(c, "control:BooleanPoint"))    return "bi." + suffix;
    if (isType(c, "control:EnumWritable"))    return "ev." + suffix;
    if (isType(c, "control:EnumPoint"))       return "ei." + suffix;

    // unsupported type
    throw new RuntimeException("Unsupported point type '" + c.getDisplayName(null) + "' [" + c.getSlotPath() + "]");
  }

  /**
   * Get handle Ord from component point id.
   */
  public static BOrd getOrdFromId(String id)
  {
    String handle = "h:" + id.substring(3);
    return BOrd.make(handle);
  }

  /**
   * Unescape component slot path.
   */
  public static String unescapeSlotPath(String orig)
  {
    StringBuffer buf  = new StringBuffer();
    StringBuffer temp = new StringBuffer();

    for (int i=0; i<orig.length(); i++)
    {
      int ch = orig.charAt(i);
      if (ch == '$' && (i+2 < orig.length()))
      {
        // clear out temp buffer for reuse
        temp.setLength(0);
        temp.append(orig.charAt(++i));
        temp.append(orig.charAt(++i));
        ch = (char)Integer.parseInt(temp.toString(), 16);
      }
      buf.append((char)ch);
    }

    return buf.toString();
  }

  /**
   * Convert a component slot path to point addr suffix.
   */
  public static String slotPathToSuffix(String orig)
  {
    StringBuffer buf  = new StringBuffer();

    for (int i=0; i<orig.length(); i++)
    {
      int ch = orig.charAt(i);
           if (ch == '/') { ch = '.'; }
      else if (ch == '$') { i += 2; continue; }
      else if (!Character.isLetterOrDigit(ch)) { continue; }
      buf.append((char)ch);
    }

    return buf.toString();
  }

  /**
   * Get value of given point or 'null' if not available.
   */
  public static BStatusValue getPointValue(BComponent c)
  {
    Object out = c.get("out");
    if (out instanceof BStatusValue) return (BStatusValue)out;
    return null;
  }

  /**
   * Set value of given point.
   */
  public static void setPointValue(BControlPoint point, Double dval, int level)
  {
    BPriorityLevel plevel = BPriorityLevel.make(level);

    if (point instanceof BNumericWritable)
    {
      BNumericWritable nw = (BNumericWritable)point;
      BStatusNumeric sn = (BStatusNumeric)nw.getLevel(plevel).newCopy();
      if (dval == null)
      {
        sn.setStatus(BStatus.nullStatus);
      }
      else
      {
        sn.setValue(dval.doubleValue());
        sn.setStatus(BStatus.ok);
      }
      nw.set("in" + level, sn);
    }
    else if (point instanceof BBooleanWritable)
    {
      BBooleanWritable bw = (BBooleanWritable) point;
      BStatusBoolean sb = (BStatusBoolean) bw.getLevel(plevel).newCopy();
      if (dval == null)
      {
        sb.setStatus(BStatus.nullStatus);
      }
      else
      {
        sb.setValue(dval.doubleValue() == 0 ? false : true);
        sb.setStatus(BStatus.ok);
      }
      bw.set("in" + level, sb);
    }
    else if (point instanceof BEnumWritable)
    {
      BEnumWritable ew = (BEnumWritable) point;
      BStatusEnum se = (BStatusEnum) ew.getLevel(plevel).newCopy();
      if (dval == null)
      {
        se.setStatus(BStatus.nullStatus);
      }
      else
      {
        BFacets facets = point.getFacets();
        BEnumRange range = (BEnumRange)facets.get(BFacets.RANGE);
        int ord = dval.intValue();
        if (ord < 0 || ord >= range.getOrdinals().length)
          throw new IllegalArgumentException("Ordinal out of range: " + ord);
        BEnum enm = range.get(ord);
        se.setValue(enm);
        se.setStatus(BStatus.ok);
      }
      ew.set("in" + level, se);
    }
    else
    {
      throw new IllegalArgumentException("Unsupported writable point: " + point.getSlotPath() + " [" + point.getClass() + "]");
    }
  }

  /**
   * Parse a BFacet range value into a Jaspser compatible
   * string or 'null' if string is empty.
   */
  public static String parseEnumRange(BEnumRange range)
  {
    // short-circuit if empty range
    if (range == null || range.isNull()) return null;

    // TODO: for now assume zero-based and ordered
    // {alpha=0,beta=1,gamma=2} -> alpha,beta,gamma

    StringBuffer buf = new StringBuffer();
    int[] ords = range.getOrdinals();
    for (int i=0; i<ords.length; i++)
    {
      if (i > 0) buf.append(',');
      String tag = range.get(ords[i]).getTag();
      buf.append(unescapeSlotPath(tag));
    }

    // sanity check
    if (buf.length() == 0) return null;
    return buf.toString();
  }

////////////////////////////////////////////////////////////////
// Servlet
////////////////////////////////////////////////////////////////

  /** Convenience for sendErr(404) */
  public static void sendNotFound(WebOp op) throws IOException
  {
    sendErr(op, 404, "Not Found");
  }

  /** Send an error repsponse as JSON with code and msg. */
  public static void sendErr(WebOp op, int code, String msg) throws IOException
  {
    sendErr(op, code, msg, null);
  }

  /** Send an error repsponse as JSON with code and msg. */
  public static void sendErr(WebOp op, int code, String msg, Exception cause) throws IOException
  {
    HttpServletResponse res = op.getResponse();
    res.setStatus(code);
    res.setHeader("Content-Type", "application/json");

    JsonWriter json = new JsonWriter(res.getOutputStream());
    json.write('{');
    json.writeKey("err_msg").writeVal(msg);
    if (cause != null)
    {
      json.write(',');
      json.writeKey("err_trace");
      json.writeVal(printStackTraceToString(cause));
    }
    json.write('}');
    json.flush().close();
  }

  /** Read content from request. */
  public static String readContent(WebOp op) throws IOException
  {
    StringBuffer sb = new StringBuffer();
    InputStream in = new BufferedInputStream(op.getRequest().getInputStream());
    BufferedReader br = new BufferedReader(new InputStreamReader(in));
    String line = null;
    while ((line = br.readLine()) != null)
    {
      sb.append(line);
    }
    return sb.toString();
  }

////////////////////////////////////////////////////////////////
// URI
////////////////////////////////////////////////////////////////

  /** Split a path string into array. */
  public static String[] splitPath(String path)
  {
    String[] orig = path.split("/");

    // get non-empty size
    int size = 0;
    for (int i=0; i<orig.length; i++)
      if (orig[i].length() > 0) size++;

    // filter out empty
    String[] acc = new String[size];
    int p = 0;
    for (int i=0; i<orig.length; i++)
      if (orig[i].length() > 0) acc[p++] = orig[i];
    return acc;
  }

////////////////////////////////////////////////////////////////
// Exceptions
////////////////////////////////////////////////////////////////

  /** Print stack trace to string. */
  public static String printStackTraceToString(Exception ex)
  {
    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw);
    ex.printStackTrace(pw);
    return sw.toString();
  }
}