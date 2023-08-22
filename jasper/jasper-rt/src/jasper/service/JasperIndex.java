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
// Package-Private
////////////////////////////////////////////////////////////////

  /** Clear all entries from index. */
  void clear()
  {
    smap.clear();
  }

  /** Add a new source to index. */
  void addSource(JasperSource s)
  {
    smap.put(s.id, s);
  }

////////////////////////////////////////////////////////////////
// Attributes
////////////////////////////////////////////////////////////////

  private HashMap smap = new HashMap();   // source.id : JasperSource
}