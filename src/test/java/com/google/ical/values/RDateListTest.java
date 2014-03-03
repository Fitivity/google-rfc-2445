// Copyright (C) 2006 Google Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.ical.values;

import org.junit.Before;
import org.junit.Test;

import java.util.TimeZone;

import static org.junit.Assert.assertEquals;
/**
 * @author mikesamuel+svn@gmail.com (Mike Samuel)
 */
public class RDateListTest  {

  private static final TimeZone PST =
    TimeZone.getTimeZone("America/Los_Angeles");

  @Before
  public void setUp() throws Exception {
    assertEquals(-8 * 60 * 60 * 1000, PST.getRawOffset());
  }


  @Test
  public void testRDateListParsing() throws Exception {
    RDateList rd1 = new RDateList("RDATE:20060412", PST);
    RDateList rd2 = new RDateList("RDATE:20060412T120000", PST);
    RDateList rd3 =
      new RDateList("RDATE:20060412T120000,20060413T153000Z", PST);
    assertEquals(1, rd1.getDatesUtc().length);
    assertEquals(new DateValueImpl(2006, 4, 12), rd1.getDatesUtc()[0]);
    assertEquals(1, rd2.getDatesUtc().length);
    assertEquals(new DateTimeValueImpl(2006, 4, 12, 19, 0, 0),
                 rd2.getDatesUtc()[0]);
    assertEquals(2, rd3.getDatesUtc().length);
    assertEquals(new DateTimeValueImpl(2006, 4, 12, 19, 0, 0),
                 rd3.getDatesUtc()[0]);
    assertEquals(new DateTimeValueImpl(2006, 4, 13, 15, 30, 0),
                 rd3.getDatesUtc()[1]);
  }

  @Test
  public void testRDateListParsingWithExplicitTzid() throws Exception {
    RDateList rd = new RDateList(
        "RDATE;TZID=\"America/New_York\":20060412T120000", PST);
    assertEquals(1, rd.getDatesUtc().length);
    assertEquals(new DateTimeValueImpl(2006, 4, 12, 16, 0, 0),
                 rd.getDatesUtc()[0]);
  }

  @Test
  public void testRDateIcal() throws Exception {
    RDateList rd1 = new RDateList("RDATE:20060412", PST);
    RDateList rd2 = new RDateList("RDATE:20060412T120000", PST);
    RDateList rd3 = new RDateList("rdate:20060412T120000,20060413T153000", PST);
    RDateList rd4 = new RDateList(
        "RDATE;TZID=\"America/New_York\":20060412T120000", PST);
    assertEquals("RDATE;TZID=\"America/Los_Angeles\";VALUE=DATE:20060412",
                 rd1.toIcal());
    assertEquals("RDATE;TZID=\"America/Los_Angeles\";VALUE=DATE-TIME:"
                 + "20060412T190000Z",
                 rd2.toIcal());
    assertEquals("RDATE;TZID=\"America/Los_Angeles\";VALUE=DATE-TIME"
                 + ":20060412T190000Z,20060413T223000Z",
                 rd3.toIcal());
    assertEquals("RDATE;TZID=\"America/New_York\";VALUE=DATE-TIME"
                 + ":20060412T160000Z",
                 rd4.toIcal());
  }

  @Test
  public void testRDateIcalWithXParams() throws Exception {
    RDateList rd = new RDateList(
        "RDATE;tzid=\"America/Los_Angeles\";X-FOO=BAR:20060412", PST);
    assertEquals(
        "RDATE;TZID=\"America/Los_Angeles\";VALUE=DATE;X-FOO=BAR:20060412",
        rd.toIcal());
  }
}
