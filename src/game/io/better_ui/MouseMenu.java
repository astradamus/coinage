package game.io.better_ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;

/**
 *
 */
public class MouseMenu {

  private final Font toolTipFont;

  private final GamePanel parentPanel;
  private final Dimension parentPanelBounds;
  private final int tileSize;

  private Rectangle menuViewPort;

  private String toolTipText;
  private int toolTipWidth;
  private Color toolTipColor;
  private Color toolTipBGColor;

  //  private List<Command> commands;


  public MouseMenu(GamePanel parentPanel, int parentTileSize) {
    this.toolTipFont = new Font("Monospaced", Font.BOLD, parentTileSize);
    this.tileSize = parentTileSize;
    this.parentPanel = parentPanel;
    this.parentPanelBounds = parentPanel.getPreferredSize();
  }


  public void drawOverlay(Graphics g) {
    if (menuViewPort == null) {
      return; // No mouse menu to draw.
    }
    g.setFont(toolTipFont);
    g.setColor(toolTipBGColor);
    g.fillRect(menuViewPort.x, menuViewPort.y, menuViewPort.width, menuViewPort.height);
    g.setColor(toolTipColor);
    g.drawRect(menuViewPort.x, menuViewPort.y, menuViewPort.width, menuViewPort.height);
    g.drawString(toolTipText, menuViewPort.x + (menuViewPort.width - toolTipWidth)/2,
        menuViewPort.y + g.getFontMetrics().getAscent() - menuViewPort.height/4);
  }


  private void configureViewPort(int pixelX, int pixelY, int pixelWidth, int pixelHeight) {
    int adjustedX = pixelX;
    int adjustedY = pixelY - pixelHeight/5;
    int adjustedWidth = pixelWidth+pixelWidth/6;
    int adjustedHeight = pixelHeight+pixelHeight/10;

    if (adjustedX + adjustedWidth > parentPanelBounds.getWidth()) {
      adjustedX = adjustedX - adjustedWidth - tileSize*3;
    }
    if (adjustedY + adjustedHeight > parentPanelBounds.getHeight()) {
      adjustedY -= adjustedHeight;
    }

    menuViewPort = new Rectangle(adjustedX, adjustedY, adjustedWidth, adjustedHeight);
  }


  public void setToolTip(int tileLeft, int tileTop, String tip, Color color, Color bgColor) {
    toolTipWidth = parentPanel.getFontMetrics(toolTipFont).stringWidth(tip);
    configureViewPort((tileLeft+2) * tileSize, (tileTop) * tileSize, toolTipWidth, toolTipFont
        .getSize());

    toolTipText = tip;
    toolTipColor = color;
    toolTipBGColor = bgColor;
  }

  //  public void setMenu(int tileLeft, int tileTop, String tip, Color color, List<Command>
  // commands) {
  //    this.commands = commands;
  //  }


  public void clear() {
    menuViewPort = null;
    toolTipText = null;
    toolTipColor = null;
    toolTipBGColor = null;
  }
}
