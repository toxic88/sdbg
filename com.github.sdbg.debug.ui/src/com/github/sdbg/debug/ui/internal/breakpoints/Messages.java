/*
 * Copyright (c) 2011, the Dart project authors.
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
package com.github.sdbg.debug.ui.internal.breakpoints;

import org.eclipse.osgi.util.NLS;

/**
 * I18N messages for Dart debug.
 */
public class Messages extends NLS {
  public static String breakpointAction_error;
  public static String breakpointAction_errorTogglingBreakpoint;
  public static String breakpointAction_disableBreakpoint;
  public static String breakpointAction_enableBreakpoint;

  private static final String BUNDLE_NAME = "com.github.sdbg.debug.ui.internal.breakpoints.messages"; //$NON-NLS-1$

  static {
    // initialize resource bundle
    NLS.initializeMessages(BUNDLE_NAME, Messages.class);
  }

  private Messages() {

  }

}
