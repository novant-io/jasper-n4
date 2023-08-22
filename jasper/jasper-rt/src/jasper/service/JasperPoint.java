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
 * JasperPoint.
 */
public final class JasperPoint
{
  /** Constructor */
  public JasperPoint(String addr, String name, String enums, String unit)
  {
    this.addr  = addr;
    this.name  = name;
    this.enums = enums;
    this.unit  = unit;
  }

  /** Address for this point under parent source. */
  public final String addr;

  /** Name of this point. */
  public final String name;

  /** Enum ordinal names for this point or null if not defined. */
  public final String enums;

  /** Unit for this point or null if not defined. */
  public final String unit;

  public String toString() { return name; }

// TODO FIXIT: cleanup encapsulation here (and JasperSource)
  // package private: backing comp
  //BComponent comp;
  public BComponent comp;
}