package dev.aura.bungeechat.config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.google.common.base.Preconditions;
import dev.aura.bungeechat.message.Message;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.io.File;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class MessagesTranslatorTest {
  private static File tempDir;

  @SuppressFBWarnings(
    value = "RV_RETURN_VALUE_IGNORED_BAD_PRACTICE",
    justification =
        "If deleting fails there is no point in doing anything. Files just get delete to prevent buildup on disk"
  )
  private static void cleanTempDir() {
    if (tempDir.exists()) {
      final File[] files = tempDir.listFiles();

      if (files != null) {
        for (File f : files) {
          f.delete();
        }
      }

      tempDir.delete();
    }
  }

  @BeforeClass
  public static void setUpClass() {
    tempDir = new File(System.getProperty("java.io.tmpdir"), "bungeechat");
  }

  @AfterClass
  public static void tearDownClass() {
    cleanTempDir();
  }

  @Before
  public void setUp() {
    Preconditions.checkState(tempDir.mkdirs(), "Could not create temp folder");
  }

  @After
  public void tearDown() {
    cleanTempDir();
  }

  @Test
  public void fileCopyTest() {
    new MessagesTranslator(tempDir, MessagesTranslator.DEFAULT_LANGUAGE);

    File en_US = new File(tempDir, "en_US.lang");
    File de_DE = new File(tempDir, "de_DE.lang");

    assertTrue("Expected en_US.lang to exist", en_US.exists());
    assertTrue("Expected de_DE.lang to exist", de_DE.exists());
  }

  @Test
  public void missingLanguageTest() {
    MessagesTranslator expected =
        new MessagesTranslator(tempDir, MessagesTranslator.DEFAULT_LANGUAGE);
    MessagesTranslator testee = new MessagesTranslator(tempDir, "unknown");

    for (Message message : Message.values()) {
      assertEquals(
          "Expected default language and missing language to be the same",
          expected.translateWithFallback(message),
          testee.translateWithFallback(message));
    }
  }
}
