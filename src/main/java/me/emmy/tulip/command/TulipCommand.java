package me.emmy.tulip.command;

import me.emmy.tulip.Tulip;
import me.emmy.tulip.util.CC;
import me.emmy.tulip.api.command.BaseCommand;
import me.emmy.tulip.api.command.CommandArgs;
import me.emmy.tulip.api.command.annotation.Command;
import org.bukkit.command.CommandSender;

/**
 * @author Emmy
 * @project FFA
 * @date 27/07/2024 - 17:55
 */
public class TulipCommand extends BaseCommand {

    @Command(name = "tulip", inGameOnly = false)
    @Override
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();
        Tulip plugin = Tulip.getInstance();
        
        // Definiamo un colore principale (Azzurro Moderno/Viola) per tutto il core
        String primary = "&#d48cf0"; // Un viola pastello che sta bene con il nome "Tulip"
        String secondary = "&7";
        String dot = "&f▢";

        sender.sendMessage("");
        sender.sendMessage(CC.translate(primary + "&l" + CC.SB_BAR));
        sender.sendMessage(CC.translate(" " + primary + "&lTULIP FFA CORE &8- " + secondary + "Advanced FFA Solution"));
        sender.sendMessage("");
        sender.sendMessage(CC.translate("  " + primary + dot + " &eAuthor: " + secondary + plugin.getDescription().getAuthors().get(0)));
        sender.sendMessage(CC.translate("  " + primary + dot + " &eVersion: " + secondary + plugin.getDescription().getVersion()));
        sender.sendMessage("");
        sender.sendMessage(CC.translate("  " + primary + dot + " &eGithub: " + secondary + "github.com/hmEmmy/Tulip"));
        sender.sendMessage(CC.translate("  " + primary + dot + " &eDiscord: " + secondary + "discord.gg/eT4B65k5E4"));
        sender.sendMessage("");
        sender.sendMessage(CC.translate(primary + "&l" + CC.SB_BAR));
        sender.sendMessage("");
    }
}