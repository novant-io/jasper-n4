//
// Copyright (c) 2023, Novant LLC
// Licensed under the MIT License
//
// History:
//   19 Jan 2023  Andy Frank  Creation
//

package jasper.service;


import javax.baja.log.*;
import javax.baja.sys.*;
import javax.baja.control.*;
import javax.baja.registry.*;
import jasper.servlet.*;
import jasper.util.*;

/**
 * JasperService.
 */
public final class BJasperService extends BAbstractService
{
  /*-
  class BJasperService
  {
    properties
    {
      servlet: BJasperServlet
        default{[ new BJasperServlet() ]}
    }

    actions
    {
      rebuildIndex()
    }
  }
  -*/
/*+ ------------ BEGIN BAJA AUTO GENERATED CODE ------------ +*/
/*@ $jasper.service.BJasperService(4038923251)1.0$ @*/
/* Generated Wed Apr 21 21:32:17 EDT 2021 by Slot-o-Matic 2000 (c) Tridium, Inc. 2000 */

////////////////////////////////////////////////////////////////
// Property "servlet"
////////////////////////////////////////////////////////////////

  /**
   * Slot for the <code>servlet</code> property.
   * @see jasper.service.BJasperService#getServlet
   * @see jasper.service.BJasperService#setServlet
   */
  public static final Property servlet = newProperty(0, new BJasperServlet(),null);

  /**
   * Get the <code>servlet</code> property.
   * @see jasper.service.BJasperService#servlet
   */
  public BJasperServlet getServlet() { return (BJasperServlet)get(servlet); }

  /**
   * Set the <code>servlet</code> property.
   * @see jasper.service.BJasperService#servlet
   */
  public void setServlet(BJasperServlet v) { set(servlet,v,null); }

////////////////////////////////////////////////////////////////
// Action "rebuildIndex"
////////////////////////////////////////////////////////////////

  /**
   * Slot for the <code>rebuildIndex</code> action.
   * @see jasper.service.BJasperService#rebuildIndex()
   */
  public static final Action rebuildIndex = newAction(0,null);

  /**
   * Invoke the <code>rebuildIndex</code> action.
   * @see jasper.service.BJasperService#rebuildIndex
   */
  public void rebuildIndex() { invoke(rebuildIndex,null,null); }

////////////////////////////////////////////////////////////////
// Type
////////////////////////////////////////////////////////////////

  public Type getType() { return TYPE; }
  public static final Type TYPE = Sys.loadType(BJasperService.class);

/*+ ------------ END BAJA AUTO GENERATED CODE -------------- +*/

////////////////////////////////////////////////////////////////
// BAbstractService
////////////////////////////////////////////////////////////////

  public Type[] getServiceTypes()
  {
    Type[] t = { getType() };
    return t;
  }

  public void serviceStarted() throws Exception
  {
  }

  public void atSteadyState()
  {
    doRebuildIndex();
    LOG.message("JasperService ready [version=" + moduleVer() + "]");
  }

  public void doRebuildIndex()
  {
    try
    {
      // start
      LOG.message("JasperReindexJob started...");
      BAbsTime t1 = BAbsTime.now();
      int numPoints = 0;

      // clear index
      index.clear();

      // scan station for points
      BStation station = Sys.getStation();
      BComponent[] comps = station.getComponentSpace().getAllComponents();
      for (int i=0; i<comps.length; i++)
      {
        BComponent c = comps[i];
        try
        {
          if (c instanceof BNumericPoint || c instanceof BBooleanPoint || c instanceof BEnumPoint)
          {
            // verify has source
            JasperSource source = getSource(c);
            if (source == null)
            {
              if (LOG.isTraceOn())
                LOG.trace("Source not found for point: " + c.getDisplayName(null) + " [" + c.getSlotPath() + "]");
              continue;
            }

            String addr  = JasperUtil.getPointAddr(source, c);
            String name  = c.getDisplayName(null);
            String unit  = null;
            String enums = null;

            BFacets f = (BFacets)c.get("facets");
            if (f != null)
            {
              // get units
              unit = f.gets("units", null);
              if (unit != null && unit.equals("null")) unit = null;

              // get enum range
              if (c instanceof BEnumPoint)
              {
                BEnumRange r = (BEnumRange)f.get("range");
                if (r != null) enums = JasperUtil.parseEnumRange(r);
              }
            }

            JasperPoint point = new JasperPoint(addr, name, enums, unit);
            point.comp = c;
            source.addPoint(point);
            numPoints++;
          }
          else
          {
            if (LOG.isTraceOn() && c instanceof BControlPoint)
              LOG.trace("Unsupported point:" + c.getDisplayName(null) + " [" + c.getSlotPath() + "]");
          }
        }
        catch (Exception e)
        {
          // do not fail reindex for one component; log error and continue
          LOG.error("FAILED to index: " + c.getName(), e);
        }
      }

      // TODO: this is design smell
      getServlet().setIndex(index);

      if (LOG.isTraceOn())
        LOG.trace("Total BComponents searched in station: " + comps.length);

      // complete
      BAbsTime t2 = BAbsTime.now();
      LOG.message("JasperReindexJob complete [" +
        t1.delta(t2) + ", " +
        index.numSources() + " sources, " +
        numPoints + " points]");
    }
    catch (Exception e)
    {
      LOG.error("JaserReindexJob FAILED", e);
    }
  }

////////////////////////////////////////////////////////////////
// Sources
////////////////////////////////////////////////////////////////

  /**
   * Get parent source for given point.
   */
  private JasperSource getSource(BComponent point)
  {
    // sanity check
    BComponent c = (BComponent)point.getParent();
    if (c == null) return null;

    // check if there is a better parent source
    c = findSourceComp(c);

    // check cache
    String id = JasperUtil.getSourceId(c);
    JasperSource source = index.getSource(id);

    // add to cache if not found
    if (source == null)
    {
      String name = c.getDisplayName(null);
      String path = JasperUtil.unescapeSlotPath(c.getSlotPath().toString().substring(5));

      // filter out common stuff we likley never want
      if (path.startsWith("/Services/SecurityService/")) return null;

      // index source
      source = new JasperSource(id, name, path);
      source.comp = c;
      index.addSource(source);
    }

    return source;
  }

  /**
   * Find "best" source component to use as parent source
   * */
  private BComponent findSourceComp(BComponent orig)
  {
    // never walk if no parent
    BComplex p = orig.getParent();
    if (p == null) return orig;

    // walk up to proxy device if we are directly under point proxy folder
    if (JasperUtil.isType(orig, "driver:PointDeviceExt") && JasperUtil.isType(p, "driver:Device"))
        return (BComponent)p;

    // stick with orig
    return orig;
  }

  /** Get module verson string */
  private String moduleVer()
  {
    return BJasperService.TYPE.getVendorVersion().toString();
  }

////////////////////////////////////////////////////////////////
// Attributes
////////////////////////////////////////////////////////////////

  static final Log LOG = Log.getLog("jasper");

  private JasperIndex index = new JasperIndex();
}