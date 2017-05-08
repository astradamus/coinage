package game.io.display;


import utils.ColoredString;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


/**
 *
 */
public class DisplayElement_Text implements DisplayElement {

    final List<ColoredString> texts;

    private final Font font;
    private final int paddingLeft;


    public DisplayElement_Text(int paddingLeft, Font font, List<ColoredString> texts) {
        this.font = font;
        this.paddingLeft = paddingLeft;
        this.texts = texts;
    }

    public DisplayElement_Text(int paddingLeft, Font font, Color color, String string) {
        this(paddingLeft, font, Collections.singletonList(new ColoredString(color, string)));
    }

    public DisplayElement_Text(int paddingLeft, Font font, Color color, List<String> strings) {
        this(paddingLeft, font, strings.stream()
                .map(string -> new ColoredString(color, string)).collect(Collectors.toList()));
    }


    @Override
    public void drawTo(Graphics g, int originX, int originY, int width) {

        g.clipRect(originX, originY, width, getHeight());
        g.setFont(font);

        for (int i = 0; i < texts.size(); i++) {

            ColoredString coloredString = texts.get(i);

            int adjustDown = (i + 1) * getLineHeight();


            SquareDrawer.drawString(g,
                                    coloredString.getString(), coloredString.getColor(),
                                    originX + paddingLeft, originY + adjustDown);

        }

        g.setClip(null);

    }


    int getLineHeight() {
        return font.getSize();
    }

    @Override
    public int getHeight() {
        return (int) (getLineHeight() * (texts.size() + 0.4));
    }

}