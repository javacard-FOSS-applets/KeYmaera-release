// This file is part of KeY - Integrated Deductive Software Design
// Copyright (C) 2001-2009 Universitaet Karlsruhe, Germany
//                         Universitaet Koblenz-Landau, Germany
//                         Chalmers University of Technology, Sweden
//
// The KeY system is protected by the GNU General Public License. 
// See LICENSE.TXT for details.
/* This file has been generated by Stubmaker (de.uka.ilkd.stubmaker)
 * Date: Wed May 14 11:55:45 CEST 2008
 */
package java.awt;

public abstract class Component extends java.lang.Object implements java.awt.image.ImageObserver, java.awt.MenuContainer, java.io.Serializable
{
   public final static float TOP_ALIGNMENT = 0.0;
   public final static float CENTER_ALIGNMENT = 0.5;
   public final static float BOTTOM_ALIGNMENT = 1.0;
   public final static float LEFT_ALIGNMENT = 0.0;
   public final static float RIGHT_ALIGNMENT = 1.0;

   protected Component();
   public java.lang.String getName();
   public void setName(java.lang.String arg0);
   public java.awt.Container getParent();
   public java.awt.peer.ComponentPeer getPeer();
   public void setDropTarget(java.awt.dnd.DropTarget arg0);
   public java.awt.dnd.DropTarget getDropTarget();
   public java.awt.GraphicsConfiguration getGraphicsConfiguration();
   public final java.lang.Object getTreeLock();
   public java.awt.Toolkit getToolkit();
   public boolean isValid();
   public boolean isDisplayable();
   public boolean isVisible();
   public java.awt.Point getMousePosition() throws java.awt.HeadlessException;
   public boolean isShowing();
   public boolean isEnabled();
   public void setEnabled(boolean arg0);
   public void enable();
   public void enable(boolean arg0);
   public void disable();
   public boolean isDoubleBuffered();
   public void enableInputMethods(boolean arg0);
   public void setVisible(boolean arg0);
   public void show();
   public void show(boolean arg0);
   public void hide();
   public java.awt.Color getForeground();
   public void setForeground(java.awt.Color arg0);
   public boolean isForegroundSet();
   public java.awt.Color getBackground();
   public void setBackground(java.awt.Color arg0);
   public boolean isBackgroundSet();
   public java.awt.Font getFont();
   public void setFont(java.awt.Font arg0);
   public boolean isFontSet();
   public java.util.Locale getLocale();
   public void setLocale(java.util.Locale arg0);
   public java.awt.image.ColorModel getColorModel();
   public java.awt.Point getLocation();
   public java.awt.Point getLocationOnScreen();
   public java.awt.Point location();
   public void setLocation(int arg0, int arg1);
   public void move(int arg0, int arg1);
   public void setLocation(java.awt.Point arg0);
   public java.awt.Dimension getSize();
   public java.awt.Dimension size();
   public void setSize(int arg0, int arg1);
   public void resize(int arg0, int arg1);
   public void setSize(java.awt.Dimension arg0);
   public void resize(java.awt.Dimension arg0);
   public java.awt.Rectangle getBounds();
   public java.awt.Rectangle bounds();
   public void setBounds(int arg0, int arg1, int arg2, int arg3);
   public void reshape(int arg0, int arg1, int arg2, int arg3);
   public void setBounds(java.awt.Rectangle arg0);
   public int getX();
   public int getY();
   public int getWidth();
   public int getHeight();
   public java.awt.Rectangle getBounds(java.awt.Rectangle arg0);
   public java.awt.Dimension getSize(java.awt.Dimension arg0);
   public java.awt.Point getLocation(java.awt.Point arg0);
   public boolean isOpaque();
   public boolean isLightweight();
   public void setPreferredSize(java.awt.Dimension arg0);
   public boolean isPreferredSizeSet();
   public java.awt.Dimension getPreferredSize();
   public java.awt.Dimension preferredSize();
   public void setMinimumSize(java.awt.Dimension arg0);
   public boolean isMinimumSizeSet();
   public java.awt.Dimension getMinimumSize();
   public java.awt.Dimension minimumSize();
   public void setMaximumSize(java.awt.Dimension arg0);
   public boolean isMaximumSizeSet();
   public java.awt.Dimension getMaximumSize();
   public float getAlignmentX();
   public float getAlignmentY();
   public int getBaseline(int arg0, int arg1);
   public java.awt.Component$BaselineResizeBehavior getBaselineResizeBehavior();
   public void doLayout();
   public void layout();
   public void validate();
   public void invalidate();
   public java.awt.Graphics getGraphics();
   public java.awt.FontMetrics getFontMetrics(java.awt.Font arg0);
   public void setCursor(java.awt.Cursor arg0);
   public java.awt.Cursor getCursor();
   public boolean isCursorSet();
   public void paint(java.awt.Graphics arg0);
   public void update(java.awt.Graphics arg0);
   public void paintAll(java.awt.Graphics arg0);
   public void repaint();
   public void repaint(long arg0);
   public void repaint(int arg0, int arg1, int arg2, int arg3);
   public void repaint(long arg0, int arg1, int arg2, int arg3, int arg4);
   public void print(java.awt.Graphics arg0);
   public void printAll(java.awt.Graphics arg0);
   public boolean imageUpdate(java.awt.Image arg0, int arg1, int arg2, int arg3, int arg4, int arg5);
   public java.awt.Image createImage(java.awt.image.ImageProducer arg0);
   public java.awt.Image createImage(int arg0, int arg1);
   public java.awt.image.VolatileImage createVolatileImage(int arg0, int arg1);
   public java.awt.image.VolatileImage createVolatileImage(int arg0, int arg1, java.awt.ImageCapabilities arg2) throws java.awt.AWTException;
   public boolean prepareImage(java.awt.Image arg0, java.awt.image.ImageObserver arg1);
   public boolean prepareImage(java.awt.Image arg0, int arg1, int arg2, java.awt.image.ImageObserver arg3);
   public int checkImage(java.awt.Image arg0, java.awt.image.ImageObserver arg1);
   public int checkImage(java.awt.Image arg0, int arg1, int arg2, java.awt.image.ImageObserver arg3);
   public void setIgnoreRepaint(boolean arg0);
   public boolean getIgnoreRepaint();
   public boolean contains(int arg0, int arg1);
   public boolean inside(int arg0, int arg1);
   public boolean contains(java.awt.Point arg0);
   public java.awt.Component getComponentAt(int arg0, int arg1);
   public java.awt.Component locate(int arg0, int arg1);
   public java.awt.Component getComponentAt(java.awt.Point arg0);
   public void deliverEvent(java.awt.Event arg0);
   public final void dispatchEvent(java.awt.AWTEvent arg0);
   public boolean postEvent(java.awt.Event arg0);
   public void addComponentListener(java.awt.event.ComponentListener arg0);
   public void removeComponentListener(java.awt.event.ComponentListener arg0);
   public java.awt.event.ComponentListener[] getComponentListeners();
   public void addFocusListener(java.awt.event.FocusListener arg0);
   public void removeFocusListener(java.awt.event.FocusListener arg0);
   public java.awt.event.FocusListener[] getFocusListeners();
   public void addHierarchyListener(java.awt.event.HierarchyListener arg0);
   public void removeHierarchyListener(java.awt.event.HierarchyListener arg0);
   public java.awt.event.HierarchyListener[] getHierarchyListeners();
   public void addHierarchyBoundsListener(java.awt.event.HierarchyBoundsListener arg0);
   public void removeHierarchyBoundsListener(java.awt.event.HierarchyBoundsListener arg0);
   public java.awt.event.HierarchyBoundsListener[] getHierarchyBoundsListeners();
   public void addKeyListener(java.awt.event.KeyListener arg0);
   public void removeKeyListener(java.awt.event.KeyListener arg0);
   public java.awt.event.KeyListener[] getKeyListeners();
   public void addMouseListener(java.awt.event.MouseListener arg0);
   public void removeMouseListener(java.awt.event.MouseListener arg0);
   public java.awt.event.MouseListener[] getMouseListeners();
   public void addMouseMotionListener(java.awt.event.MouseMotionListener arg0);
   public void removeMouseMotionListener(java.awt.event.MouseMotionListener arg0);
   public java.awt.event.MouseMotionListener[] getMouseMotionListeners();
   public void addMouseWheelListener(java.awt.event.MouseWheelListener arg0);
   public void removeMouseWheelListener(java.awt.event.MouseWheelListener arg0);
   public java.awt.event.MouseWheelListener[] getMouseWheelListeners();
   public void addInputMethodListener(java.awt.event.InputMethodListener arg0);
   public void removeInputMethodListener(java.awt.event.InputMethodListener arg0);
   public java.awt.event.InputMethodListener[] getInputMethodListeners();
   public java.util.EventListener[] getListeners(java.lang.Class arg0);
   public java.awt.im.InputMethodRequests getInputMethodRequests();
   public java.awt.im.InputContext getInputContext();
   protected final void enableEvents(long arg0);
   protected final void disableEvents(long arg0);
   protected java.awt.AWTEvent coalesceEvents(java.awt.AWTEvent arg0, java.awt.AWTEvent arg1);
   protected void processEvent(java.awt.AWTEvent arg0);
   protected void processComponentEvent(java.awt.event.ComponentEvent arg0);
   protected void processFocusEvent(java.awt.event.FocusEvent arg0);
   protected void processKeyEvent(java.awt.event.KeyEvent arg0);
   protected void processMouseEvent(java.awt.event.MouseEvent arg0);
   protected void processMouseMotionEvent(java.awt.event.MouseEvent arg0);
   protected void processMouseWheelEvent(java.awt.event.MouseWheelEvent arg0);
   protected void processInputMethodEvent(java.awt.event.InputMethodEvent arg0);
   protected void processHierarchyEvent(java.awt.event.HierarchyEvent arg0);
   protected void processHierarchyBoundsEvent(java.awt.event.HierarchyEvent arg0);
   public boolean handleEvent(java.awt.Event arg0);
   public boolean mouseDown(java.awt.Event arg0, int arg1, int arg2);
   public boolean mouseDrag(java.awt.Event arg0, int arg1, int arg2);
   public boolean mouseUp(java.awt.Event arg0, int arg1, int arg2);
   public boolean mouseMove(java.awt.Event arg0, int arg1, int arg2);
   public boolean mouseEnter(java.awt.Event arg0, int arg1, int arg2);
   public boolean mouseExit(java.awt.Event arg0, int arg1, int arg2);
   public boolean keyDown(java.awt.Event arg0, int arg1);
   public boolean keyUp(java.awt.Event arg0, int arg1);
   public boolean action(java.awt.Event arg0, java.lang.Object arg1);
   public void addNotify();
   public void removeNotify();
   public boolean gotFocus(java.awt.Event arg0, java.lang.Object arg1);
   public boolean lostFocus(java.awt.Event arg0, java.lang.Object arg1);
   public boolean isFocusTraversable();
   public boolean isFocusable();
   public void setFocusable(boolean arg0);
   public void setFocusTraversalKeys(int arg0, java.util.Set arg1);
   public java.util.Set getFocusTraversalKeys(int arg0);
   public boolean areFocusTraversalKeysSet(int arg0);
   public void setFocusTraversalKeysEnabled(boolean arg0);
   public boolean getFocusTraversalKeysEnabled();
   public void requestFocus();
   protected boolean requestFocus(boolean arg0);
   public boolean requestFocusInWindow();
   protected boolean requestFocusInWindow(boolean arg0);
   public void transferFocus();
   public java.awt.Container getFocusCycleRootAncestor();
   public boolean isFocusCycleRoot(java.awt.Container arg0);
   public void nextFocus();
   public void transferFocusBackward();
   public void transferFocusUpCycle();
   public boolean hasFocus();
   public boolean isFocusOwner();
   public void add(java.awt.PopupMenu arg0);
   public void remove(java.awt.MenuComponent arg0);
   protected java.lang.String paramString();
   public java.lang.String toString();
   public void list();
   public void list(java.io.PrintStream arg0);
   public void list(java.io.PrintStream arg0, int arg1);
   public void list(java.io.PrintWriter arg0);
   public void list(java.io.PrintWriter arg0, int arg1);
   public void addPropertyChangeListener(java.beans.PropertyChangeListener arg0);
   public void removePropertyChangeListener(java.beans.PropertyChangeListener arg0);
   public java.beans.PropertyChangeListener[] getPropertyChangeListeners();
   public void addPropertyChangeListener(java.lang.String arg0, java.beans.PropertyChangeListener arg1);
   public void removePropertyChangeListener(java.lang.String arg0, java.beans.PropertyChangeListener arg1);
   public java.beans.PropertyChangeListener[] getPropertyChangeListeners(java.lang.String arg0);
   protected void firePropertyChange(java.lang.String arg0, java.lang.Object arg1, java.lang.Object arg2);
   protected void firePropertyChange(java.lang.String arg0, boolean arg1, boolean arg2);
   protected void firePropertyChange(java.lang.String arg0, int arg1, int arg2);
   public void firePropertyChange(java.lang.String arg0, byte arg1, byte arg2);
   public void firePropertyChange(java.lang.String arg0, char arg1, char arg2);
   public void firePropertyChange(java.lang.String arg0, short arg1, short arg2);
   public void firePropertyChange(java.lang.String arg0, long arg1, long arg2);
   public void firePropertyChange(java.lang.String arg0, float arg1, float arg2);
   public void firePropertyChange(java.lang.String arg0, double arg1, double arg2);
   public void setComponentOrientation(java.awt.ComponentOrientation arg0);
   public java.awt.ComponentOrientation getComponentOrientation();
   public void applyComponentOrientation(java.awt.ComponentOrientation arg0);
   public javax.accessibility.AccessibleContext getAccessibleContext();
}
