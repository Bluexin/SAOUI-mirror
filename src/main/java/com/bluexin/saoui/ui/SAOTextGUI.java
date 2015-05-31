package com.bluexin.saoui.ui;

import com.bluexin.saoui.SAOJ8String;
import com.bluexin.saoui.util.SAOColor;
import com.bluexin.saoui.util.SAOParentGUI;
import com.bluexin.saoui.util.SAOGL;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

@SideOnly(Side.CLIENT)
public class SAOTextGUI extends SAOElementGUI {

    private String[] lines;
    public int fontColor;

    public SAOTextGUI(SAOParentGUI gui, int xPos, int yPos, String... strings) {
        super(gui, xPos, yPos, 0, 0);
        lines = strings;
        fontColor = SAOColor.DEFAULT_FONT_COLOR;
    }

    public SAOTextGUI(SAOParentGUI gui, int xPos, int yPos, String text, int width) {
        this(gui, xPos, yPos, toLines(text, width));
    }

    public SAOTextGUI(SAOParentGUI gui, int xPos, int yPos, String text) {
        this(gui, xPos, yPos, text, 0);
    }

    @Override
    public void update(Minecraft mc) {
        for (String line : lines) {
            final int strWidth = SAOGL.glStringWidth(line) + 16;

            if (strWidth > width) {
                width = strWidth;
            }
        }

        final int linesHeight = lines.length * SAOGL.glStringHeight() + 16;

        if (linesHeight > height) {
            height = linesHeight;
        }
    }

    @Override
    public void draw(Minecraft mc, int cursorX, int cursorY) {
        super.draw(mc, cursorX, cursorY);

        if (visibility > 0) {
            final int left = getX(false);
            final int top = getY(false);

            for (int i = 0; i < lines.length; i++) {
                SAOGL.glString(lines[i], left + 8, top + 8 + i * (SAOGL.glStringHeight() + 1), SAOColor.multiplyAlpha(fontColor, visibility));
            }
        }
    }

    public final void setText(String text) {
        lines = toLines(text, width);
    }

    public final String getText() {
        return SAOJ8String.join("\n", lines);
    }

    private static String[] toLines(String text, int width) {
        if (width <= 0) {
            return text.split("\n");
        } else {
            final String[] rawLines = text.split("\n");

            if (rawLines.length <= 0) {
                return rawLines;
            }

            final List<String> lines = new ArrayList<>();

            String cut = "";
            String line = rawLines[0];
            int rawIndex = 0;

            while (line != null) {
                int size = SAOGL.glStringWidth(line);

                while (size > width - 16) {
                    final int lastIndex = line.lastIndexOf(' ');

                    if (lastIndex != -1) {
                        cut = line.substring(lastIndex + 1) + " " + cut;
                        line = line.substring(0, lastIndex);

                        if (rawIndex + 1 < rawLines.length) {
                            rawLines[rawIndex + 1] = cut + rawLines[rawIndex + 1];
                            cut = "";
                        }
                    } else {
                        break;
                    }

                    size = SAOGL.glStringWidth(line);
                }

                if (!line.matches(" *")) {
                    lines.add(line);
                }

                if (cut.length() > 0) {
                    line = cut;
                    cut = "";
                } else if (++rawIndex < rawLines.length) {
                    line = rawLines[rawIndex];
                } else {
                    line = null;
                }
            }

            return lines.toArray(new String[lines.size()]);
        }
    }

}
