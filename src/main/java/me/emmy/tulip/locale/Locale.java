package me.emmy.tulip.locale;

import lombok.Getter;
import me.emmy.tulip.Tulip;
import me.emmy.tulip.util.CC;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public enum Locale {
    NO_PERMISSION("no-permission"),

    SPAWN_SET("spawn.set"),
    SPAWN_TELEPORTED("spawn.teleported"),
    
    ARENA_ALREADY_EXISTS("arena.already-exists"),
    ARENA_DOES_NOT_EXIST("arena.does-not-exist"),
    ARENA_CREATED("arena.created"),
    ARENA_DELETED("arena.deleted"),
    ARENA_CENTER_SET("arena.center-set"),
    ARENA_SAFE_POS_SET("arena.safe-pos-set"),
    ARENA_SPAWN_SET("arena.spawn-set"),
    ARENA_CENTER_NOT_SET("arena.center-not-set"),

    KIT_DEFAULT_DESCRIPTION("kit.default-description"),
    KIT_ALREADY_EXISTS("kit.already-exists"),
    KIT_DOES_NOT_EXIST("kit.does-not-exist"),
    KIT_CREATED("kit.created"),
    KIT_DELETED("kit.deleted"),
    KIT_INVENTORY_GIVEN("kit.inventory-given"),
    KIT_DESCRIPTION_SET("kit.description-set"),
    KIT_ICON_SET("kit.icon-set"),
    KIT_INVENTORY_SET("kit.inventory-set"),
    KIT_TOGGLED("kit.toggled"),
    KIT_DISABLED("kit.disabled"),

    FFA_MATCH_ALREADY_EXISTS("ffa-match.already-exists"),
    FFA_MATCH_DOES_NOT_EXIST("ffa-match.does-not-exist"),
    FFA_MATCH_CREATED("ffa-match.created"),
    FFA_MATCH_DELETED("ffa-match.deleted"),
    FFA_PLAYER_NOT_IN_MATCH("ffa-match.player-not-in-match"),
    FFA_KICKED_PLAYER("ffa-match.kicked-player"),
    FFA_KICKED("ffa-match.kicked"),
    FFA_MAX_PLAYERS_SET("ffa-match.max-players-set"),
    FFA_NOT_IN_MATCH("ffa-match.not-in-match"),
    FFA_ALREADY_IN_MATCH("ffa-match.already-in-match"),
    FFA_ENDERPEARL_COOLDOWN("ffa-match.enderpearl-cooldown"),
    FFA_COOLDOWN_EXPIRED("ffa-match.cooldown-expired");

    private final String path;
    // Rimosso String config perché assumiamo che tutto sia in locale.yml per semplicità
    // Se hai più file, possiamo rimetterlo, ma di solito i messaggi stanno in uno solo.

    Locale(String path) {
        this.path = path;
    }

    /**
     * Recupera la configurazione in modo sicuro
     */
    private FileConfiguration getConfig() {
        return Tulip.getInstance().getConfigService().getLocaleConfig();
    }

    /**
     * Ritorna la stringa tradotta con supporto HEX (1.16+) e Legacy (&)
     */
    public String getString() {
        String message = getConfig().getString(path);
        if (message == null) return "§cMessage not found: " + path;
        return CC.translate(message);
    }

    /**
     * Ritorna una lista di stringhe tradotte
     */
    public List<String> getStringList() {
        return getConfig().getStringList(path).stream()
                .map(CC::translate)
                .collect(Collectors.toList());
    }

    public boolean getBoolean() {
        return getConfig().getBoolean(path);
    }

    public int getInt() {
        return getConfig().getInt(path);
    }

    /**
     * Formatta il messaggio sostituendo i placeholder
     * Esempio: Locale.ARENA_CREATED.format("<arena>", "Desert")
     */
    public String format(Object... replacements) {
        String message = getString();
        for (int i = 0; i < replacements.length; i += 2) {
            if (i + 1 < replacements.length) {
                message = message.replace(replacements[i].toString(), replacements[i+1].toString());
            }
        }
        return message;
    }
}