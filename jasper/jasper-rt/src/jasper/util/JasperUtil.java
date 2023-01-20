//
// Copyright (c) 2023, Andy Frank
// Licensed under the MIT License
//
// History:
//   19 Jan 2023  Andy Frank  Creation
//

package jasper.util;

import java.io.*;
import javax.baja.io.*;
import javax.baja.control.*;
import javax.baja.naming.*;
import javax.baja.status.*;
import javax.baja.sys.*;
import javax.baja.util.*;
import javax.baja.web.*;
import javax.servlet.http.*;

/**
 * JasperUtil
 */
public final class JasperUtil
{

////////////////////////////////////////////////////////////////
// BComponent
////////////////////////////////////////////////////////////////

  /**
   * Get the Jasper point id for given point.
   */
  public static String getPointId(BComponent c)
  {
    // strip h: from handle ord
    String handle = c.getHandleOrd().toString();
    String suffix = handle.substring(2);

    // point type
    if (c instanceof BNumericWritable) return "av." + suffix;
    if (c instanceof BNumericPoint)    return "ai." + suffix;
    if (c instanceof BBooleanWritable) return "bv." + suffix;
    if (c instanceof BBooleanPoint)    return "bi." + suffix;
    if (c instanceof BEnumWritable)    return "ev." + suffix;
    if (c instanceof BEnumPoint)       return "ei." + suffix;

    // unsupported type
    throw new RuntimeException("Unsupported point type '" + c.getName() + "'");
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
   * Get the JSON representation for the current value
   * of given point, or 'null' if not available.
   */
  public static Object getPointJsonValue(BComponent c)
  {
    Object out = c.get("out");

    if (out instanceof BStatusValue)
    {
      // if "na" if status not valid
      BStatusValue val = (BStatusValue)out;
      if (!val.getStatus().isValid()) return "na";

      // return as BStatusValue and let JsonWriter encode
      return val;
    }

    // if we get here then assume no value
    return null;
  }

  /**
   * Parse a BFacet range value into a Jaspser compatible
   * or 'null' if string is empty.
   */
  public static String parseEnumRange(String range)
  {
    // short-circuit if empty string
    if (range == null || range.length() == 0) return null;

    // TODO: for now assume zero-based and ordered
    // {alpha=0,beta=1,gamma=2} -> alpha,beta,gamma

    StringBuffer buf = new StringBuffer();
    for (int i=0; i<range.length(); i++)
    {
      char ch = range.charAt(i);
      if (ch == '{') continue;
      if (ch == '=')
      {
        // eat =xxx segment
        while (ch != ',' && ch != '}')
          ch = range.charAt(++i);
      }
      if (ch == '}') continue;
      buf.append(ch);
    }
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
    HttpServletResponse res = op.getResponse();
    res.setStatus(code);
    res.setHeader("Content-Type", "application/json");
    op.getHtmlWriter().w("{\"err_msg\":\"").safe(msg).w("\"}").flush();
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
}