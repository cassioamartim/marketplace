package dev.martim.marketplace.api.menu.sound;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Sound;

@Getter
@AllArgsConstructor
public enum MenuSound {

    DONE(Sound.ENTITY_ITEM_PICKUP),
    ERROR(Sound.ENTITY_ITEM_BREAK),
    SUCCESS(Sound.ENTITY_EXPERIENCE_ORB_PICKUP),
    PAGINATED(Sound.UI_BUTTON_CLICK);

    private final Sound sound;
}
