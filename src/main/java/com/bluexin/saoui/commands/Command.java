package com.bluexin.saoui.commands;

import com.bluexin.saoui.SAOMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.StatCollector;

import java.util.MissingFormatArgumentException;

/**
 * Part of the SAOUI project.
 *
 * @author Bluexin
 */
public class Command {
    private final CommandType type;
    private final String from;
    private final String to;

    private Command(String raw) {
        if (!raw.contains("<") || !raw.contains(">")) throw new MissingFormatArgumentException("<username> not found in \"" + raw + '"');
        this.from = raw.substring(raw.indexOf('<') + 1, raw.indexOf('>')); // TODO: check with team/chat plugins prefixes!
        this.type = CommandType.getCommand(raw.substring(raw.indexOf(CommandType.PREFIX) + CommandType.PREFIX.length(), raw.indexOf(CommandType.SUFFIX)));
        this.to = SAOMod.getName(Minecraft.getMinecraft());
    }

    public Command(CommandType type, String to) {
        this.type = type;
        this.to = to;
        this.from = SAOMod.getName(Minecraft.getMinecraft());
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

    public String getTo() {
        return to;
    }

    public String[] getContent(String data) { // Only used for update atm I think, keeping it for later
        final int index = toString().length() + 1;

        if (index >= data.length()) return new String[0];
        else return data.substring(index).split(" ");
    }

    public String toChat() { // TODO: use this instead of the old methods (everywhere in the mod)
        final String format = I18n.format("commands.message.usage");
        final String cmd = format.substring(0, format.indexOf(' '));

        return cmd + ' ' + this.to + ' ' + this.type.toString() + ' ' + '<' + this.from + '>' + ' ' + StatCollector.translateToLocalFormatted(this.type.key(), this.from);
    }
}
