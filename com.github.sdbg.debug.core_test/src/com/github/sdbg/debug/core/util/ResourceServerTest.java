/*
 * Copyright (c) 2013, the Dart project authors.
 * 
 * Licensed under the Eclipse Public License v1.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.github.sdbg.debug.core.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import junit.framework.TestCase;

import org.eclipse.core.resources.IFile;

import com.github.sdbg.core.test.util.TestProject;
import com.github.sdbg.utilities.Streams;
import com.github.sdbg.utilities.URIUtilities;

public class ResourceServerTest extends TestCase {
  private ResourceServer server;
  private TestProject project;

  public void test_404MissingResource() throws Exception {
    IFile file = project.setFileContent("foo.txt", "foo");
    String url = server.getUrlForResource(file) + "s";

    HttpURLConnection connection = createConnection(url);

    assertEquals(404, connection.getResponseCode());

    connection.disconnect();
  }

  public void test_canServeResource() throws Exception {
    IFile file = project.setFileContent("foo.txt", "foo");
    String url = server.getUrlForResource(file);

    HttpURLConnection connection = createConnection(url);

    assertEquals(200, connection.getResponseCode());
    assertEquals(3, connection.getContentLength());
    assertEquals("text/plain", connection.getContentType());
    assertEquals(
        "foo",
        Streams.loadAndClose(new InputStreamReader(connection.getInputStream(), "UTF-8")));

    connection.disconnect();
    connection.getInputStream().close();
  }

  public void test_onlyServeWorkspaceFiles() throws Exception {
    File file = File.createTempFile("foo", ".txt");
    Streams.storeAndClose("foo", new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));
    file.deleteOnExit();

    String filePath = file.getAbsolutePath().replaceAll("\\\\", "/");
    if (filePath.startsWith("/")) {
      filePath = filePath.substring(1);
    }
    String url = "http://localhost:" + server.getPort() + "/" + URIUtilities.uriEncode(filePath);

    HttpURLConnection connection = createConnection(url);

    assertEquals(404, connection.getResponseCode());

    connection.disconnect();
    file.delete();
  }

  public void test_onlyServeWorkspaceFilesBadPath() throws Exception {
    File file = File.createTempFile("foo", ".txt");
    Streams.storeAndClose("foo", new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));
    file.deleteOnExit();

    String filePath = file.getAbsolutePath();
    if (filePath.startsWith("/")) {
      filePath = filePath.substring(1);
    }
    String url = "http://localhost:" + server.getPort() + "/" + URIUtilities.uriEncode(filePath);

    HttpURLConnection connection = createConnection(url);

    assertEquals(404, connection.getResponseCode());

    connection.disconnect();
    file.delete();
  }

  @Override
  protected void setUp() throws Exception {
    super.setUp();

    server = new ResourceServer();
    project = new TestProject();
  }

  @Override
  protected void tearDown() throws Exception {
    project.dispose();
    server.shutdown();

    super.tearDown();
  }

  private HttpURLConnection createConnection(String url) throws IOException {
    HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
    connection.setConnectTimeout(3000);
    connection.setReadTimeout(3000);
    return connection;
  }

}
