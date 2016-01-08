package com.bluexin.saoui.commands;

import com.bluexin.saoui.SAOMod;
import net.minecraft.client.Minecraft;

import java.util.MissingFormatArgumentException;

/**
 * Part of the SAOUI project.
 *
 * @author Bluexin
 */
public class Command {
    private final CommandType type;
    private final String from;

    private Command(String raw) {
        if (!raw.contains("<") || !raw.contains(">")) throw new MissingFormatArgumentException("<username> not found in \"" + raw + '"');
        from = raw.substring(raw.indexOf('<') + 1, raw.indexOf('>')); // TODO: check with team/chat plugins prefixes!
        type = CommandType.getCommand(raw.substring(raw.indexOf(CommandType.PREFIX) + CommandType.PREFIX.length(), raw.indexOf(CommandType.SUFFIX)));

    }

    public static boolean processCommand(String raw) {
        if (raw.contains(CommandType.PREFIX) && raw.contains(CommandType.SUFFIX)) {
            final Command command;
            try {
                command = new Command(raw);
            } catch (MissingFormatArgumentException e) {
                return false;
            }
            if (command.type != null && !command.from.equals(SAOMod.getName(Minecraft.getMinecraft()))) SAOMod.receiveSAOCommand(Minecraft.getMinecraft(), command);
            return true;
        } else return false;
    }

    public CommandType getType() {
        return type;
    }

    public String getFrom() {
        return from;
    }

    public final String[] getContent(String data) { // Only used for update atm I think, keeping it for later
        final int index = toString().length() + 1;

        if (index >= data.length()) return new String[0];
        else return data.substring(index).split(" ");
    }
}
