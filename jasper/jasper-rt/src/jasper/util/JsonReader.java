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

/**
 * JsonReader.
 */
public final class JsonReader
{
  /** Constructor. */
  public JsonReader(InputStream in) throws IOException
  {
    this.in = new BufferedReader(new InputStreamReader(in, "UTF-8"));
  }

  /** Close underlying input stream. */
  public JsonReader close() throws IOException
  {
    in.close();
    return this;
  }

  /** Parse Json and return HashMap instance. */
  public Object readVal() throws IOException
  {
    // check if we need to init reader
    if (!init)
    {
      init = true;
      this.cur  = -1;
      this.peek = in.read();
    }

    // eat leading whitespace
    eatWhitespace();

    // bool
    if (peek == 't') return readBool();
    if (peek == 'f') return readBool();

    // num
    if (peek == '-') return readNum();
    if (Character.isDigit(peek)) return readNum();

    // str
    if (peek == '\"') return readStr();

    // map
    if (peek == '{') return readMap();

    // list
    if (peek == '[') return readList();

    throw unexpectedChar(peek);
  }

  /** Read a 'true' or 'false value. */
  private Boolean readBool() throws IOException
  {
    if (peek == 't')
    {
      read('t');
      read('r');
      read('u');
      read('e');
      return Boolean.TRUE;
    }
    else
    {
      read('f');
      read('a');
      read('l');
      read('s');
      read('e');
      return Boolean.FALSE;
    }
  }

  /** Read a Number value. */
  private Double readNum() throws IOException
  {
    StringBuffer buf = new StringBuffer();
    if (peek == '-') buf.append((char)read());
    while (Character.isDigit(peek) || peek == '.' || peek == 'E' || peek == '-')
      buf.append((char)read());
    return Double.parseDouble(buf.toString());
  }

  /** Read a Str value. */
  private String readStr() throws IOException
  {
    StringBuffer buf = new StringBuffer();
    read('\"');
    while (peek != '\"')
    {
      if (peek == '\\')
      {
        int p = read();
        if (peek == 'u')
        {
          read();
          int n3 = hex(read());
          int n2 = hex(read());
          int n1 = hex(read());
          int n0 = hex(read());
          int uc = ((n3 << 12) | (n2 << 8) | (n1 << 4) | n0);
          buf.append((char)uc);
        }
        else
        {
          // TODO FIXIT!
          buf.append((char)p);
          buf.append((char)read());
        }
      }
      else
      {
        buf.append((char)read());
      }
    }
    read('\"');
    return buf.toString();
  }

  /** Read a ArrayList value. */
  private ArrayList readList() throws IOException
  {
    ArrayList list = new ArrayList();
    read('[');
    while (peek != ']')
    {
      // add key:value pair
      eatWhitespace();
      Object val = readVal();
      list.add(val);

      // verify next char is valid
      eatWhitespace();
      if (peek == ',') { read(); continue; }
      if (peek == ']') continue;
      throw unexpectedChar(peek);
    }
    read(']');
    return list;
  }

  /** Read a HashMap value. */
  private HashMap readMap() throws IOException
  {
    HashMap map = new HashMap();
    read('{');
    while (peek != '}')
    {
      // add key:value pair
      eatWhitespace();
      String key = readStr();
      eatWhitespace();
      read(':');
      eatWhitespace();
      Object val = readVal();
      map.put(key, val);

      // verify next char is valid
      eatWhitespace();
      if (peek == ',') { read(); continue; }
      if (peek == '}') continue;
      throw unexpectedChar(peek);
    }
    read('}');
    return map;
  }

  /** Read the next char from stream. */
  private int read() throws IOException
  {
    cur  = peek;
    peek = in.read();
    pos++;
    return cur;
  }

  /** Read the next char from stream and validate it matches expected. */
  private int read(int expected) throws IOException
  {
    int ch = read();
    if (ch < 0) throw new IOException("Unexpected EOS");
    if (ch != expected) throw unexpectedChar(ch);
    return ch;
  }

  /** Eat leading whitespace. */
  private void eatWhitespace() throws IOException
  {
    while (peek == ' ') read();
  }

  /** Convert hex char to base 10 digit */
  static int hex(int c)
  {
    if ('0' <= c && c <= '9') return c - '0';
    if ('a' <= c && c <= 'f') return c - 'a' + 10;
    if ('A' <= c && c <= 'F') return c - 'A' + 10;
    return -1;
  }

  private IOException unexpectedChar(int ch)
  {
    return new IOException("Unexpected char '" + ((char)ch) + "' [" + pos + "]");
  }

  private BufferedReader in;
  private boolean init = false;
  private int cur;
  private int peek;
  private int pos;
}