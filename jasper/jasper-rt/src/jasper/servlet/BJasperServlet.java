//
// Copyright (c) 2023, Novant LLC
// Licensed under the MIT License
//
// History:
//   19 Jan 2023  Andy Frank  Creation
//

package jasper.servlet;

import java.io.*;
import javax.baja.io.*;
import javax.baja.naming.*;
import javax.baja.sys.*;
import javax.baja.util.*;
import javax.baja.web.*;
import javax.servlet.http.*;
import jasper.service.*;
import jasper.util.*;

/**
 * BJasperServlet
 */
public final class BJasperServlet extends BWebServlet
{
  /*-
  class BJasperServlet
  {
  }
  -*/
/*+ ------------ BEGIN BAJA AUTO GENERATED CODE ------------ +*/
/*@ $jasper.servlet.BJasperServlet(2852803851)1.0$ @*/
/* Generated Wed Apr 21 15:57:23 EDT 2021 by Slot-o-Matic 2000 (c) Tridium, Inc. 2000 */

////////////////////////////////////////////////////////////////
// Type
////////////////////////////////////////////////////////////////

  public Type getType() { return TYPE; }
  public static final Type TYPE = Sys.loadType(BJasperServlet.class);

/*+ ------------ END BAJA AUTO GENERATED CODE -------------- +*/

////////////////////////////////////////////////////////////////
// Constructor
////////////////////////////////////////////////////////////////

  /** Constructor. */
  public BJasperServlet()
  {
    super.setServletName("jasper");
    super.setFlags(getSlot("servletName"), Flags.READONLY | Flags.SUMMARY);
  }

  /** Set backing index */
  public void setIndex(JasperIndex index) { this.index = index; }

////////////////////////////////////////////////////////////////
// Servlet
////////////////////////////////////////////////////////////////

  public void doPost(WebOp op) throws IOException
  {
    try
    {
      // NOTE: getPathInfo removes 'jasper' prefix from path already
      HttpServletRequest req = op.getRequest();
      String[] path = JasperUtil.splitPath(req.getPathInfo());

      // sanity check path is long enough
      if (path.length < 2)
      {
        JasperUtil.sendNotFound(op);
        return;
      }

      // key off version
      if (path[0].equals("v1"))
      {
        if (path[1].equals("about"))  { doAbout(op);  return; }
        if (path[1].equals("points")) { doPoints(op); return; }
        if (path[1].equals("values")) { doValues(op); return; }
      }

      // if we get here then 404
      JasperUtil.sendNotFound(op);
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
      JasperUtil.sendErr(op, 500, "Unexpected error", ex);
    }
  }

  /** Service /v1/about request. */
  private void doAbout(WebOp op) throws IOException
  {
    HttpServletResponse res = op.getResponse();
    res.setStatus(200);
    res.setHeader("Content-Type", "application/json");

    // TODO: watch trailing comma bugs; should JsonWriter handle internally?
    JsonWriter json = new JsonWriter(res.getOutputStream());
    json.write('{');

    // required fields
    json.writeKey("name").writeVal(Sys.getStation().getStationName()).write(',');
    json.writeKey("vendor").writeVal("Tridium").write(',');
    json.writeKey("model").writeVal("Niagara 4").write(',');
    json.writeKey("version").writeVal(BComponent.TYPE.getVendorVersion().toString()).write(',');

    // additional fields
    BModule module = BJasperService.TYPE.getModule();
    json.writeKey("moduleName").writeVal(module.getModuleName()).write(',');
    json.writeKey("moduleVersion").writeVal(BJasperService.TYPE.getVendorVersion().toString());

    json.write('}');
    json.flush().close();
  }

  /** Service /v1/points request. */
  private void doPoints(WebOp op) throws IOException
  {
    String[] ids = index.ids();
    int num = 0;

    // request args
    HttpServletRequest req = op.getRequest();
    String prefix = reqArgStr(req, "path_prefix", null);

    // response
    HttpServletResponse res = op.getResponse();
    res.setStatus(200);
    res.setHeader("Content-Type", "application/json");

    JsonWriter json = new JsonWriter(res.getOutputStream());
    json.write('{');
    json.writeKey("points").write('[');
    for (int i=0; i<ids.length; i++)
    {
      JasperPoint p = index.get(ids[i]);

      // skip if supplied path_prefix does not match
      if (prefix != null && !p.path.startsWith(prefix)) continue;

      // prefix trailing commas
      if (num > 0) json.write(',');

      json.write('{');
      json.writeKey("addr").writeVal(p.addr).write(',');
      json.writeKey("name").writeVal(p.name).write(',');
      json.writeKey("path").writeVal(p.path);
      if (p.enums != null)
      {
        json.write(',');
        json.writeKey("enum").writeVal(p.enums);
      }
      if (p.unit != null)
      {
        json.write(',');
        json.writeKey("unit").writeVal(p.unit);
      }
      json.write('}');
      num++;
    }
    json.write(']');
    json.write('}');
    json.flush().close();
  }

  /** Service /v1/values request. */
  private void doValues(WebOp op) throws IOException
  {
    BJasperService service = (BJasperService)this.getParent();
    String[] ids = index.ids();
    int num = 0;

    // request args
    HttpServletRequest req = op.getRequest();
    String prefix = reqArgStr(req, "path_prefix", null);

    // response
    HttpServletResponse res = op.getResponse();
    res.setStatus(200);
    res.setHeader("Content-Type", "application/json");

    JsonWriter json = new JsonWriter(res.getOutputStream());
    json.write('{');
    json.writeKey("values").write('[');
    for (int i=0; i<ids.length; i++)
    {
      JasperPoint p = index.get(ids[i]);

      // skip if supplied path_prefix does not match
      if (prefix != null && !p.path.startsWith(prefix)) continue;

      // get point value
      BOrd h = JasperUtil.getOrdFromId(p.addr);
      BComponent c = (BComponent)h.resolve(service).get();
      c.lease(1, leaseTime);
      Object val = JasperUtil.getPointJsonValue(c);

      // prefix trailing commas
      if (num > 0) json.write(',');

      json.write('{');
      json.writeKey("addr").writeVal(p.addr).write(',');
      json.writeKey("val").writeVal(val);
      json.write('}');
      num++;
    }
    json.write(']');
    json.write('}');
    json.flush().close();
  }

////////////////////////////////////////////////////////////////
// Support
////////////////////////////////////////////////////////////////

  /** Get HTTP request argument as 'String' or return 'defVal' if not found. */
  private String reqArgStr(HttpServletRequest req, String name, String defVal)
  {
    return req.getParameter(name);
  }

  /** Get HTTP request argument as 'int' or return 'defVal' if not found. */
  private int reqArgInt(HttpServletRequest req, String name, int defVal)
  {
    String str = req.getParameter(name);
    if (str == null) return defVal;
    return Integer.parseInt(str);
  }

////////////////////////////////////////////////////////////////
// Attributes
////////////////////////////////////////////////////////////////

  private JasperIndex index;
  private final long leaseTime = 60000;   // 1min in millis
}