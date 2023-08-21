//
// Copyright (c) 2023, Novant LLC
// Licensed under the MIT License
//
// History:
//   19 Jan 2023  Andy Frank  Creation
//

package jasper.service;

import java.util.*;
import javax.baja.sys.*;

/**
 * JasperIndex.
 */
public final class JasperIndex
{
  /** Constructor */
  public JasperIndex()
  {
  }

////////////////////////////////////////////////////////////////
// Package-Private
////////////////////////////////////////////////////////////////

  /** Clear all entries from index. */
  void clear()
  {
    smap.clear();
    pmap.clear();
  }

  /** Add a new source to index. */
  void addSource(JasperSource s)
  {
    smap.put(s.id, s);
  }

  /** Add a new point to index. */
  void addPoint(JasperPoint p)
  {
    pmap.put(p.addr, p);
  }

////////////////////////////////////////////////////////////////
// Sources
////////////////////////////////////////////////////////////////

  /** Return number of sources in index. */
  public int numSources()
  {
    return smap.size();
  }

  /** Get source for given addr or null if not found. */
  public JasperSource getSource(String id)
  {
    return (JasperSource)smap.get(id);
  }

  /** Get list of sources in index. */
  public Collection<JasperSource> getSources()
  {
    return smap.values();
  }

////////////////////////////////////////////////////////////////
// Points
////////////////////////////////////////////////////////////////

  /** Return number of points in index. */
  public int numPoints()
  {
    return pmap.size();
  }

  /** Get the point for given addr or null if not found. */
  public JasperPoint getPoint(String addr)
  {
    return (JasperPoint)pmap.get(addr);
  }

  /** Get list of point addrs in index. */
  public String[] pointAddrs()
  {
    // TODO: should we cache this?
    Set keys = pmap.keySet();
    String[] acc = (String[])keys.toArray(new String[keys.size()]);
    return acc;
  }

////////////////////////////////////////////////////////////////
// Attributes
////////////////////////////////////////////////////////////////

  private HashMap smap = new HashMap();   // source.id  : JasperSource
  private HashMap pmap = new HashMap();   // point.addr : JasperPoint
}