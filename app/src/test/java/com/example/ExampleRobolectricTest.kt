package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.HtmlExportGenerator
import com.example.data.RiddimGuideRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("Riddim Master", appName)
  }

  @Test
  fun `verify channels and buses repository data`() {
    assertEquals(11, RiddimGuideRepository.channels.size)
    assertEquals(8, RiddimGuideRepository.mixbuses.size)
    assertEquals(4, RiddimGuideRepository.spatialBuses.size)
    assertEquals(5, RiddimGuideRepository.masterChainStages.size)
  }

  @Test
  fun `verify html generation export contains critical tags`() {
    val html = HtmlExportGenerator.generateHtml()
    assertNotNull(html)
    assertTrue(html.contains("-6.0 LUFS"))
    assertTrue(html.contains("-0.1 dBFS"))
    assertTrue(html.contains("Neutron Clipper"))
    assertTrue(html.contains("FabFilter Pro-L 2"))
  }
}
